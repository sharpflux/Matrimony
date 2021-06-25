package com.example.matrimonyapp.modal;

public abstract  class ListObject {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_GENERAL_RIGHT = 1;
    public static final int TYPE_GENERAL_LEFT = 2;
    abstract public int getType(int userId);
}
