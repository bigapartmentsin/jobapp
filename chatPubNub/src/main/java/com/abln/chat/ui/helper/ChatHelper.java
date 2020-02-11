package com.abln.chat.ui.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatMessage.IMMsgMember;
import com.abln.chat.core.model.IMChatMessageStatus;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.core.model.IMChatRoomDetailUpdate;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMConstants;
import com.abln.chat.ui.activities.IMChatActivity;
import com.abln.chat.ui.activities.IMChatMessageInfoActivity;
import com.abln.chat.ui.activities.IMChatUserListActivity;
import com.abln.chat.utils.IMUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;

import androidx.appcompat.app.AppCompatActivity;


public class ChatHelper {
    public static ArrayList<IMChatRoomMember> getChatRoomMemberList(ArrayList<IMChatUser> chatUsers,
                                                                    IMChatUser loggedInUser) {
        ArrayList<IMChatRoomMember> chatRoomMembers = new ArrayList<>();
        for (IMChatUser chatUser : chatUsers) {
            chatRoomMembers.add(getChatRoomMember(chatUser));
        }

        IMChatRoomMember loggedInMember = new IMChatRoomMember();
        loggedInMember.userId = loggedInUser.userId;
        if (!chatRoomMembers.contains(loggedInMember)) {
            chatRoomMembers.add(getChatRoomMember(loggedInUser));
        }

        return chatRoomMembers;
    }

    public static IMChatRoomMember getChatRoomMember(IMChatUser chatUser) {
        IMChatRoomMember chatRoomMember = new IMChatRoomMember();
        chatRoomMember.userId = chatUser.userId;
        chatRoomMember.isDeleted = false;
        chatRoomMember.isMute = false;
        chatRoomMember.lastUpdatedTime = 0;
        return chatRoomMember;
    }

    public static ArrayList<IMChatUser> getChatRoomMemberList(IMChatRoomDetail chatRoomDetail) {
        ArrayList<String> userIdList = new ArrayList<>();
        if (null != chatRoomDetail && null != chatRoomDetail.roomMembers) {
            for (IMChatRoomMember chatRoomMember : chatRoomDetail.roomMembers) {
                userIdList.add(chatRoomMember.userId);
            }
        }

        return IMInstance.getInstance().getIMDatabase().getIMChatUserDao().getChatUserListByUserIds(userIdList);
    }

    public static ArrayList<String> getChatRoomMemberIdList(IMChatRoomDetail chatRoomDetail) {
        ArrayList<String> userIdList = new ArrayList<>();
        if (null != chatRoomDetail && null != chatRoomDetail.roomMembers) {
            for (IMChatRoomMember chatRoomMember : chatRoomDetail.roomMembers) {
                userIdList.add(chatRoomMember.userId);
            }
        }

        return userIdList;
    }




    public static ArrayList<IMMsgMember> getChatMessageMembers(IMChatRoomDetail chatRoomDetail) {
        ArrayList<IMMsgMember> msgMemberList = new ArrayList<>();
        if (null != chatRoomDetail && null != chatRoomDetail.roomMembers) {
            for (IMChatRoomMember chatRoomMember : chatRoomDetail.roomMembers) {
                IMMsgMember msgMember = new IMMsgMember();
                msgMember.msgUserId = chatRoomMember.userId;
                msgMember.msgDelivered = 0;
                msgMember.msgRead = 0;
                msgMemberList.add(msgMember);
            }
        }

        return msgMemberList;
    }

    public static String getChatRoomMemberIdsToPublishUpdatedChatRoomDetail(ArrayList<IMChatRoomMember> updatedChatRoomMembers,
                                                                            ArrayList<IMChatRoomMember> existingChatRoomMembers,
                                                                            String loggedInUserId) {

        ArrayList<String> allMemberIdList = new ArrayList<>();
        for (IMChatRoomMember chatRoomMember : updatedChatRoomMembers) {
            allMemberIdList.add(chatRoomMember.userId);
        }

        for (IMChatRoomMember chatRoomMember : existingChatRoomMembers) {
            allMemberIdList.add(chatRoomMember.userId);
        }

        LinkedHashSet<String> chatUserSet = new LinkedHashSet<>(allMemberIdList);
        ArrayList<String> chatUserIdList = new ArrayList<>(chatUserSet);

        StringBuilder chatUserIds = new StringBuilder();
        for (int i = 0; i < chatUserIdList.size(); i++) {
            if (!chatUserIdList.get(i).equals(loggedInUserId)) {
                chatUserIds.append(chatUserIdList.get(i));
                if (i < chatUserIdList.size() - 1) {
                    chatUserIds.append(",");
                }
            }
        }
        return chatUserIds.toString();
    }

    public static IMChatRoomDetailUpdate getChatRoomDetailUpdateMessage(String loggedInUserId, String chatRoomName,
                                                                        IMChatRoomDetail chatRoomDetail) {
        IMChatRoomDetailUpdate chatRoomDetailUpdate = new IMChatRoomDetailUpdate();
        chatRoomDetailUpdate.origin = chatRoomDetail.origin;
        chatRoomDetailUpdate.msgVersion = chatRoomDetail.msgVersion;
        chatRoomDetailUpdate.timeToken = chatRoomDetail.timeToken;
        chatRoomDetailUpdate.roomId = chatRoomDetail.roomId;
        chatRoomDetailUpdate.senderId = loggedInUserId;
        chatRoomDetailUpdate.roomName = chatRoomName;
        chatRoomDetailUpdate.timeToken = IMUtils.getDeviceTimeInMillis()
                - IMInstance.getInstance().getDeviceServerDeltaTime();
        chatRoomDetailUpdate.msgType = IMConstants.CHAT_MESSAGE_TYPE_ROOM_NAME_UPDATE;
        return chatRoomDetailUpdate;
    }

    public static String getChatRoomName(IMChatUser loggedInUser, IMChatRoomDetail chatRoomDetail) {
        String chatRoomName = "";
        try {
            if (null == chatRoomDetail.roomId) {
                chatRoomName = getChatRoomMembersNameString(chatRoomDetail);

            } else if (!IMUtils.isNullOrEmpty(chatRoomDetail.roomName)) {
                chatRoomName = chatRoomDetail.roomName;

            } else if (null != chatRoomDetail.roomType && chatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
                chatRoomName = getChatRoomMembersNameString(chatRoomDetail);

            } else if (null != chatRoomDetail.roomType && chatRoomDetail.roomType.equalsIgnoreCase(IMConstants.CHAT_ROOM_TYPE_ONE_TO_ONE)) {
                chatRoomName = getOtherUserForOneToOneChatRoom(loggedInUser.userId, chatRoomDetail).userName;

            } else {
                chatRoomName = loggedInUser.userName;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chatRoomName;
    }

    public static String getChatRoomMembersNameString(IMChatRoomDetail chatRoomDetail) {
        ArrayList<IMChatUser> chatUserList = getChatRoomMemberList(chatRoomDetail);

        ArrayList<String> userNameList = new ArrayList<>();
        for (IMChatUser chatUser : chatUserList) {
            if (chatUser.userId.equals(chatRoomDetail.adminId)) {
                userNameList.add(0, chatUser.userName);

            } else {
                userNameList.add(chatUser.userName);
            }
        }

        return IMUtils.joinString(userNameList);
    }

    public static IMChatUser getOtherUserForOneToOneChatRoom(String loggedInUserId, IMChatRoomDetail chatRoomDetail) {
        ArrayList<IMChatRoomMember> chatRoomMemberList = chatRoomDetail.roomMembers;

        String otherUserId;
        if (chatRoomMemberList.get(0).userId.equals(loggedInUserId)) {
            otherUserId = chatRoomMemberList.get(1).userId;

        } else {
            otherUserId = chatRoomMemberList.get(0).userId;
        }

        return IMInstance.getInstance().getIMDatabase().getIMChatUserDao().getChatUserById(otherUserId);
    }

    public static String getRoomIdForContextualOneToOneChatThread(String loggedInUserId, String userId, String contextId) {
        String[] roomIdArray = {loggedInUserId, userId};
        Arrays.sort(roomIdArray);
        return contextId + "-" + roomIdArray[0] + "-" + roomIdArray[1];
    }

    public static String getRoomIdForOneToOneChatThread(String loggedInUserId, String userId) {
        String[] roomIdArray = {loggedInUserId, userId};
        Arrays.sort(roomIdArray);
        return roomIdArray[0] + "-" + roomIdArray[1];
    }

    public static ArrayList<IMChatMessage> getUserSpecificMessageHistory(ArrayList<IMChatMessage> chatMessageList,
                                                                         String loggedInUserId) {
        ArrayList<IMChatMessage> list = new ArrayList<>();
        for (IMChatMessage chatMessage : chatMessageList) {
            for (IMMsgMember msgMember : chatMessage.msgMembers) {
                if (msgMember.msgUserId.equals(loggedInUserId)) {
                    list.add(chatMessage);
                    break;
                }
            }
        }
        return list;
    }

    public static boolean isMultiMediaMessage(IMChatMessage chatMessage) {
        return (chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_IMAGE) ||
                chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_VIDEO)) ||
                chatMessage.msgType.equalsIgnoreCase(IMConstants.CHAT_MESSAGE_TYPE_AUDIO);
    }

    public static void setSenderChatMsgStatus(ImageView imageView, String loggedInUserId, IMChatMessage chatMessage) {
        if (isReadByAllChatUser(loggedInUserId, chatMessage.msgMembers)) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.im_ic_tick_read);

        } else if (isDeliveredToAllChatUser(loggedInUserId, chatMessage.msgMembers)) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.im_ic_tick_delivered);

        } else if (chatMessage.isChatMsgSent) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.im_ic_tick_sent);

        } else {
            imageView.setVisibility(View.GONE);
        }
    }

    public static void publishPendingDeliveredAndReadStatusOfMessages(ArrayList<IMChatMessage> messageList, String loggedInUserId) {
        for (IMChatMessage chatMessage : messageList) {
            if (!chatMessage.msgSenderId.equals(loggedInUserId)) {

                for (IMMsgMember msgMember : chatMessage.msgMembers) {
                    if (msgMember.msgUserId.equals(loggedInUserId)) {

                        if (msgMember.msgDelivered == 0) {
                            // Publishing delivery status of the received message
                            IMInstance.getInstance().publishDeliveredOrReadStatus(chatMessage, false);
                        }

                        if (msgMember.msgRead == 0) {
                            // Publishing read status of the received message
                            IMInstance.getInstance().publishDeliveredOrReadStatus(chatMessage, true);
                        }

                        break;
                    }
                }
            }
        }
    }

    public static IMChatMessage getChatMessageWithUpdatedReadAndDeliveryStatus(IMChatMessageStatus chatMessageStatus) {
        IMChatMessage chatMessage = IMInstance.getInstance().getIMDatabase().
                getIMChatMessageDao().getChatMessageByMsgId(chatMessageStatus.msgId);
        if(chatMessage!=null) {
            for (IMMsgMember msgMember : chatMessage.msgMembers) {
                if (msgMember.msgUserId.equals(chatMessageStatus.msgSenderId)) {
                    if (chatMessageStatus.msgStatus.equals(IMConstants.CHAT_MESSAGE_DELIVERED_STATUS)) {
                        if (chatMessageStatus.timeToken > 0) {
                            msgMember.msgDelivered = chatMessageStatus.timeToken;
                        }
                    } else if (chatMessageStatus.msgStatus.equals(IMConstants.CHAT_MESSAGE_READ_STATUS)) {
                        if (chatMessageStatus.timeToken > 0) {
                            msgMember.msgRead = chatMessageStatus.timeToken;
                        }
                    }
                }
            }
        }

        return chatMessage;
    }

    public static boolean isDeliveredToAllChatUser(String loggedInUserId, ArrayList<IMMsgMember> chatMsgMembers) {
        boolean bool = true;
        for (IMMsgMember msgMember : chatMsgMembers) {
            if (!msgMember.msgUserId.equals(loggedInUserId) && msgMember.msgDelivered == 0) {
                bool = false;
                break;
            }
        }
        return bool;
    }

    public static boolean isReadByAllChatUser(String loggedInUserId, ArrayList<IMMsgMember> chatMsgMembers) {
        boolean bool = true;
        for (IMMsgMember msgMember : chatMsgMembers) {
            if (!msgMember.msgUserId.equals(loggedInUserId) && msgMember.msgRead == 0) {
                bool = false;
                break;
            }
        }
        return bool;
    }

    public static void setUserImage(IMChatUser chatUser, ImageView imageView, TextView textView) {
        if (null == chatUser) {
            return;
        }

        if (null != chatUser.userThumbnailBytes) {
            byte[] bitmapBytes = chatUser.userThumbnailBytes;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            ImageAware imageAware = new ImageViewAware(imageView);
            new RoundedBitmapDisplayer(1000).display(bitmap, imageAware, null);

        } else if (null != chatUser.userThumbnailURL) {
            DisplayImageOptions displayImageOptions = IMUtils.getRoundedImageDisplayOptions(R.drawable.im_ic_default_user_profile);
            ImageLoader.getInstance().displayImage(chatUser.userThumbnailURL, imageView, displayImageOptions);

        } else {
            imageView.setImageResource(R.drawable.im_ic_default_user_profile);
            textView.setVisibility(View.VISIBLE);
            textView.setTextSize(16);
            textView.setText(getFirstTwoCharacters(chatUser.userName));
            textView.setBackground(null);
        }
    }

    //chat on main thread

    //TODO add new users on mail thread ;
    //TODO show datauser here

    public static void doLaunchNewChatThread(Context context, ArrayList<String> userIdList, String mContextName, String mContextId) {
        Intent intent = new Intent(context, IMChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IMConstants.ADD_USER_ID_LIST, userIdList);
        bundle.putString(IMConstants.CHAT_CONTEXT_ID, mContextId);
        bundle.putString(IMConstants.CHAT_CONTEXT_NAME, mContextName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void doLaunchNewChatThread(Context context, ArrayList<String> userIdList) {
        Intent intent = new Intent(context, IMChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        System.out.println("get new id of the user "+userIdList);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(IMConstants.ADD_USER_ID_LIST, userIdList);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openChatWindowForExistingChatRoom(Context context, String chatRoomId, String contextName) {
        Intent intent = new Intent(context, IMChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IMConstants.CHAT_ROOM_ID, chatRoomId);
        bundle.putString(IMConstants.CHAT_CONTEXT_NAME, contextName);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openChatWindowForSearchChatRoom(Context context, String chatRoomId, String contextName, String msgId) {
        Intent intent = new Intent(context, IMChatActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IMConstants.CHAT_ROOM_ID, chatRoomId);
        bundle.putString(IMConstants.CHAT_CONTEXT_NAME, contextName);
        bundle.putString(IMConstants.CHAT_MSG_ID, msgId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openChatUserListActivity(Context context, String mContextName, String mContextId) {
        Intent intent = new Intent(context, IMChatUserListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IMConstants.CHAT_CONTEXT_NAME, mContextName);
        bundle.putString(IMConstants.CHAT_CONTEXT_ID, mContextId);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openChatUserListActivity(Context context) {
        Intent intent = new Intent(context, IMChatUserListActivity.class);
        Bundle bundle = new Bundle();
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public static void openChatUserListActivityToAddUser(AppCompatActivity appCompatActivity,
                                                         IMChatRoomDetail chatRoomDetail, ArrayList<String> existingUsers, String mContextId, String mContextName) {
        Intent intent = new Intent(appCompatActivity, IMChatUserListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IMConstants.CHAT_ROOM_TYPE, chatRoomDetail.roomType);
        bundle.putStringArrayList(IMConstants.EXITING_USERS_ID_LIST, existingUsers);
        bundle.putString(IMConstants.CHAT_CONTEXT_ID, mContextId);
        bundle.putString(IMConstants.CHAT_CONTEXT_NAME, mContextName);
        intent.putExtras(bundle);

        appCompatActivity.startActivityForResult(intent, IMConstants.ADD_USER_REQUEST_CODE);
    }

    public static void openChatUserListActivityToForwardChat(AppCompatActivity appCompatActivity) {
        Intent userListIntent = new Intent(appCompatActivity, IMChatUserListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(IMConstants.IS_CHAT_FORWARDED, true);
        userListIntent.putExtras(bundle);
        appCompatActivity.startActivityForResult(userListIntent, IMConstants.REQUEST_FORWARD_CHAT);
    }

    public static void openChatMessageInfoActivity(AppCompatActivity appCompatActivity, String chatMsgId) {
        Intent chatMsgInfoIntent = new Intent(appCompatActivity, IMChatMessageInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(IMConstants.CHAT_MESSAGE_ID, chatMsgId);
        chatMsgInfoIntent.putExtras(bundle);
        appCompatActivity.startActivity(chatMsgInfoIntent);
    }

    public static String getFirstTwoCharacters(String name) {
        name = name.replaceAll("  "," ");
        name = name.replaceAll("  "," ");
        String nameArray[] = name.replaceAll("  "," ").split(" ");
        String result = "";
        if(nameArray.length==1) {
            result = "" + name.charAt(0);
        }else if(nameArray.length>1){
            result = ""+name.charAt(0)+nameArray[1].charAt(0);
        }
        return result.toUpperCase();
    }
}
