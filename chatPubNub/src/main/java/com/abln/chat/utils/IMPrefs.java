package com.abln.chat.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.abln.chat.BuildConfig;


public class IMPrefs {
    private static final String TAG = IMPrefs.class.getSimpleName();

    private static IMPrefs sInstance;
    private final SharedPreferences mPref;
    private static final String PREFIX = BuildConfig.APPLICATION_ID + ".";
    private static final String FCM_REGISTRATION_TOKEN = PREFIX + "FCM REGISTRATION ID";
    private static final String LOGIN_USER_ID = PREFIX + "LOGIN USER ID";

    private IMPrefs(Context context) {
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static synchronized void initializeInstance(Context context) {
        sInstance = new IMPrefs(context);
    }

    public static synchronized IMPrefs getInstance() {
        if (null == sInstance) {
            throw new IllegalStateException(IMPrefs.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public void setFCMRegToken(String fcmRegToken) {
        mPref.edit().putString(FCM_REGISTRATION_TOKEN, fcmRegToken).apply();
    }

    public String getFCMRegToken() {
        return mPref.getString(FCM_REGISTRATION_TOKEN, null);
    }

    public void setLoginUserID(String loginUserId) {
        mPref.edit().putString(LOGIN_USER_ID, loginUserId).apply();
    }

    public String getLoginUserID() {
        return mPref.getString(LOGIN_USER_ID, null);
    }

    public boolean clear() {
        return mPref.edit().clear().commit();
    }
}
