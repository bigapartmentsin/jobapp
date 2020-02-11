package com.abln.futur.module.account.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.UpdateProfessionDetails;
import com.abln.futur.module.account.datamodel.GetLoginResponse;
import com.abln.futur.module.account.datamodel.UpdateProfessionInfo;
import com.abln.futur.module.login.UserSignupProfileRequest;
import com.abln.futur.services.NetworkOperationService;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfessionDetailsAccountActivity extends BaseActivity {

    @BindView(R.id.expSeekbar)
    SeekBar expSeekbar;

    @BindView(R.id.tvExpYrs)
    TextView tvExpyrs;

    @BindView(R.id.type_of_employment)
    Spinner spinnerIndustry;

    @BindView(R.id.occupation_edit_text)
    EditText tvOccupation;

    @BindView(R.id.university_edit_text)
    EditText tvUniverisity;


    int pos = 0;
    private Context mContext;
    private NetworkOperationService mService;
    private PrefManager prefManager = new PrefManager();
    private String[] industry = new String[]{"Select industry", "Tech", "Arts & Entertainment",
            "Banking", "Consulting", "Media & Journalism", "VC & Investment", "Education & Academia", "Governement & Politics",
            "Advertising", "Property Sales & Letting", "Construction", "Food & Beverages", " Travel & Hospitality", "Manufacturing", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profession_details_account);

        ButterKnife.bind(this);
        mContext = this;
        mService = new NetworkOperationService();
        setTaskCompleteListener(this);
        ArrayAdapter<String> e_type = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, industry);
        e_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIndustry.setAdapter(e_type);

        for (int i = 0; i < industry.length; i++) {
            if (industry[i].equalsIgnoreCase(prefManager.getUserIndustry())) {
                pos = i;
            }
        }



        spinnerIndustry.setSelection(pos);
        tvOccupation.setText(prefManager.getUserOccupation());

        tvOccupation.setSelection(tvOccupation.getText().length());

        tvExpyrs.setText(prefManager.getUserExperinece()+"+");

        tvUniverisity.setText(prefManager.getUserUniversity());

        tvUniverisity.setSelection(tvUniverisity.getText().length());


        String exp  = tvExpyrs.getText().toString();
        exp.replaceAll("\\s+","");
        String  newexp =  exp.replace("+","");

       String data =  newexp.trim();





        int i= Integer.parseInt(data);





      
        expSeekbar.setMax(12);
        expSeekbar.setProgress(i);


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

    @Override
    public void onBackPressed() {
        super.onBackPressed();


     String exp  = tvExpyrs.getText().toString();
     exp.replaceAll("\\s+","");
     String newexp = exp.replace("+","");




        doValidation(newexp);
    }

    private void doValidation(String exp) {


        if (spinnerIndustry.getSelectedItem().toString().equalsIgnoreCase("Select industry")) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Select industry");
            return;
        }

        if (tvOccupation.getText().toString().length() < 3) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Enter occupation");
            return;
        }

        updateprofessionDetails(prefManager.getApikey(),spinnerIndustry.getSelectedItem().toString(),tvOccupation.getText().toString(),
                exp, tvUniverisity.getText().toString()

                );

        prefManager.setUserIndustry(spinnerIndustry.getSelectedItem().toString());
        prefManager.setUserExperinece(exp);
        prefManager.setUserOccupation(tvOccupation.getText().toString());
        prefManager.setUserUniversity(tvUniverisity.getText().toString());




    }





    private void updateprofessionDetails(String v1 ,String v2 ,String v3 ,String v4 ,String v5){



        UpdateProfessionDetails up = new UpdateProfessionDetails();
        up.apikey = v1;
        up.techonlogy = v2 ;
        up.occupation = v3 ;
        up.experience = v4 ;
        up.university = v5 ;

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.updateprofessionalinfo);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, up);
        mContext.startService(intent);





    }






    private void getUserdata(){

        UpdateProfessionDetails in = new UpdateProfessionDetails();
        in.apikey = prefManager.getApikey();
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.updateprofessionalinfo);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, in);
        mContext.startService(intent);




    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        super.onTaskCompleted(context, intent);


        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if (responseString != null && apiUrl.equals(NetworkConfig.updateprofessionalinfo)) {

            GetLoginResponse data = DataParser.parseJson(responseString, GetLoginResponse.class);
            if (data.getStatuscode() == 0) {
                return;
            } else {
                if (data.getStatuscode() == 1) {

                    return;
                }
            }
        }







    }
}
