package com.abln.futur.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.abln.futur.common.UIUtility.hasPermissions;

public class PreLoginActivity extends BaseActivity implements GestureDetector.OnGestureListener {

    private static final String TAG = "PreloginActivity";
    private static final float SWIPE_THRESHOLD = 100;
    private static final float SWIPE_VELOCITY_THRESHOLD = 100;
    @BindView(R.id.container)
    FrameLayout flContainer;
    @BindView(R.id.tourBar1)
    TextView tvBar1;
    @BindView(R.id.tourBar2)
    TextView tvBar2;
    @BindView(R.id.tourBar3)
    TextView tvBar3;
    @BindView(R.id.tourBar4)
    TextView tvBar4;
    private Fragment[] mfragments;
    private TourGuidePage1 page1;
    private TourGuidePage2 page2;
    private TourGuidePage3 page3;
    private TourGuidePage4 page4;
    private int index;
    private int currentTabIndex;
    private Context mContext;
    private long mLastClickTime = 0;
    private GestureDetector gestureDetector;
    private TextView[] mtextViews;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_login);

        ButterKnife.bind(this);


        if (!hasPermissions(this, PERMISSIONS)) {
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }

        mContext = this;
        gestureDetector = new GestureDetector(this);

        page1 = new TourGuidePage1();
        page2 = new TourGuidePage2();
        page3 = new TourGuidePage3();
        page4 = new TourGuidePage4();


        mfragments = new Fragment[]{page1, page2, page3, page4};

        mtextViews = new TextView[]{tvBar1, tvBar2, tvBar3, tvBar4};

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, page1)
                .add(R.id.container, page2)
                .add(R.id.container, page3)
                .add(R.id.container, page4)

                .hide(page2)
                .hide(page3)
                .hide(page4)
                .show(page1).commit();


    }

    @OnClick({R.id.tour1, R.id.tour2, R.id.tour3, R.id.tour4, R.id.signUpBtn, R.id.loginBtn})
    void onClickListener(View v) {
        switch (v.getId()) {

            case R.id.tour1:
                index = 0;
                break;

            case R.id.tour2:
                index = 1;
                break;

            case R.id.tour3:
                index = 2;
                break;

            case R.id.tour4:
                index = 3;
                break;

            case R.id.signUpBtn:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;


            case R.id.loginBtn:
                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivity(intentLogin);
                break;

            default:
                break;
        }

        updateColor(index);


        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mfragments[currentTabIndex]);
            if (!mfragments[index].isAdded()) {
                trx.add(R.id.container, mfragments[index]);
            }
            trx.show(mfragments[index]).commit();
        }
        currentTabIndex = index;
    }

    private void updateColor(int index) {
        for (int i = 0; i < mtextViews.length; i++) {
            if (index == i) {
                mtextViews[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_purple_bar));
            } else {
                mtextViews[i].setBackground(ContextCompat.getDrawable(mContext, R.drawable.rounded_white_bar));
            }
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onFling(MotionEvent downEvent, MotionEvent moveEvent, float velocityX, float velocityY) {
        boolean result = false;
        float diffY = moveEvent.getY() - downEvent.getY();
        float diffX = moveEvent.getX() - downEvent.getX();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                result = true;
            }
        } else {
            if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffY > 0) {
                    onSwipeBottom();
                } else {
                    onSwipeTop();
                }
                result = true;
            }
        }
        return result;
    }


    private void onSwipeBottom() {
        Log.d(TAG, "Swipe Bottom");
    }

    private void onSwipeRight() {
        Log.d(TAG, "Swipe Right");
        Log.d(TAG, "Swipe right" + currentTabIndex);
        if (currentTabIndex >= 1) {
            currentTabIndex--;
            updateColor(currentTabIndex);
            updateFragmentBwd(currentTabIndex);
        }
    }

    private void onSwipeLeft() {
        Log.d(TAG, "Swipe Left" + currentTabIndex);
        if (currentTabIndex < 3) {
            currentTabIndex++;
            updateColor(currentTabIndex);
            updateFragmentFwd(currentTabIndex);
        }
    }

    private void updateFragmentFwd(int currentTabIndex) {
        if (currentTabIndex > 3 || currentTabIndex < 1) {
            return;
        }

        FragmentTransaction trx = getSupportFragmentManager()
                .beginTransaction();
        trx.hide(mfragments[currentTabIndex - 1]);
        if (!mfragments[currentTabIndex].isAdded()) {
            trx.add(R.id.container, mfragments[currentTabIndex]);
        }
        trx.show(mfragments[currentTabIndex]).commit();
    }


    private void updateFragmentBwd(int currentTabIndex) {

        FragmentTransaction trx = getSupportFragmentManager()
                .beginTransaction();
        trx.hide(mfragments[currentTabIndex + 1]);
        if (!mfragments[currentTabIndex].isAdded()) {
            trx.add(R.id.container, mfragments[currentTabIndex]);
        }
        trx.show(mfragments[currentTabIndex]).commit();
    }

    private void onSwipeTop() {
        Log.d(TAG, "Swipe Top");
    }
}
