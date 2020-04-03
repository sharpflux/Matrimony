package com.example.matrimonyapp.modal;

public class ChatModel {

    String messageSent, messageTime;

    public ChatModel() {
    }

    public ChatModel(String messageSent, String messageTime) {
        this.messageSent = messageSent;
        this.messageTime = messageTime;
    }

    public String getMessageSent() {
        return messageSent;
    }

    public void setMessageSent(String messageSent) {
        this.messageSent = messageSent;
    }

    public String getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(String messageTime) {
        this.messageTime = messageTime;
    }
}
