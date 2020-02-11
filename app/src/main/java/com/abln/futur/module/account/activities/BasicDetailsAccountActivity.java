package com.abln.futur.module.account.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.FLog;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AccountOne;
import com.abln.futur.common.models.SetUser;
import com.abln.futur.common.models.UserData;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.module.account.datamodel.UpdateBasicInfo;
import com.abln.futur.module.base.adapter.UpdateLocationRequest;
import com.abln.futur.module.login.UserSignupProfileRequest;
import com.abln.futur.services.NetworkOperationService;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.gson.JsonObject;
import com.rtchagas.pingplacepicker.PingPlacePicker;

import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class BasicDetailsAccountActivity extends NewBaseActivity {


    private static int MAP_REQUEST_CODE = 421;
    //  @BindView(R.id.gender_type)
    // Spinner gender_spinner;
    // @BindView(R.id.name_edit_text)
    // TextInputEditText etName;
    int PLACE_PICKER_REQUEST = 1;


    @BindView(R.id.jobLocationtext)
    TextView joblocationinfo;

    @BindView(R.id.male)
    TextView male;

    @BindView(R.id.female)
    TextView female;

    @BindView(R.id.name_edit_text)
    EditText name_edit_text;


    @BindView(R.id.back_arrow)
    ImageView backarrow;

    String address;
    String value = "";
    private String[] gender = {"Gender", "Male", "Female", "Transgender"};
    private Context mContext;
    private PrefManager prefManager = new PrefManager();
    private String name = "", gendero = "", addresso = "";
    private String lat = "", lag = "", revgeo = "";


    private String uName ;
    private String uGender ;
    private String uaddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_basic_details_account;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContext = this;
        getUInfo();

        ArrayAdapter<String> e_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gender);
        e_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        name_edit_text.setText(prefManager.getUserName());

        if (prefManager.getGender().equalsIgnoreCase("Male")) {
            female.setBackground(null);
            male.setBackground(getDrawable(R.drawable.ic_rectanglein));
            value = "male";
            male.setTextColor(getColor(R.color.gendercolor));
            female.setTextColor(getColor(R.color.gender_default_text));

        } else if (prefManager.getGender().equalsIgnoreCase("Female")) {

            male.setBackground(null);
            female.setBackground(getDrawable(R.drawable.ic_rectanglein));
            female.setTextColor(getColor(R.color.gendercolor));
            value = "female";
            male.setTextColor(getColor(R.color.gender_default_text));


        }




        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                female.setBackground(null);
                female.setTextColor(getColor(R.color.gender_default_text));
                male.setBackground(getDrawable(R.drawable.ic_rectanglein));
                value = "male";
                male.setTextColor(getColor(R.color.gendercolor));


            }
        });


        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                male.setBackground(null);
                male.setTextColor(getColor(R.color.gender_default_text));
                female.setBackground(getDrawable(R.drawable.ic_rectanglein));

                value = "female";
                female.setTextColor(getColor(R.color.gendercolor));

            }
        });


    }

    @OnClick({R.id.jobLocation})
    void onclick(View v) {
        switch (v.getId()) {

            case R.id.jobLocation:
                showPlacePicker();


        }
    }

    private void showPlacePicker() {
        PingPlacePicker.IntentBuilder builder = new PingPlacePicker.IntentBuilder();
        builder.setAndroidApiKey("AIzaSyAcbb9wwo1yWoCwvPB1fq7LxKgFaFFZw4M")


                .setMapsApiKey("AIzaSyAcbb9wwo1yWoCwvPB1fq7LxKgFaFFZw4M");


        try {
            Intent placeIntent = builder.build(this);
            startActivityForResult(placeIntent, PLACE_PICKER_REQUEST);
        } catch (Exception ex) {
            // Google Play services is not available...
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        doValidation();
    }

    private void doValidation() {
        if (name_edit_text.getText().length() < 3) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Enter the name");
            return;
        }

        name = prefManager.getUserName();
        gendero = prefManager.getGender();


        if (!name.equalsIgnoreCase(name_edit_text.getText().toString())) {
            UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(), "Saved");

            updateLocation();
        }

        if (!gendero.equalsIgnoreCase(value)) {
            UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(), "Saved");
            updateLocation();
        }

//        if (uaddress.equalsIgnoreCase("") ){
//            updateLocation();
//        }

        updateLocation();


    }

    private void updateBasicInfoApi() {
        UpdateBasicInfo updateBasicInfo = new UpdateBasicInfo();
        updateBasicInfo.setApiKey(prefManager.getApikey());
        updateBasicInfo.setGender(prefManager.getGender());

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.updatebasicInfo);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, updateBasicInfo);
        mContext.startService(intent);
    }

    private void updateApi() {

        //    prefManager.setUserName(etName.getText().toString());
        //  prefManager.setGender(gender_spinner.getSelectedItem().toString());

        UserSignupProfileRequest userSignupProfileRequest = new UserSignupProfileRequest();
        userSignupProfileRequest.setApikey(prefManager.getApikey());
        userSignupProfileRequest.setGender(prefManager.getGender());
        userSignupProfileRequest.setName(prefManager.getUserName());
        userSignupProfileRequest.setTechnology(prefManager.getUserIndustry());
        userSignupProfileRequest.setOccupation(prefManager.getUserOccupation());
        userSignupProfileRequest.setExperience(prefManager.getUserExperinece());
        userSignupProfileRequest.setUniversity(prefManager.getUserUniversity());//TODO: add lat lon value

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.user_info);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, userSignupProfileRequest);
        mContext.startService(intent);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAP_REQUEST_CODE) {
            prefManager.setMylocationLat(prefManager.getSelectedLat());
            prefManager.setMylocationLon(prefManager.getSelectedLon());
        }


        if ((requestCode == PLACE_PICKER_REQUEST) && (resultCode == RESULT_OK)) {
            Place place = PingPlacePicker.getPlace(data);
            if (place != null) {
                information(place);
            }
        }
    }

    private void information(Place place) {


        address = place.getAddress();
        joblocationinfo.setText(address);

        uaddress = address;


        revgeo = address;

        LatLng latLng = place.getLatLng();
        lat = String.valueOf(latLng.latitude);
        lag = String.valueOf(latLng.longitude);

        prefManager.setLat(lat);
        prefManager.setLon(lag);


    }


    private void updateLocation() {


        prefManager.setGender(value);

//        if (address == null) {
//            address = "";
//            prefManager.setMyaddress("");
//        } else {
//            prefManager.setMyaddress(address);
//        }

        prefManager.setUserName(name_edit_text.getText().toString());

        changeData(lat,lag ,uaddress ,value , name_edit_text.getText().toString());



    }


    private void getUserData() {

    }



    public void onTaskCompleted(Context context, Intent intent) {

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.getAllPatient)) {


        }


    }




    private void updatelocaion() {


        GetUserRequest mFavoriteRquestBody = new GetUserRequest();
        mFavoriteRquestBody.setDocId("ed3202810f1407e97b53fb759d631c29");
        mFavoriteRquestBody.setCount(2);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getAllPatient);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mContext.startService(intent);
    }


    private void gpsServerData() {
        if (prefManager.getMylocationLat().equalsIgnoreCase("") && prefManager.getMylocationLon().equalsIgnoreCase("")) {
            FLog.d("", "Failed Location");
        } else {
            UpdateLocationRequest updateLocationRequest = new UpdateLocationRequest();
            updateLocationRequest.setApikey(prefManager.getApikey());
            updateLocationRequest.setLat(prefManager.getMylocationLat());
            updateLocationRequest.setLng(prefManager.getMylocationLon());
            updateLocationRequest.setAddress(prefManager.getMyaddress());
            HashMap<String, String> headerMap = new HashMap<>();
            headerMap.put("Content-Type", "application/json");

            Intent intent = new Intent(mContext, NetworkOperationService.class);
            intent.putExtra(NetworkConfig.API_URL, NetworkConfig.updateLocation);
            intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
            intent.putExtra(NetworkConfig.INPUT_BODY, updateLocationRequest);
            mContext.startService(intent);
        }
    }



    private void  getUInfo(){

        AccountOne val = new AccountOne();
        val.apikey = prefManager.getApikey();

        compositeDisposable.add(apiService.getuserdata2("v1/getuserdata2",val)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<UserData>>(true,this)
                               {
                                   @Override
                                   public void onApiSuccess(BaseResponse baseResponse) {
                                       if (baseResponse.statuscode == 1 ){

                                           UserData userData = (UserData) baseResponse.data;
                                           System.out.println("Data:"+userData.apikey) ;
                                           System.out.println("Data:" +userData.address);

                                           lat = userData.lat;
                                           lag = userData.lng;
                                           uaddress = userData.address;


                                           if (uaddress.equalsIgnoreCase("")) {
                                               address = "Home Location";
                                           } else {
                                               joblocationinfo.setText(uaddress);
                                           }



                                       }
                                       else if(baseResponse.statuscode == 0 ){

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

    private void changeData(String l1 ,String l2,String ad , String gen ,String nam){

        SetUser user = new SetUser();
        user.apikey = prefManager.getApikey();
        user.address =ad;
        user.lat = l1;
        user.lng = l2 ;
        user.gender = gen;
        user.name = nam;






        compositeDisposable.add(apiService.setuserdata2("v1/basic-info",user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true,this)
                               {
                                   @Override
                                   public void onApiSuccess(BaseResponse baseResponse) {


                                       if (baseResponse.statuscode == 1 ){





                                       }
                                       else if(baseResponse.statuscode == 0 ){

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
