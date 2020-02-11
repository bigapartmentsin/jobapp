package com.abln.futur.module.global.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.MainActivity;
import com.abln.futur.R;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.Name;
import com.abln.futur.common.Title;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AccountOne;
import com.abln.futur.common.models.BitmapUtils;
import com.abln.futur.common.models.UserData;
import com.abln.futur.common.newview.DataView;
import com.abln.futur.common.postjobs.BaseNewFragment;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.customViews.Croller;
import com.abln.futur.customViews.OnCrollerChangeListener;
import com.abln.futur.customViews.ViewRotation;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.chats.adapter.GetAll_UserResponse;
import com.abln.futur.services.NetworkOperationService;
import com.akaita.android.circularseekbar.CircularSeekBar;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFragment extends BaseNewFragment implements TaskCompleteListener, JobRoleAdapter.FolderClickListener, View.OnTouchListener {

    private static final String TAG = "GlobalFragment";

    private static Bitmap imageOriginal, imageScaled;
    private static Matrix matrix;
    public MainActivity activity;
    //public Croller croller;
    @BindView(R.id.recycler_searchRole)
    RecyclerView recycler_searchRole;

    @BindView(R.id.globalJobsSearch)
    RelativeLayout rlGlobalSearch;

    @BindView(R.id.globalViewJobs)
    RelativeLayout rlGlobalView;

    @BindView(R.id.JobSwitch)
    SwitchCompat JobSwitch;


    @BindView(R.id.editText_search)
    AutoCompleteTextView jobsearch;



    @BindView(R.id.imageView1)
    ViewRotation wheelMenu;


    private String  rollingvalue = "3";



  //  private ViewRotation wheelMenu;


    private Location mCurrentLocation;
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500000;
    private static final int REQUEST_CHECK_SETTINGS = 100;

    int currAngle = 0;
    int mile = 0;
    int result = 0;
    LinearLayoutManager linearLayoutManager;
    Context context;
    private JobRoleAdapter mAdapter;
    private List<GetAll_UserResponse.PatientList> mySGList = new ArrayList<>();
    private Context mContext;
    private NetworkOperationService mService;
    private String[] resources = new String[]{
            "global_ic_tech",
            "global_ic_a_entertainment",
            "global_ic_banking",
            "global_ic_consulting",
            "global_ic_media_gen",
            "vc_investment",
            "global_ic_edu_other",
            "global_ic_gov_polit",
            "global_ic_advt",
            "property_sale",
            "global_ic_construction",
            "food_bevara",
            "travel_spoer",
            "global_ic_manufacturing",
            "global_ic_others"
    };
    private String[] industry = new String[]{
            "Tech",
            "Arts & Entertainment",
            "Banking",
            "Consulting",
            "Media & Journalism",
            "VC & Investment",
            "Education & Academia",
            "Governement & Politics",
            "Advertising",
            "Property Sales & Letting",
            "Construction",
            "Food & Beverages",
            " Travel & Hospitality",
            "Manufacturing",
            "Others"};

    private int dialerHeight, dialerWidth;
    private GestureDetector detector;
    private boolean[] quadrantTouched;
    private boolean allowRotating;


    private int job_category = -1;


    private int radius;


    public GlobalFragment() {
        // Required empty public constructor
    }

    /**
     * @return The selected quadrant.
     */
    private static int getQuadrant(double x, double y) {
        if (x >= 0) {
            return y >= 0 ? 1 : 4;
        } else {
            return y >= 0 ? 2 : 3;
        }
    }




    private void getFullData() {


        compositeDisposable.add(apiService.getfulldata("v1/full-title")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Title>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        if (baseResponse.statuscode == 1) {
                            Title data = (Title) baseResponse.data;
                            ArrayList<Name> suggestion = data.result;
                            SearchAdapter adapter = new SearchAdapter(getActivity(), suggestion);
                            jobsearch.setThreshold(1);
                            jobsearch.setAdapter(adapter);
                        } else {

                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));
    }




    @Override
    public int getLayoutId() {
        return R.layout.fragment_global;
    }

    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        getFullData();

    }


    private void dataview() {


        linearLayoutManager = new LinearLayoutManager(getActivity());
        recycler_searchRole.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        recycler_searchRole.setHasFixedSize(true);


        // load the image only once
        if (imageOriginal == null) {
            imageOriginal = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_circle);
        }

        // initialize the matrix only once
        if (matrix == null) {
            matrix = new Matrix();
        } else {
            // not needed, you can also post the matrix immediately to restore the old state
            matrix.reset();
        }

        detector = new GestureDetector(getActivity(), new MyGestureDetector());

        // there is no 0th quadrant, to keep it simple the first value gets ignored
        quadrantTouched = new boolean[]{false, false, false, false, false};
        allowRotating = false;

//
//        dialer.setOnTouchListener(new MyOnTouchListener());
//        dialer.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//
//            @Override
//            public void onGlobalLayout() {
//
//                // method called more than once, but the values only need to be initialized one time
//                if (dialerHeight == 0 || dialerWidth == 0) {
//                    dialerHeight = 220;
//                    dialer.getHeight();
//                    dialerWidth = 220;//dialer.getWidth();
//
//                    // resize
//                    Matrix resize = new Matrix();
//                    resize.postScale((float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getWidth(), (float) Math.min(dialerWidth, dialerHeight) / (float) imageOriginal.getHeight());
//                    imageScaled = Bitmap.createBitmap(imageOriginal, 0, 0, imageOriginal.getWidth(), imageOriginal.getHeight(), resize, false);
//
//                    // translate to the image view's center
//                    float translateX = dialerWidth / 2 - imageScaled.getWidth() / 2;
//                    float translateY = dialerHeight / 2 - imageScaled.getHeight() / 2;
//                    matrix.postTranslate(translateX, translateY);
//
//                    dialer.setImageBitmap(imageScaled);
//                    dialer.setImageMatrix(matrix);
//                }
//            }
//        });



    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ButterKnife.bind(this, view);
        mContext = getActivity();
        mService = new NetworkOperationService();
        context = getActivity();
        mContext = getActivity();
        initLocation();
        dataview();
        wheelMenu.setDivCount(18);
        wheelMenu.setWheelImage(R.drawable.ic_circle);
        wheelMenu.setAlternateTopDiv(1);

        // @Code



        wheelMenu.setWheelChangeListener(new ViewRotation.WheelChangeListener(){
            @Override
            public void onSelectionChange(int selectedPosition) {




                int value = selectedPosition+1;

                switch (value){
                    case 1:
                        break;
                    case 2:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"8");
                        rollingvalue="8";
                        break;
                    case 3:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"7");
                        rollingvalue="7";
                        break;
                    case 4:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"6");
                        rollingvalue="6";
                        break;
                    case 5:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"5");
                        rollingvalue="5";
                        break;
                    case 6:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"4");
                        rollingvalue="4";
                        break;
                    case 7:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"3");
                        rollingvalue="3";
                        break;
                    case 8:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"2");
                        rollingvalue="2";
                        break;
                    case 9:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"1");
                        rollingvalue="1";
                        break;
                    case 10:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"18");
                        rollingvalue="18";
                        break;
                    case 11:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"17");
                        rollingvalue="17";
                        break;
                    case 12:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"16");
                        rollingvalue="16";
                        break;
                    case 13:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"15");
                        rollingvalue="15";
                        break;
                    case 14:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"14");
                        rollingvalue="14";
                        break;
                    case 15:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"13");
                        rollingvalue="13";
                        break;
                    case 16:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"12");
                        rollingvalue="12";
                        break;
                    case 17:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"11");
                        rollingvalue="11";

                        break;
                    case 18:
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"10");
                        rollingvalue="10";
                        break;
                    case 19:
                        break;
                    case 20:
                        break;








                }


                // selectedPositionText.setText("selected: " + (selectedPosition + 1));
            }
        });





        mAdapter = new JobRoleAdapter(mContext, mySGList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recycler_searchRole.setLayoutManager(mLayoutManager);
        recycler_searchRole.setItemAnimator(new DefaultItemAnimator());
        recycler_searchRole.setAdapter(mAdapter);

        loadJobCategories();

        JobSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                adjustView();
            }
        });


        //croller disable commenting ;


//        croller.setOnCrollerChangeListener(new OnCrollerChangeListener() {
//            @Override
//            public void onProgressChanged(Croller croller, int progress) {
//
//
//                radius = progress;
//
//             Log.d("TOTAL", String.valueOf(progress));
//
//            }
//
//            @Override
//            public void onStartTrackingTouch(Croller croller) {
//             //   Toast.makeText(MainActivity.this, "Start", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onStopTrackingTouch(Croller croller) {
//               // Toast.makeText(MainActivity.this, "Stop", Toast.LENGTH_SHORT).show();
//            }
//        });


        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        startLocationUpdates();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            // open device settings when the permission is
                            // denied permanently
                            //TODO  openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


        getUserLocation();

    }


    private void getUserLocation() {


        if (mCurrentLocation != null) {

            Geocoder geocoder = null;
            List<Address> addresses = null;

            try {
                geocoder = new Geocoder(mContext, Locale.getDefault());
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                System.out.println("stuff" + mCurrentLocation.getLongitude() + "-" + mCurrentLocation.getLatitude());


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void makePopUp(String ti) {


        post post = new post();
        post.title = ti;


        compositeDisposable.add(apiService.getTitle("v1/title",post)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Title>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        //   itemAdapter.notifyDataSetChanged();

                        if (baseResponse.statuscode == 0) {


                        } else if (baseResponse.statuscode == 1) {

                            Title info = (Title) baseResponse.data;
                            ArrayList<Name> suggestion = info.result;


                            jobsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                    Toast.makeText(getActivity(), "Clicked item " + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();

                                }


                            });
                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                    }
                }));


    }


    @OnClick({R.id.JobSwitch, R.id.btn_search})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.JobSwitch:
                adjustView();
                break;
            case R.id.btn_search:
                performSearch();
                break;
        }
    }

    private void performSearch() {
        if (!JobSwitch.isChecked()) {

            //TODO same jobsearch for the industry
            //TODO look for job current location .
            // if current location is not set

            if (mCurrentLocation != null) {


                if (!jobsearch.getText().toString().equalsIgnoreCase("")) {


                    System.out.println("Moving from search pattern current location ");

                 // String mLng = String.valueOf(mCurrentLocation.getLongitude());
                  //String mLat = String.valueOf(mCurrentLocation.getLatitude());

                  System.out.println("Current location data "+prefManager.getLon()+"---"+prefManager.getLat());



                    Intent ii = new Intent(mContext, DataView.class);

                    Log.d("PutExtra", String.valueOf(radius));
                   // ii.putExtra("Angle", radius);
                    ii.putExtra("title", jobsearch.getText().toString());
                    ii.putExtra("roolingvalue",rollingvalue);
                    ii.putExtra("lat",prefManager.getLat());
                    ii.putExtra("lng",prefManager.getLon());
                    startActivity(ii);
                    jobsearch.setText("");
                    jobsearch.clearFocus();

                } else {

                    UIUtility.showToastMsg_withErrorShort(getActivity(), "Please Enter a Job Title");
                }


            } else {
                System.out.println("null for getting location ");

                UIUtility.showToastMsg_withErrorShort(getActivity(), "Location Access is need getting home location");
                getUInfo();


            }


        } else {


            //Im a
            if (job_category == -1) {
                UIUtility.showToastMsg_withAlertInfoShort(mContext, "Select the job category to continue");
                return;
            }
            Intent ii = new Intent(mContext, NonJobSeekerSearchResultActivity.class);
            ii.putExtra("Angle", currAngle);
            startActivity(ii);


        }
    }

    private void adjustView() {
        if (!JobSwitch.isChecked()) {

            rlGlobalSearch.setVisibility(View.VISIBLE);
            rlGlobalView.setVisibility(View.GONE);


        } else {


            rlGlobalView.setVisibility(View.VISIBLE);
            rlGlobalSearch.setVisibility(View.GONE);
        }
    }

    private void loadJobCategories() {
        for (int i = 0; i < resources.length; i++) {
            GetAll_UserResponse.PatientList response = new GetAll_UserResponse.PatientList();
            response.setPatientId(industry[i]);
            response.setPhoto(resources[i]);
            mySGList.add(i, response);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {

    }

    @Override
    public void onScrollEnd(int lastPosition) {

    }

    @Override
    public void onPatientClicked(int position) {
        job_category = position;
    }

    /**
     * @return The angle of the unit circle with the image view's center
     */
    private double getAngle(double xTouch, double yTouch) {
        double x = xTouch - (dialerWidth / 2d);
        double y = dialerHeight - yTouch - (dialerHeight / 2d);

        switch (getQuadrant(x, y)) {
            case 1:


                return Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;

            case 2:

                return 180 - Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;

            case 3:

                return 180 + (-1 * Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI);
            case 4:


                return 360 + Math.asin(y / Math.hypot(x, y)) * 180 / Math.PI;
            default:
                return 0;
        }
    }

    /**
     * Rotate the dialer.
     *
     * @param degrees The degrees, the dialer should get rotated.
     */
    private void rotateDialer(float degrees) {
        matrix.postRotate(degrees, dialerWidth / 2, dialerHeight / 2);

       // dialer.setImageMatrix(matrix);
    }

    public void callMethod() {
        if (currAngle < 270) {
            result = currAngle / 18;
            if (currAngle % 18 == 0) {
                mile = 15 - result;
            } else {
                mile = 15 - result;
            }

        } else if (currAngle >= 270) {

            result = currAngle / 18;
            /*if(currAngle%18==0)
            {
                mile=20+(15-result);
            }
            else{*/
            mile = 20 + (15 - result);
            //   }
        }
        Toast.makeText(getActivity(), String.valueOf(mile), Toast.LENGTH_SHORT).show();
    }


    private class MyOnTouchListener implements View.OnTouchListener {
        private double startAngle;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    // reset the touched quadrants
                    for (int i = 0; i < quadrantTouched.length; i++) {
                        quadrantTouched[i] = false;
                    }

                    allowRotating = false;

                    startAngle = getAngle(event.getX(), event.getY());
                    break;

                case MotionEvent.ACTION_MOVE:
                    double currentAngle = getAngle(event.getX(), event.getY());
                    rotateDialer((float) (startAngle - currentAngle));
                    startAngle = currentAngle;
                    currAngle = (int) Math.round(currentAngle);
                    Log.e("current angle", startAngle + "currentAnhe" + currentAngle);
                    break;

                case MotionEvent.ACTION_UP:
                    allowRotating = true;
                    break;
            }

            // set the touched quadrant to true
            quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;

            detector.onTouchEvent(event);

            return true;
        }

    }

    private class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // get the quadrant of the start and the end of the fling
            int q1 = getQuadrant(e1.getX() - (dialerWidth / 2), dialerHeight - e1.getY() - (dialerHeight / 2));
            int q2 = getQuadrant(e2.getX() - (dialerWidth / 2), dialerHeight - e2.getY() - (dialerHeight / 2));

            // the inversed rotations
            if ((q1 == 2 && q2 == 2 && Math.abs(velocityX) < Math.abs(velocityY))
                    || (q1 == 3 && q2 == 3)
                    || (q1 == 1 && q2 == 3)
                    || (q1 == 4 && q2 == 4 && Math.abs(velocityX) > Math.abs(velocityY))
                    || ((q1 == 2 && q2 == 3) || (q1 == 3 && q2 == 2))
                    || ((q1 == 3 && q2 == 4) || (q1 == 4 && q2 == 3))
                    || (q1 == 2 && q2 == 4 && quadrantTouched[3])
                    || (q1 == 4 && q2 == 2 && quadrantTouched[3])) {

             //   dialer.post(new FlingRunnable(-1 * (velocityX + velocityY)));
            } else {
                // the normal rotation
           //     dialer.post(new FlingRunnable(velocityX + velocityY));
            }

            return true;
        }
    }

    /**
     * A {@link Runnable} for animating the the dialer's fling.
     */
    private class FlingRunnable implements Runnable {

        private float velocity;

        public FlingRunnable(float velocity) {
            this.velocity = velocity;
        }

        @Override
        public void run() {
            if (Math.abs(velocity) > 5) {
                rotateDialer(velocity / 75);
                velocity /= 1.0666F;

                // post this instance again
           //     dialer.post(this);
            }
        }

    }


    private void initLocation() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        mSettingsClient = LocationServices.getSettingsClient(mContext);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                // mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

            }
        };
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener((Activity) mContext, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        //noinspection MissingPermission
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());


                    }
                })
                .addOnFailureListener((Activity) mContext, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(getActivity(), REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);

                                Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                        }


                    }
                });


    }


    /// custom function to do the touch roatation ;


    private double mCurrAngle = 0;
    private double mPrevAngle = 0;
    ImageView bask;
    private double startAngle = 9;


    @Override
    public boolean onTouch(View v, MotionEvent event) {





        //// --- new code goes here
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // reset the touched quadrants
                for (int i = 0; i < quadrantTouched.length; i++) {
                    quadrantTouched[i] = false;
                }
                allowRotating = false;
                startAngle = getAngle(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                double currentAngle = getAngle(event.getX(), event.getY());
                rotateDialer((float) (startAngle - currentAngle));
                startAngle = currentAngle;
                break;
            case MotionEvent.ACTION_UP:
                allowRotating = true;
                break;
        }

// set the touched quadrant to true
        quadrantTouched[getQuadrant(event.getX() - (dialerWidth / 2), dialerHeight - event.getY() - (dialerHeight / 2))] = true;
        detector.onTouchEvent(event);
        return true;





    }

    private void animate(double fromDegrees, double toDegrees, long durationMillis) {
        final RotateAnimation rotate = new RotateAnimation((float) fromDegrees, (float) toDegrees,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(durationMillis);
        rotate.setFillEnabled(true);
        rotate.setFillAfter(true);
     //   wheel.startAnimation(rotate);
        System.out.println(mCurrAngle);
    }





     // touch event class ;

//
//
//    @Override
//    public boolean onTouchEvent(MotionEvent e) {
//
//
//        if (BitmapUtils.getDistance(e.getX(), e.getY(), midx, midy) > Math.max(mainCircleRadius, Math.max(backCircleRadius, progressRadius))) {
//            if (startEventSent && mCrollerChangeListener != null) {
//                mCrollerChangeListener.onStopTrackingTouch(this);
//                startEventSent = false;
//            }
//            return super.onTouchEvent(e);
//        }
//
//
//
//        if (e.getAction() == MotionEvent.ACTION_DOWN) {
//
//            float dx = e.getX() - midx;
//            float dy = e.getY() - midy;
//            downdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
//            downdeg -= 90;
//            if (downdeg < 0) {
//                downdeg += 360;
//            }
//            downdeg = (float) Math.floor((downdeg / 360) * (max + 5));
//
//            if (mCrollerChangeListener != null) {
//                mCrollerChangeListener.onStartTrackingTouch(this);
//                startEventSent = true;
//            }
//
//            return true;
//        }
//        if (e.getAction() == MotionEvent.ACTION_MOVE) {
//            float dx = e.getX() - midx;
//            float dy = e.getY() - midy;
//            currdeg = (float) ((Math.atan2(dy, dx) * 180) / Math.PI);
//            currdeg -= 90;
//            if (currdeg < 0) {
//                currdeg += 360;
//            }
//            currdeg = (float) Math.floor((currdeg / 360) * (max + 5));
//
//            if ((currdeg / (max + 4)) > 0.75f && ((downdeg - 0) / (max + 4)) < 0.25f) {
//                if (isAntiClockwise) {
//                    deg++;
//                    if (deg > max + 2) {
//                        deg = max + 2;
//                    }
//                } else {
//                    deg--;
//                    if (deg < (min + 2)) {
//                        deg = (min + 2);
//                    }
//                }
//            } else if ((downdeg / (max + 4)) > 0.75f && ((currdeg - 0) / (max + 4)) < 0.25f) {
//                if (isAntiClockwise) {
//                    deg--;
//                    if (deg < (min + 2)) {
//                        deg = (min + 2);
//                    }
//                } else {
//                    deg++;
//                    if (deg > max + 2) {
//                        deg = max + 2;
//                    }
//                }
//            } else {
//                if (isAntiClockwise) {
//                    deg -= (currdeg - downdeg);
//                } else {
//                    deg += (currdeg - downdeg);
//                }
//                if (deg > max + 2) {
//                    deg = max + 2;
//                }
//                if (deg < (min + 2)) {
//                    deg = (min + 2);
//                }
//            }
//
//            downdeg = currdeg;
//
//            invalidate();
//            return true;
//
//        }
//        if (e.getAction() == MotionEvent.ACTION_UP) {
//            if (mCrollerChangeListener != null) {
//                mCrollerChangeListener.onStopTrackingTouch(this);
//                startEventSent = false;
//            }
//            return true;
//        }
//        return super.onTouchEvent(e);
//    }




    // new function for the nobe thing .



    private void getUInfo() {

        AccountOne val = new AccountOne();
        val.apikey = prefManager.getApikey();

        compositeDisposable.add(apiService.getuserdata2("v1/getuserdata2",val)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<UserData>>(true, this) {
                                   @Override
                                   public void onApiSuccess(BaseResponse baseResponse) {
                                       if (baseResponse.statuscode == 1) {

                                           UserData userData = (UserData) baseResponse.data;


                                           //  userData.address

                                           if (!userData.address.equalsIgnoreCase("")) {

                                               System.out.println("Getting user location here"+userData.address +"lat"+userData.lat+"lng"+userData.lng);





                                           } else {



                                             //TODO Location inforamtion  short

                                            //   startLocationUpdates();

                                           }


                                       } else if (baseResponse.statuscode == 0) {

                                           //Sorry some the data get expried ;
                                       }

                                   }

                                   @Override
                                   public void onFailure(Throwable e) {

                                   }
                               }

                )


        );


    }




}
