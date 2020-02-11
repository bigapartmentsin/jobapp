package com.abln.chat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abln.chat.R;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.adapters.IMChatSelectedUserListAdapter.SelectedUserViewHolder;
import com.abln.chat.ui.adapters.IMChatUserListAdapter.ChatUserDetail;
import com.abln.chat.ui.helper.ChatHelper;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class IMChatSelectedUserListAdapter extends RecyclerView.Adapter<SelectedUserViewHolder> {
    private static final String TAG = IMChatSelectedUserListAdapter.class.getSimpleName();

    private Context mContext;
    private IMChatUserListAdapter mIMChatUserListAdapter;
    private ArrayList<IMChatUser> mSelectedUserList = new ArrayList<>();

    IMChatSelectedUserListAdapter(Context context, IMChatUserListAdapter chatUserListAdapter) {
        this.mContext = context;
        this.mIMChatUserListAdapter = chatUserListAdapter;
    }

    static class SelectedUserViewHolder extends RecyclerView.ViewHolder {
        ImageView mUserImg;
        TextView mUserTextImg;
        ImageView mUserCloseBg;
        TextView mSelectedUserName;

        SelectedUserViewHolder(View itemView) {
            super(itemView);
            mUserImg = (ImageView) itemView.findViewById(R.id.im_iv_selected_user_img);
            mUserTextImg = (TextView) itemView.findViewById(R.id.im_tv_selected_user_img);
            mUserCloseBg = (ImageView) itemView.findViewById(R.id.im_iv_selected_user_close_bg);
            mSelectedUserName = (TextView) itemView.findViewById(R.id.im_tv_selected_user_name);
        }
    }

    @Override
    public SelectedUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.im_item_selected_user, null);
        return new SelectedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SelectedUserViewHolder holder, int position) {
        holder.mUserImg.setTag(position);
        ChatHelper.setUserImage(mSelectedUserList.get(position), holder.mUserImg, holder.mUserTextImg);

        holder.mSelectedUserName.setText(mSelectedUserList.get(position).userName);
        setOnCloseBgClickListener(holder, position);
    }


    @Override
    public int getItemCount() {
        return mSelectedUserList.size();
    }

    private void setOnCloseBgClickListener(SelectedUserViewHolder holder, final int position) {
        holder.mUserCloseBg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeUser(position);
            }
        });
    }

    void updateSelectedUsers(ArrayList<ChatUserDetail> chatUserDetailList) {
        mSelectedUserList.clear();
        for (ChatUserDetail chatUserDetail : chatUserDetailList) {
            if (!chatUserDetail.isExitingUser && chatUserDetail.isSelected) {
                mSelectedUserList.add(chatUserDetail.chatUser);
            }
        }

        notifyDataSetChanged();
    }

    private void removeUser(int position) {
        mIMChatUserListAdapter.deselectUser(mSelectedUserList.get(position));
        mSelectedUserList.remove(position);
        notifyDataSetChanged();
    }
}

