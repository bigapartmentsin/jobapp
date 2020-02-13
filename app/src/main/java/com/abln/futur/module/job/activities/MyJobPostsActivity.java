package com.abln.futur.module.job.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.savedlist.SavedListFragment;
import com.abln.futur.customViews.FragmentAdapter;

import java.util.ArrayList;

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


    @BindView(R.id.pager)
    ViewPager viewPager;


    private Fragment[] mfragments;

    private SavedListFragment mySavedJobPostsFragment;
    private MyPostedJobsFragment myPostedJobsFragment;

    private int index;
    private int currentTabIndex;


    private ArrayList<Fragment> fragmentArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_job_posts);

        ButterKnife.bind(this);

        mySavedJobPostsFragment = new SavedListFragment();
        myPostedJobsFragment = new MyPostedJobsFragment();





        fragmentArrayList = new ArrayList<>();
        fragmentArrayList.add(mySavedJobPostsFragment);
        fragmentArrayList.add(myPostedJobsFragment);




        FragmentAdapter fragmentAdapter = new FragmentAdapter(this, fragmentArrayList);

        viewPager.setAdapter(fragmentAdapter);
        viewPager.setOffscreenPageLimit(2);

        selectTab(0);



        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                selectTab(position);
            }
        });



    }


    private void selectTab(int position) {
        index = position;
        switch (position) {
            case 0:



                viewPosted.setVisibility(View.VISIBLE);
                viewSaved.setVisibility(View.INVISIBLE);
                tvSaved.setTypeface(Typeface.DEFAULT);
                tvPosted.setTypeface(Typeface.DEFAULT_BOLD);

                viewSaved.setVisibility(View.VISIBLE);
                viewPosted.setVisibility(View.INVISIBLE);
                tvSaved.setTypeface(Typeface.DEFAULT_BOLD);
                tvPosted.setTypeface(Typeface.DEFAULT);

                break;

            case 1:

                viewSaved.setVisibility(View.VISIBLE);
                viewPosted.setVisibility(View.INVISIBLE);
                tvSaved.setTypeface(Typeface.DEFAULT_BOLD);
                tvPosted.setTypeface(Typeface.DEFAULT);


                viewPosted.setVisibility(View.VISIBLE);
                viewSaved.setVisibility(View.INVISIBLE);
                tvSaved.setTypeface(Typeface.DEFAULT);
                tvPosted.setTypeface(Typeface.DEFAULT_BOLD);

                break;


        }

        viewPager.setCurrentItem(position);

    }


    @OnClick({R.id.tvSaved, R.id.tvPosted, R.id.back_arrow})
    void onClickListener(View v) {
        switch (v.getId()) {
            case R.id.tvSaved:

              selectTab(0);


                break;
            case R.id.tvPosted:

                selectTab(1);




                break;

            case R.id.back_arrow:
                finish();
                break;


            default:
                break;


        }
        currentTabIndex = index;


    }


}
