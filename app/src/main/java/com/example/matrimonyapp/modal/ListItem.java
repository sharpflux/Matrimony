package com.example.matrimonyapp.modal;

public abstract class ListItem {
    public static final int TYPE_DATE = 0;
    public static final int TYPE_MINE = 1;
    public static final int TYPE_OTHER = 2;
    abstract public int getType();
}