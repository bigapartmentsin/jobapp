package com.abln.futur;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.abln.chat.core.base.IMUnSeenChatMsgCountListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.FLog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.interfaces.TaskCompleteListener;

public class MainActivity extends BaseActivity implements TaskCompleteListener, IMUnSeenChatMsgCountListener {

    private static final String TAG = "MainActivity";
    private Login login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login = new Login();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, login)

                .show(login).commit();
        //   getAllPatientListFromServer();
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        super.onTaskCompleted(context, intent);

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


    }


    @Override
    public void onChatMessageReceived(int totalUnSeenChatMsgCount, IMChatMessage latestChatMessage) {

        FLog.d(TAG, "MEssage received");
    }
}
