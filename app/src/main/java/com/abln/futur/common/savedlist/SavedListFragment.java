package com.abln.futur.common.savedlist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.activites.RenderPdfView;
import com.abln.futur.activites.StoriesActivity;
import com.abln.futur.common.Address;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.Minfo;
import com.abln.futur.common.models.Mkey;
import com.abln.futur.common.models.Mstories;
import com.abln.futur.common.newview.DataView;
import com.abln.futur.common.postjobs.BaseNewFragment;
import com.abln.futur.common.postjobs.post;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SavedListFragment extends BaseNewFragment implements SavedItem.clickHandler {


    PrefManager prefManager = new PrefManager();
    @BindView(R.id.recycler_mySavedJobs)
    RecyclerView recyclerView_mySavedJobs;
    private SavedItem savedItem;
    private Class<SavedListFragment> ctx = SavedListFragment.class;
    private Context mContext;
    private SavedItem saveditems;

    public SavedListFragment() {


    }

    public static SavedListFragment newInstance() {
        SavedListFragment fragment = new SavedListFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {

        return R.layout.fragment_my_saved_job_posts;

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        getsaveddata();

        recyclerView_mySavedJobs.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));


    }


    private void getsaveddata() {
        post datahandel = new post();
        datahandel.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getSavedData("v1/getsaved",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Address>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        Address data_list = (Address) baseResponse.data;
                        ArrayList<Savedlist> final_list = data_list.result;


                        saveditems = new SavedItem(getActivity(), final_list, SavedListFragment.this);
                        recyclerView_mySavedJobs.setAdapter(saveditems);

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

    private void savedPostData(Savedlist finalDataSets, String val) {
        post fulldatavalues = new post();


        fulldatavalues.postid = finalDataSets.post_id;
        fulldatavalues.recruiterid = finalDataSets.rec_id;
        fulldatavalues.canid = prefManager.getApikey();
        fulldatavalues.starred_status = val;

        compositeDisposable.add(apiService.onSaved("v1/saved",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        UIUtility.showToastMsg_withSuccessShort(getActivity(), " Unsaved Done ");
                        getsaveddata();

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Network Error ");

                    }
                }));


    }


    @Override
    public void onapplyClick(Savedlist list) {


        //onapplyClick

        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.yes_or_no_dilog_apply_post, viewGroup, false);

        TextView no = dialogView.findViewById(R.id.cancle_txt_apply);
        TextView yes = dialogView.findViewById(R.id.yest_text_apply);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkResumeAvaliablity(list);

                alertDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }


    private void clickappy(Savedlist finalDataSets) {

        //onApply

        post fulldatavalues = new post();


        fulldatavalues.postid = finalDataSets.post_id;
        fulldatavalues.recruiterid = finalDataSets.rec_id;
        fulldatavalues.canid = prefManager.getApikey();

        compositeDisposable.add(apiService.onApply("v1/can-rec-apply",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        //   itemAdapter.notifyDataSetChanged();

                        if (baseResponse.statuscode == 0) {

                            UIUtility.showToastMsg_withErrorShort(getActivity(), "Something went wrong !");
                        }
                        if (baseResponse.statuscode == 1) {

                            getsaveddata();
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getActivity(), "Network Error ");

                    }
                }));


    }



    /*
    *
    *        post data = new post();
        data.apikey = prefManager.getApikey();


        compositeDisposable.add(apiService.checkresume(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        if( baseResponse.statuscode==0){

                            showCustomDialog();

                        }else{
                            applyjobpost(finalDataSets);
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(),"Network Error ");

                    }
                }));
                *
                *  ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.test_two_data, viewGroup, false);

      ImageView upload =   dialogView.findViewById(R.id.upload_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtility.showToastMsg_withAlertInfoShort(mContext, "Upload pfd");
                openFilePickerToUploadJobPost_Pdf();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    *
    * */

    @Override
    public void onRemoveSaved(Savedlist savedlist) {


        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.yes_or_no_dilog_unsave_post, viewGroup, false);

        TextView no = dialogView.findViewById(R.id.cancle_txt_apply);
        TextView yes = dialogView.findViewById(R.id.yest_text_apply);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                savedPostData(savedlist, "0");

                alertDialog.dismiss();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();


    }


    private void checkResumeAvaliablity(Savedlist savedlist) {


        post checkresume = new post();

        checkresume.apikey = prefManager.getApikey();

        compositeDisposable.add(apiService.checkresume("v1/get-resume",checkresume)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {


                                   @Override
                                   public void onApiSuccess(BaseResponse baseResponse) {


                                       if (baseResponse.statuscode == 0) {

                                           invokePopUp();

                                       } else {

                                           //make a job apply post  .
                                           clickappy(savedlist);
                                       }


                                   }


                                   @Override
                                   public void onFailure(Throwable e) {

                                       UIUtility.showToastMsg_withErrorShort(getActivity(), "Network Error");
                                   }
                               }


                )


        );

    }


    private void invokePopUp() {

        ViewGroup viewGroup = getActivity().findViewById(android.R.id.content);
        View popupview = LayoutInflater.from(getActivity()).inflate(R.layout.test_two_data, viewGroup, false);

        ImageView upload = popupview.findViewById(R.id.upload_button);
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setView(popupview);
        AlertDialog ad = bld.create();


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ad.dismiss();

            }
        });

        ad.show();
    }


    @Override
    public void imageClick(Savedlist savedlist) {

        UIUtility.showToastMsg_withAlertInfoShort(getContext(),"Redirecting");

        String key = savedlist.post_id ;


        getdata(key,"","","");


    }










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



}
