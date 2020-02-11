package com.abln.futur.common;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BottomNav extends BottomSheetDialogFragment {

    public String TAG = getClass().getSimpleName();
    protected CompositeDisposable compositeDisposable;
    protected PrefManager prefManager;
    protected Handler apiService;
    private Unbinder unbinder;
    private BottomSheetHandler bottomSheetHandler;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initDialog(getView());
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

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        super.startActivityForResult(intent, requestCode);
    }


    public interface BottomSheetHandler {
        void onClose();
    }
}
