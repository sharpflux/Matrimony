package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.matrimonyapp.R;

public class FullImageActivity extends AppCompatActivity {

    ImageView myImage, back;
    private String currentLanguage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);
        myImage = findViewById(R.id.image);
        back = findViewById(R.id.back);

        Glide.with(this)
                .load(getIntent().getStringExtra("image"))
                .placeholder(R.color.codeGray)
                .into(myImage);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });


        currentLanguage = getResources().getConfiguration().locale.getLanguage();


    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


}