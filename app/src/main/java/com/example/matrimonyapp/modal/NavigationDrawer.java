package com.example.matrimonyapp.modal;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MyProfileActivity;
import com.example.matrimonyapp.activity.SimpleGestureFilter;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class NavigationDrawer {

    private DrawerLayout drawer;


    private Integer[] IMAGES = {R.drawable.shopping_bag, R.drawable.order, R.drawable.my_acc,
            R.drawable.offer, R.drawable.notification, R.drawable.start1,  R.drawable.my_acc};

    private String[] TEXT = {"My Cart", "My Orders", "My Account",  "Offer Zone", "Notification", "Rate App","Logout"};

    private Toolbar toolbar;
    private RecyclerView drawerrecyview;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private ArrayList<NavigationItemListModel> activityListModelArrayList;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;

    Context context;


    public NavigationDrawer() {
    }

    public void navigation(DrawerLayout t_drawerLayout, NavigationView t_navigationView,
                           RecyclerView t_drawerRecyclerView) {

        drawer = t_drawerLayout;//findViewById(R.id.drawer_layout);
        navigationView = t_navigationView; //findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle((Activity) context, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
        actionBarDrawerToggle.setDrawerIndicatorEnabled(false);

        ((Activity)context).invalidateOptionsMenu();

        ////////////////////////////////
        drawerrecyview = t_drawerRecyclerView;//findViewById(R.id.drawerrecyview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        drawerrecyview.setLayoutManager(layoutManager);
        drawerrecyview.setItemAnimator(new DefaultItemAnimator());


        activityListModelArrayList = new ArrayList<>();
        for (int i = 0; i < IMAGES.length; i++) {
            NavigationItemListModel activityListModel = new NavigationItemListModel();
            activityListModel.setTxt(TEXT[i]);
            activityListModel.setImage(IMAGES[i]);
            activityListModelArrayList.add(activityListModel);
        }

        drawerrecyview.setAdapter(navigationDrawerAdapter);
        navigationDrawerAdapter = new NavigationDrawerAdapter(context, activityListModelArrayList);

       // setToolbar();


    }


   /*
    private void setToolbar() {


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.imageView_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Click", "keryu");

                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
            }
        });

    }
    */
}
