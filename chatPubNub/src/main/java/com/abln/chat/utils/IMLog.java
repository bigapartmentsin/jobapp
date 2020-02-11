package com.abln.chat.utils;


import com.abln.chat.BuildConfig;

public class IMLog {
    static final boolean LOG = BuildConfig.DEBUG;
    static final boolean LOG_I = true;
    static final boolean LOG_E = true;
    static final boolean LOG_D = true;
    static final boolean LOG_V = true;
    static final boolean LOG_W = true;

    public static void i(String tag, String msg) {
        if (LOG && LOG_I) {
            android.util.Log.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (LOG && LOG_E) {
            android.util.Log.e(tag, msg);
        }
    }

    public static void e(String tag, String msg, Throwable tr) {
        if (LOG && LOG_E) {
            android.util.Log.e(tag, msg, tr);
        }
    }

    public static void d(String tag, String msg) {
        if (LOG && LOG_D) {
            android.util.Log.d(tag, msg);
        }
    }

    public static void v(String tag, String msg) {
        if (LOG && LOG_V) {
            android.util.Log.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (LOG && LOG_W) {
            android.util.Log.w(tag, msg);
        }
    }
}
