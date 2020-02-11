package com.abln.chat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.activities.IMChatThreadInfoActivity;
import com.abln.chat.ui.adapters.IMChatThreadInfoMemberAdapter.ChatInfoRecipientHolder;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.utils.IMSwipeEventListener;
import com.abln.chat.utils.IMSwipeEventListener.OnSwipeLayoutCallbacks;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;


public class IMChatThreadInfoMemberAdapter extends RecyclerView.Adapter<ChatInfoRecipientHolder> implements OnSwipeLayoutCallbacks {
    private static final String TAG = IMChatThreadInfoMemberAdapter.class.getSimpleName();

    private Context mContext;
    private int mPreviousSwipePosition = -1;
    private RecyclerView mRecipientsRecyclerView;

    private ArrayList<IMChatUser> mChatRoomMemberList = new ArrayList<>();
    private IMChatUser mLoggedInUser = new IMChatUser();
    private IMChatRoomDetail mIMChatRoomDetail;

    public IMChatThreadInfoMemberAdapter(Context context, RecyclerView recipientsRecyclerView,
                                         IMChatUser loggedInUser, IMChatRoomDetail chatRoomDetail) {
        this.mContext = context;
        this.mRecipientsRecyclerView = recipientsRecyclerView;
        this.mLoggedInUser = loggedInUser;
        this.mIMChatRoomDetail = chatRoomDetail;
    }

    @Override
    public ChatInfoRecipientHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.im_item_chat_thread_info_user, null);
        return new ChatInfoRecipientHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatInfoRecipientHolder chatInfoRecipientHolder, int position) {
        resetChatInfoRecipientHolder(chatInfoRecipientHolder);

        chatInfoRecipientHolder.mUserName.setText(mChatRoomMemberList.get(position).userName);

        ChatHelper.setUserImage(mChatRoomMemberList.get(position), chatInfoRecipientHolder.mUserImg,
                chatInfoRecipientHolder.mUserTextImg);
        setUserOnlineStatus(mChatRoomMemberList.get(position), chatInfoRecipientHolder.mUserOnlineBg);

        if (mChatRoomMemberList.get(position).userId.equals(mIMChatRoomDetail.adminId)) {
            chatInfoRecipientHolder.mAdminUser.setVisibility(View.VISIBLE);
        } else {
            chatInfoRecipientHolder.mAdminUser.setVisibility(View.GONE);
        }

        chatInfoRecipientHolder.swipeLayout.addSwipeListener(new IMSwipeEventListener(position, this));
        enableOrDisableSwipeAction(chatInfoRecipientHolder, position);
        onRecipientDeleteClickListner(chatInfoRecipientHolder, position);
    }

    private void setUserOnlineStatus(IMChatUser chatUser, ImageView userOnlineImg) {
        if (chatUser.isOnline) {
            userOnlineImg.setVisibility(View.VISIBLE);

        } else {
            userOnlineImg.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mChatRoomMemberList.size();
    }

    @Override
    public void closeSwipes() {
        if (mPreviousSwipePosition != -1) {
            for (int i = 0; i <= mChatRoomMemberList.size(); i++) {
                View child = mRecipientsRecyclerView.getChildAt(i);
                if (child != null) {
                    ChatInfoRecipientHolder chatInfoRecipientHolder = (ChatInfoRecipientHolder)
                            mRecipientsRecyclerView.getChildViewHolder(child);
                    chatInfoRecipientHolder.swipeLayout.close();
                }
            }
        }
    }

    @Override
    public void setPreviousPosition(int position) {
        mPreviousSwipePosition = position;
    }

    private void resetChatInfoRecipientHolder(ChatInfoRecipientHolder chatInfoRecipientHolder) {
        chatInfoRecipientHolder.mUserName.setText("");

        chatInfoRecipientHolder.mUserImg.setVisibility(View.VISIBLE);
        chatInfoRecipientHolder.mUserImg.setImageDrawable(null);

        chatInfoRecipientHolder.mUserTextImg.setVisibility(View.GONE);
        chatInfoRecipientHolder.mUserTextImg.setText("");
    }

    private void enableOrDisableSwipeAction(ChatInfoRecipientHolder chatInfoRecipientHolder, int position) {
        if (mIMChatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {

            if (mIMChatRoomDetail.adminId.equals(mLoggedInUser.userId)) {
                if (mIMChatRoomDetail.adminId.equals(mChatRoomMemberList.get(position).userId)) {
                    chatInfoRecipientHolder.swipeLayout.setRightSwipeEnabled(false);

                } else {
                    chatInfoRecipientHolder.swipeLayout.setRightSwipeEnabled(true);
                }

            } else {
                chatInfoRecipientHolder.swipeLayout.setRightSwipeEnabled(false);
            }

        } else {
            chatInfoRecipientHolder.swipeLayout.setRightSwipeEnabled(false);
        }
    }

    private void onRecipientDeleteClickListner(ChatInfoRecipientHolder chatInfoRecipientHolder, final int pos) {

        chatInfoRecipientHolder.mDeleteButtonLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pos < mChatRoomMemberList.size()) {
                    mChatRoomMemberList.remove(pos);
                    notifyDataSetChanged();
                    ((IMChatThreadInfoActivity) mContext).updateParticipantCount(mChatRoomMemberList.size());
                }
            }
        });
    }

    public void setRecipients(ArrayList<IMChatUser> recipientsList) {


        mChatRoomMemberList.clear();
        for (IMChatUser chatUser : recipientsList) {
            if (chatUser.userId.equals(mIMChatRoomDetail.adminId)) {
                mChatRoomMemberList.add(0, chatUser);
            } else {
                mChatRoomMemberList.add(chatUser);
            }
        }
        notifyDataSetChanged();

        ((IMChatThreadInfoActivity) mContext).updateParticipantCount(mChatRoomMemberList.size());


    }

    public void updateOnlineStatus(String userId, boolean isOnline) {


        int index = 0;
        for (int i = 0; i < mChatRoomMemberList.size(); i++) {
            if (mChatRoomMemberList.get(i).userId.equalsIgnoreCase(userId)) {
                mChatRoomMemberList.get(i).isOnline = isOnline;
                index = i;
                break;
            }
        }

        notifyItemChanged(index);
    }

    public ArrayList<String> getAllRecipientsIdList() {


        ArrayList<String> usersIdList = new ArrayList<>();
        for (IMChatUser chatUser : this.mChatRoomMemberList) {
            usersIdList.add(chatUser.userId);
        }
        return usersIdList;
    }

    static class ChatInfoRecipientHolder extends RecyclerView.ViewHolder {


        FrameLayout mUserContainer;
        ImageView mUserImg;
        TextView mUserTextImg;
        ImageView mUserOnlineBg;
        TextView mUserName;
        TextView mAdminUser;
        RelativeLayout mDeleteButtonLayout;
        SwipeLayout swipeLayout;

        ChatInfoRecipientHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.im_sl_chat_info_recipient_layout);
            mUserContainer = (FrameLayout) itemView.findViewById(R.id.im_fl_chat_thread_info_user_profile_layout);
            mUserImg = (ImageView) mUserContainer.findViewById(R.id.im_iv_chat_thread_info_user_profile_img);
            mUserTextImg = (TextView) mUserContainer.findViewById(R.id.im_tv_chat_thread_info_user_profile_img);
            mUserOnlineBg = (ImageView) mUserContainer.findViewById(R.id.im_iv_user_profile_online_bg);
            mUserName = (TextView) itemView.findViewById(R.id.im_tv_chat_thread_user_name);
            mAdminUser = (TextView) itemView.findViewById(R.id.im_tv_chat_thread_admin_user);
            mDeleteButtonLayout = (RelativeLayout) itemView.findViewById(R.id.im_rl_chat_thread_info_recipient_delete);
        }
    }
}

