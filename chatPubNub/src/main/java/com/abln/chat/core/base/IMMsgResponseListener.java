package com.abln.chat.core.base;

import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;

public interface IMMsgResponseListener {
    void onGetServerTime(long serverTimeToken);

    void onChatRoomDetailMessagePublished(IMChatRoomDetail chatRoomDetail);

    void onChatMessagePublished(IMChatMessage chatMessage);

    void onChatRoomDetailMessageReceived(IMChatRoomDetail chatRoomDetail);

    void onChatRoomNameUpdateMessageReceived(IMChatRoomDetailUpdate chatRoomDetailUpdate);

    void onChatMessageReceived(IMChatMessage chatMessage);

    void onChatMessageStatusReceived(IMChatMessageStatus chatMessageStatus);

    void onGetTypingState(String roomId, String uuid, boolean isTyping);

    void onGetOnlineState(String uuid, boolean isOnline);
}
