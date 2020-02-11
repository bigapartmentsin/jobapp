package com.abln.chat.ui.helper;

import android.content.Context;
import android.view.View;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.ui.adapters.IMChatMessageAdapter;
import com.abln.chat.ui.adapters.IMChatMessageAdapter.MessageViewHolder;


public class ReceivedImageHandler {
    Context mContext;
    IMChatMessageAdapter mChatMessageAdapter;

    public ReceivedImageHandler(Context context, IMChatMessageAdapter chatMessageAdapter) {
        this.mContext = context;
        this.mChatMessageAdapter = chatMessageAdapter;
    }

    public void setUp(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        setImageDownloadStatus(messageViewHolder, chatMessage, position);
    }

    private void setImageDownloadStatus(MessageViewHolder messageViewHolder, IMChatMessage chatMessage, int position) {
        if (mChatMessageAdapter.isTransferIdAlreadyExist(chatMessage.msgId)) {

            TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().
                    getTransferById(mChatMessageAdapter.getTransferIdByMsgId(chatMessage.msgId));

            if (transferObserver.getState().equals(TransferState.IN_PROGRESS)) {
                setReceivedImageThumb(messageViewHolder, chatMessage);
                setImageDownloadProgress(messageViewHolder, transferObserver.getBytesTransferred(),
                        transferObserver.getBytesTotal());

            } else if (!transferObserver.getState().equals(TransferState.COMPLETED)) {
                setReceivedImageThumb(messageViewHolder, chatMessage);
                setImageWithDownloadBtn(messageViewHolder, chatMessage);

            } else {
                setImageDownloadCompleted(messageViewHolder, chatMessage, position);
            }

        } else {
            setReceivedImageThumb(messageViewHolder, chatMessage);
            setImageWithDownloadBtn(messageViewHolder, chatMessage);
        }
    }




    private void setReceivedImageThumb(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        ChatMultiMediaHelper.loadBase64Thumbnail(mContext, messageViewHolder.ivRecipientImageMsgImageThumb,
                chatMessage, true);
    }

    public void setImageWithDownloadBtn(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        setOnImageDownloadBtnClickListener(messageViewHolder, chatMessage);
        messageViewHolder.btnRecipientImageMsgDownloadBtn.setVisibility(View.VISIBLE);
        messageViewHolder.cpbRecipientImageMsgProgressBar.setVisibility(View.GONE);
    }

    private void setImageDownloading(MessageViewHolder messageViewHolder, IMChatMessage chatMessage) {
        ChatMultiMediaHelper.downloadImageFromS3Bucket(mContext, chatMessage);
        messageViewHolder.cpbRecipientImageMsgProgressBar.setVisibility(View.VISIBLE);
        messageViewHolder.btnRecipientImageMsgDownloadBtn.setVisibility(View.GONE);
    }

    public void setImageDownloadProgress(MessageViewHolder messageViewHolder,
                                          long bytesCurrent, long bytesTotal) {
        messageViewHolder.cpbRecipientImageMsgProgressBar.setVisibility(View.VISIBLE);
        messageViewHolder.btnRecipientImageMsgDownloadBtn.setVisibility(View.GONE);
        float progress = (float) ((double) bytesCurrent * 100 / bytesTotal);
        messageViewHolder.cpbRecipientImageMsgProgressBar.setProgress(progress);
    }

    public void setImageDownloadCompleted(MessageViewHolder messageViewHolder,
                                           IMChatMessage chatMessage, int position) {
        String imageFilePath = ChatMultiMediaHelper.getExistingImageFromPLivePath(chatMessage.msgId);
        ChatMultiMediaHelper.loadImageFromLocalStoragePath(mContext, imageFilePath,
                messageViewHolder.ivRecipientImageMsgImageThumb);
        mChatMessageAdapter.setOnImageClickListener(position, messageViewHolder.itemView,
                messageViewHolder.ivRecipientImageMsgImageThumb);
        messageViewHolder.btnRecipientImageMsgDownloadBtn.setVisibility(View.GONE);
        messageViewHolder.cpbRecipientImageMsgProgressBar.setVisibility(View.GONE);
    }

    private void setOnImageDownloadBtnClickListener(final MessageViewHolder messageViewHolder,
                                                    final IMChatMessage chatMessage) {

        messageViewHolder.btnRecipientImageMsgDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if (isNetworkAvailaible) {
                    setImageDownloading(messageViewHolder, chatMessage);
//                } else {
//                    Toast.makeText(mContext, "Please check your internet connection",
//                            Toast.LENGTH_LONG).show();
//                }
            }
        });

    }

}


