package com.abln.chat;


import android.app.Service;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abln.chat.core.base.IMBaseListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.activities.IMChatActivity;
import com.abln.chat.ui.activities.IMChatThreadListActivity;
import com.abln.chat.ui.adapters.IMChatThreadRecyclerAdapter;
import com.abln.chat.ui.adapters.JobDataAdapter;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.AccountOne;
import com.abln.chat.utils.AdModule;
import com.abln.chat.utils.AdapterInvite;
import com.abln.chat.utils.Admod;
import com.abln.chat.utils.BaseResponse;
import com.abln.chat.utils.BaseView;
import com.abln.chat.utils.GlobalSingleCallback;
import com.abln.chat.utils.IMSoftKeyboard;
import com.abln.chat.utils.ModChat;
import com.abln.chat.utils.Models;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

import static com.abln.chat.ui.activities.IMBaseActivity.isAppInForeGround;
import static com.abln.chat.ui.activities.IMBaseActivity.isChangeScreen;
import static com.abln.chat.ui.activities.IMBaseActivity.isScreenInForeGround;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsViewFragment extends Fragment implements IMBaseListener, BaseView ,AdapterInvite.clickHandler{


    protected CompositeDisposable compositeDisposable;
    protected com.abln.chat.utils.Handler apiService;
    private static final String TAG = "ChatsViewFragmet";
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

    private Context mContext;

    private IMChatUser mLoggedInUser = new IMChatUser();

    private boolean firstVisit;


    public ChatsViewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        firstVisit = true;
        return inflater.inflate(R.layout.fragment_chats_view, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity();
        apiService = com.abln.chat.utils.Service.getApiService();
        compositeDisposable = new CompositeDisposable();
        setLoggedInUser();
        setBundleData();
        initViews(view);
        initControlInitials();
        attachKeyboardListeners();


        getInvites(mLoggedInUser.userId);
        recyclerViewinvit.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.HORIZONTAL, false));


    }









    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    private void setLoggedInUser() {
        // Setting Logged In IMChatUser
        mLoggedInUser = IMInstance.getInstance().getLoggedInUser();
    }

    private void setBundleData() {
        Bundle bundle = getActivity().getIntent().getExtras();
        mContextId = getActivity().getIntent().getStringExtra("Context_Id");
        mContextName = getActivity().getIntent().getStringExtra("Context_Name");

        if (null != bundle) {

        }
    }


    private void setDataRetrival(String key){

        //  first om nam shivaya



    }

private RelativeLayout r1;
    private void initViews(View view) {

        recyclerViewinvit = (RecyclerView) view.findViewById(R.id.recycler_stories) ;
        activityRootView = (RelativeLayout) view.findViewById(R.id.im_rl_activity_chat_history);
        mChatHistoryRecyclerView = (RecyclerView) view.findViewById(R.id.im_rv_chat_history);
        mTvNoChatHistory = (TextView) view.findViewById(R.id.im_tv_no_chat_history);
        mLaunchNewChatThread = (FloatingActionButton) view.findViewById(R.id.im_fl_start_new_chat_thread);
        mLaunchNewChatThread.setImageDrawable(getResources().getDrawable(R.drawable.im_ic_add_fab));
        mLaunchNewChatThread.setOnClickListener(onNewChatThreadClickListener);
        r1 = (RelativeLayout) view.findViewById(R.id.r1);
    }

    private void initControlInitials() {
        setUpActionBar();

        //todo commenting the two variable parts ;
        setChatHistoryAdapter();
        showChatHistory();
    }

    private void setUpActionBar() {
//        setUpToolbar();
//        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setTitle(getResources().getString(R.string.im_chat_thread_list_header));
    }

    public void attachKeyboardListeners() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        mIMSoftKeyboard = new IMSoftKeyboard(activityRootView, inputMethodManager);
        mIMSoftKeyboard.setSoftKeyboardCallback(softKeyboardChanged);
    }

    IMSoftKeyboard.SoftKeyboardChanged softKeyboardChanged = new IMSoftKeyboard.SoftKeyboardChanged() {
        @Override
        public void onSoftKeyboardHide() {
//            this.runOnUiThread(new Runnable() {
//                public void run() {
//                    mLaunchNewChatThread.setVisibility(View.VISIBLE);
//                }
//            });




        }

        @Override
        public void onSoftKeyboardShow() {
//            IMChatThreadListActivity.this.runOnUiThread(new Runnable() {
//                public void run() {
//                    mLaunchNewChatThread.setVisibility(View.GONE);
//                }
//            });




        }
    };


    private void setChatHistoryAdapter() {
        mIMChatThreadRecyclerAdapter = new IMChatThreadRecyclerAdapter(mContext, mChatHistoryRecyclerView, mLoggedInUser);
        mChatHistoryRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mChatHistoryRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mChatHistoryRecyclerView.setAdapter(mIMChatThreadRecyclerAdapter);
    }

    private void showChatHistory() {
        ArrayList<IMChatThreadListActivity.ChatThreadItem> chatThreadItemsList = getChatHistoryList();
        if (chatThreadItemsList != null && chatThreadItemsList.size() > 0) {
            hideNoChatHistoryMsg();
            mIMChatThreadRecyclerAdapter.addChatHistoryList(getOrderedChatHistoryListByTime(chatThreadItemsList), false);
            setUserStateForAllChatThreads(chatThreadItemsList);

        } else {
            showNoChatHistoryMsg();
        }
    }

    private void updateChatHistory() {
        ArrayList<IMChatThreadListActivity.ChatThreadItem> chatThreadItemsList = getChatHistoryList();
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

    private void setUserStateForAllChatThreads(ArrayList<IMChatThreadListActivity.ChatThreadItem> chatThreadItemsList) {
        ArrayList<String> chatRoomIdList = new ArrayList<>();
        for (IMChatThreadListActivity.ChatThreadItem chatThreadItem : chatThreadItemsList) {
            if (null != chatThreadItem.chatRoomDetail) {
                chatRoomIdList.add(chatThreadItem.chatRoomDetail.roomId);
            }
        }

        if (chatRoomIdList.size() > 0) {
            IMInstance.getInstance().fetchUserStateForChatRooms(chatRoomIdList);
        }
    }

    private ArrayList<IMChatThreadListActivity.ChatThreadItem> getChatHistoryList() {
        ArrayList<IMChatRoomDetail> chatRoomList = new ArrayList<>(IMInstance.getInstance().getIMDatabase().
                getIMChatRoomDetailDao().getAllChatRoomDetail());

        ArrayList<IMChatThreadListActivity.ChatThreadItem> chatThreadItemList = new ArrayList<>();
        for (IMChatRoomDetail chatRoomDetail : chatRoomList) {
            if (!chatRoomDetail.isChatRoomDeleted) {
                IMChatThreadListActivity.ChatThreadItem chatThreadItem = new IMChatThreadListActivity.ChatThreadItem();
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

    private ArrayList<IMChatThreadListActivity.ChatThreadItem> getOrderedChatHistoryListByTime(ArrayList<IMChatThreadListActivity.ChatThreadItem> chatHistoryList) {
        Collections.sort(chatHistoryList, new Comparator<IMChatThreadListActivity.ChatThreadItem>() {
            @Override
            public int compare(IMChatThreadListActivity.ChatThreadItem item1, IMChatThreadListActivity.ChatThreadItem item2) {

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
            //getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_grey_boulder)));
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

    @Override
    public void onResume() {
        super.onResume();
        if (null != IMInstance.getInstance()) {
            IMInstance.getInstance().setIMBaseListener(this);
        }
        if (firstVisit) {

            updateChatHistory();
            firstVisit = false;
        }


        getInvites(mLoggedInUser.userId);
    }

    @Override
    public void onStart() {
        if (!isAppInForeGround) {
            isAppInForeGround = true;
            isChangeScreen = false;
            onAppStart();
        } else {
            isChangeScreen = true;
        }
        isScreenInForeGround = true;
        super.onStart();
        Log.d(TAG,"onstart");
        updateChatHistory();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!isScreenInForeGround || !isChangeScreen) {
            isAppInForeGround = false;
            onAppPause();
        }
        isScreenInForeGround = false;
        Log.d(TAG,"onstop");
    }

    private boolean isClicked = true;
    private View.OnClickListener onNewChatThreadClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            if (!isClicked)
//                return;

            isClicked = false;
            ChatHelper.openChatUserListActivity(mContext, mContextName, mContextId);
        }

    };

    private void onAppStart() {
        if (null != IMInstance.getInstance().getIMConfiguration()) {
            IMInstance.getInstance().subscribeToPresenceChannel();
        }
    }



    private void onAppPause() {
        if (null != IMInstance.getInstance().getIMConfiguration()) {
            IMInstance.getInstance().unSubscribeFromPresenceChannel();
        }
    }


    @Override
    public void onDeviceTimeUpdated() {

    }

    @Override
    public void onChatMessageReceived(IMChatMessage chatMessage) {
        if (null != IMInstance.getInstance().getIMConfiguration()) {

        }
    }

    @Override
    public void onChatRoomDetailReceived(IMChatRoomDetail chatRoomDetail) {
        if (null != IMInstance.getInstance().getIMConfiguration()) {

        }
    }

    @Override
    public void onChatRoomNameUpdateMessageReceived(IMChatRoomDetailUpdate chatRoomDetailUpdate) {

    }

    @Override
    public void onChatMessageStatusReceived(IMChatMessageStatus chatMessageStatus) {

    }

    @Override
    public void onChatTypingStatusReceived(String roomId, String userId, Boolean isTyping) {

    }




    /// thats is the main data source to handle the informaiton
    RecyclerView recyclerViewinvit;
AdapterInvite inboxitem;
    private void getInvites(String key ){
        AccountOne accountOne = new AccountOne();
      //  accountOne.candidate_id = "c36934b50f187e72956d57bedf6e5aa2";

        accountOne.candidate_id = key;

        compositeDisposable.add(apiService.getusers(accountOne)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AdModule>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        if (baseResponse.statuscode == 1) {
                            r1.setVisibility(View.VISIBLE);
                            AdModule data_list = (AdModule) baseResponse.data;
                            ArrayList<Admod>  final_list = data_list.invites;
                            inboxitem = new AdapterInvite(getActivity(), final_list,ChatsViewFragment.this);
                            recyclerViewinvit.setAdapter(inboxitem);




                        } else if (baseResponse.statuscode == 0) {


                            r1.setVisibility(View.GONE);






                        }




                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));



    }


    @Override
    public void showProgress(String text) {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showInternetError() {

    }

    @Override
    public void showErrorDialog(String title, String description, String positiveBtn) {

    }

    @Override
    public void datarefresh() {
        getInvites(mLoggedInUser.userId);
    }
}
