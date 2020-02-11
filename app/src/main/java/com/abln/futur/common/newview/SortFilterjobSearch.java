package com.abln.futur.common.newview;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.abln.futur.R;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.postjobs.post;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SortFilterjobSearch extends SmallFragment {

    PrefManager prefManager = new PrefManager();

    @BindView(R.id.expSeekbar)
    SeekBar seekbar;

    @BindView(R.id.tvExpYrs)
    TextView textView;


    @BindView(R.id.doneBtn)
    TextView applybutton;


    //getExperience;

    public static SortFilterjobSearch newInstance() {

        SortFilterjobSearch fragment = new SortFilterjobSearch();
        return fragment;

    }

    @Override
    public int getLayoutId() {
        return R.layout.filterlayout_job_search;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);


    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExperience();


    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        seekbar.setMin(0);
        seekbar.setMax(12);


        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                textView.setText(new Integer(progress) + "+");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        applybutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtility.showToastMsg_withAlertInfoShort(getActivity(), "Apply ");
                Intent i = new Intent(getActivity(), DataView.class);
                getActivity().startActivity(i);

                dismiss();
            }
        });
    }

    private void getExperience() {
        post dataset = new post();
        dataset.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getExperience("v1/experience",dataset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Experience>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        Experience experience = (Experience) baseResponse.data;


                        if (baseResponse.statuscode == 1) {


                            String exp = experience.exp;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                seekbar.setProgress(Integer.parseInt(exp));


                            }


                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Network Error ");

                    }
                }));
    }


}
