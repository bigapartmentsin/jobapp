package com.abln.futur.activites;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.abln.chat.core.base.IMBaseListener;
import com.abln.chat.ui.activities.IMBaseActivity;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.ConstantUtils;
import com.abln.futur.common.DataCache;
import com.abln.futur.common.FLog;
import com.abln.futur.common.NetworkChangeReceiver;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.ResponseHandler;
import com.abln.futur.common.Service;
import com.abln.futur.common.Handler;
import com.abln.futur.common.postjobs.BaseView;
import com.abln.futur.firebase.FirebaseAppConfig;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.listeners.PermissionCallback;
import com.google.android.gms.tasks.OnCompleteListener;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;


public class BaseActivity extends IMBaseActivity implements TaskCompleteListener, BaseView,IMBaseListener, ResponseHandler {



    public static final int LOCATION_REQ = 7446;
    private static final String TAG = "BaseActivity";
    private TaskCompleteListener mTaskCompleteListener;
    private BroadcastReceiver mBroadcastReceiver;
    private PrefManager mLocalSession = new PrefManager();
    private long lastSyncTime = 0;
    public DataCache dataCache;
    protected CompositeDisposable compositeDisposable;
    protected Handler apiService;

    //sumeeth added
    private NetworkChangeReceiver mNetworkChangeReceiver;
    private PermissionCallback callback;
    private int requestcode;
    //private GoogleApiClient mGoogleApiClient;
    private PermissionCallback mCallback;
//    private LocationRequest mLocationRequest;
    private int previousEvent = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ConstantUtils.SCREEN_NAME,getClass().getSimpleName());


        dataCache = DataCache.getInstance();
        apiService = Service.getApiService();

        compositeDisposable = new CompositeDisposable();

        if (AppConfig.isTablet()) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        mNetworkChangeReceiver = new NetworkChangeReceiver();

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (null != mTaskCompleteListener)
                    mTaskCompleteListener.onTaskCompleted(context, intent);

            }
        };


        IntentFilter intentFilter = new IntentFilter(NetworkConfig.TASK_COMPLETE);
        intentFilter.addAction(NetworkConfig.DOWNLOAD_COMPLETE);
        intentFilter.addAction(NetworkConfig.NETWORK_STATE);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        setTaskCompleteListener(this);
        registerReceiver(mNetworkChangeReceiver, intentFilter);
        LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, intentFilter);


        FLog.d(TAG, "Registered receiver for : TASK_COMPLETE,DOWNLOAD_COMPLETE");
    }

    protected void setTaskCompleteListener(TaskCompleteListener listener) {
        if (null != listener)
            mTaskCompleteListener = listener;
    }

    @Override
    protected void onResume() {
        super.onResume();


        // Firebase remote config
        final FirebaseAppConfig firebaseAppConfig = FirebaseAppConfig.getInstance();
        firebaseAppConfig.fetchConfigs(this, new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull com.google.android.gms.tasks.Task task) {
                firebaseAppConfig.getFirebaseRemoteConfig().activateFetched();
                firebaseAppConfig.showUpdateDialog(BaseActivity.this);
                firebaseAppConfig.setupConfigeration();
            }

        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        FLog.i(TAG, "onStop ....." + AppConfig.isAppIsInBackground());
//        if (AppConfig.isAppIsInBackground()) {
//            mLocalSession.saveIsLocked(true);
//        } else {
//            Intent timerIntent = new Intent(this, NetworkOperationService.class);
//            timerIntent.putExtra(NetworkConfig.API_URL, NetworkConfig.SESSION_MANAGEMENT);
//            timerIntent.putExtra(NetworkConfig.SESSION_TYPE, NetworkConfig.SESSION_STOP);
//            startService(timerIntent);
//        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FLog.i(TAG, "onRestart ....." + AppConfig.isAppIsInBackground());
        // FLog.i(TAG, "On Restart ....." + mLocalSession.getIsLocked() + mLocalSession.getIsSessionOut());
//        if (mLocalSession != null && mLocalSession.getIsLocked()) {
//            if (mLocalSession.getIsLockScreenConfigured()) {
//                FLog.i(TAG, "On Restart .....locked" + mLocalSession.getIsLocked());
//                Intent lockIntent = new Intent(this, LockActivity.class);
//                startActivity(lockIntent);
//
//            }
//            return;
//        } else if (mLocalSession != null && mLocalSession.getIsSessionOut()) {
//
//            FLog.i(TAG, "On Restart .....login" + mLocalSession.getIsSessionOut());
//            Intent lockIntent = new Intent(this, LoginActivity.class);
//            startActivity(lockIntent);
//
//            return;
//        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mBroadcastReceiver) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
            FLog.d(TAG, "Unregistered receiver for TASK_COMPLETE,DOWNLOAD_COMPLETE");
        }

        if (null != mNetworkChangeReceiver) {
            unregisterReceiver(mNetworkChangeReceiver);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return dispatchTouchEvent(event, true);
    }

    public boolean dispatchTouchEvent(MotionEvent event, boolean isEnable) {
        if (isEnable) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_UP:
                    if (mLocalSession != null && mLocalSession.getAccessToken() != null) {
                        if (previousEvent != event.getAction()
                                && event.getAction() != MotionEvent.ACTION_UP) {
                            FLog.d(TAG, "onTouchEvent");
                        }
                    }
                    previousEvent = event.getAction();
            }
        }
        return super.dispatchTouchEvent(event);
    }


    public void showAlertDialogAndExitApp(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(BaseActivity.this).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                });

        alertDialog.show();
    }

    private void makeServerDownTimeDetailsCall() {
//        String userAgent = "Android|" + FuturClient.getDeviceId();
//        RealmListBody body = new RealmListBody();
//
//        HashMap<String, String> headerMap = new HashMap<>();
//        headerMap.put("TenantId", mLocalSession.getTenantId());
//
//        Intent intent = new Intent(this, NetworkOperationService.class);
//        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.GET_DOWNTIME_DETAILS);
//        intent.putExtra(NetworkConfig.INPUT_BODY, body);
//        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
//        this.startService(intent);
    }

    private void makeServerDownTimeCall() {
//        NetworkOperationService.makeGetCall(this, NetworkConfig.GET_DOWNTIME);
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {

    }

    protected void addFragment(@IdRes int containerViewId,
                               @NonNull Fragment fragment,
                               @NonNull String fragmentTag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(containerViewId, fragment, fragmentTag)
                .disallowAddToBackStack()
                .commit();
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }


// sumeeth added code to do the changes


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (callback != null) {
            if (requestCode == this.requestcode) {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                            callback.onPermissionStatus(false);
                            break;
                        }
                    }
                    callback.onPermissionStatus(true);
                } else {
                    callback.onPermissionStatus(false);
                }
            } else {
                callback.onPermissionStatus(true);
            }
        }

    }

    public boolean isPemissionAllowed(String permission) {
        return ContextCompat.checkSelfPermission(getApplicationContext(),
                permission) == PackageManager.PERMISSION_GRANTED;
    }


    /*public void showLocationOnDialog(final PermissionCallback callback) {
        mCallback = callback;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
//                LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
//                        callback.onPermissionStatus(true);
                        startFetchingLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    BaseActivity.this, LOCATION_REQ);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        callback.onPermissionStatus(false);
                        break;
                }
            }
        });
    }*/

    public void requestPermission(String permission, int requestcode, PermissionCallback callback) {
        this.requestcode = requestcode;
        if (isPemissionAllowed(permission)) {
            callback.onPermissionStatus(true);
        } else {
            this.requestcode = requestcode;
            this.callback = callback;
            ActivityCompat.requestPermissions(this,
                    new String[]{permission},
                    requestcode);
        }
    }

    public void requestPermission(String[] permission, int requestcode, PermissionCallback callback) {
        try {
            ArrayList<String> list = isPemissionAllowed(permission);
            if (list.size() == 0) {
                callback.onPermissionStatus(true);
            } else {
                String[] permissionList = new String[list.size()];
                for (int i = 0; i < permissionList.length; i++) {
                    permissionList[i] = list.get(i);
                }
                this.requestcode = requestcode;
                this.callback = callback;
                ActivityCompat.requestPermissions(this,
                        permissionList,
                        requestcode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<String> isPemissionAllowed(String[] permission) {
        ArrayList<String> list = new ArrayList<>();
        try {
            for (String permssion : permission) {
                boolean isGranted = ContextCompat.checkSelfPermission(getApplicationContext(),
                        permssion) == PackageManager.PERMISSION_GRANTED;
                if (!isGranted) {
                    list.add(permssion);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case LOCATION_REQ:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        if (mCallback != null) {
                            mCallback.onPermissionStatus(true);
                        }
                        break;
                    case Activity.RESULT_CANCELED:
                        if (mCallback != null) {
                            mCallback.onPermissionStatus(false);
                        }
                        break;
                }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }


    /*public void startFetchingLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d("LocationService", "onConnected (line 49): " + location);
            if (location == null) {
                if (mLocationRequest == null) {
                    mLocationRequest = LocationRequest.create()
                            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                            .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                            .setFastestInterval(1 * 1000);
                }
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                LocationData locationData = new LocationData();
                locationData.location = location;
                EventBus.getDefault().post(locationData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/


    public void onConnected(Bundle dataBundle) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSuccess(String response, Object data, int urlId, int position) {

    }




    @Override
    public void onFailure(Exception e, int urlId) {

    }

    @Override
    public void showProgress(String text) {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showInternetError() {

    }

    @Override
    public void showErrorDialog(String title, String description, String positiveBtn) {

    }


    public void replaceFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {

    }
}
