package com.abln.futur.activites;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.abln.futur.R;
import com.abln.futur.common.Address;
import com.abln.futur.common.AnalyticsJobs;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.ModChat;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.ModelAnaly;
import com.abln.futur.common.models.RequestGlobal;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.module.job.activities.MyPostedJobListRequest;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Analytics extends BaseActivity  implements View.OnClickListener , AnalyticsJobs.Handler, InboxHolder.Handler {


PrefManager prefManager = new PrefManager();
    BottomSheetDialog bts;
    View sheetView;
    TabLayout tabLayout;
    ViewPager viewPager;
    ModelAnaly modelAnaly;
    TextView textView,job_popup_text,totalapplication,webapplication;
    LinearLayout mobileapplicatent;

    ImageView image_menu,imdot ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_model);
        Bundle bundle = getIntent().getExtras();
        String apikey =    bundle.getString("data");
        String name = bundle.getString("name");
        tabLayout = findViewById(R.id.tabLyt);
        viewPager = findViewById(R.id.view_pager);
        job_popup_text = findViewById(R.id.job_popup_text);
        image_menu =findViewById(R.id.image_menu);
        totalapplication = findViewById(R.id.totalapplication);
        mobileapplicatent = findViewById(R.id.mobileapplicatent);
        webapplication = findViewById(R.id.webapplication);
        imdot = findViewById(R.id.imdot);
        webapplication.setText("WEB APPLICANTS (Coming Soon)");
        webapplication.setPaintFlags(webapplication.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        job_popup_text.setOnClickListener(this);
        image_menu.setOnClickListener(this);
        imdot.setOnClickListener(this);
        mobileapplicatent.setOnClickListener(this);
        job_popup_text.setText(name);
        System.out.println("Skey"+apikey);
        getdata(apikey);
        key = apikey;
        getMobile(apikey);

    }


    RecyclerView recyclerView;



    private void getDialog() {
        bts = new BottomSheetDialog(this);
        sheetView = getLayoutInflater().inflate(R.layout.popup_chat__datahandler, null);
        TextView close = sheetView.findViewById(R.id.tvExpYrs);
        recyclerView = sheetView.findViewById(R.id.itemview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
        getsaveddata();
        bts.setContentView(sheetView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bts.dismiss();
            }
        });
    }




    private AnalyticsJobs inboxitem;

    private void getsaveddata() {
        post datahandel = new post();

        datahandel.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getPosted("v1/short-postinfo",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Address>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        com.abln.futur.common.Address data_list = (com.abln.futur.common.Address) baseResponse.data;
                        ArrayList<ModChat> final_list = data_list.post_list;
                        inboxitem = new AnalyticsJobs(Analytics.this, final_list,Analytics.this);
                        recyclerView.setAdapter(inboxitem);
                        bts.show();

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }




    private  void getdata(String key){

        RequestGlobal global = new RequestGlobal();
        global.apikey = key;
        compositeDisposable.add(apiService.getdatainfo("v1/get-info2",global)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<ModelAnaly>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {



                        ModelAnaly data_list = (ModelAnaly) baseResponse.data;
                        InboxHolder adapter = new InboxHolder(Analytics.this,data_list,Analytics.this);
                        viewPager.setAdapter(adapter);
                        tabLayout.setupWithViewPager(viewPager, true);





                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }


    private void getMobile(String key){
        RequestGlobal global = new RequestGlobal();
        global.pid = key;
        compositeDisposable.add(apiService.mobileapplicaion("v1/mobile-applications",global)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<MobileApplicants>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        MobileApplicants data_list = (MobileApplicants) baseResponse.data;


                        if(data_list.mobile_applications.equals("0")){

                            totalapplication.setText("");

                        }else{
                            totalapplication.setText(data_list.mobile_applications);

                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.job_popup_text:
                getDialog();
                break;
            case R.id.image_menu:
                getDialog();

                break;
            case R.id.imdot:
                break;

            case R.id.mobileapplicatent:
                Intent listapp = new Intent(Analytics.this, ListApplicataints.class);
                listapp.putExtra("data",key);
                startActivity(listapp);
                // todo
                break;

        }
    }

    @Override
    public void onTapclicked(ModChat savedlist) {
        job_popup_text.setText(savedlist.name.replace("Post",""));
        UIUtility.showToastMsg_withAlertInfoShort(this,"Change the status  ");
        getdata(savedlist.pid);
        key = savedlist.pid;
        getMobile(savedlist.pid);
        bts.dismiss();

    }

    String key ;
    @Override
    public void onFullList(ModChat savedlist) {
        key  = savedlist.pid;
    }

    @Override
    public void onDelete(ModelAnaly modelAnaly) {




remove(modelAnaly);

    }

    @Override
    public void onShare(ModelAnaly d) {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.abln.futur")));


    }

    @Override
    public void onTimer(ModelAnaly a) {






    }




    private void remove(ModelAnaly ls){
        MyPostedJobListRequest mjr = new MyPostedJobListRequest();
        mjr.postid = ls.pid;

        compositeDisposable.add(apiService.remove("v1/delete-post",mjr)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Deleted");
                            finish();

                        } else {
                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Error in Deleting");

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));




    }
}
