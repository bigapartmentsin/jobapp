package com.abln.chat.ui.activities;

import android.os.Bundle;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.base.IMBaseListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;



public class IMBaseActivity extends AppCompatActivity implements IMBaseListener {
    public static final String TAG = IMBaseActivity.class.getSimpleName();

    public static boolean isAppInForeGround = false;
    public static boolean isScreenInForeGround = false;
    public static boolean isChangeScreen = false;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.im_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public Toolbar getToolBar() {
        return mToolbar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null != IMInstance.getInstance()) {
            IMInstance.getInstance().setIMBaseListener(this);
        }
    }

    @Override
    protected void onStart() {
        if (!isAppInForeGround) {
            isAppInForeGround = true;
            isChangeScreen = false;
            onAppStart();
        } else {
            isChangeScreen = true;
        }
        isScreenInForeGround = true;

        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isScreenInForeGround || !isChangeScreen) {
            isAppInForeGround = false;
            onAppPause();
        }
        isScreenInForeGround = false;
    }

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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
