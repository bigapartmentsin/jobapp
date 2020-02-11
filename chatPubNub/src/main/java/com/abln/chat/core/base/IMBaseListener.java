package com.abln.chat.core.base;

import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;


public interface IMBaseListener {

    void onDeviceTimeUpdated();

    void onChatMessageReceived(IMChatMessage chatMessage);

    void onChatRoomDetailReceived(IMChatRoomDetail chatRoomDetail);

    void onChatRoomNameUpdateMessageReceived(IMChatRoomDetailUpdate chatRoomDetailUpdate);

    void onChatMessageStatusReceived(IMChatMessageStatus chatMessageStatus);

    void onChatTypingStatusReceived(String roomId, String userId, Boolean isTyping);
}
