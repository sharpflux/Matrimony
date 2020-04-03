package com.example.matrimonyapp.activity;

import android.os.Bundle;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.fragment.BasicDetailsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Registration");

        RelativeLayout relativeLayout = findViewById(R.id.dynamic_fragment_frame_layout);
        relativeLayout.getClass().getName();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.dynamic_fragment_frame_layout, new BasicDetailsFragment());
        //fragmentTransaction.addToBackStack(getClas);
        fragmentTransaction.commit();


    }
}
