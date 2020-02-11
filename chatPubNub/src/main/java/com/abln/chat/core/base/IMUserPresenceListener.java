package com.abln.chat.core.base;

public interface IMUserPresenceListener {
    void onUserStatusReceived(String userId, Boolean isOnline);
}
