package com.abln.futur;

import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;

import androidx.core.app.NotificationCompat;

import com.abln.chat.core.base.IMUnSeenChatMsgCountListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.FLog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.UIUtility;
import com.abln.futur.interfaces.TaskCompleteListener;

public class MainActivity extends BaseActivity implements TaskCompleteListener, IMUnSeenChatMsgCountListener {

    private static final String TAG = "MainActivity";
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = new Login();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, login)

                .show(login).commit();





    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        super.onTaskCompleted(context, intent);

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


    }


    public static void StaticFuncion() {


    }

    MainActivity() {
        System.out.println("This is my main activity ");
    }

    public static void main(String[] arg) {
        System.out.println("This is my main function ");
        StaticFuncion();







    }


    public static void getinformationOfCelephone(String CHANNEL_ID){

//        Intent fullScreenIntent = new Intent(this, MainActivity.class);
//        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
//                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, CHANNEL_ID)
//                        .setSmallIcon(R.drawable.im_ic_notification_mute)
//                        .setContentTitle("Futur Notification")
//                        .setContentText("Lets see the notification")
//                        .setPriority(NotificationCompat.PRIORITY_HIGH)
//                        .setCategory(NotificationCompat.CATEGORY_CALL)
//
//
//         .setFullScreenIntent(fullScreenPendingIntent, true);
//
//        Notification incomingCallNotification = notificationBuilder.build();
//
//
//      //  startForeground(notificationId, notification);




    }


    @Override
    public void onChatMessageReceived(int totalUnSeenChatMsgCount, IMChatMessage latestChatMessage) {

        FLog.d(TAG, "MEssage received");

        FLog.d("TAG","Message Delivered ");

    }





    public void changeStoreRoom(){

        //break the barrier of life and move towars the nation
        // om nam shivaya
    }





}
