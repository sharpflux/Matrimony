package com.example.matrimonyapp.modal;

public class MineItem extends ListItem {
    private ChatModel pojoOfJsonArray;
    private Boolean isSelected = false;
    public ChatModel getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }

    public void setChatModelArray(ChatModel pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    public Boolean IsSelected()
    {
        return isSelected;
    }

    public void setSelected(Boolean selected)
    {
        isSelected = selected;
    }

    @Override
    public int getType() {
        return TYPE_MINE;
    }


}