package com.abln.chat.ui.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.abln.chat.core.model.IMChatHistoryResponse;
import com.abln.chat.pubnub.IMPubNubHelper;
import com.abln.chat.pubnub.IMPubNubUtils;
import com.abln.chat.ui.adapters.JobDataAdapter;
import com.abln.chat.utils.AccountOne;
import com.abln.chat.utils.BaseResponse;
import com.abln.chat.utils.BaseView;
import com.abln.chat.utils.GlobalSingleCallback;
import com.abln.chat.utils.ModChat;
import com.abln.chat.utils.Models;
import com.abln.chat.utils.PdfViewer;
import com.abln.chat.utils.Service;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.base.IMChattingListener;
import com.abln.chat.core.base.IMUserPresenceListener;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.Network.DataParser;
import com.abln.chat.ui.Network.IMMessageListResponse;
import com.abln.chat.ui.Network.IMNetworkResponse;
import com.abln.chat.ui.adapters.IMChatMessageAdapter;
import com.abln.chat.ui.gcm.IMChatMultiMediaUploadDownloadService;
import com.abln.chat.ui.gcm.IMPushNotificationHelper;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.ui.helper.ChatMultiMediaHelper;
import com.abln.chat.utils.IMBottomReachedListener;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMNetwork;
import com.abln.chat.utils.IMUIUtils;
import com.abln.chat.utils.IMUtils;
import com.abln.chat.utils.TourGuideHelper;
import com.pubnub.api.models.consumer.history.PNHistoryItemResult;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.JsonObject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;


public class IMChatActivity extends IMBaseActivity implements IMNetworkResponse, IMBottomReachedListener, BaseView ,JobDataAdapter.clickHandler{
    public static final String TAG = IMChatActivity.class.getSimpleName();


    // new data are addded out here for the networking purpose ;

    protected CompositeDisposable compositeDisposable;
    protected com.abln.chat.utils.Handler apiService;


    private IMChatMessageAdapter mIMChatMessageAdapter;
    private RecyclerView mChatRecyclerView;

    private AutoCompleteTextView acTvMessage;
    private TextView mtvDisableBandMsg, mtvTypingStatus;

    private ImageView mUploadBtn, mChatMsgCopy;
    private ProgressBar mProgressBar;
    private FloatingActionButton mFlDownChat;
    private static ImageView mSendMsgBtn;

    private NetworkChangeReceiver mNetworkChangeReceiver;

    private IMChatUser mLoggedInUser = new IMChatUser();
    public IMChatRoomDetail mIMChatRoomDetail = new IMChatRoomDetail();
    private ArrayList<IMChatUser> mSelectedChatUsers = new ArrayList<>();
    private boolean isExistingChatThread = false;
    private boolean isForwardedChatThread = false;
    private boolean isMsgHasBeenPublishedForNewChatRoom = true;
    private String mCameraImageOrVideoMsgId = "";
    private Uri mCameraImageUri = null;
    private Uri mCameraVideoUri = null;

    // Toolbar
    private Toolbar mToolbar;
    private ImageView mToolbarMsgInfoIcon, mToolbarMsgForwardIcon, mToolbarUserProfileImg, mToolbarUserOnlineImg;
    private ImageView mAddUsers;
    private TextView mtvToolbarTitle, mtvToolbarSubTitle, mToolbarUserProfileTextImg, mtvContextName;
    private LinearLayout mToolbarForwardChat;
    private CardView mChatBottomBar;
    private LinearLayout mToolbarAddUser;
    private String mContextName;
    private String mContextId;
    private String mChatMsgId;
    private IMNetworkResponse mNetworkResponse = null;
    private IMBottomReachedListener mBottomReachedListener = null;

    private RelativeLayout datahandler;

    private CardView cvAttachmentOptions;
    private TextView tvOpenGallery, tvTakePhoto, tvTakeVideo, tvTakeLocation;
    private boolean mHistoryCall = false;
    private int MAP_REQUEST_CODE = 400;

    public static String latitude = " ";
    public static String longitude = " ";
    public static String latitude_selected = " ";
    public static String longitude_selected = " ";

    public String mreciver;
    public String msender;

    public String prekey;
    public String enduser;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.im_activity_chat);
        apiService = Service.getApiService();
        compositeDisposable = new CompositeDisposable();

        setLoggedInUser();
        setBundleData();
        initViews();
        setUpToolBar();
        setUpChatWindowControls();
        setChatThreadStatus();
        setUpChatWindowData();

        datahandler.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {



            //    Toast.makeText(getApplicationContext(),"invoke",Toast.LENGTH_LONG).show();
                getDilog();


                //check for the first data  :

                //if()

                // else

                // else if ()
                // handler data model
                // show pop up

                //Toast.makeText(getApplicationContext(),"getting the information",Toast.LENGTH_LONG).show();



            }
        });




//        showTourGuide();
    }




    // TODO : hadling the call of dataviews data has to pass from the sharedpreference ;

JobDataAdapter inboxitem;
    private void getTotalNumberofJobPost(){

        AccountOne getUsers = new AccountOne();


        getUsers.userOneKey = "";
        getUsers.userTwoKey = "";


       // getUsers.reciever = "ad99eda18886557d6e5e61d6f30638a4";
        //getUsers.sender = "c7ca9a4d17330ae7b2f680d95b192dd4";




        getUsers.reciever = prekey;
        getUsers.sender = enduser;



        compositeDisposable.add(apiService.getdata("v1/chat-post-popup",getUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new GlobalSingleCallback<BaseResponse<ModChat>>(true, this) {
                    @Override
                    public void onApiSuccess(BaseResponse baseResponse) {

                        if (baseResponse.statuscode == 1) {




                         ModChat   data_list =  (ModChat)   baseResponse.data;
                         ArrayList<Models>  final_list  =  data_list.post_list;
                         inboxitem = new JobDataAdapter(IMChatActivity.this, final_list,IMChatActivity.this);
                            recyclerView.setAdapter(inboxitem);
                            bts.show();




                        } else if (baseResponse.statuscode == 0) {


                            System.out.print("Data is corrupted ");





                        } else if (baseResponse.statuscode == 2){


                            ModChat   data_list =  (ModChat)   baseResponse.data;


                         ArrayList<Models> models =    data_list.post_list;


                         for (Models a : models){

                            System.out.println("File"+a.file);
                            System.out.println("File Name "+a.filename);
                            downloadForm(a.file,a.filename);

                         }

                        // Models


                           // downloadForm(file,fileName);


                        }




                    }

                    @Override
                    public void onFailure(Throwable e) {

                    }
                }));



    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        IMInstance.getInstance().setIMChattingListener(mIMChattingListener, mIMChatRoomDetail.roomId);
        IMInstance.getInstance().setUserPresenceListener(mIMUserPresenceListener);
        IMInstance.getInstance().fetchUserStateOnPresenceChannel();

        IMUtils.registerBroadCast(IMChatActivity.this, mNetworkChangeReceiver, IMConstants.NETWORK_CHANGE_BROADCAST_ACTION);
        IMUtils.registerBroadCast(IMChatActivity.this, mMultiMediaTransferIdBroadcastReceiver,
                IMChatMultiMediaUploadDownloadService.MULTIMEDIA_TRANSFER_ID_BROADCAST_ACTION);
        IMUtils.registerBroadCast(IMChatActivity.this, mMultiMediaUploadDownloadProgressBroadcastReceiver,
                IMChatMultiMediaUploadDownloadService.MULTIMEDIA_UPLOAD_DOWNLOAD_PROGRESS_BROADCAST_ACTION);
        IMUtils.registerBroadCast(IMChatActivity.this, mMultiMediaUploadDownloadStateBroadcastReceiver,
                IMChatMultiMediaUploadDownloadService.MULTIMEDIA_UPLOAD_DOWNLOAD_STATE_BROADCAST_ACTION);

        if (mContextName != null && mContextName.length() > 0 && !(mContextName.equalsIgnoreCase("null"))) {
            mtvContextName.setVisibility(View.VISIBLE);
            mtvContextName.setText(mContextName);
        } else {
            mtvContextName.setVisibility(View.GONE);
        }

        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) mChatRecyclerView
                .getLayoutManager();
        final boolean[] loading = {false};

        final int visibleThreshold = 1;
        if (mIMChatMessageAdapter != null && mIMChatMessageAdapter.getItemCount() < 0)
            mFlDownChat.hide();


        final SwipeRefreshLayout pullToRefresh = (SwipeRefreshLayout) findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        mChatRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = linearLayoutManager.getItemCount();
                int lastVisibleItem = linearLayoutManager
                        .findLastVisibleItemPosition();
                int firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                long timeStamp = 0;
                if (totalItemCount - firstVisibleItem >= totalItemCount) {
                    ArrayList<IMChatMessage> chatMessageList = new ArrayList<>(IMInstance.getInstance().getIMDatabase()
                            .getIMChatMessageDao().getChatMessageForRoomIdInSorted(mIMChatRoomDetail.roomId));
                    if (chatMessageList.size() <= 0)
                        timeStamp = 0;
                    else
                        timeStamp = chatMessageList.get(0).timeToken;
                    makeAzureFunctionCallForMessages(timeStamp, 5);
                }
                if (totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    // End has been reached
                    // Do something
                    mFlDownChat.hide();
                    loading[0] = true;
                } else {
                    if (mIMChatMessageAdapter != null && mIMChatMessageAdapter.getItemCount() > 1)
                        mFlDownChat.show();
                }

            }
        });
    }


    private void refreshPage() {
        long timeStamp = 0;
        ArrayList<IMChatMessage> chatMessageList = new ArrayList<>(IMInstance.getInstance().getIMDatabase()
                .getIMChatMessageDao().getChatMessageForRoomIdInSorted(mIMChatRoomDetail.roomId));


        System.out.println("Get info"+mIMChatRoomDetail.roomId);
        if (chatMessageList.size() <= 0)
            timeStamp = 0;
        else
            timeStamp = chatMessageList.get(0).timeToken;
        makeAzureFunctionCallForMessages(timeStamp, 5);

    }

    @Override
    protected void onStop() {
        super.onStop();
        IMInstance.getInstance().updateCurrentRoomState(false);

        IMUtils.unregisterBroadCast(IMChatActivity.this, mNetworkChangeReceiver);
        IMUtils.unregisterBroadCast(IMChatActivity.this, mMultiMediaTransferIdBroadcastReceiver);
        IMUtils.unregisterBroadCast(IMChatActivity.this, mMultiMediaUploadDownloadProgressBroadcastReceiver);
        IMUtils.unregisterBroadCast(IMChatActivity.this, mMultiMediaUploadDownloadStateBroadcastReceiver);
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "The onReStart has been called");
        super.onRestart();
        IMInstance.getInstance().updateCurrentRoomState(true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        setLoggedInUser();
        setBundleData();
        setUpChatWindowControls();
        setChatThreadStatus();
        setUpChatWindowData();



    }

    private void setLoggedInUser() {
        // Setting Logged In IMChatUser
        mLoggedInUser = IMInstance.getInstance().getLoggedInUser();

        System.out.println("Loggedinuser"+mLoggedInUser.userId);

        prekey = mLoggedInUser.userId;

    }

    private void setBundleData() {
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {







            mIMChatRoomDetail.roomId = bundle.getString(IMConstants.CHAT_ROOM_ID, null);
            isForwardedChatThread = bundle.getBoolean(IMConstants.IS_CHAT_FORWARDED, false);
            mContextName = bundle.getString(IMConstants.CHAT_CONTEXT_NAME);
            mContextId = bundle.getString(IMConstants.CHAT_CONTEXT_ID);
            mChatMsgId = bundle.getString(IMConstants.CHAT_MSG_ID);
            System.out.println("DataSet-->"+mIMChatRoomDetail.roomId);


            String newinfo = mIMChatRoomDetail.adminId;

            System.out.println("AdminID"+newinfo);


            System.out.println("Selected user ");



            // TODO room id getting from history

            if (mIMChatRoomDetail.roomId != null){


                String newstring = mIMChatRoomDetail.roomId;
                String[] parts = newstring.split("-");
                System.out.println("new string "+parts[0]+"second"+parts[1]);

                mreciver =  parts[0];
                msender = parts[1];



                if (prekey.equals(mreciver)){

                    System.out.println("Check what is the input msender()"+msender);

                    enduser = msender;
                }

                if(prekey.equals(msender)){


                    System.out.println("Check what is the input mreciver "+mreciver);

                    enduser= mreciver;



                }

            }else {

                //TODO navigation from chat flow

                System.out.println("System id is comming empty navigation from the ");
            }









            System.out.println("----------");
            ArrayList<String> selectedUserIdList = bundle.getStringArrayList(IMConstants.ADD_USER_ID_LIST);

            System.out.println("null data : its comming from click on chat "+selectedUserIdList);

            if (selectedUserIdList != null){
                for (String s : selectedUserIdList){
                    enduser = s;
                }

               // enduser = Arrays.toString(selectedUserIdList);


            }

            if (null != selectedUserIdList) {

                System.out.println("null data -- : ");
                mSelectedChatUsers = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                        getChatUserListByUserIds(selectedUserIdList);


            }



        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {

        datahandler = (RelativeLayout) findViewById(R.id.datahandler);

        mNetworkChangeReceiver = new NetworkChangeReceiver();
        mChatRecyclerView = (RecyclerView) findViewById(R.id.im_rv_chat);

        mNetworkResponse = (IMNetworkResponse) this;
        mBottomReachedListener = (IMBottomReachedListener) this;

        mSendMsgBtn = (ImageView) findViewById(R.id.im_btn_send_response_chat);
        acTvMessage = (AutoCompleteTextView) findViewById(R.id.im_tv_auto_complete_chat);
        mtvDisableBandMsg = (TextView) findViewById(R.id.im_tv_disable_band_msg);
        mtvTypingStatus = (TextView) findViewById(R.id.im_tv_chat_typing_status);
        mUploadBtn = (ImageView) findViewById(R.id.im_btn_upload_chat);
        mProgressBar = (ProgressBar) findViewById(R.id.pbHeaderProgress);
        mFlDownChat = (FloatingActionButton) findViewById(R.id.im_fl_down_chat);


        // Action bar option menu items
        mtvToolbarTitle = (TextView) findViewById(R.id.im_tv_chat_toolbar_title);
        mtvToolbarSubTitle = (TextView) findViewById(R.id.im_tv_chat_toolbar_sub_title);
        mtvContextName = (TextView) findViewById(R.id.im_contextName);
        mToolbarMsgInfoIcon = (ImageView) findViewById(R.id.im_iv_toolbar_chat_info);
        mChatMsgCopy = (ImageView) findViewById(R.id.im_iv_chat_msg_copy);
        mToolbarMsgForwardIcon = (ImageView) findViewById(R.id.im_iv_toolbar_chat_forward);

        //TODO: has to be removed after chat forward implementation
        mToolbarMsgForwardIcon.setVisibility(View.GONE);
        mToolbarUserProfileImg = (ImageView) findViewById(R.id.im_iv_toolbar_users_profile_pic);
        mToolbarUserProfileTextImg = (TextView) findViewById(R.id.im_tv_toolbar_users_profile_pic);
        mToolbarUserOnlineImg = (ImageView) findViewById(R.id.im_iv_user_profile_online_bg);
        mAddUsers = (ImageView) findViewById(R.id.im_iv_add_participant);

        mToolbarAddUser = (LinearLayout) findViewById(R.id.im_ll_chat_window_add_user);
        mToolbarForwardChat = (LinearLayout) findViewById(R.id.im_rl_toolbar_forward_chat_options);
        mChatBottomBar = (CardView) findViewById(R.id.im_rl_chat_users_layout);

        mUploadBtn.setOnClickListener(mOnUploadBtnClickListener);
        mFlDownChat.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mIMChatMessageAdapter.scrolltoend();
            }
        });
        cvAttachmentOptions = (CardView) findViewById(R.id.cvAttachmentOptions);
        tvOpenGallery = (TextView) findViewById(R.id.tvOpenGallery);
        tvTakePhoto = (TextView) findViewById(R.id.tvTakePhoto);
        tvTakeVideo = (TextView) findViewById(R.id.tvTakeVideo);
        tvTakeLocation = (TextView) findViewById(R.id.tvTakeLocation);

        tvOpenGallery.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChatMultiMediaHelper.checkMediaPermission_storage(IMChatActivity.this)) {
                    cvAttachmentOptions.setVisibility(View.GONE);
                    ChatMultiMediaHelper.openGallery(IMChatActivity.this);
                }
            }
        });

        tvTakePhoto.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChatMultiMediaHelper.checkMediaPermission_camera(IMChatActivity.this)) {
                    cvAttachmentOptions.setVisibility(View.GONE);
                    ChatMultiMediaHelper.takePhoto(IMChatActivity.this, mCameraImageUri);
                }
            }
        });

        tvTakeVideo.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChatMultiMediaHelper.checkMediaPermission_camera(IMChatActivity.this)) {
                    cvAttachmentOptions.setVisibility(View.GONE);
                    ChatMultiMediaHelper.takeVideo(IMChatActivity.this, mCameraVideoUri);
                }
            }
        });

        tvTakeLocation.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChatMultiMediaHelper.checkLocationPermission(IMChatActivity.this)) {
                    cvAttachmentOptions.setVisibility(View.GONE);
                    Intent intent = new Intent(IMChatActivity.this, MapsActivity.class);
                    startActivityForResult(intent,MAP_REQUEST_CODE);

                }
            }
        });
    }

    private void setUpToolBar() {
        mToolbar = (Toolbar) findViewById(R.id.im_toolbar);
        mToolbar.setContentInsetStartWithNavigation(0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        showOrHideActionBarOption(false);
    }

    public void showOrHideActionBarOption(Boolean bool) {
        if (bool) {
            mChatMsgCopy.setVisibility(View.VISIBLE);
            mToolbarForwardChat.setVisibility(View.VISIBLE);
            mToolbarAddUser.setVisibility(View.GONE);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_black_50_alpha)));

        } else {
            mChatMsgCopy.setVisibility(View.GONE);
            mToolbarForwardChat.setVisibility(View.GONE);
            mToolbarAddUser.setVisibility(View.GONE);//TODO
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.im_toolbar_color)));
        }
    }

    public void toggleOptionMenuItems(boolean isVisible) {
        if (isVisible) {
            mToolbarMsgInfoIcon.setVisibility(View.VISIBLE);
        } else {
            mToolbarMsgInfoIcon.setVisibility(View.GONE);
        }
    }

    private void setUpChatWindowControls() {
        acTvMessage.addTextChangedListener(mOnTextChangeListener);
        mSendMsgBtn.setOnClickListener(mOnSendMsgClickListener);
        mAddUsers.setOnClickListener(mOnAddUsersClickListener);
        mToolbarMsgInfoIcon.setOnClickListener(mOnToolBarIconClickListener);
        mChatMsgCopy.setOnClickListener(mOnChatMessageCopyClickListener);
        mToolbarMsgForwardIcon.setOnClickListener(mOnToolBarIconClickListener);
        mChatRecyclerView.setVisibility(View.VISIBLE);
        setChatListAdapter();
    }

    private void setChatListAdapter() {
        mIMChatMessageAdapter = new IMChatMessageAdapter(this, mChatRecyclerView, mLoggedInUser, new ArrayList<IMChatMessage>(), mChatMsgId, mBottomReachedListener);
        mChatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mChatRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mChatRecyclerView.setAdapter(mIMChatMessageAdapter);
    }

    private void setChatThreadStatus() {
        if (null == mIMChatRoomDetail.roomId) {
            String oneToOneChatRoomId = null;
            if (mSelectedChatUsers.size() == 1) {
                if (mContextId != null && mContextName != null && mContextId.length() > 0 && mContextName.length() > 0) {
                    oneToOneChatRoomId = ChatHelper.getRoomIdForContextualOneToOneChatThread(mLoggedInUser.userId, mSelectedChatUsers.get(0).userId, mContextId);
                } else {
                    oneToOneChatRoomId = ChatHelper.getRoomIdForOneToOneChatThread(mLoggedInUser.userId, mSelectedChatUsers.get(0).userId);
                }
                if (IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().isChatRoomExist(oneToOneChatRoomId)) {
                    mIMChatRoomDetail.roomId = oneToOneChatRoomId;
                    isExistingChatThread = true;
                }
            }

        } else {
            isExistingChatThread = true;
        }
    }

    @Override
    public void processFinish(String output) {
        syncRoomListWithAzure(output);
    }

    @Override
    public void onProcessFinish(List<PNHistoryItemResult> results) {
        syncRoomListWithAzure(results);
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("messageList.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    private void syncRoomListWithAzure(String chatRoomDetail) {

        try {
            JsonObject object = new JsonObject();
            object.getAsJsonObject(chatRoomDetail);
            JsonObject parseObject = IMPubNubUtils.getMessageDataFromPNGCM(object);


            IMChatHistoryResponse imChatHistoryResponse = DataParser.parseJson(chatRoomDetail, IMChatHistoryResponse.class);
            imChatHistoryResponse.getEntry().getPnApns().getAps().getMessage();
            IMMessageListResponse imMessageListResponse = DataParser.parseJson(chatRoomDetail, IMMessageListResponse.class);
            for (int i = 0; i < parseObject.size(); i++) {
                IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().insertChatMessage(imMessageListResponse.getMessageList().get(i));
            }
            mtvToolbarSubTitle.setVisibility(View.GONE);
            mtvToolbarSubTitle.setText("");
            mProgressBar.setVisibility(View.GONE);
            mHistoryCall = true;
            setChatRoomHistory();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void syncRoomListWithAzure(List<PNHistoryItemResult> chatRoomDetail) {
        try {
            for (int i = 0; i < chatRoomDetail.size(); i++) {
                    IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().insertChatMessage(DataParser.parseJson(IMPubNubUtils.getMessageDataFromPNGCM(chatRoomDetail.get(i).getEntry().getAsJsonObject()).toString(),IMMessageListResponse.class));
            }
            setChatRoomHistory();
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            mtvToolbarSubTitle.setVisibility(View.GONE);
            mtvToolbarSubTitle.setText("");
            mProgressBar.setVisibility(View.GONE);
            mHistoryCall = true;
        }
    }

    private void setUpChatWindowData() {
        if (isForwardedChatThread) {
            setUpForwardedChatRoom();

        } else if (isExistingChatThread) {
            setUpExistingChatRoom();

        } else {
            setUpNewChatRoom();
        }

        // Setting up the chat thread info listener after setting up the complete chat window data
        mToolbar.setOnClickListener(mOnToolBarClickListener);
    }


    private void setUpForwardedChatRoom() {
        setUpNewChatRoom();
        String message = getIntent().getExtras().getString(IMConstants.CHAT_FORWARDED_MESSAGE);
        final IMChatMessage chatMessage = postMessageOnWindow(message, null);

        // publishing the forward message with some delay, delay has been given for setting up the new chat room
        isMsgHasBeenPublishedForNewChatRoom = false;
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                publishMessage(chatMessage);
                isMsgHasBeenPublishedForNewChatRoom = true;
            }
        }, 2000);
    }

    private void setUpNewChatRoom() {

        //ERROR TODO  ERROR in creating ;
        createChatRoomInfo();
        setActionBarUserImage();
        setChatRoomNameAsSubtitle();
        IMInstance.getInstance().createChatRoom(mIMChatRoomDetail);
        IMInstance.getInstance().fetchUserStateForParticularChatRoom(mIMChatRoomDetail.roomId);
    }

    private void setUpExistingChatRoom() {
        mIMChatRoomDetail = IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao()
                .getChatRoomDetail(mIMChatRoomDetail.roomId);
        mIMChatMessageAdapter.setChatRoomDetail(mIMChatRoomDetail);
        setExistingChatRoomInitial();
        setActionBarUserImage();
        setChatRoomNameAsSubtitle();
        setChatRoomPermissions();
        // makeAzureFunctionCallForRooms();
        int unreadCount = 0;
        for (int i = 0; i < mIMChatRoomDetail.roomMembers.size(); i++) {
            if (mIMChatRoomDetail.roomMembers.get(i).getUserId().equalsIgnoreCase(mLoggedInUser.userId) &&
                    mIMChatRoomDetail.roomMembers.get(i).getTotalCount() != null &&
                    mIMChatRoomDetail.roomMembers.get(i).getTotalCount() != null) {
                unreadCount = mIMChatRoomDetail.roomMembers.get(i).getTotalCount() - mIMChatRoomDetail.roomMembers.get(i).getReadCount();
            }
        }

        if (IMUtils.getChannelsSyncTable() != null && (IMUtils.getChannelsSyncTable().get(mIMChatRoomDetail.roomId) != null)
                && !IMUtils.getChannelsSyncTable().get(mIMChatRoomDetail.roomId) && unreadCount > 0) {
            IMUtils.updateSyncedChannels(mIMChatRoomDetail.roomId, true);
            makeAzureFunctionCallForMessages(0, 0);
        } else {
            setChatRoomHistory();
        }
        ChatHelper.publishPendingDeliveredAndReadStatusOfMessages(mIMChatMessageAdapter.getAllChatMessages(), mLoggedInUser.userId);
        IMInstance.getInstance().fetchUserStateForParticularChatRoom(mIMChatRoomDetail.roomId);
    }

    private void setExistingChatRoomInitial() {
        // Make all notification as read when existing room gets open
        IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().
                updateChatMessageSeenStatusByRoomId(mIMChatRoomDetail.roomId, true);

        // if user has deleted any one to one chat room and try to post any message by selecting the same user from contact list,
        // this thread will not show in chat history because the status is deleted, so why updating the deleted status,
        // for one to one chat room if it has been deleted before from chat history
        if (mIMChatRoomDetail.roomType.equals(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().
                    updateChatRoomDeleteStatus(mIMChatRoomDetail.roomId, false);
        }

        acTvMessage.setText(mIMChatRoomDetail.lastTypedText);

        // Removing all chat push notification if exist when any room gets open
        IMPushNotificationHelper.cancelNotification(this);
    }

    private void setActionBarUserImage() {
        if (null != mIMChatRoomDetail.roomType && mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            ChatHelper.setUserImage(ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId,
                    mIMChatRoomDetail), mToolbarUserProfileImg, mToolbarUserProfileTextImg);

        } else {
            mToolbarUserProfileImg.setImageResource(R.drawable.im_ic_default_chat_group);
        }
    }

    private void setOnlineUserStatus(String userId, boolean isOnline) {
        if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)
                && ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId, mIMChatRoomDetail).userId.equals(userId)) {
            if (isOnline) {
                mToolbarUserOnlineImg.setVisibility(View.VISIBLE);
            } else {
                mToolbarUserOnlineImg.setVisibility(View.GONE);
            }
        }
    }

    private void setChatRoomNameAsSubtitle() {
        if (null == getSupportActionBar()) {
            return;

        } else if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
            if (!IMUtils.isNullOrEmpty(mIMChatRoomDetail.roomName)) {
                mtvToolbarTitle.setText(mIMChatRoomDetail.roomName);
                mtvToolbarSubTitle.setVisibility(View.VISIBLE);
                mtvToolbarSubTitle.setText(ChatHelper.getChatRoomMembersNameString(mIMChatRoomDetail));

            } else {
                mtvToolbarTitle.setText(ChatHelper.getChatRoomName(mLoggedInUser, mIMChatRoomDetail));
                mtvToolbarSubTitle.setVisibility(View.GONE);
            }

        } else if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            mtvToolbarTitle.setText(ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId, mIMChatRoomDetail).userName);

            mtvToolbarSubTitle.setVisibility(View.GONE);
        }
    }

    private void setChatRoomPermissions() {
        if (!mIMChatRoomDetail.adminId.equals(mLoggedInUser.userId)
                && !mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            mAddUsers.setVisibility(View.GONE);
            acTvMessage.setAdapter(null);
            enableOrDisableTypingControl();
        }
    }

    private void enableOrDisableTypingControl() {
        boolean bool = false;
        for (IMChatRoomMember chatRoomMember : mIMChatRoomDetail.roomMembers) {
            if (chatRoomMember.userId.equals(mLoggedInUser.userId)) {
                bool = true;
                break;
            }
        }

        if (bool) {
            mChatBottomBar.setVisibility(View.VISIBLE);
            mtvDisableBandMsg.setVisibility(View.GONE);
        } else {
            mtvDisableBandMsg.setVisibility(View.VISIBLE);
            mChatBottomBar.setVisibility(View.GONE);
        }
    }


    private void makeAzureFunctionCallForMessages(long lastMsgTimeToken, int pageSize) {

        mtvToolbarSubTitle.setVisibility(View.VISIBLE);
        mtvToolbarSubTitle.setText("Syncing");
        long timeStamp;
        timeStamp = lastMsgTimeToken;
        IMPubNubHelper.getInstance().getChatHistory(mIMChatRoomDetail.roomId,timeStamp,50,mNetworkResponse);
        mToolbar.setSubtitle("Syncing");


    }

    private void setChatRoomHistory() {
        ArrayList<IMChatMessage> chatMessageList = new ArrayList<>(IMInstance.getInstance().getIMDatabase()
                .getIMChatMessageDao().getChatMessageForRoomIdInSorted(mIMChatRoomDetail.roomId));
        mIMChatMessageAdapter.addChatHistory(ChatHelper.getUserSpecificMessageHistory(chatMessageList, mLoggedInUser.userId), mHistoryCall);
        mHistoryCall = false;
    }

    // This method will be called if the logged in user is Admin and make changes for room members and name
    // Below set of lines should be called in order only
    private void publishUpdatedChatRoomDetail(ArrayList<IMChatUser> users, String chatRoomName) {
        IMChatRoomMember chatRoomMember = new IMChatRoomMember();
        chatRoomMember.userId = mLoggedInUser.userId;

        if (mIMChatRoomDetail.roomMembers.contains(chatRoomMember)) {
            if (mIMChatRoomDetail.adminId.equals(mLoggedInUser.userId)) {
                if (null == chatRoomName) {
                    chatRoomName = mIMChatRoomDetail.roomName;
                }
                publishChatRoomDetailUpdate(users, chatRoomName);

            } else {
                publishChatRoomNameUpdate(chatRoomName);
            }
        }
    }

    private void publishChatRoomDetailUpdate(ArrayList<IMChatUser> users, String chatRoomName) {
        ArrayList<IMChatRoomMember> updatedChatRoomMembers = ChatHelper.getChatRoomMemberList
                (users, mLoggedInUser);

        String userIdsToPublishInfo = ChatHelper.getChatRoomMemberIdsToPublishUpdatedChatRoomDetail
                (updatedChatRoomMembers, mIMChatRoomDetail.roomMembers, mLoggedInUser.userId);

        IMChatRoomDetail chatRoomDetail = mIMChatRoomDetail;
        chatRoomDetail.roomMembers = updatedChatRoomMembers;
        chatRoomDetail.roomName = chatRoomName;

        IMInstance.getInstance().publishChatRoomDetailUpdate(userIdsToPublishInfo, chatRoomDetail);
        IMInstance.getInstance().fetchUserStateForParticularChatRoom(mIMChatRoomDetail.roomId);
    }

    private void publishChatRoomNameUpdate(String chatRoomName) {
        IMChatRoomDetailUpdate chatRoomDetailUpdate = ChatHelper.getChatRoomDetailUpdateMessage
                (mLoggedInUser.userId, chatRoomName, mIMChatRoomDetail);
        IMInstance.getInstance().publishChatRoomNameUpdate(mIMChatRoomDetail.roomId,
                chatRoomDetailUpdate);
    }

    // This method will be called if the logged in user is Admin and receive updated chat room detail after publishing it
    // Below set of lines should be called in order only
    private void updatePublishedChatRoomDetail(IMChatRoomDetail chatRoomDetail) {
        mIMChatRoomDetail = chatRoomDetail;
        setChatRoomNameAsSubtitle();
    }

    // This method will be called if the logged in user is not Admin and receive any updated chat room detail
    // Below set of lines should be called in order only
    private void updateReceivedChatRoomDetail(IMChatRoomDetail chatRoomDetail) {
        mIMChatRoomDetail = chatRoomDetail;
        setChatRoomNameAsSubtitle();

        setChatRoomPermissions();
        IMInstance.getInstance().fetchUserStateForParticularChatRoom(mIMChatRoomDetail.roomId);
    }

    // This method will be called if user receive the chat room name updated by any of user expect Admin
    private void updateReceivedChatRoomName(String roomName) {
        mIMChatRoomDetail.roomName = roomName;
        setChatRoomNameAsSubtitle();
    }

    private void doValidation() {
        if (IMUtils.isNullOrEmpty(acTvMessage.getText().toString())) {

        } else if (!isRecipientsAdded()) {
            IMUIUtils.showAlert(this, getResources().getString(R.string.im_chat_error_text),
                    getResources().getString(R.string.im_add_recipient_chat_alert_msg));

        } else {
            IMChatMessage chatMessage = postMessageOnWindow(acTvMessage.getText().toString().trim(), null);
            publishMessage(chatMessage);
            acTvMessage.setText("");
        }
    }

    private boolean isRecipientsAdded() {
        boolean bool = false;
        if (mIMChatRoomDetail.roomMembers.size() > 1) {
            bool = true;
        }
        return bool;
    }

    private IMChatMessage postMessageOnWindow(String message, Uri uri) {
        IMChatMessage chatMessage = getChatMessage(message, uri);
        IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().insertChatMessage(chatMessage);
        mIMChatMessageAdapter.addChatMessage(chatMessage);
        return chatMessage;
    }

    public void publishMessage(IMChatMessage chatMessage) {
        IMInstance.getInstance().publishChatMessage(mIMChatRoomDetail.roomId, chatMessage);
    }

    private void setTypingStatus(String chatUserId, Boolean isTyping) {
        if (mtvTypingStatus.getVisibility() != View.VISIBLE && isTyping) {
            mtvTypingStatus.setVisibility(View.VISIBLE);
            mtvTypingStatus.setText(IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                    getChatUserById(chatUserId).userName + " " + getResources().getString(R.string.im_chat_typing_status));

        } else if (mtvTypingStatus.getVisibility() == View.VISIBLE && !isTyping) {
            mtvTypingStatus.setVisibility(View.GONE);
        }
    }

    private void createChatRoomInfo() {


        //TODO ERROR :

        mIMChatRoomDetail.origin = IMConstants.CHAT_MESSAGE_ORIGIN_CHAT;
        mIMChatRoomDetail.msgType = IMConstants.CHAT_MESSAGE_TYPE_ADMIN;
        mIMChatRoomDetail.adminId = mLoggedInUser.userId;
        if (mContextId != null && mContextName != null && mContextId.length() > 0 && mContextName.length() > 0) {
            mIMChatRoomDetail.roomMetaData.put("contextId", mContextId);
            mIMChatRoomDetail.roomMetaData.put("contextName", mContextName);
            mtvContextName.setVisibility(View.VISIBLE);
            mtvContextName.setText(mContextName);
        } else {
            mIMChatRoomDetail.roomMetaData.put("contextId", "");
            mIMChatRoomDetail.roomMetaData.put("contextName", "");
            mtvContextName.setVisibility(View.GONE);
        }
        mIMChatRoomDetail.msgVersion = IMConstants.CHAT_VERSION;
        mIMChatRoomDetail.roomName = "";
        mIMChatRoomDetail.timeToken = IMUtils.getDeviceTimeInMillis()
                - IMInstance.getInstance().getDeviceServerDeltaTime();

        if (mSelectedChatUsers.size() == 1) {
            if (mContextId != null && mContextName != null && mContextId.length() > 0 && mContextName.length() > 0) {
                mIMChatRoomDetail.roomId = ChatHelper.getRoomIdForContextualOneToOneChatThread(mLoggedInUser.userId,
                        mSelectedChatUsers.get(0).userId, mContextId);
            } else {
                mIMChatRoomDetail.roomId = ChatHelper.getRoomIdForOneToOneChatThread(mLoggedInUser.userId,
                        mSelectedChatUsers.get(0).userId);
            }

            mIMChatRoomDetail.roomType = IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE;

        } else if (mSelectedChatUsers.size() > 1) {
            if (mContextId != null && mContextName != null && mContextId.length() > 0 && mContextName.length() > 0) {
                mIMChatRoomDetail.roomId = mContextId + "-" + UUID.randomUUID().toString();
            } else {
                mIMChatRoomDetail.roomId = UUID.randomUUID().toString();
            }
            mIMChatRoomDetail.roomType = IMConstants.CHAT_ROOM_TYPE_GROUP;
        }

        mIMChatRoomDetail.roomMembers = ChatHelper.getChatRoomMemberList(mSelectedChatUsers, mLoggedInUser);
        mIMChatMessageAdapter.setChatRoomDetail(mIMChatRoomDetail);
    }

    private IMChatMessage getChatMessage(String message, Uri uri) {
        IMChatMessage chatMessage = new IMChatMessage();
        chatMessage.origin = IMConstants.CHAT_MESSAGE_ORIGIN_CHAT;
        chatMessage.msgSenderId = mLoggedInUser.userId;
        chatMessage.roomId = mIMChatRoomDetail.roomId;
        chatMessage.msgVersion = IMConstants.CHAT_VERSION;
        chatMessage.timeToken = IMUtils.getDeviceTimeInMillis()
                - IMInstance.getInstance().getDeviceServerDeltaTime();
        chatMessage.msgText = message;
        chatMessage.msgMembers = ChatHelper.getChatMessageMembers(mIMChatRoomDetail);
        chatMessage.isChatMsgSeen = true;

        if (null != uri && null != mCameraImageOrVideoMsgId && (uri.equals(mCameraImageUri)
                || uri.equals(mCameraVideoUri))) {
            chatMessage.msgId = mCameraImageOrVideoMsgId;

        } else {
            chatMessage.msgId = UUID.randomUUID().toString();
        }

        if (null != uri) {
            chatMessage.imageOrVideoFilePath = uri.toString();
            String mimeType = ChatMultiMediaHelper.getMimeType(this, uri);
            String filePath = ChatMultiMediaHelper.getGalleryFilePath(getApplicationContext(), uri.toString());
            if (!ChatMultiMediaHelper.getFileSizeCompatibility(filePath))
                return null;

            if (mimeType == null) {
                Toast.makeText(getApplicationContext(), "Invalid Media", Toast.LENGTH_SHORT).show();
                return null;
            }
            if (mimeType.contains("image")) {
                chatMessage.msgType = IMConstants.CHAT_MESSAGE_TYPE_IMAGE;

            } else if (mimeType.contains("video")) {
                chatMessage.msgType = IMConstants.CHAT_MESSAGE_TYPE_VIDEO;
            }

        } else {
            chatMessage.msgType = IMConstants.CHAT_MESSAGE_TYPE_TEXT;
        }


        return chatMessage;
    }

    private TextWatcher mOnTextChangeListener = new TextWatcher() {
        Timer timer;
        boolean isTimerStarted = false;
        boolean isTyping = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(final CharSequence s, int start, int before, int count) {
            if (isTimerStarted) {
                timer.cancel();
                isTimerStarted = false;
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (isTyping && (s.length() == 0)) {
                        IMInstance.getInstance().publishChatTypingStatus(mIMChatRoomDetail.roomId, false);
                        isTyping = false;

                    } else if (!isTyping && (s.length() > 0)) {
                        IMInstance.getInstance().publishChatTypingStatus(mIMChatRoomDetail.roomId, true);
                        isTyping = true;
                    }
                }
            }).start();
        }

        @Override
        public void afterTextChanged(Editable s) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    IMInstance.getInstance().publishChatTypingStatus(mIMChatRoomDetail.roomId, false);
                    isTyping = false;
                }
            }, IMConstants.TYPING_TIMEOUT);

            isTimerStarted = true;
        }
    };

    private OnClickListener mOnToolBarIconClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.im_iv_toolbar_chat_forward) {
                ChatHelper.openChatUserListActivityToForwardChat(IMChatActivity.this);

            } else if (v.getId() == R.id.im_iv_toolbar_chat_info) {
                IMChatMessage chatMessage = IMInstance.getInstance().getIMDatabase().getIMChatMessageDao()
                        .getChatMessageByMsgId(mIMChatMessageAdapter.getSelectedChatMessageId());

                if (null != chatMessage) {
                    ChatHelper.openChatMessageInfoActivity(IMChatActivity.this, mIMChatMessageAdapter.getSelectedChatMessageId());
                    mIMChatMessageAdapter.resetSelectedChatMsgItemPos();
                    showOrHideActionBarOption(false);
                }
            }
        }
    };

    private View.OnClickListener mOnChatMessageCopyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText(IMConstants.CHAT_MESSAGE_ORIGIN_CHAT,
                    mIMChatMessageAdapter.getAllCopiedChatMessage());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(IMChatActivity.this, getResources().getString(R.string.im_copied), Toast.LENGTH_SHORT).show();
        }
    };

    private OnClickListener mOnToolBarClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(IMChatActivity.this, IMChatThreadInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(IMConstants.CHAT_ROOM_ID, mIMChatRoomDetail.roomId);
            intent.putExtras(bundle);
            startActivityForResult(intent, IMConstants.CHAT_INFO_REQUEST_CODE);
        }
    };

    private OnClickListener mOnAddUsersClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            ChatHelper.openChatUserListActivityToAddUser(IMChatActivity.this, mIMChatRoomDetail,
                    ChatHelper.getChatRoomMemberIdList(mIMChatRoomDetail),
                    mIMChatRoomDetail.roomMetaData.get("contextId"), mIMChatRoomDetail.roomMetaData.get("contextName"));
        }
    };

    private OnClickListener mOnSendMsgClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            doValidation();
        }
    };

    OnClickListener mOnUploadBtnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!isRecipientsAdded()) {
                IMUIUtils.showAlert(IMChatActivity.this, getResources().getString(R.string.im_chat_error_text),
                        getResources().getString(R.string.im_add_recipient_chat_alert_msg));

            } else {
                mCameraImageOrVideoMsgId = UUID.randomUUID().toString();
                File imageFilePath = new File(ChatMultiMediaHelper.getPLiveSentImagePath() + "/" + mCameraImageOrVideoMsgId + ".jpg");

                File videoFilePath = new File(ChatMultiMediaHelper.getPLiveSentVideoPath() + "/" + mCameraImageOrVideoMsgId + ".mp4");


//                mCameraImageUri = Uri.fromFile(imageFilePath);
//                mCameraVideoUri = Uri.fromFile(videoFilePath);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    mCameraImageUri = FileProvider.getUriForFile(
                            getBaseContext(),
                            getBaseContext().getApplicationContext()
                                    .getPackageName() + ".provider", imageFilePath);
                    mCameraVideoUri = FileProvider.getUriForFile(
                            getBaseContext(),
                            getBaseContext().getApplicationContext()
                                    .getPackageName() + ".provider", videoFilePath);
                } else {
                    mCameraImageUri = Uri.fromFile(imageFilePath);
                    mCameraVideoUri = Uri.fromFile(videoFilePath);
                }

                cvAttachmentOptions.setVisibility(cvAttachmentOptions.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                //ChatMultiMediaHelper.showPopupMenuForMultiMediaUploadOptions(IMChatActivity.this, view, mCameraImageUri, mCameraVideoUri);
            }
        }
    };

    OnClickListener mFlDownChatClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mIMChatMessageAdapter.scrolltoend();
        }
    };

    private IMChattingListener mIMChattingListener = new IMChattingListener() {

        @Override
        public void onDeviceTimeUpdated() {
            setUpExistingChatRoom();
        }

        @Override
        public void onChatMessageReceived(final IMChatMessage chatMessage) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIMChatMessageAdapter.addChatMessage(chatMessage);
                }
            });
        }

        @Override
        public void onChatMessagePublished(final IMChatMessage chatMessage) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mIMChatMessageAdapter.updateChatMessage(chatMessage);
                }
            });
        }

        @Override
        public void onChatRoomDetailReceived(final IMChatRoomDetail chatRoomDetail) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateReceivedChatRoomDetail(chatRoomDetail);
                }
            });
        }

        @Override
        public void onChatRoomDetailPublished(final IMChatRoomDetail chatRoomDetail) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updatePublishedChatRoomDetail(chatRoomDetail);
                }
            });
        }

        @Override
        public void onChatRoomNameUpdateMessageReceived(final IMChatRoomDetailUpdate chatRoomDetailUpdate) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateReceivedChatRoomName(chatRoomDetailUpdate.roomName);
                }
            });
        }

        @Override
        public void onChatMessageStatusReceived(final IMChatMessageStatus chatMessageStatus) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    IMChatMessage chatMessage = ChatHelper.getChatMessageWithUpdatedReadAndDeliveryStatus(chatMessageStatus);
                    mIMChatMessageAdapter.updateChatMessage(chatMessage);
                }
            });
        }

        @Override
        public void onChatTypingStatusReceived(final String roomId, final String userId,
                                               final Boolean isTyping) {


            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setTypingStatus(userId, isTyping);
                }
            });
        }
    };

    private IMUserPresenceListener mIMUserPresenceListener = new IMUserPresenceListener() {
        @Override
        public void onUserStatusReceived(final String userId, final Boolean isOnline) {
            IMChatActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setOnlineUserStatus(userId, isOnline);
                }
            });

        }
    };


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            handleBackNavigation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        handleBackNavigation();
    }

    private void handleBackNavigation() {
        if (mToolbarForwardChat.getVisibility() == View.VISIBLE) {
            mIMChatMessageAdapter.resetSelectedChatMsgItemPos();
            showOrHideActionBarOption(false);

        } else if (!isMsgHasBeenPublishedForNewChatRoom) {
            Toast.makeText(this, getResources().getString(R.string.im_setting_up_chatting), Toast.LENGTH_LONG).show();

        } else {
            closeChatWindow();
        }
    }

    private void closeChatWindow() {
        IMInstance.getInstance().setIMChattingListener(null, null);
        IMInstance.getInstance().updateCurrentRoomState(false);

        saveLastTypedText();
        finish();
    }

    private void saveLastTypedText() {
        IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().
                updateChatRoomLastTypedText(mIMChatRoomDetail.roomId, acTvMessage.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "The onActivityResult has been called");
        super.onActivityResult(requestCode, resultCode, data);

        if (null != data && ((requestCode == IMConstants.ADD_USER_REQUEST_CODE) || requestCode == IMConstants.CHAT_INFO_REQUEST_CODE)) {
            ArrayList<IMChatUser> users = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                    getChatUserListByUserIds(data.getStringArrayListExtra(IMConstants.ADD_USER_ID_LIST));
            publishUpdatedChatRoomDetail(users, data.getStringExtra(IMConstants.CHAT_ROOM_NAME));

        } else if (null != data && requestCode == IMConstants.REQUEST_FORWARD_CHAT) {
            Intent intent = new Intent(this, IMChatActivity.class);
            Bundle bundle = new Bundle();
            bundle.putBoolean(IMConstants.IS_CHAT_FORWARDED, true);
            bundle.putString(IMConstants.CHAT_FORWARDED_MESSAGE, mIMChatMessageAdapter.getAllSelectedChatMessage());
            bundle.putStringArrayList(IMConstants.ADD_USER_ID_LIST, data.getStringArrayListExtra(IMConstants.ADD_USER_ID_LIST));
            intent.putExtras(bundle);
            startActivity(intent);
            closeChatWindow();

        } else if (resultCode == RESULT_OK && requestCode == ChatMultiMediaHelper.REQUEST_TAKE_GALLERY_IMAGE_OR_VIDEO) {
            if (data.getData().toString().contains("image") || data.getData().toString().contains("video") || data.getData().toString().contains("mp4") ||
                    data.getData().toString().contains("jpg") || data.getData().toString().contains("png")) {
                if (ChatMultiMediaHelper.getFileSizeCompatibility(ChatMultiMediaHelper.getGalleryFilePath(getApplicationContext(), data.getData().toString()))) {
                    postMessageOnWindow("", data.getData());

                } else {
                    Toast.makeText(getApplicationContext(), "File size exceeded", Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Media", Toast.LENGTH_SHORT).show();
            }

        } else if (resultCode == RESULT_OK && requestCode == ChatMultiMediaHelper.REQUEST_TAKE_CAMERA_IMAGE) {
            if (ChatMultiMediaHelper.getFileSizeCompatibility(ChatMultiMediaHelper.getGalleryFilePath(getApplicationContext(), mCameraImageUri.toString()))) {
                postMessageOnWindow("", mCameraImageUri);
            } else {
                Toast.makeText(getApplicationContext(), "File size exceeded", Toast.LENGTH_SHORT).show();

            }

        } else if (resultCode == RESULT_OK && requestCode == ChatMultiMediaHelper.REQUEST_TAKE_CAMERA_VIDEO) {
            if (ChatMultiMediaHelper.getFileSizeCompatibility(ChatMultiMediaHelper.getGalleryFilePath(getApplicationContext(), mCameraVideoUri.toString()))) {
                postMessageOnWindow("", mCameraVideoUri);
            } else {
                Toast.makeText(getApplicationContext(), "File size exceeded", Toast.LENGTH_SHORT).show();

            }

        } else if(requestCode == MAP_REQUEST_CODE){
            sendLocationInMessage();
        }
    }

    private void sendLocationInMessage() {
        acTvMessage.setText("Location shared"+" "+"https://www.google.com/maps/search/?api=1&query="+latitude_selected+","+longitude_selected+"");
        doValidation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ChatMultiMediaHelper.handlePermissionsForMultiMedia(this, requestCode, grantResults, mCameraImageUri, mCameraVideoUri);
    }

    @Override
    public void onBottomReached(boolean bottom) {
        if (bottom) mFlDownChat.setVisibility(View.GONE);
        else mFlDownChat.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress(String text) {

    }

    @Override
    public void dismissProgress() {

    }

    @Override
    public void showInternetError() {

    }

    @Override
    public void showErrorDialog(String title, String description, String positiveBtn) {

    }

    @Override
    public void onView() {

        //TODO  move to stories ;

    }

    public static class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (null != mSendMsgBtn) {
                if (IMNetwork.isConnected(context)) {
                    mSendMsgBtn.setVisibility(View.VISIBLE);

                } else {
                    mSendMsgBtn.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private final BroadcastReceiver mMultiMediaTransferIdBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                String roomId = bundle.getString(IMConstants.MULTIMEDIA_FILE_ROOM_ID);
                if (null != roomId && roomId.equals(mIMChatRoomDetail.roomId)) {
                    int transferId = bundle.getInt(IMConstants.TRANSFER_ID);
                    mIMChatMessageAdapter.addCurrentMediaTransferIdToList(transferId);
                }
            }
        }
    };

    private final BroadcastReceiver mMultiMediaUploadDownloadProgressBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                String roomId = bundle.getString(IMConstants.MULTIMEDIA_FILE_ROOM_ID);
                if (null != roomId && roomId.equals(mIMChatRoomDetail.roomId)) {
                    String transferType = bundle.getString(IMConstants.TRANSFER_TYPE);
                    String fileType = bundle.getString(IMConstants.FILE_TYPE);
                    int transferId = bundle.getInt(IMConstants.TRANSFER_ID);
                    long bytesCurrent = bundle.getLong(IMConstants.BYTES_TRANSFER_CURRENT);
                    long bytesTotal = bundle.getLong(IMConstants.BYTES_TRANSFER_TOTAL);
                    mIMChatMessageAdapter.setUploadDownLoadProgress(transferType, fileType, transferId, bytesCurrent, bytesTotal);
                }
            }
        }
    };

    private final BroadcastReceiver mMultiMediaUploadDownloadStateBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            if (null != bundle) {
                String roomId = bundle.getString(IMConstants.MULTIMEDIA_FILE_ROOM_ID);
                if (null != roomId && roomId.equals(mIMChatRoomDetail.roomId)) {
                    String transferType = bundle.getString(IMConstants.TRANSFER_TYPE);
                    String fileType = bundle.getString(IMConstants.FILE_TYPE);
                    int transferId = bundle.getInt(IMConstants.TRANSFER_ID);
                    mIMChatMessageAdapter.setUploadDownLoadState(transferType, fileType, transferId);
                }
            }
        }
    };

    public void showTourGuide() {
        TourGuideHelper tourGuideHelper = new TourGuideHelper(this);
        tourGuideHelper.addView(mAddUsers, "Add User", "You can selects multiple users to chat with !", Gravity.BOTTOM);
        tourGuideHelper.addView(mUploadBtn, "Multimedia", "Send Images and Video messages !", Gravity.TOP | Gravity.RIGHT);
        tourGuideHelper.showGuide();
    }



    RecyclerView recyclerView;
    BottomSheetDialog bts;
    View sheetView;
    public void getDilog(){


        bts = new BottomSheetDialog(this,R.style.BottomSheetDialogTheme);
        TextView close;

        sheetView = getLayoutInflater().inflate(R.layout.popup_chat__datahandler, null);
        recyclerView = sheetView.findViewById(R.id.itemview);
        close = sheetView.findViewById(R.id.tvExpYrs);

        getTotalNumberofJobPost();
        // invoke data calling function ;


        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

        bts.setContentView(sheetView);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bts.dismiss();

            }
        });


//bts.show();




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
        new IMChatActivity.GetFiles().execute(Url_path, file_name);
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



    //todo moving to the data handler
    public void getUrl(String url) {

        Intent i = new Intent(this, PdfViewer.class);
        i.putExtra("url",url);
        startActivity(i);

    }



    public void handlerdataset(){


    }
}