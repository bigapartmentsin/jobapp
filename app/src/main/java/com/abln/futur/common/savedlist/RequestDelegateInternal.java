package com.abln.futur.common.savedlist;

public interface RequestDelegateInternal {
    void run(long response, int errorCode, String errorText, int networkType);
}
