package com.abln.futur;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.abln.chat.IMInstance;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.Network.DataParser;
import com.abln.chat.ui.activities.IMChatThreadListActivity;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.UIUtility;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.datamodel.GetUserResponse;
import com.abln.futur.fragments.BaseFragment;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.services.NetworkOperationService;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class Login extends BaseFragment implements TaskCompleteListener {


    private static final String TAG = "LoginFragment";
    private Context mContext;
    private NetworkOperationService mService;
    private Button chatBtn;

    public Login() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        mService = new NetworkOperationService();
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTaskCompleteListener(mContext, this);
        chatBtn = view.findViewById(R.id.chat);

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(view.getContext(), IMChatThreadListActivity.class);
//                chatIntent.putExtra("Context_Id","1001");
//                chatIntent.putExtra("Context_Name","Developer");
                startActivity(chatIntent);
            }
        });
        //getAllPatientListFromServer();
    }

    private void getAllPatientListFromServer() {

        FuturProgressDialog.show(mContext, false);
        GetUserRequest mFavoriteRquestBody = new GetUserRequest();
        mFavoriteRquestBody.setDocId("ed3202810f1407e97b53fb759d631c29");
        mFavoriteRquestBody.setCount(2);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getAllPatient);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mContext.startService(intent);
    }

    @Override
    public void onTaskCompleted(Context context, Intent intent) {

        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);

        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.getAllPatient)) {
            FuturProgressDialog.dismissDialog();
            GetUserResponse getAllPatientResponse = DataParser.parseJson(responseString, GetUserResponse.class);
            if (getAllPatientResponse.getStatuscode() == 0) {
                UIUtility.showToastMsg_short(mContext, getAllPatientResponse.getStatusMessage());
                return;
            } else if (getAllPatientResponse.getStatuscode() == 1) {

                if (getAllPatientResponse.getData().getPatientList().size() == 0) {
                    UIUtility.showToastMsg_short(mContext, "No data found");
                    return;
                }
                String firstName = null;
                ArrayList<IMChatUser> chatUsers = new ArrayList<>();
                for (int i = 0; i < getAllPatientResponse.getData().getPatientList().size(); i++) {
                    firstName = getAllPatientResponse.getData().getPatientList().get(i).getName();

                    IMChatUser chatUser = new IMChatUser();
                    if (null != firstName) {
                        chatUser.userId = getAllPatientResponse.getData().getPatientList().get(i).getPatientId() + "";
                        chatUser.userName = firstName.trim();
                        chatUsers.add(chatUser);
                    }

                    //   mySGList.add(getAllPatientResponse.getData().getPatientList().get(i));
                }

                AppConfig.saveChatInfo(chatUsers, (BaseActivity) getActivity());
                IMInstance.getInstance().setUnSeenChatMsgCountListener((MainActivity) getActivity());
                IMInstance.getInstance().updateLoggedInuserStatus(true);
                //IMInstance.getInstance().updateFreshLogin(true);
                FLog.d(TAG, String.valueOf(IMInstance.getInstance().getTotalUnSeenChatMsgCount()));


            }


        }

    }
}
