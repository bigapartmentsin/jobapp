package com.abln.futur.module.base.adapter;

import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class SwipeAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private ArrayList<String> mTabtitles = new ArrayList<>();

    public SwipeAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragments, String titles) {
        this.mFragments.add(fragments);
        this.mTabtitles.add(titles);
    }

    @Override
    public Fragment getItem(int i) {
        return mFragments.get(i);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabtitles.get(position);
    }

    @Override
    public Parcelable saveState() {
        // Do Nothing
        return null;
    }

}
