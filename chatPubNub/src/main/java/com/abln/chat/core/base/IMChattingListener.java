package com.abln.chat.core.base;

import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatRoomDetail;

public interface IMChattingListener extends IMBaseListener {

    void onChatRoomDetailPublished(IMChatRoomDetail chatRoomDetail);

    void onChatMessagePublished(IMChatMessage chatMessage);

}
