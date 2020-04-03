package com.example.matrimonyapp.fragment;


import android.app.DatePickerDialog;
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
import com.example.matrimonyapp.customViews.CustomDialogAddPercentage;
import com.example.matrimonyapp.customViews.CustomDialogAddYear;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class QualificationDetailsFragment extends Fragment {

    View view;
    private TextView textView_saveAndContinue, textView_qualificationId, textView_highestQualificationLevelId;
    private EditText editText_highestQualificationLevel, editText_qualification, editText_institue,
            editText_percentage, editText_passingYear,
    /*editText_sscNameOfInstitute, editText_sscPer,editText_hscNameOfInstitute,
            editText_hscPer,
            editText_gradCourse, editText_gradNameOfInstitute, editText_gradPer,
            editText_pgCourse, editText_pgNameOfInstitute, editText_pgPer,*/ editText_hobby,
            editText_socialContributions ;

    private ImageView imageView_back;

    String sscNameOfInstitute, sscPer,hscNameOfInstitute, hscPer,
            gradCourse, gradNameOfInstitute, gradPer,
            pgCourse, pgNameOfInstitute, pgPer, hobby, percentage,
            socialContributions ;


    int mYear, mMonth, mDay;

    DatePickerDialog datePickerDialog;

    DataFetcher dataFetcher;
    Bundle bundle;
    UserModel userModel;

    int qualificationDetailsId=0;

    CustomDialogAddPercentage customDialogAddPercentage;
    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    public QualificationDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_qualification_details, container, false);

        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv =((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Qualification");

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

        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

/*        editText_sscNameOfInstitute=view.findViewById(R.id.editText_sscNameOfInstitute);
        editText_sscPer=view.findViewById(R.id.editText_sscPer);
        editText_hscNameOfInstitute=view.findViewById(R.id.editText_hscNameOfInstitute);
        editText_hscPer=view.findViewById(R.id.editText_hscPer);
        editText_gradCourse=view.findViewById(R.id.editText_gradCourse);
        editText_gradNameOfInstitute=view.findViewById(R.id.editText_gradNameOfInstitute);
        editText_gradPer=view.findViewById(R.id.editText_gradPer);
        editText_pgCourse=view.findViewById(R.id.editText_pgCourse);
        editText_pgNameOfInstitute=view.findViewById(R.id.editText_pgNameOfInstitute);
        editText_pgPer=view.findViewById(R.id.editText_pgPer);
        */
        editText_highestQualificationLevel=view.findViewById(R.id.editText_highestQualificationLevel);
        editText_qualification=view.findViewById(R.id.editText_qualification);
        editText_institue=view.findViewById(R.id.editText_institue);
        editText_percentage=view.findViewById(R.id.editText_percentage);
        editText_passingYear=view.findViewById(R.id.editText_passingYear);
        editText_hobby=view.findViewById(R.id.editText_hobby);
        editText_socialContributions=view.findViewById(R.id.editText_socialContributions);
        textView_highestQualificationLevelId=view.findViewById(R.id.textView_highestQualificationLevelId);
        textView_qualificationId=view.findViewById(R.id.textView_qualificationId);


        bundle = getArguments();


        dataFetcher = new DataFetcher("Qualification",getContext());

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());

        editText_percentage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialogAddPercentage = new CustomDialogAddPercentage(getContext(),editText_percentage);
                customDialogAddPercentage.show();

            }
        });

       /* editText_passingYear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar c = Calendar.getInstance();

                mYear = c.get(Calendar.YEAR);
                //mMonth = c.get(Calendar.MONTH);


                 datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {

                        mYear = year;
                        mMonth = monthOfYear;
                        mDay = dayOfMonth;


                        int localMonth = (mMonth)+1;
                        String monthString = localMonth<10?"0"+localMonth:Integer.toString(localMonth);

                        String localYear = Integer.toString(mYear);

                        editText_passingYear.setText(new StringBuilder().append(localYear));




                    }
                },mYear,mMonth,mDay);



                try{




                    Field[] datePickerDialogFields = datePickerDialog.getClass().getDeclaredFields();

                    for(Field datePickerDialogField : datePickerDialogFields)
                    {
                        if(datePickerDialogField.getName().equals("mDatePicker"))
                        {
                            datePickerDialogField.setAccessible(true);

                            DatePicker datePicker1 = (DatePicker)datePickerDialogField.get(datePickerDialog);

                            DatePicker monthPicker = (DatePicker)datePickerDialogField.get(datePickerDialog);

                            Field datePickerFields[] = datePickerDialogField.getType().getDeclaredFields();

                            for(Field datePickerField : datePickerFields)
                            {

                                if("mDelegate".equals(datePickerField.getName()))
                                {
                                    if(Modifier.isPrivate(datePickerField.getModifiers()))
                                    {
                                        datePickerField.setAccessible(true);
                                        Object o = datePickerField.get(datePicker1);

                                        Field[] df = o.getClass().getDeclaredFields();
                                        for(Field newdf : df)
                                        {
                                            if(Modifier.isPrivate(newdf.getModifiers()))
                                            {
                                                newdf.setAccessible(true);
                                                if(newdf.getName().equals("mDayPickerView") )
                                                {
                                                    Object dayPicker = newdf.get(o);
                                                    ((View)dayPicker).setVisibility(View.GONE);

                                                }
                                            }
                                        }

                                    }



                                }

                            }
                            datePicker1.setCalendarViewShown(false);

                        }


                    }

                    datePickerDialog.show();


                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }





            }
        });
*/

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


        AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");


        /*editText_sscPer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {

                if(b==true)
                    editText_sscPer.setHint("--.--");
                else
                    editText_sscPer.setHint("");
            }
        });

        editText_sscPer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });*/


        textView_saveAndContinue = ((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);
        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");




/*                sscNameOfInstitute = editText_sscNameOfInstitute.getText().toString();
                sscPer= editText_sscPer.getText().toString();
                hscNameOfInstitute= editText_hscNameOfInstitute.getText().toString();
                hscPer= editText_hscPer.getText().toString();
                gradCourse= editText_gradCourse.getText().toString();
                gradNameOfInstitute= editText_gradNameOfInstitute.getText().toString();
                gradPer= editText_gradPer.getText().toString();
                pgCourse= editText_pgCourse.getText().toString();
                pgNameOfInstitute= editText_pgNameOfInstitute.getText().toString();
                pgPer= editText_pgPer.getText().toString();

                hobby= editText_hobby.getText().toString();
                socialContributions= editText_socialContributions.getText().toString();
                *//*= .getText().toString();*/

                if (bundle!=null)
                {
/*                    bundle.putString("sscNameOfInstitute",sscNameOfInstitute);
                    bundle.putString("sscPer",sscPer);
                    bundle.putString("hscNameOfInstitute",hscNameOfInstitute);
                    bundle.putString("hscPer",hscPer);
                    bundle.putString("gradCourse",gradCourse);
                    bundle.putString("gradNameOfInstitute",gradNameOfInstitute);
                    bundle.putString("gradPer",gradPer);
                    bundle.putString("pgCourse",pgCourse);
                    bundle.putString("pgNameOfInstitute",pgNameOfInstitute);
                    bundle.putString("pgPer",pgPer);
                    */
                    bundle.putString("hobby",hobby);
                    bundle.putString("socialContributions",socialContributions);
                }
                ProfessionalDetailsFragment professionalDetailsFragment = new ProfessionalDetailsFragment();
                professionalDetailsFragment.setArguments(bundle);

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


        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, Integer.parseInt(editText_passingYear.getText().toString()));
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        final String passingYear = sdf.format(calendar.getTime());

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_QUALIFICATIONDETAILS,
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

                                Toast.makeText(getContext(),"Qualification details saved successfully!", Toast.LENGTH_SHORT).show();

                                ProfessionalDetailsFragment professionalDetailsFragment = new ProfessionalDetailsFragment();
                                professionalDetailsFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, professionalDetailsFragment);
                                fragmentTransaction.commit() ;

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

                params.put("QualificationDetailsId",String.valueOf(qualificationDetailsId));
                params.put("UserId",userModel.getUserId());
                params.put("QualificationLevelId",textView_highestQualificationLevelId.getText().toString());
                params.put("QualificationId",textView_qualificationId.getText().toString());
                params.put("Sch_Uni",editText_institue.getText().toString());
                params.put("Percentage",editText_percentage.getText().toString().replaceAll("%","").trim());
                params.put("PassingYear",passingYear);
                params.put("Hobby",editText_hobby.getText().toString());
                params.put("Social_Contribution",editText_socialContributions.getText().toString());
                params.put("LanguageType",userModel.getLanguage());

                return params;
            }
        };

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

                                    //editText_.setText(jsonObject.getString(""));

                                    try {

                                        String myFormat = "yyyy";
                                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                                        String passingYear = sdf.format(jsonObject.getString("PassingYear"));
                                        editText_passingYear.setText(passingYear);

                                    }
                                    catch (Exception e)
                                    {
                                        e.printStackTrace();
                                    }

                                }



                            }
                            else
                            {
                                qualificationDetailsId = 0;
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
                else if(params[0].toString().equals("insertDetails"))
                {
                    insertDetails();
                }


                if(params[0].toString().equals("QualificationLevel"))
                {
                    dataFetcher.loadList(URLs.URL_GET_QUALIFICATIONLEVEL+"Language="+userModel.getLanguage(),
                            "QualificationLevelId",
                            "QualificationLevelName", editText_highestQualificationLevel,
                            textView_highestQualificationLevelId, getContext(), customDialogLoadingProgressBar);

                }

                else if(params[0].toString().equals("QualificationName"))
                {
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
