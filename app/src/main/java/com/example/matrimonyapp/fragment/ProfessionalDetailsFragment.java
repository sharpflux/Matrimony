package com.example.matrimonyapp.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.DataFetcher;
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
public class ProfessionalDetailsFragment extends Fragment {

    View view;
    private TextView textView_saveAndContinue, textView_occupationId, textView_designationId;

    private EditText editText_companyName, editText_occupation, editText_designation, editText_experience,
            editText_income, editText_companyAddress;

    private ImageView imageView_back;

    String companyName, occupation, designation, experience, income, companyAddress;

    Bundle bundle;
    UserModel userModel;
    DataFetcher dataFetcher;

    int professionalDetailsId=0;
    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    public ProfessionalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_professional_details, container, false);

        bundle = getArguments();



        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        dataFetcher = new DataFetcher("Professional",getContext());

        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv =((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Professional Details");


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStack();
                }
            }
        });

        editText_companyName = view.findViewById(R.id.editText_companyName);
        editText_occupation = view.findViewById(R.id.editText_occupation);
        textView_occupationId = view.findViewById(R.id.textView_occupationId);
        textView_designationId = view.findViewById(R.id.textView_designationId);
        editText_designation = view.findViewById(R.id.editText_designation);
        editText_experience = view.findViewById(R.id.editText_experience);
        editText_income = view.findViewById(R.id.editText_income);
        editText_companyAddress = view.findViewById(R.id.editText_companyAddress);

        editText_occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("Occupation");
            }
        });

        editText_designation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("Designation");
            }
        });

        textView_saveAndContinue = ((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);
        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*companyName = editText_companyName.getText().toString();
                occupation = editText_occupation.getText().toString();
                designation = editText_designation.getText().toString();
                experience = editText_experience.getText().toString();
                income = editText_income.getText().toString();
                companyAddress = editText_companyAddress.getText().toString();

                bundle.putString("companyName",companyName);
                bundle.putString("occupation",occupation);
                bundle.putString("designation",designation);
                bundle.putString("experience",experience);
                bundle.putString("income",income);
                bundle.putString("companyAddress",companyAddress);*/


                PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();
               // personalDetailsFragment.setArguments(bundle);


                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, personalDetailsFragment);
                fragmentTransaction.commit() ;
            }
        });


        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());

        AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");


        return view;
    }


    void getDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PROFESSIONALDETAILS+"UserId="+1+"&Language="+userModel.getLanguage(),
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
                                    professionalDetailsId = jsonObject.getInt("ProfessionalDetailsId");


                                    textView_occupationId.setText(jsonObject.getString("OccupationId"));
                                    textView_designationId.setText(jsonObject.getString("DesignationId"));

                                    editText_occupation.setText(jsonObject.getString("OccupationName"));
                                    editText_designation.setText(jsonObject.getString("DesignationName"));
                                    editText_companyName.setText(jsonObject.getString("CompanyName"));
                                    //editText_percentage.setText(jsonObject.getString("OtherCommunity"));
                                    editText_companyAddress.setText(jsonObject.getString("CompanyAddress"));
                                    editText_experience.setText(jsonObject.getString("Experience"));
                                    editText_income.setText(jsonObject.getString("MonthlyIncome"));



                                }



                            }
                            else
                            {
                                professionalDetailsId = 0;
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

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                //customDialogLoadingProgressBar.dismiss();

                if(params[0].toString().equals("getDetails"))
                {
                    getDetails();

                }
                if(params[0].toString().equals("Occupation"))
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId",
                            "OccupationName", editText_occupation,
                            textView_occupationId, getContext(), customDialogLoadingProgressBar);

                }
                else if(params[0].toString().equals("Designation"))
                {
                    dataFetcher.loadList(URLs.URL_GET_DESIGNATION+"Language="+userModel.getLanguage(),
                            "DesignationId",
                            "DesignationName", editText_designation,
                            textView_designationId, getContext(), customDialogLoadingProgressBar);

                }




            } catch (Exception e) {
                e.printStackTrace();
                //resp = e.getMessage();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {


        }


        @Override
        protected void onPreExecute() {


            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }


}
