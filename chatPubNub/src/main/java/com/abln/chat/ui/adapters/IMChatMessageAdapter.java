package com.abln.chat.ui.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferType;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMBottomReachedListener;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.ui.activities.IMChatActivity;
import com.abln.chat.ui.helper.AWSClientHelper;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.ui.helper.ReceivedImageHandler;
import com.abln.chat.ui.helper.ReceivedVideoHandler;
import com.abln.chat.ui.helper.SentImageHandler;
import com.abln.chat.ui.helper.SentVideoHandler;
import com.abln.chat.ui.helper.ChatMultiMediaHelper;
import com.abln.chat.utils.IMUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class IMChatMessageAdapter extends RecyclerView.Adapter<IMChatMessageAdapter.MessageViewHolder> {
    public static final String TAG = IMChatMessageAdapter.class.getSimpleName();
    private final String mSearchedMsgId;

    private Context mContext;
    private RecyclerView mRecyclerView;
    final private Handler handler = new Handler();

    private ArrayList<IMChatMessage> mChatMessageList = new ArrayList<>();
    private ArrayList<Integer> mSelectedPositionList = new ArrayList<>();
    private HashMap<String, Integer> mAllMediaTransferIdList = new HashMap<>();
    private HashSet<String> mInProgressMultiMediaMsgId = new HashSet<>();

    private IMChatRoomDetail mIMChatRoomDetail;
    private IMChatUser mLoggedInUser;
    private boolean isLongPress;

    private SentImageHandler mSentImageHandler;
    private SentVideoHandler mSentVideoHandler;
    private ReceivedImageHandler mReceivedImageHandler;
    private IMBottomReachedListener imBottomReachedListener;
    private ReceivedVideoHandler mReceivedVideoHandler;

    public IMChatMessageAdapter(Context context, RecyclerView recyclerView, IMChatUser loggedInUser, ArrayList<IMChatMessage> userMessageList, String mChatMsgId, IMBottomReachedListener imBottomReachedListener) {
        this.mContext = context;
        this.mRecyclerView = recyclerView;

        this.mChatMessageList = userMessageList;
        this.mLoggedInUser = loggedInUser;
        this.mSearchedMsgId = mChatMsgId;
        this.imBottomReachedListener = imBottomReachedListener;

        setRecyclerViewProperties();
        setChatMessageDefaultSelectedPos();
        //prepareMediaCompletedTransferList(); TODO required AWS initialization

        mSentImageHandler = new SentImageHandler(context, this);
        mSentVideoHandler = new SentVideoHandler(context, this);
        mReceivedImageHandler = new ReceivedImageHandler(context, this);
        mReceivedVideoHandler = new ReceivedVideoHandler(context, this);


    }

    private void setRecyclerViewProperties() {
        this.mRecyclerView.setHasFixedSize(true);
        this.mRecyclerView.setItemViewCacheSize(30);
        this.mRecyclerView.setDrawingCacheEnabled(true);
        this.mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
//        this.mRecyclerView.getRecycledViewPool().setMaxRecycledViews(30);
    }


    private void setChatMessageDefaultSelectedPos() {
        for (int i = 0; i < mChatMessageList.size(); i++) {
            // Below condition is required, so that if a chat message has been added
            // after selection will not override the previous selection
            if (i > mSelectedPositionList.size() - 1) {
                mSelectedPositionList.add(0);
            }
        }
    }

    private void prepareMediaCompletedTransferList() {
        List<TransferObserver> uploadObservers = AWSClientHelper.getInstance().getTransferUtility()
                .getTransfersWithType(TransferType.UPLOAD);

        List<TransferObserver> downloadObservers = AWSClientHelper.getInstance().getTransferUtility()
                .getTransfersWithType(TransferType.DOWNLOAD);

        for (TransferObserver transferObserver : uploadObservers) {
            mAllMediaTransferIdList.put(transferObserver.getKey(), transferObserver.getId());
        }

        for (TransferObserver transferObserver : downloadObservers) {
            mAllMediaTransferIdList.put(transferObserver.getKey(), transferObserver.getId());
        }
    }


    public void setChatRoomDetail(IMChatRoomDetail chatRoomDetail) {
        this.mIMChatRoomDetail = chatRoomDetail;
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public View itemView;

        View senderTextMsg;
        TextView tvSenderTextMsg;
        TextView tvSenderTextMsgTime;
        ImageView ivSenderTextMsgStatusImage;

        View senderImageMsg;
        TextView tvSenderImageMsgTime;
        public ImageView ivSenderImageMsgImageThumb;
        public ImageView ivSenderImageMsgStatusImage;
        public Button btnSenderImageMsgUploadBtn;
        public CircularProgressBar cpbSenderImageMsgProgressBar;

        View senderVideoMsg;
        TextView tvSenderVideoMsgTime;
        public TextView tvSenderVideoMsgVideoDuration;
        public ImageView ivSenderVideoMsgImageThumb;
        ImageView ivSenderVideoMsgStatusImage;
        public ImageView ivSenderVideoMsgPlayIcon;
        public Button btnSenderVideoMsgUploadBtn;
        public CircularProgressBar cpbSenderVideoMsgProgressBar;

        View senderAudioMsg;

        View recipientTextMsg;
        TextView tvRecipientTextMsg;
        TextView tvRecipientTextMsgTime;
        TextView tvRecipientTextMsgOwnerName;

        View recipientImageMsg;
        TextView tvRecipientImageMsgTime;
        TextView tvRecipientImageMsgOwnerName;
        public ImageView ivRecipientImageMsgImageThumb;
        public Button btnRecipientImageMsgDownloadBtn;
        public CircularProgressBar cpbRecipientImageMsgProgressBar;

        View recipientVideoMsg;
        TextView tvRecipientVideoMsgTime;
        TextView tvRecipientVideoMsgOwnerName;
        public TextView tvRecipientVideoMsgVideoDuration;
        public ImageView ivRecipientVideoMsgImageThumb;
        public ImageView ivRecipientVideoMsgPlayIcon;
        public Button btnRecipientVideoMsgDownloadBtn;
        public CircularProgressBar cpbRecipientVideoMsgProgressBar;

        View recipientAudioMsg;


        MessageViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;

            senderTextMsg = itemView.findViewById(R.id.im_include_sender_text_msg);
            tvSenderTextMsg = (TextView) itemView.findViewById(R.id.im_tv_sender_text_msg_text);
            tvSenderTextMsgTime = (TextView) itemView.findViewById(R.id.im_tv_sender_text_msg_time);
            ivSenderTextMsgStatusImage = (ImageView) itemView.findViewById(R.id.im_iv_sender_text_msg_status);

            senderImageMsg = itemView.findViewById(R.id.im_include_sender_image_msg);
            tvSenderImageMsgTime = (TextView) itemView.findViewById(R.id.im_tv_sender_image_msg_time);
            ivSenderImageMsgImageThumb = (ImageView) itemView.findViewById(R.id.im_iv_sender_image_msg_thumb);
            ivSenderImageMsgStatusImage = (ImageView) itemView.findViewById(R.id.im_iv_sender_image_msg_status);
            btnSenderImageMsgUploadBtn = (Button) itemView.findViewById(R.id.im_btn_sender_image_msg_upload);
            cpbSenderImageMsgProgressBar = (CircularProgressBar) itemView.findViewById(R.id.im_cpb_sender_image_msg_progress);

            senderVideoMsg = itemView.findViewById(R.id.im_include_sender_video_msg);
            tvSenderVideoMsgTime = (TextView) itemView.findViewById(R.id.im_tv_sender_video_msg_time);
            tvSenderVideoMsgVideoDuration = (TextView) itemView.findViewById(R.id.im_tv_sender_video_msg_video_duration);
            ivSenderVideoMsgImageThumb = (ImageView) itemView.findViewById(R.id.im_iv_sender_video_msg_thumb);
            ivSenderVideoMsgStatusImage = (ImageView) itemView.findViewById(R.id.im_iv_sender_video_msg_status);
            ivSenderVideoMsgPlayIcon = (ImageView) itemView.findViewById(R.id.im_iv_sender_video_msg_play_icon);
            btnSenderVideoMsgUploadBtn = (Button) itemView.findViewById(R.id.im_btn_sender_video_msg_upload);
            cpbSenderVideoMsgProgressBar = (CircularProgressBar) itemView.findViewById(R.id.im_cpb_sender_video_msg_progress);

            recipientTextMsg = itemView.findViewById(R.id.im_include_recipient_text_msg);
            tvRecipientTextMsg = (TextView) itemView.findViewById(R.id.im_tv_recipient_text_msg_text);
            tvRecipientTextMsgTime = (TextView) itemView.findViewById(R.id.im_tv_recipient_text_msg_time);
            tvRecipientTextMsgOwnerName = (TextView) itemView.findViewById(R.id.im_tv_recipient_text_msg_owner_name);

            recipientImageMsg = itemView.findViewById(R.id.im_include_recipient_image_msg);
            tvRecipientImageMsgTime = (TextView) itemView.findViewById(R.id.im_tv_recipient_image_msg_time);
            tvRecipientImageMsgOwnerName = (TextView) itemView.findViewById(R.id.im_tv_recipient_image_msg_owner_name);
            ivRecipientImageMsgImageThumb = (ImageView) itemView.findViewById(R.id.im_iv_recipient_image_msg_thumb);
            btnRecipientImageMsgDownloadBtn = (Button) itemView.findViewById(R.id.im_btn_recipient_image_msg_download);
            cpbRecipientImageMsgProgressBar = (CircularProgressBar) itemView.findViewById(R.id.im_cpb_recipient_image_msg_progress);

            recipientVideoMsg = itemView.findViewById(R.id.im_include_recipient_video_msg);
            tvRecipientVideoMsgTime = (TextView) itemView.findViewById(R.id.im_tv_recipient_video_msg_time);
            tvRecipientVideoMsgOwnerName = (TextView) itemView.findViewById(R.id.im_tv_recipient_video_msg_owner_name);
            tvRecipientVideoMsgVideoDuration = (TextView) itemView.findViewById(R.id.im_tv_recipient_video_msg_video_duration);
            ivRecipientVideoMsgImageThumb = (ImageView) itemView.findViewById(R.id.im_iv_recipient_video_msg_thumb);
            ivRecipientVideoMsgPlayIcon = (ImageView) itemView.findViewById(R.id.im_iv_recipient_video_msg_play_icon);
            btnRecipientVideoMsgDownloadBtn = (Button) itemView.findViewById(R.id.im_btn_recipient_video_msg_download);
            cpbRecipientVideoMsgProgressBar = (CircularProgressBar) itemView.findViewById(R.id.im_cpb_recipient_video_msg_progress);

        }
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.im_item_chat_message, parent, false);
        return new MessageViewHolder(v);
    }

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 0;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    @Override
    public void onBindViewHolder(MessageViewHolder messageViewHolder, int position) {
        resetMessageViewHolder(messageViewHolder);
        int rowType = getItemViewType(position);

        switch (rowType) {
            case 0:
                if (mChatMessageList.get(position).msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_TEXT)) {
                    setSenderTextMessage(messageViewHolder, position);

                } else if (mChatMessageList.get(position).msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_IMAGE)) {
                    setSenderImageMessage(messageViewHolder, position);

                } else if (mChatMessageList.get(position).msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
                    setSenderVideoMessage(messageViewHolder, position);
                }
                if (mChatMessageList.get(position).msgId.equalsIgnoreCase(mSearchedMsgId))
                    setSenderSearchedTextColor(messageViewHolder, position);

                break;

            case 1:
                if (mChatMessageList.get(position).msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_TEXT)) {
                    setRecipientTextMessage(messageViewHolder, position);

                } else if (mChatMessageList.get(position).msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_IMAGE)) {
                    setRecipientImageMessage(messageViewHolder, position);

                } else if (mChatMessageList.get(position).msgType.equals(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
                    setRecipientVideoMessage(messageViewHolder, position);
                }
                if (mChatMessageList.get(position).msgId.equalsIgnoreCase(mSearchedMsgId))
                    setReceiverSearchedTextColor(messageViewHolder, position);

                break;
        }


        setOnItemLongClickListener(messageViewHolder.itemView, position);
        setOnItemClickListener(messageViewHolder.itemView, position);
    }


    private void setReceiverSearchedTextColor(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.recipientTextMsg.setBackgroundColor(Color.parseColor("#214F4B"));
    }

    private void setSenderSearchedTextColor(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.senderTextMsg.setBackgroundColor(Color.parseColor("#214F4B"));
    }

    private void resetMessageViewHolder(MessageViewHolder messageViewHolder) {
        messageViewHolder.senderTextMsg.setVisibility(View.GONE);
        messageViewHolder.senderImageMsg.setVisibility(View.GONE);
        messageViewHolder.senderVideoMsg.setVisibility(View.GONE);

        messageViewHolder.recipientTextMsg.setVisibility(View.GONE);
        messageViewHolder.recipientImageMsg.setVisibility(View.GONE);
        messageViewHolder.recipientVideoMsg.setVisibility(View.GONE);

    }


    @Override
    public int getItemCount() {
        return mChatMessageList.size();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatMessageList.get(position).msgSenderId.equalsIgnoreCase(mLoggedInUser.userId)) {
            return 0;
        } else {
            return 1;
        }
    }

    private void setSenderTextMessage(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.senderTextMsg.setVisibility(View.VISIBLE);
        IMChatMessage chatMessage = mChatMessageList.get(position);

        messageViewHolder.tvSenderTextMsg.setText(chatMessage.msgText);
        setChatMessageTime(messageViewHolder.tvSenderTextMsgTime, chatMessage);
        ChatHelper.setSenderChatMsgStatus(messageViewHolder.ivSenderTextMsgStatusImage, mLoggedInUser.userId, chatMessage);

        setChatMsgSelectionIfExist(messageViewHolder.itemView, position);
    }

    private void setSenderImageMessage(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.senderImageMsg.setVisibility(View.VISIBLE);
        IMChatMessage chatMessage = mChatMessageList.get(position);

        mSentImageHandler.setUp(messageViewHolder, chatMessage, position);
        setChatMessageTime(messageViewHolder.tvSenderImageMsgTime, chatMessage);
        ChatHelper.setSenderChatMsgStatus(messageViewHolder.ivSenderImageMsgStatusImage, mLoggedInUser.userId, chatMessage);

        setChatMsgSelectionIfExist(messageViewHolder.itemView, position);
    }

    private void setSenderVideoMessage(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.senderVideoMsg.setVisibility(View.VISIBLE);
        IMChatMessage chatMessage = mChatMessageList.get(position);

        mSentVideoHandler.setUp(messageViewHolder, chatMessage, position);
        setChatMessageTime(messageViewHolder.tvSenderVideoMsgTime, chatMessage);
        ChatHelper.setSenderChatMsgStatus(messageViewHolder.ivSenderVideoMsgStatusImage, mLoggedInUser.userId, chatMessage);

        setChatMsgSelectionIfExist(messageViewHolder.itemView, position);
        setOnVideoThumbnailClickListener(messageViewHolder.ivSenderVideoMsgImageThumb, chatMessage.msgId);
    }

    private void setRecipientTextMessage(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.recipientTextMsg.setVisibility(View.VISIBLE);
        IMChatMessage chatMessage = mChatMessageList.get(position);

        messageViewHolder.tvRecipientTextMsg.setText(chatMessage.msgText);
        setChatMessageTime(messageViewHolder.tvRecipientTextMsgTime, chatMessage);
        setReceivedMsgOwnerName(messageViewHolder.tvRecipientTextMsgOwnerName, chatMessage.msgSenderId);

        setChatMsgSelectionIfExist(messageViewHolder.itemView, position);
    }

    private void setRecipientImageMessage(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.recipientImageMsg.setVisibility(View.VISIBLE);
        IMChatMessage chatMessage = mChatMessageList.get(position);

        mReceivedImageHandler.setUp(messageViewHolder, chatMessage, position);
        setChatMessageTime(messageViewHolder.tvRecipientImageMsgTime, chatMessage);
        setReceivedMsgOwnerName(messageViewHolder.tvRecipientImageMsgOwnerName, chatMessage.msgSenderId);

        setChatMsgSelectionIfExist(messageViewHolder.itemView, position);
    }

    private void setRecipientVideoMessage(MessageViewHolder messageViewHolder, int position) {
        messageViewHolder.recipientVideoMsg.setVisibility(View.VISIBLE);
        IMChatMessage chatMessage = mChatMessageList.get(position);

        mReceivedVideoHandler.setUp(messageViewHolder, chatMessage, position);
        setChatMessageTime(messageViewHolder.tvRecipientVideoMsgTime, chatMessage);
        setReceivedMsgOwnerName(messageViewHolder.tvRecipientVideoMsgOwnerName, chatMessage.msgSenderId);

        setChatMsgSelectionIfExist(messageViewHolder.itemView, position);
    }

    private void setChatMessageTime(TextView textView, IMChatMessage chatMessage) {
        long timeToken = chatMessage.timeToken + IMInstance.getInstance().getDeviceServerDeltaTime();
        textView.setText(IMUtils.formatPubNubTimeStamp(timeToken));
    }

    private void setChatMsgSelectionIfExist(View view, int position) {
        if (mSelectedPositionList.get(position) == 1) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.im_anakiwa_40_alpha));
        } else {
            view.setBackgroundColor(0);
        }
    }

    private void setReceivedMsgOwnerName(TextView textView, String ownerId) {
        if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            textView.setVisibility(View.GONE);

        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(IMInstance.getInstance().getIMDatabase().getIMChatUserDao()
                    .getChatUserById(ownerId).userName);
        }
    }

    public void setOnImageClickListener(final int position, final View itemView, ImageView imageThumb) {
        imageThumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongPress) {
                    performItemClickAction(itemView, position);

                } else {
                    ChatMultiMediaHelper.openChatMultimediaSlideActivity(mContext, mIMChatRoomDetail.roomId,
                            mChatMessageList.get(position).msgId);
                }
            }
        });
    }

    private void setOnVideoThumbnailClickListener(ImageView thumbnail, final String msgId) {
        thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMultiMediaHelper.openChatMultimediaSlideActivity(mContext, mIMChatRoomDetail.roomId, msgId);
            }
        });
    }

    private void setOnItemLongClickListener(final View view, final int position) {
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                isLongPress = true;
                performItemClickAction(view, position);
                return true;
            }
        });
    }

    private void setOnItemClickListener(final View view, final int position) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isLongPress) {
                    performItemClickAction(view, position);
                }
            }
        });
    }

    private void performItemClickAction(View view, final int position) {
        if (mSelectedPositionList.get(position) == 0) {
            view.setBackgroundColor(mContext.getResources().getColor(R.color.im_anakiwa_40_alpha));
            mSelectedPositionList.set(position, 1);

        } else {
            view.setBackgroundColor(0);
            mSelectedPositionList.set(position, 0);
        }

        showHideToolbarOptions();
        toggleOptionMenuItems();
    }

    private void showHideToolbarOptions() {
        IMChatActivity chatActivity = (IMChatActivity) mContext;
        if (mSelectedPositionList.contains(1)) {
            chatActivity.showOrHideActionBarOption(true);
        } else {
            chatActivity.showOrHideActionBarOption(false);
            // removing the long press state
            isLongPress = false;
        }
    }

    private void toggleOptionMenuItems() {
        boolean bool = true;
        int count = 0;
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            if (mSelectedPositionList.get(i) == 1 && mChatMessageList.get(i).msgSenderId.
                    equals(mLoggedInUser.userId)) {
                ++count;
                if (count > 1) {
                    bool = false;
                    break;
                }
            }

            if (mSelectedPositionList.get(i) == 1 && !mChatMessageList.get(i).msgSenderId.
                    equals(mLoggedInUser.userId)) {
                bool = false;
                break;
            }
        }
        ((IMChatActivity) mContext).toggleOptionMenuItems(bool);
    }

    public void resetSelectedChatMsgItemPos() {
        mSelectedPositionList.clear();
        for (int i = 0; i < mChatMessageList.size(); i++) {
            mSelectedPositionList.add(0);
        }
        // removing the long press state
        isLongPress = false;
        notifyDataSetChanged();
    }

    public String getSelectedChatMessageId() {
        String msgId = "";
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            if (mSelectedPositionList.get(i) == 1) {
                msgId = mChatMessageList.get(i).msgId;
                break;
            }
        }
        return msgId;
    }

    public String getAllSelectedChatMessage() {
        StringBuilder chatMsg = new StringBuilder();
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            if (mSelectedPositionList.get(i) == 1) {
                chatMsg.append(mChatMessageList.get(i).msgText);
                if (i < mSelectedPositionList.size() - 1) {
                    chatMsg.append("\n");
                }
            }
        }
        return chatMsg.toString();
    }

    public String getAllCopiedChatMessage() {
        StringBuilder chatMsg = new StringBuilder();
        for (int i = 0; i < mSelectedPositionList.size(); i++) {
            if (mSelectedPositionList.get(i) == 1) {
                chatMsg.append(mChatMessageList.get(i).msgText);
                if (i < mSelectedPositionList.size() - 1) {
                    chatMsg.append(" ");
                }
            }
        }
        return chatMsg.toString();
    }

    public ArrayList<IMChatMessage> getAllChatMessages() {
        return mChatMessageList;
    }

    public MessageViewHolder getMessageViewHolderByMsgId(String msgId) {
        IMChatMessage chatMessage = new IMChatMessage();
        chatMessage.msgId = msgId;
        int position = mChatMessageList.indexOf(chatMessage);

        return (MessageViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
    }

    private int getMessageViewHolderIndexByMsgId(String msgId) {
        IMChatMessage chatMessage = new IMChatMessage();
        chatMessage.msgId = msgId;
        return mChatMessageList.indexOf(chatMessage);
    }

    public void addChatHistory(ArrayList<IMChatMessage> chatMessagesList, boolean historyCall) {
        mChatMessageList.clear();
        mChatMessageList.addAll(chatMessagesList);
        setChatMessageDefaultSelectedPos();
        notifyDataSetChanged();
        if (chatMessagesList.size() > 0) {
            if (mSearchedMsgId != null && mSearchedMsgId.length() > 0) {
                for (int i = 0; i < chatMessagesList.size(); i++) {
                    if (chatMessagesList.get(i).msgId.equalsIgnoreCase(mSearchedMsgId)) {
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,
                                null, i);

                    }

                }
            } else {
                if (!historyCall)
                    scrollListToThePosition(chatMessagesList.get(chatMessagesList.size() - 1));
            }
        }
    }

    public void scrolltoend() {
        if (mChatMessageList.size() > 1)
            mRecyclerView.getLayoutManager().scrollToPosition(mChatMessageList.size() - 1);

    }

    public void scrolltoPosition(int position) {
        if (mChatMessageList.size() > 1)
            mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView, null, 0);
    }


    public void addChatMessage(IMChatMessage chatMessage) {
        if (-1 == getMessageViewHolderIndexByMsgId(chatMessage.msgId)) {
            mChatMessageList.add(chatMessage);
            setChatMessageDefaultSelectedPos();
            scrollListToThePosition(chatMessage);
            notifyItemInserted(getItemCount() - 1);
        }
    }

    public void updateChatMessage(IMChatMessage chatMessage) {
        int index = mChatMessageList.indexOf(chatMessage);
        if (-1 != index) {
            mChatMessageList.set(index, chatMessage);
            notifyItemChanged(index);
        }
    }

    private void scrollListToThePosition(IMChatMessage chatMessage) {
        if (ChatHelper.isMultiMediaMessage(chatMessage)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (getItemCount() > 0) {
                        mRecyclerView.getLayoutManager().smoothScrollToPosition(mRecyclerView,
                                null, getItemCount() - 1);
                    }
                }
            }, 1000);

        } else {
            mRecyclerView.scrollToPosition(getItemCount() - 1);
        }
    }


    public void setInProgressMultiMediaMsg(String msgId) {
        mInProgressMultiMediaMsgId.add(msgId);
    }

    public boolean isInProgressMultiMediaMsg(String msgId) {
        return mInProgressMultiMediaMsgId.contains(msgId);
    }

    public boolean isTransferIdAlreadyExist(String transferId) {
        return mAllMediaTransferIdList.containsKey(transferId);
    }

    public Integer getTransferIdByMsgId(String msgId) {
        return mAllMediaTransferIdList.get(msgId);

    }

    public void addCurrentMediaTransferIdToList(int id) {
        TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().getTransferById(id);
        mAllMediaTransferIdList.put(transferObserver.getKey(), transferObserver.getId());
    }

    public void setUploadDownLoadProgress(final String transferType, final String fileType, final int transferId,
                                          final long bytesCurrent, final long bytesTotal) {
        final TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().getTransferById(transferId);
        final int position = getMessageViewHolderIndexByMsgId(transferObserver.getKey());

        if (position >= 0) {
            final IMChatMessage chatMessage = mChatMessageList.get(position);
            final MessageViewHolder messageViewHolder = (MessageViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);

            if (null == messageViewHolder) {
                return;
            }


            if (transferObserver.getState().equals(TransferState.IN_PROGRESS)) {
                if (transferType.equals(IMConstants.TRANSFER_TYPE_UPLOAD)) {
                    if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                        mSentImageHandler.setImageUploadProgress(messageViewHolder, bytesCurrent, bytesTotal);

                    } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                        mSentVideoHandler.setVideoUploadProgress(messageViewHolder, bytesCurrent, bytesTotal);
                    }

                } else if (transferType.equals(IMConstants.TRANSFER_TYPE_DOWNLOAD)) {
                    if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                        mReceivedImageHandler.setImageDownloadProgress(messageViewHolder, bytesCurrent, bytesTotal);

                    } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                        mReceivedVideoHandler.setVideoDownloadProgress(messageViewHolder, chatMessage, bytesCurrent, bytesTotal);
                    }
                }
            }

        }
    }

    public void setUploadDownLoadState(final String transferType, final String fileType, final int transferId) {
        // Below sequence of code lines should be maintained
        final TransferObserver transferObserver = AWSClientHelper.getInstance().getTransferUtility().getTransferById(transferId);
        final int position = getMessageViewHolderIndexByMsgId(transferObserver.getKey());

        if (position >= 0) {
            final IMChatMessage chatMessage = mChatMessageList.get(position);
            final MessageViewHolder messageViewHolder = (MessageViewHolder) mRecyclerView.findViewHolderForAdapterPosition(position);

            if (null == messageViewHolder) {
                return;
            }
            if (transferObserver.getState().equals(TransferState.COMPLETED)) {
                if (transferType.equals(IMConstants.TRANSFER_TYPE_UPLOAD)) {
                    if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                        mSentImageHandler.setImageUploadCompleted(messageViewHolder);

                    } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                        mSentVideoHandler.setVideoUploadCompleted(messageViewHolder);
                    }

                } else if (transferType.equals(IMConstants.TRANSFER_TYPE_DOWNLOAD)) {
                    if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                        mReceivedImageHandler.setImageDownloadCompleted(messageViewHolder, chatMessage, position);

                    } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                        mReceivedVideoHandler.setVideoDownloadCompleted(messageViewHolder, chatMessage);
                    }
                }
                mAllMediaTransferIdList.put(transferObserver.getKey(), transferId);

            } else if (transferObserver.getState().equals(TransferState.CANCELED)) {
                if (transferType.equals(IMConstants.TRANSFER_TYPE_UPLOAD)) {
                    if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                        mSentImageHandler.setImageWithUploadBtn(messageViewHolder, chatMessage);

                    } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                        mSentVideoHandler.setVideoWithUploadBtn(messageViewHolder, chatMessage);
                    }

                } else if (transferType.equals(IMConstants.TRANSFER_TYPE_DOWNLOAD)) {
                    if (fileType.equals(IMConstants.FILE_TYPE_IMAGE)) {
                        mReceivedImageHandler.setImageWithDownloadBtn(messageViewHolder, chatMessage);

                    } else if (fileType.equals(IMConstants.FILE_TYPE_VIDEO)) {
                        mReceivedVideoHandler.setVideoWithDownloadBtn(messageViewHolder, chatMessage);
                    }
                }
            }

        }
    }

}


