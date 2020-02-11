package com.abln.chat.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.activities.IMChatThreadListActivity;
import com.abln.chat.ui.activities.IMChatThreadListActivity.ChatThreadItem;
import com.abln.chat.ui.gcm.IMPushNotificationHelper;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMSwipeEventListener;
import com.abln.chat.utils.IMSwipeEventListener.OnSwipeLayoutCallbacks;
import com.abln.chat.utils.IMUIUtils;
import com.abln.chat.utils.IMUtils;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class IMChatThreadRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements OnSwipeLayoutCallbacks, Filterable {
    public static final String TAG = IMChatThreadRecyclerAdapter.class.getSimpleName();

    private static final int HEADER = 0;
    public static final int CHILD = 1;

    private Context mContext;
    private RecyclerView mChatHistoryRecyclerView;

    public ArrayList<ChatThreadItem> mChatThreadList = new ArrayList<>();
    private ArrayList<ChatThreadItem> mChatThreadListTemp = new ArrayList<>();
    private ChatHistoryItemFilter mChatThreadItemFilter = new ChatHistoryItemFilter();
    private IMChatUser mLoggedInUser = new IMChatUser();

    private int mPreviousSwipePosition = -1;
    public String mSearchFilterPattern = "";
    private boolean mIsAzureApiCalled = false;


    public IMChatThreadRecyclerAdapter(Context context, RecyclerView chatHistoryRecyclerView, IMChatUser loggedInUser) {
        mContext = context;
        mChatHistoryRecyclerView = chatHistoryRecyclerView;
        this.mLoggedInUser = loggedInUser;
    }

    private static class ChatHistoryHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvChatHistoryHeader;

        ChatHistoryHeaderViewHolder(View itemView) {
            super(itemView);
            tvChatHistoryHeader = (TextView) itemView.findViewById(R.id.im_tv_chat_history_header);
        }
    }

    private static class ChatHistoryChildViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        SwipeLayout swipeLayout;
        TextView tvChatUserOrGroupName;
        TextView tvChatContextMetaData;
        ImageView imgChatMessageStatus;
        TextView tvChatMessage;
        TextView tvContextName;
        TextView tvChatDate;
        TextView tvChatNotificationCount;
        TextView tvMuteNotification;
        ImageView imgChatThreadInitiator;
        TextView textImgChatThreadInitiator;
        ImageView imgChatNotificationMute;
        RelativeLayout deleteButtonLayout;
        RelativeLayout muteButtonLayout;
        LinearLayout mChatUserMsgLayout;

        ChatHistoryChildViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.im_sl_chat_history_layout);

            tvChatUserOrGroupName = (TextView) itemView.findViewById(R.id.im_tv_chat_history_user_or_group_name);
            tvContextName = (TextView) itemView.findViewById(R.id.im_tv_chat_history_chat_context);

            tvChatContextMetaData = (TextView) itemView.findViewById(R.id.im_tv_chat_history_chat_context_metadata);
            imgChatMessageStatus = (ImageView) itemView.findViewById(R.id.im_iv_chat_history_message_status);

            tvChatMessage = (TextView) itemView.findViewById(R.id.im_tv_chat_history_message);
            tvChatDate = (TextView) itemView.findViewById(R.id.im_tv_chat_history_date);

            tvMuteNotification = (TextView) itemView.findViewById(R.id.im_tv_chat_history_mute);
            tvChatNotificationCount = (TextView) itemView.findViewById(R.id.im_tv_chat_notification_count);

            imgChatThreadInitiator = (ImageView) itemView.findViewById(R.id.im_iv_item_chat_history_user_profile);
            textImgChatThreadInitiator = (TextView) itemView.findViewById(R.id.im_tv_item_chat_history_user_profile);

            imgChatNotificationMute = (ImageView) itemView.findViewById(R.id.im_iv_chat_notification_mute);
            deleteButtonLayout = (RelativeLayout) itemView.findViewById(R.id.im_rl_chat_history_delete);

            muteButtonLayout = (RelativeLayout) itemView.findViewById(R.id.im_rl_chat_history_mute);
            mChatUserMsgLayout = (LinearLayout) itemView.findViewById(R.id.im_ll_chat_history_msg);

        }
    }

//    @Override
//    public long getItemId(int position) {
//        return mChatThreadListTemp.get(position).chatRoomDetail.roomId;
//    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (type) {
            case HEADER:
                View headerView = inflater.inflate(R.layout.im_item_chat_thread_list_header, parent, false);
                return new ChatHistoryHeaderViewHolder(headerView);

            case CHILD:
                View childView = inflater.inflate(R.layout.im_item_chat_thread_list, parent, false);
                return new ChatHistoryChildViewHolder(childView);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ChatThreadItem chatThreadItem = mChatThreadListTemp.get(position);

        switch (chatThreadItem.type) {
            case HEADER:
                final ChatHistoryHeaderViewHolder chatHistoryHeaderViewHolder = (ChatHistoryHeaderViewHolder) holder;
                chatHistoryHeaderViewHolder.tvChatHistoryHeader.setText(chatThreadItem.headerName);
                break;

            case CHILD:
                ChatHistoryChildViewHolder chatHistoryChildViewHolder = (ChatHistoryChildViewHolder) holder;
                resetChatHistoryChildViewHolder(chatHistoryChildViewHolder);
             //   setChatMessage(chatHistoryChildViewHolder, chatThreadItem);
                setChatHistoryLastMessage(chatHistoryChildViewHolder, chatThreadItem);
                setMessageTime(chatHistoryChildViewHolder, chatThreadItem);
                setChatRoomImageAndName(chatHistoryChildViewHolder, chatThreadItem, position);
                setChatNotificationCount(chatHistoryChildViewHolder, chatThreadItem);
                setMuteUnMuteStatus(chatHistoryChildViewHolder, chatThreadItem);

                setOnMuteChatNotificationItemClickListener(chatHistoryChildViewHolder, chatThreadItem);
                setOnDeleteChatHistoryItemClickListener(chatHistoryChildViewHolder, position);

                setOnHistoryItemClickListener(chatHistoryChildViewHolder, chatHistoryChildViewHolder.swipeLayout, chatThreadItem);
                chatHistoryChildViewHolder.swipeLayout.addSwipeListener(new IMSwipeEventListener(position, this));

                enableOrDisableSwipeAction(chatHistoryChildViewHolder);
                addEmptySpaceAtLastIndex(chatHistoryChildViewHolder, position);

                break;
        }
    }

    private void setChatHistoryLastMessage(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem) {

    }

    private void addEmptySpaceAtLastIndex(ChatHistoryChildViewHolder chatHistoryChildViewHolder, int position) {
        if (position + 1 == getItemCount()) {
            // set bottom margin to 80dp.
            setBottomMargin(chatHistoryChildViewHolder.itemView, (int) (80 * Resources.getSystem().getDisplayMetrics().density));
        } else {
            // reset bottom margin back to zero. (your value may be different)
            setBottomMargin(chatHistoryChildViewHolder.itemView, 0);
        }
    }

    public static void setBottomMargin(View view, int bottomMargin) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, bottomMargin);
            view.requestLayout();
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mChatThreadListTemp.get(position).type;
    }

    @Override
    public int getItemCount() {
        if (mChatThreadListTemp != null)
            return mChatThreadListTemp.size();
        else return 0;
    }

    private void resetChatHistoryChildViewHolder(ChatHistoryChildViewHolder chatHistoryChildViewHolder) {
        chatHistoryChildViewHolder.tvChatContextMetaData.setText("");
        chatHistoryChildViewHolder.tvChatContextMetaData.setVisibility(View.GONE);

        chatHistoryChildViewHolder.tvChatNotificationCount.setText("");
        chatHistoryChildViewHolder.tvChatNotificationCount.setVisibility(View.GONE);

        chatHistoryChildViewHolder.tvChatUserOrGroupName.setText("");
        chatHistoryChildViewHolder.tvContextName.setText("");
        chatHistoryChildViewHolder.tvChatDate.setText("");

        chatHistoryChildViewHolder.tvChatMessage.setText("");
        chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

        chatHistoryChildViewHolder.imgChatNotificationMute.setVisibility(View.GONE);

        chatHistoryChildViewHolder.imgChatThreadInitiator.setVisibility(View.VISIBLE);
        chatHistoryChildViewHolder.imgChatThreadInitiator.setImageDrawable(null);

        chatHistoryChildViewHolder.textImgChatThreadInitiator.setVisibility(View.GONE);
        chatHistoryChildViewHolder.textImgChatThreadInitiator.setText("");

        chatHistoryChildViewHolder.imgChatMessageStatus.setImageResource(0);
    }

    private void setChatMessage(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem) {




        if (!IMUtils.isNullOrEmpty(chatThreadItem.typingStatus)) {
            chatHistoryChildViewHolder.tvChatMessage.setTextColor(mContext.getResources().getColor(R.color.im_mountain_meadow_approx));
            chatHistoryChildViewHolder.imgChatMessageStatus.setVisibility(View.GONE);
            chatHistoryChildViewHolder.tvChatMessage.setText(chatThreadItem.typingStatus);
            chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablePadding(5);

        }

        else if (null != chatThreadItem.chatMessage) {

            String senderName = getLastMsgSenderNameForGroup(chatThreadItem);

            if (chatThreadItem.chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_IMAGE)) {
                chatHistoryChildViewHolder.tvChatMessage.setTextColor(mContext.getResources().getColor(R.color.im_silver_chalice_approx));
                chatHistoryChildViewHolder.tvChatMessage.setText(senderName + mContext.getResources().getString(R.string.im_photo));
                chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablePadding(5);
                chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chat_msg_photo, 0, 0, 0);
                setChatMessageStatus(chatHistoryChildViewHolder, chatThreadItem);

            }

            else if (chatThreadItem.chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) {
                chatHistoryChildViewHolder.tvChatMessage.setTextColor(mContext.getResources().getColor(R.color.im_silver_chalice_approx));
                chatHistoryChildViewHolder.tvChatMessage.setText(senderName + mContext.getResources().getString(R.string.im_video));
                chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablePadding(5);
                chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_chat_msg_photo, 0, 0, 0);
                setChatMessageStatus(chatHistoryChildViewHolder, chatThreadItem);

            }

            else {
                if (isSearchModeOn()) {
                    Spannable spannable = IMUIUtils.getHighlightedSpan(mSearchFilterPattern, chatThreadItem.chatMessage.msgText);
                    CharSequence charSequence = TextUtils.concat(senderName, spannable);
                    chatHistoryChildViewHolder.tvChatMessage.setText(charSequence);
                } else {

                    //TODO crash is happning
                    //
                    chatHistoryChildViewHolder.tvChatMessage.setText(senderName.concat(chatThreadItem.chatMessage.msgText));
                }

                chatHistoryChildViewHolder.tvChatMessage.setTextColor(mContext.getResources().getColor(R.color.im_silver_chalice_approx));
                chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablePadding(5);
                setChatMessageStatus(chatHistoryChildViewHolder, chatThreadItem);
            }
        }



    }

    private void setChatMessageStatus(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem) {

        if (mLoggedInUser != null && chatThreadItem.chatMessage.msgSenderId.equals(mLoggedInUser.userId)) {
            ChatHelper.setSenderChatMsgStatus(chatHistoryChildViewHolder.imgChatMessageStatus, mLoggedInUser.userId, chatThreadItem.chatMessage);

        } else {
            chatHistoryChildViewHolder.imgChatMessageStatus.setVisibility(View.GONE);
        }
    }

    private void setMessageTime(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem) {
        long timeToken = 0;
        if (null != chatThreadItem.chatMessage) {
            if (chatThreadItem.chatMessage != null) {
                timeToken = chatThreadItem.chatMessage.timeToken + IMInstance.getInstance().getDeviceServerDeltaTime();
            }
            chatHistoryChildViewHolder.tvChatDate.setText(IMUtils.formatPubNubTimeStamp(timeToken));
            if (chatThreadItem.chatRoomDetail.roomMetaData.get("contextName") != null &&
                    (chatThreadItem.chatRoomDetail.roomMetaData.get("contextName").length() > 0)
                    && !(chatThreadItem.chatRoomDetail.roomMetaData.get("contextName").equalsIgnoreCase("null"))) {
                chatHistoryChildViewHolder.tvContextName.setVisibility(View.VISIBLE);
                chatHistoryChildViewHolder.tvContextName.setText(chatThreadItem.chatRoomDetail.roomMetaData.get("contextName"));

            } else {
                chatHistoryChildViewHolder.tvContextName.setVisibility(View.GONE);

            }
        } else if (null != chatThreadItem.chatRoomDetail) {
            timeToken = chatThreadItem.chatRoomDetail.timeToken + IMInstance.getInstance().getDeviceServerDeltaTime();
            // This is required if chat thread has been created and there no any chat message on the Chat thread.
            chatHistoryChildViewHolder.tvChatDate.setText(IMUtils.formatPubNubTimeStamp(timeToken));
            if (chatThreadItem.chatRoomDetail.roomMetaData.get("contextName") != null &&
                    (chatThreadItem.chatRoomDetail.roomMetaData.get("contextName").length() > 0)
                    && !(chatThreadItem.chatRoomDetail.roomMetaData.get("contextName").equalsIgnoreCase("null"))) {
                chatHistoryChildViewHolder.tvContextName.setVisibility(View.VISIBLE);
                chatHistoryChildViewHolder.tvContextName.setText(chatThreadItem.chatRoomDetail.roomMetaData.get("contextName"));

            } else {
                chatHistoryChildViewHolder.tvContextName.setVisibility(View.GONE);
            }
        }
    }

    private void setChatRoomImageAndName(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem, int position) {
        if (null == chatThreadItem.chatRoomDetail) {
            return;
        }

        if (chatThreadItem.chatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
            Drawable[] itemDrawables = {mContext.getResources().getDrawable(R.drawable.im_bg_contact_item1),
                    mContext.getResources().getDrawable(R.drawable.im_bg_contact_item2),
                    mContext.getResources().getDrawable(R.drawable.im_bg_contact_item3)};
            chatHistoryChildViewHolder.imgChatThreadInitiator.setImageDrawable(itemDrawables[position % itemDrawables.length]);

        } else {
            ChatHelper.setUserImage(ChatHelper.getOtherUserForOneToOneChatRoom(mLoggedInUser.userId, chatThreadItem.chatRoomDetail),
                    chatHistoryChildViewHolder.imgChatThreadInitiator, chatHistoryChildViewHolder.textImgChatThreadInitiator);
        }

        String chatRoomName = ChatHelper.getChatRoomName(mLoggedInUser, chatThreadItem.chatRoomDetail);

        if (isSearchModeOn()) {
            chatHistoryChildViewHolder.tvChatUserOrGroupName.setText(IMUIUtils.getHighlightedSpan(mSearchFilterPattern, chatRoomName));
            chatHistoryChildViewHolder.tvContextName.setText(IMUIUtils.getHighlightedSpan(mSearchFilterPattern, chatThreadItem.chatRoomDetail.roomMetaData.get("contextName")));

        } else {
            chatHistoryChildViewHolder.tvChatUserOrGroupName.setText(chatRoomName);
        }

    }

    private void setChatNotificationCount(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem) {
        if (null == chatThreadItem.chatRoomDetail) {
            return;
        }
        long notificationCount = 0;
        long readCount = 0;
        long totalCount = 0;

        if (mIsAzureApiCalled ||
                ((IMUtils.getChannelsSyncTable() != null) &&
                        (IMUtils.getChannelsSyncTable().get(chatThreadItem.chatRoomDetail.roomId) != null) &&
                        !IMUtils.getChannelsSyncTable().get(chatThreadItem.chatRoomDetail.roomId))) {
            for (int i = 0; i < chatThreadItem.chatRoomDetail.roomMembers.size(); i++) {
                if (chatThreadItem.chatRoomDetail.roomMembers.get(i).getUserId().equalsIgnoreCase(mLoggedInUser.userId) &&
                        chatThreadItem.chatRoomDetail.roomMembers.get(i).getTotalCount() != null &&
                        chatThreadItem.chatRoomDetail.roomMembers.get(i).getTotalCount() != null) {

                    readCount = chatThreadItem.chatRoomDetail.roomMembers.get(i).getReadCount();
                    totalCount = chatThreadItem.chatRoomDetail.roomMembers.get(i).getTotalCount();
                    chatHistoryChildViewHolder.tvChatMessage.setText(chatThreadItem.chatRoomDetail.roomMembers.get(i).getLastMessage());
                    break;
                }
            }
            notificationCount = totalCount - readCount;

            chatHistoryChildViewHolder.tvChatMessage.setTextColor(mContext.getResources().getColor(R.color.im_silver_chalice_approx));
            chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            chatHistoryChildViewHolder.tvChatMessage.setCompoundDrawablePadding(5);
            // setChatMessageStatus(chatHistoryChildViewHolder, chatThreadItem);


        } else {

            notificationCount = IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().
                    getUnSeenChatMessageCountByRoomId(chatThreadItem.chatRoomDetail.roomId);
        }

        if (notificationCount > 0) {
            chatHistoryChildViewHolder.tvChatNotificationCount.setVisibility(View.VISIBLE);
            chatHistoryChildViewHolder.tvChatNotificationCount.setText(String.valueOf(notificationCount));

        } else {
            chatHistoryChildViewHolder.tvChatNotificationCount.setVisibility(View.GONE);
        }
        mIsAzureApiCalled = false;
    }

    private void setMuteUnMuteStatus(ChatHistoryChildViewHolder chatHistoryChildViewHolder, ChatThreadItem chatThreadItem) {
        if (null == chatThreadItem.chatRoomDetail) {
            return;
        }

        if (chatThreadItem.chatRoomDetail.isChatRoomMuted) {
            muteChatThread(chatHistoryChildViewHolder);
        } else {
            umMuteChatThread(chatHistoryChildViewHolder);
        }
    }

    private void setOnHistoryItemClickListener(final ChatHistoryChildViewHolder holder,
                                               final SwipeLayout swipeLayout, final ChatThreadItem chatThreadItem) {
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SwipeLayout.Status.Close == swipeLayout.getOpenStatus()) {
                    if (isSearchModeOn() && chatThreadItem.headerName.equalsIgnoreCase("MESSAGES"))
                        ChatHelper.openChatWindowForSearchChatRoom(mContext, chatThreadItem.chatRoomDetail.roomId, chatThreadItem.chatRoomDetail.roomMetaData.get("contextName"), chatThreadItem.chatMessage.msgId);
                    else
                        ChatHelper.openChatWindowForExistingChatRoom(mContext, chatThreadItem.chatRoomDetail.roomId, chatThreadItem.chatRoomDetail.roomMetaData.get("contextName"));
                }
                hideSoftKeyboard((Activity) mContext);
            }
        });
    }

    public static void hideSoftKeyboard(Activity activity) {//TODO crashing in null for getWindowToken()
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setOnDeleteChatHistoryItemClickListener(ChatHistoryChildViewHolder chatHistoryViewHolder, final int pos) {
        chatHistoryViewHolder.deleteButtonLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                IMPushNotificationHelper.cancelNotification(mContext);

                String chatRoomId = mChatThreadList.get(pos).chatRoomDetail.roomId;
                IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().updateChatRoomDeleteStatus(chatRoomId, true);
                IMInstance.getInstance().getIMDatabase().getIMChatMessageDao().deleteChatMessagesByRoomId(chatRoomId);
                mChatThreadList.remove(pos);
                mChatThreadListTemp.remove(pos);
                notifyDataSetChanged();

                if (mChatThreadList.size() == 0 || mChatThreadListTemp.size() == 0) {
                    //((IMChatThreadListActivity) mContext).showNoChatHistoryMsg();//TODO crash
                }
            }
        });
    }

    private String getLastMsgSenderNameForGroup(ChatThreadItem chatThreadItem) {
        String senderName = "";
        if (mLoggedInUser != null && chatThreadItem.chatMessage != null &&
                !chatThreadItem.chatMessage.msgSenderId.equalsIgnoreCase(mLoggedInUser.userId)
                && !chatThreadItem.chatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
            senderName = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                    getChatUserById(chatThreadItem.chatMessage.msgSenderId).userName + ": ";
        }

        return senderName;
    }

    private void enableOrDisableSwipeAction(ChatHistoryChildViewHolder chatHistoryViewHolder) {
        if (mSearchFilterPattern.length() < 3) {
            chatHistoryViewHolder.swipeLayout.setRightSwipeEnabled(true);
        } else {
            chatHistoryViewHolder.swipeLayout.setRightSwipeEnabled(false);
        }
    }

    private void setOnMuteChatNotificationItemClickListener(final ChatHistoryChildViewHolder chatHistoryChildViewHolder,
                                                            final ChatThreadItem chatThreadItem) {
        if (null == chatThreadItem.chatRoomDetail) {
            return;
        }
        chatHistoryChildViewHolder.muteButtonLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (chatThreadItem.chatRoomDetail.isChatRoomMuted) {
                    umMuteChatThread(chatHistoryChildViewHolder);
                    chatThreadItem.chatRoomDetail.isChatRoomMuted = false;
                    IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().
                            updateChatRoomMuteStatus(chatThreadItem.chatRoomDetail.roomId, false);

                } else {
                    muteChatThread(chatHistoryChildViewHolder);
                    chatThreadItem.chatRoomDetail.isChatRoomMuted = true;
                    IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().
                            updateChatRoomMuteStatus(chatThreadItem.chatRoomDetail.roomId, true);
                }
            }
        });
    }

    private void muteChatThread(ChatHistoryChildViewHolder chatHistoryChildViewHolder) {
        chatHistoryChildViewHolder.imgChatNotificationMute.setVisibility(View.VISIBLE);
        chatHistoryChildViewHolder.tvMuteNotification.setText(R.string.im_unmute);
        chatHistoryChildViewHolder.tvMuteNotification.setCompoundDrawablesWithIntrinsicBounds(null,
                mContext.getResources().getDrawable(R.drawable.im_ic_swipe_unmute), null, null);
    }

    private void umMuteChatThread(ChatHistoryChildViewHolder chatHistoryChildViewHolder) {
        chatHistoryChildViewHolder.imgChatNotificationMute.setVisibility(View.GONE);
        chatHistoryChildViewHolder.tvMuteNotification.setText(R.string.im_mute);
        chatHistoryChildViewHolder.tvMuteNotification.setCompoundDrawablesWithIntrinsicBounds(null,
                mContext.getResources().getDrawable(R.drawable.im_ic_swipe_mute), null, null);
    }

    public void addChatHistoryList(ArrayList<ChatThreadItem> chatHistoryList, boolean isAzureApiCalled) {
        mIsAzureApiCalled = isAzureApiCalled;
        mChatThreadList.clear();
        mChatThreadListTemp.clear();

        mChatThreadList.addAll(chatHistoryList);
        mChatThreadListTemp.addAll(chatHistoryList);

        notifyDataSetChanged();
    }

    public void updateNotificationLastMessage() {


    }

    public void updateTypingStatus(String roomId, String userId, boolean isTyping) {
        IMChatRoomMember chatRoomMember = new IMChatRoomMember();
        chatRoomMember.userId = mLoggedInUser.userId;

        for (ChatThreadItem chatThreadItem : mChatThreadListTemp) {
            if (null != chatThreadItem.chatRoomDetail && chatThreadItem.chatRoomDetail.roomId.equals(roomId)
                    && chatThreadItem.chatRoomDetail.roomMembers.contains(chatRoomMember)) {

                if (isTyping) {
                    IMChatUser chatUser = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().getChatUserById(userId);
                    chatThreadItem.typingStatus = chatUser.userName + " " + mContext.getResources().getString(R.string.im_chat_typing_status);

                } else {
                    chatThreadItem.typingStatus = "";
                }
            }
        }

        notifyDataSetChanged();
    }

    private void updateChaHistoryList() {
        notifyDataSetChanged();
    }

    private boolean isSearchModeOn() {
        return (!IMUtils.isNullOrEmpty(mSearchFilterPattern) && mSearchFilterPattern.length() >= 3);
    }

    private class ChatHistoryItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ChatThreadItem> filteredList = new ArrayList<>();
            mSearchFilterPattern = constraint.toString().toLowerCase().trim();
            if (mSearchFilterPattern.length() < 3) {
                mChatThreadListTemp = mChatThreadList;
                filteredList.addAll(mChatThreadListTemp);

            } else {
                filteredList = getFilteredChatHistoryItemList(mSearchFilterPattern);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mChatThreadListTemp = (ArrayList<ChatThreadItem>) results.values;
            updateChaHistoryList();
        }

    }

    @Override
    public void closeSwipes() {
        if (mPreviousSwipePosition != -1) {
            for (int i = 0; i <= mChatHistoryRecyclerView.getChildCount(); i++) {

                View child = mChatHistoryRecyclerView.getChildAt(i);
                if (child != null) {
                    ChatHistoryChildViewHolder chatHistoryChildViewHolder = (ChatHistoryChildViewHolder) mChatHistoryRecyclerView.getChildViewHolder(child);
                    chatHistoryChildViewHolder.swipeLayout.close(true);
                }
            }
        }
    }

    @Override
    public void setPreviousPosition(int position) {
        mPreviousSwipePosition = position;
    }

    @Override
    public Filter getFilter() {
        return mChatThreadItemFilter;
    }

    private ArrayList<ChatThreadItem> getFilteredChatHistoryItemList(String filterContent) {
        ArrayList<ChatThreadItem> filteredList = new ArrayList<>();

        filteredList.addAll(getFilteredChatHistoryItemsForGroupNameTxt(filterContent));
        filteredList.addAll(getFilteredChatHistoryItemsForMessageText(filterContent));
        filteredList.addAll(getFilteredChatHistoryItemsForContextNameTxt(filterContent));

        return filteredList;
    }

    private ArrayList<ChatThreadItem> getFilteredChatHistoryItemsForGroupNameTxt(String filterContent) {
        ArrayList<ChatThreadItem> filteredList = new ArrayList<>();

        for (ChatThreadItem chatThreadItem : mChatThreadList) {
            if (null != chatThreadItem.chatRoomDetail && chatThreadItem.chatRoomDetail.
                    roomName.toLowerCase().contains(filterContent.toLowerCase())) {
                filteredList.add(chatThreadItem);
            }
        }

        if (filteredList.size() > 0) {
            ChatThreadItem chatThreadItem = new ChatThreadItem();
            chatThreadItem.type = IMChatThreadRecyclerAdapter.HEADER;
            chatThreadItem.headerName = mContext.getResources().getString(R.string.im_name);
            filteredList.add(0, chatThreadItem);
        }

        return filteredList;
    }


    private ArrayList<ChatThreadItem> getFilteredChatHistoryItemsForContextNameTxt(String filterContent) {
        ArrayList<ChatThreadItem> filteredList = new ArrayList<>();

        for (ChatThreadItem chatThreadItem : mChatThreadList) {
            if (null != chatThreadItem.chatRoomDetail && chatThreadItem.chatRoomDetail != null && chatThreadItem.chatRoomDetail.roomMetaData != null && chatThreadItem.chatRoomDetail.roomMetaData.get("contextName")
                    .toLowerCase().contains(filterContent.toLowerCase())) {
                filteredList.add(chatThreadItem);
            }
        }

        if (filteredList.size() > 0) {
            ChatThreadItem chatThreadItem = new ChatThreadItem();
            chatThreadItem.type = IMChatThreadRecyclerAdapter.HEADER;
            chatThreadItem.headerName = mContext.getResources().getString(R.string.im_context_name);
            filteredList.add(0, chatThreadItem);
        }

        return filteredList;
    }

    private ArrayList<ChatThreadItem> getFilteredChatHistoryItemsForMessageText(String filterContent) {
        ArrayList<ChatThreadItem> filteredList = new ArrayList<>();

        StringBuilder availableRoomsBuilder = new StringBuilder();
        for (ChatThreadItem chatThreadItem : mChatThreadList) {
            if (null != chatThreadItem.chatRoomDetail) {
                availableRoomsBuilder.append(chatThreadItem.chatRoomDetail.roomId + ",");
            }
        }

        String availableRooms = availableRoomsBuilder.toString();

        ArrayList<IMChatMessage> chatMessagesList = new ArrayList<>(IMInstance.getInstance().getIMDatabase().
                getIMChatMessageDao().getChatMessageBySearchText("%" + filterContent + "%"));

        for (IMChatMessage chatMessage : chatMessagesList) {
            if (availableRooms.contains(chatMessage.roomId + ",")) {
                ChatThreadItem chatThreadItem = new ChatThreadItem();
                chatThreadItem.type = IMChatThreadRecyclerAdapter.CHILD;
                chatThreadItem.headerName = mContext.getResources().getString(R.string.im_messages);
                chatThreadItem.chatRoomDetail = IMInstance.getInstance().getIMDatabase().
                        getIMChatRoomDetailDao().getChatRoomDetail(chatMessage.roomId);
                chatThreadItem.chatMessage = chatMessage;
                filteredList.add(chatThreadItem);
            }
        }

        if (filteredList.size() > 0) {
            ChatThreadItem chatThreadItem = new ChatThreadItem();
            chatThreadItem.type = IMChatThreadRecyclerAdapter.HEADER;
            chatThreadItem.headerName = mContext.getResources().getString(R.string.im_messages);
            filteredList.add(0, chatThreadItem);
        }


        return filteredList;
    }
}

