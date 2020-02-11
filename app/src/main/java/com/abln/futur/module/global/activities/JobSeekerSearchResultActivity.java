package com.abln.futur.module.global.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.common.Addinfo;
import com.abln.futur.common.Address;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.common.searchjob.JobSearcData;
import com.abln.futur.common.searchjob.Search;
import com.abln.futur.common.searchjob.SearchJob;
import com.abln.futur.common.searchjob.SearchResult;
import com.abln.futur.services.NetworkOperationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class JobSeekerSearchResultActivity extends NewBaseActivity implements JobSearcData.clickHandler {


//    private final String mDisplayDatePattern = "MMM dd, yyyy h:mm a";
//    private final String mAPIDatePattern = "MMM dd, yyyy HH:mm";


    private static final String TAG = "ChatsFragment";
    @BindView(R.id.jobResult_recyclerview)
    RecyclerView jobResult_recyclerview;
    @BindView(R.id.grid_layout)
    ImageView imageView;
    @BindView(R.id.list_layout)
    ImageView listimage;
    //sort_filter
    RecyclerView filterListView = null;
    private JobSearcData jobSearcData;
    private Context mContext;
    private NetworkOperationService mService;
    private JobSeekerSearchAdapter mAdapter;
    private ArrayList<SearchResult> mySGList = new ArrayList<>();
    private RecyclerView filterValListView;
    private FilterRecyclerAdapter adapter;
    private FilterValRecyclerAdapter filterValAdapter;
    private ArrayList<String> sizes = new ArrayList<>();
    private ArrayList<String> styles = new ArrayList<>();
    private ArrayList<String> colors = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> sizeMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> styleMultipleListModels = new ArrayList<>();
    private ArrayList<FilterDefaultMultipleListModel> colorMultipleListModels = new ArrayList<>();

    private List<String> rootFilters;
    private Button btnClear;
    private Button btnFilter;
    private ArrayList<String> sizeSelected = new ArrayList<String>();
    private ArrayList<String> colorSelected = new ArrayList<String>();
    private ArrayList<String> styleSelected = new ArrayList<String>();


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


    @Override
    public int getLayoutId() {
        return R.layout.activity_job_seeker_search_result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mService = new NetworkOperationService();


        searchjobs("developer", "2", "12.91582330", "77.64532000", "1");
        jobResult_recyclerview.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));


//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        jobResult_recyclerview.setLayoutManager(mLayoutManager);
//        GridLayoutManager mGridManager = new GridLayoutManager(mContext, 3);
//        jobResult_recyclerview.setLayoutManager(mGridManager);
//        jobResult_recyclerview.setItemAnimator(new DefaultItemAnimator());


//grid view ;


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //listimage.setVisibility(View.VISIBLE);
                //imageView.setVisibility(View.GONE);
                // imageView.setImageDrawable(null);
                // UIUtility.showToastMsg_withSuccessShort(getApplication(),"GIRD VIEW ");
                //   listimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_view));
                // jobResult_recyclerview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

            }


        });


        listimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//
//
//                listimage.setVisibility(View.GONE);
//                imageView.setVisibility(View.VISIBLE);
//                imageView.setImageDrawable(null);
//                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid_view));
//
//                UIUtility.showToastMsg_withSuccessShort(getApplication(),"LIST VIEW ");
//
//
//
//                jobResult_recyclerview.setLayoutManager(new GridLayoutManager(getApplicationContext(), 3));


            }
        });

    }


    public void onTaskCompleted(Context context, Intent intent) {

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equals(NetworkConfig.searchresult)) {


            //SearchJob
            SearchJob data = DataParser.parseJson(responseString, SearchJob.class);
            Log.d("MYTAG", String.valueOf(data.getStatuscode()));

            if (data.getStatuscode() == 0) {


                UIUtility.showToastMsg_withErrorShort(mContext, "Error getting information");

            } else if (data.getStatuscode() == 1) {

                UIUtility.showToastMsg_withSuccessShort(mContext, "Successfully got the information ");

            }
        }


    }


    private void searchjobs(String val1, String val2, String val3, String val4, String val5) {
        post data = new post();


        Search result = new Search();
        result.industry = val1;
        result.radius = val2;
        result.lat = val3;
        result.lng = val4;
        result.pagecount = val5;

        data.apikey = "c7ca9a4d17330ae7b2f680d95b192dd4";
        compositeDisposable.add(apiService.searchjobs("v1/ssearch",result)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Address>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        Address address = (Address) baseResponse.data;
                        ArrayList<Addinfo> address_list = address.address;
                        if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully loaded ");


                            Address userData = (Address) baseResponse.data;
                            ArrayList<SearchResult> jc = userData.search_results;
                            jobSearcData = new JobSearcData(JobSeekerSearchResultActivity.this, jc, JobSeekerSearchResultActivity.this);
                            jobResult_recyclerview.setAdapter(jobSearcData);


                        } else {


                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Something Happend");
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));
    }


    private void makecall(String val1, String val2, String val3, String val4, String val5) {


        Search result = new Search();
        result.industry = val1;
        result.radius = val2;
        result.lat = val3;
        result.lng = val4;
        result.pagecount = val5;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.searchresult);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, result);
        mContext.startService(intent);


    }


    @Override
    public void onApplyClick(SearchResult searchResult) {

        searchResult.getid();
        //  searchResult.




/*       // Search result = new Search();
        int statuscode = 6 ;

        if(statuscode == 0){

            UIUtility.showToastMsg_withSuccessShort(mContext ,"Not exists !");

        }else if(statuscode == 1){

            UIUtility.showToastMsg_withSuccessShort(mContext ,"successfully Applied");

        }else  if(statuscode == 2){

            UIUtility.showToastMsg_withSuccessShort(mContext ,"Error in pdf problem");


        }else if (statuscode == 3){

            UIUtility.showToastMsg_withSuccessShort(mContext ,"Error in ");

        }else if(statuscode == 4){

            UIUtility.showToastMsg_withSuccessShort(mContext ,"Make happen !");

        }*/

    }


    @Override
    public void onapplySave(SearchResult searchResult) {

        //    :postid    :recruiterid   :canid    :starred_status  (0/1)

        Toast.makeText(mContext, "Saved Successfully ", Toast.LENGTH_LONG).show();


    }


}
