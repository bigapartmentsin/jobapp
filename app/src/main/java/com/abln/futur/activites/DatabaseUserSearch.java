package com.abln.futur.activites;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.futur.R;
import com.abln.futur.common.Address;
import com.abln.futur.common.BaseResponse;
import com.abln.futur.common.GlobalSingleCallback;
import com.abln.futur.common.NewBaseActivity;
import com.abln.futur.common.UIUtility;
import com.abln.futur.common.models.AppliedData;
import com.abln.futur.common.models.RequestGlobal;
import com.abln.futur.common.newview.DataView;
import com.abln.futur.common.newview.FinalDataSets;
import com.abln.futur.common.newview.ItemAdapter;
import com.abln.futur.common.postjobs.post;
import com.google.gson.JsonObject;
import com.jakewharton.rxbinding3.widget.RxTextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DatabaseUserSearch extends NewBaseActivity implements DatabaseHolder.EventHandler {


    @BindView(R.id.jobResult_recyclerviewNonJobSeeker)
    RecyclerView recyclerView_mySavedJobs;


    @BindView(R.id.sendJobInvite)
    TextView sendjobinvit;

    @BindView(R.id.searchJobsRole)
    EditText searchjob;


  private   DatabaseHolder    itemAdapter;

    @Override
    public int getLayoutId() {


        return R.layout.activity_non_job_seeker_search_result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView_mySavedJobs.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        getFullData("","","","","","");
        sendjobinvit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UIUtility.showToastMsg_withAlertInfoShort(getApplicationContext(),"Data fetched");

                for (SearchDatamodel mo: selecteddata
                     ) {


                    System.out.println("name"+mo.first_name);
                }


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



    }

    //TODO handle data with the interface and work on it .



// "12.91582330" , "77.64532000" , "10"  , apikey= "c7ca9a4d17330ae7b2f680d95b192dd4"
// ,industry BANKING;
    private void getFullData(String v1, String v2, String v3, String v4, String v5, String v6) {



        RequestGlobal fulldatavalues = new RequestGlobal();
        fulldatavalues.lat = "12.91582330";
        fulldatavalues.lng = "77.64532000";
        fulldatavalues.apikey = "c7ca9a4d17330ae7b2f680d95b192dd4";
        fulldatavalues.industry = "EDUCATION";
        fulldatavalues.radius = "10";


        compositeDisposable.add(apiService.getusersearch(fulldatavalues)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<AppliedData>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {




                        if (baseResponse.statuscode == 1) {

                            UIUtility.showToastMsg_withSuccessShort(getApplicationContext(), "Successfully loaded ");
                            AppliedData userData = (AppliedData) baseResponse.data;
                            ArrayList<SearchDatamodel> jc = userData.results;
                                itemAdapter = new DatabaseHolder(DatabaseUserSearch.this, jc,DatabaseUserSearch.this);
                            recyclerView_mySavedJobs.setAdapter(itemAdapter);



                        } else if (baseResponse.statuscode == 0) {


                            AppliedData userData = (AppliedData) baseResponse.data;
                            ArrayList<SearchDatamodel> jc = userData.results;
                            UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Sorry no data found !");
                        }


                    }

                    @Override
                    public void onFailure(Throwable e) {


                        // UIUtility.showToastMsg_withErrorShort(getApplicationContext(), "Network Error ");

                    }
                }));


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

        System.out.println("selected"+item.first_name);

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
        System.out.println(" REmoved selected"+item.first_name);

        selecteddata.remove(item);
        if (selecteddata.size() > 0){

            sendjobinvit.setVisibility(View.VISIBLE);

        }else{

            sendjobinvit.setVisibility(View.INVISIBLE);
        }


    }







}
