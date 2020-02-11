package com.abln.futur.module.login;

import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.FLog;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignUpActivity extends BaseActivity {

    private static final String TAG = "PreloginActivity";
    @BindView(R.id.container)
    FrameLayout flContainer;

    private Fragment[] mfragments;

    private SignUpFragment signUpFragment;
    private BasicDetailsFragment basicDetailsFragment;
    private ProfessionDetailsFragment professionDetailsFragment;


    private int index;
    private int currentTabIndex;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FLog.d("TimeTaken", "Splash" + SystemClock.currentThreadTimeMillis());

        ButterKnife.bind(this);
        mContext = this;

        signUpFragment = new SignUpFragment();
        basicDetailsFragment = new BasicDetailsFragment();
        professionDetailsFragment = new ProfessionDetailsFragment();

        mfragments = new Fragment[]{signUpFragment, basicDetailsFragment, professionDetailsFragment};

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, signUpFragment)
                .add(R.id.container, basicDetailsFragment)
                .add(R.id.container, professionDetailsFragment)

                .hide(basicDetailsFragment)
                .hide(professionDetailsFragment)
                .show(signUpFragment).commit();
    }
}
