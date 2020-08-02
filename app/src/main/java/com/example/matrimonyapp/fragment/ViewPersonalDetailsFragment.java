package com.example.matrimonyapp.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.HomeActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPersonalDetailsFragment extends Fragment {


    View view;

    TextView textView_name, textView_gender, textView_birthdate, textView_age, textView_location,
            textView_motherTongue, textView_height, textView_weight, textView_colour,
            textView_maritalStatus, textView_familyStatus, textView_familyType, textView_familyValues,
            textView_disability, textView_disabilityType, textView_diet, textView_livesWithFamily,
            textView_religion, textView_caste, textView_subCaste, textView_gothram, textView_dosh ;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    UserModel userModel;

    public ViewPersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_personal_details, container, false);

        init();

        return  view;

    }
    private void init() {

        textView_name = view.findViewById(R.id.textView_name);
        textView_gender = view.findViewById(R.id.textView_gender);
        textView_birthdate = view.findViewById(R.id.textView_birthdate);
        textView_age = view.findViewById(R.id.textView_age);
        textView_location = view.findViewById(R.id.textView_location);
        textView_motherTongue = view.findViewById(R.id.textView_motherTongue);
        textView_height = view.findViewById(R.id.textView_height);
        textView_weight = view.findViewById(R.id.textView_weight);
        textView_colour = view.findViewById(R.id.textView_colour);
        textView_maritalStatus = view.findViewById(R.id.textView_maritalStatus);
        textView_familyStatus = view.findViewById(R.id.textView_familyStatus);
        textView_familyType = view.findViewById(R.id.textView_familyType);
        textView_familyValues = view.findViewById(R.id.textView_familyValues);
        textView_disability = view.findViewById(R.id.textView_disability);
        textView_disabilityType = view.findViewById(R.id.textView_disabilityType);
        textView_diet = view.findViewById(R.id.textView_diet);
        textView_livesWithFamily = view.findViewById(R.id.textView_livesWithFamily);
        textView_religion = view.findViewById(R.id.textView_religion);
        textView_caste = view.findViewById(R.id.textView_caste);
        textView_subCaste = view.findViewById(R.id.textView_subCaste);
        textView_gothram = view.findViewById(R.id.textView_gothram);
        textView_dosh = view.findViewById(R.id.textView_dosh);

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
                getDetails();
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

    void getDetails()
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


}
