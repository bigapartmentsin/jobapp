package com.abln.chat.ui.gcm;

import android.content.Context;

import com.abln.chat.IMInstance;
import com.abln.chat.R;
import com.abln.chat.core.model.IMChatMessage;
import com.abln.chat.core.model.IMChatRoomDetail;
import com.abln.chat.core.model.IMChatRoomDetail.IMChatRoomMember;
import com.abln.chat.core.model.IMChatUser;
import com.abln.chat.utils.IMConstants;

import java.util.ArrayList;

public class IMPushNotificationHandler {

    public static void processChatRoomDetail(Context context, IMChatRoomDetail chatRoomDetail) {

        if (IMInstance.getInstance().getIMDatabase().getIMChatRoomDetailDao().isChatRoomExist(chatRoomDetail.roomId)) {
            IMChatRoomDetail existingRoomDetail = IMInstance.getInstance().getIMDatabase().
                    getIMChatRoomDetailDao().getChatRoomDetail(chatRoomDetail.roomId);

            if (!existingRoomDetail.isChatRoomMuted) {
                ArrayList<String> newRoomMembersIdList = new ArrayList<>();
                for (IMChatRoomMember chatRoomMember : chatRoomDetail.roomMembers) {
                    newRoomMembersIdList.add(chatRoomMember.userId);
                }

                ArrayList<String> existingRoomMembersIdList = new ArrayList<>();
                for (IMChatRoomMember chatRoomMember : existingRoomDetail.roomMembers) {
                    existingRoomMembersIdList.add(chatRoomMember.userId);
                }

                ArrayList<IMChatUser> usersAdded = getUsersAdded(newRoomMembersIdList, existingRoomMembersIdList);
                ArrayList<IMChatUser> usersRemoved = getUsersRemoved(newRoomMembersIdList, existingRoomMembersIdList);

                if (usersAdded.size() > 0 || usersRemoved.size() > 0) {
                    showUsersAddedOrRemovedMsg(usersAdded, usersRemoved, chatRoomDetail, context);
                }

            } else {
                // Here Chat Thread is muted
            }

        } else {
            // Here New Chat Thread is created
            if (null != chatRoomDetail.roomType && chatRoomDetail.roomType.equals(IMConstants.CHAT_ROOM_TYPE_GROUP)) {
                IMChatUser senderUser = IMInstance.getInstance().getIMDatabase().
                        getIMChatUserDao().getChatUserById(chatRoomDetail.adminId);
                String disPlayMessage = senderUser.userName + " " + context.getResources().getString(R.string.im_added_you_to_a_conversation);
                IMPushNotificationHelper.sendChatRoomDetailNotification(context,
                        chatRoomDetail.roomId, disPlayMessage);
            }
        }
    }

    public static void processChatMessage(Context context, IMChatMessage chatMessage) {
        IMChatRoomDetail chatRoomDetail = IMInstance.getInstance().getIMDatabase().
                getIMChatRoomDetailDao().getChatRoomDetail(chatMessage.roomId);
        if (!chatRoomDetail.isChatRoomMuted) {
            IMPushNotificationHelper.sendChatMessageNotification(context);
        }
    }

    private static void showUsersAddedOrRemovedMsg(ArrayList<IMChatUser> chatUsersAdded,
                                                   ArrayList<IMChatUser> chatUsersRemoved,
                                                   IMChatRoomDetail chatRoomDetail, Context context) {
        // TODO: ADMIN ADDED OR REMOVED USERS
        String groupNameString = "";

        if (null != chatRoomDetail.roomName && chatRoomDetail.roomName.equals("")) {
            groupNameString = " " + context.getResources().getString(R.string.im_to_a_conversation);

        } else {
            groupNameString = " to " + chatRoomDetail.roomName;
        }



        String senderName = IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                getChatUserById(chatRoomDetail.adminId).userName;

        if (null != chatUsersAdded && chatUsersAdded.size() > 0) {
            String addedUsersString = getUsersNameAsString(chatUsersAdded);
            String displayMessage = senderName + " " + context.getResources().getString(R.string.im_added) + " " + addedUsersString + groupNameString;
            IMPushNotificationHelper.sendChatRoomDetailNotification(context, chatRoomDetail.roomId, displayMessage);
        }

        if (null != chatUsersRemoved && chatUsersRemoved.size() > 0) {
            String removedUsersString = getUsersNameAsString(chatUsersRemoved);
            if (removedUsersString.contains("you")) {
                removedUsersString = "you";
            }

            String displayMessage = senderName + " " + context.getResources().getString(R.string.im_removed) + " " + removedUsersString + groupNameString;
            IMPushNotificationHelper.sendChatRoomDetailNotification(context, chatRoomDetail.roomId, displayMessage);

        }
    }

    private static ArrayList<IMChatUser> getUsersAdded(ArrayList<String> newRoomMembersIdList,
                                                       ArrayList<String> existingRoomMembersIdList) {
        for (String userId : existingRoomMembersIdList) {
            newRoomMembersIdList.remove(userId);
        }

        return IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                getChatUserListByUserIds(new ArrayList<>(newRoomMembersIdList));
    }

    private static ArrayList<IMChatUser> getUsersRemoved(ArrayList<String> newRoomMembersIdList,
                                                         ArrayList<String> existingRoomMembersIdList) {
        for (String userId : newRoomMembersIdList) {
            existingRoomMembersIdList.remove(userId);
        }

        return IMInstance.getInstance().getIMDatabase().getIMChatUserDao().
                getChatUserListByUserIds(new ArrayList<>(existingRoomMembersIdList));
    }

    private static String getUsersNameAsString(ArrayList<IMChatUser> chatUserList) {
        StringBuilder useNames = new StringBuilder();
        IMChatUser loggedInUser = IMInstance.getInstance().getLoggedInUser();

        for (int i = 0; i < chatUserList.size(); i++) {
            if (i != 0) {
                useNames.append(" , ");
            }

            IMChatUser chatUser = chatUserList.get(i);

            if (loggedInUser.userId.equals(chatUser.userId)) {
                useNames.append("you");

            } else {
                useNames.append(chatUser.userName);
            }

            if (i == 2 && chatUserList.size() > 2) {
                useNames.append("...");
                break;
            }
        }
        return useNames.toString();
    }
}
