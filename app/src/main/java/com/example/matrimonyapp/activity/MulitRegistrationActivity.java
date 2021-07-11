package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.fragment.BasicDetailsFragment;
import com.example.matrimonyapp.fragment.ProfessionalDetailsFragment;
import com.example.matrimonyapp.fragment.RegistrationBasicFragment;
import com.example.matrimonyapp.fragment.RegistrationProfessional;

public class MulitRegistrationActivity extends AppCompatActivity {


    FrameLayout contentFrame;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulit_registration);
        btn = findViewById(R.id.btn);

        fragmentManager = getSupportFragmentManager();
        replaceFragment(new BasicDetailsFragment(), "BasicFragment", false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new ProfessionalDetailsFragment(), "ProfessionalFragment", false);
            }
        });
    }

    public void replaceFragment(Fragment fragment, String fragmentName, boolean backStack) {
        if (!isFinishing()) {
            Log.e("TAG", "replaceFragment: ********");
            try {

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentFrame, fragment, fragmentName);
                if (backStack) {
                    fragmentTransaction.addToBackStack(fragmentName);
                }
                fragmentTransaction.commit();
            } catch (Exception e) {

                Log.e("TAG", "replaceFragment: exceptin block catch******************" + e);
                e.printStackTrace();
                Log.e("TAG", "replaceFragment: " + e);
            }
        }
    }
}