package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.GalleryAdapter;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.modal.NavigationDrawer;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.modal.TimelineModel;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MyProfileActivity extends AppCompatActivity {

    ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_myProfile;
    TextView textView_editProfile, textView_editProfile2, textView_addBio, textView_setPreferences,
            textView_changeProfilePhoto;




    ArrayList<Uri> mArrayUri;
    GridView gvGallery;
    Uri uri;
    GalleryAdapter galleryAdapter;
    Bitmap bitmap, O;
    GestureDetector gestureDetector;
    GestureDetectorCompat gestureDetector_gridView;





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
    private SimpleGestureFilter detector;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);

        mArrayUri = new ArrayList<Uri>();

        gvGallery = findViewById(R.id.gridView_gallery);
        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);

        textView_editProfile = findViewById(R.id.textView_editProfile);
        textView_editProfile2 = findViewById(R.id.textView_editProfile2);
        textView_addBio = findViewById(R.id.textView_addBio);
        textView_setPreferences = findViewById(R.id.textView_setPreferences);
        textView_changeProfilePhoto = findViewById(R.id.textView_changeProfilePhoto);



        onClickListener();




        //uri = Uri.parse(getResources().getDrawable(R.drawable.noimage).toString());

        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower3)
        +"/"+this.getResources().getResourceTypeName(R.drawable.flower3)
        +"/"+this.getResources().getResourceEntryName(R.drawable.flower3));

        imageView_myProfile.setColorFilter(ContextCompat.getColor(this,R.color.project_color));





        navigation();



        /*BitmapDrawable BD = (BitmapDrawable)imageView_myProfile.getDrawable();
        bitmap = BD.getBitmap();

        O = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());

        for(int i=0; i<bitmap.getWidth(); i++)
        {
            for (int j=0; j<bitmap.getHeight(); j++)
            {
                int p = bitmap.getPixel(i,j);
                int b = Color.LTGRAY;

                int x=0;
                int y=0;
                b=b+150;
                O.setPixel(i,j,Color.argb(Color.alpha(p),x,y,b));

            }

        }
        imageView_myProfile.setImageBitmap(O);*/







        for(int i=0; i<7; i++) {
            mArrayUri.add(uri);
        }

        gestureDetector = new GestureDetector(this,new Gesture());

        galleryAdapter = new GalleryAdapter(this,mArrayUri,MyProfileActivity.this,getResources());
        gvGallery.setAdapter(galleryAdapter);
        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                .getLayoutParams();
        mlp.setMargins(0, 5, 0, 0);//2nd para gvGallery.getHorizontalSpacing()


        setGridViewHeight(gvGallery,3);


        gestureDetector_gridView = new GestureDetectorCompat(getApplicationContext(),new MyGestureListener());
        gestureDetector_gridView.setIsLongpressEnabled(true);


    }

    private void onClickListener() {

        onClickNewActivity(imageView_home, MyProfileActivity.this, HomeActivity.class);
        onClickNewActivity(imageView_addPhoto, MyProfileActivity.this, DirectMessagesActivity.class);
        onClickNewActivity(imageView_like, MyProfileActivity.this, InterestActivity.class);
        onClickNewActivity(textView_editProfile, MyProfileActivity.this, MainActivity.class);
        onClickNewActivity(textView_editProfile2, MyProfileActivity.this, MainActivity.class);
        onClickNewActivity(textView_setPreferences, MyProfileActivity.this, SetPreferencesActivity.class);


/*
        textView_editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(MyProfileActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });*/

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

    private void navigation() {


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


        drawerrecyview = findViewById(R.id.drawerrecyview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyProfileActivity.this);
        drawerrecyview.setLayoutManager(layoutManager);
        drawerrecyview.setItemAnimator(new DefaultItemAnimator());


        activityListModelArrayList = new ArrayList<>();
        for (int i = 0; i < IMAGES.length; i++) {
            NavigationItemListModel activityListModel = new NavigationItemListModel();
            activityListModel.setTxt(TEXT[i]);
            activityListModel.setImage(IMAGES[i]);
            activityListModelArrayList.add(activityListModel);
        }

        navigationDrawerAdapter = new NavigationDrawerAdapter(MyProfileActivity.this, activityListModelArrayList);
        drawerrecyview.setAdapter(navigationDrawerAdapter);



        setToolbar();

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

    void setGridViewHeight(GridView gridView, int noOfColumns)
    {

        ListAdapter gridViewAdapter = gridView.getAdapter();


        if(gridViewAdapter == null)
            return;

        else
        {
            int totalHt;
            int items = gridViewAdapter.getCount();
            int rows;

            View listItem = gridViewAdapter.getView(0,null,gridView);
            listItem.measure(0,0);
            totalHt = listItem.getMeasuredHeight();

            float x;

            if(items > noOfColumns)
            {
                x = items / noOfColumns;

                if(items % noOfColumns != 0 )
                {
                    rows = (int)(x+1);

                }
                else
                {
                    rows = (int)x;
                }
                totalHt *= rows;
                totalHt += gridView.getVerticalSpacing() * rows;

            }

            ViewGroup.LayoutParams params = gridView.getLayoutParams();
            params.height = totalHt;
            gridView.setLayoutParams(params);

        }
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);



        }
    }

    class Gesture extends GestureDetector.SimpleOnGestureListener{
        public boolean onSingleTapUp(MotionEvent ev) {
            return false;
        }

        public void onLongPress(MotionEvent ev) {



        }


        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                float distanceY) {


            return false;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            return false;
        }
    }

}
