package com.example.matrimonyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ImageSliderAdapter;
import com.example.matrimonyapp.adapter.ProfileTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.example.matrimonyapp.modal.SingleImage;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewProfileDetailsActivity extends AppCompatActivity {

    ImageView imageView_home, imageView_search, imageView_addPhoto, imageView_like ,imageView_profilePic,
            imageView_back;
    ArrayList<Uri> mArrayUri;

    Uri uri;
    private Bundle bundle;

    private UserModel userModel;
    boolean follow_flag=false;
    ArrayList<SingleImage>  arrayList_singleImage;

    ImageView toolbarImageView;
    CircleImageView circleImage_profilePic;
    RelativeLayout relativeLayout_image;
    private TextView textView_name;
    //private TextView textView_nameAgeTitle, textView_designationName, textView_companyName, textView_highestQualificationInstitute;

    TabLayout tabLayout_details;
    CustomViewPager viewPager_details;
    //TextView textView_follow, textView_following;
    LinearLayout linearLayout_privateAccount;
    View view_include_onFollow, view_include_unfollowed;

    private String userId="0";

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    //ViewPager2 viewPager2_singleImage;
    Handler sliderHandler = new Handler();

    private int BLUR_PRECENTAGE = 50;
    private String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile_details);


        init();


        bundle = getIntent().getExtras();

        if(bundle!=null)
        {
            userId = bundle.getString("userId");


            Glide.with(ViewProfileDetailsActivity.this)
                    .load(URLs.MainURL+bundle.getString("userProfilePic"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.color.quantum_grey100)
                    .into(circleImage_profilePic);


            Glide.with(ViewProfileDetailsActivity.this)
                    .load(URLs.MainURL+bundle.getString("userProfilePic"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.color.quantum_grey100)
                    .into(toolbarImageView);

            //textView_name.setText(bundle.getString("userName"));
            /*textView_nameAgeTitle.setText(bundle.getString("userName")+", "+bundle.getString("userAge")+"yrs");
            textView_designationName.setText(bundle.getString("userOccupation"));
            textView_companyName.setText(bundle.getString("userCompany"));
            textView_highestQualificationInstitute.setText(bundle.getString("userQualification"));

            Glide.with(ViewProfileDetailsActivity.this)
                    .load(URLs.MainURL+bundle.getString("userProfilePic"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.color.quantum_grey100)
                    .into(imageView_profilePic);
*/
        }





        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        viewPager_details.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_details.setTabGravity(TabLayout.GRAVITY_FILL);
        final ProfileTabLayoutAdapter profileTabLayoutAdapter = new ProfileTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_details.getTabCount(), userId);
        viewPager_details.setAdapter(profileTabLayoutAdapter);



        viewPager_details.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout_details));

        tabLayout_details.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager_details.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower2)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower2)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower2));

        arrayList_singleImage = new ArrayList<SingleImage>();

        ArrayList<String> sliderImages = new ArrayList<>();
        for(int i=0; i<5; i++)
        {
            //mArrayUri.add(uri);
            //SingleImage singleImage = new SingleImage(uri.toString());
            sliderImages.add(bundle.getString("userProfilePic"));

        }

        /*viewPager2_singleImage.setAdapter(new ImageSliderAdapter(ViewProfileDetailsActivity.this,
                sliderImages,viewPager2_singleImage));

        viewPager2_settings();*/

/*

        arrayList_horizontalImage = new ArrayList<SingleImage>();

        for(int i=0; i<9; i++) {
            mArrayUri.add(uri);
            SingleImage singleImage = new SingleImage(uri.toString());
            arrayList_horizontalImage.add(singleImage);

        }

        */
        /*gestureDetector = new GestureDetector(this,new MyProfileActivity.Gesture());*//*


        uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower1)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower1)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower1));

        arrayList_horizontalImage.get(0).setUri(uri.toString());
        arrayList_horizontalImage.get(8).setUri(uri.toString());

        horizontalImageAdapter = new HorizontalImageAdapter(this, arrayList_horizontalImage);
        LinearLayoutManager horizontalLayourManager = new LinearLayoutManager(ViewProfileActivity.this,
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView_horizontalImages.setLayoutManager(horizontalLayourManager);
        recyclerView_horizontalImages.setAdapter(horizontalImageAdapter);
*/

/*

        textView_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(follow_flag == true)
                {

                    view_include_onFollow.setVisibility(View.GONE);
                    view_include_unfollowed.setVisibility(View.VISIBLE);
                    follow_flag=false;
                }
            }
        });
        textView_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(follow_flag == false)
                {
                    view_include_onFollow.setVisibility(View.VISIBLE);
                    view_include_unfollowed.setVisibility(View.GONE);
                    follow_flag=true;
                }
                else
                {
                    view_include_onFollow.setVisibility(View.GONE);
                    view_include_unfollowed.setVisibility(View.INVISIBLE);
                    follow_flag=false;

                }


            }
        });


*/


/*        galleryAdapter = new GalleryAdapter(this,mArrayUri,ViewProfileActivity.this,getResources());
        gvGallery.setAdapter(galleryAdapter);
        gvGallery.setVerticalSpacing(gvGallery.getHorizontalSpacing());
        ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) gvGallery
                .getLayoutParams();
        mlp.setMargins(0, 5, 0, 0);//2nd para gvGallery.getHorizontalSpacing()


        setGridViewHeight(gvGallery,3);*/


    }



    private void init() {
        mArrayUri = new ArrayList<Uri>();

        userModel = CustomSharedPreference.getInstance(ViewProfileDetailsActivity.this).getUser();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(ViewProfileDetailsActivity.this);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        toolbarImageView = findViewById(R.id.toolbarImageView);
        viewPager_details = findViewById(R.id.viewPager_details);
        tabLayout_details = findViewById(R.id.tabLayout_details);
        textView_name = findViewById(R.id.toolbarTitle);
        circleImage_profilePic = findViewById(R.id.circleImage_profilePic);
/*        textView_follow = findViewById(R.id.textView_follow);
        textView_following = findViewById(R.id.textView_following);*/
/*        linearLayout_privateAccount = findViewById(R.id.linearLayout_privateAccount);
        view_include_onFollow = findViewById(R.id.include_onFollowingProfile);
        view_include_unfollowed = findViewById(R.id.include_unfollowedProfile);

        textView_nameAgeTitle = findViewById(R.id.textView_nameAgeTitle);
        textView_designationName = findViewById(R.id.textView_designationName);
        textView_companyName = findViewById(R.id.textView_companyName);
        textView_companyName = findViewById(R.id.textView_companyName);
        textView_highestQualificationInstitute = findViewById(R.id.textView_highestQualificationInstitute);*/


/*        imageView_home = findViewById(R.id.imageView_home);
        imageView_search = findViewById(R.id.imageView_search);
        imageView_addPhoto= findViewById(R.id.imageView_addPhoto);
        imageView_like = findViewById(R.id.imageView_like);
        imageView_myProfile = findViewById(R.id.imageView_myProfile);*/
        imageView_back = findViewById(R.id.backButton);
        //imageView_home.setColorFilter(ContextCompat.getColor(this,R.color.project_color));
        imageView_profilePic = findViewById(R.id.imageView_profilePic);
        //recyclerView_horizontalImages = findViewById(R.id.recyclerView_horizontalImages);
      //  viewPager2_singleImage = findViewById(R.id.viewPager2_singleImage);
        relativeLayout_image = findViewById(R.id.relativeLayout_image);


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute();

    }


    /*public void viewPager2_settings()
    {
        viewPager2_singleImage.setCurrentItem(1);

        viewPager2_singleImage.setClipToPadding(false);
        viewPager2_singleImage.setClipChildren(false);
        viewPager2_singleImage.setOffscreenPageLimit(3);
        viewPager2_singleImage.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });


        viewPager2_singleImage.setPageTransformer(compositePageTransformer);

        viewPager2_singleImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });


    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2_singleImage.setCurrentItem(viewPager2_singleImage.getCurrentItem()+1);
        }
    };
*/

   /* @Override
    protected void onPause() {
        super.onPause();

        sliderHandler.removeCallbacks(sliderRunnable);
    }*/
    @Override
    protected void onResume() {
        super.onResume();



        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


    void getBasicDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_BASICDETAILS+"UserId="+userId+"&Language="+userModel.getLanguage(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if(jsonArray.length()>0)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if(!jsonObject.getBoolean("error"))
                                {
                                    //basicDetailsId = jsonObject.getInt("BasicDetailsId");


                                    textView_name.setText(jsonObject.getString("FullName"));

                                    Glide.with(ViewProfileDetailsActivity.this)
                                            .load(URLs.MainURL+jsonObject.getString("ProfileImage"))
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .placeholder(R.color.quantum_grey100)
                                            .into(circleImage_profilePic);

                                    Glide.with(ViewProfileDetailsActivity.this)
                                            .load(URLs.MainURL+jsonObject.getString("ProfileImage"))
                                            .skipMemoryCache(true)
                                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                                            .placeholder(R.color.quantum_grey100)
                                            .into(toolbarImageView);



                                   /* checkIfEmpty(textView_birthdate, jsonObject.getString("Birthdate"));
                                    checkIfEmpty(textView_mobileNo, jsonObject.getString("MobileNo"));
                                    checkIfEmpty(textView_emailId, jsonObject.getString("EmailId"));


                                    checkIfEmpty(textView_country, jsonObject.getString("PermanantCountryName"));
                                    checkIfEmpty(textView_state, jsonObject.getString("PermanantState"));
                                    checkIfEmpty(textView_city, jsonObject.getString("PermanantCity"));
                                    checkIfEmpty(textView_address, jsonObject.getString("PermanantAddress"));
                                    checkIfEmpty(textView_village, jsonObject.getString("PermanantVillage"));
                                    checkIfEmpty(textView_postalCode, jsonObject.getString("PermanantPostCode"));
*/





                                }



                            }
                            else
                            {

                                //Toast.makeText(EditProfileActivity.this,"Please enter your details! ",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ViewProfileDetailsActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ViewProfileDetailsActivity.this).addToRequestQueue(stringRequest);
    }
    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... strings) {


            getBasicDetails();


            return null;


        }

        @Override
        protected void onPreExecute() {
            customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

}