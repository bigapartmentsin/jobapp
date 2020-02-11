package com.abln.futur.module.job.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.savedlist.SavedListFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyJobPostsActivity extends BaseActivity {

    @BindView(R.id.tvSaved)
    TextView tvSaved;

    @BindView(R.id.tvPosted)
    TextView tvPosted;

    @BindView(R.id.saved_view)
    View viewSaved;

    @BindView(R.id.posted_view)
    View viewPosted;


    private Fragment[] mfragments;

    private SavedListFragment mySavedJobPostsFragment;
    private MyPostedJobsFragment myPostedJobsFragment;

    private int index;
    private int currentTabIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job_posts);

        ButterKnife.bind(this);

        mySavedJobPostsFragment = new SavedListFragment();
        myPostedJobsFragment = new MyPostedJobsFragment();

        mfragments = new Fragment[]{mySavedJobPostsFragment, myPostedJobsFragment};


        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, mySavedJobPostsFragment)
                .add(R.id.container, myPostedJobsFragment)
                .hide(myPostedJobsFragment)
                .show(mySavedJobPostsFragment).commit();

    }


    @OnClick({R.id.tvSaved, R.id.tvPosted, R.id.back_arrow})
    void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.tvSaved:
                index = 0;
                viewSaved.setVisibility(View.VISIBLE);
                viewPosted.setVisibility(View.INVISIBLE);
                tvSaved.setTypeface(Typeface.DEFAULT_BOLD);
                tvPosted.setTypeface(Typeface.DEFAULT);
                break;
            case R.id.tvPosted:
                index = 1;
                viewPosted.setVisibility(View.VISIBLE);
                viewSaved.setVisibility(View.INVISIBLE);
                tvSaved.setTypeface(Typeface.DEFAULT);
                tvPosted.setTypeface(Typeface.DEFAULT_BOLD);

                break;

            case R.id.back_arrow:
                finish();
                break;


            default:
                break;


        }

        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(mfragments[currentTabIndex]);
            if (!mfragments[index].isAdded()) {
                trx.add(R.id.container, mfragments[index]);
            }
            trx.show(mfragments[index]).commit();
        }
        currentTabIndex = index;
    }


}
