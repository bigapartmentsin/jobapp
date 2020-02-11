package com.abln.futur.module.job.activities;


import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.postjobs.BaseNewFragment;
import com.abln.futur.services.NetworkOperationService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MySavedJobPostsFragment extends BaseNewFragment {


    private static final String TAG = "MySavedJobPostsFragment";
    @BindView(R.id.recycler_mySavedJobs)
    RecyclerView recyclerView_mySavedJobs;
    private MySavedJobPostsAdapter mAdapter;
    private List<MyjobPost.UserList> mySGList = new ArrayList<>();
    private Context mContext;
    private NetworkOperationService mService;


    public MySavedJobPostsFragment() {
        // Required empty public constructor
    }


    public static MySavedJobPostsFragment newInstance() {
        MySavedJobPostsFragment fragment = new MySavedJobPostsFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_saved_job_posts;
    }


}
