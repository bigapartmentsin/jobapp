package com.abln.chat.ui.activities;

import android.os.Bundle;
import android.view.MenuItem;

import com.abln.chat.R;
import com.abln.chat.ui.adapters.ChatMultiMediaSliderAdapter;
import com.abln.chat.ui.helper.ChatMultiMediaHelper;
import com.abln.chat.utils.IMConstants;

import androidx.viewpager.widget.ViewPager;

public class IMChatMultiMediaSlideActivity extends IMBaseActivity {

    private String mRoomId, mMsgId;
    private ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_chat_multimedia_slide);
        setUpActionBar();
        setBundleData();
        initViews();
        initControlInitials();
    }

    private void setBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mRoomId = bundle.getString(IMConstants.CHAT_ROOM_ID, null);
            mMsgId = bundle.getString(IMConstants.CHAT_MESSAGE_ID);
        }
    }


    private void initViews() {
        mViewPager = (ViewPager) findViewById(R.id.im_vp_image_slider);
    }

    private void setUpActionBar() {
        setUpToolbar();
        getToolBar().setBackgroundColor(getResources().getColor(R.color.im_transparent));
    }

    private void initControlInitials() {
        ChatMultiMediaSliderAdapter mPagerAdapter = new ChatMultiMediaSliderAdapter(this);
        mViewPager.setAdapter(mPagerAdapter);

        mPagerAdapter.setMultimediaList(ChatMultiMediaHelper.getMultimediaFilesForParticularRoom(mRoomId));
        mViewPager.setCurrentItem(mPagerAdapter.getCurrentPos(mMsgId));
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
