package com.example.matrimonyapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.RegisterTransporterAdapter;
import com.example.matrimonyapp.modal.RegisterTransporterModel;

import java.util.ArrayList;

public class RegisterTransporterActivity extends AppCompatActivity {

    TextView textView_add, textView_submit;


    RegisterTransporterAdapter registerTransporterAdapter;
    ArrayList<RegisterTransporterModel> registerTransporterModelList;
    RecyclerView recyclerView_registerTransporter;
    RegisterTransporterModel registerTransporterModel;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister_transporter);

        textView_add = findViewById(R.id.textView_add);
        textView_submit = findViewById(R.id.textView_submit);
        recyclerView_registerTransporter = findViewById(R.id.recyclerView_registerTransporter);

        context = getApplicationContext();

        registerTransporterModelList = new ArrayList<RegisterTransporterModel>();

        RegisterTransporterModel registerTransporterModel = new RegisterTransporterModel();
        registerTransporterModelList.add(registerTransporterModel);

        registerTransporterAdapter = new RegisterTransporterAdapter(this,registerTransporterModelList);
        recyclerView_registerTransporter.setAdapter(registerTransporterAdapter);
        recyclerView_registerTransporter.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_registerTransporter.setLayoutManager(mLayoutManager);

        textView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                RegisterTransporterModel registerTransporterModel1 = new RegisterTransporterModel();
                registerTransporterModelList.add(registerTransporterModel1);
                registerTransporterAdapter.notifyDataSetChanged();
            }
        });

        textView_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

    }

}
