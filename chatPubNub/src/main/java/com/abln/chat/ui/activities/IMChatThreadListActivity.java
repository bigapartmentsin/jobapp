package com.abln.chat.ui.activities;

import android.app.Service;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.adapters.IMChatThreadRecyclerAdapter;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.IMSoftKeyboard;
import com.abln.chat.utils.IMUIUtils;
import com.abln.chat.utils.TourGuideHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


public class IMChatThreadListActivity extends IMBaseActivity {
    public static final String TAG = IMChatThreadListActivity.class.getSimpleName();

    private RecyclerView mChatHistoryRecyclerView;
    private IMChatThreadRecyclerAdapter mIMChatThreadRecyclerAdapter;
    private TextView mTvNoChatHistory;
    private String mContextId;
    private String mContextName;
    private FloatingActionButton mLaunchNewChatThread;
    private IMSoftKeyboard mIMSoftKeyboard;
    private RelativeLayout activityRootView;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private AppCompatEditText mEtSearchChatMessage;

    private IMChatUser mLoggedInUser = new IMChatUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_chat_thread_list);

        setLoggedInUser();
        setBundleData();
        initViews();
        initControlInitials();
        attachKeyboardListeners();

    }

    private void setLoggedInUser() {
        // Setting Logged In IMChatUser
        mLoggedInUser = IMInstance.getInstance().getLoggedInUser();

        System.out.println("MloggedInUser--"+mLoggedInUser);
    }

    private void setBundleData() {
        Bundle bundle = getIntent().getExtras();
        mContextId = getIntent().getStringExtra("Context_Id");
        mContextName = getIntent().getStringExtra("Context_Name");




        System.out.println("MloggedInUserConID--"+mContextId);
        System.out.println("MloggedInUserName--"+mContextName);

        if (null != bundle) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isClicked = true;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateChatHistory();
    }

    private void initViews() {
        activityRootView = (RelativeLayout) findViewById(R.id.im_rl_activity_chat_history);
        mChatHistoryRecyclerView = (RecyclerView) findViewById(R.id.im_rv_chat_history);
        mTvNoChatHistory = (TextView) findViewById(R.id.im_tv_no_chat_history);
        mLaunchNewChatThread = (FloatingActionButton) findViewById(R.id.im_fl_start_new_chat_thread);
        mLaunchNewChatThread.setImageDrawable(getResources().getDrawable(R.drawable.im_ic_add_fab));
        mLaunchNewChatThread.setOnClickListener(onNewChatThreadClickListener);
    }

    private void initControlInitials() {
        setUpActionBar();
        setChatHistoryAdapter();
        showChatHistory();
    }

    private void setUpActionBar() {
        setUpToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.im_chat_thread_list_header));
    }

    public void attachKeyboardListeners() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        mIMSoftKeyboard = new IMSoftKeyboard(activityRootView, inputMethodManager);
        mIMSoftKeyboard.setSoftKeyboardCallback(softKeyboardChanged);
    }

    IMSoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new IMSoftKeyboard.SoftKeyboardChanged() {
        @Override
        public void onSoftKeyboardHide() {
            IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mLaunchNewChatThread.setVisibility(View.VISIBLE);
                }
            });
        }

        @Override
        public void onSoftKeyboardShow() {
            IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    mLaunchNewChatThread.setVisibility(View.GONE);
                }
            });
        }
    };


    private void setChatHistoryAdapter() {
        mIMChatThreadRecyclerAdapter = new IMChatThreadRecyclerAdapter(this, mChatHistoryRecyclerView, mLoggedInUser);
        mChatHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatHistoryRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mChatHistoryRecyclerView.setAdapter(mIMChatThreadRecyclerAdapter);
    }

    private void showChatHistory() {
        ArrayList<ChatThreadItem> chatThreadItemsList = getChatHistoryList();
        if (chatThreadItemsList != null && chatThreadItemsList.size() > 0) {
            hideNoChatHistoryMsg();
            mIMChatThreadRecyclerAdapter.addChatHistoryList(getOrderedChatHistoryListByTime(chatThreadItemsList), false);
            setUserStateForAllChatThreads(chatThreadItemsList);

        } else {
            showNoChatHistoryMsg();
        }
    }

    private void updateChatHistory() {
        ArrayList<ChatThreadItem> chatThreadItemsList = getChatHistoryList();
        if (chatThreadItemsList.size() > 0) {
            hideNoChatHistoryMsg();

        } else {
            return;
        }

        if (mIMChatThreadRecyclerAdapter.mSearchFilterPattern.length() < 3) {
            mIMChatThreadRecyclerAdapter.addChatHistoryList(getOrderedChatHistoryListByTime(chatThreadItemsList), false);

        } else {
            // updating the chat history for the messages received while user was in the search mode
            mIMChatThreadRecyclerAdapter.mChatThreadList = getOrderedChatHistoryListByTime(chatThreadItemsList);

            // updating the chat history screen for searched chat threads
            mIMChatThreadRecyclerAdapter.notifyDataSetChanged();
        }

        setUserStateForAllChatThreads(chatThreadItemsList);
    }

    private void setUserStateForAllChatThreads(ArrayList<ChatThreadItem> chatThreadItemsList) {
        ArrayList<String> chatRoomIdList = new ArrayList<>();
        for (ChatThreadItem chatThreadItem : chatThreadItemsList) {
            if (null != chatThreadItem.chatRoomDetail) {
                chatRoomIdList.add(chatThreadItem.chatRoomDetail.roomId);
            }
        }

        if (chatRoomIdList.size() > 0) {
            IMInstance.getInstance().fetchUserStateForChatRooms(chatRoomIdList);
        }
    }

    private ArrayList<ChatThreadItem> getChatHistoryList() {
        ArrayList<IMChatRoomDetail> chatRoomList = new ArrayList<>(IMInstance.getInstance().getIMDatabase().
                getIMChatRoomDetailDao().getAllChatRoomDetail());

        ArrayList<ChatThreadItem> chatThreadItemList = new ArrayList<>();
        for (IMChatRoomDetail chatRoomDetail : chatRoomList) {
            if (!chatRoomDetail.isChatRoomDeleted) {
                ChatThreadItem chatThreadItem = new ChatThreadItem();
                chatThreadItem.type = IMChatThreadRecyclerAdapter.CHILD;
                chatThreadItem.headerName = "";
                chatThreadItem.typingStatus = "";
                chatThreadItem.chatRoomDetail = chatRoomDetail;
                chatThreadItem.chatMessage = IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().
                        getLatestChatMessageByRoomId(chatRoomDetail.roomId);
                chatThreadItem.chatRoomDetail.roomName = ChatHelper.getChatRoomName(mLoggedInUser, chatRoomDetail);

                if (null != mContextId && mContextId.length() > 0) {
                    if (mContextId.equalsIgnoreCase(chatRoomDetail.roomMetaData.get("contextId"))) {
                        chatThreadItemList.add(chatThreadItem);
                    }
                } else {
                    chatThreadItemList.add(chatThreadItem);
                }
            }
        }

        return chatThreadItemList;
    }

    private ArrayList<ChatThreadItem> getOrderedChatHistoryListByTime(ArrayList<ChatThreadItem> chatHistoryList) {
        Collections.sort(chatHistoryList, new Comparator<ChatThreadItem>() {
            @Override
            public int compare(ChatThreadItem item1, ChatThreadItem item2) {

                if (null != item1.chatMessage && null != item1.chatRoomDetail) {
                    item1.chatRoomDetail.timeToken = item1.chatMessage.timeToken;
                }
                if (null != item2.chatMessage && null != item2.chatRoomDetail) {
                    item2.chatRoomDetail.timeToken = item2.chatMessage.timeToken;
                }

                if (null != item1.chatRoomDetail && null != item2.chatRoomDetail) {
                    return Long.valueOf(item2.chatRoomDetail.timeToken).compareTo(item1.chatRoomDetail.timeToken);
                } else {
                    return -1;
                }
            }
        });
        return chatHistoryList;
    }

    public void hideNoChatHistoryMsg() {
        mTvNoChatHistory.setVisibility(View.GONE);
    }

    public void showNoChatHistoryMsg() {
        mTvNoChatHistory.setVisibility(View.VISIBLE);
    }

    private MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            clearFilters();
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_grey_boulder)));
            return false;
        }
    };

    private void clearFilters() {
        mEtSearchChatMessage.setText("");
        if (null != mIMChatThreadRecyclerAdapter) {
            mIMChatThreadRecyclerAdapter.getFilter().filter("");
        }
    }

    private TextWatcher mOnTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            if (null != mIMChatThreadRecyclerAdapter) {
                mIMChatThreadRecyclerAdapter.getFilter().filter(charSequence);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    private boolean isClicked = true;
    private View.OnClickListener onNewChatThreadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isClicked)
                return;

            isClicked = false;
            ChatHelper.openChatUserListActivity(IMChatThreadListActivity.this, mContextName, mContextId);
        }

    };

    @Override
    public void onDeviceTimeUpdated() {
        super.onDeviceTimeUpdated();
        IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateChatHistory();
            }
        });
    }

    @Override
    public void onChatMessageReceived(IMChatMessage chatMessage) {
        super.onChatMessageReceived(chatMessage);
        IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateChatHistory();
            }
        });
    }

    @Override
    public void onChatRoomDetailReceived(IMChatRoomDetail chatRoomDetail) {
        super.onChatRoomDetailReceived(chatRoomDetail);
        IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateChatHistory();
            }
        });
    }

    @Override
    public void onChatRoomNameUpdateMessageReceived(IMChatRoomDetailUpdate chatRoomDetailUpdate) {
        super.onChatRoomNameUpdateMessageReceived(chatRoomDetailUpdate);
        IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateChatHistory();
            }
        });
    }

    @Override
    public void onChatMessageStatusReceived(IMChatMessageStatus chatMessageStatus) {
        super.onChatMessageStatusReceived(chatMessageStatus);
        IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updateChatHistory();
            }
        });
    }

    @Override
    public void onChatTypingStatusReceived(final String roomId, final String userId, final Boolean isTyping) {
        super.onChatTypingStatusReceived(roomId, userId, isTyping);
        IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mIMChatThreadRecyclerAdapter.updateTypingStatus(roomId, userId, isTyping);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.im_menu_chat_search, menu);
        mSearchAction = menu.findItem(R.id.im_search);
//        showTourGuide();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackNavigation();
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
            return true;

        } else if (item.getItemId() == R.id.im_search) {
            handleMenuSearch();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void handleMenuSearch() {
        ActionBar actionBar = getSupportActionBar();
        if (isSearchOpened) {


            actionBar.setDisplayShowCustomEnabled(false);
            actionBar.setDisplayShowTitleEnabled(true);
            hideSoftKeyBoard();
            clearFilters();
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.im_ic_search));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
            isSearchOpened = false;



        } else {
            actionBar.setDisplayShowCustomEnabled(true);
            View view = LayoutInflater.from(actionBar.getThemedContext()).inflate(R.layout.im_item_chat_search_bar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            actionBar.setCustomView(view, params);
            actionBar.setDisplayShowTitleEnabled(false);
            mEtSearchChatMessage = (AppCompatEditText) actionBar.getCustomView().findViewById(R.id.im_ed_search_chat);
            mEtSearchChatMessage.addTextChangedListener(mOnTextChangedListener);
            mEtSearchChatMessage.requestFocus();
            IMUIUtils.showSoftKeyboardForWindow(this);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.im_ic_close_btn));
            mSearchAction.setOnMenuItemClickListener(mOnMenuItemClickListener);
            isSearchOpened = true;
        }
    }

    private void hideSoftKeyBoard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            IMUIUtils.hideSoftKeyboard(this, view);
        }
    }

    private void handleBackNavigation() {
        if (isSearchOpened) {
            handleMenuSearch();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        handleBackNavigation();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIMSoftKeyboard.unRegisterSoftKeyboardCallback();
    }


    public static class ChatThreadItem {
        public int type;
        public String headerName;
        public String typingStatus;
        public IMChatRoomDetail chatRoomDetail;
        public IMChatMessage chatMessage;
    }

    public void showTourGuide() {
        TourGuideHelper tourGuideHelper = new TourGuideHelper(this);
        tourGuideHelper.showView(mLaunchNewChatThread, "New Chat", "You can start a chat by tapping on the plus icon !", Gravity.TOP | Gravity.LEFT);
    }
}