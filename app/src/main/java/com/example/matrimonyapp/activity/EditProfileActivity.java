package com.example.matrimonyapp.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

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
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogChangeProfilePic;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.fragment.PersonalDetailsFragment;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

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

    private ImageView imageView_back;
    private CircleImageView circleImageView_profilePic;


    private TextView textView_religion, textView_caste, textView_gothram, textView_motherToungue;

    private TextView textView_name, textView_birthdate, textView_mobileNo, textView_emailId, textView_country,
            textView_state, textView_city, textView_village, textView_address, textView_postalCode;

    private TextView textView_fatherName, textView_fatherOccupation,textView_fatherIncome, textView_motherName,
            textView_motherOccupation, textView_motherIncome;

    private TextView textView_highestQualificationLevel, textView_highestQualification, textView_nameOfInstitute,
            textView_percentage, textView_passingYear;

    private TextView textView_currentService, textView_designation, textView_experience, textView_annualIncome;

    private TextView textView_height, textView_weight, textView_colour, textView_maritalStatus, textView_livesWithFamily;

    private RelativeLayout relativeLayout_changeProfilePic;

    private LinearLayout linearLayout_basicDetails, linearLayout_religiousDetails, linearLayout_personalDetails,
            linearLayout_qualificationDetails, linearLayout_professionalDetails, linearLayout_familyDetails;


    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;



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
        setContentView(R.layout.activity_edit_profile);


        init();

        onClickListener();



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
                enableRuntimePermission();
                changeProfilepicDialog();

            }
        });

        callFragment(linearLayout_basicDetails, "Basic");
        callFragment(linearLayout_religiousDetails, "Religious");
        callFragment(linearLayout_personalDetails, "Personal");
        callFragment(linearLayout_qualificationDetails, "Qualification");
        callFragment(linearLayout_professionalDetails, "Professional");
        callFragment(linearLayout_familyDetails, "Family");
        //callFragment(linearLayout_basicDetails, "Basic");



    }

    private void callFragment(LinearLayout linearLayout, final String fragmentName) {

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragmentName",fragmentName);
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


        imageView_back = findViewById(R.id.imageView_back);
        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);


        textView_name = findViewById(R.id.textView_name);
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


        relativeLayout_changeProfilePic = findViewById(R.id.relativeLayout_changeProfilePic);
        linearLayout_basicDetails = findViewById(R.id.linearLayout_basicDetails);
        linearLayout_religiousDetails = findViewById(R.id.linearLayout_religiousDetails);
        linearLayout_personalDetails = findViewById(R.id.linearLayout_personalDetails);
        linearLayout_qualificationDetails = findViewById(R.id.linearLayout_qualificationDetails);
        linearLayout_professionalDetails = findViewById(R.id.linearLayout_professionalDetails);
        linearLayout_familyDetails = findViewById(R.id.linearLayout_familyDetails);



        userModel = CustomSharedPreference.getInstance(EditProfileActivity.this).getUser();
        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(EditProfileActivity.this);
        customDialogChangeProfilePic = new CustomDialogChangeProfilePic(EditProfileActivity.this);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");


    }



    void insertProfilePic()
    {



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_PROFILEPIC,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {
                                //   getDetails();
                                getProfilePic();
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

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);



    }

    void getProfilePic()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PROFILEPIC + "UserId=" + userModel.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONArray jsonArray = new JSONArray(response);
                            if(jsonArray.length()==1)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {

                                    //ImageUrl - 0 or 1 :- default img
                                    if(!(URLs.MainURL+jsonObject.getString("ImageUrl")).equals("1")
                                            && !(URLs.MainURL+jsonObject.getString("ImageUrl")).equals("0"))
                                    {
                                        //to clear cache
                                        Picasso.get().invalidate(URLs.MainURL + jsonObject.getString("ImageUrl"));
                                        Picasso.get().load(URLs.MainURL + jsonObject.getString("ImageUrl"))
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .error(R.drawable.default_profile)
                                                .placeholder(R.drawable.default_profile)
                                                .into(circleImageView_profilePic);

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

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);

    }



    void getBasicDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_BASICDETAILS+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
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



                                    checkIfEmpty(textView_name, jsonObject.getString("FullName"));
                                    checkIfEmpty(textView_birthdate, jsonObject.getString("Birthdate"));
                                    checkIfEmpty(textView_mobileNo, jsonObject.getString("MobileNo"));
                                    checkIfEmpty(textView_emailId, jsonObject.getString("EmailId"));


                                    checkIfEmpty(textView_country, jsonObject.getString("PermanantCountryName"));
                                    checkIfEmpty(textView_state, jsonObject.getString("PermanantState"));
                                    checkIfEmpty(textView_city, jsonObject.getString("PermanantCity"));
                                    checkIfEmpty(textView_address, jsonObject.getString("PermanantAddress"));
                                    checkIfEmpty(textView_village, jsonObject.getString("PermanantVillage"));
                                    checkIfEmpty(textView_postalCode, jsonObject.getString("PermanantPostCode"));






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
                        Toast.makeText(EditProfileActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);
    }



    void getReligiousDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_RELIGIONDETAIL+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
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
                                    //religionDetailsId = jsonObject.getInt("ReligiousDetailsId");

                                    checkIfEmpty(textView_religion, jsonObject.getString("ReligionName"));
                                    checkIfEmpty(textView_caste, jsonObject.getString("CasteName"));
                                    checkIfEmpty(textView_gothram, jsonObject.getString("Gothram"));
                                    checkIfEmpty(textView_motherToungue, jsonObject.getString("MotherTongueName"));


                                    //textView_gothram.setText(jsonObject.getString("SubCasteName"));
                                    //checkBox_otherCaste.setChecked(jsonObject.getBoolean("OtherCommunity"));

                                }



                            }
                            else
                            {

                                //Toast.makeText(EditProfileActivity.this,"Invalid Details GET! ",Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);




    }



    void getPersonalDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PERSONALDETAILS+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
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
                                    //personalDetailsId = jsonObject.getInt("PersonalDetailsId");

                                    checkIfEmpty(textView_height, jsonObject.getString("Height"));
                                    checkIfEmpty(textView_weight, jsonObject.getString("Weight"));
                                    checkIfEmpty(textView_livesWithFamily, jsonObject.getString("LivesWithFamily"));
                                    checkIfEmpty(textView_colour, jsonObject.getString("SkinColourName"));
                                    checkIfEmpty(textView_maritalStatus, jsonObject.getString("MaritalStatusName"));



                                }



                            }
                            else
                            {
                                customDialogLoadingProgressBar.dismiss();
                                //Toast.makeText(EditProfileActivity.this,"Sorry for the inconvenience \nPlease try again!" ,Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);




    }


    void getQualificationDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_QUALIFICATIONDETAILS+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
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
                                    //qualificationDetailsId = jsonObject.getInt("QualificationDetailsId");
                                    checkIfEmpty(textView_highestQualificationLevel, jsonObject.getString("QualificationLevelName"));
                                    checkIfEmpty(textView_highestQualification, jsonObject.getString("Qualification"));
                                    checkIfEmpty(textView_nameOfInstitute, jsonObject.getString("Sch_Uni"));
                                    checkIfEmpty(textView_percentage, jsonObject.getString("Percentage"));
                                    checkIfEmpty(textView_passingYear, jsonObject.getString("PassingYearString"));

                                    /*textView_highestQualificationLevel.setText(jsonObject.getString(""));
                                    textView_highestQualification.setText(jsonObject.getString(""));
                                    textView_nameOfInstitute.setText(jsonObject.getString(""));
                                    textView_percentage.setText(jsonObject.getString("")+" %");
                                    textView_passingYear.setText(jsonObject.getString(""));*/


                                }



                            }
                            else
                            {

                                customDialogLoadingProgressBar.dismiss();
                                //Toast.makeText(EditProfileActivity.this,"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
                                customDialogLoadingProgressBar.dismiss();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            customDialogLoadingProgressBar.dismiss();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditProfileActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        customDialogLoadingProgressBar.dismiss();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);

    }


    void getProfessionalDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PROFESSIONALDETAILS+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
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
                                    //professionalDetailsId = jsonObject.getInt("ProfessionalDetailsId");

                                    String currentService = "";
                                    if(jsonObject.getString("ServiceTypeId").equals("1"))
                                    {
                                        currentService = getResources().getString(R.string.govt_job);

                                    }
                                    else
                                    {
                                        currentService = getResources().getString(R.string.private_job);
                                    }

                                    checkIfEmpty(textView_currentService, currentService);
                                    checkIfEmpty(textView_designation, jsonObject.getString("DesignationName"));
                                    checkIfEmpty(textView_experience, jsonObject.getString("ExperienceInYears"));
                                    checkIfEmpty(textView_annualIncome, jsonObject.getString("SalaryPackageName"));




                                    //editText_occupation.setText(jsonObject.getString("OccupationName"));






                                }



                            }
                            else
                            {

                                customDialogLoadingProgressBar.dismiss();
                                //Toast.makeText(EditProfileActivity.this,"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
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

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);
    }



    void getFamilyDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_FAMILYDETAILS + "UserId=" + userModel.getUserId() + "&Language=" + userModel.getLanguage(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {
                                    //familyDetailsId = jsonObject.getInt("FamilyDetailsId");


                                    //checkBox_fatherIsAlive.setChecked(jsonObject.getString("IsAliveFather").equals("1"));


                                    checkIfEmpty(textView_fatherName, jsonObject.getString("FullnameFather"));
                                    checkIfEmpty(textView_fatherOccupation, jsonObject.getString("OccupationNameFather"));
                                    checkIfEmpty(textView_motherName, jsonObject.getString("FullnameMother"));
                                    checkIfEmpty(textView_motherOccupation, jsonObject.getString("OccupationNameMother"));



                                    //checkBox_motherIsAlive.setChecked(jsonObject.getString("IsAliveMother").equals("1"));

                                    //editText_motherAnnualIncome.setText(jsonObject.getString("AnnualIncomeMother"));
                                    //   editText_familyIncome.setText(jsonObject.getString("SalaryPackageName"));
                                    //textView_familyIncome.setText(jsonObject.getString("SalaryPackageId"));




                                }

                            } else {

                                customDialogLoadingProgressBar.dismiss();
                                //Toast.makeText(EditProfileActivity.this, " Please enter your details! ", Toast.LENGTH_SHORT).show();
                            }


                        }catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        Toast.makeText(EditProfileActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(EditProfileActivity.this).addToRequestQueue(stringRequest);


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


    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {

            if (params[0].equals("getDetails"))
            {
                getProfilePic();
                getBasicDetails();
                getReligiousDetails();
                getPersonalDetails();
                getQualificationDetails();
                getProfessionalDetails();
                getFamilyDetails();

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