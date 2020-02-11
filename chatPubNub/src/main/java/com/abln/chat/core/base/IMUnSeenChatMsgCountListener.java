package com.abln.chat.core.base;

import com.abln.chat.core.model.IMChatMessage;


public interface IMUnSeenChatMsgCountListener {
    void onChatMessageReceived(int totalUnSeenChatMsgCount, IMChatMessage latestChatMessage);
}
