package com.abln.chat.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.base.IMUserPresenceListener;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.adapters.IMChatThreadInfoMemberAdapter;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMNetwork;
import com.abln.chat.utils.IMUtils;
import com.abln.chat.utils.TourGuideHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class IMChatThreadInfoActivity extends IMBaseActivity {
    public static final String TAG = IMChatThreadInfoActivity.class.getSimpleName();

    private AppCompatEditText mTvGroupName;
    private RelativeLayout mRlAddParticipantLayout, mRlHeaderParticipant;
    private TextView mTvParticipantCount, mTextImgUserProfile;
    private ImageView mImgUserProfile, mImgOnlineStatus, mImgAddParticipants;

    private RecyclerView mAddUsersRecyclerView;
    private static IMChatThreadInfoMemberAdapter mIMChatThreadInfoMemberAdapter;


    private IMChatRoomDetail mIMChatRoomDetail = new IMChatRoomDetail();
    private static IMChatUser mLoggedInUser = new IMChatUser();
    private String mChatRoomName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_chat_thread_info);

        setLoggedInUser();
        setBundleData();
        setUpActionBar();
        initViews();
        initControlInitials();
//        showTourGuide();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.im_menu_done, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        IMInstance.getInstance().setUserPresenceListener(mIMUserPresenceListener);
    }

    private void setLoggedInUser() {
        // Setting Logged In IMChatUser
        mLoggedInUser = IMInstance.getInstance().getLoggedInUser();
    }

    private void setBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle && null != bundle.getString(IMConstants.CHAT_ROOM_ID, null)) {
            mIMChatRoomDetail = IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().
                    getChatRoomDetail(bundle.getString(IMConstants.CHAT_ROOM_ID, null));
            mChatRoomName = IMUtils.getEmptyStringIfValueNull(mIMChatRoomDetail.roomName);
        }
    }

    private void setUpActionBar() {
        setUpToolbar();
        getToolBar().setTitle(getResources().getString(R.string.im_info));
    }

    private void initViews() {

        mImgUserProfile = (ImageView) findViewById(R.id.im_iv_chat_thread_icon);
        mTextImgUserProfile = (TextView) findViewById(R.id.im_tv_chat_thread_icon);
        mImgOnlineStatus = (ImageView) findViewById(R.id.im_iv_user_profile_online_bg);
        mImgAddParticipants = (ImageView) findViewById(R.id.im_iv_add_participants);

        mTvGroupName = (AppCompatEditText) findViewById(R.id.im_et_group_name);
        mTvParticipantCount = (TextView) findViewById(R.id.im_tv_number_of_participant);

        mRlAddParticipantLayout = (RelativeLayout) findViewById(R.id.im_rl_add_participant);
        mRlHeaderParticipant = (RelativeLayout) findViewById(R.id.im_ll_header_participant);

        mAddUsersRecyclerView = (RecyclerView) findViewById(R.id.im_rv_participant);

        setGroupName(IMUtils.getEmptyStringIfValueNull(mIMChatRoomDetail.roomName));
        mTvGroupName.setSelection(mTvGroupName.getText().length());

        mTvGroupName.addTextChangedListener(mOnGroupNameChangeListener);
        mTvGroupName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvGroupName.setFocusableInTouchMode(true);
            }
        });

        mImgAddParticipants.setOnClickListener(mOnAddUsersClickListener);

    }

    private void setGroupName(String chatRoomName) {
        if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
            mTvGroupName.setText(chatRoomName);

        } else {
            mTvGroupName.setText(ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId,
                    mIMChatRoomDetail).userName);
        }

    }

    private void initControlInitials() {
        IMInstance.getInstance().fetchUserStateOnPresenceChannel();
        setUserProfileImage();
        setRecipientsListAdapter();
        setAddParticipantLayout();
        showViewBasedOnThreadType();
    }

    private void setUserProfileImage() {
        if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
            mImgUserProfile.setBackgroundDrawable(getResources().getDrawable(R.drawable.im_ic_default_chat_group));

        } else {
            ChatHelper.setUserImage(ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId,
                    mIMChatRoomDetail), mImgUserProfile, mTextImgUserProfile);
        }
    }

    private void setRecipientsListAdapter() {
        mIMChatThreadInfoMemberAdapter = new IMChatThreadInfoMemberAdapter(this, mAddUsersRecyclerView,
                mLoggedInUser, mIMChatRoomDetail);

        DividerItemDecoration verticalDecoration = new DividerItemDecoration(
                mAddUsersRecyclerView.getContext(), DividerItemDecoration.VERTICAL);
        Drawable verticalDivider = ContextCompat.getDrawable(this, R.drawable.im_divider_recycle_view);
        verticalDecoration.setDrawable(verticalDivider);
        mAddUsersRecyclerView.addItemDecoration(verticalDecoration);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mAddUsersRecyclerView.setLayoutManager(linearLayoutManager);
        mAddUsersRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mAddUsersRecyclerView.setAdapter(mIMChatThreadInfoMemberAdapter);

        mIMChatThreadInfoMemberAdapter.setRecipients(ChatHelper.getChatRoomMemberList(mIMChatRoomDetail));
    }

    private void setAddParticipantLayout() {
        if (mIMChatRoomDetail.adminId.equals(mLoggedInUser.userId) ||
                mIMChatRoomDetail.roomType.equals(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            mRlAddParticipantLayout.setVisibility(View.VISIBLE);

        } else {
            mRlAddParticipantLayout.setVisibility(View.GONE);
        }
    }

    private void showViewBasedOnThreadType() {
        if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
            mTvGroupName.setEnabled(true);
            mTvParticipantCount.setVisibility(View.VISIBLE);
            mRlHeaderParticipant.setVisibility(View.VISIBLE);
            mAddUsersRecyclerView.setVisibility(View.VISIBLE);

        } else {
            mTvGroupName.setEnabled(false);
            mRlHeaderParticipant.setVisibility(View.GONE);
            mTvParticipantCount.setVisibility(View.GONE);
            mAddUsersRecyclerView.setVisibility(View.GONE);
        }
    }

    public void updateParticipantCount(int userCount) {
        mTvParticipantCount.setText(String.valueOf(userCount));
    }

    private boolean isChatThreadInfoUpdated() {
        boolean bool = true;
        ArrayList<String> newChatRoomMembersList = mIMChatThreadInfoMemberAdapter.getAllRecipientsIdList();
        List<String> existingChatRoomMembersList = ChatHelper.getChatRoomMemberIdList(mIMChatRoomDetail);

        if (null != mIMChatRoomDetail.roomName
                && mIMChatRoomDetail.roomName.equalsIgnoreCase(mChatRoomName)
                && newChatRoomMembersList.containsAll(existingChatRoomMembersList)
                && existingChatRoomMembersList.containsAll(newChatRoomMembersList)) {
            bool = false;
        }

        return bool;
    }

    private void setOnlineStatusInThreadInfo(String userId, Boolean isOnline) {
        if (null != mIMChatRoomDetail.roomType && mIMChatRoomDetail.roomType.
                equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE) &&
                ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId,
                        mIMChatRoomDetail).userId.equals(userId)) {

            if (isOnline) {
                mImgOnlineStatus.setVisibility(View.VISIBLE);

            } else {
                mImgOnlineStatus.setVisibility(View.GONE);
            }
        }
    }

    private IMUserPresenceListener mIMUserPresenceListener = new IMUserPresenceListener() {
        @Override
        public void onUserStatusReceived(final String userId, final Boolean isOnline) {
            IMChatThreadInfoActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mIMChatThreadInfoMemberAdapter.updateOnlineStatus(userId, isOnline);
                    setOnlineStatusInThreadInfo(userId, isOnline);


                }
            });
        }
    };

    private View.OnClickListener mOnAddUsersClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ChatHelper.openChatUserListActivityToAddUser(IMChatThreadInfoActivity.this,
                    mIMChatRoomDetail, mIMChatThreadInfoMemberAdapter.getAllRecipientsIdList(),
                    mIMChatRoomDetail.roomMetaData.get("contextId"), mIMChatRoomDetail.roomMetaData.get("contextName"));
        }
    };

    private TextWatcher mOnGroupNameChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mChatRoomName = s.toString();
            } else {
                mChatRoomName = "";
            }
        }
    };

    public static class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (IMNetwork.isConnected(context)) {
                // need not to do anything, online call back will get from pubNub
            } else if (null != mIMChatThreadInfoMemberAdapter) {
                mIMChatThreadInfoMemberAdapter.updateOnlineStatus(mLoggedInUser.userId, false);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();

        } else if (item.getItemId() == R.id.im_action_done) {
            if (isChatThreadInfoUpdated()) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(IMConstants.ADD_USER_ID_LIST,
                        mIMChatThreadInfoMemberAdapter.getAllRecipientsIdList());
                intent.putExtra(IMConstants.CHAT_ROOM_NAME, mChatRoomName);
                setResult(IMConstants.CHAT_INFO_REQUEST_CODE, intent);
            }
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != data && requestCode == IMConstants.ADD_USER_REQUEST_CODE) {
            ArrayList<IMChatUser> chatUserList = IMInstance.getInstance().getIMDatabase().getIMChatUserDao()
                    .getChatUserListByUserIds(data.getStringArrayListExtra(IMConstants.ADD_USER_ID_LIST));

            chatUserList.add(IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                    getChatUserById(mIMChatRoomDetail.adminId));
            mIMChatThreadInfoMemberAdapter.setRecipients(chatUserList);
        }
    }

    public void showTourGuide() {
        TourGuideHelper tourGuideHelper = new TourGuideHelper(this);
        tourGuideHelper.showView(mImgAddParticipants, "Add User", "You can selects multiple users to chat with !", Gravity.BOTTOM | Gravity.LEFT);
    }
}
