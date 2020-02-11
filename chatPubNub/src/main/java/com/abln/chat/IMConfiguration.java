package com.abln.chat;

import com.abln.chat.core.model.IMChatUser;

import java.util.ArrayList;

public class IMConfiguration {

    ArrayList<IMChatUser> chatUserList = new ArrayList<>();
    IMChatUser loggedInUser;

    public String pushNotificationTitle;
    public String fcmRegistrationToken;
    public String subscribeKey;
    public String publishKey;
    public Integer userPresenceTimeOut;

    // Multimedia
    public String s3EndPoint;
    public String s3ImageBucketPath;
    public String s3VideoBucketPath;
    public String s3DocBucketPath;

    public String s3SecretKey;
    public String s3AccessKey;

    public IMConfiguration(ArrayList<IMChatUser> chatUserList, IMChatUser loggedInUser, String pushNotificationTitle,
                           String fcmRegistrationToken, String subscribeKey, String publishKey, Integer userPresenceTimeOut,
                           String s3EndPoint, String s3ImageBucketPath, String s3VideoBucketPath,String s3DocBucketPath, String s3SecretKey, String s3AccessKey) {

        this.chatUserList = chatUserList;
        this.loggedInUser = loggedInUser;
        this.pushNotificationTitle = pushNotificationTitle;
        this.fcmRegistrationToken = fcmRegistrationToken;
        this.subscribeKey = subscribeKey;
        this.publishKey = publishKey;
        this.userPresenceTimeOut = userPresenceTimeOut;

        this.s3EndPoint = s3EndPoint;
        this.s3ImageBucketPath = s3ImageBucketPath;
        this.s3VideoBucketPath = s3VideoBucketPath;
        this.s3DocBucketPath = s3DocBucketPath;
        this.s3SecretKey = s3SecretKey;
        this.s3AccessKey = s3AccessKey;
    }


    public String s3AccountId;
    public String s3ProvideName;
    public String cognitoPoolId;
    public String congnitoIdentityId;
    public String cognitoToken;




}
