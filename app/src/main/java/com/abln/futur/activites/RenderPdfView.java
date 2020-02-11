package com.abln.futur.activites;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.abln.futur.R;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.FLog;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.UpdateKey;
import com.abln.futur.common.postjobs.post;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.gson.JsonObject;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;
import static com.abln.futur.common.UIUtility.screenStoryView;

public class RenderPdfView extends BaseActivity implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener, View.OnClickListener
{

    String fileName;
    String pdfFileName;
    PDFView pdfView;
    FrameLayout fl;
    TextView tvsavedjob;
    TextView tvsharejob;
    TextView tvapplyjob;
    String apply,star;
    private String pid,uid;
    private String pdfFilePath = "";
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    private PrefManager prefManager = new PrefManager();

    public static final String SAMPLE_FILE = "pdfview.pdf";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_render);
        getinfo();
        pdfView = findViewById(R.id.pdfView);
        fl = findViewById(R.id.event_wraper);
        tvsavedjob = findViewById(R.id.tvsavedjob);
        tvsharejob = findViewById(R.id.tvsharejob);
        tvapplyjob = findViewById(R.id.tvapplyjob);

        screenStoryView(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        Bundle bundle = getIntent().getExtras();
        String   val = bundle.getString("pdf");
        String name = bundle.getString("name");
        apply = bundle.getString("apply");
        star = bundle.getString("star");
        uid = bundle.getString("uid");
        pid = bundle.getString("key");
        tvsavedjob.setOnClickListener(this);
        tvapplyjob.setOnClickListener(this);
        tvsharejob.setOnClickListener(this);




        downloadForm(val,name);
        if (star.equalsIgnoreCase("0")) {
            tvsavedjob.setBackground(getApplicationContext().getDrawable(R.drawable.star_unsel));
        } else {
            tvsavedjob.setBackground(getApplicationContext().getDrawable(R.drawable.star_selt_one));

        }


        if (apply.equalsIgnoreCase("1")) {
            tvapplyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_applied));

        } else {
             tvapplyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_apply_new_curve));

        }




    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tvsavedjob:


                if (star.equalsIgnoreCase("0")) {
                    savedPostData("1");
                } else {
                    savedPostData("0");
                }

                break;
            case R.id.tvsharejob:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.abln.futur")));

                break;
            case R.id.tvapplyjob:


                if(apply.equalsIgnoreCase("1")){

                    UIUtility.showToastMsg_withAlertInfoShort(RenderPdfView.this,"you cannot apply to this job");
                }else{
                    ispdfexist();
                }




                break;


        }
    }

    private void getpdffile(String path){
        File file = new File(URI.create(path).getPath());
        pdfView.fromFile(file)
                .defaultPage(0)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .spacing(10)
                .onPageChange(this)
                .load();

        fl.setVisibility(View.VISIBLE);
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {
        UIUtility.showToastMsg_withErrorShort(this,"Error");
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
            getUrl(fileName);


        }
    }


    public void getUrl(String url){
        Uri path = Uri.fromFile(new File("/storage/self/primary/FuturApp/" + url));
        File file = new File(path.getPath());
        if (file.exists()) {
            getpdffile(path.toString());
        }else{
            UIUtility.showToastMsg_withErrorShort(this,"File got corrupted");
        }

    }


    public String getPDFPath(Uri uri){
        final String id = DocumentsContract.getDocumentId(uri);
        final Uri contentUri = ContentUris.withAppendedId(
                Uri.parse("content://downloads/futurapp"), Long.valueOf(id));
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }


    private void downloadForm(String Url_path, String file_name) {
        new RenderPdfView.GetFiles().execute(Url_path, file_name);
    }


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





    private void ispdfexist() {


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

                            //TODO applyjobpost have to do .

                               applyjobpost();

                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));


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
                UIUtility.showToastMsg_withAlertInfoShort(RenderPdfView.this, "Upload pfd");
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

                String path = getFilePathFromURI(RenderPdfView.this, uri);

                pdfFilePath = path;

                uplodpdf(prefManager.getApikey(), pdfFilePath);
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
                        UIUtility.showToastMsg_withErrorShort(RenderPdfView.this, "Opp's something happened ");
                    }
                })));


    }




    private void savedPostData(String val) {

        post fulldatavalues = new post();
        fulldatavalues.postid = pid;
        fulldatavalues.recruiterid = uid;
        fulldatavalues.canid = prefManager.getApikey();
        fulldatavalues.starred_status = val;


        compositeDisposable.add(apiService.onSaved("v1/saved",fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {



                        if (baseResponse.statuscode==1){


                        }else if (baseResponse.statuscode == 2){

                            System.out.println("Check for data value "+val);

                            if(val.equalsIgnoreCase("0")){

                                star = "0";

                                tvsavedjob.setBackground(getApplicationContext().getDrawable(R.drawable.star_unsel));

                                System.out.println("unselectedd");


                            }else if(val.equalsIgnoreCase("1")){

                                star = "1" ;

                                System.out.println("Selected");


                                tvsavedjob.setBackground(getApplicationContext().getDrawable(R.drawable.star_selt_one));

                            }
                            //todo logic for handling the state
                            //  finalDataSets.isstarred = val;
                            //itemAdapter.notifyDataSetChanged();
                            // todo end the handling state


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



    private void applyjobpost() {



        if (apply.equalsIgnoreCase("0")) {

            clickappy();

        } else {

            UIUtility.showToastMsg_withAlertInfoShort(this, "You have already applied to the post ");

        }




    }





    private void clickappy() {

        //onApply

        //TODO postid , recruiterid , canid

        post fulldatavalues = new post();
        fulldatavalues.postid = pid;                     // postid
        fulldatavalues.recruiterid =uid;                // uid
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


                            //  TODO :   :
                            tvapplyjob.setClickable(false);
                            tvapplyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_applied));

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Applied");



                        }

                        else if (baseResponse.statuscode == 4){
                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "You Cannot apply to your own post");

                        }

                        else if(baseResponse.statuscode == 2){
                            tvapplyjob.setClickable(false);

                            tvapplyjob.setBackground(getApplicationContext().getDrawable(R.drawable.ic_applied));
                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully Applied");

                        }

                    }

                    @Override
                    public void onFailure(Throwable e) {


                        //    UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));

    }



    private void getinfo(){

        UpdateKey key = new UpdateKey();
        key.pid = pid;
        key.cid =prefManager.getApikey() ;



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




}
