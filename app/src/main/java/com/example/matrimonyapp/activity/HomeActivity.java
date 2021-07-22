package com.example.matrimonyapp.activity;

import android.Manifest;
import android.app.ActivityManager;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
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
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.DailyRecommendationsAdapter;
import com.example.matrimonyapp.adapter.NavigationDrawerAdapter;
import com.example.matrimonyapp.adapter.RecentlyViewedAdapter;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomNavigationView;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.NavigationItemListModel;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatJobService;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.service.SensorService;
import com.example.matrimonyapp.sqlite.SQLiteRecentlyViewedProfiles;
import com.example.matrimonyapp.sqlite.SQLiteSetPreference;
import com.example.matrimonyapp.sqlite.SQLiteVehicleDetails;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationView;
/*import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;*/
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
    private RelativeLayout relativeLayout_message;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;
    private RecyclerView drawerrecyview;
    private NavigationDrawerAdapter navigationDrawerAdapter;
    private ArrayList<NavigationItemListModel> activityListModelArrayList;

    private SQLiteRecentlyViewedProfiles sqLiteRecentlyViewedProfiles;

    public static HubConnection hubConnection;

    //private SimpleGestureFilter detector;

    TimelineAdapter timelineAdapter;
    public static RecentlyViewedAdapter recentlyViewedAdapter;
    DailyRecommendationsAdapter dailyRecommendationsAdapter;
    private ImageView imageView_home, imageView_search, imageView_message, imageView_like ,imageView_myProfile;

    private TextView textView_welcomeUserName;
    private ImageView imageView_profilePic;

    ArrayList<TimelineModel> timelineModelList,  arrayList_dailyRecommendations;
    public  static ArrayList<TimelineModel> arrayList_recentlyviewed;
    RecyclerView recyclerView_timeline, recyclerView_recentlyViewed, recyclerView_dailyRecommendations;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    SQLiteSetPreference sqLiteSetPreference;

    static String gender, maritalStatusIds, familyTypeIds, familyValueIds,
            familyIncomeIds, individualIncomeIds, qualificationLevelIds, qualificationIds, dietIds, colorIds,
            occupationIds, religionIds, casteIds, subCasteIds, ageMin, ageMax, heightMin, heightMax,
            currentService, serviceType, workingIn, residentialCountryIds, residentialStateIds, residentialCityIds, workingCountryIds, workingStateIds, workingCityIds ;

    private Handler mHandler;

    private ComponentName serviceComponentName;
    //FirebaseAuth firebaseAuth;
    //DatabaseReference databaseReference;
    private CircleImageView circleImageView_headerProfilePic;
    private ExpandableListView expandableList;
    private DrawerLayout drawerLayout;

    LinearLayout linearChat, linearLayout_dailyRecommendations, linearLayout_recentlyViewed;

    CircleImageView circleImage_welcomeProfilePic, circleImage_progressProfilePic;


    MyReceiver myReceiver;
    ChatService chatService;
    boolean mBound = false;

    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    private static final String TAG = MainActivity.class.getSimpleName();

    public Context getCtx() {
        return ctx;
    }




    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "notifyAdapter":


                    break;
                case "getallMessages":

                    break;

                case "UserList":

                    break;
            }
        }
    } // MyReceiver
    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {

        try {
            if(myReceiver!=null)
                unregisterReceiver(myReceiver);

            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    public void scheduleJob(View v) {
       JobInfo.Builder builder = new JobInfo.Builder(0, serviceComponentName);
        builder.setPersisted(true);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            builder.setPeriodic(1*60*1000,60*60*10000);
        }else {
            builder.setPeriodic(1*60*1000);
        }
        // Start the job
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        // start and get the result
        int jobResult = scheduler.schedule(builder.build());

        if(jobResult == JobScheduler.RESULT_FAILURE) {
            Log.d("TAG", "Job failed to start");
        }else if(jobResult == JobScheduler.RESULT_SUCCESS){

            Log.d("TAG", "Job Running");
        }


    }


    @Override
    protected void onDestroy() {
        Log.i(TAG, "onDestroy");

        // ondestroy service not being called
        stopService(mServiceIntent);

        super.onDestroy();
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        //scheduler.cancel(0);
        List<JobInfo> jobs = scheduler.getAllPendingJobs();

        if(jobs.isEmpty()){
            Log.d("TAG", "No Job to cancel")   ;
        }else{
            int id = jobs.get(0).getId();

            scheduler.cancel(id);
            Log.d("TAG", "Job stopped");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mHandler = new Handler();
        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(HomeActivity.this);

        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        ctx = this;
        serviceComponentName = new ComponentName(this, ChatJobService.class);
        mHandler.post(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    public void run(){

                        mSensorService = new SensorService(getCtx());
                        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
                        if( ! isMyServiceRunning(mSensorService.getClass())) {
                            startService(mServiceIntent);
                        }
                    }
                }).start();



            }
        });



        init();


        mHandler.post(new Runnable() {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    public void run(){

                        FirebaseMessaging.getInstance().getToken() .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                String token = task.getResult();

                                //Toast.makeText(HomeActivity.this, token, Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).start();



            }
        });


        FirebaseMessaging.getInstance().subscribeToTopic("All");
        FirebaseMessaging.getInstance().subscribeToTopic(userModel.getUserId());


        navigation();

        //connect();

        setToolbar();


        linearChat=findViewById(R.id.linearChat);
        linearChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(HomeActivity.this, SubscriptionActivity.class);
                startActivity(intent);
            }
        });




        swipeRefresLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute("GetDetails");
                    }
                });
                swipeRefresLayout.setRefreshing(false);
            }
        });

        onClickListener();

    }


    private boolean isMyServiceRunning(Class <?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "isMyServiceRunning? " + true + "");
                return true;
            }
        }

        Log.i(TAG, "isMyServiceRunning? " + false + "");
        return false;
    }

    private void connect() {
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notifyAdapter");
        registerReceiver(myReceiver, intentFilter);
    }
    private void navigation()
    {


        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        expandableList = (ExpandableListView) findViewById(R.id.navigationmenu);
        final NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);


        View view_header = navigationView.getHeaderView(0);

        circleImageView_headerProfilePic = view_header.findViewById(R.id.circleImageView_profilePic);
        textView_welcomeUserName = view_header.findViewById(R.id.textView_welcomeUserName);
        TextView textView_editProfile= view_header.findViewById(R.id.textView_editProfile);
        LinearLayout linearLayout_navigationHeader = view_header.findViewById(R.id.linearLayout_navigationHeader);

        Glide.with(HomeActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.drawable.default_profile)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(circleImageView_headerProfilePic);

        textView_welcomeUserName.setText(userModel.getFullName());

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

        relativeLayout_message = findViewById(R.id.relativeLayout_message);


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

        timelineAdapter = new TimelineAdapter(this,timelineModelList, getWindowManager().getDefaultDisplay());
        recyclerView_timeline.setAdapter(timelineAdapter);
        recyclerView_timeline.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView_timeline.setLayoutManager(mLayoutManager);


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



        if (!CustomSharedPreference.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(this).getUser();

        Glide.with(HomeActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.color.quantum_grey100)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(circleImage_welcomeProfilePic);

        Glide.with(HomeActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.color.quantum_grey100)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(circleImage_progressProfilePic);


        /*firebaseAuth = FirebaseAuth.getInstance();
        firebaseLogin(userModel.getEmailId(),"123456");*/


        sqLiteRecentlyViewedProfiles = new SQLiteRecentlyViewedProfiles(HomeActivity.this);

        Cursor cursor = sqLiteRecentlyViewedProfiles.getAllData();
        if (cursor != null)
        {
            Log.d("CURSOR","\n\n----------------------------\n"+
                    cursor.toString()+"\n----------------------------\n\n");
            arrayList_recentlyviewed.clear();
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                TimelineModel timelineModel = new TimelineModel();
                timelineModel.setUserId(cursor.getString(1));
                timelineModel.setUserName(cursor.getString(2));
                timelineModel.setProfilePic(cursor.getString(3));
                timelineModel.setUserCity(cursor.getString(4));
                arrayList_recentlyviewed.add(timelineModel);
            }
            recentlyViewedAdapter.notifyDataSetChanged();
            cursor.close();
        }


        //sqLiteRecentlyViewedProfiles.onDatabaseChanged();



        swipeRefresLayout.setColorSchemeResources(R.color.project_color);

       AsyncTaskRunner runner = new AsyncTaskRunner();
       runner.execute("GetDetails");
        AsyncTaskRunner profileChecker = new AsyncTaskRunner();
        profileChecker.execute("ProfileChecker");
    }


    private void onClickListener() {

        onClickNewActivity(imageView_search, HomeActivity.this, SetPreferencesActivity.class);
        //onClickNewActivity(imageView_message, HomeActivity.this, DirectMessagesActivity.class);
        onClickNewActivity(imageView_myProfile, HomeActivity.this, MyProfileActivity.class);
        onClickNewActivity(imageView_like, HomeActivity.this, InterestActivity.class);
        onClickNewActivity(linearLayout_dailyRecommendations, HomeActivity.this, ViewAllActivity.class);
        onClickNewActivity(linearLayout_recentlyViewed, HomeActivity.this, ViewAllActivity.class);


        relativeLayout_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(HomeActivity.this, SignalRUserChatsActivity.class);
                startActivity(intent);

            }
        });


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

    /*private void firebaseLogin(String emailId, String password)
    {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                   *//* Intent intent = new Intent(Home.this, HomeActivity.class);
                    startActivity(intent);
                    finish();*//*

                }

            }
        });
    }*/


    @Override
    protected void onResume() {
        super.onResume();
        connect();
        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }
/*

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
*/





    private void setToolbar() {

        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.main_toolbar);
        }

    }




    private void insertDetails() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_GETPROFILES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            Log.d("RESPONSE",response);
                            //converting response to json object
                            //JSONObject jsonObject = new JSONObject(response);
                            //arrayList_recentlyviewed.clear();
                            arrayList_dailyRecommendations.clear();

                            JSONArray jsonArray =new JSONArray(response);

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
                                    timelineModel.setUserGender(userJson.getString("Gender"));
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
                                    timelineModel.setInterested(userJson.getString("Interested"));
                                    timelineModel.setFavorites(userJson.getString("Favorites"));
                                    timelineModel.setRejected(userJson.getString("Rejected"));
                                    timelineModelList.add(timelineModel);


                                   // arrayList_recentlyviewed.add(timelineModel);
                                    arrayList_dailyRecommendations.add(timelineModel);
                                }
                                //   getDetails();
                                ///getProfilePic();

                                timelineAdapter.notifyDataSetChanged();
                                customDialogLoadingProgressBar.dismiss();


                                //recentlyViewedAdapter.notifyDataSetChanged();
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

                params.put("PageIndex :","1");
                params.put("PageSize :","50");
                params.put("UserId :",userModel.getUserId());


/*
                params.put("SetPreferenceId","0");
                params.put("UserId",userModel.getUserId());

                params.put("LookingFor", gender);
                params.put("RelegionIds", religionIds);
                params.put("CasteIds", casteIds);
                params.put("SubCasteIds", subCasteIds);
                params.put("ResidentialCountryIds", residentialCountryIds);
                params.put("ResidentialStateIds", residentialStateIds);
                params.put("ResidentialCityIds", residentialCityIds);
                params.put("WorkingCountryIds", workingCountryIds);
                params.put("WorkingStateIds", workingStateIds);
                params.put("WorkingCityIds", workingCityIds);


                params.put("AgeMin", ageMin);  //8
                params.put("AgeMax", ageMax); //9
                //12
                params.put("HeightMin", heightMin);//22
                params.put("HeightMax", heightMax);   //23

                params.put("QualificationLevelIds", qualificationLevelIds);  //13
                params.put("QualificationIds", qualificationIds);    //14
                params.put("ServiceType", serviceType);
                params.put("WorkingIn", workingIn);
                params.put("CurrentService", currentService);
                params.put("OccupationIds", occupationIds);                //17

                params.put("ExpectedFamilyIncomeIds", familyIncomeIds);        //18
                params.put("ExpectedIndividualIncomeIds", individualIncomeIds);//19
                params.put("FamilyTypeIds", familyTypeIds);                    //20
                params.put("FamilyValuesIds", familyValueIds);                  //21
                params.put("DietIds", dietIds);                            //16
                params.put("ColorIds", colorIds);                     //24
                params.put("MaritalStatusIds", maritalStatusIds);            //15



*/




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

            }
            if(params[0].equals("GetDetails"))
            {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        insertDetails();
                    }
                });

            }
            if(params[0].equals("ProfileChecker"))
            {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        new Thread(new Runnable() {
                            public void run(){
                                ProfileChecker();
                            }
                        }).start();



                    }
                });

            }

            return null;
        }


        @Override
        protected void onPreExecute() {
                customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            customDialogLoadingProgressBar.dismiss();
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


    private void ProfileChecker() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PROFILECHECKER+userModel.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            Log.d("RESPONSE",response);

                            arrayList_dailyRecommendations.clear();

                            JSONArray jsonArray =new JSONArray(response);
                            customDialogLoadingProgressBar.dismiss();
                            if(jsonArray.length()>0)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                if(!jsonObject.getString("FragmentName").equals("COMPLETED")){
                                    CustomSharedPreference customSharedPreference = CustomSharedPreference.getInstance(HomeActivity.this);
                                    UserModel userModel = customSharedPreference.getUser();
                                    if(!jsonObject.getString("FragmentName").equals("Family")){
                                        userModel.setUserType("NewUser");
                                        customSharedPreference.saveUser(userModel);
                                        Toast.makeText(getApplicationContext(), "Please complete profile first", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                        intent.putExtra("fragmentName",jsonObject.getString("FragmentName"));
                                        intent.putExtra("ShowBackButton","No");
                                        startActivity(intent);
                                        finish();

                                    }
                                    else{

                                        userModel.setUserType("OldUser");
                                        customSharedPreference.saveUser(userModel);

                                    }

                                }

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
                params.put("UserId :",userModel.getUserId());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(HomeActivity.this).addToRequestQueue(stringRequest);
    }



}
