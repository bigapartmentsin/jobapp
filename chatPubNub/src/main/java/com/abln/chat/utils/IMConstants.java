package com.abln.chat.utils;

public class IMConstants {
    // Activity request code's
    public static int ADD_USER_REQUEST_CODE = 100;
    public static int CHAT_INFO_REQUEST_CODE = 110;
    public static int REQUEST_FORWARD_CHAT = 4000;

    public static final String NETWORK_CHANGE_BROADCAST_ACTION = "NETWORK CHANGE BROADCAST ACTION";

    // chat related
    public static final String CHAT_ROOM_ID = "CHAT ROOM ID";
    public static final String CHAT_MESSAGE_ID = "CHAT MESSAGE ID";
    public static final String CHAT_ROOM_NAME = "CHAT ROOM NAME";
    public static final String CHAT_ROOM_TYPE = "CHAT ROOM TYPE";
    public static final String CHAT_FORWARDED_MESSAGE = "CHAT FORWARDED MESSAGE";
    public static final String IS_CHAT_FORWARDED = "IS CHAT FORWARDED";
    public static final String CHAT_CONTEXT_ID = "CHAT ROOM CONTEXT ID";
    public static final String CHAT_CONTEXT_NAME = "CHAT ROOM CONTEXT NAME";
    public static final String CHAT_MSG_ID="CHAT MSG ID";

    // Multimedia Related
    public static final String MULTIMEDIA_CHAT_MSG = "MULTIMEDIA CHAT MSG";
    public static final String MULTIMEDIA_FILE_ROOM_ID = "MULTIMEDIA FILE ROOM ID";
    public static final String FILE_PATH = "FILE PATH";
    public static final String FILE_TYPE = "FILE TYPE";
    public static final String FILE_TYPE_IMAGE = "FILE TYPE IMAGE";
    public static final String FILE_TYPE_VIDEO = "FILE TYPE VIDEO";
    public static final String FILE_TYPE_DOC = "FILE TYPE DOC";
    public static final String TRANSFER_ID = "TRANSFER ID";
    public static final String TRANSFER_TYPE = "TRANSFER TYPE";
    public static final String TRANSFER_TYPE_UPLOAD = "TRANSFER TYPE UPLOAD";
    public static final String TRANSFER_TYPE_DOWNLOAD = "TRANSFER TYPE DOWNLOAD";
    public static final String BYTES_TRANSFER_CURRENT = "BYTES TRANSFER CURRENT";
    public static final String BYTES_TRANSFER_TOTAL = "BYTES TRANSFER TOTAL";

    // User list screen
    public static final String ADD_USER_ID_LIST = "ADD USER ID LIST";
    public static final String EXITING_USERS_ID_LIST = "EXITING USERS ID LIST";


    //******************************************* VALUES *******************************************


    // data for Chat origin and version Values
    public static final String CHAT_MESSAGE_ORIGIN_CHAT = "CHAT";
    public static final String CHAT_VERSION = "1.0";

    // different Chat Message Type Values
    public static final String CHAT_MESSAGE_TYPE_ADMIN = "Admin";
    public static final String CHAT_MESSAGE_TYPE_TEXT = "Text";
    public static final String CHAT_MESSAGE_TYPE_VIDEO = "VIDEO";
    public static final String CHAT_MESSAGE_TYPE_AUDIO = "AUDIO";
    public static final String CHAT_MESSAGE_TYPE_IMAGE = "IMAGE";
    public static final String CHAT_MESSAGE_TYPE_STATUS = "Status";
    public static final String CHAT_MESSAGE_TYPE_ROOM_NAME_UPDATE = "RoomNameUpdate";

    // different Chat Room Type Values
    public static final String CHAT_ROOM_TYPE_ONE_TO_ONE = "oneToOne";
    public static final String CHAT_ROOM_TYPE_GROUP = "group";

    // different Chat Message Status Values
    public static final String CHAT_MESSAGE_DELIVERED_STATUS = "delivered";
    public static final String CHAT_MESSAGE_READ_STATUS = "read";

    // data for user state Values
    public static final int TYPING_TIMEOUT = 2000;
    public static final String IS_TYPING = "isTyping";

    // data for Chat msg Type Value
    public static final String CHAT_MESSAGE_TYPE = "msgType";

    // data for Chat user presence Value
    public static final String PRESENCE_CHANNEL_ID = "com-futur-abln";

    // data for user events Values
    public static final String LEAVE = "leave";
    public static final String JOIN = "join";
    public static final String TIMEOUT = "timeout";

}
