package com.abln.chat.ui.helper;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.ui.adapters.IMChatMessageAdapter;
import com.abln.chat.ui.adapters.IMChatMessageAdapter.MessageViewHolder;


public class ReceivedVideoHandler {
    Context mContext;
    IMChatMessageAdapter mChatMessageAdapter;

    public ReceivedVideoHandler(Context context, IMChatMessageAdapter chatMessageAdapter) {
        this.mContext = context;
        this.mChatMessageAdapter = chatMessageAdapter;
    }

    public void setUp(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        setVideoDownloadStatus(messageViewHolder, chatMessage, position);
    }

    private void setVideoDownloadStatus(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        if (mChatMessageAdapter.isTransferIdAlreadyExist(chatMessage.msgId)) {

            TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().
                    getTransferById(mChatMessageAdapter.getTransferIdByMsgId(chatMessage.msgId));

            if (transferObserver.getState().equals(TransferState.IN_PROGRESS)) {
                setReceivedVideoThumb(messageViewHolder, chatMessage);
                setVideoDownloadProgress(messageViewHolder, chatMessage, transferObserver.
                        getBytesTransferred(), transferObserver.getBytesTotal());

            } else if (!transferObserver.getState().equals(TransferState.COMPLETED)) {
                setReceivedVideoThumb(messageViewHolder, chatMessage);
                setVideoWithDownloadBtn(messageViewHolder, chatMessage);

            } else if (transferObserver.getState().equals(TransferState.COMPLETED)) {
                setVideoDownloadCompleted(messageViewHolder, chatMessage);
            }

        } else {
            setReceivedVideoThumb(messageViewHolder, chatMessage);
            setVideoWithDownloadBtn(messageViewHolder, chatMessage);
        }
    }




    private void setReceivedVideoThumb(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        ChatMultiMediaHelper.loadBase64Thumbnail(mContext, messageViewHolder.ivRecipientVideoMsgImageThumb, chatMessage, false);
    }

    public void setVideoWithDownloadBtn(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        setOnVideoDownloadBtnClickListener(messageViewHolder, chatMessage);
        messageViewHolder.tvRecipientVideoMsgVideoDuration.setText(chatMessage.msgMultimediaInfo.videoDuration);
        messageViewHolder.btnRecipientVideoMsgDownloadBtn.setVisibility(View.VISIBLE);
        messageViewHolder.cpbRecipientVideoMsgProgressBar.setVisibility(View.GONE);
        messageViewHolder.ivRecipientVideoMsgPlayIcon.setVisibility(View.GONE);
    }

    private void startDownloadingVideo(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        ChatMultiMediaHelper.downloadVideoFromS3Bucket(mContext, chatMessage);
        messageViewHolder.cpbRecipientVideoMsgProgressBar.setVisibility(View.VISIBLE);
        messageViewHolder.btnRecipientVideoMsgDownloadBtn.setVisibility(View.GONE);
    }

    public void setVideoDownloadProgress(MessageViewHolder messageViewHolder, IMChatMessage chatMessage,
                                         long bytesCurrent, long bytesTotal) {
        messageViewHolder.tvRecipientVideoMsgVideoDuration.setText(chatMessage.msgMultimediaInfo.videoDuration);
        messageViewHolder.cpbRecipientVideoMsgProgressBar.setVisibility(View.VISIBLE);
        messageViewHolder.btnRecipientVideoMsgDownloadBtn.setVisibility(View.GONE);
        messageViewHolder.ivRecipientVideoMsgPlayIcon.setVisibility(View.GONE);
        float progress = (float) ((double) bytesCurrent * 100 / bytesTotal);
        messageViewHolder.cpbRecipientVideoMsgProgressBar.setProgress(progress);
    }

    public void setVideoDownloadCompleted(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        String videoFilePath = ChatMultiMediaHelper.getExistingVideoFromPLivePath(chatMessage.msgId);
        ChatMultiMediaHelper.loadVideoThumbnail(videoFilePath, messageViewHolder.ivRecipientVideoMsgImageThumb);
        setOnVideoThumbnailClickListener(messageViewHolder.ivRecipientVideoMsgImageThumb, chatMessage);
        setOnVideoPlayClickListener(messageViewHolder.ivRecipientVideoMsgPlayIcon, videoFilePath);
        messageViewHolder.tvRecipientVideoMsgVideoDuration.setText(ChatMultiMediaHelper.getVideoDuration(videoFilePath));
        messageViewHolder.ivRecipientVideoMsgPlayIcon.setVisibility(View.VISIBLE);
        messageViewHolder.btnRecipientVideoMsgDownloadBtn.setVisibility(View.GONE);
        messageViewHolder.cpbRecipientVideoMsgProgressBar.setVisibility(View.GONE);
    }

    private void setOnVideoDownloadBtnClickListener(final MessageViewHolder messageViewHolder,
                                                    final IMChatMessage chatMessage) {
        messageViewHolder.btnRecipientVideoMsgDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (isNetworkAvailaible)
                    startDownloadingVideo(messageViewHolder, chatMessage);
//                else {
//                    Toast.makeText(mContext, "Please check your internet connection",
//                            Toast.LENGTH_LONG).show();
//                }
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


