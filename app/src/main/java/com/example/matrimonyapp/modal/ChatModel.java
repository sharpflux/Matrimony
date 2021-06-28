package com.example.matrimonyapp.modal;

import java.io.Serializable;

public class ChatModel implements Serializable {

    String senderId, receiverId, message, messageTime, messageStatus,time;

    private Boolean isSelected = false;
    public ChatModel() {
    }

    public ChatModel(String message, String messageTime, String senderId, String receiverId, String messageStatus) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageTime = messageTime;
        this.messageStatus = messageStatus;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean IsSelected()
    {
        return isSelected;
    }

    public void setSelected(Boolean selected)
    {
        isSelected = selected;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
    }

}
