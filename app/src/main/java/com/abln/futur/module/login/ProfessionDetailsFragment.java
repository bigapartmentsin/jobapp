package com.abln.futur.module.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.activites.DashboardActivity;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.fragments.BaseFragment;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.account.datamodel.GetLoginResponse;
import com.abln.futur.services.NetworkOperationService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfessionDetailsFragment extends BaseFragment implements TaskCompleteListener {

    @BindView(R.id.expSeekbar)
    SeekBar expSeekbar;

    @BindView(R.id.tvExpYrs)
    TextView tvExpyrs;

    @BindView(R.id.type_of_employment)
    Spinner spinnerIndustry;

    @BindView(R.id.occupation_edit_text)
    TextInputEditText tvOccupation;

    private Context mContext;
    private NetworkOperationService mService;
    private PrefManager prefManager = new PrefManager();

    private String[] industry = new String[]{
            "Select industry",
            "Tech",
            "Arts & Entertainment",
            "Banking",
            "Consulting",
            "Media & Journalism",
            "VC & Investment",
            "Education & Academia",
            "Government & Politics",
            "Advertising",
            "Property Sales & Letting",
            "Construction",
            "Food & Beverages",
            "Beauty & Wellness",
            "Driver & Delivery",
            "Events & Promotion",
            "Travel & Hospitality",
            "Manufacturing",
            "Others"
    };




    public ProfessionDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profession_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mService = new NetworkOperationService();
        setTaskCompleteListener(mContext, this);
        ArrayAdapter<String> e_type = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, industry);
        e_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIndustry.setAdapter(e_type);

        expSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                tvExpyrs.setText("" + i + " +");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @OnClick(R.id.btnDone)
    void onclick(View v) {
        switch (v.getId()) {
            case R.id.btnDone:
                doValidation();
        }
    }

    private void doValidation() {
        if (spinnerIndustry.getSelectedItem().toString().equalsIgnoreCase("Select industry")) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Select industry");
            return;
        }

        if (tvOccupation.getText().toString().length() < 3) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Enter occupation");
            return;
        }

        prefManager.setUserIndustry(spinnerIndustry.getSelectedItem().toString());


        String str = tvExpyrs.getText().toString().trim();
        String strNew = str.replace("+", "");
        prefManager.setUserExperinece(strNew);


        prefManager.setUserOccupation(tvOccupation.getText().toString());

        makeApiCalltoUpdateProfile();
    }

    private void makeApiCalltoUpdateProfile() {
        FuturProgressDialog.show(mContext, false);
        UserSignupProfileRequest userSignupProfileRequest = new UserSignupProfileRequest();
        userSignupProfileRequest.setApikey(prefManager.getApikey());
        userSignupProfileRequest.setGender(prefManager.getGender());
        userSignupProfileRequest.setName(prefManager.getUserName());
        userSignupProfileRequest.setTechnology(prefManager.getUserIndustry());
        userSignupProfileRequest.setOccupation(prefManager.getUserOccupation());
        userSignupProfileRequest.setExperience(prefManager.getUserExperinece());
        userSignupProfileRequest.setUniversity(prefManager.getUserUniversity());

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.user_info);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, userSignupProfileRequest);
        mContext.startService(intent);

    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equals(NetworkConfig.user_info)) {
            FuturProgressDialog.dismissDialog();
            GetLoginResponse data = DataParser.parseJson(responseString, GetLoginResponse.class);
            if (data.getStatuscode() == 0) {

                UIUtility.showToastMsg_withErrorShort(mContext, data.getStatusMessage());
                return;
            } else {
                if (data.getStatuscode() == 1) {
                    UIUtility.showToastMsg_withSuccessShort(mContext, "Profile created successfully !!");
                    Intent ii = new Intent(this.getContext(), DashboardActivity.class);
                    ii.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ii);
                    getActivity().finish();
                }
            }
        }

    }
}
