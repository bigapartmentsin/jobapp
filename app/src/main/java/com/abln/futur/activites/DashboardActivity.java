package com.abln.futur.activites;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.abln.chat.ChatsViewFragment;
import com.abln.chat.ui.Network.DataParser;
import com.abln.chat.ui.activities.IMChatActivity;
import com.abln.chat.utils.CircleImageView;
import com.abln.futur.BuildConfig;
import com.abln.futur.R;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturApiClient;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.InboxItem;
import com.abln.futur.common.ModChat;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AccountOne;
import com.abln.futur.common.models.UserData;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.customViews.FragmentAdapter;
import com.abln.futur.interfaces.NetworkOperation;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.listeners.ProgressRequestBody;
import com.abln.futur.module.account.fragments.AccountFragment;
import com.abln.futur.module.base.adapter.UpdateLocationRequest;
import com.abln.futur.module.chats.adapter.ChatsFragment;
import com.abln.futur.module.global.activities.GlobalFragment;
import com.abln.futur.module.global.activities.MapsActivity;
import com.abln.futur.services.NetworkOperationService;
import com.abln.futur.utils.FuturNotificationHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;

public class DashboardActivity extends BaseActivity implements TaskCompleteListener, EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks ,InboxItem.clickHandler{

    private static final String TAG = "DashBoardActivity";
    private static final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 1010;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 1000000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 500000;
    private static final int REQUEST_CHECK_SETTINGS = 100;
    private static final String[] LOCATION_AND_CONTACTS =
            {Manifest.permission.ACCESS_FINE_LOCATION};

    private static final int RC_LOCATION_CONTACTS_PERM = 124;

    BottomSheetDialog bts;
    View sheetView;

   // ArrayList<ModChat> final_list;


    //urls array that should be shown as a story
    private final String[] resources = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00001.jpg?alt=media&token=460667e4-e084-4dc5-b873-eefa028cec32",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00002.jpg?alt=media&token=e8e86192-eb5d-4e99-b1a8-f00debcdc016",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00004.jpg?alt=media&token=af71cbf5-4be3-4f8a-8a2b-2994bce38377",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00005.jpg?alt=media&token=7d179938-c419-44f4-b965-1993858d6e71",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00006.jpg?alt=media&token=cdd14cf5-6ed0-4fb7-95f5-74618528a48b",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00007.jpg?alt=media&token=98524820-6d7c-4fb4-89b1-65301e1d6053",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00008.jpg?alt=media&token=7ef9ed49-3221-4d49-8fb4-2c79e5dab333",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00009.jpg?alt=media&token=00d56a11-7a92-4998-a05a-e1dd77b02fe4",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00010.jpg?alt=media&token=24f8f091-acb9-432a-ae0f-7e6227d18803",
    };
    int PLACE_PICKER_REQUEST = 1;
    @BindView(R.id.tvChat)
    TextView tvChat;

    @BindView(R.id.tvGlobal)
    TextView tvGlobal;

    @BindView(R.id.tvAcc)
    TextView tvAccount;

    @BindView(R.id.chat_view)
    View viewChat;

    @BindView(R.id.global_view)
    View viewGlobal;

    @BindView(R.id.account_view)
    View viewAccount;

    @BindView(R.id.edit_spinner)
    TextView epEditSpinner;

    @BindView(R.id.llchatMenu)
    RelativeLayout rlChatMenu;

    @BindView(R.id.accountMenu)
    RelativeLayout rlAccountMenu;

    @BindView(R.id.globalMenu)
    RelativeLayout rlGlobalMenu;

    @BindView(R.id.dashPages)
    ViewPager pager;

    @BindView(R.id.img_User)
    CircleImageView ivUserAvatar;


    @BindView(R.id.home_location_tag)
    TextView homelocationTag;

    @BindView(R.id.pager)
    ViewPager viewPager;


    private int REQUEST_CAMERA = 000, SELECT_FILE = 222;
    private Fragment[] mfragments;
    private ChatsFragment chatsFragment;
    private GlobalFragment globalFragment;
    private AccountFragment accountFragment;


    // adding new chatfragment manager ;

    private ChatsViewFragment viewFragment;

    //

    private int index;
    private int currentTabIndex = 1;
    private PrefManager prefManager = new PrefManager();
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    public  LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    public  Location mCurrentLocation;
    private double sourceLat = 0, sourceLag = 0;
    private Context mContext;
    private String[] locationNames = {"Current Location", "Custom Location", "Hyderabad", "Bengaluru", "Pune", "Ahmedabad"};
    private String pictureImagePath = "";
    private String picturePath = "";

    String popUpContents[];
    PopupWindow popupWindowCity;
    private ArrayList<Fragment> fragmentArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setTaskCompleteListener(this);

        ButterKnife.bind(this);
        mContext = this;

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_dropdown_item, locationNames);

        initdata();
        //TODO location tag .
        //homelocationTag
        getUserDatainfo();
        getUInfo();

        List<String> citylilst = new ArrayList<String>();
        citylilst.add("Current Location::1");
        citylilst.add("Custom Location::2");
        citylilst.add("Bengaluru::3");
        citylilst.add("Hyderabad::4");


        // convert to simple array
        popUpContents = new String[citylilst.size()];
        citylilst.toArray(popUpContents);

        popupWindowCity = popupWindowCity();


        epEditSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //  popupWindowCity.showAsDropDown(v, -5, 0);

                setPopup();

            }
        });


        initLocation();

       // chatsFragment = new ChatsFragment();
        globalFragment = new GlobalFragment();
        accountFragment = new AccountFragment();


        viewFragment = new ChatsViewFragment();




        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(viewFragment);
        fragmentArrayList.add(globalFragment);
        fragmentArrayList.add(accountFragment);



        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, fragmentArrayList);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(3);
        selectTab(0);
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }
        });




        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

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


        if (prefManager.getUserPhoto() == null) {




        } else {
            ivUserAvatar.setBackgroundDrawable(null);
            ivUserAvatar.setImageBitmap(prefManager.getUserPhoto());
        }


        rlChatMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();
            }
        });


        // TODO
        //getsaveddata();


    }



    //TODO imageupload avatar .




    private void setPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);


// add a list

        builder.setItems(locationNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: //
                        updateLocationUI();
                        break;
                    case 1: //
                        showPlacePicker();
                    case 2:

                    case 3:

                    case 4:
                }
            }
        });

// create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private ArrayAdapter<String> cityAdapter(String cityArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cityArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                // setting the ID and text for every items in the list
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                // visual settings for the list item
                TextView listItem = new TextView(DashboardActivity.this);

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(13);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.BLACK);
                listItem.setBackgroundColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }


    public PopupWindow popupWindowCity() {

        // initialize a pop up window type
        PopupWindow popupWindow = new PopupWindow(this);

        // the drop down list is a list view
        ListView lsvcity = new ListView(this);

        // set our adapter and pass our pop up window contents
        lsvcity.setAdapter(cityAdapter(popUpContents));

        // set the item click listener
        lsvcity.setOnItemClickListener(new CityDropdownOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(450);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        // set the list view as pop up window content
        popupWindow.setContentView(lsvcity);

        return popupWindow;
    }

    private void diloglist() {


    }

    private void checkforhomelocation() {
        if (prefManager.getMylocationLat().equalsIgnoreCase("") && prefManager.getMylocationLat().equalsIgnoreCase("")) {

            Log.d(TAG, "Sorry the location have not been updated so far ");


        } else {


            ProgressRequestBody.UploadCallbacks callbacks = new ProgressRequestBody.UploadCallbacks() {
                @Override
                public void onProgressUpdate(int percentage) {

                    UIUtility.showToastMsg_withSuccessShort(mContext, "getActivty");
                }

                @Override
                public void onError() {

                    UIUtility.showToastMsg_withAlertInfoShort(mContext, "Change the uses to update the locaiton ");

                }

                @Override
                public void onFinish() {

                    UIUtility.showToastMsg_withErrorShort(mContext, "update the loast location ");

                }
            };


            UpdateLocationRequest updateLocationRequest = new UpdateLocationRequest();
            updateLocationRequest.setApikey(prefManager.getApikey());

            updateLocationRequest.setAddress(prefManager.getMyaddress());

            updateLocationRequest.setLat(prefManager.getMylocationLat());
            updateLocationRequest.setLng(prefManager.getMylocationLat());

            HashMap<String, String> headermap = new HashMap<>();
            headermap.put("Content-Type", "applications/json");


            Intent intent = new Intent(mContext, NetworkOperationService.class);
            intent.putExtra(NetworkConfig.API_URL, NetworkConfig.updateLocation);
            intent.putExtra(NetworkConfig.HEADER_MAP, headermap);
            headermap.put("Content-type", "application/json");
            intent.putExtra(NetworkConfig.INPUT_BODY, updateLocationRequest);
            mContext.startService(intent);


        }
    }


    private void setHomeLocation(String lat, String lng) {

        Intent intent = new Intent(getLocalClassName().concat("HOME"));
        intent.putExtra(NetworkConfig.REQUEST_TYPE, "update the location ");
        intent.putExtra(NetworkConfig.HEADER_MAP, "");

    }


    @OnClick({R.id.tvChat, R.id.tvGlobal, R.id.tvAcc, R.id.img_loctn, R.id.accountMenu})
    void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.tvChat:
                selectTab(0);
                break;
            case R.id.tvGlobal:
                selectTab(1);
                break;
            case R.id.tvAcc:
                selectTab(2);
                break;

            case R.id.img_loctn:
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
                break;

            case R.id.accountMenu:
                checkForPermissionAndTakeImage();
                break;

            default:
                break;
        }

        currentTabIndex = index;

    }


    private void selectTab(int position) {
        index = position;
        switch (position) {
            case 0:
                //llchatMenu
                viewChat.setVisibility(View.VISIBLE);
                viewGlobal.setVisibility(View.INVISIBLE);
                viewAccount.setVisibility(View.INVISIBLE);

                tvChat.setTypeface(Typeface.DEFAULT_BOLD);
                tvGlobal.setTypeface(Typeface.DEFAULT);
                tvAccount.setTypeface(Typeface.DEFAULT);

                rlChatMenu.setVisibility(View.VISIBLE);
                rlGlobalMenu.setVisibility(View.INVISIBLE);
                rlAccountMenu.setVisibility(View.INVISIBLE);

                break;

            case 1:
                viewChat.setVisibility(View.INVISIBLE);
                viewGlobal.setVisibility(View.VISIBLE);
                viewAccount.setVisibility(View.INVISIBLE);

                tvChat.setTypeface(Typeface.DEFAULT);
                tvGlobal.setTypeface(Typeface.DEFAULT_BOLD);
                tvAccount.setTypeface(Typeface.DEFAULT);

                rlChatMenu.setVisibility(View.INVISIBLE);
                rlGlobalMenu.setVisibility(View.VISIBLE);
                rlAccountMenu.setVisibility(View.INVISIBLE);

                break;


            case 2:
                viewChat.setVisibility(View.INVISIBLE);
                viewGlobal.setVisibility(View.INVISIBLE);
                viewAccount.setVisibility(View.VISIBLE);

                tvChat.setTypeface(Typeface.DEFAULT);
                tvGlobal.setTypeface(Typeface.DEFAULT);
                tvAccount.setTypeface(Typeface.DEFAULT_BOLD);

                rlChatMenu.setVisibility(View.INVISIBLE);
                rlGlobalMenu.setVisibility(View.INVISIBLE);
                rlAccountMenu.setVisibility(View.VISIBLE);

                break;
        }


//        if (currentTabIndex != index) {
//            FragmentTransaction trx = getSupportFragmentManager()
//                    .beginTransaction();
//            trx.hide(mfragments[currentTabIndex]);
//            if (!mfragments[index].isAdded()) {
//                trx.add(R.id.container, mfragments[index]);
//            }
//            trx.show(mfragments[index]).commit();
//        }
//        currentTabIndex = index;

       viewPager.setCurrentItem(position);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    private void launchStories() {
    }

    private void updateLocationUI() {
        if (mCurrentLocation != null) {

            Geocoder geocoder = null;
            List<Address> addresses = null;

            try {
                geocoder = new Geocoder(mContext, Locale.getDefault());
                addresses = geocoder.getFromLocation(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

            } catch (Exception e) {
                e.printStackTrace();
            }
            if (addresses != null) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                epEditSpinner.setText(address);
                homelocationTag.setText("Current Location");
                //  epEditSpinner.setSelection(epEditSpinner.getText().length());
                //     epEditSpinner.requestFocus();


                sourceLat = mCurrentLocation.getLatitude();
                sourceLag = mCurrentLocation.getLongitude();
                String stgLat = String.valueOf(sourceLat);
                String stgLag = String.valueOf(sourceLag);
                System.out.println("Changing the lat long  "+stgLat+"-"+stgLag);
                prefManager.setLat(stgLat);
                prefManager.setLon(stgLag);
                prefManager.setMylocationLat(stgLat);
                prefManager.setMylocationLon(stgLag);
                prefManager.setMyaddress(address);
                IMChatActivity.latitude = prefManager.getMylocationLat();
                IMChatActivity.longitude = prefManager.getMylocationLon();
            }
        } else {

            locationAndContactsTask();

            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Accessing Current Location");
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
                //TODO UPDated the location
                updateLocationUI();
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

                        updateLocationUI();
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
                                    rae.startResolutionForResult(getParent(), REQUEST_CHECK_SETTINGS);
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

                        updateLocationUI();
                    }
                });
    }

    private void openSettings() {
        Intent intent = new Intent();
        intent.setAction(
                Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package",
                BuildConfig.APPLICATION_ID, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        startActivity(intent);
    }


    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyBpFqMMVYzhJollDlqLgouez9vJVj33Wpg")
                .setMapsApiKey("AIzaSyDTJeRUhI_2G4rC-an7_ZRXs02CxLexitw");

        try {
            Intent placeIntent = builder.build(DashboardActivity.this);
            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
        } catch (Exception ex) {
            // Google Play services is not available...
        }
    }


    String userprofile;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
//        fragment.onActivityResult(requestCode, resultCode, data);


        int currentItem = viewPager.getCurrentItem();
        Fragment fragment = fragmentArrayList.get(currentItem);
        fragment.onActivityResult(requestCode, resultCode, data);


        if ((requestCode == PLACE_PICKER_REQUEST) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                // updateLocation(place);

                String address = place.getAddress();
                epEditSpinner.setText(address);
                homelocationTag.setText("Current Location");
                String lat = String.valueOf(place.getLatLng().latitude);
                String lng = String.valueOf(place.getLatLng().longitude);


                System.out.println("Location data changed in place picker");

                prefManager.setLat(lat);

                prefManager.setLon(lng);


            }
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);
                userprofile = getFilePathFromURI(getApplicationContext(), uri);
                //TODO upload avater ;
                Glide.with(getApplicationContext()).load(userprofile)
                        .apply(RequestOptions.bitmapTransform(new RoundedCorners(14)))
                        .into(ivUserAvatar);

            }
        }

        if (requestCode == REQUEST_CAMERA) {


            File imgFile = new File(pictureImagePath);
            if (imgFile.exists()) {
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivUserAvatar.setBackgroundDrawable(null);
                ivUserAvatar.setImageBitmap(myBitmap);
                prefManager.setUserPhoto(myBitmap);
                uploadProfile(pictureImagePath);
            }


        }


        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {



            String yes = getString(R.string.yes);
            String no = getString(R.string.no);

            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(
                    this,
                    getString(R.string.returned_from_app_settings_to_activity,
                            hasCameraPermission() ? yes : no,
                            hasLocationAndContactsPermissions() ? yes : no,
                            hasSmsPermission() ? yes : no),
                    Toast.LENGTH_LONG)
                    .show();



        }


    }

    public String getImageFilePath(Uri uri) {

        File file = new File(uri.getPath());
        String imagePath = "";
        String[] filePath = file.getPath().split(":");
        String image_id = filePath[filePath.length - 1];
        Cursor cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor != null) {
            cursor.moveToFirst();
            imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return imagePath;
    }


    public void updateLocationInfo(String lat, String lng, String fulladdress) {


        if (lat.isEmpty() || lng.isEmpty()) {
            askLocationPermission();

        } else {


            StringBuffer buffer = new StringBuffer();
            buffer.charAt(0);
        }


    }


    public void askLocationPermission() {

        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)

                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {


                        UIUtility.showToastMsg_withAlertInfoShort(getApplication(), "Alert Location information is need ");

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {


                        UIUtility.showToastMsg_withAlertInfoShort(getApplication(), "Sorry you have been declined the location permission");

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {


                        UIUtility.showToastMsg_withAlertInfoShort(getApplication(), "Access Rational permission to access the user location ");


                    }
                })
                .check();

    }


    private void uploadProfile(String imgName) {
        try {
            if (imgName == null) {
                return;
            }
            FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Uploading", "Profile Photo Upload started");
            File imageFile = new File(imgName);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
            MultipartBody.Part body = MultipartBody.Part.createFormData("img", imgName, requestFile);

            RequestBody partMap = RequestBody.create(okhttp3.MultipartBody.FORM, prefManager.getApikey());
            NetworkOperation apiService = FuturApiClient.getClient2().create(NetworkOperation.class);
            Call<Object> call = apiService.updateUserAvatar(body, partMap);

            call.enqueue(new Callback<Object>() {
                @Override
                public void onResponse(Call<Object> call, Response<Object> response) {
                    try {
                        UIUtility.showToastMsg_withSuccessShort(mContext, "User avatar updated successfully");
                        FuturNotificationHandler.removeAllNotifications();
                        FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Done !", "User avatar updated successfully");
                        requestForFileDeletion();
                    } catch (Exception ex) {
                        UIUtility.showToastMsg_withErrorShort(mContext, "Not Uploaded");
                        FuturNotificationHandler.removeAllNotifications();
                        FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Failed !", "Profile Photo Not Uploaded");
                    }
                }

                @Override
                public void onFailure(Call<Object> call, Throwable t) {
                    UIUtility.showToastMsg_withErrorShort(mContext, "Upload Failed");
                    FuturNotificationHandler.removeAllNotifications();
                    FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Failed !", "Profile Photo Upload Failed");
                }
            });
        } catch (Exception e) {
            FuturNotificationHandler.removeAllNotifications();
        }
    }

    private void requestForFileDeletion() {
        if (!pictureImagePath.equalsIgnoreCase("")) {
            File deleteImg = new File(pictureImagePath);
            if (deleteImg.exists()) {
                deleteImg.delete();
            }
        }
    }


    /*
     * Activity life cycyle*/

    @Override
    protected void onStop() {
        super.onStop();
        FLog.d(TAG, "onstop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        FLog.d(TAG, "onesume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FLog.d(TAG, "onrestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        FLog.d(TAG, "onPause");
    }

    private void checkForPermissionAndTakeImage() {
        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openStorage();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            //openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openStorage() {
        selectImage();
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Library", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(DashboardActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UIUtility.checkPermission(DashboardActivity.this);

                if (items[item].equals("Take Photo")) {
                    if (result)
                        checkCameraPermission();

                } else if (items[item].equals("Choose from Library")) {
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void checkCameraPermission() {
        Dexter.withActivity((Activity) mContext)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openBackCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
//                            openSettings();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    private void openBackCamera() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir;
        if (Build.VERSION.SDK_INT >= 24) {
            storageDir = new File(android.os.Environment.getExternalStorageDirectory(),
                    "Futur/");
        } else if (Build.VERSION.SDK_INT > 19) {
            storageDir = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
        } else {
            storageDir = new File(Environment.getExternalStorageDirectory(), "");
        }
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        Uri outputFileUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", file);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }


    private void getUserDatainfo() {


        AccountOne val = new AccountOne();
        val.apikey = prefManager.getApikey();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.userdatainfo);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, val);
        mContext.startService(intent);


    }


    private void initdata(){



        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL,"https://api.myjson.com/bins/hj7tc" );
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        mContext.startService(intent);




    }

    //Look for task has been completed or not .
    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        super.onTaskCompleted(context, intent);

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if (responseString != null && apiUrl.equals("https://api.myjson.com/bins/hj7tc")) {


//




            ModelData data = DataParser.parseJson(responseString, ModelData.class);



            if (data.getStatuscode() == 0) {

                UIUtility.showToastMsg_withSuccessShort(getApplicationContext(),"Loaded the data");

            }else if (data.getStatuscode() == 1){

                UIUtility.showToastMsg_withSuccessShort(getApplicationContext(),"Success the data");

            }


        }


    }


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

                                               epEditSpinner.setText(userData.address);
                                               prefManager.setLat(userData.lat);
                                               prefManager.setLon(userData.lng);
                                               homelocationTag.setText("Home Location");



                                           } else {
                                               homelocationTag.setText("Current Location");
                                               startLocationUpdates();

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


    @AfterPermissionGranted(RC_LOCATION_CONTACTS_PERM)
    public void locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            Toast.makeText(this, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show();
            //TODO location permission ;
            startLocationUpdates();

        } else {
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.rationale_location_contacts),
                    RC_LOCATION_CONTACTS_PERM,
                    LOCATION_AND_CONTACTS);
        }
    }


    private boolean hasLocationAndContactsPermissions() {
        return EasyPermissions.hasPermissions(getApplicationContext(), LOCATION_AND_CONTACTS);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }


    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d(TAG, "onRationaleAccepted:" + requestCode);
    }

    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d(TAG, "onRationaleDenied:" + requestCode);
    }


    private boolean hasStoragePermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private boolean hasSmsPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.READ_SMS);
    }

    private boolean hasCameraPermission() {
        return EasyPermissions.hasPermissions(this, Manifest.permission.CAMERA);
    }


    RecyclerView recyclerView;

    private void getDialog() {
        bts = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        TextView close;
        sheetView = getLayoutInflater().inflate(R.layout.popup_chat_inbox, null);
        recyclerView = sheetView.findViewById(R.id.itemview);
        close = sheetView.findViewById(R.id.tvExpYrs);
             getsaveddata();


            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


            //  }
        bts.setContentView(sheetView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bts.dismiss();

            }
        });

    }



// TODO main futncion to handel the data cluster .
    // handling the main function to clear the data sets and addingthe information
    //:Making meeh is the best thing i ever came accross the fuction to seek the chalanges .


    private InboxItem inboxitem;
    private void getsaveddata() {
        post datahandel = new post();
        datahandel.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getPosted("v1/short-postinfo",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<com.abln.futur.common.Address>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        com.abln.futur.common.Address data_list = (com.abln.futur.common.Address) baseResponse.data;
                        ArrayList<ModChat>  final_list = data_list.post_list;
                        inboxitem = new InboxItem(DashboardActivity.this, final_list,DashboardActivity.this);
                        recyclerView.setAdapter(inboxitem);
                        bts.show();
                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }

    @Override
    public void onapplyClick() {

        bts.dismiss();

       // getsaveddata();

    }
}
