package com.abln.chat.ui.gcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.abln.chat.IMInstance;


public class DeviceTimeChangedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (null != IMInstance.getInstance()) {
            IMInstance.getInstance().updateServerDeviceDeltaTime();
        }
    }


}
