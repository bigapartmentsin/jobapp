package com.abln.futur.module.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PinEntryEditText;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.fragments.BaseFragment;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.account.datamodel.GetLoginRequest;
import com.abln.futur.module.account.datamodel.GetLoginResponse;
import com.abln.futur.module.account.datamodel.GetSignUpOTPRequest;
import com.abln.futur.module.account.datamodel.GetSignUpOTPResponse;
import com.abln.futur.services.NetworkOperationService;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends BaseFragment implements TaskCompleteListener {

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


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mService = new NetworkOperationService();
        basicDetailsFragment = new BasicDetailsFragment();
        setTaskCompleteListener(mContext, this);

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

            case R.id.back_arrow:
                //TODO
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
                        updateUserOTPinServer();
                        updateFragments();
                    } else {
                        UIUtility.showToastMsg_withErrorShort(mContext, "Wrong OTP !..");
                        return;
                    }
                }
                break;
        }
    }

    @Override
    public boolean onBackPressed() {
        return super.onBackPressed();
    }

    private void updateUserOTPinServer() {

        VerifyOtpRequest verifyOtpRequest = new VerifyOtpRequest();
        verifyOtpRequest.setApikey(prefManager.getApikey());
        verifyOtpRequest.setVerifyotp(String.valueOf(mOtpReceived));


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.verifiy_otp);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, verifyOtpRequest);
        mContext.startService(intent);
    }


    private void sendActualOTP() {
        FuturProgressDialog.show(mContext, false);
        GetSignUpOTPRequest getSignUpOTPRequest = new GetSignUpOTPRequest();
        getSignUpOTPRequest.setMobile(countryCode.getSelectedCountryCodeWithPlus() + etMobileNumber.getText().toString());
        getSignUpOTPRequest.setMobile_code(countryCode.getSelectedCountryCodeWithPlus());


        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);

        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.user_signUp);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, getSignUpOTPRequest);
        mContext.startService(intent);
        etMobileNumber.setEnabled(false);
        btnGetOTP.setEnabled(false);
    }

    private void updateFragments() {
//        if(prevselectedPos == -1){
//            UIUtility.showToastMsg_short(getActivity(), "Select your role");
//            return;
//        }
//
//        if( selectedPos == 10){
//            UIUtility.showToastMsg_short(getActivity(), "Select your role");
//            return;
//        }


        FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();
        trx.hide(SignUpFragment.this);
        //TODO:  change the condition
        if (1 == 1) {
            trx.add(R.id.container, basicDetailsFragment);
        }
        trx.addToBackStack(null);
        trx.show(basicDetailsFragment).commit();


    }


    @Override
    public void onTaskCompleted(Context context, Intent intent) {

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equals(NetworkConfig.login)) {
            FuturProgressDialog.dismissDialog();
            GetLoginResponse data = DataParser.parseJson(responseString, GetLoginResponse.class);
            if (data.getStatuscode() == 0) {
                btnGetOTP.setEnabled(true);
                btnGetOTP.setTextColor(getResources().getColor(R.color.color_white));
                btnGetOTP.setBackground(getResources().getDrawable(R.drawable.round_purple_get_otp));
            } else {
                if (data.getStatuscode() == 1) {
                    UIUtility.showToastMsg_withErrorShort(mContext, "Your phone number already exist !!  Please use login");
                    return;
                }
            }
        }


        if (responseString != null && apiUrl.equals(NetworkConfig.user_signUp)) {
            FuturProgressDialog.dismissDialog();
            GetSignUpOTPResponse data = DataParser.parseJson(responseString, GetSignUpOTPResponse.class);
            if (data.getStatuscode() == 1) {
                UIUtility.showToastMsg_withSuccessShort(mContext, "OTP sent");
                llverifyotp.setVisibility(View.VISIBLE);
                prefManager.setApikey(data.getData().getApikey());
                mOtpReceived = data.getData().getOtp();
                setTimer(300);
                return;
            } else {

            }
        }

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
