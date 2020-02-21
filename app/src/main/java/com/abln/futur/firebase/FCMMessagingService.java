package com.abln.futur.firebase;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.abln.chat.IMConfiguration;
import com.abln.chat.IMInstance;
import com.abln.chat.core.model.IMChatUser;
import com.abln.futur.R;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


/**
 * Created by krishna on 7/19/17.
 */
public class FCMMessagingService extends FirebaseMessagingService {
    public static final String TAG = FCMMessagingService.class.getSimpleName();


    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("TAG", s);


    }

    // This method will be called on every new message received
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Log.v(TAG, remoteMessage.getMessageId());
        //  Getting the message from the bundle
        try {
            String message = remoteMessage.getData().get("message");
            if (null != message) {
                processChatPushNotification(message);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    private void processChatPushNotification(String message) {
        if (null == IMInstance.getInstance().getIMConfiguration()) {
            IMConfiguration imConfiguration = new IMConfiguration(null, getLoggedInUser(),//TODO
                    getResources().getString(R.string.im_push_notification_title),
                    FirebaseInstanceId.getInstance().getToken(),
                    AppConfig.PN_SUB,
                    AppConfig.PN_PUB,
                    60,
                    NetworkConfig.S3_EP_URL,
                    NetworkConfig.S3_IMAGES,
                    NetworkConfig.S3_VIDEOS,
                    NetworkConfig.S3_DOC,
                    NetworkConfig.S3_S_KEY,
                    NetworkConfig.S3_A_KEY);
            IMInstance.getInstance().setIMConfiguration(imConfiguration);
        }

        JsonObject jsonObject = (new JsonParser()).parse(message).getAsJsonObject();
        IMInstance.getInstance().handlePushNotification(jsonObject);
    }

    private IMChatUser getLoggedInUser() {
        PrefManager prefManager = new PrefManager();
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String loggedInUserId = sharedPref.getString("LOGGED_IN_USER_ID", "");

        IMChatUser chatUser = new IMChatUser();
        chatUser.userId = prefManager.getApikey();
//        chatUser.userName = "USER " + loggedInUserId.substring(3);
        chatUser.userName = prefManager.getUserName();
        return chatUser;
    }


}
