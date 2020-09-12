package com.example.matrimonyapp.modal;

import java.util.ArrayList;

public class ExpandedMenuModel {

    String menuName = "";
    int menuIconId = -1;
    boolean hasSubMenu=false;// menu icon resource id
    ArrayList<String> arrayList_subMenu;
/*    public String getIconName() {
        return iconName;
    }
    public void setIconName(String iconName) {
        this.iconName = iconName;
    }
    public int getIconImg() {
        return iconImg;
    }
    public void setIconImg(int iconImg) {
        this.iconImg = iconImg;
    }*/

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuIconId() {
        return menuIconId;
    }

    public void setMenuIconId(int menuIconId) {
        this.menuIconId = menuIconId;
    }

    public boolean isHasSubMenu() {
        return hasSubMenu;
    }

    public void setHasSubMenu(boolean hasSubMenu) {
        this.hasSubMenu = hasSubMenu;
    }

    public ArrayList<String> getArrayList_subMenu() {
        return arrayList_subMenu;
    }

    public void setArrayList_subMenu(ArrayList<String> arrayList_subMenu) {
        this.arrayList_subMenu = arrayList_subMenu;
    }
}