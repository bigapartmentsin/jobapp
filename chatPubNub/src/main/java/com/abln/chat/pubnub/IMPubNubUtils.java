package com.abln.chat.pubnub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.abln.chat.IMInstance;
import com.abln.chat.core.model.IMAlertMessage;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.utils.IMConstants;


public class IMPubNubUtils {

    private static final String MESSAGE = "message";
    private static final String DATA = "data";
    private static final String PN_GCM = "pn_gcm";

    private static final String CONTENT_AVAILABLE = "content-available";
    private static final String SOUND = "sound";
    private static final String ALERT = "alert";
    private static final String APS = "aps";
    private static final String PN_APNS = "pn_apns";


    public static IMChatMessageStatus getChatMessageStatus(IMChatMessage chatMessage, String loggedInUserId,
                                                           long timeToken, boolean isForRead) {

        IMChatMessageStatus chatMessageStatus = new IMChatMessageStatus();
        chatMessageStatus.origin = IMConstants.CHAT_MESSAGE_ORIGIN_CHAT;
        chatMessageStatus.msgType = IMConstants.CHAT_MESSAGE_TYPE_STATUS;
        chatMessageStatus.msgSenderId = loggedInUserId;
        chatMessageStatus.roomId = chatMessage.roomId;
        chatMessageStatus.msgId = chatMessage.msgId;
        chatMessageStatus.msgOwnerId = chatMessage.msgSenderId;
        chatMessageStatus.msgVersion = chatMessage.msgVersion;
        chatMessageStatus.timeToken = timeToken;

        if (isForRead) {
            chatMessageStatus.msgStatus = IMConstants.CHAT_MESSAGE_READ_STATUS;
        } else {
            chatMessageStatus.msgStatus = IMConstants.CHAT_MESSAGE_DELIVERED_STATUS;
        }

        return chatMessageStatus;

    }

    static JsonObject getPushNotificationWrapData(JsonObject message) {

        JsonObject jsonObject = new JsonObject();
        try {
            jsonObject.add(PN_GCM, getJsonDataObjForPNGCM(message));
            jsonObject.add(PN_APNS, getJsonAPSObjForPNAPNS(message));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject;

    }


    private static JsonObject getJsonDataObjForPNGCM(JsonObject message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(DATA, getJsonMessageObjForDATA(message));
        return jsonObject;
    }

    private static JsonObject getJsonMessageObjForDATA(JsonObject message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(MESSAGE, message);
        return jsonObject;
    }

    private static JsonObject getJsonAPSObjForPNAPNS(JsonObject message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add(APS, getJsonMessageObjForAPS(message));
        return jsonObject;
    }

    private static JsonObject getJsonMessageObjForAPS(JsonObject message) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CONTENT_AVAILABLE, true);
        jsonObject.addProperty(SOUND, "default");
        jsonObject.add(ALERT, getAlertNotification(message));
        jsonObject.add(MESSAGE, message);
        return jsonObject;
    }

    private static JsonObject getAlertNotification(JsonObject message) {
        Gson mGson = new Gson();
        JsonElement jsonElement = null;
        //  AccessToken accessToken = DataParser.parseJson(responseString, AccessToken.class);
        IMChatMessage imChatMessage = new GsonBuilder().create().fromJson(message, IMChatMessage.class);
        if (imChatMessage == null)
            return null;
        try {

            IMAlertMessage alert = new IMAlertMessage();
            if (imChatMessage.msgType.equalsIgnoreCase("IMAGE")) {
                alert.setBody("Image");
            } else if (imChatMessage.msgType.equalsIgnoreCase("VIDEO")) {
                alert.setBody("Video (" + imChatMessage.msgMultimediaInfo.videoDuration + ")");
            } else {
                alert.setBody(imChatMessage.msgText);
            }
            alert.setTitle(IMInstance.getInstance().getLoggedInUser().userName);
            String jsonString = mGson.toJson(alert);
            jsonElement = mGson.fromJson(jsonString, JsonElement.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonElement.getAsJsonObject();
    }

    public static JsonObject getMessageDataFromPNGCM(JsonObject jsonObject) {
        JsonObject jsonMessageObj = null;
        try {
            JsonObject jsonPNGCMObj = jsonObject.getAsJsonObject(PN_GCM);
            JsonObject jsonDataObj = jsonPNGCMObj.getAsJsonObject(DATA);
            jsonMessageObj = jsonDataObj.getAsJsonObject(MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonMessageObj;
    }
}
