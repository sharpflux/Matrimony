package com.example.matrimonyapp.fragment;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.CandidateQualificationAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.customViews.CustomDialogAddEducation;
import com.example.matrimonyapp.customViews.CustomDialogAddPercentage;
import com.example.matrimonyapp.customViews.CustomDialogAddSibling;
import com.example.matrimonyapp.customViews.CustomDialogAddYear;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.CandidateEducationModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteEducationDetails;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class QualificationDetailsFragment extends Fragment {

    View view;
    private TextView textView_saveAndContinue, textView_qualificationId, textView_highestQualificationLevelId,tvHeadingEducationDetails;
    private EditText editText_highestQualificationLevel, editText_qualification, editText_institue,
            editText_percentage, editText_passingYear, editText_hobby, editText_socialContributions ;
    /*editText_sscNameOfInstitute, editText_sscPer,editText_hscNameOfInstitute,
            editText_hscPer,
            editText_gradCourse, editText_gradNameOfInstitute, editText_gradPer,
            editText_pgCourse, editText_pgNameOfInstitute, editText_pgPer,*/

    private boolean isLoggedIn=false;
    private ImageView imageView_back,imageView_addEducation;

/*    String sscNameOfInstitute, sscPer,hscNameOfInstitute, hscPer,
            gradCourse, gradNameOfInstitute, gradPer,
            pgCourse, pgNameOfInstitute, pgPer, hobby, percentage,
            socialContributions ;

    int mYear, mMonth, mDay;
    DatePickerDialog datePickerDialog;

            */

    public Context context;

    DataFetcher dataFetcher;
    Bundle bundle;
    UserModel userModel;

    int qualificationDetailsId=0;

    CustomDialogAddPercentage customDialogAddPercentage;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    private CustomDialogAddEducation customDialogAddEducation;
    private RecyclerView recyclerView_addEducation;
    private AddPersonAdapter addPersonAdapter_education;
    private ArrayList<AddPersonModel> addPersonModelArrayList_education;

    private ArrayList<CandidateEducationModel> candidateQualificationArrayList_education;
    private CandidateQualificationAdapter Adapter_candidateQualification;

    private SQLiteEducationDetails sqLiteEducationDetails;
    private StringBuilder stringBuilder_education;

    public QualificationDetailsFragment() {
        // Required empty public constructor
    }
    private Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qualification_details, container, false);

        context = getContext();

        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv =((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText(context.getResources().getString(R.string.qualification_details));

        mHandler = new Handler();
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

//        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
//            startActivity(new Intent(getContext(), LoginActivity.class));
//        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();
        stringBuilder_education = new StringBuilder();

        editText_highestQualificationLevel=view.findViewById(R.id.editText_highestQualificationLevel);
        editText_qualification=view.findViewById(R.id.editText_qualification);
        editText_institue=view.findViewById(R.id.editText_institue);
        editText_percentage=view.findViewById(R.id.editText_percentage);
        editText_passingYear=view.findViewById(R.id.editText_passingYear);
        editText_hobby=view.findViewById(R.id.editText_hobby);
        editText_socialContributions=view.findViewById(R.id.editText_socialContributions);
        textView_highestQualificationLevelId=view.findViewById(R.id.textView_highestQualificationLevelId);
        textView_qualificationId=view.findViewById(R.id.textView_qualificationId);
        recyclerView_addEducation = view.findViewById(R.id.recyclerView_addEducation);
        imageView_addEducation = view.findViewById(R.id.imageView_addEducation);
        tvHeadingEducationDetails=view.findViewById(R.id.tvHeadingEducationDetails);

        bundle = getArguments();
        isLoggedIn = CustomSharedPreference.getInstance(getContext()).isLoggedIn();



        dataFetcher = new DataFetcher("Qualification",getContext());


        editText_percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogAddPercentage = new CustomDialogAddPercentage(getContext(),editText_percentage);
                customDialogAddPercentage.show();
            }
        });



        editText_passingYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogAddYear customDialogAddYear = new CustomDialogAddYear(getContext(),editText_passingYear);
                customDialogAddYear.show();
            }
        });


        editText_highestQualificationLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("QualificationLevel");
            }
        });

        editText_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("QualificationName", textView_highestQualificationLevelId.getText().toString());
            }
        });


        addPersonModelArrayList_education = new ArrayList<>();

        candidateQualificationArrayList_education=new ArrayList<>();

        sqLiteEducationDetails = new SQLiteEducationDetails(getContext());


        addPersonAdapter_education = new AddPersonAdapter(getContext(), addPersonModelArrayList_education, "Sibling");
        recyclerView_addEducation.setAdapter(addPersonAdapter_education);
        recyclerView_addEducation.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_education = new LinearLayoutManager(getContext());
        recyclerView_addEducation.setLayoutManager(linearLayoutManager_education);


        ViewGroup.LayoutParams params = recyclerView_addEducation.getLayoutParams();
        int complaint_height=200;
        if (addPersonModelArrayList_education.size() > 2) {
            params.height = ((addPersonModelArrayList_education.size() - 1) * 100) + complaint_height;
        } else {
            params.height = complaint_height+100;
        }
        recyclerView_addEducation.setLayoutParams(params);



/*
        Adapter_candidateQualification = new CandidateQualificationAdapter(context,candidateQualificationArrayList_education);
        recyclerView_addEducation.setAdapter(Adapter_candidateQualification);
        recyclerView_addEducation.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_education = new LinearLayoutManager(getContext());
        recyclerView_addEducation.setLayoutManager(linearLayoutManager_education);
*/




        imageView_addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialogAddEducation = new CustomDialogAddEducation(getContext(), "0", "0",
                        addPersonAdapter_education, addPersonModelArrayList_education, 0,recyclerView_addEducation);
                customDialogAddEducation.show();

            }
        });

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
        customDialogLoadingProgressBar.setCancelable(false);

        if (isLoggedIn) {
            AsyncTaskLoad getTask = new AsyncTaskLoad();
            getTask.execute("getDetails");
        }




        textView_saveAndContinue = ((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);
        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//
//                        AsyncTaskLoad insertTask = new AsyncTaskLoad();
//                        insertTask.execute("insertDetails");


               /* getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(activity, "Hello", Toast.LENGTH_SHORT).show();
                    }
                });*/


                ProfessionalDetailsFragment professionalDetailsFragment = new ProfessionalDetailsFragment();
                //professionalDetailsFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, professionalDetailsFragment);
                fragmentTransaction.commit() ;

            }
        });


        return view;

    }


    private void insertDetails()
    {

        if (textView_highestQualificationLevelId.getText().equals("0")) {
            editText_highestQualificationLevel.setError("Required");
            return;
        }
        if (textView_qualificationId.getText().equals("0")) {
            editText_qualification.setError("Required");
            return;
        }

        if (editText_institue.getText().toString().isEmpty()) {
            editText_institue.setError("Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        if (editText_percentage.getText().toString().isEmpty()) {
            editText_percentage.setError("Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        if (editText_hobby.getText().toString().isEmpty()) {
            editText_hobby.setError("Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        if (editText_socialContributions.getText().toString().isEmpty()) {
            editText_socialContributions.setError("Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        if (editText_passingYear.getText().toString().isEmpty()) {
            editText_passingYear.setError("Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(editText_passingYear.getText().toString()));
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        final String passingYear = sdf.format(calendar.getTime());






        stringBuilder_education.append("<?xml version=\"1.0\" ?>");
        stringBuilder_education.append("<LowerEducation>");
        Cursor cursor_eduction = sqLiteEducationDetails.getAllData();

        if(cursor_eduction!=null)
        {

            for (boolean hasItem = cursor_eduction.moveToFirst(); hasItem; hasItem = cursor_eduction.moveToNext()) {

                stringBuilder_education.append("<Functions>");
                stringBuilder_education.append("<LowerQualificationUserId>"+cursor_eduction.getString(cursor_eduction.getColumnIndex(SQLiteEducationDetails.EDUCATION_DETAILS_ID))+"</LowerQualificationUserId>");
                stringBuilder_education.append("<EducationLevelId>"+cursor_eduction.getString(cursor_eduction.getColumnIndex(SQLiteEducationDetails.QUALIFICATION_LEVEL_ID))+"</EducationLevelId>");
                stringBuilder_education.append("<EducationNameId>"+cursor_eduction.getString(cursor_eduction.getColumnIndex(SQLiteEducationDetails.QUALIFICATION_ID))+"</EducationNameId>");
                stringBuilder_education.append("<NameOfInstitute>"+cursor_eduction.getString(cursor_eduction.getColumnIndex(SQLiteEducationDetails.INSTITUTE_NAME))+"</NameOfInstitute>");
                stringBuilder_education.append("<LowerQualificationPercentage>"+cursor_eduction.getString(cursor_eduction.getColumnIndex(SQLiteEducationDetails.PERCENTAGE)).toString().replaceAll("%","").trim()+"</LowerQualificationPercentage>");
                stringBuilder_education.append("<LowerQualificationPassingYear>"+cursor_eduction.getString(cursor_eduction.getColumnIndex(SQLiteEducationDetails.PASSING_YEAR))+"</LowerQualificationPassingYear>");
                stringBuilder_education.append("<LowerQualificationLanguageType>"+userModel.getLanguage()+"</LowerQualificationLanguageType>");
                stringBuilder_education.append("</Functions>");

            }

            stringBuilder_education.append("</LowerEducation>");

        }
        else {
            tvHeadingEducationDetails.setError("Please Enter Eductational Details");
            return;
        }

        cursor_eduction.close();








        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_QUALIFICATIONDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);
                            customDialogLoadingProgressBar.dismiss();
                            if(jsonObject.getString("message").equals("Success") && !jsonObject.getBoolean("error"))
                            {
                                getDetails();

                                Toast.makeText(getContext(),"Qualification details saved successfully!", Toast.LENGTH_SHORT).show();

                                getActivity().finish();

                                /*ProfessionalDetailsFragment professionalDetailsFragment = new ProfessionalDetailsFragment();
                                professionalDetailsFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, professionalDetailsFragment);
                                fragmentTransaction.commit() ;*/

                            }
                            else
                            {
                                Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
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

                params.put("QualificationDetailsId",String.valueOf(qualificationDetailsId));
                params.put("UserId",userModel.getUserId());
                params.put("QualificationLevelId",textView_highestQualificationLevelId.getText().toString());
                params.put("QualificationId",textView_qualificationId.getText().toString());
                params.put("Sch_Uni",editText_institue.getText().toString());
                params.put("Percentage",editText_percentage.getText().toString().replaceAll("%","").trim());
                params.put("PassingYear",passingYear);
                params.put("Hobby",editText_hobby.getText().toString());
                params.put("Social_Contribution",editText_socialContributions.getText().toString());
                params.put("xmlDataLowerQualificationDetail",stringBuilder_education.toString());
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
                                    qualificationDetailsId = jsonObject.getInt("QualificationDetailsId");


                                    textView_highestQualificationLevelId.setText(jsonObject.getString("QualificationLevelId"));
                                    textView_qualificationId.setText(jsonObject.getString("QualificationId"));

                                    editText_highestQualificationLevel.setText(jsonObject.getString("QualificationLevelName"));
                                    editText_qualification.setText(jsonObject.getString("Qualification"));
                                    editText_institue.setText(jsonObject.getString("Sch_Uni"));
                                    editText_percentage.setText(jsonObject.getString("Percentage")+" %");
                                    editText_passingYear.setText(jsonObject.getString("PassingYearString"));
                                    editText_hobby.setText(jsonObject.getString("Hobby"));
                                    editText_socialContributions.setText(jsonObject.getString("Social_Contribution"));

                                    sqLiteEducationDetails.deleteAll();
                                    candidateQualificationArrayList_education.clear();
                                  //  Adapter_candidateQualification.notifyDataSetChanged();

                                    addPersonModelArrayList_education.clear();



                                    JSONArray jsonArray_education = jsonObject.getJSONArray("LowerQualificationLST");

                                    for(int j=0; j< jsonArray_education.length(); j++) {
                                        JSONObject jsonObject_details = jsonArray_education.getJSONObject(j);


                                        long id = sqLiteEducationDetails.insertEducationDetails(
                                                jsonObject_details.getString("LowerQualificationDetailId"),
                                                jsonObject_details.getString("QualificationLevelName"),
                                                jsonObject_details.getString("QualificationLevelId"),
                                                jsonObject_details.getString("Qualification"),
                                                jsonObject_details.getString("QualificationId"),
                                                jsonObject_details.getString("NameOfInstitute"),
                                                jsonObject_details.getString("Percentage"),
                                                jsonObject_details.getString("PassingYear") );



                                        addPersonModelArrayList_education.add(new AddPersonModel(String.valueOf(id),
                                                jsonObject_details.getString("QualificationLevelName"),
                                                jsonObject_details.getString("Qualification"),
                                                jsonObject_details.getString("NameOfInstitute")));


                           /*             candidateQualificationArrayList_education.add(new
                                                        CandidateEducationModel(  jsonObject_details.getString("LowerQualificationDetailId"),
                                                jsonObject_details.getString("QualificationLevelId"),
                                                jsonObject_details.getString("QualificationId"),
                                                jsonObject_details.getString("QualificationLevelName"),
                                                jsonObject_details.getString("Qualification"),
                                                jsonObject_details.getString("NameOfInstitute"),
                                                jsonObject_details.getString("Percentage"),
                                                jsonObject_details.getString("PassingYear"))
                                        );*/
                                    }

                                    addPersonAdapter_education.notifyDataSetChanged();

                                    ViewGroup.LayoutParams params = recyclerView_addEducation.getLayoutParams();
                                    int complaint_height=200;
                                    if (addPersonModelArrayList_education.size() > 2) {
                                        params.height = ((addPersonModelArrayList_education.size() - 1) * 100) + complaint_height;
                                    } else {
                                        params.height = complaint_height+100;
                                    }
                                    recyclerView_addEducation.setLayoutParams(params);


                                    //editText_.setText(jsonObject.getString(""));
                                    customDialogLoadingProgressBar.dismiss();
                                }



                            }
                            else
                            {
                                qualificationDetailsId = 0;
                                customDialogLoadingProgressBar.dismiss();

                                if (getContext() != null) {
                                    Toast.makeText(getContext(),"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
                                }

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
                        Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
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

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                //customDialogLoadingProgressBar.dismiss();

                if(params[0].equals("getDetails"))
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            getDetails();
                        }
                    });

                }
                else if(params[0].equals("insertDetails"))
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            insertDetails();
                        }
                    });

                }


                if(params[0].toString().equals("QualificationLevel"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dataFetcher.loadList(URLs.URL_GET_QUALIFICATIONLEVEL+"Language="+userModel.getLanguage(),
                                    "QualificationLevelId",
                                    "QualificationLevelName", editText_highestQualificationLevel,
                                    textView_highestQualificationLevelId, getContext(), customDialogLoadingProgressBar);
                            editText_highestQualificationLevel.setError(null);
                        }
                    });


                }

                else if(params[0].toString().equals("QualificationName"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            editText_qualification.setError(null);
                        }
                    });
                        dataFetcher.loadList(URLs.URL_GET_QUALIFICATIONNAME+"QualificationLevelId="
                                    +params[1].toString()+
                                    "&Language="+userModel.getLanguage(),"QualificationId",
                            "Qualification", editText_qualification, textView_qualificationId,getContext(),
                            customDialogLoadingProgressBar);

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
