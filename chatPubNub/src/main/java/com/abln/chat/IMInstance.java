package com.abln.chat;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.abln.chat.core.base.IMBaseListener;
import com.abln.chat.core.base.IMChattingListener;
import com.abln.chat.core.base.IMMsgResponseHandler;
import com.abln.chat.core.base.IMMsgResponseListener;
import com.abln.chat.core.base.IMUnSeenChatMsgCountListener;
import com.abln.chat.core.base.IMUserPresenceListener;
import com.abln.chat.core.db.IMAppDatabase;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.pubnub.IMPubNubHelper;
import com.abln.chat.pubnub.IMPubNubUtils;
import com.abln.chat.ui.gcm.IMPushNotificationHandler;
import com.abln.chat.ui.gcm.IMPushNotificationHelper;
import com.abln.chat.ui.helper.AWSClientHelper;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMPrefs;
import com.abln.chat.utils.IMUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class IMInstance implements IMMsgResponseListener {
    public static final String TAG = IMInstance.class.getSimpleName();

    private static IMInstance mIMInstance;

    private String currentRoomId;
    public static boolean isNetworkAvailaible=false;
    public static boolean isFreshLogin=false;
    public static boolean isAppLaunched=false;
    private boolean isCurrentRoomActive = false;
    private IMBaseListener mIMBaseListener;
    private IMChattingListener mIMChattingListener;
    private IMUnSeenChatMsgCountListener mIMUnSeenChatMsgCountListener;
    private IMUserPresenceListener mIMUserPresenceListener;
    private IMMsgResponseHandler mIMMsgResponseHandler;


    private IMConfiguration mIMConfiguration;
    private IMChatUser mLoggedInUser;

    private long mDeviceServerDeltaTime;


    private Context mContext;

    private Gson mGson = new Gson();

    private IMInstance() {

    }

    public synchronized void init(Context context) {
        this.mContext = context;
        IMAppDatabase.initDatabase(mContext);
        IMPrefs.initializeInstance(mContext);
        IMUtils.setImageLoaderLibraryConfiguration(mContext);
    }

    public static IMInstance getInstance() {
        if (null == mIMInstance) {
            mIMInstance = new IMInstance();
        }
        return mIMInstance;
    }

    public static void stashInstance() {
        mIMInstance = null;
    }

    public void setIMConfiguration(IMConfiguration configuration, IMBaseListener baseListener) {
        setIMConfiguration(configuration);
        this.mIMBaseListener = baseListener;
    }

    public void setAppLaunchedKey(Boolean key) {

        this.isAppLaunched = key;
    }

    public void setIMConfiguration(IMConfiguration configuration) {
        this.mIMConfiguration = configuration;
        this.mLoggedInUser = mIMConfiguration.loggedInUser;
        this.mIMMsgResponseHandler = new IMMsgResponseHandler(this, getIMDatabase(), mLoggedInUser);

        AWSClientHelper.initAWSClientHelper(mContext, configuration);
        getIMDatabase().getIMChatUserDao().insertAll(mIMConfiguration.chatUserList);
        IMPrefs.getInstance().setFCMRegToken(mIMConfiguration.fcmRegistrationToken);
        initiatePubNub(mIMMsgResponseHandler);
    }

    private void initiatePubNub(IMMsgResponseHandler msgResponseHandler) {
        IMPubNubHelper.getInstance().initPubNub(mIMConfiguration.publishKey, mIMConfiguration.subscribeKey,
                mIMConfiguration.userPresenceTimeOut, mIMConfiguration.loggedInUser, msgResponseHandler);
        IMPubNubHelper.getInstance().subscribeToRelevantChannels(getActiveChatRoomIdList());
        IMPubNubHelper.getInstance().fetchPubNubServerTime();
    }

    public void updateServerDeviceDeltaTime() {
        IMPubNubHelper.getInstance().fetchPubNubServerTime();
    }

    public void updateFCMRegistrationToken(String fcmRegistrationToken) {
        IMPrefs.getInstance().setFCMRegToken(fcmRegistrationToken);
    }

    public void setIMChattingListener(IMChattingListener chattingListener, String activeRoomId) {
        this.mIMChattingListener = chattingListener;
        this.currentRoomId = activeRoomId;
        this.isCurrentRoomActive = true;
    }

    public void updateCurrentRoomState(boolean bool) {
        this.isCurrentRoomActive = bool;
    }

    public void setIMBaseListener(IMBaseListener baseListener) {
        this.mIMBaseListener = baseListener;
    }

    public void setUnSeenChatMsgCountListener(IMUnSeenChatMsgCountListener unSeenChatMsgCountListener) {
        this.mIMUnSeenChatMsgCountListener = unSeenChatMsgCountListener;
    }

    public void setUserPresenceListener(IMUserPresenceListener userPresenceListener) {
        this.mIMUserPresenceListener = userPresenceListener;
    }

    public int getTotalUnSeenChatMsgCount() {
        return getIMDatabase().getIMChatMessageDao().getTotalUnSeenChatMessageCount();
    }

    public IMConfiguration getIMConfiguration() {
        return mIMConfiguration;
    }

    public IMAppDatabase getIMDatabase() {
        return IMAppDatabase.getDatabase();
    }

    public IMChatUser getLoggedInUser() {
        return mLoggedInUser;
    }

    public long getDeviceServerDeltaTime() {
        return mDeviceServerDeltaTime;
    }

    public void handlePushNotification(JsonObject jsonObject) {
        if (null == this.mIMBaseListener) {
            mIMMsgResponseHandler.onMessageReceived(jsonObject);
        }
    }

    public void subscribeToPresenceChannel() {
        IMPubNubHelper.getInstance().subscribeToParticularChannels(IMConstants.PRESENCE_CHANNEL_ID);
    }

    public void unSubscribeFromPresenceChannel() {
        IMPubNubHelper.getInstance().unSubscribeFromParticularChannels(IMConstants.PRESENCE_CHANNEL_ID);
    }

    public void createChatRoom(IMChatRoomDetail chatRoomDetail) {
        for (IMChatRoomMember chatRoomMember : chatRoomDetail.roomMembers) {
            publishChatRoomDetail(chatRoomMember.userId, chatRoomDetail);
        }
    }

    public void publishChatRoomDetailUpdate(String chatUserIdsToUpdateDetail, IMChatRoomDetail chatRoomDetail) {
        List<String> chatRoomMemberIdList = Arrays.asList(chatUserIdsToUpdateDetail.trim().split(","));
        for (String memberId : chatRoomMemberIdList) {
            publishChatRoomDetail(memberId, chatRoomDetail);
        }
    }

    public void publishChatRoomNameUpdate(String channelId, IMChatRoomDetailUpdate chatRoomDetailUpdate) {
        publish(channelId, chatRoomDetailUpdate);
    }

    public void publishChatMessage(String channelId, IMChatMessage chatMessage) {
        publish(channelId, chatMessage);
    }

    public void publishChatTypingStatus(String channelId, boolean isTyping) {
        List<String> list = Collections.singletonList(channelId);
        HashMap<String, Boolean> map = new HashMap<>();
        map.put(IMConstants.IS_TYPING, isTyping);
        IMPubNubHelper.getInstance().setPresenceState(list, map, mLoggedInUser.userId);
    }

    public void publishDeliveredOrReadStatus(IMChatMessage chatMessage, boolean isForRead) {
        long timeToken = IMUtils.getDeviceTimeInMillis() - getDeviceServerDeltaTime();
        IMChatMessageStatus chatMessageStatus = IMPubNubUtils.getChatMessageStatus
                (chatMessage, mLoggedInUser.userId, timeToken, isForRead);
        publish(chatMessageStatus.roomId, chatMessageStatus);
    }

    public void fetchUserStateOnPresenceChannel() {
        ArrayList<String> chatRoomIdList = new ArrayList<>();
        chatRoomIdList.add(IMConstants.PRESENCE_CHANNEL_ID);
        fetchUserStateForChatRooms(chatRoomIdList);
    }

    public void fetchUserStateForParticularChatRoom(String chatRoomId) {
        ArrayList<String> chatRoomIdList = new ArrayList<>();
        chatRoomIdList.add(chatRoomId);
        fetchUserStateForChatRooms(chatRoomIdList);
    }

    public void clearAllChatData() {
        IMAppDatabase.getDatabase().clearAllChatData();
        IMPrefs.getInstance().clear();
        IMPubNubHelper.getInstance().disConnectPubNub();
        IMPubNubHelper.getInstance().destroyPubNub();
    }

    public void updateLoggedInuserStatus(boolean isLoggedin)
    {
        IMPushNotificationHelper.updateLoggedInuserStatus(mContext,isLoggedin);
    }

    public void updateInternetConnection(boolean isNetworkAvailaible)
    {this.isNetworkAvailaible=isNetworkAvailaible;
    }

    public void updateFreshLogin(boolean isLoggedIn){
        isFreshLogin=isLoggedIn;
    }


    public void fetchUserStateForChatRooms(ArrayList<String> chatRoomIdList) {
        IMPubNubHelper.getInstance().fetchHereNowState(chatRoomIdList);
    }

    private void publishChatRoomDetail(String channelId, IMChatRoomDetail chatRoomDetail) {
        publish(channelId, chatRoomDetail);
    }

    private void publish(String channelId, Object object) {
        String jsonString = mGson.toJson(object);
        JsonElement jsonElement = mGson.fromJson(jsonString, JsonElement.class);
        IMPubNubHelper.getInstance().publishData(channelId, jsonElement.getAsJsonObject());
    }

    private ArrayList<String> getActiveChatRoomIdList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(mLoggedInUser.userId);
        list.add(IMConstants.PRESENCE_CHANNEL_ID);
        list.addAll(getIMDatabase().getIMChatRoomDetailDao().getActiveChatRoomIdList(mLoggedInUser.userId));
        return list;
    }

    private ArrayList<String> getInActiveChatRoomIdList() {
        ArrayList<String> list = new ArrayList<>();
        list.addAll(getIMDatabase().getIMChatRoomDetailDao().getInActiveChatRoomIdList(mLoggedInUser.userId));
        return list;
    }

    private ArrayList<String> getAllChatRoomIdList() {
        ArrayList<String> list = new ArrayList<>();
        list.add(mLoggedInUser.userId);
        list.add(IMConstants.PRESENCE_CHANNEL_ID);
        list.addAll(getIMDatabase().getIMChatRoomDetailDao().getActiveChatRoomIdList(mLoggedInUser.userId));
        return list;
    }

    private void cacheChatRoomDetail(IMChatRoomDetail chatRoomDetail) {
        getIMDatabase().getIMChatRoomDetailDao().createOrUpdateChatRoomDetail(chatRoomDetail);
        IMPubNubHelper.getInstance().unSubscribeFromNonRelevantChannels(getInActiveChatRoomIdList());
        IMPubNubHelper.getInstance().subscribeToRelevantChannels(getActiveChatRoomIdList());
    }

    @Override
    public void onGetServerTime(long serverTimeToken) {
        mDeviceServerDeltaTime = IMUtils.getDeviceTimeInMillis() - (serverTimeToken / 10000);
        if (null != this.mIMChattingListener) {
            this.mIMChattingListener.onDeviceTimeUpdated();

        } else if (null != this.mIMBaseListener) {
            this.mIMBaseListener.onDeviceTimeUpdated();
        }
    }

    @Override
    public void onChatRoomDetailMessagePublished(IMChatRoomDetail chatRoomDetail) {
        cacheChatRoomDetail(chatRoomDetail);
        if (null != this.mIMChattingListener) {
            this.mIMChattingListener.onChatRoomDetailPublished(chatRoomDetail);
        }
    }

    @Override
    public void onChatMessagePublished(IMChatMessage chatMessage) {
        if (null != this.mIMChattingListener) {
            this.mIMChattingListener.onChatMessagePublished(chatMessage);
        }
    }

    @Override
    public void onChatRoomDetailMessageReceived(IMChatRoomDetail chatRoomDetail) {
        if (null != this.mIMChattingListener && null != this.currentRoomId
                && this.currentRoomId.equals(chatRoomDetail.roomId)) {

            if (!isCurrentRoomActive) {
                IMPushNotificationHandler.processChatRoomDetail(mContext, chatRoomDetail);
            }
            cacheChatRoomDetail(chatRoomDetail);
            this.mIMChattingListener.onChatRoomDetailReceived(chatRoomDetail);

        } else {
            IMPushNotificationHandler.processChatRoomDetail(mContext, chatRoomDetail);
            cacheChatRoomDetail(chatRoomDetail);
            if (null != this.mIMBaseListener) {
                this.mIMBaseListener.onChatRoomDetailReceived(chatRoomDetail);
            }
        }
    }

    @Override
    public void onChatRoomNameUpdateMessageReceived(IMChatRoomDetailUpdate chatRoomDetailUpdate) {
        if (null != this.mIMChattingListener && null != this.currentRoomId
                && this.currentRoomId.equals(chatRoomDetailUpdate.roomId)) {
            this.mIMChattingListener.onChatRoomNameUpdateMessageReceived(chatRoomDetailUpdate);

        } else if (null != this.mIMBaseListener) {
            this.mIMBaseListener.onChatRoomNameUpdateMessageReceived(chatRoomDetailUpdate);
        }

    }

    @Override
    public void onChatMessageReceived(IMChatMessage chatMessage) {
        // Marking chat message as seen
        getIMDatabase().getIMChatMessageDao().updateChatMessageSeenStatusByMsgId(chatMessage.msgId, false);

        // Publishing delivery status of the received message
        publishDeliveredOrReadStatus(chatMessage, false);


        if (null != this.mIMChattingListener && null != this.currentRoomId
                && this.currentRoomId.equals(chatMessage.roomId)) {

            if (!isCurrentRoomActive) {
                IMPushNotificationHandler.processChatMessage(mContext, chatMessage);

            } else {
                // Marking chat message as seen
                IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().
                        updateChatMessageSeenStatusByMsgId(chatMessage.msgId, true);

                // Publishing read status of the received message
                IMInstance.getInstance().publishDeliveredOrReadStatus(chatMessage, true);
            }

            this.mIMChattingListener.onChatMessageReceived(chatMessage);

        } else {
            IMPushNotificationHandler.processChatMessage(mContext, chatMessage);
            if (null != this.mIMBaseListener) {
                this.mIMBaseListener.onChatMessageReceived(chatMessage);
            }
        }

        if (null != mIMUnSeenChatMsgCountListener) {
            mIMUnSeenChatMsgCountListener.onChatMessageReceived(getIMDatabase().getIMChatMessageDao().
                    getTotalUnSeenChatMessageCount(), chatMessage);
        }
    }

    @Override
    public void onChatMessageStatusReceived(IMChatMessageStatus chatMessageStatus) {
        if (null != this.mIMChattingListener && null != this.currentRoomId
                && this.currentRoomId.equals(chatMessageStatus.roomId)) {
            this.mIMChattingListener.onChatMessageStatusReceived(chatMessageStatus);

        } else if (null != this.mIMBaseListener) {
            this.mIMBaseListener.onChatMessageStatusReceived(chatMessageStatus);
        }
    }

    @Override
    public void onGetTypingState(String roomId, String uuid, boolean isTyping) {
        if (null != this.mIMChattingListener && null != this.currentRoomId
                && this.currentRoomId.equals(roomId)) {
            this.mIMChattingListener.onChatTypingStatusReceived(roomId, uuid, isTyping);

        } else if (null != this.mIMBaseListener) {
            this.mIMBaseListener.onChatTypingStatusReceived(roomId, uuid, isTyping);
        }
    }

    @Override
    public void onGetOnlineState(String uuid, boolean isOnline) {
        if (null != mIMUserPresenceListener) {
            mIMUserPresenceListener.onUserStatusReceived(uuid, isOnline);
        }
    }
}
