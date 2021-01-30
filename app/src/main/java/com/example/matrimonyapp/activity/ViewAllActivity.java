package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomNavigationView;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewAllActivity extends AppCompatActivity {

    private TextView textView_title;

    private RecyclerView recyclerView_profiles;
    private TimelineAdapter timelineAdapter;
    private ArrayList<TimelineModel> timelineModelList;

    private UserModel userModel;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    private DrawerLayout drawerLayout;
    private ExpandableListView expandableList;
    private CircleImageView circleImageView_headerProfilePic;
    private TextView textView_welcomeUserName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        init();

        textView_title.setText(getResources().getString(R.string.recently_viewed_profiles));


        timelineAdapter = new TimelineAdapter(this,timelineModelList, getWindowManager().getDefaultDisplay());
        recyclerView_profiles.setAdapter(timelineAdapter);
        recyclerView_profiles.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView_profiles.setLayoutManager(layoutManager);



    }

    private void init() {

        userModel = CustomSharedPreference.getInstance(this).getUser();

        textView_title = findViewById(R.id.loginTitle);
        recyclerView_profiles = findViewById(R.id.recyclerView_profiles);

        timelineModelList = new ArrayList<>();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(ViewAllActivity.this);

        navigation();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("insertDetails");


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

        Glide.with(ViewAllActivity.this)
                .load(URLs.MainURL+userModel.getProfilePic())
                .placeholder(R.drawable.default_profile)
                .into(circleImageView_headerProfilePic);

        textView_welcomeUserName.setText(userModel.getFullName());
        //textView_emailId.setText(userModel.getEmailId());
        linearLayout_navigationHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAllActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });
        CustomNavigationView customNavigationView = new CustomNavigationView(ViewAllActivity.this,
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

                                }

                                timelineAdapter.notifyDataSetChanged();
                                customDialogLoadingProgressBar.dismiss();



                            }
                            else
                            {
                                Toast.makeText(ViewAllActivity.this,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewAllActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                // params.put("UserId",userModel.getUserId());                 //1
                params.put("PageIndex", "1");                               //2
                params.put("PageSize", "100");                              //3
                params.put("Gender", "0");                               //4
                params.put("StateId", "0");                            //5
                params.put("DistrictId", "0");                     //6
                params.put("TalukasId", "0");                         //7
                params.put("AgeMin", "0");  //8
                params.put("AgeMax", "0"); //9
                params.put("ReligionId", "0");                      //10
                params.put("CasteId", "0");                            //11
                params.put("SubCastId", "0");                       //12
                params.put("HightestQulificationLevelIds", "0");  //13
                params.put("HightestQulificationIds", "0");    //14
                params.put("MaritalStatusIds", "0");            //15
                params.put("DietsIds", "0");                            //16
                params.put("OccupationsIds", "0");                //17
                params.put("ExpectedFamilyIncome", "0");        //18
                params.put("ExpectedIndividualIncome", "0");//19
                params.put("FamilyType", "0");                    //20
                params.put("FamilyValue", "0");                  //21
                params.put("Height", "0");//22
                params.put("HeightMax", "0");   //23
                params.put("SkinColourName", "0");                     //24

/*
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

*/
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ViewAllActivity.this).addToRequestQueue(stringRequest);




    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {

            if (params[0].equals("insertDetails"))
            {
                insertDetails();
            }

            return params[0];
        }

        @Override
        protected void onPreExecute()
        {
            customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s)
        {

        }
    }

}