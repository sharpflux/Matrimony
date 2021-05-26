package com.example.matrimonyapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.EditProfileActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.customViews.CustomDialogChangeProfilePic;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AboutEditProfileFragment extends Fragment {

    private View view;
    private Context context;


    private TextView textView_religion, textView_caste, textView_gothram, textView_motherToungue,tvAddMoreImages;

    private TextView textView_name, textView_birthdate, textView_mobileNo, textView_emailId, textView_country,
            textView_state, textView_city, textView_village, textView_address, textView_postalCode;

    private TextView textView_fatherName, textView_fatherOccupation,textView_fatherIncome, textView_motherName,
            textView_motherOccupation, textView_motherIncome;

    private TextView textView_highestQualificationLevel, textView_highestQualification, textView_nameOfInstitute,
            textView_percentage, textView_passingYear;

    private TextView textView_currentService, textView_designation, textView_experience, textView_annualIncome;

    private TextView textView_height, textView_weight, textView_colour, textView_maritalStatus, textView_livesWithFamily;

    private LinearLayout linearLayout_basicDetails, linearLayout_religiousDetails, linearLayout_personalDetails,
            linearLayout_qualificationDetails, linearLayout_professionalDetails, linearLayout_familyDetails;
    private UserModel userModel;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_edit_profile, container, false);

        init();


        onClickListener();

        tvAddMoreImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((EditProfileActivity)getActivity()).openFragment("Test");
            }
        });

        //AsyncTaskRunner runner = new AsyncTaskRunner();
        //runner.execute("getDetails");

        return view;
    }

    private void onClickListener() {

        callFragment(linearLayout_basicDetails, "Basic");
        callFragment(linearLayout_religiousDetails, "Religious");
        callFragment(linearLayout_personalDetails, "Personal");
        callFragment(linearLayout_qualificationDetails, "Qualification");
        callFragment(linearLayout_professionalDetails, "Professional");
        callFragment(linearLayout_familyDetails, "Family");

    }

    private void callFragment(LinearLayout linearLayout, final String fragmentName) {

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("fragmentName",fragmentName);
                context.startActivity(intent);
            }
        });

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

                                //Toast.makeText(contextcontext,"Please enter your details! ",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
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

                                //Toast.makeText(context,"Invalid Details GET! ",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);




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
                                //Toast.makeText(context,"Sorry for the inconvenience \nPlease try again!" ,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);




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
                                //Toast.makeText(context,"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

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
                                //Toast.makeText(context,"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
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
                                //Toast.makeText(context, " Please enter your details! ", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }




    private void checkIfEmpty(TextView textView, String value) {

        value = value.trim();

        if(value.isEmpty() || value.equals("0") || value.equals(""))
        {
            textView.setText(getResources().getString(R.string.not_filled));
            textView.setTextColor(ContextCompat.getColor(context, R.color.red_btn_bg_color));
        }
        else
        {
            textView.setText(value);
            textView.setTextColor(ContextCompat.getColor(context, R.color.quantum_grey700));

        }

    }



    private void init() {



        textView_name = view.findViewById(R.id.textView_name);
        textView_birthdate = view.findViewById(R.id.textView_birthdate);
        textView_mobileNo = view.findViewById(R.id.textView_mobileNo);
        textView_emailId = view.findViewById(R.id.textView_emailId);
        textView_country = view.findViewById(R.id.textView_country);
        textView_state = view.findViewById(R.id.textView_state);
        textView_city = view.findViewById(R.id.textView_city);
        textView_village = view.findViewById(R.id.textView_village);
        textView_address = view.findViewById(R.id.textView_address);
        textView_postalCode = view.findViewById(R.id.textView_postalCode);


        textView_religion = view.findViewById(R.id.textView_religion);
        textView_caste = view.findViewById(R.id.textView_caste);
        textView_gothram = view.findViewById(R.id.textView_gothram);
        textView_motherToungue = view.findViewById(R.id.textView_motherToungue);


        textView_height = view.findViewById(R.id.textView_height);
        textView_weight = view.findViewById(R.id.textView_weight);
        textView_colour = view.findViewById(R.id.textView_colour);
        textView_maritalStatus = view.findViewById(R.id.textView_maritalStatus);
        textView_livesWithFamily = view.findViewById(R.id.textView_livesWithFamily);


        textView_highestQualificationLevel = view.findViewById(R.id.textView_highestQualificationLevel);
        textView_highestQualification = view.findViewById(R.id.textView_highestQualification);
        textView_nameOfInstitute = view.findViewById(R.id.textView_nameOfInstitute);
        textView_percentage = view.findViewById(R.id.textView_percentage);
        textView_passingYear = view.findViewById(R.id.textView_passingYear);


        textView_currentService = view.findViewById(R.id.textView_currentService);
        textView_designation = view.findViewById(R.id.textView_designation);
        textView_experience = view.findViewById(R.id.textView_experience);
        textView_annualIncome = view.findViewById(R.id.textView_annualIncome);


        textView_fatherName = view.findViewById(R.id.textView_fatherName);
        textView_fatherOccupation = view.findViewById(R.id.textView_fatherOccupation);
        textView_fatherIncome = view.findViewById(R.id.textView_fatherIncome);
        textView_motherName = view.findViewById(R.id.textView_motherName);
        textView_motherOccupation = view.findViewById(R.id.textView_motherOccupation);
        textView_motherIncome = view.findViewById(R.id.textView_motherIncome);



        linearLayout_basicDetails = view.findViewById(R.id.linearLayout_basicDetails);
        linearLayout_religiousDetails = view.findViewById(R.id.linearLayout_religiousDetails);
        linearLayout_personalDetails = view.findViewById(R.id.linearLayout_personalDetails);
        linearLayout_qualificationDetails = view.findViewById(R.id.linearLayout_qualificationDetails);
        linearLayout_professionalDetails = view.findViewById(R.id.linearLayout_professionalDetails);
        linearLayout_familyDetails = view.findViewById(R.id.linearLayout_familyDetails);

        tvAddMoreImages=view.findViewById(R.id.tvAddMoreImages);


        context = getContext();

        userModel = CustomSharedPreference.getInstance(context).getUser();
        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");


    }



    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {


        @Override
        protected String doInBackground(String... params) {

            if (params[0].equals("getDetails"))
            {
                getBasicDetails();
                getReligiousDetails();
                getPersonalDetails();
                getQualificationDetails();
                getProfessionalDetails();
                getFamilyDetails();

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
