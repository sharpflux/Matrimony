package com.example.matrimonyapp.modal;

public class OtherItem extends ListItem {
    private ChatModel pojoOfJsonArray;
    private Boolean isSelected = false;
    public ChatModel getPojoOfJsonArray() {
        return pojoOfJsonArray;
    }
    public Boolean IsSelected()
    {
        return isSelected;
    }

    public void setSelected(Boolean selected)
    {
        isSelected = selected;
    }

    public void setPojoOfJsonArray(ChatModel pojoOfJsonArray) {
        this.pojoOfJsonArray = pojoOfJsonArray;
    }

    @Override
    public int getType() {
        return TYPE_OTHER;
    }


}