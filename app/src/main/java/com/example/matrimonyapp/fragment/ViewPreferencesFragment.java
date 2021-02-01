package com.example.matrimonyapp.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.HomeActivity;
import com.example.matrimonyapp.activity.SetPreferencesActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.ViewMultipleDetailsAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteLanguageKnownDetails;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPreferencesFragment extends Fragment {


    View view;
    private Bundle bundle;
    private String userId, activityName;

    TextView textView_lookingFor, textView_age, textView_height, textView_colour,
            textView_maritalStatus, textView_familyType, textView_familyValues,textView_diet,
            textView_religion, textView_caste, textView_subCaste, textView_qualificationLevel,
            textView_qualification, textView_serviceType, textView_workingIn,  textView_currentService,
            textView_occupation, textView_expectedIndividualIncome, textView_expectedFamilyIncome,
            textView_workingCountry, textView_workingState, textView_workingCity,
            textView_residentialCountry, textView_residentialState, textView_residentialCity,
            textView_familyStatus
           ; //textView_dosh, textView_alternetMobileNo, textView_alternetEmailId,



    private LinearLayout linearLayout_preferences;



    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    UserModel userModel;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_preferences, container, false);

        init();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");

        return view;

    }

    private void init() {

        bundle = this.getArguments();
        userId = bundle.getString("userId");
        activityName = bundle.getString("activityName");







        linearLayout_preferences = view.findViewById(R.id.linearLayout_preferences);
        textView_lookingFor = view.findViewById(R.id.textView_lookingFor);
        textView_age = view.findViewById(R.id.textView_age);
        textView_height = view.findViewById(R.id.textView_height);
        textView_colour = view.findViewById(R.id.textView_colour);
        textView_maritalStatus = view.findViewById(R.id.textView_maritalStatus);
        textView_diet = view.findViewById(R.id.textView_diet);


        textView_familyType = view.findViewById(R.id.textView_familyType);
        textView_familyValues = view.findViewById(R.id.textView_familyValues);

        textView_religion = view.findViewById(R.id.textView_religion);
        textView_caste = view.findViewById(R.id.textView_caste);
        textView_subCaste = view.findViewById(R.id.textView_subCaste);


        textView_qualificationLevel = view.findViewById(R.id.textView_qualificationLevel);
        textView_qualification = view.findViewById(R.id.textView_qualification);



        textView_serviceType = view.findViewById(R.id.textView_serviceType);
        textView_workingIn = view.findViewById(R.id.textView_workingIn);
        textView_currentService = view.findViewById(R.id.textView_currentService);
        textView_occupation = view.findViewById(R.id.textView_occupation);
        textView_expectedIndividualIncome = view.findViewById(R.id.textView_expectedIndividualIncome);
        textView_expectedFamilyIncome = view.findViewById(R.id.textView_expectedFamilyIncome);



        textView_workingCountry = view.findViewById(R.id.textView_workingCountry);
        textView_workingState = view.findViewById(R.id.textView_workingState);
        textView_workingCity = view.findViewById(R.id.textView_workingCity);


        textView_residentialCountry = view.findViewById(R.id.textView_residentialCountry);
        textView_residentialState = view.findViewById(R.id.textView_residentialState);
        textView_residentialCity = view.findViewById(R.id.textView_residentialCity);




        //textView_dosh = view.findViewById(R.id.textView_dosh);

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();


        linearLayout_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityName.equals("EditProfile"))
                {
                    Intent intent = new Intent(getContext(), SetPreferencesActivity.class);
                    getContext().startActivity(intent);
                }
            }
        });



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

    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {

            if (params[0].equals("InsertDetails")) {
                //insertDetails();
            } else if (params[0].equals("getDetails")) {
                //getDetails();

            }


            return null;
        }


        @Override
        protected void onPreExecute() {
            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
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


    void getDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_BASICDETAILS + "UserId=" +userId+ "&Language=" + userModel.getLanguage(),
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


                                    checkIfEmpty(textView_age, jsonObject.getString("Age"));





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
                        Toast.makeText(getContext(), "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }








}