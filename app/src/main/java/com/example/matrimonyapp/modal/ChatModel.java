package com.example.matrimonyapp.modal;

public class ChatModel {

    String senderId, receiverId, message, messageTime, messageStatus;

    public ChatModel() {
    }

    public ChatModel(String message, String messageTime, String senderId, String receiverId, String messageStatus) {
        this.message = message;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.messageTime = messageTime;
        this.messageStatus = messageStatus;
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
