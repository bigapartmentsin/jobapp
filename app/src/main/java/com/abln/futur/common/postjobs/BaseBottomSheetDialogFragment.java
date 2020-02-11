package com.abln.futur.common.postjobs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.R;
import com.abln.futur.common.ConstantUtils;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.Service;
import com.abln.futur.common.Handler;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;


public abstract class BaseBottomSheetDialogFragment extends BottomSheetDialogFragment implements BaseView {

    public String TAG = getClass().getSimpleName();
    protected PrefManager prefManager;
    protected CompositeDisposable compositeDisposable;
    protected Handler apiService;
    private Unbinder unbinder;
    private BottomSheetHandler bottomSheetHandler;
    private Dialog progressSend;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(ConstantUtils.SCREEN_NAME,getClass().getSimpleName());
        prefManager = PrefManager.getInstance();
        compositeDisposable = new CompositeDisposable();
        apiService = Service.getApiService();
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

    public abstract int getLayoutId();

    private void initDialog(View view) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        //int height = displaymetrics.heightPixels;
        int screenHeight = displaymetrics.heightPixels;
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) view.getParent());
        mBehavior.setPeekHeight(screenHeight);
        mBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                    if (bottomSheetHandler != null) {
                        bottomSheetHandler.onClose();
                    }
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
    }

    public BottomSheetHandler getBottomSheetHandler() {
        return bottomSheetHandler;
    }

    public void setBottomSheetHandler(BottomSheetHandler bottomSheetHandler) {
        this.bottomSheetHandler = bottomSheetHandler;
    }

    public void baseBottomDissmiss() {

        dismiss();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog(getView());
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

    public void addFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getChildFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void replaceFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getChildFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public interface BottomSheetHandler {
        void onClose();
    }



    @Override
    public int getTheme() {

        return R.style.BottomSheetDialogTheme;
    }

}
