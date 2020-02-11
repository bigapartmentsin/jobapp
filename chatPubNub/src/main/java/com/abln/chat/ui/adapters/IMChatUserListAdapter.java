package com.abln.chat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.activities.IMChatUserListActivity;
import com.abln.chat.ui.helper.ChatHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import jp.wasabeef.recyclerview.animators.FadeInLeftAnimator;

public class IMChatUserListAdapter extends RecyclerView.Adapter<IMChatUserListAdapter.UserHolder> implements Filterable {
    public static final String TAG = IMChatUserListAdapter.class.getSimpleName();

    private Context mContext;

    public ArrayList<ChatUserDetail> mUserList;
    private ArrayList<ChatUserDetail> mUserListTemp;
    private ArrayList<String> mExistingUserIds;

    private UserItemFilter mUserItemFilter;

    private IMChatUser mLoggedInUser = new IMChatUser();

    private RecyclerView mSelectedUserListRecyclerView;
    private IMChatSelectedUserListAdapter mIMChatSelectedUserListAdapter;


    public IMChatUserListAdapter(Context mContext, RecyclerView selectedUserListRecyclerView, ArrayList<String> exitingUserIds) {
        this.mContext = mContext;
        this.mSelectedUserListRecyclerView = selectedUserListRecyclerView;
        this.mExistingUserIds = exitingUserIds;

        this.mUserItemFilter = new UserItemFilter();
        this.mLoggedInUser = IMInstance.getInstance().getLoggedInUser();

        prepareUserItemDetailsList(new ArrayList<>(IMInstance.getInstance().getIMDatabase().getIMChatUserDao().getAllChatUsers()));
        setSelectedUserListAdapter();
    }

    @Override
    public UserHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.im_item_chat_user_list, null);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(UserHolder holder, final int position) {
        IMChatUser chatUser = mUserListTemp.get(position).chatUser;
        resetOnBindViewHolder(holder);

        holder.tvGroupImage.setVisibility(View.GONE);
        holder.btnAddGroup.setVisibility(View.GONE);

        holder.ivUserImage.setVisibility(View.VISIBLE);
        holder.tvUserTextImage.setVisibility(View.GONE);

        holder.cbUserSelection.setVisibility(View.VISIBLE);
        holder.tvUserOrGroupName.setText(chatUser.userName);

        ChatHelper.setUserImage(chatUser, holder.ivUserImage, holder.tvUserTextImage);

        if (chatUser.isOnline) {
            holder.mUserOnlineBg.setVisibility(View.VISIBLE);

        } else {
            holder.mUserOnlineBg.setVisibility(View.GONE);
        }

        setUserSelectionState(holder, position);
        setOnUserClickListener(holder, position);
        setOnCheckBoxClickListener(holder, position);
    }


    private void resetOnBindViewHolder(UserHolder holder) {
        holder.rlContactListItem.setBackgroundResource(0);
        holder.rlContactListItem.setEnabled(true);
        holder.tvUserOrGroupName.setText("");
        holder.tvGroupImage.setText("");
        holder.btnAddGroup.setVisibility(View.GONE);
        holder.ivUserImage.setImageDrawable(null);
        holder.tvUserTextImage.setVisibility(View.GONE);
        holder.tvUserTextImage.setText("");
        holder.cbUserSelection.setVisibility(View.VISIBLE);
        holder.cbUserSelection.setButtonDrawable(R.drawable.im_bg_checkbox_selector);
        holder.cbUserSelection.setEnabled(true);
        holder.ivUserImage.setAlpha(1.0f);
        holder.tvGroupImage.setAlpha(1.0f);
        holder.tvGroupImage.setBackgroundDrawable(null);
        holder.tvUserOrGroupName.setAlpha(1.0f);
        holder.cbUserSelection.setAlpha(1.0f);
        holder.btnAddGroup.setAlpha(1.0f);
        holder.cbUserSelection.setOnClickListener(null);
        holder.itemView.setOnClickListener(null);
        holder.mUserOnlineBg.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return mUserListTemp.size();
    }

    static class UserHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlContactListItem;
        ImageView ivUserImage;
        TextView tvUserTextImage, tvUserOrGroupName, tvUserProfileOrGroupUserNames, tvGroupImage;
        CheckBox cbUserSelection;
        Button btnAddGroup;
        public View itemView;
        ImageView mUserOnlineBg;

        UserHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            rlContactListItem = (RelativeLayout) itemView.findViewById(R.id.im_rl_contact_list_item);
            ivUserImage = (ImageView) itemView.findViewById(R.id.im_iv_user_image);
            tvUserTextImage = (TextView) itemView.findViewById(R.id.im_tv_user_image);
            tvUserOrGroupName = (TextView) itemView.findViewById(R.id.im_tv_user_name_contact_list_item);
            tvUserProfileOrGroupUserNames = (TextView) itemView.findViewById(R.id.im_tv_user_profile_or_group_username);
            tvGroupImage = (TextView) itemView.findViewById(R.id.im_tv_group_image);
            cbUserSelection = (CheckBox) itemView.findViewById(R.id.im_cb_user_selection);
            btnAddGroup = (Button) itemView.findViewById(R.id.im_btn_add_group);
            mUserOnlineBg = (ImageView) itemView.findViewById(R.id.im_iv_user_profile_online_bg);
        }
    }

    private void setSelectedUserListAdapter() {
        mIMChatSelectedUserListAdapter = new IMChatSelectedUserListAdapter(mContext, this);
        LinearLayoutManager userLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        mSelectedUserListRecyclerView.setLayoutManager(userLayoutManager);
        mSelectedUserListRecyclerView.setItemAnimator(new FadeInLeftAnimator());
        mSelectedUserListRecyclerView.setAdapter(mIMChatSelectedUserListAdapter);
        mSelectedUserListRecyclerView.setVisibility(View.GONE);
        ((IMChatUserListActivity) mContext).showOrHideSelectedUserDivider(false);
    }

    private void setUserSelectionState(UserHolder holder, int position) {
        if (mUserListTemp.get(position).isExitingUser) {
            disableActions(holder);

        } else {


            if (mUserListTemp.get(position).isSelected) {
                holder.cbUserSelection.setChecked(true);

            } else {
                holder.cbUserSelection.setChecked(false);
            }
        }
    }

    private void disableActions(UserHolder holder) {
        holder.rlContactListItem.setEnabled(false);
        holder.cbUserSelection.setButtonDrawable(R.drawable.im_ic_chat_user_select);
        holder.cbUserSelection.setEnabled(false);
        holder.cbUserSelection.setChecked(true);
        holder.ivUserImage.setAlpha(0.4f);
        holder.tvGroupImage.setAlpha(0.4f);
        holder.tvUserOrGroupName.setAlpha(0.4f);
        holder.cbUserSelection.setAlpha(0.4f);
        holder.btnAddGroup.setAlpha(0.4f);
    }

    private void setOnUserClickListener(final UserHolder holder, final int position) {
        holder.itemView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(holder, position);
            }
        });
    }

    private void setOnCheckBoxClickListener(final UserHolder holder, final int position) {
        holder.cbUserSelection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                performClickAction(holder, position);
            }
        });
    }


    private void performClickAction(final UserHolder holder, final int position) {
        if (!mUserListTemp.get(position).isExitingUser && mUserListTemp.get(position).isSelected) {
            holder.cbUserSelection.setChecked(false);
            mUserListTemp.get(position).isSelected = false;

        } else {
            holder.cbUserSelection.setChecked(true);
            mUserListTemp.get(position).isSelected = true;
        }

        // below methods should be called in order only

        setUserSelection(position);
        ShowOrHideSelectedUserBand();
    }

    private void setUserSelection(int position) {
        updateOriginalList(mUserListTemp.get(position));
        mIMChatSelectedUserListAdapter.updateSelectedUsers(getSelectedUserFromBand());
    }

    private void updateOriginalList(ChatUserDetail chatUserDetail) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUserList.get(i).equals(chatUserDetail)) {
                mUserList.get(i).isSelected = chatUserDetail.isSelected;
                mUserList.get(i).isExitingUser = chatUserDetail.isExitingUser;
            }
        }
    }

    private void updateOriginalList(ArrayList<IMChatUser> chatUsersList) {
        for (IMChatUser chatUser : chatUsersList) {
            for (int i = 0; i < mUserList.size(); i++) {
                if (mUserList.get(i).chatUser.userId.equals(chatUser.userId)) {
                    mUserList.get(i).isSelected = true;
                }
            }
        }
    }

    private void updateOriginalListForDeselection(IMChatUser chatUser) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUserList.get(i).chatUser.userId.equals(chatUser.userId)) {
                mUserList.get(i).isSelected = false;
            }
        }
    }


    private void ShowOrHideSelectedUserBand() {
        int count = getSelectedUserFromBand().size();

        if (count > 0) {
            mSelectedUserListRecyclerView.setVisibility(View.VISIBLE);
            ((IMChatUserListActivity) mContext).showOrHideSelectedUserDivider(true);
            ((IMChatUserListActivity) mContext).showOrHideMenuDoneButton(true);

        } else {
            mSelectedUserListRecyclerView.setVisibility(View.GONE);
            ((IMChatUserListActivity) mContext).showOrHideSelectedUserDivider(false);
            ((IMChatUserListActivity) mContext).showOrHideMenuDoneButton(true);
        }
    }

    private ArrayList<ChatUserDetail> getSelectedUserFromBand() {
        ArrayList<ChatUserDetail> userSelectedList = new ArrayList<>();
        for (int i = 0; i < mUserList.size(); i++) {
            if (mUserList.get(i).isSelected && !mUserList.get(i).isExitingUser) {
                userSelectedList.add(mUserList.get(i));
            }
        }

        return userSelectedList;
    }

    void deselectUser(IMChatUser chatUser) {
        for (int i = 0; i < mUserListTemp.size(); i++) {
            if (!mUserListTemp.get(i).isExitingUser && mUserListTemp.get(i).chatUser.userId.equals(chatUser.userId)) {
                mUserListTemp.get(i).isSelected = false;
            }
        }

        //update original list in case of remove user from band
        updateOriginalListForDeselection(chatUser);
        ShowOrHideSelectedUserBand();
        notifyDataSetChanged();
    }

    public Boolean isLoggedInUser(IMChatUser chatUser) {
        boolean bool = false;
        if (null != chatUser) {
            if (chatUser.userId.equals(mLoggedInUser.userId)) {
                bool = true;
            }
        }

        return bool;
    }

    private Boolean isExistingUser(IMChatUser chatUser) {
        boolean bool = false;
        if (null != chatUser) {
            if (null != mExistingUserIds && !mExistingUserIds.isEmpty() && mExistingUserIds.contains(chatUser.userId)) {
                bool = true;
            }
        }
        return bool;
    }

    private void prepareUserItemDetailsList(ArrayList<IMChatUser> chatUserList) {
        ArrayList<ChatUserDetail> list = new ArrayList<>();
        for (IMChatUser chatUser : chatUserList) {
            if (null != chatUser) {
                ChatUserDetail chatUserDetail = new ChatUserDetail();
                chatUserDetail.chatUser = chatUser;

                if (isExistingUser(chatUser)) {
                    chatUserDetail.isSelected = true;
                    chatUserDetail.isExitingUser = true;

                } else {
                    chatUserDetail.isSelected = false;
                    chatUserDetail.isExitingUser = false;
                }
                if (!isLoggedInUser(chatUser)) {
                    list.add(chatUserDetail);
                }
            }
        }

        mUserList = list;
        mUserListTemp = list;
        sortingUserNamesAlphabetically(mUserListTemp);
    }

    private void updateUserList() {
        notifyDataSetChanged();
    }

    public void updateOnlineStatus(String userId, boolean isOnline) {
        int index = 0;
        for (int i = 0; i < mUserListTemp.size(); i++) {
            if (null != mUserListTemp.get(i).chatUser
                    && null != mUserListTemp.get(i).chatUser.userId
                    && mUserListTemp.get(i).chatUser.userId.equalsIgnoreCase(userId)) {

                mUserListTemp.get(i).chatUser.isOnline = isOnline;
                index = i;
                break;
            }
        }

        notifyItemChanged(index);
    }

    public class ChatUserDetail {
        public IMChatUser chatUser;
        public boolean isSelected;
        public boolean isExitingUser;

        @Override
        public boolean equals(Object object) {
            if (object instanceof ChatUserDetail) {
                ChatUserDetail chatUserDetail = (ChatUserDetail) object;

                return (chatUserDetail.chatUser.userId.equals(this.chatUser.userId));

            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return chatUser.userId.hashCode();
        }
    }

    private class UserItemFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ChatUserDetail> filteredList = new ArrayList<>();
            if (constraint.length() == 0) {
                mUserListTemp = mUserList;
                filteredList.addAll(mUserListTemp);

            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();
                filteredList = getFilteredUserList(mUserList, filterPattern);
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mUserListTemp = (ArrayList<ChatUserDetail>) results.values;
            updateUserList();
        }
    }

    @Override
    public Filter getFilter() {
        return mUserItemFilter;
    }

    private ArrayList getFilteredUserList(ArrayList<ChatUserDetail> OriginalList, String filterContent) {
        ArrayList<ChatUserDetail> filteredList = new ArrayList<>();

        for (ChatUserDetail chatUserDetail : OriginalList) {
            IMChatUser chatUser = chatUserDetail.chatUser;
            if (null != chatUser && chatUser.userName.toLowerCase().contains(filterContent)) {
                filteredList.add(chatUserDetail);
            }
        }

        return filteredList;
    }

    private void sortingUserNamesAlphabetically(ArrayList<ChatUserDetail> chatUserDetailList) {
        Collections.sort(chatUserDetailList, new Comparator<ChatUserDetail>() {
            @Override
            public int compare(ChatUserDetail lhs, ChatUserDetail rhs) {
                return lhs.chatUser.userName.toLowerCase().compareTo(rhs.chatUser.userName.toLowerCase());
            }
        });
    }
}
