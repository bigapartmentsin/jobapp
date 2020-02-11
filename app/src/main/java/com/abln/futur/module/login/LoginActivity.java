package com.abln.futur.module.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.activites.DashboardActivity;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PinEntryEditText;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.account.datamodel.GetLoginRequest;
import com.abln.futur.module.account.datamodel.GetLoginResponse;
import com.abln.futur.module.account.datamodel.GetSignUpOTPRequest;
import com.abln.futur.services.NetworkOperationService;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements TaskCompleteListener {

    @BindView(R.id.mobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.btnGetOtp)
    TextView btnGetOTP;

    @BindView(R.id.tvUserName)
    TextView tvUserName;

    @BindView(R.id.countryCodePicker)
    CountryCodePicker countryCode;

    @BindView(R.id.verifyOTP)
    LinearLayout llverifyotp;

    @BindView(R.id.cancel_txt)
    TextView tvCancel;

    @BindView(R.id.customOTP)
    PinEntryEditText customOTP;

    @BindView(R.id.loginCountDownTxt)
    TextView tvTimer;

    @BindView(R.id.resendOTP)
    TextView tvresend;

    @BindView(R.id.tvVoiceOTP)
    TextView tvVoiceOTP;


    @BindView(R.id.btnVerifyOtp)
    TextView btnVerifyOtp;


    private BasicDetailsFragment basicDetailsFragment;
    private TextWatcher phoneNumberWatcher;
    private Context mContext;
    private NetworkOperationService mService;
    private PrefManager prefManager = new PrefManager();
    private int mOtpReceived = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
        mContext = this;
        mService = new NetworkOperationService();
        basicDetailsFragment = new BasicDetailsFragment();
        setTaskCompleteListener(this);

        phoneNumberWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if (etMobileNumber.getText().length() == 10) {
                    makeCallToGetLoginDetails();
                } else {

                }

            }
        };
        etMobileNumber.addTextChangedListener(phoneNumberWatcher);
    }

    private void makeCallToGetLoginDetails() {
        FuturProgressDialog.show(mContext, false);
        GetLoginRequest getLoginRequest = new GetLoginRequest();
        getLoginRequest.setMobile(countryCode.getSelectedCountryCodeWithPlus() + etMobileNumber.getText().toString());


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.login);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, getLoginRequest);
        mContext.startService(intent);
        etMobileNumber.setEnabled(false);
        btnGetOTP.setEnabled(false);
    }


    @OnClick({R.id.btnVerifyOtp, R.id.cancel_txt, R.id.btnGetOtp, R.id.back_arrow})
    void onCLick(View v) {
        switch (v.getId()) {

            case R.id.cancel_txt:
                etMobileNumber.setText("");
                etMobileNumber.setEnabled(true);
                btnGetOTP.setEnabled(false);
                llverifyotp.setVisibility(View.INVISIBLE);
                btnGetOTP.setTextColor(getResources().getColor(R.color.color_gray));
                btnGetOTP.setBackground(getResources().getDrawable(R.drawable.border_corner_gray));
                break;


            case R.id.btnGetOtp:
                sendActualOTP();
                break;

//            case R.id.btnVerifyOtp:
//                updateFragments();
//                break;


            case R.id.btnVerifyOtp:

                if (etMobileNumber.getText().length() < 9) {
                    UIUtility.showToastMsg_withErrorShort(mContext, "Enter the phone number !..");
                    return;
                }


                String otp = customOTP.getText().toString();
                if (otp.length() < 4) {
                    UIUtility.showToastMsg_withErrorShort(mContext, "Enter the 4 digit OTP !..");
                    return;
                } else if (otp.length() == 4) {

                    if (mOtpReceived == Integer.valueOf(otp)) {
                        UIUtility.showToastMsg_withSuccessShort(mContext, "OTP verified !..");
                        Intent ii = new Intent(mContext, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        this.finish();
                        ii.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(ii);
                    } else {
                        UIUtility.showToastMsg_withErrorShort(mContext, "Wrong OTP !..");
                        return;
                    }
                }
                break;

            case R.id.back_arrow:
                finish();
        }
    }


    private void sendActualOTP() {
        FuturProgressDialog.show(mContext, false);
        GetSignUpOTPRequest getSignUpOTPRequest = new GetSignUpOTPRequest();
        getSignUpOTPRequest.setMobile(countryCode.getSelectedCountryCodeWithPlus() + etMobileNumber.getText().toString());


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.otp);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, getSignUpOTPRequest);
        mContext.startService(intent);
        etMobileNumber.setEnabled(false);
        btnGetOTP.setEnabled(false);
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        super.onTaskCompleted(context, intent);

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equals(NetworkConfig.login)) {
            FuturProgressDialog.dismissDialog();
            GetLoginResponse data = DataParser.parseJson(responseString, GetLoginResponse.class);
            if (data.getStatuscode() == 0) {
                UIUtility.showToastMsg_withErrorShort(mContext, "Your phone number is not present!!  Please Signup");
                return;
            } else {
                if (data.getStatuscode() == 1) {

                    btnGetOTP.setEnabled(true);
                    btnGetOTP.setTextColor(getResources().getColor(R.color.color_white));
                    btnGetOTP.setBackground(getResources().getDrawable(R.drawable.round_purple_get_otp));

                    prefManager.setApikey(data.getData().getApikey());
                    prefManager.setMobilenumber(data.getData().getMobile());
                    getUserProfileDetails();
                    return;
                }
            }
        }


        if (responseString != null && apiUrl.equals(NetworkConfig.otp)) {
            FuturProgressDialog.dismissDialog();
            GetLoginOtpResponse data = DataParser.parseJson(responseString, GetLoginOtpResponse.class);
            if (data.getStatuscode() == 1) {
                UIUtility.showToastMsg_withSuccessShort(mContext, "OTP sent");
                llverifyotp.setVisibility(View.VISIBLE);
                mOtpReceived = data.getData();
                setTimer(300);
                return;
            } else {

            }
        }


        if (responseString != null && apiUrl.equals(NetworkConfig.get_user_data)) {
            GetUserDetailsResponse data = DataParser.parseJson(responseString, GetUserDetailsResponse.class);
            if (data.getStatuscode() == 1) {
                prefManager.setMobilenumber(data.getData().getMobile());
                prefManager.setUserName(data.getData().getFirstName());
                prefManager.setUserOccupation(data.getData().getOccupation());
                prefManager.setUserIndustry(data.getData().getTechonlogy());
                prefManager.setGender(data.getData().getGender());
                prefManager.setCountrycodewithPlus(data.getData().getMobileCode());
                prefManager.setUserExperinece(data.getData().exp);   //TODO Experience not coming from backend
                prefManager.setUserUniversity(data.getData().getUniversity());//
                prefManager.setCountrycodewithPlus(data.getData().getMobileCode());
                return;
            } else {

            }
        }


    }


    private void getUserProfileDetails() {
        GetUserDetails getUserDetails = new GetUserDetails();
        getUserDetails.setApikey(prefManager.getApikey());
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.get_user_data);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, getUserDetails);
        mContext.startService(intent);
    }

    private void setTimer(int timeinSeconds) {
        tvTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(timeinSeconds * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                millisUntilFinished = millisUntilFinished / 1000;
                long min = millisUntilFinished / 60;
                long sec = millisUntilFinished % 60;
                String arg1 = String.valueOf(min);
                String arg2 = String.valueOf(sec);
                if (arg1.length() == 1)
                    arg1 = "0" + arg1;
                if (arg2.length() == 1)
                    arg2 = "0" + arg2;
                tvTimer.setText(arg1 + ":" + arg2);
            }

            public void onFinish() {
                btnGetOTP.setEnabled(true);
                etMobileNumber.setEnabled(true);
            }
        }.start();
    }
}
