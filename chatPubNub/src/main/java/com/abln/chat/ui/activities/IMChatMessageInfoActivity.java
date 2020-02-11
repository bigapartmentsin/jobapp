package com.abln.chat.ui.activities;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Video.Thumbnails;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.base.IMUserPresenceListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.adapters.IMChatMessageInfoAdapter;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.ui.helper.ChatMultiMediaHelper;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMUtils;

import java.io.File;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class IMChatMessageInfoActivity extends IMBaseActivity {
    public static final String TAG = IMChatMessageInfoActivity.class.getSimpleName();

    private View mSenderTextMsg;
    private View mSenderImageMsg;
    private View mSenderVideoMsg;

    private RecyclerView mChatMessageInfoRecyclerView;

    private String mChatMsgId = "";

    private IMChatMessage mIMChatMessage = null;
    private IMChatUser mLoggedInUser = new IMChatUser();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_chat_msg_info);
        setLoggedInUser();
        setBundleData();
        initViews();
        setUpActionBar();
        setChatMessageInfo();
        setChatMessageInfoAdapter();
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
        if (null != bundle) {
            mChatMsgId = bundle.getString(IMConstants.CHAT_MESSAGE_ID, "");
        }
    }

    private void initViews() {
        mSenderTextMsg = findViewById(R.id.im_include_sender_text_msg);
        mSenderImageMsg = findViewById(R.id.im_include_sender_image_msg);
        mSenderVideoMsg = findViewById(R.id.im_include_sender_video_msg);
        mChatMessageInfoRecyclerView = (RecyclerView) findViewById(R.id.im_rv_chat_msg_info);
    }

    private void setUpActionBar() {
        setUpToolbar();
        getToolBar().setTitle(getResources().getString(R.string.im_message_info));
    }

    private void setChatMessageInfo() {
        // TODO: This we should not take from db, because after integrating history msg will not be available in db.
        if (!mChatMsgId.isEmpty()) {
            mIMChatMessage = IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().getChatMessageByMsgId(mChatMsgId);
        }

        if (mIMChatMessage.msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_TEXT)) {
            setSenderTextMsg();

        } else if (mIMChatMessage.msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_IMAGE)) {
            setSenderImageMsg();

        } else if (mIMChatMessage.msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
            setSenderVideoMsg();

        }

    }

    private void setSenderTextMsg() {
        mSenderTextMsg.setVisibility(View.VISIBLE);
        TextView tvSenderTextMsg = (TextView) findViewById(R.id.im_tv_sender_text_msg_text);
        TextView tvSenderTextMsgTime = (TextView) findViewById(R.id.im_tv_sender_text_msg_time);
        ImageView ivSenderTextMsgStatusImage = (ImageView) findViewById(R.id.im_iv_sender_text_msg_status);
        tvSenderTextMsg.setText(Html.fromHtml(mIMChatMessage.msgText.replaceAll("<p>", "").replaceAll("</p>", "")));

        setChatMessageTime(tvSenderTextMsgTime);
        ChatHelper.setSenderChatMsgStatus(ivSenderTextMsgStatusImage, mLoggedInUser.userId, mIMChatMessage);
    }

    private void setSenderImageMsg() {
        mSenderImageMsg.setVisibility(View.VISIBLE);

        ImageView ivSenderImageMsgImageThumb = (ImageView) findViewById(R.id.im_iv_sender_image_msg_thumb);
        TextView tvSenderImageMsgTime = (TextView) findViewById(R.id.im_tv_sender_image_msg_time);
        ImageView ivSenderImageMsgStatusImage = (ImageView) findViewById(R.id.im_iv_sender_image_msg_status);
        CircularProgressBar cpbSenderImageMsgProgressBar = (CircularProgressBar) findViewById(R.id.im_cpb_sender_image_msg_progress);
        cpbSenderImageMsgProgressBar.setVisibility(View.GONE);

        setChatMessageTime(tvSenderImageMsgTime);
        ChatHelper.setSenderChatMsgStatus(ivSenderImageMsgStatusImage, mLoggedInUser.userId, mIMChatMessage);

        String path;
        if (null == mIMChatMessage.imageOrVideoFilePath) {
            path = ChatMultiMediaHelper.getExistingSentImageFromPLivePath(mIMChatMessage.msgId);

        } else {
            path = ChatMultiMediaHelper.getGalleryFilePath(this, mIMChatMessage.imageOrVideoFilePath);
        }

        File imageFilePath = new File(path);
        String uri = Uri.fromFile(imageFilePath).toString();
        String decodedImgUri = Uri.decode(uri);
        ImageView imageView = ChatMultiMediaHelper.setImageWithWidthAndHeight(this, path, ivSenderImageMsgImageThumb);
        ImageAware imageAware = new ImageViewAware(imageView, false);
        ImageLoader.getInstance().displayImage(decodedImgUri, imageAware, IMUtils.getImageDisplayOptions(0));
    }

    private void setSenderVideoMsg() {
        mSenderVideoMsg.setVisibility(View.VISIBLE);
        ImageView ivSenderVideoMsgImageThumb = (ImageView) findViewById(R.id.im_iv_sender_video_msg_thumb);
        TextView tvSenderVideoMsgTime = (TextView) findViewById(R.id.im_tv_sender_video_msg_time);
        ImageView ivSenderVideoMsgStatusImage = (ImageView) findViewById(R.id.im_iv_sender_video_msg_status);
        TextView tvSenderVideoMsgVideoDuration = (TextView) findViewById(R.id.im_tv_sender_video_msg_video_duration);
        CircularProgressBar cpbSenderVideoMsgProgressBar = (CircularProgressBar) findViewById(R.id.im_cpb_sender_video_msg_progress);
        cpbSenderVideoMsgProgressBar.setVisibility(View.GONE);

        setChatMessageTime(tvSenderVideoMsgTime);
        ChatHelper.setSenderChatMsgStatus(ivSenderVideoMsgStatusImage, mLoggedInUser.userId, mIMChatMessage);

        String path;
        if (null == mIMChatMessage.imageOrVideoFilePath) {
            path = ChatMultiMediaHelper.getExistingSentVideoFromPLivePath(mIMChatMessage.msgId);

        } else {
            path = ChatMultiMediaHelper.getGalleryFilePath(this, mIMChatMessage.imageOrVideoFilePath);
        }
        tvSenderVideoMsgVideoDuration.setText(ChatMultiMediaHelper.getVideoDuration(path));

        Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(path, Thumbnails.MINI_KIND);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, 180, 180, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        ivSenderVideoMsgImageThumb.setImageBitmap(bitmap);
    }

    private void setChatMessageTime(TextView textView) {
        long timeToken = mIMChatMessage.timeToken + IMInstance.getInstance().getDeviceServerDeltaTime();
        textView.setText(IMUtils.formatPubNubTimeStamp(timeToken));
    }


    public void setChatMessageInfoAdapter() {
        for (int i = 0; i < mIMChatMessage.msgMembers.size(); i++) {
            if (mIMChatMessage.msgMembers.get(i).msgUserId.equals(mLoggedInUser.userId)) {
                mIMChatMessage.msgMembers.remove(i);
                break;
            }
        }

        IMChatMessageInfoAdapter chatMessageInfoAdapter = new IMChatMessageInfoAdapter(this, mIMChatMessage);
        mChatMessageInfoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatMessageInfoRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mChatMessageInfoRecyclerView.setAdapter(chatMessageInfoAdapter);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private IMUserPresenceListener mIMUserPresenceListener = new IMUserPresenceListener() {
        @Override
        public void onUserStatusReceived(final String userId, final Boolean isOnline) {

        }
    };

}
