package com.example.matrimonyapp.modal;

public class ChatModelObject extends ListObject {

    private ChatModel chatModel;

    public ChatModel getChatModel() {
        return chatModel;
    }

    public void setChatModel(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    @Override
    public int getType(int userId) {
        if (Integer.parseInt( this.chatModel.getReceiverId()) == userId) {
            return TYPE_GENERAL_RIGHT;
        } else
            return TYPE_GENERAL_LEFT;
    }
}
