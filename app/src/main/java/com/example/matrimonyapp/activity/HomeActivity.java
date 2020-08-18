package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteSetPreference;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class HomeActivity extends AppCompatActivity implements SimpleGestureFilter.SimpleGestureListener {

    private Integer[] IMAGES = {R.drawable.shopping_bag, R.drawable.order, R.drawable.my_acc,
            R.drawable.offer, R.drawable.notification, R.drawable.start1,  R.drawable.my_acc};

    private String[] TEXT = {"My Cart", "My Orders", "My Account",  "Offer Zone", "Notification", "Rate App","Logout"};


    private SwipeRefreshLayout swipeRefresLayout;

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
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    SQLiteSetPreference sqLiteSetPreference;

    static String gender, stateIds, districtsIds, talukaIds, maritalStatusIds, familyTypeIds, familyValueIds,
            familyIncomeIds, individualIncomeIds, qualificationLevelIds, qualificationIds, dietIds, colorIds,
            occupationIds, religionIds, casteIds, subCasteIds, ageMin, ageMax, heightMin, heightMax;
    //SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();



        // timelineAdapter = new TimelineAdapter(this,)


     /*   mSwipeRefreshLayout=findViewById(R.id.swiperefresh);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchValues();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });*/


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
        textView_welcomeUserName = findViewById(R.id.textView_welcomeUserName);
        textView_welcomeUserName.setText(userModel.getFullName());
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


/*        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+
                this.getResources().getResourcePackageName(R.drawable.flower3)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower3)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower3));
        for(int i=0; i<5; i++)
        {
            //TimelineModel timelineModel = new TimelineModel("@yourUserId"+(i+1), "User Name", "25 Yrs", "MBA - Marketing, Chennai, India","Bio loading!...",uri, getApplicationContext());
            TimelineModel timelineModel = new TimelineModel();
            timelineModelList.add(timelineModel);

        }*/

        fetchValues();
        timelineAdapter = new TimelineAdapter(this,timelineModelList, getWindowManager().getDefaultDisplay());
        recyclerView_timeline.setAdapter(timelineAdapter);
        recyclerView_timeline.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_timeline.setLayoutManager(mLayoutManager);
        //Toast.makeText(getApplicationContext(), " userId : "+userModel.getUserId(), Toast.LENGTH_SHORT).show();


        detector = new SimpleGestureFilter(this,this);


        swipeRefresLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                insertDetails();
                swipeRefresLayout.setRefreshing(false);
            }
        });
    }

    private void init()
    {
        swipeRefresLayout = findViewById(R.id.swipeRefresLayout);
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


        swipeRefresLayout.setColorSchemeResources(R.color.project_color);

    }

    private void fetchValues() {


        sqLiteSetPreference = new SQLiteSetPreference(getApplicationContext());


        if(sqLiteSetPreference.isPreferenceExistByUserId(userModel.getUserId()))
        {
            Cursor cursor_setPreference = sqLiteSetPreference.getDataByUserId(userModel.getUserId());
            if(cursor_setPreference!=null)
            {
                for (boolean hasItem = cursor_setPreference.moveToFirst(); hasItem; hasItem = cursor_setPreference.moveToNext())
                {
                    gender = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.GENDER));
                    stateIds = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.STATE_ID));
                    districtsIds = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.DISTRICT_ID));
                    talukaIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.TALUKA_ID));
                    ageMin  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.AGE_MIN));
                    ageMax  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.AGE_MAX));
                    religionIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.RELIGION_ID));
                    casteIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.CASTE_ID));
                    subCasteIds = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.SUB_CASTE_ID));
                    qualificationLevelIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.HIGHEST_QUALIFICATION_LEVEL_ID));
                    qualificationIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.HIGHEST_QUALIFICATION_ID));
                    maritalStatusIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.MARITAL_STATUS_ID));
                    dietIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.DIET_ID));
                    occupationIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.OCCUPATION_ID));
                    familyIncomeIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.EXPECTED_FAMILY_INCOME));
                    individualIncomeIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.EXPECTED_INDIVIDUAL_INCOME));
                    familyTypeIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.FAMILY_TYPE));
                    familyValueIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.FAMILY_VALUE));
                    heightMin  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.HEIGHT_MIN));
                    heightMax  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.HEIGHT_MAX));
                    colorIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.SKIN_COLOR_ID));


                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    runner.execute("InsertDetails");
                    return;

                }

            }

        }
        else
        {
            noValues();
        }


    }

    private void noValues() {
        gender = "0";
        stateIds = "0";
        districtsIds = "0";
        talukaIds  = "0";
        ageMin  = "0";
        ageMax  = "0";
        religionIds  = "0";
        casteIds  = "0";
        subCasteIds = "0";
        qualificationLevelIds  = "0";
        qualificationIds  = "0";
        maritalStatusIds  = "0";
        dietIds  = "0";
        occupationIds  = "0";
        familyIncomeIds  = "0";
        individualIncomeIds  = "0";
        familyTypeIds  = "0";
        familyValueIds  = "0";
        heightMin  = "0";
        heightMax  = "0";
        colorIds  = "0";


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertDetails");
        return;
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

        setSupportActionBar(toolbar);
/*        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(false);
       // actionBar.setTitle("");*/


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.main_toolbar);
        }
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

    private void insertDetails() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_FILTER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            Log.d("RESPONSE",response);
                            //converting response to json object
                            JSONObject jsonObject = new JSONObject(response);

                            JSONArray jsonArray = jsonObject.getJSONArray("Registrations");

                            if(jsonArray.length()>0)
                            {
                                timelineModelList.clear();
                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject userJson = jsonArray.getJSONObject(i);

                                    TimelineModel timelineModel = new TimelineModel();
                                    timelineModel.setUserName(userJson.getString("FullName"));
                                    timelineModel.setUserId(userJson.getString("UserId"));
                                    timelineModel.setProfilePic(userJson.getString("ImageUrl"));
                                    timelineModel.setUserBirthday(userJson.getString("DateOfBirthString"));
                                    timelineModel.setUserMobileNo(userJson.getString("MobileNo"));
                                    timelineModel.setUserOccupation(userJson.getString("OccupationName"));
                                    timelineModel.setUserQualification(userJson.getString("Qualification"));
                                    timelineModel.setUserEmail(userJson.getString("EmailId"));
                                    timelineModelList.add(timelineModel);
                                }
                                //   getDetails();
                                ///getProfilePic();

                                timelineAdapter.notifyDataSetChanged();
                                customDialogLoadingProgressBar.dismiss();

                               // Toast.makeText(HomeActivity.this,"Received successfully!", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(HomeActivity.this,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(HomeActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // params.put("UserId",userModel.getUserId());                 //1
                params.put("PageIndex", "1");                               //2
                params.put("PageSize", "100");                              //3
                params.put("Gender", gender);                               //4
                params.put("StateId", stateIds);                            //5
                params.put("DistrictId", districtsIds);                     //6
                params.put("TalukasId", talukaIds);                         //7
                params.put("AgeMin", ageMin);  //8
                params.put("AgeMax", ageMax); //9
                params.put("ReligionId", religionIds);                      //10
                params.put("CasteId", casteIds);                            //11
                params.put("SubCastId", subCasteIds);                       //12
                params.put("HightestQulificationLevelIds", qualificationLevelIds);  //13
                params.put("HightestQulificationIds", qualificationIds);    //14
                params.put("MaritalStatusIds", maritalStatusIds);            //15
                params.put("DietsIds", dietIds);                            //16
                params.put("OccupationsIds", occupationIds);                //17
                params.put("ExpectedFamilyIncome", familyIncomeIds);        //18
                params.put("ExpectedIndividualIncome", individualIncomeIds);//19
                params.put("FamilyType", familyTypeIds);                    //20
                params.put("FamilyValue", familyValueIds);                  //21
                params.put("Height", heightMin);//22
                params.put("HeightMax", heightMax);   //23
                params.put("SkinColourName", colorIds);                     //24

                return params;
            }
        };

        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);



    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {

            if(params[0].equals("InsertDetails"))
            {
                insertDetails();
            }
            else if(params[0].equals("getDetails"))
            {
                getDetails();
            }







            return null;
        }


        @Override
        protected void onPreExecute() {
            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(HomeActivity.this);
            customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    private void getDetails() {



    }


}
