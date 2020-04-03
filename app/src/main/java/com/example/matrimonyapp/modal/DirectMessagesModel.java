package com.example.matrimonyapp.modal;

import android.net.Uri;

public class DirectMessagesModel {

    String userId, userName, lastMessage, lastMessageTime;
    Uri uri_profilePic;


    public DirectMessagesModel() {

    }

    public DirectMessagesModel(String userId, String userName, String lastMessage, String lastMessageTime, Uri uri_profilePic) {
        this.userId = userId;
        this.userName = userName;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
        this.uri_profilePic = uri_profilePic;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    public Uri getUri_profilePic() {
        return uri_profilePic;
    }

    public void setUri_profilePic(Uri uri_profilePic) {
        this.uri_profilePic = uri_profilePic;
    }
}
