package com.abln.futur.module.global.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.abln.futur.R;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.module.chats.adapter.GetStoriesRequest;
import com.abln.futur.services.NetworkOperationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

import cdflynn.android.library.checkview.CheckView;
import es.dmoral.toasty.Toasty;

public class SendJobInvite extends Activity {

    public static String postApprovedVisitorEntryRequest(String payload) {
        StringBuffer jsonString = new StringBuffer();
        try {
            Log.d("Dgddfdf", payload + " ");
//        http://api.oye247.com/oye247/api/v1/OYENonRegularVisitorLog/GetOYENonRegularVisitorLogList/12/2018-05-09
            URL url1 = new URL(NetworkConfig.SendJobInvite);

            HttpURLConnection uc = (HttpURLConnection) url1.openConnection();
            String line;

            uc.setRequestProperty("X-OYE247-APIKey", "7470AD35-D51C-42AC-BC21-F45685805BBE");
            uc.setRequestProperty("Content-Type", "application/json");
            uc.setRequestMethod("POST");
            uc.setDoInput(true);
            uc.setInstanceFollowRedirects(false);
            uc.connect();
            OutputStreamWriter writer = new OutputStreamWriter(uc.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(payload);
            writer.close();
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            uc.disconnect();
        } catch (Exception e) {

        }
        return jsonString.toString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_job_invite);

        SendJobInvite.this.setFinishOnTouchOutside(false);

        CheckView mcheckView = findViewById(R.id.check);

        Button sendInvite = findViewById(R.id.sendInvite);
        sendInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                sendJobInvite(mcheckView);
                sendJobInviteAsyncTask();
            }
        });


    }

    private void sendJobInvite(CheckView bar) {
        //  bar.check();
        FuturProgressDialog.show(getApplicationContext(), false);
        GetStoriesRequest mFavoriteRquestBody = new GetStoriesRequest();
        mFavoriteRquestBody.setUserID("8");
        mFavoriteRquestBody.setLan("12.3435345");
        mFavoriteRquestBody.setLat("15.342351");

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(getApplicationContext(), NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.SendJobInvite);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        getApplicationContext().startService(intent);
        Toasty.success(getApplicationContext(), "Job Invite sent !", Toast.LENGTH_SHORT, true).show();
        finish();
    }

    public void sendJobInviteAsyncTask() {

        new AsyncTask<String, String, String>() {

            @Override
            protected String doInBackground(String... params) {
                String response = null;


                response = postApprovedVisitorEntryRequest("{\"VLEntryT\":\"" + "qq" + "\"," +
                        "\"VLVisLgID\":" + 12 + "," +
                        "\"VLEntyWID\":" + "11" +
                        " }");
                Log.d("Dgddfdf s", response + "");
                return response;

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toasty.success(getApplicationContext(), "Job Invite sent !", Toast.LENGTH_SHORT, true).show();
                finish();
                if (s != null) {
                    try {
                        Log.d("Dgddfdf", s + "");
                        JSONObject jsonObj = new JSONObject(s);
                        boolean successb = jsonObj.getBoolean("success");
                        try {
                            if (successb == true) {


                            } else {

                            }
                        } catch (Exception ex) {
                            Log.d("Dgddfdf r", "Json parsing error: " + ex.getMessage());
                        }
//                            Log.d("Dgddfdf",assnArry.length()+" ");
                    } catch (final JSONException e) {
                        Log.d("Dgddfdf 2", "Json parsing error: " + e.getMessage());
                    }
                } else {
                    Log.d("Dgddfdf 3", "Couldn't get json from server.");
                }

            }
        }.execute("");
    }
}
