package com.abln.futur.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.abln.futur.R;
import com.abln.futur.common.AppConfig;


class FuturNotificationBuilder {
    private static NotificationCompat.Builder notificationBuilder;

    public static NotificationCompat.Builder getNotificationBuilder(String channel_id) {
        Context context = AppConfig.getInstance();
        if (notificationBuilder == null) {
            notificationBuilder = new NotificationCompat.Builder(context, channel_id);
        }
        return notificationBuilder;
    }
}

class FuturNotificationChannelBuilder {
    private static NotificationChannel notificationChannel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static NotificationChannel getNotificationChannel(String channelId, CharSequence channelName, int channelImportance) {
        if (notificationChannel == null) {
            notificationChannel = new NotificationChannel(channelId, channelName, channelImportance);
        }
        return notificationChannel;
    }
}

public class FuturNotificationHandler {

    public static void showDownloadNotification(Context context, String notificationId, String title, String messageBody, int progress) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        String channel_id = createNotificationChannel(context);
        NotificationCompat.Builder notificationBuilder = FuturNotificationBuilder.getNotificationBuilder(channel_id);
        notificationBuilder.setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setProgress(100, progress, false)
                .setSmallIcon(android.R.drawable.ic_menu_upload)
                .setOnlyAlertOnce(true)
                .setOngoing(true)
                .setColor(ContextCompat.getColor(context, R.color.colorTheme))
                .setAutoCancel(true);


        notificationManager.notify(notificationId.hashCode(), notificationBuilder.build());
    }


    public static void showNotification(Context context, String notificationId, String title, String messageBody) {

        String channel_id = createNotificationChannel(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channel_id)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(messageBody))
                .setSmallIcon(R.drawable.ic_logo1)
                .setVibrate(new long[]{0, 0})
                .setColor(ContextCompat.getColor(context, R.color.colorTheme))
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId.hashCode(), notificationBuilder.build());
    }

    public static String createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelId = "Channel_id_Futur";
            CharSequence channelName = "Downloading..";
            String channelDescription = "Application_name Alert";
            int channelImportance = NotificationManager.IMPORTANCE_LOW;


            NotificationChannel notificationChannel1 = FuturNotificationChannelBuilder.getNotificationChannel(channelId, channelName, channelImportance);
            notificationChannel1.setDescription(channelDescription);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel1);
            return channelId;
        } else {
            return null;
        }
    }


    public static void removeNotification(String objectId) {
        NotificationManager notificationManager
                = (NotificationManager) AppConfig.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        int id = objectId.hashCode();
        notificationManager.cancel(id);
    }

    public static void removeAllNotifications() {
        NotificationManager notificationManager
                = (NotificationManager) AppConfig.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }
}
