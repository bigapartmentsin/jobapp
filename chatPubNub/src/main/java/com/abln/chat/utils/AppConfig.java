package com.abln.chat.utils;

import android.app.Application;
import android.content.Context;

public class AppConfig extends Application {


    private static AppConfig mAppConfig;
    private static Context mApplicationContext;

    public static Context getContext() {
        return mApplicationContext;
    }



    public static AppConfig getInstance() {
        return mAppConfig;
    }

}
