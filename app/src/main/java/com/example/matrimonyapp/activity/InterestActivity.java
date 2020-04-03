package com.example.matrimonyapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.InterestTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.google.android.material.tabs.TabLayout;

public class InterestActivity extends AppCompatActivity {

    TabLayout tabLayout_interest;
    CustomViewPager viewPager_interest;
    ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_myProfile, imageView_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        viewPager_interest = findViewById(R.id.viewPager_interest);
        tabLayout_interest= findViewById(R.id.tabLayout_interest);

        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);
        imageView_menu = findViewById(R.id.imageView_menu);

        imageView_menu.setVisibility(View.GONE);

        imageView_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this, HomeActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                //finish();
            }
        });


        imageView_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this, DirectMessagesActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                //finish();
            }
        });

        imageView_myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),MyProfileActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);

                //finish();

            }
        });


        viewPager_interest.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_interest.setTabGravity(TabLayout.GRAVITY_FILL);
        final InterestTabLayoutAdapter interestTabLayoutAdapter = new InterestTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_interest.getTabCount());
        viewPager_interest.setAdapter(interestTabLayoutAdapter);



        viewPager_interest.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_interest));

        tabLayout_interest.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager_interest.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
