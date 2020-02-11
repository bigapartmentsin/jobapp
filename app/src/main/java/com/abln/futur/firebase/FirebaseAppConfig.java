package com.abln.futur.firebase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.abln.futur.BuildConfig;
import com.abln.futur.R;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.customViews.CustomDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import java.util.HashMap;

public class FirebaseAppConfig {

    private static final String IGNORED_VERSION_CODE = "ignored_version_code";
    // Remote Config keys
    private static final String VERSION_CODE = "version_code";
    private static final String VERSION_NAME = "version_name";
    private static final String RELEASE_NOTE = "release_note";
    private static final String IS_FORCE_UPDATE = "is_force_update";
    private static final String SUPPORT_MAIL_ID = "support_email_id";
    private static final String PN_SUB_RELEASE = "pn_sub_release";
    private static final String PN_PUB_RELEASE = "pn_pub_release";
    private static final String PN_SUB_DEBUG = "pn_sub_debug";
    private static final String PN_PUB_DEBUG = "pn_pub_debug";
    // Amazon configurations
    public static String S3_EP_URL = "s3_end_point";
    public static String S3_S_KEY = "s3_secret_key";
    public static String S3_A_KEY = "s3_access_key";
    public static String S3_IMAGES = "s3_image_bucket";
    public static String S3_VIDEOS = "s3_video_bucket";
    public static String S3_THUMBNAILS = "s3_thumbnail_bucket";
    public static String S3_DOC = "s3_doc_bucket";
    private static FirebaseAppConfig mFirebaseAppConfig;
    // shared pref mode
    int PRIVATE_MODE = 0;
    private CustomDialog customDialog;
    // Shared preferences file name
    private String TAG = "FirebaseAppConfig";
    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private FirebaseAppConfig() {
        mFirebaseRemoteConfig = getFrcInstance();
        HashMap defaultConfig = new HashMap();
        defaultConfig.put(VERSION_CODE, BuildConfig.VERSION_CODE);
        defaultConfig.put(VERSION_NAME, BuildConfig.VERSION_NAME);
        defaultConfig.put(RELEASE_NOTE, "- Stability improvements\n- Misc. fixes");
        defaultConfig.put(IS_FORCE_UPDATE, false);
        defaultConfig.put(SUPPORT_MAIL_ID, AppConfig.getInstance().getResources().getString(R.string.help_emailid));

        defaultConfig.put(PN_SUB_RELEASE, "--");
        defaultConfig.put(PN_PUB_RELEASE, "--");

        defaultConfig.put(PN_SUB_DEBUG, "--");
        defaultConfig.put(PN_PUB_DEBUG, "--");


        defaultConfig.put(S3_EP_URL, "--");
        defaultConfig.put(S3_S_KEY, "--");

        defaultConfig.put(S3_A_KEY, "--");
        defaultConfig.put(S3_IMAGES, "--");

        defaultConfig.put(S3_VIDEOS, "--");
        defaultConfig.put(S3_THUMBNAILS, "--");
        defaultConfig.put(S3_DOC, "--");

        mFirebaseRemoteConfig.setDefaults(defaultConfig);
    }

    public static FirebaseAppConfig getInstance() {
        if (mFirebaseAppConfig == null) {
            mFirebaseAppConfig = new FirebaseAppConfig();
        }
        return mFirebaseAppConfig;
    }

    private FirebaseRemoteConfig getFrcInstance() {
        FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());
        return firebaseRemoteConfig;
    }

    public FirebaseRemoteConfig getFirebaseRemoteConfig() {
        return mFirebaseRemoteConfig;
    }

    /**
     * Fetch a welcome message from the Remote Config service, and then activate it.
     */
    public void fetchConfigs(Activity activity, OnCompleteListener listener) {
        long cacheExpiration = 12 * 60; // 12 minutes in seconds in Release mode;
        mFirebaseRemoteConfig.fetch(cacheExpiration).addOnCompleteListener(activity, listener);
    }


    public String getSupportMailId() {
        return mFirebaseRemoteConfig.getString(SUPPORT_MAIL_ID);
    }

    public boolean isUpdatedAvailable() {
        int old = BuildConfig.VERSION_CODE;
        int newVersion = Integer.valueOf("" + mFirebaseRemoteConfig.getLong(VERSION_CODE));
        return newVersion > old;
    }

    public boolean isForceUpdated() {
        return (isUpdatedAvailable() && mFirebaseRemoteConfig.getBoolean(IS_FORCE_UPDATE));
    }

    public String getNewVersion() {
        return mFirebaseRemoteConfig.getString(VERSION_NAME);
    }

    public int getVersionCode() {
        return Integer.valueOf("" + mFirebaseRemoteConfig.getLong(VERSION_CODE));
    }

    public String getReleaseNote() {
        return "Version " + getNewVersion() + "\n" + mFirebaseRemoteConfig.getString(RELEASE_NOTE).replaceAll("-n", "\n");
    }

    public void showUpdateDialog(final Context context) {
        SharedPreferences mPreferences = context.getSharedPreferences(TAG, PRIVATE_MODE);
        final SharedPreferences.Editor mEditor = mPreferences.edit();

        if (customDialog == null || (customDialog != null && !customDialog.isShowing())) {
            customDialog = new CustomDialog(context);
        } else if (customDialog.isShowing()) {
            return;
        }

        customDialog.setMessage(getReleaseNote());
        customDialog.setCancelable(false);

        if (isForceUpdated()) {
            customDialog.setTitle("Update Required");
        } else {
            customDialog.setTitle("Update Available");
            customDialog.setNegativeButton("Ignore", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEditor.putInt(IGNORED_VERSION_CODE, getVersionCode()).commit();
                    customDialog.dismiss();
                }
            });
        }

        customDialog.setPositiveButton("Update Now", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialog.dismiss();
                try {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
                } catch (android.content.ActivityNotFoundException e) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
                }
                Toast.makeText(context, "Redirecting to Playstore..", Toast.LENGTH_LONG).show();
            }
        });

        Log.i(TAG, "isForceUpdated = " + isForceUpdated()
                + "\nisUpdatedAvailable = " + isUpdatedAvailable()
                + "\nisIgnoredVersion = " + isIgnoredVersion(mPreferences)
                + "\nIgnored Version = " + mPreferences.getInt(IGNORED_VERSION_CODE, BuildConfig.VERSION_CODE)
                + "\nNew VersionCode = " + getVersionCode()
                + "\nOld VersionCode = " + BuildConfig.VERSION_NAME);

        if ((isForceUpdated() || isUpdatedAvailable()) && !isIgnoredVersion(mPreferences)) {
            customDialog.show();
        }
    }

    private boolean isIgnoredVersion(SharedPreferences preferences) {
        int ignoredVersion = preferences.getInt(IGNORED_VERSION_CODE, BuildConfig.VERSION_CODE);
        int newVersion = getVersionCode();
        return ignoredVersion == newVersion;
    }

    public void setupConfigeration() {
        if (AppConfig.isReleaseBuild()) {
            AppConfig.PN_PUB = mFirebaseRemoteConfig.getString(PN_PUB_RELEASE);
            AppConfig.PN_SUB = mFirebaseRemoteConfig.getString(PN_SUB_RELEASE);
        } else {
            AppConfig.PN_PUB = mFirebaseRemoteConfig.getString(PN_PUB_DEBUG);
            AppConfig.PN_SUB = mFirebaseRemoteConfig.getString(PN_SUB_DEBUG);
        }

        if (AppConfig.PN_PUB.equals("--")
                || AppConfig.PN_SUB.equals("--")) {
            Log.e("FirebaseAppConfig[206]", "ERROR :PubNub details are invalid," +
                    "Contact system administrator");
        }

        NetworkConfig.S3_EP_URL = mFirebaseRemoteConfig.getString(S3_EP_URL);
        NetworkConfig.S3_S_KEY = mFirebaseRemoteConfig.getString(S3_S_KEY);
        NetworkConfig.S3_A_KEY = mFirebaseRemoteConfig.getString(S3_A_KEY);
        NetworkConfig.S3_IMAGES = mFirebaseRemoteConfig.getString(S3_IMAGES);
        NetworkConfig.S3_VIDEOS = mFirebaseRemoteConfig.getString(S3_VIDEOS);
        NetworkConfig.S3_THUMBNAILS = mFirebaseRemoteConfig.getString(S3_THUMBNAILS);
        NetworkConfig.S3_DOC = mFirebaseRemoteConfig.getString(S3_DOC);


        if (NetworkConfig.S3_EP_URL.equals("--")
                || NetworkConfig.S3_S_KEY.equals("--")
                || NetworkConfig.S3_A_KEY.equals("--")
                || NetworkConfig.S3_IMAGES.equals("--")
                || NetworkConfig.S3_VIDEOS.equals("--")
                || NetworkConfig.S3_THUMBNAILS.equals("--")) {
            Log.e("FirebaseAppConfig[223]", "ERROR :Amazon S3 details are invalid, " +
                    "Contact system administrator");
        }
    }
}
