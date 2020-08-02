package com.example.matrimonyapp.modal;


public class GalleryImageModel {

    String imagePath;  //absolutePathofImage
    String title; // camera or folder
    //int resImg;  // resource img for camera or folder
    boolean isSelected; //

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}