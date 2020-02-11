package com.abln.futur.activites.chatHandler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.abln.chat.ChatsViewFragment;
import com.abln.chat.IMInstance;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.Network.DataParser;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.ConstantUtils;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.UIUtility;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.chats.adapter.ChatsFragment;
import com.abln.futur.module.chats.adapter.GetAllUsersResponse;
import com.abln.futur.services.NetworkOperationService;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;

public class ChatHandlerActivity  extends BaseActivity implements TaskCompleteListener {

    private NetworkOperationService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        setContentView(R.layout.activity_chat_handler);
        mService = new NetworkOperationService();
        setTaskCompleteListener(this);


//        Fragment fragment = null;
//        fragment = new ChatFragment();
// Bundle bundle = new Bundle();
//        bundle.putSerializable(ConstantUtils.DATA, getIntent().getSerializableExtra(ConstantUtils.DATA));
//        selectPatientTimeSlot.setArguments(bundle);
//        replaceFragment(R.id.fragment_container, fragment, false);



//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        ChatFragment chatFragment = new ChatFragment();
//        Bundle bundle = new Bundle();
//        bundle.putSerializable(ConstantUtils.DATA, getIntent().getSerializableExtra(ConstantUtils.DATA));
//        chatFragment.setArguments(bundle);
//        fragmentTransaction.add(R.id.fragment_container,chatFragment);
//        fragmentTransaction.commitAllowingStateLoss();
//        Log.d("CHATHANDLER","SAVEDSTATE");

        getAllUserListFromServer();
    }



    @Override
    protected void onResume() {
        super.onResume();

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




//                chatsViewFragment = new ChatsViewFragment();
//                getFragmentManager().beginTransaction()
//                        .add(R.id.containerChat, chatsViewFragment)
//
//                        .show(chatsViewFragment).commit();







            }


        }
    }
}
