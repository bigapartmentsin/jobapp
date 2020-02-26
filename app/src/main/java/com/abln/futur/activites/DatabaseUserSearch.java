package com.abln.futur.activites;

import android.accessibilityservice.AccessibilityService;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SymbolTable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.Address;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.ImageLoader;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.NewJobPost;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AppliedData;
import com.abln.futur.common.models.PostDataModel;
import com.abln.futur.common.models.PostDatabase;
import com.abln.futur.common.models.PostItems;
import com.abln.futur.common.models.RequestGlobal;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding3.widget.RxTextView;
import com.jaygoo.widget.OnRangeChangedListener;
import com.jaygoo.widget.RangeSeekBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindInt;
import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabaseUserSearch extends NewBaseActivity implements DatabaseHolder.EventHandler ,PostItems.clickHandler{


    @BindView(R.id.jobResult_recyclerviewNonJobSeeker)
    RecyclerView recyclerView_mySavedJobs;


    @BindView(R.id.sendJobInvite)
    TextView sendjobinvit;

    @BindView(R.id.searchJobsRole)
    EditText searchjob;


    @BindView(R.id.sort)
    ImageView sort;

    @BindView(R.id.back)
    ImageView back;

    @BindView(R.id.empty_states)
    ImageView empty_state;


  private   DatabaseHolder    itemAdapter;

  PrefManager prefManager = new PrefManager();



  String mlat, mlng, mjob, mraduis, mapikey;
    @Override
    public int getLayoutId() {


        return R.layout.activity_non_job_seeker_search_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = this.getIntent().getExtras();
        mlat = extras.getString("lat");
        mlng = extras.getString("lng");
        mjob = extras.getString("job");
        mraduis = extras.getString("radius");
        mapikey = extras.getString("apikey");
        recyclerView_mySavedJobs.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        getFullData(mlat,mlng,mapikey,mjob,mraduis,"");




        sendjobinvit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog();
            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });




        compositeDisposable.add(RxTextView.afterTextChangeEvents(searchjob).subscribe(textViewAfterTextChangeEvent -> {
            if (itemAdapter != null) {
                String searchString = textViewAfterTextChangeEvent.getEditable().toString();
                if (searchString.length() == 0) {

                } else {

                }
                itemAdapter.getFilter().filter(searchString);
            }
        }));



        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getDialogSort();


            }
        });


    }



    private void getFullData(String v1, String v2, String v3, String v4, String v5, String v6) {
        RequestGlobal fulldatavalues = new RequestGlobal();

        fulldatavalues.lat = v1;
        fulldatavalues.lng = v2;
        fulldatavalues.apikey = v3;
        fulldatavalues.industry = v4;
        fulldatavalues.radius = v5;


  compositeDisposable.add(apiService.getusersearch(fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AppliedData>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {
                        if (baseResponse.statuscode == 1) {

                            empty_state.setVisibility(View.GONE);

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully loaded ");
                            AppliedData userData = (AppliedData) baseResponse.data;
                            ArrayList<SearchDatamodel> jc = userData.results;
                                itemAdapter = new DatabaseHolder(DatabaseUserSearch.this, jc,DatabaseUserSearch.this);
                            recyclerView_mySavedJobs.setAdapter(itemAdapter);

                        } else if (baseResponse.statuscode == 0) {
                            AppliedData userData = (AppliedData) baseResponse.data;
                            ArrayList<SearchDatamodel> jc = userData.results;
                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Sorry no data found !");

                            empty_state.setVisibility(View.VISIBLE);
                            //candi_list_not_found.png



                        }
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        // UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");
                    }
                }));


    }







    //data handlinng in the informtion form the infor :

    // pop up card and moving to first heads

    // pop up in the card means the inforamtion need to and one


    //  making the inforamtion fesiable for handling the inromaton
    // 

    private void getfillDatafilter(String v1, String v2, String v3 ,String v4 ,String v5 ,String v6 ,String v7,String v8){
        RequestGlobal searchfilter = new RequestGlobal();
        searchfilter.lat = mlat;
        searchfilter.lng = mlng;
        searchfilter.apikey = prefManager.getApikey();
        searchfilter.industry = mjob;
        searchfilter.radius = "99";
        searchfilter.parameter="0";         // ZERO all // one Male // two Three;
        searchfilter.exp_from = v7;
        searchfilter.exp_to = v8;

        compositeDisposable.add(apiService.getUserfilterData(searchfilter)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AppliedData>>(true,this)
                               {


                                   @Override
                                   public void onApiSuccess(BaseResponse baseResponse) {

                                       if (baseResponse.statuscode == 1){

                                           recyclerView_mySavedJobs.setVisibility(View.VISIBLE);
                                           empty_state.setVisibility(View.GONE);

                                            AppliedData userData = (AppliedData) baseResponse.data;
                                             ArrayList<SearchDatamodel> jc = userData.results;
                                                  itemAdapter = new DatabaseHolder(DatabaseUserSearch.this, jc,DatabaseUserSearch.this);
                                                     recyclerView_mySavedJobs.setAdapter(itemAdapter);

                                           UIUtility.showToastMsg_withSuccessShort(getApplicationContext(),"Successfully loaded");


                                       }
                                       else if (baseResponse.statuscode == 0){

                                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(),"Sorry nothing found ");
                                            AppliedData userData = (AppliedData) baseResponse.data;
                                            ArrayList<SearchDatamodel> jc = userData.results;
                                           recyclerView_mySavedJobs.setVisibility(View.GONE);
                                             empty_state.setVisibility(View.VISIBLE);
                                       }

                                   }

                                   @Override
                                   public void onFailure(Throwable e) {
                                       UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error");

                                   }
                               }


                )





        ) ;


    }



    //TODO  downloadForm(file,fileName);





    private static final int MEGABYTE = 1024 * 1024;
    public static void downloadFile(String fileUrl, File directory) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(directory);
            int totalSize = urlConnection.getContentLength();
            byte[] buffer = new byte[MEGABYTE];
            int bufferLength = 0;
            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

String fileName;

    @Override
    public void showpdf(String data,String fileName) {

        downloadForm(data,fileName);

    }




    private class GetFiles extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FuturApp");
            folder.mkdirs();
            File pdfFile = new File(folder, fileName);
            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, pdfFile);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            //move the intent here .

            getUrl(fileName);


        }
    }



    public void getUrl(String url) {

        Intent i = new Intent(this,PdfViewer.class);
        i.putExtra("url",url);
        startActivity(i);

    }


    private void downloadForm(String Url_path, String file_name) {
        new GetFiles().execute(Url_path, file_name);
    }


private ArrayList<SearchDatamodel> selecteddata = new ArrayList<>();
    //check
    @Override
    public void onItemCheck(SearchDatamodel item) {



        selecteddata.add(item);


        if (selecteddata.size() > 0){

            sendjobinvit.setVisibility(View.VISIBLE);

        }else{

            sendjobinvit.setVisibility(View.INVISIBLE);
        }


    }


    //uncheck

    @Override
    public void onItemUncheck(SearchDatamodel item) {
        System.out.println(" Remove selected"+item.first_name);

        selecteddata.remove(item);
        if (selecteddata.size() > 0){
            sendjobinvit.setVisibility(View.VISIBLE);
        }else{
            sendjobinvit.setVisibility(View.INVISIBLE);
        }


    }




    BottomSheetDialog bts, sortbts;
    View sheetView,sortsheet;

    RecyclerView recyclerView;

    //TODO getdialog for the information handling ;




    private void getDialog() {
        bts = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        TextView close;
        LinearLayout createjobpost;

        sheetView = getLayoutInflater().inflate(R.layout.popup_send_user_, null);
        recyclerView = sheetView.findViewById(R.id.itemview);
        createjobpost = sheetView.findViewById(R.id.createjobpost);
        close = sheetView.findViewById(R.id.tvExpYrs);


        getsaveddata();

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, false));


        //  }
        bts.setContentView(sheetView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bts.dismiss();

            }
        });


        createjobpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NewJobPost bottomPostjob = NewJobPost.newInstance();
                bottomPostjob.show(getSupportFragmentManager(), "TAG");

            }
        });





    }



    private PostItems inboxitem;
    private void getsaveddata() {

        RequestGlobal datahandel = new RequestGlobal();
        datahandel.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getpostusers(datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<PostDatabase>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        PostDatabase data_list = (PostDatabase) baseResponse.data;
                        ArrayList<PostDataModel>  final_list = data_list.post_list;
                        inboxitem = new PostItems(DatabaseUserSearch.this, final_list,DatabaseUserSearch.this);
                        recyclerView.setAdapter(inboxitem);
                        bts.show();


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }


    @Override
    public void onapplyClick() {


        UIUtility.showToastMsg_withSuccessShort(this,"Click Handling ");

    }

@Override
public void onapplyClick(PostDataModel data) {
        final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.layout_final_invite);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    ImageView imageView =  dialog.findViewById(R.id.imageView);
  View yes = dialog.findViewById(R.id.yes_text);
  View no = dialog.findViewById(R.id.no_text);


  yes.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dialog.dismiss();
      }
  });


  no.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

          String postif = data.pid;
          String recid = prefManager.getApikey();

          for (SearchDatamodel key :selecteddata
          ) {
              System.out.println("user api key "+key.apikey+"User Name"+key.first_name+"");
              sendUser(postif,recid,key.apikey);
          }
          invitesuceess();

          dialog.dismiss();



      }
  });


   ImageLoader.loadImage(data.first_pic,imageView);
   dialog.show();






}



private void invitesuceess(){

    final Dialog dialog = new Dialog(this);
    dialog.setContentView(R.layout.layout_success_invite);
    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    View yes = dialog.findViewById(R.id.yes_text);



    yes.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog.dismiss();


        }
    });


    dialog.show();


}


// postid  , recrutitedrid , caid ,  //

private void sendUser(String v1, String v2 ,String v3){
        RequestGlobal datahandel = new RequestGlobal();
        datahandel.postid = v1 ;
    datahandel.recruiterid = v2 ;
    datahandel.canid = v3 ;
    datahandel.apikey = prefManager.getApikey();
    compositeDisposable.add(apiService.senduserpost(datahandel)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(false, this) {
                @Override
                public void onApiSuccess(BaseResponse baseResponse) {

                    if (baseResponse.statuscode == 1){



                    }else{

                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(),"Sending Failed !");
                    }

                }

                @Override
                public void onFailure(Throwable e) {




                }
            }));








}




    //popup for the
    String firstvalue , secondvalue ;
    private void getDialogSort() {
        RadioGroup radioGroup;
        SeekBar experience;
        TextView tvExpYrs;
        TextView doneBtn;

        //data handling here ;




       RangeSeekBar sb_range_;


        sortbts = new BottomSheetDialog(this);
        sortsheet = getLayoutInflater().inflate(R.layout.filterlayout_seeker, null);
        sortbts.setContentView(sortsheet);
        sb_range_ = sortsheet.findViewById(R.id.sb_range);
        tvExpYrs = sortsheet.findViewById(R.id.tvExpYrs);
        doneBtn = sortsheet.findViewById(R.id.doneBtn);
        radioGroup = sortsheet.findViewById(R.id.radioGroup);
        sb_range_.setProgress(0f,12f);
        sb_range_.setEnableThumbOverlap(false);
        sb_range_.setRange(0f,12f);
        tvExpYrs.setText("0+");

        radioGroup.check(R.id.radioButton1);






        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


             System.out.println("MaxrangLength"+firstvalue+"--"+secondvalue+"MinRange");

                      getfillDatafilter("","","","","","",firstvalue,secondvalue)    ;

                sortbts.dismiss();
            }
        });


        sb_range_.setOnRangeChangedListener(new OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float leftValue, float rightValue, boolean isFromUser) {

                   int  mleftvalue ;

                       int  mRightvalue ;


                 mleftvalue = Math.round(leftValue);
                 mRightvalue = Math.round(rightValue);

                System.out.println("leftvalue"+mleftvalue+"Rightvalue"+mRightvalue);

                String a = String.valueOf(mleftvalue);
                String b = String.valueOf(mRightvalue);


                firstvalue = String.valueOf(mleftvalue);
                secondvalue = String.valueOf(mRightvalue);




                if (a.equals("0") && b.equals("12")){

                    tvExpYrs.setText("0+ Yrs ");
                }

               else if (a.equals("1") && b.equals("12")){

                    tvExpYrs.setText("1+ Yrs ");
                }


               else if (a.equals("2") && b.equals("12")){

                    tvExpYrs.setText("2+ Yrs ");
                }

               else if (a.equals("3") && b.equals("12")){

                    tvExpYrs.setText("3+ Yrs ");
                }

              else  if (a.equals("4") && b.equals("12")){

                    tvExpYrs.setText("4+ Yrs ");
                }

             else   if (a.equals("5") && b.equals("12")){

                    tvExpYrs.setText("5+ Yrs ");
                }

              else   if (a.equals("6") && b.equals("12")){

                    tvExpYrs.setText("6+ Yrs ");
                }

                else   if (a.equals("7") && b.equals("12")){

                    tvExpYrs.setText("7+ Yrs ");
                }

                else   if (a.equals("8") && b.equals("12")){

                    tvExpYrs.setText("8+ Yrs ");
                }

                else   if (a.equals("9") && b.equals("12")){

                    tvExpYrs.setText("9+ Yrs ");
                }

                else   if (a.equals("10") && b.equals("12")){

                    tvExpYrs.setText("10+ Yrs ");
                }

                else   if (a.equals("11") && b.equals("12")){

                    tvExpYrs.setText("11+ Yrs ");
                }

else{


                     tvExpYrs.setText(a + " - " + b + " Yrs");
                }

               // tvExpYrs.setText(String.valueOf(Math.round(leftValue))+"-"+String.valueOf(Math.round(rightValue)));

            }

            @Override
            public void onStartTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }

            @Override
            public void onStopTrackingTouch(RangeSeekBar view, boolean isLeft) {

            }
        });


        sortbts.show();

    }






}
