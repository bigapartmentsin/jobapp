package com.abln.chat.ui.activities;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.base.IMUserPresenceListener;
import com.abln.chat.ui.adapters.IMChatUserListAdapter;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMUIUtils;

import java.util.ArrayList;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class IMChatUserListActivity extends IMBaseActivity {
    public static final String TAG = IMChatUserListActivity.class.getSimpleName();

    private RecyclerView mUserListRecyclerView;
    private RecyclerView mSelectedUserListRecyclerView;
    private View mSelectedUserListDivider;
    private AppCompatEditText mEtSearchContact;
    private MenuItem mSearchAction, mDoneAction;
    private IMChatUserListAdapter mIMChatUserListAdapter;

    private ArrayList<String> mExistingUserIdsUserIds;
    private String mChatRoomType = null;
    private boolean mIsChatForwarded = false;
    private boolean isSearchOpened = false;
    private String mContextName;
    private String mContextId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_chat_user_list);
        setBundleData();
        initViews();
        setUpActionBar();
        setUsers();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IMInstance.getInstance().setUserPresenceListener(mIMUserPresenceListener);
    }

    private void setBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mChatRoomType = bundle.getString(IMConstants.CHAT_ROOM_TYPE, null);
            mExistingUserIdsUserIds = bundle.getStringArrayList(IMConstants.EXITING_USERS_ID_LIST);
            mIsChatForwarded = bundle.getBoolean(IMConstants.IS_CHAT_FORWARDED, false);
            mContextName = bundle.getString(IMConstants.CHAT_CONTEXT_NAME);
            mContextId = bundle.getString(IMConstants.CHAT_CONTEXT_ID);
        }
    }

    private void initViews() {
        mUserListRecyclerView = (RecyclerView) findViewById(R.id.im_rv_user_list);
        mSelectedUserListRecyclerView = (RecyclerView) findViewById(R.id.im_rv_selected_user_list);
        mSelectedUserListDivider = findViewById(R.id.im_view_divider1);
    }

    private void setUpActionBar() {
        setUpToolbar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.im_contacts));
    }

    private void setUsers() {
        IMInstance.getInstance().fetchUserStateOnPresenceChannel();
        setUserListAdapter();
    }

    private void setUserListAdapter() {
        mIMChatUserListAdapter = new IMChatUserListAdapter(this, mSelectedUserListRecyclerView, mExistingUserIdsUserIds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mUserListRecyclerView.setLayoutManager(linearLayoutManager);
        mUserListRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mUserListRecyclerView.setAdapter(mIMChatUserListAdapter);
    }

    private TextWatcher mOnTextChangedListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (null != mIMChatUserListAdapter) {
                mIMChatUserListAdapter.getFilter().filter(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


    //TODO make sure you get the activity .

    private void sendUserListData() {
        ArrayList<String> userIdList = getSelectedUserIdList();
        if (isAnyNewUserAdded(userIdList)) {
            if (null == mChatRoomType || mChatRoomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
                if (mContextId != null && mContextName != null)

                    ChatHelper.doLaunchNewChatThread(this, userIdList, mContextName, mContextId);
                else
                    ChatHelper.doLaunchNewChatThread(this, userIdList);

                closeChatUserListWindow();

            } else if (mIsChatForwarded || mChatRoomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
                setResultForAddUsers(userIdList);
            }

        } else {
            closeChatUserListWindow();
        }
    }

    private ArrayList<String> getSelectedUserIdList() {
        ArrayList<String> userIdList = new ArrayList<>();
        if (null != mIMChatUserListAdapter) {
            for (int i = 0; i < mIMChatUserListAdapter.mUserList.size(); i++) {
                if (mIMChatUserListAdapter.mUserList.get(i).isSelected) {
                    userIdList.add(mIMChatUserListAdapter.mUserList.get(i).chatUser.userId);
                }
            }
        }




        return userIdList;
    }

    private boolean isAnyNewUserAdded(ArrayList<String> userIdList) {
        boolean bool;
        if (userIdList.size() == 0) {
            bool = false;

        } else if (null != mExistingUserIdsUserIds && userIdList.size() == mExistingUserIdsUserIds.size() && userIdList.containsAll(mExistingUserIdsUserIds)) {
            bool = false;

        } else {
            bool = true;
        }

        return bool;
    }

    private void setResultForAddUsers(ArrayList<String> userIdList) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IMConstants.ADD_USER_ID_LIST, userIdList);
        intent.putExtras(bundle);
        setResult(IMConstants.ADD_USER_REQUEST_CODE, intent);
        closeChatUserListWindow();
    }

    public void showOrHideSelectedUserDivider(boolean bool) {
        if (bool) {
            mSelectedUserListDivider.setVisibility(View.VISIBLE);
        } else {
            mSelectedUserListDivider.setVisibility(View.GONE);
        }
    }

    public void showOrHideMenuDoneButton(boolean bool) {
        if (null != mDoneAction) {
            mDoneAction.setVisible(bool);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.im_menu_chat_search, menu);
        mSearchAction = menu.findItem(R.id.im_search);
        mDoneAction = menu.findItem(R.id.im_action_done);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackNavigation();
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
            return true;

        } else if (item.getItemId() == R.id.im_search) {
            handleMenuSearch();
            return true;

        } else if (item.getItemId() == R.id.im_action_done) {
            hideSoftKeyBoard();
            sendUserListData();
        }

        return super.onOptionsItemSelected(item);
    }

    private void handleMenuSearch() {
        if (isSearchOpened) {
            hideSoftKeyBoard();
            getSupportActionBar().setDisplayShowCustomEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            clearFilters();
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.im_ic_search));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
            isSearchOpened = false;

        } else {
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            View view = LayoutInflater.from(getSupportActionBar().getThemedContext()).inflate(R.layout.im_item_chat_search_bar, null);
            ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
            getSupportActionBar().setCustomView(view, params);

            mEtSearchContact = (AppCompatEditText) getSupportActionBar().getCustomView().findViewById(R.id.im_ed_search_chat);
            mEtSearchContact.addTextChangedListener(mOnTextChangedListener);
            mEtSearchContact.requestFocus();
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

    private MenuItem.OnMenuItemClickListener mOnMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            clearFilters();
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
            return false;
        }
    };

    private void clearFilters() {
        mEtSearchContact.setText("");
        if (null != mIMChatUserListAdapter) {
            mIMChatUserListAdapter.getFilter().filter("");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        handleBackNavigation();
    }

    private void handleBackNavigation() {
        if (isSearchOpened) {
            handleMenuSearch();

        } else {
            closeChatUserListWindow();
        }
    }

    private void closeChatUserListWindow() {
        finish();
    }

    private IMUserPresenceListener mIMUserPresenceListener = new IMUserPresenceListener() {
        @Override
        public void onUserStatusReceived(final String userId, final Boolean isOnline) {
            IMChatUserListActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIMChatUserListAdapter.updateOnlineStatus(userId, isOnline);
                }
            });
        }
    };

}
