package com.abln.futur.activites;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.chat.IMInstance;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.Network.DataParser;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.futur.R;
import com.abln.futur.common.Address;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.ConstantUtils;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.ModChat;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PostJob;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AppliedData;
import com.abln.futur.common.models.AppliedInfo;
import com.abln.futur.common.models.RequestGlobal;
import com.abln.futur.common.newview.DataView;
import com.abln.futur.common.postjobs.post;
import com.abln.futur.common.savedlist.Savedlist;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.module.chats.adapter.GetAllUsersResponse;
import com.abln.futur.services.NetworkOperationService;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ListApplicataints extends BaseActivity  implements ApplicationHolder.viewHandler{

    private PrefManager prefManager = new PrefManager();

    RecyclerView applicationview;

    ConstraintLayout viewpdfview;
    Button accept;
    Button decline;

    Button revertback;

    ApplicationHolder items;


    String fileName;
    TextView totalapp ; //Total Applicants :
    private NetworkOperationService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.applicant);
        mService = new NetworkOperationService();
        setTaskCompleteListener(this);
        applicationview = findViewById(R.id.viewapplication);
        totalapp = findViewById(R.id.totalnumber);
        Bundle bundle = getIntent().getExtras();
        String apikey =    bundle.getString("data");
        getsaveddata(apikey);
        getAllUserListFromServer();
        applicationview.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));


    }


    private void getsaveddata(String apikey ) {
        RequestGlobal datahandel = new RequestGlobal();
      //  datahandel.apikey ="74edde002a6cc35780557f988b02730e";

        datahandel.apikey = apikey;
        compositeDisposable.add(apiService.recapplied("v1/get-applied",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AppliedData>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        AppliedData data_list = (AppliedData) baseResponse.data;
                        ArrayList<AppliedInfo> final_list = data_list.user_list;
                        totalapp.setText("Total Applicants : "+data_list.tstat);

                        items = new ApplicationHolder(ListApplicataints.this, final_list, ListApplicataints.this);
                        applicationview.setAdapter(items);

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }

    @Override
    public void onAccept(AppliedInfo savedlist) {
        setAccept( savedlist);


    }

    @Override
    public void onDecline(AppliedInfo savedlist) {
        setDecline(savedlist);

    }

    @Override
    public void onRevert(AppliedInfo savedlist) {


        setRevertback(savedlist);



    }

    @Override
    public void onPdfview(AppliedInfo arg) {

        //   11 and second house  //

        UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"ViewPDF ");

    }

    @Override
    public void onChat(AppliedInfo arg) {



if (!arg.canID.isEmpty()){

    ArrayList<String> userIdList = new ArrayList<String>();
    userIdList.add(arg.canID);
    ChatHelper.doLaunchNewChatThread(this, userIdList);

}


    }

    private void closeChatUserListWindow() {
        finish();
    }

    @Override
    public void onReviewd(AppliedInfo arg) {


       String file =  arg.can_details.file ;
       String fileName = arg.can_details.filename;
       UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"RESUME REVIVED ");
       downloadForm(file,fileName);


    }

    @Override
    public void onVstat(AppliedInfo arg ) {

    }

    @Override
    public void mApp(AppliedInfo arg) {

    }







    private void setAccept(AppliedInfo info) {
        RequestGlobal datahandel = new RequestGlobal();
        datahandel.canid =  info.canID;
        datahandel.postid = info.posID;

        datahandel.apikey ="74edde002a6cc35780557f988b02730e";

        compositeDisposable.add(apiService.caccept("v1/can-accept",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {


                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }



    /// declined status ;



    private void setDecline(AppliedInfo info) {


        RequestGlobal datahandel = new RequestGlobal();
        datahandel.canid =  info.canID;
        datahandel.postid = info.posID;

        compositeDisposable.add(apiService.creject("v1/can-reject",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {



                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }


    // revertback

    private void setchat() {
        RequestGlobal datahandel = new RequestGlobal();
        datahandel.apikey ="74edde002a6cc35780557f988b02730e";

        compositeDisposable.add(apiService.recapplied("v1/get-applied",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AppliedData>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        AppliedData data_list = (AppliedData) baseResponse.data;
                        ArrayList<AppliedInfo> final_list = data_list.user_list;
                        items = new ApplicationHolder(getApplicationContext(), final_list, ListApplicataints.this);
                        applicationview.setAdapter(items);

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }





    // handel chat
    private void setRevertback(AppliedInfo info) {
        RequestGlobal datahandel = new RequestGlobal();

        datahandel.postid = info.posID;
        datahandel.canid = info.canID;


        compositeDisposable.add(apiService.revertstatus("v1/revert-status",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<JsonObject>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {



                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }


    // handle reviewchat

    private void setreview() {
        RequestGlobal datahandel = new RequestGlobal();
        datahandel.apikey ="74edde002a6cc35780557f988b02730e";

        compositeDisposable.add(apiService.recapplied("v1/get-applied",datahandel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AppliedData>>(false, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        AppliedData data_list = (AppliedData) baseResponse.data;
                        ArrayList<AppliedInfo> final_list = data_list.user_list;


                        items = new ApplicationHolder(getApplicationContext(), final_list, ListApplicataints.this);
                        applicationview.setAdapter(items);

                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));

    }



    // viewresume

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

            Toast.makeText(ListApplicataints.this, "There is no any PDF Viewer", Toast.LENGTH_LONG).show();


        }

    }







    // new function added ;


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



    private void downloadForm(String Url_path, String file_name) {
        new ListApplicataints.GetFiles().execute(Url_path, file_name);
    }




    private void getAllUserListFromServer() {

        FuturProgressDialog.show(this, false);
        GetUserRequest mFavoriteRquestBody = new GetUserRequest();
        mFavoriteRquestBody.setCount(1);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(this, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getAllUser);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        startService(intent);
    }




    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if(responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.getAllUser)){
            FuturProgressDialog.dismissDialog();
            GetAllUsersResponse getAllUsersResponse = DataParser.parseJson(responseString, GetAllUsersResponse.class);
            if(getAllUsersResponse.getStatuscode() == 0){
                UIUtility.showToastMsg_short(this, getAllUsersResponse.getStatusMessage());
                return;
            } else if( getAllUsersResponse.getStatuscode() == 1){

                if(getAllUsersResponse.getData().getUserList().size() == 0){
                    UIUtility.showToastMsg_short(this, "No data found");
                    return;
                }
                String firstName = null;
                ArrayList<IMChatUser> chatUsers = new ArrayList<>();
                for(int i=0; i<getAllUsersResponse.getData().getUserList().size();i++){
                    firstName = getAllUsersResponse.getData().getUserList().get(i).getFirstName();

                    IMChatUser chatUser = new IMChatUser();
                    if (null != firstName) {
                        chatUser.userId = getAllUsersResponse.getData().getUserList().get(i).getApikey() + "";
                        chatUser.userName = firstName.trim();
                        chatUsers.add(chatUser);
                    }

                    //   mySGList.add(getAllPatientResponse.getData().getPatientList().get(i));
                }

                AppConfig.saveChatInfo(chatUsers, this);
//                IMInstance.getInstance().setUnSeenChatMsgCountListener((BaseFragment) getActivity());
                IMInstance.getInstance().updateLoggedInuserStatus(true);
//                IMInstance.getInstance().updateFreshLogin(true);
                FLog.d("ChatHandler", String.valueOf(IMInstance.getInstance().getTotalUnSeenChatMsgCount()));










            }


        }
    }



}