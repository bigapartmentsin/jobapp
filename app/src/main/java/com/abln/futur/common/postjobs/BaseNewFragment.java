package com.abln.futur.common.postjobs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.common.DataCache;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.Service;
import com.abln.futur.common.Handler;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class BaseNewFragment extends Fragment implements
        BaseView {

    public String TAG = getClass().getSimpleName();
    public DataCache dataCache;
    public PrefManager prefManager;
    protected Handler apiService;
    protected CompositeDisposable compositeDisposable;
    private Dialog progressSend;
    private Unbinder unbinder;

    @Override
    public void showProgress(String text) {
        progressSend = ProgressBarUtil.create(getActivity(), text);
        progressSend.show();
    }

    @Override
    public void showErrorDialog(String title, String description, String positiveBtn) {

    }

    public abstract int getLayoutId();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiService = Service.getApiService();

        dataCache = DataCache.getInstance();
        prefManager = PrefManager.getInstance();
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

    public void replaceFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.replace(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    public void addFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction.add(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }


}
