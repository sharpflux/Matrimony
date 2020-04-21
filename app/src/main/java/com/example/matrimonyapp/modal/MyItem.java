package com.example.matrimonyapp.modal;

public class MyItem {

    private int id;
    private String name;
    private boolean isChecked;

    public MyItem() {
        this.id = 0;
        this.name = "ABC";
    }


    public MyItem(int id, String name) {
        this.id = id;
        this.name = name;
    }


    public MyItem(int id, String name, boolean isChecked) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
