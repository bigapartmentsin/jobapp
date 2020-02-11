package com.abln.futur.module.job.activities;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.abln.chat.ui.Network.DataParser;
import com.abln.futur.R;
import com.abln.futur.activites.RenderPdfView;
import com.abln.futur.activites.StoriesActivity;
import com.abln.futur.common.Address;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.Minfo;
import com.abln.futur.common.models.Mkey;
import com.abln.futur.common.models.Mstories;
import com.abln.futur.common.newview.DataView;
import com.abln.futur.common.postjobs.BaseNewFragment;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.common.savedlist.PostedJobsmodel;
import com.abln.futur.common.savedlist.Savedlist;
import com.abln.futur.common.savedlist.UserList;
import com.abln.futur.common.savedlist.newUserListAdapter;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.services.NetworkOperationService;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyPostedJobsFragment extends BaseNewFragment implements TaskCompleteListener, newUserListAdapter.clickHandler {

    private static final String TAG = "MyPostedJobsFragment";
    @BindView(R.id.recycler_myPostedJobs)
    RecyclerView recyclerViewMyPostedJobs;
    @BindView(R.id.pullToRefresh)
    SwipeRefreshLayout pulltorfresh;
    private PrefManager prefManager = new PrefManager();
    private NetworkOperationService mService;
    private Context mContext;
    private MyPostedJobAdapter mAdapter;
   // private List<UserList> ls = new ArrayList<>();
   private UserList saveditems;
   private newUserListAdapter adapter;

    public MyPostedJobsFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_my_posted_jobs;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_posted_jobs, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mContext = getActivity();
        mService = new NetworkOperationService();

        prefManager.setTesturl("v1/getuserpostt");
        getsaveddata();


        recyclerViewMyPostedJobs.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));



        pulltorfresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getsaveddata();

                pulltorfresh.setRefreshing(false);
            }
        });


    }

    private void docallpost() {
        FuturProgressDialog.show(mContext, false);
        MyPostedJobListRequest mFavoriteRquestBody = new MyPostedJobListRequest();
        mFavoriteRquestBody.setApikey(prefManager.getApikey());

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getUserJobPost);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mContext.startService(intent);
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.getUserJobPost)) {
            FuturProgressDialog.dismissDialog();
            MyjobPost parserresp = DataParser.parseJson(responseString, MyjobPost.class);
            if (parserresp.getStatuscode() == 0) {
                // UIUtility.showToastMsg_short(mContext, parserresp.getStatusMessage());
                return;
            } else if (parserresp.getStatuscode() == 1) {

                if (parserresp.getData().getUserList().size() == 0) {
                    Log.d("TAG", "Unknown Error");
                    return;
                }

                for (int i = 0; i < parserresp.getData().getUserList().size(); i++) {


                  //  ls.add(parserresp.getData().getUserList().get(i));



                }

            }

        }


        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.dp)) {

            MyjobPost pr = DataParser.parseJson(responseString, MyjobPost.class);
            if (pr.getStatuscode() == 0) {

                UIUtility.showToastMsg_withErrorShort(mContext, "Opp's Network Error ");


            } else if (pr.getStatuscode() == 1) {

                UIUtility.showToastMsg_withSuccessShort(mContext, "Successfully Deleted the post");
                docallpost();

            } else if (pr.getStatuscode() == 2) {

                UIUtility.showToastMsg_withErrorShort(mContext, "Opp's Error ");

            }

        }


        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.time)) {

            MyjobPost pr = DataParser.parseJson(responseString, MyjobPost.class);

            if (pr.getStatuscode() == 1) {


                UIUtility.showToastMsg_withSuccessShort(mContext, "Updates ");


            } else {


                UIUtility.showToastMsg_withErrorShort(mContext, "Opp's Error ");


            }


        }


        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.date)) {
            MyjobPost pr = DataParser.parseJson(responseString, MyjobPost.class);


            if (pr.getStatuscode() == 1) {
                UIUtility.showToastMsg_withSuccessShort(mContext, "Expire date extended ");

            } else {
                UIUtility.showToastMsg_withErrorShort(mContext, "Opp's Error ");
            }
        }


    }


    public void onScrollEnd(int lastPosition) {

    }


    public void oPc(int position) {

    }


    @Override
    public void onapplyClick(Savedlist savedlist) {

    }

    @Override
    public void onRemoveSaved(Savedlist savedlist) {

    }

    @Override
    public void updatetime(String mjob, String date) {
        String dateupdate = date;

        MyPostedJobListRequest mFav = new MyPostedJobListRequest();
        mFav.date = dateupdate;
        mFav.postid = mjob;
        mFav.akey = prefManager.getApikey();

    }

    @Override
    public void updatedelete(UserList myjobPost) {
        remove(myjobPost);

    }


    public void updatedelete(MyjobPost.UserList mjp) {


        MyPostedJobListRequest mFavoriteRquestBody = new MyPostedJobListRequest();
        mFavoriteRquestBody.postid = mjp.pid;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.dp);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mContext.startService(intent);

    }


    public void updateshare(MyjobPost.UserList myjobpost) {

    }


    public void getdatetime(MyjobPost.UserList mjp) {
        MyPostedJobListRequest mFavoriteRquestBody = new MyPostedJobListRequest();
        mFavoriteRquestBody.postid = mjp.pid;
        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");
        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.diffdate);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mContext.startService(intent);

    }

    public String setinfo(String v) {


        return v;
    }



    /// all new function is adding


    private void getsaveddata() {
        post datahandel = new post();
        datahandel.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getuserpost("v1/getuserpost",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<PostedJobsmodel>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        pulltorfresh.setRefreshing(false);

                        PostedJobsmodel data_list = (PostedJobsmodel) baseResponse.data;
                        ArrayList<UserList> final_list = data_list.user_list;


                        adapter = new newUserListAdapter(getActivity(), final_list, MyPostedJobsFragment.this);
                        recyclerViewMyPostedJobs.setAdapter(adapter);

                    }

                    @Override
                    public void onFailure(Throwable e) {
                        pulltorfresh.setRefreshing(false);
                    }
                }));

    }


    private void remove(UserList ls){




            MyPostedJobListRequest mjr = new MyPostedJobListRequest();
            mjr.postid = ls.pid;

            compositeDisposable.add(apiService.remove("v1/delete-post",mjr)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(false, this) {
                        @Override
                        public void onApiSuccess(BaseResponse baseResponse) {


                            if (baseResponse.statuscode == 1) {

                                UIUtility.showToastMsg_withSuccessShort(getActivity(), "Successfully Deleted");
                                getsaveddata();

                            } else {
                                UIUtility.showToastMsg_withErrorShort(getActivity(), "Error in Deleting");

                            }

                        }

                        @Override
                        public void onFailure(Throwable e) {

                        }
                    }));




    }


    @Override
    public void share() {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.abln.futur")));
    }

    @Override
    public void imageClick(UserList myjobPost) {

        // play stories data //

        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"Redirecting");


        String key = myjobPost.pid ;
        String uid = myjobPost.user_api_key;

        getdata(key,"","",uid);



    }



    //  data to get the information  //




    private void getdata(String key, String apply, String star,String uid) {
        Mkey mkey = new Mkey();
        mkey.id = key;
        mkey.cid = prefManager.getApikey();
        compositeDisposable.add(apiService.getstories("v1/post-images",mkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Mstories>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        String url ="https://res.cloudinary.com/medinin/image/upload/v1562649354/futur";

                        if (baseResponse.statuscode == 1) {
                            //getting pdf stories;

                            Mstories soo = (Mstories) baseResponse.data;
                            Minfo da =  soo.stories;



                            /*
                            *
                            *   String key = finalDataSets.pid ;
        String uid = finalDataSets.uid;
        String apply = finalDataSets.jexpstatus;
        String star = finalDataSets.isstarred;
                            *
                            * */

                            Intent i = new Intent(getActivity(), RenderPdfView.class);
                            i.putExtra("pdf",url+da.pdf);
                            i.putExtra("name",da.name);
                            i.putExtra("apply",da.jexpstatus);
                            i.putExtra("star",da.isstarred);
                            i.putExtra("uid",da.rid);
                            i.putExtra("key",da.pid);
                            startActivity(i);




                        }

                        if (baseResponse.statuscode ==2 ){


                            //getting the stories ;




                            Mstories mstories1 = (Mstories) baseResponse.data;
                            Minfo info =  mstories1.stories;

                            ArrayList<String> keyvalue = new ArrayList<String>();

                            if (!info.jpost_one.equalsIgnoreCase("0")){

                                keyvalue.add(url+info.jpost_one);

                            }

                            if(!info.jpost_two.equalsIgnoreCase("0")){

                                keyvalue.add(url+info.jpost_two);

                            }

                            if (!info.jpost_three.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_three);
                            }

                            if (!info.jpost_four.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_four);
                            }

                            if (!info.jpost_five.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_five);
                            }

                            if (!info.jpost_six.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_six);
                            }

                            if (!info.jpost_seven.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_seven);
                            }

                            if (!info.jpost_eight.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_eight);
                            }

                            if (!info.jpost_nine.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_nine);
                            }

                            if (!info.jpost_ten.equalsIgnoreCase("0")){
                                keyvalue.add(url+info.jpost_ten);
                            }




                            Intent intent = new Intent(getActivity(), StoriesActivity.class);
                            intent.putStringArrayListExtra("DATA", keyvalue);

                            intent.putExtra("apply",info.jexpstatus);
                            intent.putExtra("star",info.isstarred);
                            intent.putExtra("uid",info.rid);
                            intent.putExtra("key",info.pid);
                            startActivity(intent);






                        }



                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Network Error ");

                    }
                }));
    }




    //

}
