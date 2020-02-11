package com.abln.chat.ui.helper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.ui.adapters.IMChatMessageAdapter;
import com.abln.chat.ui.adapters.IMChatMessageAdapter.MessageViewHolder;

public class SentVideoHandler {
    Context mContext;
    private IMChatMessageAdapter mChatMessageAdapter;

    public SentVideoHandler(Context context, IMChatMessageAdapter chatMessageAdapter) {
        this.mContext = context;
        this.mChatMessageAdapter = chatMessageAdapter;
    }

    public void setUp(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        if (!mChatMessageAdapter.isInProgressMultiMediaMsg(chatMessage.msgId)) {
            setSentVideo(messageViewHolder, chatMessage, position);
            setSentVideoStatus(messageViewHolder, chatMessage);
        }
    }

    private void setSentVideo(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        String videoFilePath = ChatMultiMediaHelper.getSenderVideoFilePath(mContext, chatMessage);
        ChatMultiMediaHelper.loadVideoThumbnail(videoFilePath, messageViewHolder.ivSenderVideoMsgImageThumb);
        messageViewHolder.tvSenderVideoMsgVideoDuration.setText(ChatMultiMediaHelper.getVideoDuration(videoFilePath));

        setOnVideoThumbnailClickListener(messageViewHolder.ivSenderVideoMsgImageThumb, chatMessage);
        setOnVideoPlayClickListener(messageViewHolder.ivSenderVideoMsgPlayIcon, videoFilePath);
    }

    private void setSentVideoStatus(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        if (mChatMessageAdapter.isTransferIdAlreadyExist(chatMessage.msgId)) {
            TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().
                    getTransferById(mChatMessageAdapter.getTransferIdByMsgId(chatMessage.msgId));

            if (transferObserver.getState().equals(TransferState.IN_PROGRESS)) {
                setVideoUploadProgress(messageViewHolder, transferObserver.getBytesTransferred(), transferObserver.getBytesTotal());

            } else if (!transferObserver.getState().equals(TransferState.COMPLETED)) {
                setVideoWithUploadBtn(messageViewHolder, chatMessage);

            } else if (transferObserver.getState().equals(TransferState.COMPLETED)) {
                setVideoUploadCompleted(messageViewHolder);
            }

        } else {
            setVideoUploading(messageViewHolder, chatMessage);
        }
    }

    public void setVideoWithUploadBtn(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        setOnVideoUploadBtnClickListener(messageViewHolder, chatMessage);
        messageViewHolder.btnSenderVideoMsgUploadBtn.setVisibility(View.VISIBLE);
        messageViewHolder.ivSenderVideoMsgPlayIcon.setVisibility(View.GONE);
        messageViewHolder.cpbSenderVideoMsgProgressBar.setVisibility(View.GONE);
    }

    private void setVideoUploading(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        mChatMessageAdapter.setInProgressMultiMediaMsg(chatMessage.msgId);
        ChatMultiMediaHelper.uploadVideoToS3Bucket(mContext, chatMessage);
        messageViewHolder.cpbSenderVideoMsgProgressBar.setVisibility(View.VISIBLE);
        messageViewHolder.btnSenderVideoMsgUploadBtn.setVisibility(View.GONE);
        messageViewHolder.ivSenderVideoMsgPlayIcon.setVisibility(View.GONE);
    }

    public void setVideoUploadProgress(MessageViewHolder messageViewHolder, long bytesCurrent, long bytesTotal) {
        messageViewHolder.cpbSenderVideoMsgProgressBar.setVisibility(View.VISIBLE);
        messageViewHolder.ivSenderVideoMsgPlayIcon.setVisibility(View.GONE);
        messageViewHolder.btnSenderVideoMsgUploadBtn.setVisibility(View.GONE);
        float progress = (float) ((double) bytesCurrent * 100 / bytesTotal);
        messageViewHolder.cpbSenderVideoMsgProgressBar.setProgress(progress);
    }

    public void setVideoUploadCompleted(MessageViewHolder messageViewHolder) {
        messageViewHolder.ivSenderVideoMsgPlayIcon.setVisibility(View.VISIBLE);
        messageViewHolder.btnSenderVideoMsgUploadBtn.setVisibility(View.GONE);
        messageViewHolder.cpbSenderVideoMsgProgressBar.setVisibility(View.GONE);
    }

    private void setOnVideoUploadBtnClickListener(final MessageViewHolder messageViewHolder, final IMChatMessage chatMessage) {
        messageViewHolder.btnSenderVideoMsgUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setVideoUploading(messageViewHolder, chatMessage);
            }
        });
    }

    private void setOnVideoThumbnailClickListener(ImageView thumbnail, final IMChatMessage chatMessage) {
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMultiMediaHelper.openChatMultimediaSlideActivity(mContext, chatMessage.roomId, chatMessage.msgId);
            }
        });
    }

    private void setOnVideoPlayClickListener(ImageView videoPlayIcon, final String videoPath) {
        videoPlayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMultiMediaHelper.openChatVideoPlayer(mContext, videoPath);
            }
        });
    }
}
