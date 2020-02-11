package com.abln.chat.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class IMNetwork {

    public static final String TAG = IMNetwork.class.getSimpleName();

    public static boolean isConnected(Context context) {
        boolean isMobileConn = false;
        boolean isWifiConn = false;
        try {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            isWifiConn = networkInfo.isConnected();
            networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            isMobileConn = networkInfo.isConnected();

            if (isWifiConn || isMobileConn) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {

            if (isWifiConn || isMobileConn) {
                return true;
            } else {
                return false;
            }
        }
    }
}
