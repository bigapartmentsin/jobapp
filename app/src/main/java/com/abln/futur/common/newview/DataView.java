package com.abln.futur.common.newview;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.activites.RenderPdfView;
import com.abln.futur.activites.StoriesActivity;
import com.abln.futur.common.Address;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.Minfo;
import com.abln.futur.common.models.Mkey;
import com.abln.futur.common.models.Mstories;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.job.activities.MyPostedJobListRequest;
import com.abln.futur.services.NetworkOperationService;
import com.abln.futur.utils.NetworkPdfviewr;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.rahuljanagouda.prettydialog.PrettyDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;
import static com.abln.futur.common.newview.ItemAdapter.SPAN_COUNT_ONE;
import static com.abln.futur.common.newview.ItemAdapter.SPAN_COUNT_THREE;

public class DataView extends NewBaseActivity implements ItemAdapter.clickHandler, TaskCompleteListener {


    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    @BindView(R.id.jobResult_recyclerview)
    RecyclerView jobResult_recyclerview;
    @BindView(R.id.grid_layout)
    ImageView imageView;
    @BindView(R.id.list_layout)
    ImageView listimage;
    @BindView(R.id.sort)
    ImageView sort;


    @BindView(R.id.back)
        ImageView back;
    Context mContext = DataView.this;
    post fulldatavalues;
    ArrayList<FinalDataSets> items;
    private GridLayoutManager gridLayoutManager;
    private ItemAdapter itemAdapter;
    private PrefManager prefManager = new PrefManager();
    private String finalexp = "";
    private String checkedStateExperience = "";
    private String checkStatesButton = "";
    private String lat;
    private String lng;
    private String radius;
    private String stringQue;
    private String pdfFilePath = "";

    RadioGroup radioGroup;
    RadioButton radioButton1;
    RadioButton radioButton2;
    RadioButton selectedRadioButton;
    BottomSheetDialog bts;
    View sheetView;

    String query,rad;

    String mlat, mlng;

    @Override
    public int getLayoutId() {
        return R.layout.activity_job_seeker_search_result;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = this.getIntent().getExtras();
        query = extras.getString("title");
       rad = extras.getString("roolingvalue");
       mlat = extras.getString("lat");
       mlng = extras.getString("lng");

   //    Log.d("TOTAL",rad);


        getExperience();


        getFullData(mlat, mlng, prefManager.getApikey(), query, rad, prefManager.getUserExperinece());

        gridLayoutManager = new GridLayoutManager(this, SPAN_COUNT_ONE);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listimage.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
                imageView.setImageDrawable(null);
                listimage.setImageDrawable(getResources().getDrawable(R.drawable.ic_list_view));
                switchLayout();

            }


        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        listimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchLayout();
                listimage.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(null);
                imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_grid_view));


                //       switchLayout();

            }
        });


        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                getDialog();


            }
        });
    }







    private void getDialog() {

        SeekBar experience;
        TextView tvExpYrs;
        TextView doneBtn;


        bts = new BottomSheetDialog(this);
        sheetView = getLayoutInflater().inflate(R.layout.filterlayout_job_search, null);
        bts.setContentView(sheetView);

        experience = sheetView.findViewById(R.id.expSeekbar);
        tvExpYrs = sheetView.findViewById(R.id.tvExpYrs);
        doneBtn = sheetView.findViewById(R.id.doneBtn);
        radioGroup = sheetView.findViewById(R.id.radioGroup);



        experience.setMin(0);
        experience.setMax(12);


        if (checkedStateExperience.equals("")) {


            if (!finalexp.isEmpty()) {


                String val = finalexp.trim();
                String val2 = val.replace("+", "");
                val2 = val2.replaceAll("\\s+", "");


                experience.setProgress(Integer.parseInt(val2));

                tvExpYrs.setText(val2 + "+");
                radioGroup.check(R.id.radioButton1);


            }

        } else {

            String str = checkedStateExperience;
            String strNew = str.replace("+", "");
            experience.setProgress(Integer.parseInt(strNew));
            tvExpYrs.setText(strNew + "+");
            if (checkStatesButton.equals("1")) {

                radioGroup.check(R.id.radioButton1);

            } else if (checkStatesButton.equals("2")) {

                radioGroup.check(R.id.radioButton2);

            }


        }


        experience.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                tvExpYrs.setText(new Integer(progress) + "+");

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // get selected radio button from radioGroup
                int selectedId = radioGroup.getCheckedRadioButtonId();


                int selectedRadioButtonID = radioGroup.getCheckedRadioButtonId();
                checkedStateExperience = tvExpYrs.getText().toString();

                switch (selectedRadioButtonID) {
                    case R.id.radioButton1:
                        checkStatesButton = "1";



                        String str = checkedStateExperience;
                        String strNew = str.replace("+", "");
                        getFullData(mlat, mlng, prefManager.getApikey(), query, rad, strNew);

                        break;
                    case R.id.radioButton2:
                        checkStatesButton = "2";
                        String ast = checkedStateExperience;
                        String nnew = ast.replace("+", "");

                        getData(mlat, mlng, prefManager.getApikey(), query, rad, nnew);

                        break;

                }


                bts.dismiss();

            }
        });


        bts.show();

    }


    private void getExperience() {
        post dataset = new post();
        dataset.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getExperience("v1/experience",dataset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Experience>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        Experience experience = (Experience) baseResponse.data;


                        if (baseResponse.statuscode == 1) {


                            String exp = experience.exp;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                                finalexp = exp.trim();
                            }


                        }
                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));
    }


    private void savedPostData(FinalDataSets finalDataSets, String val) {


        fulldatavalues.postid = finalDataSets.pid;
        fulldatavalues.recruiterid = finalDataSets.uid;
        fulldatavalues.canid = prefManager.getApikey();
        fulldatavalues.starred_status = val;


        compositeDisposable.add(apiService.onSaved("v1/saved",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {



                        if (baseResponse.statuscode==1){

                            finalDataSets.isstarred = val;
                            itemAdapter.notifyDataSetChanged();

                        }else if (baseResponse.statuscode == 2){



                            finalDataSets.isstarred = val;
                            itemAdapter.notifyDataSetChanged();





                        }else if (baseResponse.statuscode == 4 ){


                       UIUtility.showToastMsg_withErrorShort(getApplicationContext(),"cannot save your post!");


                        }



                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));


    }


    private void clickappy(FinalDataSets finalDataSets) {

        //onApply


        fulldatavalues.postid = finalDataSets.pid;
        fulldatavalues.recruiterid = finalDataSets.uid;
        fulldatavalues.canid = prefManager.getApikey();

        compositeDisposable.add(apiService.onApply("v1/can-rec-apply",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        //   itemAdapter.notifyDataSetChanged();

                        if (baseResponse.statuscode == 0) {

                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Something went wrong !");
                        } else if (baseResponse.statuscode == 1) {


                            UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(), "Already Applied to the post");

                        } else if (baseResponse.statuscode == 5) {
                            finalDataSets.jexpstatus = "1";
                            itemAdapter.notifyDataSetChanged();

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Applied");
                        }

                        else if (baseResponse.statuscode == 4){
                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "You Cannot apply to your own post");

                        }else if(baseResponse.statuscode == 2){



                            finalDataSets.jexpstatus = "1";
                            itemAdapter.notifyDataSetChanged();

                           // getFullData("12.91582330", "77.64532000", prefManager.getApikey(), query, "1", finalexp);
                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Applied");

                        }




                    }

                    @Override
                    public void onFailure(Throwable e) {


                        //    UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));

    }

    private void getFullData(String v1, String v2, String v3, String v4, String v5, String v6) {


        fulldatavalues = new post();
        fulldatavalues.lat = v1;
        fulldatavalues.lng = v2;
        fulldatavalues.apikey = v3;
        fulldatavalues.query = v4;
        fulldatavalues.radius = v5;
        fulldatavalues.experience = v6;

        compositeDisposable.add(apiService.fulldata("v1/full-data",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Address>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        Address address = (Address) baseResponse.data;


                        if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully loaded ");
                            Address userData = (Address) baseResponse.data;
                            ArrayList<FinalDataSets> jc = userData.results;

                            //ItemAdapter
                            itemAdapter = new ItemAdapter(DataView.this, jc, gridLayoutManager, DataView.this);
                            jobResult_recyclerview.setAdapter(itemAdapter);
                            jobResult_recyclerview.setLayoutManager(gridLayoutManager);


                        } else if (baseResponse.statuscode == 0) {


                            Address userData = (Address) baseResponse.data;
                            ArrayList<FinalDataSets> jc = userData.results;

                            //ItemAdapter
                            itemAdapter = new ItemAdapter(DataView.this, jc, gridLayoutManager, DataView.this);
                            jobResult_recyclerview.setAdapter(itemAdapter);
                            jobResult_recyclerview.setLayoutManager(gridLayoutManager);

                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Sorry no data found !");
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        // UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));

        /*
        *
        * add new files stories view to get i*/


    }


    @Override
    public void share() {

        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.abln.futur")));
    }

    private void switchLayout() {


        if (gridLayoutManager.getSpanCount() == SPAN_COUNT_ONE) {
            gridLayoutManager.setSpanCount(SPAN_COUNT_THREE);
        } else {
            gridLayoutManager.setSpanCount(SPAN_COUNT_ONE);
        }
        itemAdapter.notifyItemRangeChanged(0, itemAdapter.getItemCount());


    }


    @Override
    public void onApplyClick(FinalDataSets searchResult) {


        if (searchResult.jexpstatus.equalsIgnoreCase("0")) {

            //   :  TODO Apply network call :

            clickappy(searchResult);


        } else {

            //TODO SORRY you have been already applied

            UIUtility.showToastMsg_withAlertInfoShort(this, "You have already applied to the post ");


        }


    }

    @Override
    public void onapplySave(FinalDataSets searchResult) {


        if (searchResult.isstarred.equalsIgnoreCase("0")) {
            savedPostData(searchResult, "1");
        } else {
            savedPostData(searchResult, "0");
        }
    }


    @Override
    public void checkpdfexist(FinalDataSets finalDataSets) {

        ispdfexist(finalDataSets);

    }


    private void ispdfexist(FinalDataSets finalDataSets) {


        post data = new post();
        data.apikey = prefManager.getApikey();


        compositeDisposable.add(apiService.checkresume("v1/get-resume",data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        if (baseResponse.statuscode == 0) {

                            showCustomDialog();

                        } else {
                            applyjobpost(finalDataSets);
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));


    }


    private void checkpdf() {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.test_two_data, null);
        FragmentManager fm = getSupportFragmentManager();
        PrettyDialog pd = PrettyDialog.newInstance(dialogView);
        pd.show(fm, "Testing");

    }

    private void showCustomDialog() {


        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.test_two_data, viewGroup, false);

        ImageView upload = dialogView.findViewById(R.id.upload_button);
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
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF) {
            if (null != data) { // checking empty selection
                if (null != data.getClipData()) { // checking multiple selection or not


                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {


                        Uri uri = data.getClipData().getItemAt(i).getUri();
                        FLog.d("Uri", "Multiple " + uri.getPath());
                        pdfFilePath = data.getClipData().getItemAt(i).getUri().toString();
                    }


                } else {
                    Uri uri = data.getData();
                    FLog.d("Uri", "Single " + uri.getPath());
                    pdfFilePath = data.getData().getPath();


                }


                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);

                String path = getFilePathFromURI(mContext, uri);
                Log.d("ioooo", path);
                pdfFilePath = path;

                uplodpdf(prefManager.getApikey(), pdfFilePath);
            }

        }


    }


    private void openFilePickerToUploadJobPost_Pdf() {


        Intent filesIntent;
        filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filesIntent.setType("application/pdf");
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF);


    }

    @Override
    public void redirect(FinalDataSets finalDataSets) {



        String key = finalDataSets.pid ;
        String uid = finalDataSets.uid;
        String apply = finalDataSets.jexpstatus;
        String star = finalDataSets.isstarred;
        getdata(key,apply,star,uid);



    }

    private void uplodpdf(String value, String data) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(data);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf", imageFileName + file.getName(), requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), value));
        compositeDisposable.add((apiService.uploadpdf(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {




                    }

                    @Override
                    public void onFailure(Throwable e) {
                        UIUtility.showToastMsg_withErrorShort(mContext, "Opp's something happened ");
                    }
                })));


    }


    private void applyjobpost(FinalDataSets searchResult) {
        if (searchResult.jexpstatus.equalsIgnoreCase("0")) {
            itemAdapter.notifyDataSetChanged();
            //TODO Apply network call
            clickappy(searchResult);
        } else {

            //TODO SORRY you have been already applied
            UIUtility.showToastMsg_withAlertInfoShort(this, "You have already applied to the post ");
        }
    }


// do


    private void getRecently() {
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

        String resquestTpe = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiurl = intent.getStringExtra(NetworkConfig.API_URL);
        String responsString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if (responsString != null && apiurl.equalsIgnoreCase(NetworkConfig.recent)) {
            String requstType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);



        }


    }


    private void getData(String v1, String v2, String v3, String v4, String v5, String v6) {
        fulldatavalues = new post();
        fulldatavalues.lat = v1;
        fulldatavalues.lng = v2;
        fulldatavalues.apikey = v3;
        fulldatavalues.query = v4;
        fulldatavalues.radius = v5;
        fulldatavalues.experience = v6;
        fulldatavalues.recently = "1";

        compositeDisposable.add(apiService.fulldata("v1/full-data",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Address>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        Address address = (Address) baseResponse.data;
                        ArrayList<FinalDataSets> address_list = address.results;


                        if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully loaded ");
                            Address userData = (Address) baseResponse.data;
                            ArrayList<FinalDataSets> jc = userData.results;

                            //ItemAdapter
                            itemAdapter = new ItemAdapter(DataView.this, jc, gridLayoutManager, DataView.this);
                            jobResult_recyclerview.setAdapter(itemAdapter);
                            jobResult_recyclerview.setLayoutManager(gridLayoutManager);


                        } else if (baseResponse.statuscode == 0) {

                            Address userData = (Address) baseResponse.data;
                            ArrayList<FinalDataSets> jc = userData.results;

                            //ItemAdapter
                            itemAdapter = new ItemAdapter(DataView.this, jc, gridLayoutManager, DataView.this);
                            jobResult_recyclerview.setAdapter(itemAdapter);
                            jobResult_recyclerview.setLayoutManager(gridLayoutManager);

                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        //     UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");
                        Log.d("DataView", "Network Error");

                    }
                }));


    }


    private void checkStories(){
        //Check for stories ;
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




                            Intent i = new Intent(DataView.this, RenderPdfView.class);
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




                            Intent intent = new Intent(DataView.this, StoriesActivity.class);
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


                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));
    }

}
