package com.example.matrimonyapp.activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.DailyRecommendationsAdapter;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.example.matrimonyapp.adapter.RecentlyViewedAdapter;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomNavigationView;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.sqlite.SQLiteSetPreference;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;


public class HomeActivity extends AppCompatActivity  {//implements SimpleGestureFilter.SimpleGestureListener
    
    private String currentLanguage;
    private SwipeRefreshLayout swipeRefresLayout;

    private Toolbar toolbar;
    private UserModel userModel;

    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private RecyclerView drawerrecyview;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private ArrayList<NavigationItemListModel> activityListModelArrayList;

    public static HubConnection hubConnection;

    //private SimpleGestureFilter detector;

    TimelineAdapter timelineAdapter;
    RecentlyViewedAdapter recentlyViewedAdapter;
    DailyRecommendationsAdapter dailyRecommendationsAdapter;
    private ImageView imageView_home, imageView_search, imageView_message, imageView_like ,imageView_myProfile;

    private TextView textView_welcomeUserName;
    private ImageView imageView_profilePic;

    ArrayList<TimelineModel> timelineModelList, arrayList_recentlyviewed, arrayList_dailyRecommendations;
    RecyclerView recyclerView_timeline, recyclerView_recentlyViewed, recyclerView_dailyRecommendations;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    SQLiteSetPreference sqLiteSetPreference;

    static String gender, stateIds, countryIds, cityIds, maritalStatusIds, familyTypeIds, familyValueIds,
            familyIncomeIds, individualIncomeIds, qualificationLevelIds, qualificationIds, dietIds, colorIds,
            occupationIds, religionIds, casteIds, subCasteIds, ageMin, ageMax, heightMin, heightMax;


    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private CircleImageView circleImageView_headerProfilePic;
    private ExpandableListView expandableList;
    private DrawerLayout drawerLayout;

    LinearLayout linearChat, linearLayout_dailyRecommendations, linearLayout_recentlyViewed;

    CircleImageView circleImage_welcomeProfilePic, circleImage_progressProfilePic;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        init();

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 101);
            return;
        }
       // String IMEINumber = telephonyManager.getDeviceId();
        //textView.setText(IMEINumber);
        //Toast.makeText(this, " ln : "+userModel.getLanguage()+"\nIMEI : "+IMEINumber, Toast.LENGTH_SHORT).show();
        // timelineAdapter = new TimelineAdapter(this,)


        navigation();

        connect();

        setToolbar();


        linearChat=findViewById(R.id.linearChat);
        linearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, SignalRUserChatsActivity.class);
                startActivity(intent);
            }
        });



        fetchValues();

        timelineAdapter = new TimelineAdapter(this,timelineModelList, getWindowManager().getDefaultDisplay());
        recyclerView_timeline.setAdapter(timelineAdapter);
        recyclerView_timeline.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_timeline.setLayoutManager(mLayoutManager);
        //Toast.makeText(getApplicationContext(), " userId : "+userModel.getUserId(), Toast.LENGTH_SHORT).show();

        /*for(int i=0; i<5; i++)
        {
            TimelineModel timelineModel = new TimelineModel();
            timelineModel.setUserName("Dipti Gunjal");
            timelineModel.setUserId(4+"");
            timelineModel.setUserAge("26");
            timelineModel.setUserOccupation("HR");
            timelineModel.setUserCompany("Infosys");
            timelineModel.setUserQualification("MCOM");
            timelineModel.setProfilePic("Verifications/regiProfileImage/4.webp");
            arrayList_recentlyviewed.add(timelineModel);
        }*/

        recentlyViewedAdapter = new RecentlyViewedAdapter(HomeActivity.this,arrayList_recentlyviewed, getWindowManager().getDefaultDisplay());
        recyclerView_recentlyViewed.setAdapter(recentlyViewedAdapter);
        recyclerView_recentlyViewed.setHasFixedSize(true);
        LinearLayoutManager layoutManager_recentlyViewed = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_recentlyViewed.setLayoutManager(layoutManager_recentlyViewed);


        dailyRecommendationsAdapter = new DailyRecommendationsAdapter(HomeActivity.this,arrayList_dailyRecommendations, getWindowManager().getDefaultDisplay());
        recyclerView_dailyRecommendations.setAdapter(dailyRecommendationsAdapter);
        recyclerView_dailyRecommendations.setHasFixedSize(true);
        LinearLayoutManager layoutManager_dailyRecommendations = new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView_dailyRecommendations.setLayoutManager(layoutManager_dailyRecommendations);



        swipeRefresLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                insertDetails();
                swipeRefresLayout.setRefreshing(false);
            }
        });

        onClickListener();

    }


    private void connect() {

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
        TextView textView_editProfile= view_header.findViewById(R.id.textView_editProfile);
        LinearLayout linearLayout_navigationHeader = view_header.findViewById(R.id.linearLayout_navigationHeader);

        Glide.with(HomeActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.drawable.default_profile)
                .into(circleImageView_headerProfilePic);

        textView_welcomeUserName.setText(userModel.getFullName());
        //textView_emailId.setText(userModel.getEmailId());
        linearLayout_navigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        CustomNavigationView customNavigationView = new CustomNavigationView(HomeActivity.this,
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



    }



   
   
   


    private void init()
    {

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        swipeRefresLayout = findViewById(R.id.swipeRefresLayout);
        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_message = findViewById(R.id.imageView_message);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);
/*        imageView_home.setColorFilter(ContextCompat.getColor(this,R.color.white)); //home Activity highlight
        imageView_home.setBackgroundResource(R.drawable.gradient_place_order); //home Activity highlight*/

        imageView_home.setImageResource(R.drawable.filled_home);
        recyclerView_recentlyViewed = (RecyclerView) findViewById(R.id.recyclerView_recentlyViewed);
        recyclerView_timeline = (RecyclerView) findViewById(R.id.recyclerView_timeline);
        toolbar = findViewById(R.id.toolbar1);
        textView_welcomeUserName = findViewById(R.id.textView_welcomeUserName);
        imageView_profilePic = findViewById(R.id.imageView_profilePic);
        circleImage_welcomeProfilePic = findViewById(R.id.circleImage_welcomeProfilePic);
        circleImage_progressProfilePic = findViewById(R.id.circleImage_progressProfilePic);


        linearLayout_dailyRecommendations = findViewById(R.id.linearLayout_dailyRecommendations);
        linearLayout_recentlyViewed = findViewById(R.id.linearLayout_recentlyViewed);

        timelineModelList = new ArrayList<TimelineModel>();
        arrayList_recentlyviewed = new ArrayList<TimelineModel>();
        arrayList_dailyRecommendations = new ArrayList<TimelineModel>();


        recyclerView_recentlyViewed=findViewById(R.id.recyclerView_recentlyViewed);
        recyclerView_dailyRecommendations = findViewById(R.id.recyclerView_dailyRecommendations);

        if (!CustomSharedPreference.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(this).getUser();
//        textView_welcomeUserName.setText(userModel.getFullName());
        if( userModel!=null && !userModel.getProfilePic().equals("1"))
        {
            //imageView_profilePic.setImageURI();
        }



        ;
        Glide.with(HomeActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.color.quantum_grey100)
                .into(circleImage_welcomeProfilePic);

        Glide.with(HomeActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.color.quantum_grey100)
                .into(circleImage_progressProfilePic);


        firebaseAuth = FirebaseAuth.getInstance();
        firebaseLogin(userModel.getEmailId(),"123456");

        swipeRefresLayout.setColorSchemeResources(R.color.project_color);

       // register("","","");

    }


    private void onClickListener() {


        //onClickNewActivity(imageView_home, HomeActivity.this, HomeActivity.class);
        onClickNewActivity(imageView_search, HomeActivity.this, SetPreferencesActivity.class);
        onClickNewActivity(imageView_message, HomeActivity.this, DirectMessagesActivity.class);
        onClickNewActivity(imageView_myProfile, HomeActivity.this, MyProfileActivity.class);
        onClickNewActivity(imageView_like, HomeActivity.this, InterestActivity.class);
        onClickNewActivity(linearLayout_dailyRecommendations, HomeActivity.this, ViewAllActivity.class);



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

    private void firebaseLogin(String emailId, String password)
    {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                   /* Intent intent = new Intent(Home.this, HomeActivity.class);
                    startActivity(intent);
                    finish();*/

                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

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
                    countryIds = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.COUNTRY_ID));
                    cityIds  = cursor_setPreference.getString(cursor_setPreference.getColumnIndex(SQLiteSetPreference.CITY_ID));
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
        countryIds = "0";
        cityIds  = "0";
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

/*
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

                */
/*Intent i = new Intent(HomeActivity.this,LoginActivity.class);
                startActivity(i);*//*


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
*/



/*    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }*/

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
       /* actionBarDrawerToggle.syncState();*/
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        /*actionBarDrawerToggle.onConfigurationChanged(newConfig);*/
    }

/*    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.END)) {

            drawer.closeDrawer(Gravity.RIGHT); //OPEN Nav Drawer!
        } else {
            finish();
        }
    }*/


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
        /*findViewById(R.id.imageView_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Click", "keryu");

                if (drawer.isDrawerOpen(navigationView)) {
                    drawer.closeDrawer(navigationView);
                } else {
                    drawer.openDrawer(navigationView);
                }
            }
        });*/


    }


   /* public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.END);
        return true;


    }*/

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
                            arrayList_recentlyviewed.clear();
                            arrayList_dailyRecommendations.clear();

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
                                    //timelineModel.setUserOccupation(userJson.getString("OccupationName"));
                                    timelineModel.setUserOccupation("MBA-Marketing");
                                    //timelineModel.setUserQualification(userJson.getString("Qualification"));
                                    timelineModel.setUserQualification("MBA-Marketing");
                                    timelineModel.setUserEmail(userJson.getString("EmailId"));
                                    timelineModel.setUserAge(userJson.getString("Age"));
                                    timelineModel.setUserCompany("Infosys");
                                    timelineModel.setUserHeight(userJson.getString("Height"));
                                    timelineModel.setUserCity("Tamil Nadu, India");
                                    timelineModel.setUserReligion("Hindu");
                                    timelineModel.setUserMaritalStatus("Never Married");
                                    timelineModelList.add(timelineModel);

                                    arrayList_recentlyviewed.add(timelineModel);
                                    arrayList_dailyRecommendations.add(timelineModel);
                                }
                                //   getDetails();
                                ///getProfilePic();

                                timelineAdapter.notifyDataSetChanged();
                                customDialogLoadingProgressBar.dismiss();


                                recentlyViewedAdapter.notifyDataSetChanged();
                                dailyRecommendationsAdapter.notifyDataSetChanged();
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
                params.put("DistrictId", countryIds);                     //6
                params.put("TalukasId", cityIds);                         //7
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
