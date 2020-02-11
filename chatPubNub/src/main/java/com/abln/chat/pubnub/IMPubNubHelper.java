package com.abln.chat.pubnub;

import android.util.Log;

import com.abln.chat.ui.Network.IMNetworkResponse;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.abln.chat.core.base.IMMsgResponseHandler;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMPrefs;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNLogVerbosity;
import com.pubnub.api.enums.PNPushType;
import com.pubnub.api.enums.PNReconnectionPolicy;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.PNTimeResult;
import com.pubnub.api.models.consumer.history.PNHistoryResult;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;
import com.pubnub.api.models.consumer.push.PNPushAddChannelResult;
import com.pubnub.api.models.consumer.push.PNPushRemoveChannelResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class IMPubNubHelper {
    public static final String TAG = IMPubNubHelper.class.getSimpleName();

    private static IMPubNubHelper mIMPubNubHelper;

    private PubNub mPubNub;
    private String mPublishKey;
    private String mSubscribeKey;
    private int mPresenceTimeOut;
    private IMChatUser mLoggedInUser;

    private IMMsgResponseHandler mIMMsgResponseHandler;

    private static final String PRESENCE_CHANNEL_POSTFIX = "-pnpres";


    private IMPubNubHelper() {

    }

    public static IMPubNubHelper getInstance() {
        if (null == mIMPubNubHelper) {
            mIMPubNubHelper = new IMPubNubHelper();
        }
        // Return the instance
        return mIMPubNubHelper;
    }

    public void initPubNub(String publishKey, String subscribeKey, int presenceTimeOut,
                           IMChatUser loggedInUser, IMMsgResponseHandler msgResponseHandler) {

        this.mPublishKey = publishKey;
        this.mSubscribeKey = subscribeKey;
        this.mPresenceTimeOut = presenceTimeOut;
        this.mLoggedInUser = loggedInUser;
        this.mIMMsgResponseHandler = msgResponseHandler;

        mPubNub = new PubNub(preparePNConfiguration());
        mPubNub.removeListener(getSubscribeCallback());
        mPubNub.addListener(getSubscribeCallback());
    }

    private PNConfiguration preparePNConfiguration() {
        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setUuid(mLoggedInUser.userId);
        pnConfiguration.setPublishKey(mPublishKey);
        pnConfiguration.setSubscribeKey(mSubscribeKey);
        pnConfiguration.setPresenceTimeout(mPresenceTimeOut);

        pnConfiguration.setSecure(true);
        pnConfiguration.setReconnectionPolicy(PNReconnectionPolicy.LINEAR);
        pnConfiguration.setLogVerbosity(PNLogVerbosity.BODY);
        return pnConfiguration;
    }

    public void fetchPubNubServerTime() {
        if (mPubNub != null)
            mPubNub.time().async(getServerTimeCallback());
    }

    public void disConnectPubNub() {

        mPubNub.disconnect();
    }

    public void destroyPubNub() {
        mPubNub.destroy();
    }

    public void publishData(String channelId, JsonObject data) {

        data = IMPubNubUtils.getPushNotificationWrapData(data);
        mPubNub.publish().message(data)
                .channel(channelId)
                .shouldStore(true)
                .usePOST(true)
                .async(getMessagePublishCallback(data));

    }

    public void getChatHistory(String channelID, long endTimeToken, int count, final IMNetworkResponse mNetworkResponse) {
        if(mPubNub == null)
            return;

        mPubNub.history()
                .channel(channelID)
                .end(endTimeToken)
                .count(count)
                .reverse(false)
                .async(new PNCallback<PNHistoryResult>() {
                    @Override
                    public void onResponse(PNHistoryResult result, PNStatus status) {
                        if(result!=null && status.getStatusCode() == 200){
                            mNetworkResponse.onProcessFinish(result.getMessages());
                        }else{
                            mNetworkResponse.onProcessFinish(null);
                        }

                    }
                });
    }

    public void setPresenceState(List<String> channels, HashMap userState, String uuid) {
        if (mPubNub == null)
            return;
        mPubNub.setPresenceState()
                .channels(channels) // apply on those channel groups
                .state(userState) // the new state
                .uuid(uuid)
                .async(getPresenceStateCallback());
    }

    public void fetchHereNowState(List<String> channelList) {
        if (mPubNub == null)
            return;
        mPubNub.hereNow()
                .channels(channelList) // channels to fetch state for
                .includeState(true) // include state with request (false by default)
                .includeUUIDs(true) // if false, only shows occupancy count
                .async(getHereNowCallback());
    }


    // subscribe to rooms where logged in user is part of
    public void subscribeToParticularChannels(String channel) {
        ArrayList<String> list = new ArrayList<>();
        list.add(channel);
        subscribeToRelevantChannels(list);
    }

    // un subscribe from rooms where logged in user is not part of
    public void unSubscribeFromParticularChannels(String channel) {
        ArrayList<String> list = new ArrayList<>();
        list.add(channel);
        unSubscribeFromNonRelevantChannels(list);
    }

    // subscribe to rooms where logged in user is part of
    public void subscribeToRelevantChannels(ArrayList<String> channelList) {
        if(mPubNub!=null)
        mPubNub.subscribe().channels(channelList).withPresence().execute();
        addPushNotificationsOnChannels(channelList);

    }

    // un subscribe from rooms where logged in user is not part of
    public void unSubscribeFromNonRelevantChannels(ArrayList<String> channelList) {
        mPubNub.unsubscribe().channels(channelList).execute();
        removePushNotificationsFromChannels(channelList);
    }

    // un subscribe from all rooms
    public void unSubscribeFromAllChannels(ArrayList<String> channelList) {
        // un subscribe from all  channels
        mPubNub.unsubscribe().channels(channelList).execute();
        removePushNotificationsFromChannels(channelList);
    }

    public void addPushNotificationForAllSubscribedChatRooms(ArrayList<String> channelList) {
        addPushNotificationsOnChannels(channelList);
    }

    public void addPushNotificationForParticularChatRoom(String chatRoomId) {
        ArrayList<String> channelList = new ArrayList<>();
        channelList.add(chatRoomId);
        addPushNotificationsOnChannels(channelList);
    }

    public void removePushNotificationForAllSubscribedChatRooms(ArrayList<String> channelList) {
        removePushNotificationsFromChannels(channelList);
    }

    public void removePushNotificationForParticularChatRoom(String chatRoomId) {
        ArrayList<String> channelList = new ArrayList<>();
        channelList.add(chatRoomId);
        removePushNotificationsFromChannels(channelList);
    }

    private void addPushNotificationsOnChannels(ArrayList<String> channelList) {
        String deviceId = IMPrefs.getInstance().getFCMRegToken();
        if (null == deviceId) {
            return;
        }

        mPubNub.addPushNotificationsOnChannels()
                .pushType(PNPushType.GCM)
                .channels(channelList)
                .deviceId(deviceId)
                .async(getPushChannelAddCallback());
    }

    private void removePushNotificationsFromChannels(ArrayList<String> channelList) {
        String deviceId = IMPrefs.getInstance().getFCMRegToken();
        if (null == deviceId) {
            return;
        }

        mPubNub.removePushNotificationsFromChannels()
                .pushType(PNPushType.GCM)
                .channels(channelList)
                .deviceId(deviceId)
                .async(getPushChannelRemoveCallback());

    }

    private boolean isLoggedInUser(String userId) {

        return mLoggedInUser.userId.equals(userId);
    }

    private PNCallback<PNTimeResult> getServerTimeCallback() {
        return new PNCallback<PNTimeResult>() {
            @Override
            public void onResponse(PNTimeResult result, PNStatus status) {
                try {
                    mIMMsgResponseHandler.onGetServerTime(result.getTimetoken());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private PNCallback<PNPublishResult> getMessagePublishCallback(final JsonObject jsonObject) {

        return new PNCallback<PNPublishResult>() {
            @Override
            public void onResponse(PNPublishResult result, PNStatus status) {
                // handle publish result, status always present, result if successful
                if (null != result && !status.isError()) {
                    mIMMsgResponseHandler.onMessagePublished(IMPubNubUtils.getMessageDataFromPNGCM(jsonObject));
                }
            }
        };

    }

    private PNCallback<PNSetStateResult> getPresenceStateCallback() {

        return new PNCallback<PNSetStateResult>() {
            @Override
            public void onResponse(PNSetStateResult result, PNStatus status) {
                // handle publish result, status always present, result if successful
                // status.isError to see if error happened
            }
        };

    }

    public PNCallback<PNPushAddChannelResult> getPushChannelAddCallback() {

        return new PNCallback<PNPushAddChannelResult>() {
            @Override
            public void onResponse(PNPushAddChannelResult result, PNStatus status) {

            }
        };

    }

    public PNCallback<PNPushRemoveChannelResult> getPushChannelRemoveCallback() {

        return new PNCallback<PNPushRemoveChannelResult>() {
            @Override
            public void onResponse(PNPushRemoveChannelResult result, PNStatus status) {

            }
        };

    }

    private PNCallback<PNHereNowResult> getHereNowCallback() {

        return new PNCallback<PNHereNowResult>() {
            @Override
            public void onResponse(PNHereNowResult result, PNStatus status) {
                try {
                    Map map = result.getChannels();
                    Iterator it = map.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        PNHereNowChannelData pnHereNowChannelData = (PNHereNowChannelData) pair.getValue();
                        if (null == pnHereNowChannelData) {
                            return;
                        }

                        List<PNHereNowOccupantData> occupantList = pnHereNowChannelData.getOccupants();
                        for (PNHereNowOccupantData pnHereNowOccupantData : occupantList) {
                            if (!isLoggedInUser(pnHereNowOccupantData.getUuid())) {

                                if (map.containsKey(IMConstants.PRESENCE_CHANNEL_ID)) {
                                    mIMMsgResponseHandler.onPresenceEventReceived(null, pnHereNowOccupantData.getUuid());

                                } else if (null != pnHereNowOccupantData.getState()) {
                                    JsonObject jsonObject = pnHereNowOccupantData.getState().getAsJsonObject();
                                    mIMMsgResponseHandler.onTypingStatusReceived(pnHereNowChannelData.getChannelName(),
                                            pnHereNowOccupantData.getUuid(), jsonObject);
                                }
                            }
                        }
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };

    }

    private SubscribeCallback getSubscribeCallback() {

        return new SubscribeCallback() {
            @Override
            public void status(PubNub pubnub, PNStatus status) {

            }

            @Override
            public void message(PubNub pubnub, PNMessageResult message) {
                mIMMsgResponseHandler.onMessageReceived(IMPubNubUtils.getMessageDataFromPNGCM(
                        message.getMessage().getAsJsonObject()));
            }

            @Override
            public void presence(PubNub pubnub, PNPresenceEventResult presence) {
                try {
                    if (!isLoggedInUser(presence.getUuid()) && null != presence.getState()) {
                        JsonObject jsonObject = presence.getState().getAsJsonObject();
                        String subscribedChannel = presence.getSubscribedChannel();
                        String roomId = subscribedChannel.substring(0, subscribedChannel.indexOf(PRESENCE_CHANNEL_POSTFIX));
                        mIMMsgResponseHandler.onTypingStatusReceived(roomId, presence.getUuid(), jsonObject);

                    } else if (null != presence.getEvent()) {
                        mIMMsgResponseHandler.onPresenceEventReceived(presence.getEvent(), presence.getUuid());
                    }

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };


    }
}
