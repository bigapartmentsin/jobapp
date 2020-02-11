package com.abln.futur.module.global.activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.account.activities.CreateJobPostActivity;
import com.abln.futur.module.chats.adapter.GetAll_UserResponse;
import com.abln.futur.services.NetworkOperationService;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NonJobSeekerSearchResultActivity extends BaseActivity implements TaskCompleteListener, NonJobSeekerSearchAdapter.FolderClickListener, NonJobSeekerJobsPostAdapter.FolderClickListener {


    private static final String TAG = "NonJobSeekerActv";
    @BindView(R.id.jobResult_recyclerviewNonJobSeeker)
    RecyclerView jobResult_recyclerview;
    @BindView(R.id.searchJobsRole)
    SearchView searchJobRole;
    @BindView(R.id.sendJobInvite)
    TextView tvSendJobInvite;
    private Context mContext;
    private NetworkOperationService mService;
    private NonJobSeekerSearchAdapter mAdapter;
    private List<NonJobSeekerSearchResult.PatientList> mySGList = new ArrayList<>();
    private List<NonJobSeekerSearchResult.PatientList> userSelectedList = new ArrayList<>();
    private String[] resources = new String[]{
            "iconfinder_1",
            "iconfinder_2",
            "iconfinder_3",
            "iconfinder_4",
            "iconfinder_5",
            "iconfinder_6",
            "iconfinder_7",
            "iconfinder_8",
            "iconfinder_9",
            "iconfinder_10",
            "iconfinder_11",
            "iconfinder_12",
            "iconfinder_13",
            "iconfinder_14",
            "iconfinder_15",
            "iconfinder_1",
            "iconfinder_2",
            "iconfinder_3",
            "iconfinder_4",
            "iconfinder_5",
            "iconfinder_6",
            "iconfinder_7",
            "iconfinder_8",
            "iconfinder_9",
            "iconfinder_10",
            "iconfinder_11",
            "iconfinder_12",
            "iconfinder_13",
            "iconfinder_14",
            "iconfinder_15"
    };

    private String[] industry = new String[]{"1 km", "5 km",
            "4 km", "3 km", "20 km", "90 km", "2 km", "7 km",
            "9 km", "15 km", "8 km", "6 km", " 7 km", "2 km", "2 km",
            "1 km", "5 km",
            "4 km", "3 km", "20 km", "90 km", "2 km", "7 km",
            "9 km", "15 km", "8 km", "6 km", " 7 km", "2 km", "2 km"};

    private String[] name = new String[]{"Bharath", "Sharath", "Kuamr", "Hello", "World"};

    private String[] designation = new String[]{"Android Dv", "DevOps", "Tester", "CEO", "Senior software engineer"};

    private String[] resources_jobsPosts = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00001.jpg?alt=media&token=460667e4-e084-4dc5-b873-eefa028cec32",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00002.jpg?alt=media&token=e8e86192-eb5d-4e99-b1a8-f00debcdc016",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00004.jpg?alt=media&token=af71cbf5-4be3-4f8a-8a2b-2994bce38377",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00005.jpg?alt=media&token=7d179938-c419-44f4-b965-1993858d6e71",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00006.jpg?alt=media&token=cdd14cf5-6ed0-4fb7-95f5-74618528a48b",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00007.jpg?alt=media&token=98524820-6d7c-4fb4-89b1-65301e1d6053",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00008.jpg?alt=media&token=7ef9ed49-3221-4d49-8fb4-2c79e5dab333",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00009.jpg?alt=media&token=00d56a11-7a92-4998-a05a-e1dd77b02fe4",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00010.jpg?alt=media&token=24f8f091-acb9-432a-ae0f-7e6227d18803",
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_job_seeker_search_result);

        ButterKnife.bind(this);
        mContext = this;
        mService = new NetworkOperationService();
        setTaskCompleteListener(this);

        searchJobRole.onActionViewExpanded();

        if (!searchJobRole.isFocused()) {
            searchJobRole.clearFocus();
        }

        mAdapter = new NonJobSeekerSearchAdapter(getApplicationContext(), mySGList, this, userSelectedList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        jobResult_recyclerview.setLayoutManager(mLayoutManager);
        jobResult_recyclerview.setItemAnimator(new DefaultItemAnimator());
        jobResult_recyclerview.setAdapter(mAdapter);

        addRecyclerVIew();


        SearchManager searchManager = (SearchManager) mContext.getSystemService(Context.SEARCH_SERVICE);
        searchJobRole.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchJobRole.setMaxWidth(Integer.MAX_VALUE);

        searchJobRole.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    @OnClick(R.id.sendJobInvite)
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.sendJobInvite:
                openJobPostsPopUpMenu();
        }
    }

    private void openJobPostsPopUpMenu() {
        View view = getLayoutInflater().inflate(R.layout.popup_job_posts, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);

        NonJobSeekerJobsPostAdapter mAdapter;
        List<GetAll_UserResponse.PatientList> mySGList = new ArrayList<>();

        RecyclerView recyclerViewStories = view.findViewById(R.id.jobsPostRecyclewView);

        mAdapter = new NonJobSeekerJobsPostAdapter(mContext, mySGList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewStories.setLayoutManager(mLayoutManager);
        recyclerViewStories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStories.setAdapter(mAdapter);

        for (int i = 0; i < resources_jobsPosts.length; i++) {
            GetAll_UserResponse.PatientList response = new GetAll_UserResponse.PatientList();
            response.setPatientId("1");
            response.setPhoto(resources_jobsPosts[i]);
            mySGList.add(i, response);
        }
        mAdapter.notifyDataSetChanged();


        view.findViewById(R.id.createJobPost).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, CreateJobPostActivity.class);
                startActivity(intent);
            }
        });


//        view.findViewById(R.id.cancle_txt).setOnClickListener(new View.OnClickListener() {
//            public void onClick(View arg0) {
//                dialog.dismiss();
//            }
//        });
        dialog.show();
    }

    private void filter(String text) {
        ArrayList<NonJobSeekerSearchResult.PatientList> filterdNames = new ArrayList<>();


        for (NonJobSeekerSearchResult.PatientList s : mySGList) {
            if (s.getName().toLowerCase().contains(text.toLowerCase()) || s.getDesignation().toLowerCase().contains(text.toLowerCase())) {
                filterdNames.add(s);
            } else {
                // Toast.makeText(getApplicationContext(), "No results found", Toast.LENGTH_SHORT).show();
            }
        }
        mAdapter.filterList(filterdNames);
    }

    private void addRecyclerVIew() {
        for (int i = 0; i < 5; i++) {
            NonJobSeekerSearchResult.PatientList response = new NonJobSeekerSearchResult.PatientList();
            response.setPatientId(industry[i]);
            response.setPhoto(resources[i]);
            response.setName(name[i]);
            response.setDesignation(designation[i]);
            response.setExperience(industry[i]);
            response.setDistance(industry[i]);
            mySGList.add(i, response);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScrollEnd(int lastPosition) {

    }

    @Override
    public void onPatientClicked(int position) {

    }

    @Override
    public void radioButtonClicked(int position, NonJobSeekerSearchResult.PatientList userList) {
        if (!userSelectedList.contains(userList)) {
            userSelectedList.add(userList);
        }
        tvSendJobInvite.setEnabled(true);
        tvSendJobInvite.setTextColor(mContext.getResources().getColor(R.color.color_white));
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


    @Override
    public void onScrollJobPostsEnd(int lastPosition) {

    }

    @Override
    public void onJobPostSelected(GetAll_UserResponse.PatientList position) {
        Log.d("Hello", "Pos" + position);
//        openSendBottomSheetDialog(position);
        Intent ii = new Intent(mContext, SendJobInvite.class);
        startActivity(ii);
    }

    private void openSendBottomSheetDialog(GetAll_UserResponse.PatientList position) {

        View view = getLayoutInflater().inflate(R.layout.popup_sendjob_alert, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);


        view.findViewById(R.id.sendInvite).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {

                Intent intent = new Intent(mContext, CreateJobPostActivity.class);
                startActivity(intent);
            }
        });

        dialog.show();
    }
}
