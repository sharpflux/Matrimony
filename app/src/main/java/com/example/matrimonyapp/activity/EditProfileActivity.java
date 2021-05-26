package com.example.matrimonyapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.example.matrimonyapp.adapter.ProfileTabLayoutAdapter;
import com.example.matrimonyapp.customViews.CustomDialogChangeProfilePic;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomViewPager;
import com.example.matrimonyapp.fragment.BlankFragment;
import com.example.matrimonyapp.fragment.PersonalDetailsFragment;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.tabs.TabLayout;
//import com.squareup.picasso.MemoryPolicy;
//import com.squareup.picasso.NetworkPolicy;
//import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditProfileActivity extends AppCompatActivity {

    private ImageView imageView_back, toolbarImageView;
    private CircleImageView circleImageView_profilePic, circleImage_profilePic;


    private RelativeLayout relativeLayout_changeProfilePic;


    private Handler mHandler;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    //Tabs
    TabLayout tabLayout_details;
    CustomViewPager viewPager_details;


    //Change Profile Pic
    private CustomDialogChangeProfilePic customDialogChangeProfilePic;
    private Intent intent_camera;
    public static final int REQUEST_CAMERA = 1, SELECT_FILE = 0, ACTIVITY_CONSTANT = 2;
    private String selfieString;
    public static final int RequestPermissionCode = 1;





    private UserModel userModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_profile);

        mHandler = new Handler();
        init();

        onClickListener();




        viewPager_details.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        tabLayout_details.setTabGravity(TabLayout.GRAVITY_FILL);
        final ProfileTabLayoutAdapter profileTabLayoutAdapter = new ProfileTabLayoutAdapter(
                this,getSupportFragmentManager(),tabLayout_details.getTabCount(), userModel.getUserId(),"EditProfile");
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




//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//        toolbar.setTitle("My Profile");

    }

    private void onClickListener() {

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        relativeLayout_changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFragment("Hellow");
                //enableRuntimePermission();
                //changeProfilepicDialog();

            }
        });


        //callFragment(linearLayout_basicDetails, "Basic");



    }

    private void callFragment(LinearLayout linearLayout, final String fragmentName) {

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragmentName",fragmentName);
                intent.putExtra("ShowBackButton","Yes");
                startActivity(intent);
            }
        });

    }


    public void enableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(EditProfileActivity.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] PResult) {

        switch (RC) {

            case RequestPermissionCode:

                if (PResult.length > 0 && PResult[0] == PackageManager.PERMISSION_GRANTED) {

                    // Toast.makeText(MyProfileActivity.this, "Permission Granted, Now your application can access CAMERA.", Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(EditProfileActivity.this, "Permission Canceled, Now your application cannot access CAMERA.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }




    private void changeProfilepicDialog() {

        customDialogChangeProfilePic.show();

        customDialogChangeProfilePic.textView_addFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogChangeProfilePic.dismiss();
                intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, REQUEST_CAMERA);
            }
        });


        customDialogChangeProfilePic.textView_addFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogChangeProfilePic.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode)
            {
                case SELECT_FILE:
                    onSelectFromGalleryResult(data);
                    break;
                case REQUEST_CAMERA:
                    onCaptureImageResult(data);
                    break;

            }


        }


    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();


        Bitmap newBitmap = Bitmap.createBitmap(thumbnail.getWidth(), thumbnail.getHeight(), thumbnail.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(thumbnail, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        selfieString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);


        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertProfilePic");

    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bm, 0, 0, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

            selfieString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        }
        circleImageView_profilePic.setImageBitmap(bm);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertProfilePic");

    }




    private void init() {


        imageView_back = findViewById(R.id.backButton);
        toolbarImageView = findViewById(R.id.toolbarImageView);
        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);
        circleImage_profilePic = findViewById(R.id.circleImage_profilePic);

        viewPager_details = findViewById(R.id.viewPager_details);
        tabLayout_details = findViewById(R.id.tabLayout_details);


        /*textView_name = findViewById(R.id.textView_name);
        textView_birthdate = findViewById(R.id.textView_birthdate);
        textView_mobileNo = findViewById(R.id.textView_mobileNo);
        textView_emailId = findViewById(R.id.textView_emailId);
        textView_country = findViewById(R.id.textView_country);
        textView_state = findViewById(R.id.textView_state);
        textView_city = findViewById(R.id.textView_city);
        textView_village = findViewById(R.id.textView_village);
        textView_address = findViewById(R.id.textView_address);
        textView_postalCode = findViewById(R.id.textView_postalCode);


        textView_religion = findViewById(R.id.textView_religion);
        textView_caste = findViewById(R.id.textView_caste);
        textView_gothram = findViewById(R.id.textView_gothram);
        textView_motherToungue = findViewById(R.id.textView_motherToungue);


        textView_height = findViewById(R.id.textView_height);
        textView_weight = findViewById(R.id.textView_weight);
        textView_colour = findViewById(R.id.textView_colour);
        textView_maritalStatus = findViewById(R.id.textView_maritalStatus);
        textView_livesWithFamily = findViewById(R.id.textView_livesWithFamily);


        textView_highestQualificationLevel = findViewById(R.id.textView_highestQualificationLevel);
        textView_highestQualification = findViewById(R.id.textView_highestQualification);
        textView_nameOfInstitute = findViewById(R.id.textView_nameOfInstitute);
        textView_percentage = findViewById(R.id.textView_percentage);
        textView_passingYear = findViewById(R.id.textView_passingYear);


        textView_currentService = findViewById(R.id.textView_currentService);
        textView_designation = findViewById(R.id.textView_designation);
        textView_experience = findViewById(R.id.textView_experience);
        textView_annualIncome = findViewById(R.id.textView_annualIncome);


        textView_fatherName = findViewById(R.id.textView_fatherName);
        textView_fatherOccupation = findViewById(R.id.textView_fatherOccupation);
        textView_fatherIncome = findViewById(R.id.textView_fatherIncome);
        textView_motherName = findViewById(R.id.textView_motherName);
        textView_motherOccupation = findViewById(R.id.textView_motherOccupation);
        textView_motherIncome = findViewById(R.id.textView_motherIncome);
*/

        relativeLayout_changeProfilePic = findViewById(R.id.relativeLayout_changeProfilePic);
        /*linearLayout_basicDetails = findViewById(R.id.linearLayout_basicDetails);
        linearLayout_religiousDetails = findViewById(R.id.linearLayout_religiousDetails);
        linearLayout_personalDetails = findViewById(R.id.linearLayout_personalDetails);
        linearLayout_qualificationDetails = findViewById(R.id.linearLayout_qualificationDetails);
        linearLayout_professionalDetails = findViewById(R.id.linearLayout_professionalDetails);
        linearLayout_familyDetails = findViewById(R.id.linearLayout_familyDetails);*/



        userModel = CustomSharedPreference.getInstance(EditProfileActivity.this).getUser();
        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(EditProfileActivity.this);
        customDialogChangeProfilePic = new CustomDialogChangeProfilePic(EditProfileActivity.this);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");


    }



    void insertProfilePic()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,URLs.URL_POST_PROFILEPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {

                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        getProfilePic();
                                    }
                                });


                                //   getDetails();

                                //Picasso.get().(userModel.getProfilePic()).into(circleImageView_profilePic);

                                // userModel.setProfilePic(URLs.MainURL+jsonObject.getString("ImageUrl"));
                                Toast.makeText(EditProfileActivity.this,"Profile Pic updated successfully!", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(EditProfileActivity.this,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();


                params.put("UserId",userModel.getUserId());
                params.put("ProfileImage",selfieString);


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }

    void getProfilePic()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URLs.URL_GET_PROFILEPIC + "UserId=" + userModel.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            customDialogLoadingProgressBar.dismiss();
                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()==1)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {

                                    //ImageUrl - 0 or 1 :- default img
                                    if(!(URLs.MainURL+jsonObject.getString("ImageUrl")).equals("1")
                                            && !(URLs.MainURL+jsonObject.getString("ImageUrl")).equals("0"))
                                    {
                                        Glide.with(EditProfileActivity.this)
                                        .load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .placeholder(R.color.codeGray)
                                                .skipMemoryCache(true)
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .into(circleImageView_profilePic);
                                        Glide.with(EditProfileActivity.this)
                                                .load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .placeholder(R.color.codeGray)
                                                .skipMemoryCache(true)
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .into(toolbarImageView);
                                        Glide.with(EditProfileActivity.this)
                                                .load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .placeholder(R.color.codeGray)
                                                .skipMemoryCache(true)
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .into(circleImage_profilePic);

                                        //to clear cache
/*
                                        Picasso.get().invalidate(URLs.MainURL + jsonObject.getString("ImageUrl"));
                                        Picasso.get().load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .error(R.drawable.default_profile)
                                                .placeholder(R.color.codeGray)
                                                .into(circleImageView_profilePic);                                        Picasso.get().invalidate(URLs.MainURL + jsonObject.getString("ImageUrl"));
                                        Picasso.get().load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .error(R.drawable.default_profile)
                                                .placeholder(R.color.quantum_bluegrey900)
                                                .into(toolbarImageView);
*/


/*
                                        Picasso.get().load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .error(R.drawable.default_profile)
                                                .placeholder(R.color.quantum_bluegrey900)
                                                .into(circleImage_profilePic);
*/

                                        userModel.setProfilePic(URLs.MainURL+jsonObject.getString("ImageUrl"));


                                    }
                                    else{
                                        userModel.setProfilePic("");
                                    }

                                }
                                else
                                {
                                    Toast.makeText(EditProfileActivity.this, "Error while loading Profile pic! ", Toast.LENGTH_SHORT).show();
                                }

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return super.getParams();
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

    }




    private void checkIfEmpty(TextView textView, String value) {

        value = value.trim();

        if(value.isEmpty() || value.equals("0") || value.equals(""))
        {
            textView.setText(getResources().getString(R.string.not_filled));
        }
        else
        {
            textView.setText(value);
        }

    }

    public void openFragment(String text) {
        BlankFragment fragment = BlankFragment.newInstance(text);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.add(R.id.fragment_container, fragment, "BLANK_FRAGMENT").commit();
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {

            if (params[0].equals("getDetails"))
            {
                getProfilePic();


            }
            else if(params[0].equals("InsertProfilePic"))
            {
                insertProfilePic();
            }


            return params[0];
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