package com.abln.chat.ui.helper;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.ui.adapters.IMChatMessageAdapter;
import com.abln.chat.ui.adapters.IMChatMessageAdapter.MessageViewHolder;

public class SentImageHandler {

    private Context mContext;
    private IMChatMessageAdapter mChatMessageAdapter;

    public SentImageHandler(Context context, IMChatMessageAdapter chatMessageAdapter) {
        this.mContext = context;
        this.mChatMessageAdapter = chatMessageAdapter;
    }

    public void setUp(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        setSentImage(messageViewHolder, chatMessage, position);
        if (!mChatMessageAdapter.isInProgressMultiMediaMsg(chatMessage.msgId)) {
            setSentImageStatus(messageViewHolder, chatMessage);
        }
    }

    private void setSentImage(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        String imageFilePath = ChatMultiMediaHelper.getSenderImageFilePath(mContext, chatMessage);
        ChatMultiMediaHelper.loadImageFromLocalStoragePath(mContext, imageFilePath,
                messageViewHolder.ivSenderImageMsgImageThumb);
        mChatMessageAdapter.setOnImageClickListener(position, messageViewHolder.itemView,
                messageViewHolder.ivSenderImageMsgImageThumb);
    }




    private void setSentImageStatus(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        if (mChatMessageAdapter.isTransferIdAlreadyExist(chatMessage.msgId)) {

            TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().
                    getTransferById(mChatMessageAdapter.getTransferIdByMsgId(chatMessage.msgId));

            if (transferObserver.getState().equals(TransferState.IN_PROGRESS)) {
                setImageUploadProgress(messageViewHolder, transferObserver.getBytesTransferred(),
                        transferObserver.getBytesTotal());

            } else if (!transferObserver.getState().equals(TransferState.COMPLETED)) {
                setImageWithUploadBtn(messageViewHolder, chatMessage);

            } else if (transferObserver.getState().equals(TransferState.COMPLETED)) {
                setImageUploadCompleted(messageViewHolder);
            }

        } else {
            setImageUploading(messageViewHolder, chatMessage);
        }
    }

    public void setImageWithUploadBtn(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        setOnImageUploadBtnClickListener(messageViewHolder, chatMessage);
        messageViewHolder.btnSenderImageMsgUploadBtn.setVisibility(View.VISIBLE);
        messageViewHolder.cpbSenderImageMsgProgressBar.setVisibility(View.GONE);
    }

    private void setImageUploading(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        mChatMessageAdapter.setInProgressMultiMediaMsg(chatMessage.msgId);
        ChatMultiMediaHelper.uploadImageToS3Bucket(mContext, messageViewHolder.ivSenderImageMsgImageThumb, chatMessage);
        messageViewHolder.btnSenderImageMsgUploadBtn.setVisibility(View.GONE);
        messageViewHolder.cpbSenderImageMsgProgressBar.setVisibility(View.VISIBLE);
    }

    public void setImageUploadProgress(MessageViewHolder messageViewHolder, long bytesCurrent,
                                       long bytesTotal) {
        messageViewHolder.btnSenderImageMsgUploadBtn.setVisibility(View.GONE);
        messageViewHolder.cpbSenderImageMsgProgressBar.setVisibility(View.VISIBLE);
        float progress = (float) ((double) bytesCurrent * 100 / bytesTotal);
        messageViewHolder.cpbSenderImageMsgProgressBar.setProgress(progress);
    }

    public void setImageUploadCompleted(MessageViewHolder messageViewHolder) {
        messageViewHolder.btnSenderImageMsgUploadBtn.setVisibility(View.GONE);
        messageViewHolder.cpbSenderImageMsgProgressBar.setVisibility(View.GONE);
    }

    private void setOnImageUploadBtnClickListener(final MessageViewHolder messageViewHolder,
                                                  final IMChatMessage chatMessage) {
        messageViewHolder.btnSenderImageMsgUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setImageUploading(messageViewHolder, chatMessage);
            }
        });
    }
}
