package com.example.matrimonyapp.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.volley.CustomSharedPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



            Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 5 seconds
                        sleep(1 * 1000);

                        if (CustomSharedPreference.getInstance(getApplicationContext()).isLoggedIn()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }

                        // intent = new Intent(SplashActivity.this,Registration_Demo.class);

                        // After 5 seconds redirect to another intent


                        //Remove activity
                        finish();

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }
            };

            // start thread
            background.start();


    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
