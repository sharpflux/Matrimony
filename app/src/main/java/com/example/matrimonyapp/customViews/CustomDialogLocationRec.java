package com.example.matrimonyapp.customViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.AddLocationAdapter;

public class CustomDialogLocationRec extends Dialog {
    Button btn_next;
    public Context context;
    AddLocationAdapter dataAdapter;
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    SearchView searchView_customDialog;

    public CustomDialogLocationRec(@NonNull Context context, AddLocationAdapter dataAdapter) {
        super(context);
        this.context = context;
        this.dataAdapter = dataAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog_location_rev);

        btn_next=findViewById(R.id.btn_next);

        recyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        searchView_customDialog = findViewById(R.id.searchView_customDialog);



        recyclerView.setAdapter(dataAdapter);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
