package com.example.matrimonyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import in.codeshuffle.typewriterview.TypeWriterListener;
import in.codeshuffle.typewriterview.TypeWriterView;

public class SplashActivity extends AppCompatActivity {

    public static final int SPLASH_TIMEOUT=1000;
    Animation animation_top, animation_bottom;

    ImageView imageView_logo;
    TextView textView_appName;
    TypeWriterView textView_tagLine;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        animation_top = AnimationUtils.loadAnimation(this, R.anim.anim_slide_from_top);
        animation_bottom = AnimationUtils.loadAnimation(this, R.anim.anim_slide_from_bottom);

        imageView_logo = findViewById(R.id.imageView_logo);
        textView_appName = findViewById(R.id.textView_appName);
        textView_tagLine = findViewById(R.id.textView_tagLine);


        imageView_logo.setAnimation(animation_top);
        textView_appName.setAnimation(animation_bottom);
        textView_tagLine.setWithMusic(false);
        textView_appName.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView_tagLine.setDelay(50);
                textView_tagLine.animateText(getResources().getString(R.string.tag_line));
                textView_tagLine.setWithMusic(false);

                textView_tagLine.setTypeWriterListener(new TypeWriterListener() {
                    @Override
                    public void onTypingStart(String text) {

                    }

                    @Override
                    public void onTypingEnd(String text) {
                        textView_tagLine.removeAnimation();
                        try {
                            Thread.sleep( 1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (CustomSharedPreference.getInstance(getApplicationContext()).isLoggedIn()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            intent.putExtra("ActivityState","started");
                            finish();
                            startActivity(intent);
                        }


                    }

                    @Override
                    public void onCharacterTyped(String text, int position) {

                    }

                    @Override
                    public void onTypingRemoved(String text) {

                    }
                });

            }
        }, 700);
        //textView_tagLine.setAnimation(animation_bottom);
/*        textView_appName.setDelay(150);
        textView_appName.animateText("Matrimony");
        textView_appName.setWithMusic(true);*/

           /* Thread background = new Thread() {
                public void run() {

                    try {
                        // Thread will sleep for 5 seconds
                        sleep(1 * 5000);

                        if (CustomSharedPreference.getInstance(getApplicationContext()).isLoggedIn()) {
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        }
                        else
                        {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            finish();
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
*/

    }
    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

}
