package com.abln.futur.module.login;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.R;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.google.android.material.textfield.TextInputEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class BasicDetailsFragment extends Fragment {

    @BindView(R.id.gender_type)
    Spinner gender_spinner;

    @BindView(R.id.name_edit_text)
    TextInputEditText etName;

    //https://stackoverflow.com/questions/55028892/how-can-i-create-custom-switches-in-android-with-text-on-both-side-of-switchs-t

    private String[] gender = {"Gender", "Male", "Female", "Transgender"};

    private ProfessionDetailsFragment professionDetailsFragment;
    private Context mContext;
    private PrefManager prefManager = new PrefManager();


    public BasicDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_basic_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        professionDetailsFragment = new ProfessionDetailsFragment();

        ArrayAdapter<String> e_type = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, gender);
        e_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender_spinner.setAdapter(e_type);


    }

    @OnClick({R.id.next})
    void onCLick(View v) {
        switch (v.getId()) {
            case R.id.next:
                doValidation();

        }
    }

    private void doValidation() {
        if (etName.getText().length() < 3) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Enter the name");
            return;
        }

        if (gender_spinner.getSelectedItem().toString().equalsIgnoreCase("Gender")) {
            UIUtility.showToastMsg_withErrorShort(mContext, "Select the gender");
            return;
        }
        updateFragments();
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


        prefManager.setUserName(etName.getText().toString());
        prefManager.setGender(gender_spinner.getSelectedItem().toString());
        FragmentTransaction trx = getActivity().getSupportFragmentManager().beginTransaction();
        trx.hide(BasicDetailsFragment.this);
        //TODO:  change the condition
        if (1 == 1) {
            trx.add(R.id.container, professionDetailsFragment);
        }
        trx.addToBackStack(null);
        trx.show(professionDetailsFragment).commit();


    }


}
