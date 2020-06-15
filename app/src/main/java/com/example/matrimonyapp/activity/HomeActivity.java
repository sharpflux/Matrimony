package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private Integer[] IMAGES = {R.drawable.shopping_bag, R.drawable.order, R.drawable.my_acc,
            R.drawable.offer, R.drawable.notification, R.drawable.start1,  R.drawable.my_acc};

    private String[] TEXT = {"My Cart", "My Orders", "My Account",  "Offer Zone", "Notification", "Rate App","Logout"};


    private Toolbar toolbar;
    private UserModel userModel;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private RecyclerView drawerrecyview;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private ArrayList<NavigationItemListModel> activityListModelArrayList;

    private SimpleGestureFilter detector;

    TimelineAdapter timelineAdapter;
    private ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_myProfile;

    private TextView textView_welcomeUserName;
    private ImageView imageView_profilePic;

    ArrayList<TimelineModel> timelineModelList;
    RecyclerView recyclerView_timeline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);
        imageView_home.setColorFilter(ContextCompat.getColor(this,R.color.project_color));
        recyclerView_timeline = (RecyclerView) findViewById(R.id.recyclerView_timeline);
        toolbar = findViewById(R.id.toolbar1);
        textView_welcomeUserName = findViewById(R.id.textView_welcomeUserName);
        imageView_profilePic = findViewById(R.id.imageView_profilePic);
        // timelineAdapter = new TimelineAdapter(this,)


        if (!CustomSharedPreference.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(this).getUser();
        textView_welcomeUserName.setText(userModel.getFullName());
        if( userModel!=null && !userModel.getProfilePic().equals("1"))
        {
            //imageView_profilePic.setImageURI();
        }

        imageView_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),SetPreferencesActivity.class);
                startActivity(intent);

            }
        });

        imageView_addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),DirectMessagesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });


        imageView_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, InterestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        imageView_myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),MyProfileActivity.class);

                startActivity(intent);

            }
        });


        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

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

        invalidateOptionsMenu();

        ////////////////////////////////
        drawerrecyview = findViewById(R.id.drawerrecyview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        drawerrecyview.setLayoutManager(layoutManager);
        drawerrecyview.setItemAnimator(new DefaultItemAnimator());


        activityListModelArrayList = new ArrayList<>();
        for (int i = 0; i < IMAGES.length; i++) {
            NavigationItemListModel activityListModel = new NavigationItemListModel();
            activityListModel.setTxt(TEXT[i]);
            activityListModel.setImage(IMAGES[i]);
            activityListModelArrayList.add(activityListModel);
        }

        navigationDrawerAdapter = new NavigationDrawerAdapter(HomeActivity.this, activityListModelArrayList);
        drawerrecyview.setAdapter(navigationDrawerAdapter);

        setToolbar();







        timelineModelList = new ArrayList<TimelineModel>();


        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+
                this.getResources().getResourcePackageName(R.drawable.flower3)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower3)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower3));
        for(int i=0; i<5; i++)
        {
            TimelineModel timelineModel = new TimelineModel("@yourUserId"+(i+1), "User Name", "25 Yrs", "MBA - Marketing, Chennai, India","Bio loading!...",uri, getApplicationContext());
            timelineModelList.add(timelineModel);

        }

        timelineAdapter = new TimelineAdapter(this,timelineModelList, getWindowManager().getDefaultDisplay());
        recyclerView_timeline.setAdapter(timelineAdapter);
        recyclerView_timeline.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_timeline.setLayoutManager(mLayoutManager);

       /* recyclerView_timeline.addOnItemTouchListener(new RecyclerViewTouchListener(getApplicationContext(),
                recyclerView_timeline, new RecyclerViewTouchListener.OnTouchActionListener() {
            @Override
            public void onLeftSwipe(View view, final int position) {

*//*                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.anim_slide_in_right);
                //animation.setStartOffset(0);
                view.startAnimation(animation);*/
        /*

                final int pos = position;
                Display display = getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);


                view.animate().alpha(0f).setDuration(500).translationX(-size.x);
                view.postOnAnimationDelayed(new Runnable() {
                    @Override
                    public void run() {
                        timelineModelList.remove(pos);
                        timelineAdapter.notifyDataSetChanged();
                    }
                },500);


*//*
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                },500);*/
        /*

            }

            @Override
            public void onRightSwipe(View view, int position) {

            }

            @Override
            public void onClick(View view, int position) {

            }
        }));


*/
        /*this.overridePendingTransition(R.anim.animation1,
                R.anim.animation2);*/

        detector = new SimpleGestureFilter(this,this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent me){
        // Call onTouchEvent of SimpleGestureFilter class
        this.detector.onTouchEvent(me);
        return super.dispatchTouchEvent(me);
    }
    @Override
    public void onSwipe(int direction) {
        String str = "";

        switch (direction) {

            case SimpleGestureFilter.SWIPE_RIGHT : str = "Swipe Right";

                /*Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);*/

                break;
            case SimpleGestureFilter.SWIPE_LEFT :  str = "Swipe Left";
                //Intent i = new Intent(this, MyProfileActivity.class);
               // Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.anim_slide_in_left,R.anim.anim_slide_out_left).toBundle();
                //startActivity(i, bundle);
                //startActivity(i);
                break;
            case SimpleGestureFilter.SWIPE_DOWN :  str = "Swipe Down";
                break;
            case SimpleGestureFilter.SWIPE_UP :    str = "Swipe Up";
                break;

        }
       // Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDoubleTap() {
       // Toast.makeText(this, "Double Tap", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {

            drawer.closeDrawer(Gravity.RIGHT); //OPEN Nav Drawer!
        } else {
            finish();
        }
    }


    private void setToolbar() {

/*        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
       // actionBar.setTitle("");*/
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


    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;


    }



}
