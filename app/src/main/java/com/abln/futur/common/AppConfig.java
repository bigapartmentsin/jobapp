package com.abln.futur.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.abln.chat.IMConfiguration;
import com.abln.chat.IMInstance;
import com.abln.chat.core.base.IMBaseListener;
import com.abln.chat.core.model.IMChatUser;
import com.abln.futur.BuildConfig;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

//Chat


/**
 * Created by Bharath on 06/22/2019.
 */
public class AppConfig extends Application {
    public static final boolean FEATURE_SECURE_CONNECTION = AppConfig.isReleaseBuild();
    public static final String TAG = AppConfig.class.getSimpleName();
    public static boolean FEATURE_CHAT = false;
    public static boolean FEATURE_SCREENERS = false;
    public static String PN_PUB = "--";
    public static String PN_SUB = "--";
    private static AppConfig mAppConfig;
    private static Context mApplicationContext;
    private PrefManager mLocalSession;

    public static Context getContext() {
        return mApplicationContext;
    }

    //TODO: Chat module
    public static void saveChatInfo(final ArrayList<IMChatUser> chatUsers, final IMBaseListener imBaseListener) {
        final IMChatUser chatUser = new IMChatUser();
        PrefManager mLocalSession = new PrefManager();
        String userId = new PrefManager().getMobilenumber();

//        String firstName = "Peter";
//        chatUser.userId = "2d9150abe953194249e5e9cf08fa376d";

//        chatUser.userId = "ed3202810f1407e97b53fb759d631c29";
        String firstName = mLocalSession.getUserName();
        chatUser.userId = mLocalSession.getApikey();

        if (null != firstName) {
            chatUser.userName = firstName;
            chatUsers.add(chatUser);
        }
        boolean isOldDataPresent = clearOldUserChatData();
        IMConfiguration configuration = new IMConfiguration(chatUsers, chatUser, "Title",
                FirebaseInstanceId.getInstance().getToken(),
                PN_SUB,
                PN_PUB,
                60,
                NetworkConfig.S3_EP_URL,
                NetworkConfig.S3_IMAGES,
                NetworkConfig.S3_VIDEOS,
                NetworkConfig.S3_DOC,
                NetworkConfig.S3_S_KEY,
                NetworkConfig.S3_A_KEY);

        IMInstance.getInstance().setIMConfiguration(configuration, imBaseListener);
        if (isOldDataPresent) {
            IMInstance.getInstance().clearAllChatData();
            IMInstance.getInstance().setIMConfiguration(configuration, imBaseListener);
        }
    }

    /**
     * @return AppConfig instance.
     */

    public static AppConfig getInstance() {
        return mAppConfig;
    }

    /**
     * @return true if APK is signed, or else return false.
     */
    public static boolean isReleaseBuild() {
        return !BuildConfig.DEBUG;
    }

    public static boolean isTablet() {
        return false;
//        return (mAppConfig.getResources().getConfiguration().screenLayout
//                & Configuration.SCREENLAYOUT_SIZE_MASK)
//                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    //Api to check whether the app is in background
    public static boolean isAppIsInBackground() {
        boolean isInBackground = true;
        try {
            ActivityManager am = (ActivityManager) mAppConfig.getSystemService(ACTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
                List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
                for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                    if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                        for (String activeProcess : processInfo.pkgList) {
                            if (activeProcess.equals(mAppConfig.getPackageName())) {
                                isInBackground = false;
                            }
                        }
                    }
                }
            } else {
                List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
                ComponentName componentInfo = taskInfo.get(0).topActivity;
                if (componentInfo.getPackageName().equals(mAppConfig.getPackageName())) {
                    isInBackground = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FLog.e("App in background :", "Something went wrong");
        }

        return isInBackground;
    }

    public static boolean isNetworkAvailable() {
        return new NetworkChangeReceiver.ServiceManager(mAppConfig).isNetworkAvailable();
    }

    public static boolean clearOldUserChatData() {

        /*if (new LocalSession().getUserId().equals(new AppSession().getChatUserId())) {
            return false;

        } else {
            new AppSession().saveChatUserId(new LocalSession().getUserId());
            return true;
        }


//TODO::
*/
//        PrefManager prefManager = new PrefManager();
//        prefManager.clearSession();
        return false;
    }

    public static Float getAppVersion() {
        String versionCode = BuildConfig.VERSION_NAME;
        return Float.valueOf(versionCode);
    }

    public static boolean isCompatibleWithCurrentCodeVersion() {
        Float serverVersion = new PrefManager().getServerVersions();
        return getAppVersion() >= serverVersion;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppConfig = this;
        mApplicationContext = getApplicationContext();
        mLocalSession = new PrefManager();
        isReleaseBuild();
        FLog.d("Helloworld", "App initialized");

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        enableCrashlytics();

//      Chat module
        IMInstance.getInstance().init(mAppConfig);
        //// Uncaught-Exception Handler Initialization
        new UCEHandler.Builder(this)
                .setTrackActivitiesEnabled(true)
                .addCommaSeparatedEmailAddresses("medinin2019@gmail.com")
                .build();
    }

    public void enableCrashlytics() {
        Fabric.with(this, new Crashlytics());
//        if (isReleaseBuild()) {
//            Fabric.with(this, new Crashlytics());
//        }
    }





}