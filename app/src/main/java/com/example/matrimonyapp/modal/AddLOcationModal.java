package com.example.matrimonyapp.modal;

public class AddLOcationModal {
    private String name;
   private String id;
    public String FilterBy;
    public  boolean IsChecked;

    public AddLOcationModal(String name, String id, String filterBy) {
        this.name = name;
        this.id = id;
        FilterBy = filterBy;

    }

    public boolean isChecked() {
        return IsChecked;
    }

    public void setChecked(boolean checked) {
        IsChecked = checked;
    }

    public String getFilterBy() {
        return FilterBy;
    }

    public void setFilterBy(String filterBy) {
        FilterBy = filterBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean getSelected() {
        return IsChecked;
    }
}
