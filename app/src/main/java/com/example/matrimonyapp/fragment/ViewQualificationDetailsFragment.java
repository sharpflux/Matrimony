package com.example.matrimonyapp.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.matrimonyapp.adapter.AddPersonAdapter;
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
public class ViewQualificationDetailsFragment extends Fragment {

    View view;
    private Bundle bundle;
    private String userId;

    TextView textView_currentService, textView_nameOfDepartment, textView_occupation,
            textView_workingCountry, textView_workingState, textView_workingCity,
            textView_companyName, textView_designation, textView_experience, textView_annualIncome,
            textView_workingAddress, textView_qualificationLevel, textView_qualification, textView_nameOfInstitute,
            textView_percentage, textView_passingYear, textView_hobby, textView_socialContribution;



    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    UserModel userModel;


    public ViewQualificationDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_qualification_details, container, false);

        init();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");

        return view;
    }

    private  void init()
    {
        bundle = this.getArguments();
        userId = bundle.getString("userId");


        textView_companyName = view.findViewById(R.id.editText_companyName);
        textView_designation = view.findViewById(R.id.textView_designation);
        textView_experience = view.findViewById(R.id.textView_experience);
        textView_annualIncome = view.findViewById(R.id.textView_annualIncome);
        textView_workingAddress = view.findViewById(R.id.textView_workingAddress);
        textView_qualificationLevel = view.findViewById(R.id.textView_qualificationLevel);
        textView_qualification = view.findViewById(R.id.textView_qualification);
        textView_nameOfInstitute = view.findViewById(R.id.textView_nameOfInstitute);
        textView_percentage = view.findViewById(R.id.textView_percentage);
        textView_passingYear = view.findViewById(R.id.textView_passingYear);
        textView_hobby = view.findViewById(R.id.textView_hobby);
        textView_socialContribution = view.findViewById(R.id.textView_socialContribution);

        textView_currentService = view.findViewById(R.id.textView_currentService);
        textView_nameOfDepartment = view.findViewById(R.id.textView_nameOfDepartment);
        textView_occupation = view.findViewById(R.id.textView_occupation);
        textView_workingCountry = view.findViewById(R.id.textView_workingCountry);
        textView_workingState = view.findViewById(R.id.textView_workingState);
        textView_workingCity = view.findViewById(R.id.textView_workingCity);


        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();




    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {

            if(params[0].equals("InsertDetails"))
            {
                //insertDetails();
            }
            else if(params[0].equals("getDetails"))
            {
                getQualificationDetails();
                getProfessionalDetails();
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



    void getQualificationDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_QUALIFICATIONDETAILS+"UserId="+ 18 +"&Language="+userModel.getLanguage(),
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


                                    textView_qualificationLevel.setText(jsonObject.getString("QualificationLevelName"));
                                    textView_qualification.setText(jsonObject.getString("Qualification"));
                                    textView_nameOfInstitute.setText(jsonObject.getString("Sch_Uni"));
                                    textView_passingYear.setText(jsonObject.getString("PassingYearString"));
                                    textView_percentage.setText(jsonObject.getString("Percentage")+" %");

                                    textView_hobby.setText(jsonObject.getString("Hobby"));
                                    textView_socialContribution.setText(jsonObject.getString("Social_Contribution"));


                                    /*qualificationDetailsId = jsonObject.getInt("QualificationDetailsId");


                                    textView_highestQualificationLevelId.setText(jsonObject.getString("QualificationLevelId"));
                                    textView_qualificationId.setText(jsonObject.getString("QualificationId"));

                                    editText_highestQualificationLevel.setText(jsonObject.getString("QualificationLevelName"));
                                    editText_qualification.setText(jsonObject.getString("Qualification"));
                                    editText_institue.setText(jsonObject.getString("Sch_Uni"));
                                    editText_percentage.setText(jsonObject.getString("Percentage")+" %");
                                    editText_passingYear.setText(jsonObject.getString("PassingYearString"));
                                    editText_hobby.setText(jsonObject.getString("Hobby"));
                                    editText_socialContributions.setText(jsonObject.getString("Social_Contribution"));
*/
                                    //editText_.setText(jsonObject.getString(""));

                                }



                            }
                            else
                            {
                                //qualificationDetailsId = 0;
                                Toast.makeText(getContext(),"Invalid Details GET! ",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

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

    void getProfessionalDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PROFESSIONALDETAILS+"UserId="+18+"&Language="+userModel.getLanguage(),
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


                                    //textView_currentService.setText(jsonObject.getString(""));
                                    //textView_nameOfDepartment.setText(jsonObject.getString(""));
                                    //textView_companyName.setText(jsonObject.getString(""));
                                    textView_occupation.setText(jsonObject.getString("OccupationName"));
                                    textView_designation.setText(jsonObject.getString("DesignationName"));
                                    textView_experience.setText(jsonObject.getString("Experience"));
                                    //textView_annualIncome.setText(jsonObject.getString(""));
                                    textView_workingAddress.setText(jsonObject.getString("CompanyAddress"));
                                   /* textView_workingCountry.setText(jsonObject.getString(""));
                                    textView_workingState.setText(jsonObject.getString(""));
                                    textView_workingCity.setText(jsonObject.getString(""));*/

/*

                                    textView_occupationId.setText(jsonObject.getString("OccupationId"));
                                    textView_designationId.setText(jsonObject.getString("DesignationId"));

                                    editText_occupation.setText(jsonObject.getString("OccupationName"));
                                    editText_designation.setText(jsonObject.getString("DesignationName"));
                                    editText_companyName.setText(jsonObject.getString("CompanyName"));
                                    //editText_percentage.setText(jsonObject.getString("OtherCommunity"));
                                    editText_companyAddress.setText(jsonObject.getString("CompanyAddress"));
                                    editText_experience.setText(jsonObject.getString("Experience"));
                                    editText_income.setText(jsonObject.getString("MonthlyIncome"));
*/



                                }



                            }
                            else
                            {
                                //professionalDetailsId = 0;
                                customDialogLoadingProgressBar.dismiss();
                                Toast.makeText(getContext(),"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

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
