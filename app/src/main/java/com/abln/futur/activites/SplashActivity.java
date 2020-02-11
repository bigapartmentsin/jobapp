package com.abln.futur.activites;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.abln.chat.IMInstance;
import com.abln.futur.R;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.module.login.PreLoginActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.abln.futur.common.UIUtility.ANIMATING_TIME;
import static com.abln.futur.common.UIUtility.hasPermissions;


public class
SplashActivity extends AppCompatActivity {


    private final int PERMISSION_ALL = 1;
    private final String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_SMS,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.RECEIVE_SMS,
            android.Manifest.permission.CALL_PHONE,
            android.Manifest.permission.RECORD_AUDIO,
            android.Manifest.permission.ACCESS_WIFI_STATE,

            android.Manifest.permission.MODIFY_AUDIO_SETTINGS
    };

    private static final String TAG = "SplashActivity";

    private static PrefManager mLocalSession = new PrefManager();

    @BindView(R.id.logo_img)
    ImageView bgLogo;

    @BindView(R.id.version)
    TextView tvVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }


        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(SplashActivity.this, new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken", newToken);
                updateToken(newToken);
            }
        });


        if (AppConfig.isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        mLocalSession.saveAccessToken("hello");

        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        tvVersion.setText("Version: " + AppConfig.getAppVersion());
        startAnimation();


    }

    private void updateToken(String token) {
        if (null != IMInstance.getInstance()) {
            IMInstance.getInstance().updateFCMRegistrationToken(token);
        }
    }


    private void startAnimation() {

        final Animation zoomAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom);
        bgLogo.startAnimation(zoomAnimation);
        zoomAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Handler mHandler = new Handler(getMainLooper());
                Runnable mRunnable = new Runnable() {
                    @Override
                    public void run() {
                        startFunctions();
                    }
                };
                mHandler.postDelayed(mRunnable, ANIMATING_TIME);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    private void startFunctions() {
        if (!mLocalSession.getApikey().equalsIgnoreCase(" ")) {
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            startActivity(new Intent(this, PreLoginActivity.class));
            finish();
        }
    }
}
