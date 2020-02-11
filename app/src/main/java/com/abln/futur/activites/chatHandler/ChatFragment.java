package com.abln.futur.activites.chatHandler;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abln.chat.ChatsViewFragment;
import com.abln.chat.IMInstance;
import com.abln.chat.core.base.IMUnSeenChatMsgCountListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.Network.DataParser;
import com.abln.chat.ui.activities.IMChatThreadListActivity;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.ConstantUtils;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.fragments.BaseFragment;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.module.chats.adapter.GetAllUsersResponse;
import com.abln.futur.module.chats.adapter.GetAll_UserResponse;
import com.abln.futur.module.chats.adapter.GetStoriesRequest;
import com.abln.futur.module.chats.adapter.StoriesAdapter;
import com.abln.futur.services.NetworkOperationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements TaskCompleteListener, StoriesAdapter.FolderClickListener, IMUnSeenChatMsgCountListener {


    private static final String TAG = "ChatFragment";
    private Context mContext;
    private NetworkOperationService mService;





    private PrefManager prefManager = new PrefManager();



    @BindView(R.id.chatLauncher)
    ImageView chatBtn;

    @BindView(R.id.containerChat)
    FrameLayout container;


    public ChatFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.handler_chat, container, false);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);
        mContext = getActivity();
        mService = new NetworkOperationService();
        setTaskCompleteListener(mContext,this);
    //  id =   getArguments().getSerializable(ConstantUtils.DATA);







        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ArrayList<String> userIdList = new ArrayList<String>();
                userIdList.add("37bcc17c227ce9f317dfa77fdb774374");
                ChatHelper.doLaunchNewChatThread(mContext, userIdList);

            }
        });



        getAllUserListFromServer();


    }





    private void getAllUserListFromServer() {

        FuturProgressDialog.show(mContext, false);
        GetUserRequest mFavoriteRquestBody = new GetUserRequest();
        mFavoriteRquestBody.setCount(1);

        HashMap<String, String> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json");

        Intent intent = new Intent(mContext, NetworkOperationService.class);
        intent.putExtra(NetworkConfig.API_URL, NetworkConfig.getAllUser);
        intent.putExtra(NetworkConfig.HEADER_MAP, headerMap);
        intent.putExtra(NetworkConfig.INPUT_BODY, mFavoriteRquestBody);
        mContext.startService(intent);
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
                UIUtility.showToastMsg_short(mContext, getAllUsersResponse.getStatusMessage());
                return;
            } else if( getAllUsersResponse.getStatuscode() == 1){

                if(getAllUsersResponse.getData().getUserList().size() == 0){
                    UIUtility.showToastMsg_short(mContext, "No data found");
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

                AppConfig.saveChatInfo(chatUsers, (BaseActivity) getActivity());
//                IMInstance.getInstance().setUnSeenChatMsgCountListener((BaseFragment) getActivity());
                IMInstance.getInstance().updateLoggedInuserStatus(true);
//                IMInstance.getInstance().updateFreshLogin(true);
                FLog.d(TAG, String.valueOf(IMInstance.getInstance().getTotalUnSeenChatMsgCount()));




//                chatsViewFragment = new ChatsViewFragment();
//                getFragmentManager().beginTransaction()
//                        .add(R.id.containerChat, chatsViewFragment)
//
//                        .show(chatsViewFragment).commit();



                chatBtn.performClick();

            }


        }
    }

    @Override
    public void onScrollEnd(int lastPosition) {

    }

    @Override
    public void onPatientClicked(int position) {
        FLog.d("Position",""+position);


    }



    @Override
    public void onChatMessageReceived(int totalUnSeenChatMsgCount, IMChatMessage latestChatMessage) {

    }

}
