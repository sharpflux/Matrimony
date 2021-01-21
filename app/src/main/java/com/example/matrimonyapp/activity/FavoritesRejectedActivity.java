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

public class FavoritesRejectedActivity extends AppCompatActivity {

    TabLayout tabLayout_favorites;
    CustomViewPager viewPager_favorites;
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
        setContentView(R.layout.activity_favorites_rejected);

        init();


        userModel = CustomSharedPreference.getInstance(FavoritesRejectedActivity.this).getUser();
        //imageView_menu.setVisibility(View.GONE);



        viewPager_favorites.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_favorites.setTabGravity(TabLayout.GRAVITY_FILL);
        final InterestTabLayoutAdapter interestTabLayoutAdapter = new InterestTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_favorites.getTabCount(),"Favorites");
        viewPager_favorites.setAdapter(interestTabLayoutAdapter);



        viewPager_favorites.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_favorites));

        tabLayout_favorites.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager_favorites.setCurrentItem(tab.getPosition());

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



    private void navigation()
    {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);

        //arrayList_expandedMenuModel = new ArrayList<>();
        View view_header = navigationView.getHeaderView(0);

        circleImageView_headerProfilePic = view_header.findViewById(R.id.circleImageView_profilePic);
        textView_welcomeUserName = view_header.findViewById(R.id.textView_welcomeUserName);



        Glide.with(FavoritesRejectedActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.drawable.default_profile)
                .into(circleImageView_headerProfilePic);

        textView_welcomeUserName.setText(userModel.getFullName());

        CustomNavigationView customNavigationView = new CustomNavigationView(FavoritesRejectedActivity.this,
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



    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
    private void init() {

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        viewPager_favorites = findViewById(R.id.viewPager_favorites);
        tabLayout_favorites= findViewById(R.id.tabLayout_favorites);

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


}
