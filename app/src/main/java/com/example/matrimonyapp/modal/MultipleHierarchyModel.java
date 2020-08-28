package com.example.matrimonyapp.modal;

import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MultipleHierarchyModel {

    EditText view;
    ArrayList arrayList;

    public MultipleHierarchyModel() {
    }

    public MultipleHierarchyModel(EditText view, ArrayList arrayList) {
        this.view = view;
        this.arrayList = arrayList;
    }

    public EditText getView() {
        return view;
    }

    public void setView(EditText view) {
        this.view = view;
    }

    public ArrayList getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList arrayList) {
        this.arrayList = arrayList;
    }
}
