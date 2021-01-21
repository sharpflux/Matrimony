package com.example.matrimonyapp.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.InterestTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomNavigationView;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.google.android.gms.maps.model.Circle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class InterestActivity extends AppCompatActivity {

    TabLayout tabLayout_interest;
    CustomViewPager viewPager_interest;
    ImageView imageView_home, imageView_search, imageView_message, imageView_like ,imageView_myProfile, imageView_menu;

    private String currentLanguage;
    private DrawerLayout drawerLayout;
    ExpandableListView expandableList;
    TextView textView_welcomeUserName;
    CircleImageView circleImageView_headerProfilePic;
    UserModel userModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interest);

        init();


        userModel = CustomSharedPreference.getInstance(InterestActivity.this).getUser();
        //imageView_menu.setVisibility(View.GONE);

/*        imageView_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this, HomeActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                //finish();
            }
        });


        imageView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InterestActivity.this, SetPreferencesActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityIfNeeded(intent,0);
                //finish();
            }
        });

        imageView_message.setOnClickListener(new View.OnClickListener() {
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
        });*/


        viewPager_interest.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_interest.setTabGravity(TabLayout.GRAVITY_FILL);
        final InterestTabLayoutAdapter interestTabLayoutAdapter = new InterestTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_interest.getTabCount(),"Interest");
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

        onClickListener();
        navigation();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    private void navigation()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        //arrayList_expandedMenuModel = new ArrayList<>();
        View view_header = navigationView.getHeaderView(0);

        circleImageView_headerProfilePic = view_header.findViewById(R.id.circleImageView_profilePic);
        textView_welcomeUserName = view_header.findViewById(R.id.textView_welcomeUserName);



        Glide.with(InterestActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.drawable.default_profile)
                .into(circleImageView_headerProfilePic);

        textView_welcomeUserName.setText(userModel.getFullName());

        CustomNavigationView customNavigationView = new CustomNavigationView(InterestActivity.this,
                drawerLayout, expandableList, navigationView);

        customNavigationView.createNavigation();


        findViewById(R.id.imageView_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(drawerLayout.isDrawerOpen(navigationView))
                {
                    drawerLayout.closeDrawers();
                }
                else {
                    drawerLayout.openDrawer(navigationView);
                }

            }
        });

        //prepareListData();


        // setting list adapter

/*
      expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
          @Override
          public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
              //Log.d("DEBUG", "submenu item clicked");
              return false;
          }
      });
      expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
          @Override
          public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
              //Log.d("DEBUG", "heading clicked");
              return false;
          }
      });*/
    }



    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

    private void onClickListener() {


        onClickNewActivity(imageView_home, InterestActivity.this, HomeActivity.class);
        onClickNewActivity(imageView_search, InterestActivity.this, SetPreferencesActivity.class);
        onClickNewActivity(imageView_message, InterestActivity.this, DirectMessagesActivity.class);
        onClickNewActivity(imageView_myProfile, InterestActivity.this, MyProfileActivity.class);
        //onClickNewActivity(imageView_like, InterestActivity.this, InterestActivity.class);
    }

    private void init() {

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        viewPager_interest = findViewById(R.id.viewPager_interest);
        tabLayout_interest= findViewById(R.id.tabLayout_interest);

        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_message = findViewById(R.id.imageView_message);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);
        imageView_menu = findViewById(R.id.imageView_menu);

/*        imageView_like.setColorFilter(ContextCompat.getColor(this,R.color.white));
        imageView_like.setBackgroundResource(R.drawable.gradient_place_order);*/

        imageView_like.setImageResource(R.drawable.red_heart);

    }

    private void onClickNewActivity(View view, final Context context, final Class goToClass) {

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, goToClass);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });


    }

}
