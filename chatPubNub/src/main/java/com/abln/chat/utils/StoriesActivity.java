package com.abln.chat.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.abln.chat.R;
import com.abln.chat.core.model.IMChatUser;



import com.amazonaws.services.cognitosync.model.SubscribeToDatasetRequest;
import com.bumptech.glide.Glide;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;
import static com.abln.chat.ui.helper.ChatMultiMediaHelper.openChatMultimediaSlideActivity;


//
public class StoriesActivity extends AppCompatActivity implements StoriesProgressView.StoriesListener ,BaseView{


    protected CompositeDisposable compositeDisposable;
    protected com.abln.chat.utils.Handler apiService;
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    private StoriesProgressView storiesProgressView;
    private ImageView image;
    private String pdfFilePath = "";
    private FrameLayout event_wraper;
    private int counter = 0;
    private  String[] resourcesList ;
    private TextView savedjob;
    private TextView sharejobpost;
    private TextView applyjob;
    private TextView saveclick;
    long pressTime = 0L;
    long limit = 500L;
    String val ;
    View top,bottom;
    private String  apply,star;
    private String pid,uid;

    public String mainkey ;





    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };






   // private  void changeInformaton

    private IMChatUser mLoggedInUser = new IMChatUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_stories);
        apiService = com.abln.chat.utils.Service.getApiService();
        compositeDisposable = new CompositeDisposable();
        event_wraper = (FrameLayout) findViewById(R.id.event_wraper);
        storiesProgressView = findViewById(R.id.stories);
        sharejobpost = findViewById(R.id.sharejobpost);
        applyjob = findViewById(R.id.applyjob);
        saveclick = findViewById(R.id.saveclick);
        getinfo();
        init();

        Bundle bundle = getIntent().getExtras();
        screenStoryView(this);
        ArrayList<String> val = bundle.getStringArrayList("DATA");
        apply = bundle.getString("apply");
        star = bundle.getString("star");
        uid = bundle.getString("uid");
        pid = bundle.getString("key");
        mainkey = bundle.getString("cid");


     // ic_path_rounded:


        if (star.equalsIgnoreCase("0")) {


            saveclick.setBackground(getApplicationContext().getDrawable(R.drawable.star_unsel));
        } else {

            saveclick.setBackground(getApplicationContext().getDrawable(R.drawable.star_selt_one));

        }


        if (apply.equalsIgnoreCase("1")) {

            applyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_applied));

        } else {

            applyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_apply_new_curve));

        }



        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        resourcesList = new String[val.size()];
        resourcesList = GetStringArray(val);
        for (String key: val
        ) {

            Log.d("VAlue ----" , key);

        }
        getvisualstories();
    }








    private void init(){
        sharejobpost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.abln.futur")));

            }
        });

        applyjob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(apply.equalsIgnoreCase("1")){



                    Toast.makeText(StoriesActivity.this,"You cannot apply to this job",Toast.LENGTH_LONG).show();
                }else{
                    ispdfexist();
                }


            }
        });


        saveclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (star.equalsIgnoreCase("0")) {

                    savedPostData("1");

                } else {

                    savedPostData("0");

                }

            }
        });

    }




    private  void loadImageIntrnl(String imagePath, ImageView imageView) {
        try {


                    Glide.with(StoriesActivity.this).load(imagePath)
                            .dontAnimate()
                            .fitCenter()
                            .into(imageView);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public void getvisualstories(){

        storiesProgressView.setStoriesCount(resourcesList.length);
        storiesProgressView.setStoryDuration(9000L);

        storiesProgressView.setStoriesListener(this);

        counter = 0;
        storiesProgressView.startStories(counter);

        image = findViewById(R.id.image);


        System.out.println("------------ImageName"+resourcesList[counter]);

        loadImageIntrnl(resourcesList[counter],image);







        // bind reverse view
        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        // bind skip view
        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.skip();
            }
        });
        skip.setOnTouchListener(onTouchListener);


    }

    @Override
    public void onNext() {

      //  ImageLoader.loadImage(resourcesList[++counter], image);


        loadImageIntrnl(resourcesList[++counter], image);
    }

    @Override
    public void onPrev() {
        if ((counter - 1) < 0) return;

       // ImageLoader.loadImage(resourcesList[--counter], image);


        loadImageIntrnl(resourcesList[--counter], image);


    }

    @Override
    public void onComplete() {
    }

    @Override
    protected void onDestroy() {
        // Very important !
        storiesProgressView.destroy();
        super.onDestroy();
    }




    private void setImage(){

     //   Mkey mkey = new Mkey();


    }





    public static String[] GetStringArray(ArrayList<String> arr)
    {


        String str[] = new String[arr.size()];


        for (int j = 0; j < arr.size(); j++) {


            str[j] = arr.get(j);
        }

        return str;
    }



    private void viewPdfFile(String pdfFile) {
        try {
            Uri path = Uri.fromFile(new File("/storage/self/primary/FuturApp/" + pdfFile));
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.setDataAndType(path, "application/pdf");
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

            Toast.makeText(StoriesActivity.this, "There is no any PDF Viewer", Toast.LENGTH_LONG).show();


        }

    }

    @Override
    public void showProgress(String text) {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showInternetError() {

    }

    @Override
    public void showErrorDialog(String title, String description, String positiveBtn) {

    }


    private class GetFiles extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];
            String fileName = strings[1];
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "FuturApp");
            File flodaer = new File(getFilesDir(),"theFolder");
            flodaer.mkdir();
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            File pf = new File(flodaer,fileName

            );

            try {


                pf.createNewFile();


            } catch (IOException e) {
                e.printStackTrace();
            }
            downloadFile(fileUrl, pf);

            return null;
        }
    }



    private void downloadfileold(String Url_path, String file_name) {
        new GetFiles().execute(Url_path, file_name);
    }


    private static final int MEGABYTE = 1024 * 1024;
    public static void downloadFile(String fileUrl, File directory) {
        try {

            URL url = new URL(fileUrl);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            //urlConnection.setRequestMethod("GET");
            //urlConnection.setDoOutput(true);
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




    public void getSaved(){






    }



    public void getApply(){



    }




    private void getdata(String key) {
        Mkey mkey = new Mkey();
        mkey.id = key;
        compositeDisposable.add(apiService.getstories("v1/post-images",mkey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<Mstories>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {




                        if (baseResponse.statuscode == 1) {








                        }

                        if (baseResponse.statuscode ==2 ){





                        }



                    }

                    @Override
                    public void onFailure(Throwable e) {




                    }
                }));
    }




    private void ispdfexist() {


        AccountOne data = new AccountOne();
        data.apikey = mainkey;


        compositeDisposable.add(apiService.checkresume("v1/get-resume",data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        if (baseResponse.statuscode == 0) {

                            showCustomDialog();

                        } else {

                            //TODO applyjobpost have to do .

                            applyjobpost();

                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                    //    UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                        Toast.makeText(StoriesActivity.this,"Network Error",Toast.LENGTH_LONG).show();

                    }
                }));


    }



    private void showDataStoies()

    {
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dailogView  = LayoutInflater.from(this).inflate(R.layout.test_two_data,viewGroup,false);
        ImageView newimageView = dailogView.findViewById(R.id.upload_button);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dailogView);

        final AlertDialog alertDialog = builder.create();


        newimageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(StoriesActivity.this,"Making custome Dialog", Toast.LENGTH_LONG).show();

                openFilePickerToUploadJobPost_Pdf();
                alertDialog.show();

            }
        });


        alertDialog.show();

    }




    private void touchStoried(){


        //TODO touch the stories a
    }

    private void showCustomDialog() {


        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.test_two_data, viewGroup, false);

        ImageView upload = dialogView.findViewById(R.id.upload_button);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               // UIUtility.showToastMsg_withAlertInfoShort(StoriesActivity.this, "Upload pdf");

                Toast.makeText(StoriesActivity.this,"Upload pdf",Toast.LENGTH_LONG).show();

                openFilePickerToUploadJobPost_Pdf();
                alertDialog.dismiss();
            }
        });


        alertDialog.show();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF) {
            if (null != data) { // checking empty selection
                if (null != data.getClipData()) { // checking multiple selection or not


                    for (int i = 0; i < data.getClipData().getItemCount(); i++) {


                        Uri uri = data.getClipData().getItemAt(i).getUri();




                        pdfFilePath = data.getClipData().getItemAt(i).getUri().toString();
                    }


                } else {
                    Uri uri = data.getData();





                    pdfFilePath = data.getData().getPath();


                }


                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);

                String path = getFilePathFromURI(StoriesActivity.this, uri);

                pdfFilePath = path;

                uplodpdf(mainkey, pdfFilePath);
            }

        }


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


                       // UIUtility.showToastMsg_withErrorShort(StoriesActivity.this, "Opp's something happened ");

                        Toast.makeText(StoriesActivity.this,"Opp's something happened",Toast.LENGTH_LONG).show();
                    }
                })));


    }




    //TODO
    //jexpstatus;
    //jexpstatus;
    //jexpstatus;

    private void applyjobpost() {



        if (apply.equalsIgnoreCase("0")) {

            clickappy();

        } else {




            Toast.makeText(StoriesActivity.this,"You have already applied to the post",Toast.LENGTH_LONG).show();

        }




    }




    private void clickappy() {

        //onApply

        //TODO postid , recruiterid , canid

        post fulldatavalues = new post();
        fulldatavalues.postid = pid;                     // postid
        fulldatavalues.recruiterid =uid;                // uid
        fulldatavalues.canid = mainkey;
        compositeDisposable.add(apiService.onApply("v1/can-rec-apply",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        //   itemAdapter.notifyDataSetChanged();

                        if (baseResponse.statuscode == 0) {


                            Toast.makeText(StoriesActivity.this,"Something went wrong ",Toast.LENGTH_LONG).show();


                        } else if (baseResponse.statuscode == 1) {



                            Toast.makeText(StoriesActivity.this,"Already Applied to the post ",Toast.LENGTH_LONG).show();

                        } else if (baseResponse.statuscode == 5) {


                          //  TODO :   :

                            applyjob.setClickable(false);

                            applyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_applied));

                            Toast.makeText(StoriesActivity.this,"Successfully Applied",Toast.LENGTH_LONG).show();

                          ///  UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Applied");



                        }

                        else if (baseResponse.statuscode == 4){

                            Toast.makeText(StoriesActivity.this,"You Cannot apply to your own post",Toast.LENGTH_LONG).show();
                        //    UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "You Cannot apply to your own post");

                        }


                        else if(baseResponse.statuscode == 2){

                            applyjob.setClickable(false);

                            applyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_applied));

                            Toast.makeText(StoriesActivity.this , "Successfully Applied",Toast.LENGTH_LONG).show();

                          ///  UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Applied");

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        //    UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));

    }






    private void savedPostData(final String val) {

        post fulldatavalues = new post();
        fulldatavalues.postid = pid;
        fulldatavalues.recruiterid = uid;
        fulldatavalues.canid =     mainkey  ; //         prefManager.getApikey();
        fulldatavalues.starred_status = val;


        compositeDisposable.add(apiService.onSaved("v1/saved",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {



                        if (baseResponse.statuscode==1){

                            star = "1";
                            saveclick.setBackground(getApplicationContext().getDrawable(R.drawable.star_selt_one));


                        }else if (baseResponse.statuscode == 2){


                            if(val.equalsIgnoreCase("0")){

                                star = "0";
                                saveclick.setBackground(getApplicationContext().getDrawable(R.drawable.star_unsel));



                            }else if(val.equalsIgnoreCase("1")){

                                star = "1";
                                saveclick.setBackground(getApplicationContext().getDrawable(R.drawable.star_selt_one));

                            }
                            //todo logic for handling the state
                              //  finalDataSets.isstarred = val;
                             //itemAdapter.notifyDataSetChanged();
                            // todo end the handling state


                        }else if (baseResponse.statuscode == 4 ){


                          //  UIUtility.showToastMsg_withErrorShort(getApplicationContext(),"cannot save your post!");

                            Toast.makeText(StoriesActivity.this,"Cannot save your post",Toast.LENGTH_LONG).show();


                        }



                    }

                    @Override
                    public void onFailure(Throwable e) {


                    //    UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");


                        Toast.makeText(StoriesActivity.this,"Network Error",Toast.LENGTH_LONG).show();


                    }
                }));


    }





    private void getinfo(){

        UpdateKey key = new UpdateKey();
        key.pid = pid;
        key.cid =mainkey ;



        compositeDisposable.add(apiService.updateinfo("v1/stories-status",key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JobData>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        JobData soo = (JobData) baseResponse.data;
                        JobInfo da =  soo.results;







                    }

                    @Override
                    public void onFailure(Throwable e) {



                    }
                }));




    }




    public static  void screenStoryView(Activity activity){
        if(android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN)
        {
            //Code for Android version below JellyBean
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }else
        {
            //Code for Android version above JellyBean
            // Hide the status bar.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);

        }
    }




}