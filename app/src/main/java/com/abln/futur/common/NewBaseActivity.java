package com.abln.futur.common;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.R;
import com.abln.futur.common.postjobs.BaseView;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

public abstract class NewBaseActivity extends AppCompatActivity implements
        BaseView, DialogUtils.DialogListener, ResponseHandler {


    public String TAG = getClass().getSimpleName();
    public DataCache dataCache;
    protected CompositeDisposable compositeDisposable;
    protected Handler apiService;
    protected PrefManager prefManager;
    private Dialog progressSend;
    private Unbinder unbinder;


    public abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        dataCache = DataCache.getInstance();
        apiService = Service.getApiService();
        unbinder = ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();

    }


    public void setUpToolbar(String title, boolean enableBack) {
        setUpToolbar(title, enableBack, 0);
    }

    public void setUpToolbar(String title, boolean enableBack, int icon) {
        try {
            Toolbar mToolbar = findViewById(R.id.toolbar);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle(null);
            mToolbar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(enableBack);
            if (icon != 0) {
                mToolbar.setNavigationIcon(icon);
            }
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void showProgress(String text) {
        // progressSend = ProgressBarUtil.create(this, text);
        // progressSend.show();
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
    public void showErrorDialog(String title, String description, String positiveBtn) {
        showAlertDialog(title, positiveBtn, null, description);
    }


    public void setTitle(String title) {
        setTitle(title, true);
    }

    public void setTitle(String title, boolean isBackButton) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(title);
            setSupportActionBar(toolbar);
            try {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(isBackButton);
                    getSupportActionBar().setDisplayShowHomeEnabled(isBackButton);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    NewBaseActivity.this.onBackPressed();
                }
            });
        }
    }


    @Override
    public void showInternetError() {

    }


    public void replaceFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    public void addFragment(int conainerId, Fragment fragment, boolean isAddToBackstack) {
        String tag = fragment.getClass().getSimpleName();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(conainerId, fragment, tag);
        if (isAddToBackstack) {
            fragmentTransaction.addToBackStack(tag);
        }
        fragmentTransaction.commit();
    }

    public void showAlertDialog(String titletext, String posBtnTxt, String negBtntxt, String description) {
        DialogUtils.showAlertDialog(this, titletext, posBtnTxt, negBtntxt, description, this);
    }

    @Override
    public void onPositiveButtonClick() {

    }

    @Override
    public void onNegativeButtonClick() {

    }


    @Override
    public void onSuccess(String responce, Object data, int urlId, int position) {

    }

    @Override
    public void onFailure(Exception e, int urlId) {

    }


}
