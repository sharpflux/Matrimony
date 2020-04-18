package com.example.matrimonyapp.customViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.AddLocationAdapter;
import com.example.matrimonyapp.modal.AddLOcationModal;

import java.util.ArrayList;

public class CustomDialogLocationRec extends Dialog {
   // Button btn_next;
    public Context context;
    AddLocationAdapter dataAdapter;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    SearchView searchView_customDialog;
    AddLOcationModal addLOcationModal;
    ArrayList<AddLOcationModal> list;
    StringBuilder state_builder_id;
    Button btn_clearall,btn_cancel,btn_ok;

    public CustomDialogLocationRec(@NonNull Context context, AddLocationAdapter dataAdapter) {
        super(context);
        this.context = context;
        this.dataAdapter = dataAdapter;
        state_builder_id = new StringBuilder();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_location_rev);

      //  btn_next=findViewById(R.id.btn_next);

        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        searchView_customDialog = findViewById(R.id.searchView_customDialog);
        btn_ok = findViewById(R.id.btn_ok);
        btn_cancel = findViewById(R.id.btn_cancel);
        btn_clearall = findViewById(R.id.btn_clearall);


        list = new ArrayList<>();

        recyclerView.setAdapter(dataAdapter);

       /* btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*for (int i = 0; i < productlist.size(); i++) {
                    AddLOcationModal filter = productlist.get(i);
                    if (filter.isChecked()) {
                        state_builder_id.append(filter.getId() + ",");
                    }
                }*//*
                dismiss();
            }
        });*/
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < list.size(); i++) {
                    AddLOcationModal filter = list.get(i);
                    if (filter.isChecked()) {
                        state_builder_id.append(filter.getId() + ",");
                       String StateId=(state_builder_id.append(filter.getId() + ",").toString());
                        Toast.makeText(getContext(), StateId, Toast.LENGTH_SHORT).show();
                    }
                }
               // dismiss();
            }
        });
    }
}
