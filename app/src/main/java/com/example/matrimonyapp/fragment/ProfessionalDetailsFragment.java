package com.example.matrimonyapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.HomeActivity;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.customViews.CustomDialogAddProperty;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.FieldValidation;
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
    private TextView textView_saveAndContinue, textView_occupationId, textView_designationId,
            textView_countryId, textView_stateId, textView_cityId, textView_income;

    private EditText editText_companyName, editText_occupation, editText_designation, editText_experience,
            editText_income, editText_companyAddress,  editText_departmentName, editText_country,
            editText_state, editText_city;
//editText_companyCountry, editText_companyState,editText_companyDistrict, editText_companyTaluka,
    public Context context;
    private ImageView imageView_back;
    private boolean isLoggedIn=false;
    private RadioGroup radioGroup_currentService;
    private CardView cardView_governmentService, cardView_privateService;
    private Handler mHandler;
    Bundle bundle;
    UserModel userModel;
    DataFetcher dataFetcher;

    int professionalDetailsId=0;
    private int currentCountryId =0, currentStateId=0, newCountryId=0, newStateId=0;
    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    private String currentService;

    public ProfessionalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        view = inflater.inflate(R.layout.fragment_professional_details, container, false);

        bundle = getArguments();
        context = getContext();
        mHandler = new Handler();
        isLoggedIn = CustomSharedPreference.getInstance(getContext()).isLoggedIn();
//        if (!) {
//            startActivity(new Intent(getContext(), LoginActivity.class));
//        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        dataFetcher = new DataFetcher("Professional",getContext());

        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv =((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        //tv.setText("Professional Details");
        tv.setText(context.getResources().getString(R.string.professional_details));


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStack();
                }
                else
                {
                    getActivity().finish();
                }
            }
        });

        radioGroup_currentService = view.findViewById(R.id.radioGroup_currentService);
        cardView_governmentService = view.findViewById(R.id.cardView_governmentService);
        cardView_privateService = view.findViewById(R.id.cardView_privateService);
        editText_departmentName = view.findViewById(R.id.editText_departmentName);
        editText_companyName = view.findViewById(R.id.editText_companyName);
        editText_occupation = view.findViewById(R.id.editText_occupation);
        textView_occupationId = view.findViewById(R.id.textView_occupationId);
        textView_designationId = view.findViewById(R.id.textView_designationId);
        editText_designation = view.findViewById(R.id.editText_designation);
        editText_experience = view.findViewById(R.id.editText_experience);
        editText_income = view.findViewById(R.id.editText_income);
        textView_income = view.findViewById(R.id.textView_income);

        editText_companyAddress = view.findViewById(R.id.editText_companyAddress);
        editText_country = view.findViewById(R.id.editText_country);
        textView_countryId = view.findViewById(R.id.textView_countryId);
        editText_state = view.findViewById(R.id.editText_state);
        textView_stateId = view.findViewById(R.id.textView_stateId);
        editText_city = view.findViewById(R.id.editText_city);
        textView_cityId = view.findViewById(R.id.textView_cityId);
/*
        editText_companyCountry = view.findViewById(R.id.editText_companyCountry);
        editText_companyState = view.findViewById(R.id.editText_companyState);
        editText_companyDistrict = view.findViewById(R.id.editText_companyDistrict);
        editText_companyTaluka = view.findViewById(R.id.editText_companyTaluka);
        textView_companyCountryId = view.findViewById(R.id.textView_companyCountryId);
        textView_companyStateId = view.findViewById(R.id.textView_companyStateId);
        textView_companyDistrictId = view.findViewById(R.id.textView_companyDistrictId);
        textView_companyTalukaId = view.findViewById(R.id.textView_companyTalukaId);
*/




        radioGroup_currentService.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

                if(checkedId==R.id.radioButton_govermentService)
                {
                    cardView_governmentService.setVisibility(View.VISIBLE);
                    cardView_privateService.setVisibility(View.GONE);
                    editText_companyName.setText("");
                    editText_occupation.setText("");
                    textView_occupationId.setText("0");
                }
                else
                {
                    cardView_governmentService.setVisibility(View.GONE);
                    cardView_privateService.setVisibility(View.VISIBLE);
                    editText_departmentName.setText("");
                }
            }
        });



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

                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");

            }
        });
        editText_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("Income");

            }
        });




        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
        if (CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            AsyncTaskLoad getTask = new AsyncTaskLoad();
            getTask.execute("getDetails");
        }



        showPopUp(editText_country, "Country");
        showPopUp(editText_state, "State");
        showPopUp(editText_city, "City");

/*        showPopupSDT(editText_companyState, "State", null);
        showPopupSDT(editText_companyDistrict, "District", textView_companyStateId);
        showPopupSDT(editText_companyTaluka, "Taluka", textView_companyDistrictId);
        FieldValidation.textChangedListenerForSDT(editText_companyState, editText_companyDistrict, editText_companyTaluka,
                textView_companyStateId, textView_companyDistrictId, textView_companyTalukaId);*/

        return view;
    }


    private void showPopUp(final EditText editText, final String urlFor)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            AsyncTaskLoad runner = new AsyncTaskLoad();

                String id="";

                if(urlFor.equals("Country"))
                {

                }
                else if(urlFor.equals("State"))
                {
                    id =  textView_countryId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select Country first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                else if(urlFor.equals("City"))
                {
                    id = textView_stateId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select State first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                runner.execute(urlFor, id);



            }
        });


    }

   /* public void showPopupSDT(EditText editText, final String urlFor, final TextView textView_id)
    {

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = FieldValidation.onClickListenerForSDT(urlFor, textView_id, getContext());

                AsyncTaskLoad runner =  new AsyncTaskLoad();
                runner.execute(urlFor, id);

            }
        });



    }*/


    private void insertDetails()
    {

      final  String CompanyOrDepartmentName;
        RadioButton radioButton_currentService = (RadioButton)view.findViewById(radioGroup_currentService.getCheckedRadioButtonId());
        if(radioButton_currentService==view.findViewById(R.id.radioButton_govermentService))
        {
            currentService = "1";
            CompanyOrDepartmentName=editText_departmentName.getText().toString();
        }
        else
        {
            currentService = "2";
            CompanyOrDepartmentName=editText_companyName.getText().toString();
        }
/*        if(radioButton_currentService!=null)
        {
            currentService = radioButton_currentService.getText().toString();
        }*/

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_PROFESSIONALDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);

                            if(jsonObject.getString("message").equals("Success") &&
                                    !jsonObject.getBoolean("error"))
                            {
                                getDetails();

                                Toast.makeText(getContext(),"Professional details saved successfully!", Toast.LENGTH_SHORT).show();

                                getActivity().finish();

                                if(isLoggedIn)
                                {
                                    getContext().startActivity((new Intent(getContext(), HomeActivity.class)));
                                }

                                /*UploadDocumentsFragment uploadDocumentsFragment = new UploadDocumentsFragment();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, uploadDocumentsFragment);
                                fragmentTransaction.commit() ;*/

                            }
                            else
                            {
                                Toast.makeText(getContext(),"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
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
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("ProfessionalDetailsId",String.valueOf(professionalDetailsId));
                params.put("UserId",userModel.getUserId());
                //change -> add in API
                params.put("ServiceTypeId",currentService);
                params.put("CompanyName",CompanyOrDepartmentName.toString());
                params.put("DesignationId",textView_designationId.getText().toString());
                //params.put("DepartmentName",editText_departmentName.getText().toString());
                params.put("ExperienceInYears",editText_experience.getText().toString());
                params.put("AnnualIncome",textView_income.getText().toString());
                params.put("WorkAddress",editText_companyAddress.getText().toString());
//              params.put("OccupationId",textView_occupationId.getText().toString());
                params.put("CountryId",textView_countryId.getText().toString());
                params.put("StateId",textView_stateId.getText().toString());
                params.put("CityId",textView_cityId.getText().toString());
                params.put("LanguageType",userModel.getLanguage());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }
    void getDetails()
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
                                    professionalDetailsId = jsonObject.getInt("ProfessionalDetailsId");

                                    if(jsonObject.getString("ServiceTypeId").equals("1"))
                                    {
                                        editText_departmentName.setText(jsonObject.getString("CompanyName"));
                                        ((RadioButton)radioGroup_currentService.getChildAt(0)).setChecked(true);
                                    }
                                    else
                                    {
                                        ((RadioButton)radioGroup_currentService.getChildAt(1)).setChecked(true);
                                        editText_companyName.setText(jsonObject.getString("CompanyName"));
                                    }


                                    //textView_occupationId.setText(jsonObject.getString("OccupationId"));
                                    textView_designationId.setText(jsonObject.getString("DesignationId"));

                                    editText_occupation.setText(jsonObject.getString("OccupationName"));
                                    editText_designation.setText(jsonObject.getString("DesignationName"));

                                    textView_designationId.setText(jsonObject.getString("DesignationId"));

                                    editText_companyName.setText(jsonObject.getString("CompanyName"));
                                    //editText_percentage.setText(jsonObject.getString("OtherCommunity"));
                                    editText_companyAddress.setText(jsonObject.getString("CompanyAddress"));
                                    editText_experience.setText(jsonObject.getString("ExperienceInYears"));
                                    editText_income.setText(jsonObject.getString("SalaryPackageName"));
                                    textView_income.setText(jsonObject.getString("SalaryPackageId"));

                                    editText_country.setText(jsonObject.getString("CompanyName"));
                                    textView_countryId.setText(jsonObject.getString("CountryId"));
                                    editText_state.setText(jsonObject.getString("StateName"));
                                    textView_stateId.setText(jsonObject.getString("StateId"));
                                    editText_city.setText(jsonObject.getString("CityName"));
                                    textView_cityId.setText(jsonObject.getString("CityId"));


                                }



                            }
                            else
                            {
                                professionalDetailsId = 0;
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
                else if(params[0].toString().equals("insertDetails"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            insertDetails();
                        }
                    });
/*
                    FamilyDetailsFragment familyDetailsFragment = new FamilyDetailsFragment();

                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, familyDetailsFragment);
                    fragmentTransaction.commit() ;*/
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
                else if(params[0].equals("Income"))
                {
                    dataFetcher.loadList(URLs.URL_GET_SALARY+"Language="+userModel.getLanguage(),
                            "SalaryPackageId", "SalaryPackageName", editText_income,
                            textView_income, getContext(), customDialogLoadingProgressBar);


                }

                if(params[0].equals("Country"))
                {

                    dataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                            "Name", editText_country, textView_countryId,getContext(),
                            customDialogLoadingProgressBar);



                }
                else if(params[0].equals("State"))
                {
                    String id = textView_countryId.getText().toString();

                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage()
                                    + "&CountryID="+id,"StatesID", "StatesName",
                            editText_state, textView_stateId,getContext(),
                            customDialogLoadingProgressBar);



                }
                else if(params[0].equals("City"))
                {
                    String id = textView_stateId.getText().toString();

                    dataFetcher.loadList(URLs.URL_GET_CITY+"Language="+userModel.getLanguage()
                                    + "&StateID="+id,"ID",
                            "Name", editText_city, textView_cityId,getContext(),
                            customDialogLoadingProgressBar);


                }

/*                else if(params[0].equals("State"))
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),"StatesID",
                            "StatesName", editText_companyState, textView_companyStateId,getContext(), customDialogLoadingProgressBar);


                }
                else if(params[0].equals("District"))
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_companyDistrict, textView_companyDistrictId,
                            getContext(), customDialogLoadingProgressBar);

                }
                else if(params[0].equals("Taluka"))
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_companyTaluka, textView_companyTalukaId,
                            getContext(), customDialogLoadingProgressBar);
                }*/
                else if(params[0].equals("Salary"))
                {
                    dataFetcher.loadList(URLs.URL_GET_SALARY+"Language="+userModel.getLanguage(),
                            "SalaryPackageId", "SalaryPackageName", editText_income,
                            textView_income, getContext(), customDialogLoadingProgressBar);


                }

                return params[0];

            } catch (Exception e) {
                e.printStackTrace();
                //resp = e.getMessage();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {

            if(result.equals("Country"))
            {
                newCountryId = Integer.parseInt(textView_countryId.getText().toString());
                if (currentCountryId != newCountryId) {
                    currentCountryId = newCountryId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_stateId.setText("0");
                            editText_state.setText("");
                            textView_cityId.setText("0");
                            editText_city.setText("");
                        }
                    });

                }
            }
            else if(result.equals("State"))
            {
                newStateId = Integer.parseInt(textView_stateId.getText().toString());
                if (currentStateId != newStateId) {
                    currentStateId = newStateId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_cityId.setText("0");
                            editText_city.setText("");
                        }
                    });

                }
            }
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
