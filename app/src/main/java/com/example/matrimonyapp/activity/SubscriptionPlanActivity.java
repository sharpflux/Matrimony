package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.matrimonyapp.R;

public class SubscriptionPlanActivity extends AppCompatActivity {


    private TextView textView_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);

        textView_toolbar = findViewById(R.id.textView_toolbar);
        textView_toolbar.setText(getResources().getString(R.string.subscription_plan));


    }
}