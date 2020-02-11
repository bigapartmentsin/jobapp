package com.abln.chat.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessage.IMMsgMember;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.ui.adapters.IMChatMessageInfoAdapter.ChatInfoViewHolder;
import com.abln.chat.ui.helper.ChatHelper;
import com.abln.chat.utils.IMUtils;

import androidx.recyclerview.widget.RecyclerView;


public class IMChatMessageInfoAdapter extends RecyclerView.Adapter<ChatInfoViewHolder> {
    public static final String TAG = IMChatMessageInfoAdapter.class.getSimpleName();

    private Context mContext;
    private IMChatMessage mIMChatMessage = null;

    public IMChatMessageInfoAdapter(Context context, IMChatMessage chatMessage) {
        this.mContext = context;
        this.mIMChatMessage = chatMessage;
    }


    static class ChatInfoViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private ImageView ivChatMsgRecipient;
        private TextView tvChatMsgRecipient;
        private TextView tvChatMsgReadTime;
        private TextView tvChatMsgDeliverTime;
        private TextView tvChatMsgUserName;

        ChatInfoViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivChatMsgRecipient = (ImageView) itemView.findViewById(R.id.im_iv_chat_msg_info_recipient);
            tvChatMsgRecipient = (TextView) itemView.findViewById(R.id.im_tv_chat_msg_info_recipient);
            tvChatMsgReadTime = (TextView) itemView.findViewById(R.id.im_tv_chat_msg_info_read_time);
            tvChatMsgDeliverTime = (TextView) itemView.findViewById(R.id.im_tv_chat_msg_info_deliver_time);
            tvChatMsgUserName = (TextView) itemView.findViewById(R.id.im_tv_chat_msg_info_user_name);
        }
    }

    @Override
    public ChatInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View sender = LayoutInflater.from(parent.getContext()).inflate(R.layout.im_item_chat_msg_info, parent, false);
        return new ChatInfoViewHolder(sender);

    }

    @Override
    public void onBindViewHolder(final ChatInfoViewHolder holder, final int position) {
        IMMsgMember msgMember = mIMChatMessage.msgMembers.get(position);

        IMChatUser chatUser = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().getChatUserById(msgMember.msgUserId);
        ChatHelper.setUserImage(chatUser, holder.ivChatMsgRecipient, holder.tvChatMsgRecipient);
        holder.tvChatMsgUserName.setText(chatUser.userName);

        if (0 != msgMember.msgRead) {
            holder.tvChatMsgReadTime.setText(IMUtils.formatPubNubTimeStampWithMonthName(msgMember.msgRead));
        }

        if (0 != msgMember.msgDelivered) {
            holder.tvChatMsgDeliverTime.setText(IMUtils.formatPubNubTimeStampWithMonthName(msgMember.msgDelivered));
        }
    }

    @Override
    public int getItemCount() {
        return mIMChatMessage.msgMembers.size();
    }
}

