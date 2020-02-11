package com.abln.futur.common.newview;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.abln.futur.common.Service;
import com.abln.futur.common.Handler;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.postjobs.BaseView;
import com.abln.futur.common.postjobs.ProgressBarUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class SmallFragment extends DialogFragment implements BaseView {


    public String TAG = getClass().getSimpleName();
    protected CompositeDisposable compositeDisposable;
    protected Handler apiService;
    private Unbinder unbinder;
    private Dialog progressSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        apiService = Service.getApiService();
        compositeDisposable = new CompositeDisposable();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        compositeDisposable.dispose();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, UIUtility.getScreenHeight() / 2);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }


    public abstract int getLayoutId();


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void showProgress(String text) {
        progressSend = ProgressBarUtil.create(getActivity(), text);
        progressSend.show();
    }

    @Override
    public void showErrorDialog(String title, String description, String positiveBtn) {
    }


    @Override
    public void dismissProgress() {
        try {
            if (null != progressSend && progressSend.isShowing())
                progressSend.cancel();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void showInternetError() {

    }


}
