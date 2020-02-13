package com.abln.futur.module.chats.adapter;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
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
import com.abln.futur.R;
import com.abln.futur.activites.BaseActivity;
import com.abln.futur.common.AppConfig;
import com.abln.futur.common.FLog;
import com.abln.futur.common.FuturProgressDialog;
import com.abln.futur.common.NetworkConfig;
import com.abln.futur.common.PrefManager;
import com.abln.futur.common.UIUtility;
import com.abln.futur.datamodel.GetUserRequest;
import com.abln.futur.fragments.BaseFragment;
import com.abln.futur.interfaces.TaskCompleteListener;
import com.abln.futur.services.NetworkOperationService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends BaseFragment implements TaskCompleteListener, StoriesAdapter.FolderClickListener, IMUnSeenChatMsgCountListener {


    private static final String TAG = "ChatsFragment";
    @BindView(R.id.recycler_stories)
    RecyclerView recyclerViewStories;
    @BindView(R.id.chatLauncher)
    ImageView chatBtn;
    @BindView(R.id.containerChat)
    FrameLayout chatcontainer;
    private Context mContext;
    private NetworkOperationService mService;
    private StoriesAdapter mAdapter;
    private ChatsViewFragment chatsViewFragment;
    private List<GetAll_UserResponse.PatientList> mySGList = new ArrayList<>();

    private PrefManager prefManager = new PrefManager();
    private String[] resources = new String[]{
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00001.jpg?alt=media&token=460667e4-e084-4dc5-b873-eefa028cec32",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00002.jpg?alt=media&token=e8e86192-eb5d-4e99-b1a8-f00debcdc016",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00004.jpg?alt=media&token=af71cbf5-4be3-4f8a-8a2b-2994bce38377",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00005.jpg?alt=media&token=7d179938-c419-44f4-b965-1993858d6e71",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00006.jpg?alt=media&token=cdd14cf5-6ed0-4fb7-95f5-74618528a48b",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00007.jpg?alt=media&token=98524820-6d7c-4fb4-89b1-65301e1d6053",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00008.jpg?alt=media&token=7ef9ed49-3221-4d49-8fb4-2c79e5dab333",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00009.jpg?alt=media&token=00d56a11-7a92-4998-a05a-e1dd77b02fe4",
            "https://firebasestorage.googleapis.com/v0/b/firebase-satya.appspot.com/o/images%2Fi00010.jpg?alt=media&token=24f8f091-acb9-432a-ae0f-7e6227d18803",
    };

    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chats, container, false);
    }


    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
        mContext = getActivity();
        mService = new NetworkOperationService();
        setTaskCompleteListener(mContext, this);


        mAdapter = new StoriesAdapter(mContext, mySGList, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerViewStories.setLayoutManager(mLayoutManager);
        recyclerViewStories.setItemAnimator(new DefaultItemAnimator());
        recyclerViewStories.setAdapter(mAdapter);

        addRecyclerVIew();

        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chatIntent = new Intent(view.getContext(), IMChatThreadListActivity.class);
                chatIntent.putExtra("Context_Id", "1001");
                startActivity(chatIntent);
            }
        });

        //TODO crash is happing no view found for id containerchat for fragment  chatviewfragment; from getalluserlistfromserver ;
        getAllUserListFromServer();






    }


    private void getAllUserListFromServer() {
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

    private void addRecyclerVIew() {


        for (int i = 0; i < resources.length; i++) {
            GetAll_UserResponse.PatientList response = new GetAll_UserResponse.PatientList();
            response.setPatientId("1");
            response.setPhoto(resources[i]);
            mySGList.add(i, response);
        }
        mAdapter.notifyDataSetChanged();


    }



    @Override
    public void onTaskCompleted(Context context, Intent intent) {
        String requestType = intent.getStringExtra(NetworkConfig.REQUEST_TYPE);
        String apiUrl = intent.getStringExtra(NetworkConfig.API_URL);
        String responseString = intent.getStringExtra(NetworkConfig.RESPONSE_BODY);


        if (responseString != null && apiUrl.equalsIgnoreCase(NetworkConfig.getAllUser)) {
            FuturProgressDialog.dismissDialog();
            GetAllUsersResponse getAllUsersResponse = DataParser.parseJson(responseString, GetAllUsersResponse.class);
            if (getAllUsersResponse.getStatuscode() == 0) {
                UIUtility.showToastMsg_short(mContext, getAllUsersResponse.getStatusMessage());
                return;
            } else if (getAllUsersResponse.getStatuscode() == 1) {

                if (getAllUsersResponse.getData().getUserList().size() == 0) {
                    UIUtility.showToastMsg_short(mContext, "No data found");
                    return;
                }



                String firstName = null;


                ArrayList<IMChatUser> chatUsers = new ArrayList<>();
                for (int i = 0; i < getAllUsersResponse.getData().getUserList().size(); i++) {
                    firstName = getAllUsersResponse.getData().getUserList().get(i).getFirstName();

                    IMChatUser chatUser = new IMChatUser();
                    if (null != firstName) {

                        System.out.println("Getting user id ");

                        chatUser.userId = getAllUsersResponse.getData().getUserList().get(i).getApikey() + "";
                        chatUser.userName = firstName.trim();
                        chatUsers.add(chatUser);
                    }

                    //   mySGList.add(getAllPatientResponse.getData().getPatientList().get(i));
                }

                AppConfig.saveChatInfo(chatUsers, (BaseActivity) getActivity());


//                IMInstance.getInstance().setUnSeenChatMsgCountListener((BaseFragment) getActivity());
                IMInstance.getInstance().updateLoggedInuserStatus(true);
                FLog.d(TAG, String.valueOf(IMInstance.getInstance().getTotalUnSeenChatMsgCount()));


                System.out.println("DataSetchanges happing");

                chatsViewFragment = new ChatsViewFragment();
                getParentFragmentManager().beginTransaction()
                        .add(R.id.containerChat, chatsViewFragment)
                        .show(chatsViewFragment).commitAllowingStateLoss();



            }


        }
    }

    @Override
    public void onScrollEnd(int lastPosition) {



        getView().jumpDrawablesToCurrentState();

        try {
            getAllUserListFromServer();
        } catch (Exception e) {
            e.printStackTrace();
        }


        //Toast todo activity  //


    }

    @Override
    public void onPatientClicked(int position) {
        FLog.d("Position", "" + position);
        launchStories(resources.length - position - 1);

    }

    private void launchStories(int pos) {

//
//        //launch with presettings
//        Intent a = new Intent(mContext, StatusStoriesActivity.class);
//        a.putExtra(StatusStoriesActivity.STATUS_RESOURCES_KEY, resources);
//        a.putExtra(StatusStoriesActivity.STATUS_DURATION_KEY, 300L);
//        a.putExtra(StatusStoriesActivity.IS_IMMERSIVE_KEY, true);
//        a.putExtra(StatusStoriesActivity.IS_CACHING_ENABLED_KEY, true);
//        a.putExtra(StatusStoriesActivity.IS_TEXT_PROGRESS_ENABLED_KEY, false);
//      //  a.putExtra(StatusStoriesActivity.START_POSITION,pos);


        //  startActivity(a);
    }

    @Override
    public void onChatMessageReceived(int totalUnSeenChatMsgCount, IMChatMessage latestChatMessage) {



    }

}
