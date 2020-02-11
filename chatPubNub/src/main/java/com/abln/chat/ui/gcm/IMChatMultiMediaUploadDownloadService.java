package com.abln.chat.ui.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.abln.chat.IMInstance;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.ui.helper.AWSClientHelper;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.ui.helper.ChatMultiMediaHelper;
import com.abln.chat.utils.IMConstants;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class IMChatMultiMediaUploadDownloadService extends Service {
    public static final String TAG = IMChatMultiMediaUploadDownloadService.class.getSimpleName();

    public static final String MULTIMEDIA_TRANSFER_ID_BROADCAST_ACTION = "MULTIMEDIA TRANSFER ID BROADCAST ACTION";
    public static final String MULTIMEDIA_UPLOAD_DOWNLOAD_PROGRESS_BROADCAST_ACTION = "MULTIMEDIA UPLOAD DOWNLOAD PROGRESS BROADCAST ACTION";
    public static final String MULTIMEDIA_UPLOAD_DOWNLOAD_STATE_BROADCAST_ACTION = "MULTIMEDIA UPLOAD DOWNLOAD STATE BROADCAST ACTION";


    private boolean isTokenExpired = false;

    private ArrayList<Intent> mFailedTransfersList = new ArrayList<>();
    private ArrayList<Integer> mCurrentTransfersIdList = new ArrayList<>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        processMultiMediaFile(intent);

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        cancelAllCurrentTransfers();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelAllCurrentTransfers();
    }

    private void processMultiMediaFile(Intent intent) {
        if (null == intent) {
            return;
        }

        String filePath = intent.getStringExtra(IMConstants.FILE_PATH);
        String fileType = intent.getStringExtra(IMConstants.FILE_TYPE);
        String transferType = intent.getStringExtra(IMConstants.TRANSFER_TYPE);
        IMChatMessage chatMessage = (IMChatMessage) intent.getSerializableExtra(IMConstants.MULTIMEDIA_CHAT_MSG);
        IMChatRoomDetail chatRoomDetail = IMInstance.getInstance().getIMDatabase().
                getIMChatRoomDetailDao().getChatRoomDetail(chatMessage.roomId);

        copyFileToLocalStorage(chatMessage, transferType, fileType, filePath);

        doUploadDownloadToS3Bucket(intent, chatRoomDetail, chatMessage, transferType, fileType, filePath);
    }

    private void copyFileToLocalStorage(IMChatMessage chatMessage, String transferType, String fileType, String filePath) {
        if (null == fileType || null == transferType) {
            return;
        }

        if (transferType.equals(IMConstants.TRANSFER_TYPE_UPLOAD)) {
            if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                ChatMultiMediaHelper.copySentImageToPLivePath(filePath, chatMessage.msgId);

            } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                ChatMultiMediaHelper.copySentVideoToPLivePath(filePath, chatMessage.msgId);
            }
        }
    }

    private void doUploadDownloadToS3Bucket(Intent intent, IMChatRoomDetail chatRoomDetail, IMChatMessage chatMessage,
                                            String transferType, String fileType, String filePath) {

        if (null != filePath && null != fileType && null != transferType) {
            TransferUtility transferUtility = AWSClientHelper.getInstance().getTransferUtility();
            TransferObserver transferObserver;

            if (transferType.equals(IMConstants.TRANSFER_TYPE_UPLOAD)) {
                transferObserver = transferUtility.upload(
                        getS3BucketPath(fileType),  /* The bucket to upload to */
                        chatMessage.msgId,    /* The key for the uploaded object */
                        new File(filePath)        /* The file where the data to upload exists */
                );

            } else {
                transferObserver = transferUtility.download(
                        getS3BucketPath(fileType),  /* The bucket to download from */
                        chatMessage.msgId,    /* The key for the download object */
                        new File(filePath)        /* The file where the data to download exists */
                );
            }

            int transferId = transferObserver.getId();
                            mCurrentTransfersIdList.add(transferId);

            broadcastCurrentTransferId(chatRoomDetail, transferId);
            transferObserver.setTransferListener(new UploadDownloadListener(intent, chatRoomDetail,
                    chatMessage, transferType, fileType));
        }
    }

    public String getS3BucketPath(String fileType) {
        String bucketName = "";

        if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
            bucketName = IMInstance.getInstance().getIMConfiguration().s3ImageBucketPath;

        } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
            bucketName = IMInstance.getInstance().getIMConfiguration().s3VideoBucketPath;

        } else if (fileType.equals(IMConstants.FILE_TYPE_DOC)) {
            bucketName = IMInstance.getInstance().getIMConfiguration().s3DocBucketPath;
        }

        return bucketName;
    }

    public class UploadDownloadListener implements TransferListener {
        Intent intent;
        IMChatRoomDetail chatRoomDetail;
        IMChatMessage chatMessage;
        String transferType;
        String fileType;

        UploadDownloadListener(Intent intent, IMChatRoomDetail chatRoomDetail, IMChatMessage chatMessage,
                               String transferType, String fileType) {
            this.intent = intent;
            this.chatRoomDetail = chatRoomDetail;
            this.chatMessage = chatMessage;
            this.transferType = transferType;
            this.fileType = fileType;
        }

        @Override
        public void onError(int id, Exception e) {
            // TODO: STORAGE PERMISSION ERROR TO BE HANDLED IN CASE OF DOWNLOAD

            Log.e(TAG, "Error during upload: " + id + ": " + e.getMessage());
            if (e.getMessage().contains("Token is expired")) {
                mFailedTransfersList.add(intent);
                if (!isTokenExpired) {
                    isTokenExpired = true;

                }

            } else {
                broadcastMultiMediaUploadDownloadState(chatRoomDetail, transferType, fileType, id);
            }
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format(Locale.getDefault(), "onProgressChanged: %d, total: %d, current: %d", id, bytesTotal, bytesCurrent));
            broadcastMultiMediaUploadDownloadProgress(chatRoomDetail, transferType, fileType, id, bytesCurrent, bytesTotal);
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            if (newState.equals(TransferState.COMPLETED)) {
                broadcastMultiMediaUploadDownloadState(chatRoomDetail, transferType, fileType, id);
                mCurrentTransfersIdList.remove(Integer.valueOf(id));

                publishMultiMediaUploadedMessage(chatRoomDetail, chatMessage, newState, transferType);
            }
        }
    }

    private void broadcastCurrentTransferId(IMChatRoomDetail chatRoomDetail, int transferId) {
        Intent intent = new Intent(MULTIMEDIA_TRANSFER_ID_BROADCAST_ACTION);
        intent.putExtra(IMConstants.MULTIMEDIA_FILE_ROOM_ID, chatRoomDetail.roomId);
        intent.putExtra(IMConstants.TRANSFER_ID, transferId);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastMultiMediaUploadDownloadProgress(IMChatRoomDetail chatRoomDetail, String transferType,
                                                           String fileType, int id, long bytesCurrent, long bytesTotal) {
        Intent intent = new Intent(MULTIMEDIA_UPLOAD_DOWNLOAD_PROGRESS_BROADCAST_ACTION);
        intent.putExtra(IMConstants.MULTIMEDIA_FILE_ROOM_ID, chatRoomDetail.roomId);
        intent.putExtra(IMConstants.TRANSFER_TYPE, transferType);
        intent.putExtra(IMConstants.FILE_TYPE, fileType);
        intent.putExtra(IMConstants.TRANSFER_ID, id);
        intent.putExtra(IMConstants.BYTES_TRANSFER_CURRENT, bytesCurrent);
        intent.putExtra(IMConstants.BYTES_TRANSFER_TOTAL, bytesTotal);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void broadcastMultiMediaUploadDownloadState(IMChatRoomDetail chatRoomDetail, String transferType,
                                                        String fileType, int id) {
        Intent intent = new Intent(MULTIMEDIA_UPLOAD_DOWNLOAD_STATE_BROADCAST_ACTION);
        intent.putExtra(IMConstants.MULTIMEDIA_FILE_ROOM_ID, chatRoomDetail.roomId);
        intent.putExtra(IMConstants.TRANSFER_TYPE, transferType);
        intent.putExtra(IMConstants.FILE_TYPE, fileType);
        intent.putExtra(IMConstants.TRANSFER_ID, id);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void publishMultiMediaUploadedMessage(IMChatRoomDetail chatRoomDetail, IMChatMessage chatMessage,
                                                  TransferState transferState, String transferType) {
        if (transferState.equals(TransferState.COMPLETED) && transferType.equals(IMConstants.TRANSFER_TYPE_UPLOAD)) {
            // Here we are just updating the intended users, if any user removed or added while uploading multimedia file
            chatMessage.msgMembers = ChatHelper.getChatMessageMembers(chatRoomDetail);
            IMInstance.getInstance().publishChatMessage(chatRoomDetail.roomId, chatMessage);
        }
    }

    private void cancelAllCurrentTransfers() {
        for (Integer id : mCurrentTransfersIdList) {
            AWSClientHelper.getInstance().getTransferUtility().cancel(id);
        }
    }



    private void retryFailedTransfersAfterTokenUpdate() {
        for (Intent intent : mFailedTransfersList) {
            processMultiMediaFile(intent);
        }
    }
}
