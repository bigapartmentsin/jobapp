package com.abln.chat.core.base;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.abln.chat.core.db.IMAppDatabase;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMConstants;

public class IMMsgResponseHandler {
    private IMAppDatabase mAppDatabase;
    private IMMsgResponseListener mIMMsgResponseListener;
    private IMChatUser mLoggedInUser;

    private Gson mGson;

    public IMMsgResponseHandler(IMMsgResponseListener msgResponseListener, IMAppDatabase appDatabase, IMChatUser loggedInUser) {
        mIMMsgResponseListener = msgResponseListener;
        mAppDatabase = appDatabase;
        mLoggedInUser = loggedInUser;

        mGson = new Gson();
    }

    public void onGetServerTime(long serverTimeToken) {
        mIMMsgResponseListener.onGetServerTime(serverTimeToken);
    }

    public void onMessagePublished(JsonObject jsonObject) {
        String msgType = jsonObject.get(IMConstants.CHAT_MESSAGE_TYPE).getAsString();

        if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_ADMIN)) {
            handlePublishedChatRoomDetailMessage(jsonObject);

        } else if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_ROOM_NAME_UPDATE)) {
            handlePublishedChatRoomNameUpdateMessage(jsonObject);

        } else if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_TEXT) ||
                msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_IMAGE) ||
                msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
            handlePublishedChatMessage(jsonObject);

        } else if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_STATUS)) {
            handlePublishedChatMessageStatus(jsonObject);
        }
    }

    private void handlePublishedChatRoomDetailMessage(JsonObject jsonObject) {
        IMChatRoomDetail chatRoomDetail = mGson.fromJson(jsonObject, IMChatRoomDetail.class);
        if (isLoggedInUser(chatRoomDetail.adminId)) {
            mIMMsgResponseListener.onChatRoomDetailMessagePublished(chatRoomDetail);
        }
    }

    private void handlePublishedChatRoomNameUpdateMessage(JsonObject jsonObject) {
        IMChatRoomDetailUpdate chatRoomDetailUpdate = mGson.fromJson(jsonObject, IMChatRoomDetailUpdate.class);
        mAppDatabase.getIMChatRoomDetailDao().updateChatRoomName(chatRoomDetailUpdate.roomId, chatRoomDetailUpdate.roomName);
    }

    private void handlePublishedChatMessage(JsonObject jsonObject) {
        IMChatMessage chatMessage = mGson.fromJson(jsonObject, IMChatMessage.class);
        chatMessage.isChatMsgSent = true;

        mAppDatabase.getIMChatMessageDao().updateChatMessageSentStatusAndTimeByMsgId
                (chatMessage.msgId, chatMessage.isChatMsgSent, chatMessage.timeToken);
        mIMMsgResponseListener.onChatMessagePublished(chatMessage);
    }

    private void handlePublishedChatMessageStatus(JsonObject jsonObject) {
        IMChatMessageStatus chatMessageStatus = mGson.fromJson(jsonObject, IMChatMessageStatus.class);
        if (isLoggedInUser(chatMessageStatus.msgSenderId) && !isLoggedInUser(chatMessageStatus.msgOwnerId)) {
            // updating the status message in db
            mAppDatabase.getIMChatMsgMemberDao().updateChatMessageStatus(chatMessageStatus);
        }
    }

    public void onMessageReceived(JsonObject jsonObject) {
        String msgType = jsonObject.get(IMConstants.CHAT_MESSAGE_TYPE).getAsString();

        if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_ADMIN)) {
            handleReceivedChatRoomDetailMessage(jsonObject);

        } else if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_ROOM_NAME_UPDATE)) {
            handleReceivedChatRoomNameUpdateMessage(jsonObject);

        } else if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_TEXT) ||
                msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_IMAGE) ||
                msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
            handleReceivedChatMessage(jsonObject);

        } else if (msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_STATUS)) {
            handleReceivedChatMessageStatus(jsonObject);
        }
    }

    private void handleReceivedChatRoomDetailMessage(JsonObject jsonObject) {
        IMChatRoomDetail chatRoomDetail = mGson.fromJson(jsonObject, IMChatRoomDetail.class);
        if (!isLoggedInUser(chatRoomDetail.adminId)) {
            mIMMsgResponseListener.onChatRoomDetailMessageReceived(chatRoomDetail);
        }
    }

    private void handleReceivedChatRoomNameUpdateMessage(JsonObject jsonObject) {
        IMChatRoomDetailUpdate chatRoomDetailUpdate = mGson.fromJson(jsonObject, IMChatRoomDetailUpdate.class);
        mAppDatabase.getIMChatRoomDetailDao().updateChatRoomName(chatRoomDetailUpdate.roomId, chatRoomDetailUpdate.roomName);
        mIMMsgResponseListener.onChatRoomNameUpdateMessageReceived(chatRoomDetailUpdate);
    }

    private void handleReceivedChatMessage(JsonObject jsonObject) {
        IMChatMessage chatMessage = mGson.fromJson(jsonObject, IMChatMessage.class);
        if (!isLoggedInUser(chatMessage.msgSenderId)) {
            // Updating room deleted status, in case chat history has been deleted before
            mAppDatabase.getIMChatRoomDetailDao().updateChatRoomDeleteStatus(chatMessage.roomId, false);

            // Caching the received message
            mAppDatabase.getIMChatMessageDao().insertChatMessage(chatMessage);

            mIMMsgResponseListener.onChatMessageReceived(chatMessage);
        }
    }

    private void handleReceivedChatMessageStatus(JsonObject jsonObject) {
        IMChatMessageStatus chatMessageStatus = mGson.fromJson(jsonObject, IMChatMessageStatus.class);

        // Handling the Delivery or Read status for the messages sent by logged in user
        if (!isLoggedInUser(chatMessageStatus.msgSenderId) && isLoggedInUser(chatMessageStatus.msgOwnerId)) {

            // updating the status message in db
            mAppDatabase.getIMChatMsgMemberDao().updateChatMessageStatus(chatMessageStatus);

            mIMMsgResponseListener.onChatMessageStatusReceived(chatMessageStatus);

            // Handling the Delivery or Read status for the messages received by logged in user
        } else if (isLoggedInUser(chatMessageStatus.msgSenderId) && !isLoggedInUser(chatMessageStatus.msgOwnerId)) {
            // updating the status message in db
            mAppDatabase.getIMChatMsgMemberDao().updateChatMessageStatus(chatMessageStatus);
        }
    }

    public void onTypingStatusReceived(String chatRoomId, String userId, JsonObject jsonObject) {
        mIMMsgResponseListener.onGetTypingState(chatRoomId, userId, jsonObject.get(IMConstants.IS_TYPING).getAsBoolean());
    }

    public void onPresenceEventReceived(String event, String userId) {
        if (null != event) {
            if (event.equalsIgnoreCase(IMConstants.JOIN)) {
                mIMMsgResponseListener.onGetOnlineState(userId, true);

            } else if (event.equalsIgnoreCase(IMConstants.LEAVE) ||
                    event.equalsIgnoreCase(IMConstants.TIMEOUT)) {
                mIMMsgResponseListener.onGetOnlineState(userId, false);
            }
        } else {
            mIMMsgResponseListener.onGetOnlineState(userId, true);
        }
    }

    private boolean isLoggedInUser(String userId) {
        return mLoggedInUser.userId.equals(userId);
    }

}
