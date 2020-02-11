package com.abln.futur.customViews;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class FragmentAdapter extends FragmentPagerAdapter {


    private Activity activity;
    private ArrayList<Fragment> fragments;
    private ArrayList<String> titles;

    public FragmentAdapter(FragmentActivity fragmentActivity, ArrayList<Fragment> fragments) {
        super(fragmentActivity.getSupportFragmentManager());
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}


