package com.example.matrimonyapp.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.matrimonyapp.modal.MyItem;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BasicDetailsFragment extends Fragment {

    EditText editText_birthdate, editText_age, editText_birthTime, editText_firstName, editText_altMobileNo, editText_altEmailId,
            editText_mobileNo, editText_address, editText_emailId, editText_birthState, editText_birthTaluka, editText_birthPlace,
            editText_birthDistrict, editText_state, editText_pincode, editText_taluka, editText_district;

    private ImageView imageView_back;

    private RadioGroup radioGroup_gender, radioGroup_birthTimeType;

    private TextView textView_saveAndContinue;
    private TextView textView_stateId, textView_districtId, textView_talukaId, textView_birthStateId,
            textView_birthDistrictId, textView_birthTalukaId;

    private View view;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    protected int basicDetailsId=0;

    private int mHour, mMinute;

    private String timeHrs="00:00:00", timeMin;
    AlertDialog.Builder builder;
    ProgressDialog progressDialog;
    Context context;

    private String fullName, gender, birthdate, birthTime, birthTimeType, birthPlace, birthState, birthTaluka,
            birthDistrict, mobileNo, altMobileNo, altEmailId, emailId, address, state, pincode, taluka, district,
            birthStateId, birthTalukaId, birthDistrictId, stateId, talukaId, districtId;

    ArrayList<MyItem> list;

    DataFetcher dataFetcher;

    UserModel userModel;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_basic_details, container, false);

        context = getContext();

        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());

        editText_firstName = (EditText) view.findViewById(R.id.editText_firstName);
        editText_altMobileNo = view.findViewById(R.id.editText_altMobileNo);
        editText_altEmailId = view.findViewById(R.id.editText_altEmailId);

        radioGroup_gender = (RadioGroup) view.findViewById(R.id.radioGroup_gender);

        editText_birthdate=view.findViewById(R.id.editText_birthdate);
        editText_birthTime=view.findViewById(R.id.editText_time);
        editText_age=view.findViewById(R.id.editText_age);

        radioGroup_birthTimeType= view.findViewById(R.id.radioGroup_birthTimeType);

        editText_birthPlace = view.findViewById(R.id.editText_birthPlace);
        editText_birthState = view.findViewById(R.id.editText_birthState);
        editText_birthTaluka = view.findViewById(R.id.editText_birthTaluka);
        editText_birthDistrict = view.findViewById(R.id.editText_birthDistrict);
        editText_mobileNo = view.findViewById(R.id.editText_mobileNo);
        editText_emailId= view.findViewById(R.id.editText_emailId);
        editText_address = view.findViewById(R.id.editText_address);

        editText_state= view.findViewById(R.id.editText_state);
        editText_pincode = view.findViewById(R.id.editText_pincode);
        editText_taluka = view.findViewById(R.id.editText_taluka);
        editText_district = view.findViewById(R.id.editText_district);


        textView_stateId = view.findViewById(R.id.textView_stateId);
        textView_districtId= view.findViewById(R.id.textView_districtId);
        textView_talukaId = view.findViewById(R.id.textView_talukaId);

        textView_birthStateId= view.findViewById(R.id.textView_birthStateId);
        textView_birthDistrictId = view.findViewById(R.id.textView_birthDistrictId);
        textView_birthTalukaId = view.findViewById(R.id.textView_birthTalukaId);


        if(userModel!=null)
        {
            editText_firstName.setText(userModel.getFullName());
            //editText_birthdate.setText(userModel.getBirthdate());
            editText_age.setText(userModel.getAge());
            editText_mobileNo.setText(userModel.getMobileNo());
            editText_emailId.setText(userModel.getEmailId());

            final Calendar calendar = Calendar.getInstance();
            String myFormat = "yyyy-MM-dd";
            SimpleDateFormat formatter = new SimpleDateFormat(myFormat);
            try {
                Date date = formatter.parse(userModel.getBirthdate());

                editText_birthdate.setText(formatter.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }


        AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");


        FieldValidation fieldValidation = new FieldValidation(context);

        gender = FieldValidation.radioGroupValidation(radioGroup_gender);
        birthTimeType = FieldValidation.radioGroupValidation(radioGroup_birthTimeType);


        editText_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        final Calendar calendar = Calendar.getInstance();
                    //DatePickerDialog d = new DatePickerDialog()
                        DatePickerDialog dialog = new DatePickerDialog(getActivity(),R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                                calendar.set(Calendar.YEAR, year);
                                calendar.set(Calendar.MONTH, (month));
                                calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                                String myFormat = "yyyy-MM-dd";
                                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                                editText_birthdate.setText(sdf.format(calendar.getTime()));
                                String age = getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                editText_age.setText(age);
                            }
                        },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        // dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
                        calendar.add(Calendar.YEAR, -20);

                        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
                        dialog.show();




            }
        });


        editText_birthTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),R.style.DialogTheme,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                int hrs;
                                String time="";
                                timeHrs = hourOfDay+":"+minute+":00";
                                if(hourOfDay>12)
                                {
                                    hourOfDay = hourOfDay%12;

                                    time =  String.format("%02d",hourOfDay)+":"+String.format("%02d",minute)+"pm";
                                }
                                else if(hourOfDay==12)
                                {
                                    time =  String.format("%02d",hourOfDay)+":"+String.format("%02d",minute)+"pm";
                                }
                                else
                                {
                                    time = String.format("%02d",hourOfDay)+":"+String.format("%02d",minute)+"am";
                                }

                                editText_birthTime.setText(time);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

/*    radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {

            int id = radioGroup.getCheckedRadioButtonId();

            RadioButton rb = view.findViewById(id);

            gender= rb.getText().toString();
            //Toast.makeText(getContext(),gender,Toast.LENGTH_LONG).show();
        }
    });*/

/*
    radioGroup_birthTimeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();

                RadioButton rb = view.findViewById(id);

                birthTimeType = rb.getText().toString();
                //Toast.makeText(getContext(),gender,Toast.LENGTH_LONG).show();
            }
        });*/

/*

        radioGroup_birthTimeType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();

                RadioButton rb = view.findViewById(id);

                birthTimeType= rb.getText().toString();
                //Toast.makeText(getContext(),birthTimeType,Toast.LENGTH_SHORT).show();
            }
        });
*/

        imageView_back=((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Basic Details");
        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStack();
                }
                else
                    getActivity().finish();
            }
        });

/*        editText_birthPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });*/

        list = new ArrayList<MyItem>();


        dataFetcher = new DataFetcher("State",getContext());



        editText_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "State";
                runner.execute(control);
               /* dataFetcher.loadList(URLs.URL_GETSTATE+"Language=en","StatesID",
                        "StatesName", editText_state, textView_stateId,getContext());*/


            }
        });

        editText_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "District";
                String id = textView_stateId.getText().toString();
                runner.execute(control,id);
/*


*/

            }
        });

        editText_taluka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                String control = "Taluka";
                String id = textView_districtId.getText().toString();
                runner.execute(control,id);

            }
        });


        showPopUp(editText_birthState, "BirthState");
        showPopUp(editText_birthDistrict, "BirthDistrict");
        showPopUp(editText_birthTaluka, "BirthTaluka");


        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");

/*                Bundle bundle = new Bundle();
                bundle.putString("fullName",fullName);
                bundle.putString("gender",gender);
                bundle.putString("birthdate",birthdate);
                bundle.putString("birthTime",birthTime);
                bundle.putString("birthTimeType",birthTimeType);
                bundle.putString("birthPlace",birthPlace);
                bundle.putString("birthTaluka",birthTaluka);
                bundle.putString("birthDistrict",birthDistrict);
                bundle.putString("mobileNo",mobileNo);
                bundle.putString("emailId",emailId);
                bundle.putString("address",address);
                bundle.putString("state",state);
                bundle.putString("pincode",pincode);
                bundle.putString("taluka",taluka);
                bundle.putString("district",district);



                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

                fragmentTransaction.addToBackStack(null);

                ReligiousDetailsFragment religiousDetailsFragment = new ReligiousDetailsFragment();
                religiousDetailsFragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, religiousDetailsFragment);
                fragmentTransaction.commit() ;*/



            }
        });


        //textChangedListener();


        return view;

    }




    private void textChangedListener()
    {

        editText_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_district.setText("");
                textView_districtId.setText("0");
                editText_taluka.setText("");
                textView_talukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_taluka.setText("");
                textView_talukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_birthState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_birthDistrict.setText("");
                textView_birthDistrictId.setText("0");
                editText_birthTaluka.setText("");
                textView_birthTalukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_birthDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_birthTaluka.setText("");
                textView_birthTalukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }


    void insertDetails()
    {

        fullName = editText_firstName.getText().toString().trim();

        birthdate = editText_birthdate.getText().toString().trim();
        birthTime = timeHrs;
        birthTimeType= ((RadioButton)view.findViewById(radioGroup_birthTimeType.getCheckedRadioButtonId()))
                .getText().toString();

        birthPlace = editText_birthPlace.getText().toString().trim();
        birthTalukaId = textView_birthTalukaId.getText().toString().trim();
        birthDistrictId = textView_birthDistrictId.getText().toString().trim();
        birthStateId = textView_birthStateId.getText().toString().trim();

        address = editText_address.getText().toString().trim();
        emailId = editText_emailId.getText().toString().trim();
        altEmailId = editText_altEmailId.getText().toString().trim();
        mobileNo = editText_mobileNo.getText().toString().trim();
        altMobileNo = editText_altMobileNo.getText().toString().trim();

        stateId = textView_stateId.getText().toString().trim();
        pincode = editText_pincode.getText().toString().trim();
        talukaId = textView_talukaId.getText().toString().trim();
        districtId = textView_districtId.getText().toString().trim();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_BASICDETAILS,
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

                                    Toast.makeText(context,"Basic details saved successfully!", Toast.LENGTH_SHORT).show();

                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    fragmentTransaction.addToBackStack(null);

                                    ReligiousDetailsFragment religiousDetailsFragment = new ReligiousDetailsFragment();

                                    fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, religiousDetailsFragment);
                                    fragmentTransaction.commit();

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

                params.put("BasicDetailsId",String.valueOf(basicDetailsId));
                params.put("UserId",userModel.getUserId());
                params.put("AlternateNo",altMobileNo);
                params.put("AlternateEmail",altEmailId);
                params.put("BirthTime",birthTime);
                params.put("AccuracyOfTime",birthTimeType);
                params.put("BirthPlace",birthPlace);
                params.put("BirthTalukaId",birthTalukaId);
                params.put("BirthDistrictId",birthDistrictId);
                params.put("BirthStatesID",birthStateId);
                params.put("StatesID",stateId);
                params.put("DistrictId",districtId);
                params.put("TalukasId",talukaId);
                params.put("Address",address);
                params.put("Pincode",pincode);
                params.put("LanguageType",userModel.getLanguage());

                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);



    }

    void getDetails()
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
                                    basicDetailsId = jsonObject.getInt("BasicDetailsId");

/*
                                    address = jsonObject.getString("Address");
                                    birthTimeType = jsonObject.getString("BirthTimeString");
                                    birthPlace = jsonObject.getString("BirthPlace");
                                    birthState= jsonObject.getString("StatesNameBirth");
                                    birthDistrict = jsonObject.getString("DistrictNameBirth");
                                    birthTaluka = jsonObject.getString("TalukaNameBirth");
                                    altMobileNo = jsonObject.getString("AlternateNo");
                                    altEmailId = jsonObject.getString("AlternateEmail");
                                    state = jsonObject.getString("StatesName");
                                    district = jsonObject.getString("DistrictName");
                                    taluka = jsonObject.getString("TalukaName");
                                    stateId = jsonObject.getString("StatesID");
                                    districtId = jsonObject.getString("DistrictId");
                                    talukaId = jsonObject.getString("TalukasId");
                                    birthStateId = jsonObject.getString("TalukasId");
                                    birthDistrictId = jsonObject.getString("BirthDistrictId");
                                    birthTalukaId = jsonObject.getString("BirthTalukaId");


                                    pincode = jsonObject.getString("Pincode");*/


                                    textView_birthStateId.setText(jsonObject.getString("BirthStatesID"));
                                    textView_birthDistrictId.setText(jsonObject.getString("BirthDistrictId"));
                                    textView_birthTalukaId.setText(jsonObject.getString("BirthTalukaId"));

                                    textView_stateId.setText(jsonObject.getString("StatesID"));
                                    textView_districtId.setText(jsonObject.getString("DistrictId"));
                                    textView_talukaId.setText(jsonObject.getString("TalukasId"));

                                    editText_address.setText(jsonObject.getString("Address"));
                                    //editText_birthTime.setText(jsonObject.getString("BirthTimeString"));
                                    editText_birthPlace.setText(jsonObject.getString("BirthPlace"));
                                    editText_birthState.setText(jsonObject.getString("StatesNameBirth"));
                                    editText_birthDistrict.setText(jsonObject.getString("DistrictNameBirth"));
                                    editText_birthTaluka.setText(jsonObject.getString("TalukaNameBirth"));
                                    editText_altMobileNo.setText(jsonObject.getString("AlternateNo"));
                                    editText_altEmailId.setText(jsonObject.getString("AlternateEmail"));
                                    editText_state.setText(jsonObject.getString("StatesName"));
                                    editText_district.setText(jsonObject.getString("DistrictName"));
                                    editText_taluka.setText(jsonObject.getString("TalukaName"));
                                    editText_pincode.setText(jsonObject.getString("Pincode"));
                                    //editText_.setText(jsonObject.getString(""));

                                    FieldValidation.setRadioButtonAccToValue(radioGroup_birthTimeType,
                                            jsonObject.getString("AccuracyOfTime"));

                                    String birthTimeString = jsonObject.getString("BirthTimeString");
                                    String []t = birthTimeString.split(":");

                                    int hr = Integer.parseInt(t[0]);
                                    int min = Integer.parseInt(t[1]);


                                    String time="";

                                    timeHrs = hr+":"+min+":00";

                                    if(hr>12)
                                    {
                                        hr = hr%12;

                                        time = String.format("%02d",hr)+":"+String.format("%02d",min)+"pm";
                                    }
                                    else if(hr==12)
                                    {
                                        time = String.format("%02d",hr)+":"+String.format("%02d",min)+"pm";
                                    }
                                    else
                                    {
                                        time = String.format("%02d",hr)+":"+String.format("%02d",min)+"am";
                                    }

                                    editText_birthTime.setText(time);


                                }



                            }
                            else
                            {
                                basicDetailsId = 0;
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


    private void register() {


        builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);

        fullName = editText_firstName.getText().toString();
        mobileNo  = editText_mobileNo.getText().toString();
        address = editText_address.getText().toString();
        emailId = editText_emailId.getText().toString();
        int selectedId = radioGroup_gender.getCheckedRadioButtonId();
        RadioButton rb=(RadioButton)view.findViewById(selectedId);
        gender = rb.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            Toast.makeText(getContext(),"OKAY",Toast.LENGTH_LONG).show();
                            //converting response to json object

                            JSONObject obj = new JSONObject(response);

                            progressDialog.dismiss();
                            if (!obj.getBoolean("error")) {


                                FragmentTransaction transection = getFragmentManager().beginTransaction();
                                transection.replace(R.id.dynamic_fragment_frame_layout, new ReligiousDetailsFragment());
                                transection.commit() ;

/*                                progressDialog = ProgressDialog.show(getContext(),
                                        "Successfull...",
                                        "registration successfull..");*/


                            }
                            else{
                                builder.setMessage("User already exists with same Mobile No please Login")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("User already exists");
                                alert.show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        /*txt_signUp.setText("Sign Up");
                        txt_signUp.setEnabled(true);*/
                        progressDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        builder.setMessage("Something went wrong please try again !")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Error");
                        alert.show();
                        /*txt_signUp.setText("Sign Up");
                        txt_signUp.setEnabled(true);*/
                        progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId","0");
                params.put("FullName", fullName);
                params.put("MobileNo", mobileNo);
                params.put("EmailId", emailId);
                params.put("Gender", gender);
                params.put("Address", address);
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }




   String getAge(int year, int month, int day)
   {

       Calendar dob = Calendar.getInstance();
       Calendar today = Calendar.getInstance();

       dob.set(year, month, day);

       int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

       if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
       {
           age--;
       }

       //Integer ageInt = new Integer(age);

       return String.valueOf(age);



   }


    private void showPopUp(EditText editText, final String urlFor)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();

                String id="";

                if(urlFor.equals("BirthDistrict"))
                {
                    id =  textView_birthStateId.getText().toString();
                }

                if(urlFor.equals("BirthTaluka"))
                {
                    id = textView_birthDistrictId.getText().toString();
                }

                runner.execute(urlFor, id);
            }
        });


    }


    @Override
    public void onResume() {
        super.onResume();

        AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");

    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String functionFor;



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

                if(params[0].toString().equals("State"))
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),"StatesID",
                            "StatesName", editText_state, textView_stateId,getContext(),
                            customDialogLoadingProgressBar);


                }
                else if(params[0].toString()=="District")
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_district,
                            textView_districtId,getContext() ,customDialogLoadingProgressBar);

                }
                else if(params[0].toString()=="Taluka")
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_taluka,
                            textView_talukaId,getContext() ,customDialogLoadingProgressBar);
                }
                else if(params[0].toString()=="BirthState")
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),
                            "StatesID", "StatesName", editText_birthState,
                            textView_birthStateId,getContext() ,customDialogLoadingProgressBar);


                }
                else if(params[0].toString()=="BirthDistrict")
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_birthDistrict,
                            textView_birthDistrictId,getContext() ,customDialogLoadingProgressBar);

                }
                else if(params[0].toString()=="BirthTaluka")
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_birthTaluka,
                            textView_birthTalukaId,getContext() ,customDialogLoadingProgressBar);
                }



            } catch (Exception e) {
                e.printStackTrace();
                //resp = e.getMessage();
            }
            return functionFor;
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
