package com.abln.futur.activites;

import android.content.Context;
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
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.chats.adapter.GetAllUsersResponse;
import com.abln.futur.module.login.PreLoginActivity;
import com.abln.futur.services.NetworkOperationService;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.abln.futur.common.UIUtility.ANIMATING_TIME;
import static com.abln.futur.common.UIUtility.hasPermissions;


public class
SplashActivity extends BaseActivity implements TaskCompleteListener {


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


        getAllUserListFromServer();


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




    // new function is added to handle crash ;
    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.getAllUser)) {
            FuturProgressDialog.dismissDialog();
            GetAllUsersResponse getAllUsersResponse = DataParser.parseJson(responseString, GetAllUsersResponse.class);
            if (getAllUsersResponse.getStatuscode() == 0) {
                UIUtility.showToastMsg_short(this, getAllUsersResponse.getStatusMessage());
                return;
            } else if (getAllUsersResponse.getStatuscode() == 1) {

                if (getAllUsersResponse.getData().getUserList().size() == 0) {
                    UIUtility.showToastMsg_short(this, "No data found");
                    return;
                }



                String firstName = null;


                ArrayList<IMChatUser> chatUsers = new ArrayList<>();
                for (int i = 0; i < getAllUsersResponse.getData().getUserList().size(); i++) {
                    firstName = getAllUsersResponse.getData().getUserList().get(i).getFirstName();

                    IMChatUser chatUser = new IMChatUser();
                    if (null != firstName) {

                        System.out.println("Getting user id ");

                        chatUser.userId = getAllUsersResponse.getData().getUserList().get(i).getApikey() + "";
                        chatUser.userName = firstName.trim();
                        chatUsers.add(chatUser);
                    }

                    //   mySGList.add(getAllPatientResponse.getData().getPatientList().get(i));
                }

                AppConfig.saveChatInfo(chatUsers, this);


//                IMInstance.getInstance().setUnSeenChatMsgCountListener((BaseFragment) getActivity());
                IMInstance.getInstance().updateLoggedInuserStatus(true);
                FLog.d(TAG, String.valueOf(IMInstance.getInstance().getTotalUnSeenChatMsgCount()));




                startAnimation();



            }


        }
    }



    private void getAllUserListFromServer() {
        GetUserRequest mFavoriteRquestBody = new GetUserRequest();
        mFavoriteRquestBody.setCount(1);
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(this, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getAllUser);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        startService(intent);
    }







}
