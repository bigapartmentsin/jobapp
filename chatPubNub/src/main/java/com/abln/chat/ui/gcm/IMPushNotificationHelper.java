package com.abln.chat.ui.gcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationCompat.InboxStyle;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.ui.activities.IMChatActivity;
import com.abln.chat.ui.activities.IMChatThreadListActivity;
import com.abln.chat.ui.helper.ChatHelper;

import java.util.ArrayList;
import java.util.HashSet;


public class IMPushNotificationHelper {
    public static final String CHAT_PUSH_NOTIFICATION_ID = "CHAT PUSH NOTIFICATION ID";
    public static final int CHAT_PUSH_NOTIFICATION_ID_VALUE = 1001;

    public static void sendChatRoomDetailNotification(Context context, String chatRoomId, String displayMessage) {
        ArrayList<IMChatMessage> unSeenChatMessageList = new ArrayList<>(IMInstance.getInstance().getIMDatabase().
                getIMChatMessageDao().getTotalUnseenChatMessage());

        int threadCount = getChatThreadCountForUnSeenMsg(unSeenChatMessageList);

        if (!IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().isChatRoomExist(chatRoomId)) {
            threadCount = threadCount + 1;
        }

        Intent intent;
        if (threadCount == 1 && unSeenChatMessageList.size() == 0) {
            intent = getIMChatThreadListIntent(context);

        } else if (threadCount == 1 && unSeenChatMessageList.size() > 0) {
            intent = getIMChatWindowIntent(context, unSeenChatMessageList.get(0).roomId);

        } else if (threadCount > 1) {
            intent = getIMChatThreadListIntent(context);

        } else {
            cancelNotification(context);
            return;
        }

        ArrayList<String> notificationList = getNotificationList(unSeenChatMessageList);
        notificationList.add(0, displayMessage);
        showPushNotification(context, intent, unSeenChatMessageList.size(), threadCount, notificationList);
    }

    public static void updateLoggedInuserStatus(Context mContext, boolean isUserLoggedIn) {
//        isUserLoggedInforPush = isUserLoggedIn;
//        cancelNotification(mContext);
    }

    public static void sendChatMessageNotification(Context context) {
        Intent intent = null;
        ArrayList<IMChatMessage> unSeenChatMessageList = new ArrayList<>(IMInstance.getInstance().getIMDatabase().
                getIMChatMessageDao().getTotalUnseenChatMessage());

        int threadCount = getChatThreadCountForUnSeenMsg(unSeenChatMessageList);

        if (threadCount == 1) {
            intent = getIMChatWindowIntent(context, unSeenChatMessageList.get(0).roomId);

        } else if (threadCount > 1) {
            intent = getIMChatThreadListIntent(context);

        } else {
            cancelNotification(context);
            return;
        }



        ArrayList<String> notificationList = getNotificationList(unSeenChatMessageList);
        showPushNotification(context, intent, unSeenChatMessageList.size(), threadCount, notificationList);
    }

    private static int getChatThreadCountForUnSeenMsg(ArrayList<IMChatMessage> unSeenChatMessageList) {
        HashSet<String> chatRoomList = new HashSet<>();
        for (IMChatMessage chatMessage : unSeenChatMessageList) {
            chatRoomList.add(chatMessage.roomId);
        }

        return chatRoomList.size();
    }

    private static Intent getIMChatThreadListIntent(Context context) {
        Intent intent = new Intent(context, IMChatThreadListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(IMPushNotificationHelper.CHAT_PUSH_NOTIFICATION_ID, IMPushNotificationHelper.CHAT_PUSH_NOTIFICATION_ID_VALUE);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private static Intent getIMChatWindowIntent(Context context, String roomId) {
        Intent intent = new Intent(context, IMChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IMConstants.CHAT_ROOM_ID, roomId);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtras(bundle);
        return intent;
    }

    private static ArrayList<String> getNotificationList(ArrayList<IMChatMessage> unSeenChatMessageList) {
        ArrayList<String> notificationList = new ArrayList<>();
        IMChatUser loggedInUser = IMInstance.getInstance().getLoggedInUser();

        for (IMChatMessage chatMessage : unSeenChatMessageList) {
            IMChatRoomDetail chatRoomDetail = IMInstance.getInstance().getIMDatabase().
                    getIMChatRoomDetailDao().getChatRoomDetail(chatMessage.roomId);

            String chatRoomName = ChatHelper.getChatRoomName(loggedInUser, chatRoomDetail);
            String chatMsgSenderName = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                    getChatUserById(chatMessage.msgSenderId).userName;

            notificationList.add(chatMsgSenderName + "@ " + chatRoomName + ": " + getMessageText(chatMessage));

            if (notificationList.size() == 5) {
                break;
            }
        }

        return notificationList;
    }

    private static String getMessageText(IMChatMessage chatMessage) {
        String msgText = "";
        if (chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_TEXT)) {
            msgText = chatMessage.msgText;

        } else if (chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_IMAGE)) {
            //TODO image icon will be shown
            msgText = IMConstants.CHAT_MESSAGE_TYPE_IMAGE;

        } else if (chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
            //TODO Video icon will be shown
            msgText = IMConstants.CHAT_MESSAGE_TYPE_VIDEO;

        } else if (chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_AUDIO)) {
            //TODO Audio icon will be shown
            msgText = IMConstants.CHAT_MESSAGE_TYPE_AUDIO;
        }
        return msgText;
    }

    // This method is generating a notification and displaying the notification
    public static void showPushNotification(Context context, Intent intent, int totalUnSeenChatMsgCount,
                                            int totalUnSeenChatThreadCount, ArrayList<String> notificationList) {

        InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(context.getResources().getString(R.string.im_chat_message));
        inboxStyle.setSummaryText(totalUnSeenChatMsgCount + " " + context.getResources().getString(R.string.im_message_from)
                + " " + totalUnSeenChatThreadCount + context.getResources().getString(R.string.im_chats));

        for (String notification : notificationList) {
            inboxStyle.addLine(notification);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.im_ic_push_notification)
                .setContentTitle(IMInstance.getInstance().getIMConfiguration().pushNotificationTitle)
                .setContentText(context.getResources().getString(R.string.im_chat_message))
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(inboxStyle);

        if (Build.VERSION.SDK_INT >= 21) {
            builder.setVisibility(Notification.VISIBILITY_PUBLIC);
            builder.setVibrate(new long[0]);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(IMPushNotificationHelper.CHAT_PUSH_NOTIFICATION_ID_VALUE, builder.build());

    }

    public static void setNotificationSound(boolean isNotificationSoundEnabled, Context context) {
        if (isNotificationSoundEnabled && checkVibrationIsOn(context)) {
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(500);
        }
        if (isNotificationSoundEnabled && checkRingerIsOn(context)) {
            ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);
            toneG.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500);
        }
    }

    private static boolean checkVibrationIsOn(Context context) {
        boolean status = false;
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        if (am.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
            status = true;
        } else if (1 == Settings.System.getInt(context.getContentResolver(), "vibrate_when_ringing", 0)) //vibrate on
            status = true;
        return status;
    }

    private static boolean checkRingerIsOn(Context context) {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
    }

    public static void cancelNotification(Context ctx) {
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager nMgr = (NotificationManager) ctx.getSystemService(ns);
        nMgr.cancel(CHAT_PUSH_NOTIFICATION_ID_VALUE);
    }
}
