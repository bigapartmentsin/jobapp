package com.abln.futur.common;

import android.util.Log;

import static com.abln.futur.common.AppConfig.isReleaseBuild;

public class FLog {
    private static boolean RELEASE_BUILD = isReleaseBuild();

    public static void i(String TAG, String log) {
        Log.i("Futur::" + TAG, log);
    }

    public static void d(String TAG, String log) {
        if (!RELEASE_BUILD)
            Log.d("Futur::" + TAG, log);
    }

    public static void w(String TAG, String log) {
        Log.w("Futur::" + TAG, log);
    }

    public static void e(String TAG, String log) {
        Log.e("Futur::" + TAG, log);
        if (isReleaseBuild()) {
        }
    }
}
