package com.abln.futur.module.account.fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abln.chat.IMInstance;
import com.abln.chat.ui.activities.IMChatActivity;
import com.abln.chat.utils.PdfViewer;
import com.abln.futur.R;
import com.abln.futur.activites.DashboardActivity;
import com.abln.futur.activites.SplashActivity;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FileDataHandler;
import com.abln.futur.common.FileViewer;
import com.abln.futur.common.FuturApiClient;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NewJobPost;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AccountOne;
import com.abln.futur.common.models.GetTotalNumberPost;
import com.abln.futur.common.models.RequestGlobal;
import com.abln.futur.common.models.TotalNumber;
import com.abln.futur.common.newview.FinalDataSets;
import com.abln.futur.common.postjobs.BaseNewFragment;
import com.abln.futur.common.postjobs.GetNormalpost;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.fragments.BaseFragment;
import com.abln.futur.interfaces.NetworkOperation;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.account.activities.BasicDetailsAccountActivity;
import com.abln.futur.module.account.activities.OthersAccountActivity;
import com.abln.futur.module.account.activities.ProfessionDetailsAccountActivity;
import com.abln.futur.module.job.activities.MyJobPostsActivity;
import com.abln.futur.services.NetworkOperationService;
import com.abln.futur.utils.FuturNotificationHandler;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.JsonObject;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.abln.chat.ui.helper.ChatMultiMediaHelper.getFilePathFromURI;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends BaseNewFragment implements TaskCompleteListener {

    private static final String TAG = "ChatsFragment";
    @BindView(R.id.editPhoneNumber)
    TextView tvPhoneNumber;


    @BindView(R.id.resumeUpload)
    ImageView ivresume;





    @BindView(R.id.totalnumberofpost)
    TextView totanumberofjobs;
    private Context mContext;
    private NetworkOperationService mService;
    private String mfilename;
    private int REQUEST_CODE_FILE = 1000;
    private PrefManager prefManager = new PrefManager();
    private static int REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF = 2020;
    private String pdfFilePath = "";

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_account;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mContext = getActivity();
        mService = new NetworkOperationService();
        ispdfexist();
        //  setTaskCompleteListener(mContext, this);
        getTotalNumberofJobPost();
        tvPhoneNumber.setText(prefManager.getMobilenumber());

    }


    private void selectResumePopup() {
        final CharSequence[] items = {"View Resume", "Upload New Resume",};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = UIUtility.checkPermission(getActivity());

                if (items[item].equals("View Resume")) {
                    if (result)
                        UIUtility.showToastMsg_withAlertInfoShort(getActivity(),"View Resume");
                         getfileurl();

                } else if (items[item].equals("Upload New Resume")) {
                    if (result)
                        openfilepicker();


                }
            }
        });
        builder.show();
    }


public void viewResume(String url , String filename){







}




    String fileName;

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

    //private static final int MEGABYTE = 1024 * 1024 ;
    //public static void doiwnalodifle(String fileurl , File directory){
    // urlconeciton.connect();
    //
    //fileoutputstrea . added out thefunction to add the main fucntion in the url
    // }



    //todo moving to the data handler
    public void getUrl(String url) {

        Intent i = new Intent(getContext(), FileViewer.class);
        i.putExtra("url",url);
        startActivity(i);

    }




    @OnClick({R.id.resumeUpload, R.id.postaJob, R.id.myJobPosts, R.id.editPhoneNumber, R.id.editBasicDetails,
            R.id.editProfDetails, R.id.editOtherAccount, R.id.openSignOutDialog, R.id.raiseTicketSec,
            R.id.inviteFriends, R.id.rate_app_txt})
    void onCLick(View v) {
        switch (v.getId()) {
            case R.id.resumeUpload:



//TODO handel the objec files

                if (isexist){

                    selectResumePopup();
                }else {
                    openfilepicker();
                }




                break;

            case R.id.postaJob:
                createAJobPost();
                break;


            case R.id.myJobPosts:
                Intent intent1 = new Intent(mContext, MyJobPostsActivity.class);
                startActivity(intent1);
                break;


            case R.id.editPhoneNumber:
                //Intent intent = new Intent(mContext, BasicNumber.class);
                //startActivity(intent);
                break;

            case R.id.editBasicDetails:
                Intent basicDetails = new Intent(mContext, BasicDetailsAccountActivity.class);
                startActivity(basicDetails);
                break;


            case R.id.editProfDetails:
                Intent profDetails = new Intent(mContext, ProfessionDetailsAccountActivity.class);
                startActivity(profDetails);
                break;


            case R.id.editOtherAccount:
                Intent otherAccount = new Intent(mContext, OthersAccountActivity.class);
                startActivity(otherAccount);
                break;

            case R.id.openSignOutDialog:
                openSignOutDialog(v);
                break;


            case R.id.raiseTicketSec:
                final Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setType("message/rfc822");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"dev.medinin@gmail.com"});
                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback ");
                emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                break;

            case R.id.inviteFriends:
                String shareBody = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();

                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Futur");

                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Futur " + shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
                break;

            case R.id.rate_app_txt:
                Intent ip = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + mContext.getPackageName()));
                startActivity(ip);
                break;
        }
    }

    private void createAJobPost() {
        NewJobPost bottomPostjob = NewJobPost.newInstance();
        bottomPostjob.show(getFragmentManager(), "TAG");

    }

    private void uploadResume() {
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(REQUEST_CODE_FILE)
                .withFilter(Pattern.compile(".*\\.pdf$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(false) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }


    public void openSignOutDialog(View v) {
        View view = getLayoutInflater().inflate(R.layout.signout_popup, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext);
        dialog.setContentView(view);
        view.findViewById(R.id.signout_txt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                prefManager.clearSession();
                IMInstance.getInstance().clearAllChatData();
                Intent intent = new Intent(AppConfig.getInstance().getApplicationContext(), SplashActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().finish();
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        view.findViewById(R.id.cancle_txt).setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FILE && resultCode == RESULT_OK) {
            mfilename = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            uploadResume(mfilename);
        }

        if (requestCode == REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF) {
            if (null != data) { // checking empty selection

                Uri uri = data.getData();
                String uriString = uri.toString();
                File myFile = new File(uriString);

                String path = getFilePathFromURI(getActivity(), uri);

                pdfFilePath = path;
                uplodpdf(pdfFilePath);
            }

        }
    }


    public void uploadResume(String imgName) {
        FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Uploading", "Resume Upload started");
        File imageFile = new File(imgName);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf", imgName, requestFile);

        RequestBody partMap = RequestBody.create(okhttp3.MultipartBody.FORM, prefManager.getApikey());
        NetworkOperation apiService = FuturApiClient.getClient2().create(NetworkOperation.class);
        Call<Object> call = apiService.updateResume(body, partMap);

        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    UIUtility.showToastMsg_withSuccessShort(mContext, "Resume updated successfully");
                    FuturNotificationHandler.removeAllNotifications();
                    FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Done !", "Resume updated successfully");
                } catch (Exception ex) {
                    UIUtility.showToastMsg_withErrorShort(mContext, "Not Uploaded");
                    FuturNotificationHandler.removeAllNotifications();
                    FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Failed !", "Resume Not Uploaded");
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                UIUtility.showToastMsg_withErrorShort(mContext, "Upload Failed");
                FuturNotificationHandler.removeAllNotifications();
                FuturNotificationHandler.showNotification(mContext, prefManager.getApikey(), "Failed !", "Resume Upload Failed");
            }
        });
    }


    public void uploadNewResumeFunction(String key ,String data){

        downloadForm(data,key);

    }



    private void downloadForm(String Url_path, String file_name) {
        new AccountFragment.GetFiles().execute(Url_path, file_name);
    }


    private void openfilepicker() {
        Intent filesIntent;
        filesIntent = new Intent(Intent.ACTION_GET_CONTENT);
        filesIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        filesIntent.addCategory(Intent.CATEGORY_OPENABLE);
        filesIntent.setType("application/pdf");
        startActivityForResult(filesIntent, REQUEST_CODE_FOR_ON_ACTIVITY_RESULT_PDF);
    }




    private void uplodpdf( String filname) {


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "img_" + timeStamp;
        File file = new File(filname);
        RequestBody requestFile = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("pdf",file.getName(),requestFile);
        Map<String, RequestBody> partMap = new HashMap<>();
        partMap.put("apikey", RequestBody.create(MediaType.parse("text/plain"), prefManager.getApikey()));
        compositeDisposable.add((apiService.uploadpdf(partMap, body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                        UIUtility.showToastMsg_withSuccessShort(mContext,"Resume uploaded Successfully");

                        ispdfexist();
                    }
                    @Override
                    public void onFailure(Throwable e) {
                        UIUtility.showToastMsg_withErrorShort(mContext, "Opp's something happened ");
                    }
                })));



    }


//TODO resume file url


    private void getfileurl(){
        RequestGlobal fileview = new RequestGlobal();
        fileview.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.viewfile("v1/view-resume",fileview)
        .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<FileDataHandler>>(true,this)
                               {
                                   @Override
                                   public void onFailure(Throwable e) {

                                   }


                                   @Override
                                   public void onApiSuccess(BaseResponse baseResponse) {



                                       if (baseResponse.statuscode == 1){


                                        FileDataHandler data = (FileDataHandler)   baseResponse.data;

                                        uploadNewResumeFunction(data.filename,data.file);



                                       }else{

                                           UIUtility.showToastMsg_withErrorShort(getActivity(),"Network Error ");

                                       }

                                   }



                               }

                )

        );

    }




    private void getTotalNumberofJobPost(){

        AccountOne getTotalNumberPost = new AccountOne();
        getTotalNumberPost.apikey = prefManager.getApikey();
        compositeDisposable.add(apiService.getTotalData("v1/cpost",getTotalNumberPost)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<TotalNumber>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        if (baseResponse.statuscode == 1) {


                            TotalNumber totalNumber =(TotalNumber) baseResponse.data;

                            totanumberofjobs.setText("("+totalNumber.count+")");



                        } else if (baseResponse.statuscode == 0) {





                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));



    }


    boolean isexist;
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

                            isexist=false;
                          //  ivresume.setImageDrawable(mContext.getDrawable(R.drawable.ic_upload_resume));
                            // resume not found

                        } else {


                          //  ivresume.setImageDrawable(mContext.getDrawable(R.drawable.ic_update_resume_new));
                            isexist=true;

                            //resume found
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {




                    }
                }));


    }







}
