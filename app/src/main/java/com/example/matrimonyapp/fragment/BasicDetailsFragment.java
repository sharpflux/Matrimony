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
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

    private EditText editText_birthdate, editText_age, editText_birthTime, editText_firstName, editText_altMobileNo, editText_altEmailId,
            editText_mobileNo, editText_address, editText_emailId, editText_birthState, editText_birthTaluka, editText_birthPlace,
            editText_birthDistrict, editText_state, editText_postalCode, editText_taluka, editText_district,
            editText_currentState, editText_currentDistrict, editText_currentTaluka, editText_currentPostalCode,
            editText_currentAddress, editText_currentVillage, editText_village, editText_currentCountry, editText_country;

    private ImageView imageView_back;

    private RadioGroup radioGroup_gender, radioGroup_birthTimeType;

    private TextView textView_saveAndContinue;
    private TextView textView_stateId, textView_districtId, textView_talukaId, textView_birthStateId,
            textView_birthDistrictId, textView_birthTalukaId, textView_currentStateId, textView_currentDistrictId,
            textView_currentTalukaId, textView_countryId, textView_currentCountryId;

    private View view;

    private CheckBox checkBox_isAddressSame;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    protected int basicDetailsId=0;

    private int mHour, mMinute;

    private String timeHrs="00:00:00", timeMin;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    Context context;

    private String fullName, gender, birthdate, birthTime, birthTimeType, birthPlace, birthState, birthTaluka,
            birthDistrict, mobileNo, altMobileNo, altEmailId, emailId, address, state, postalCode, taluka, district,
            birthStateId, birthTalukaId, birthDistrictId, stateId, talukaId, districtId;

    private ArrayList<MyItem> list;

    private DataFetcher dataFetcher;

    private UserModel userModel;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_basic_details, container, false);

        context = getContext();

        init();


        if(userModel!=null)
        {
            editText_firstName.setText(userModel.getFullName());
            //editText_birthdate.setText(userModel.getBirthdate());
            editText_age.setText(userModel.getAge());
            editText_mobileNo.setText(userModel.getMobileNo());
            FieldValidation.setRadioButtonAccToValue(radioGroup_gender,userModel.getGender());
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


        FieldValidation.setRadioButtonAccToValue(radioGroup_gender,userModel.getGender());
        gender = FieldValidation.radioGroupValidation(radioGroup_gender);
        birthTimeType = FieldValidation.radioGroupValidation(radioGroup_birthTimeType);



        checkBox_isAddressSame.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    editText_currentAddress.setText(editText_address.getText().toString());
                    editText_currentVillage.setText(editText_village.getText().toString());

                    editText_currentTaluka.setText(editText_taluka.getText().toString());
                    editText_currentDistrict.setText(editText_district.getText().toString());
                    editText_currentState.setText(editText_state.getText());
                    editText_currentCountry.setText(editText_country.getText());
                    editText_currentPostalCode.setText(editText_postalCode.getText().toString());

                    textView_currentTalukaId.setText(textView_talukaId.getText().toString());
                    textView_currentDistrictId.setText(textView_districtId.getText().toString());
                    textView_currentStateId.setText(textView_stateId.getText().toString());
                    textView_currentCountryId.setText(textView_countryId.getText().toString());

                    textView_currentTalukaId.setEnabled(false);
                    textView_currentDistrictId.setEnabled(false);
                    textView_currentStateId.setEnabled(false);
                    textView_currentCountryId.setEnabled(false);

                    editText_currentAddress.setEnabled(false);
                    editText_currentVillage.setEnabled(false);

                    editText_currentTaluka.setEnabled(false);
                    editText_currentDistrict.setEnabled(false);
                    editText_currentState.setEnabled(false);
                    editText_currentCountry.setEnabled(false);
                    editText_currentPostalCode.setEnabled(false);

                }
                else
                {

                    editText_currentAddress.setText("");
                    editText_currentVillage.setText("");

                    editText_currentTaluka.setText("");
                    editText_currentDistrict.setText("");
                    editText_currentState.setText("");
                    editText_currentCountry.setText("");
                    editText_currentPostalCode.setText("");

                    textView_currentTalukaId.setText("0");
                    textView_currentDistrictId.setText("0");
                    textView_currentStateId.setText("0");
                    textView_currentCountryId.setText("0");

                    textView_currentTalukaId.setEnabled(true);
                    textView_currentDistrictId.setEnabled(true);
                    textView_currentStateId.setEnabled(true);
                    textView_currentCountryId.setEnabled(true);

                    editText_currentAddress.setEnabled(true);
                    editText_currentVillage.setEnabled(true);

                    editText_currentTaluka.setEnabled(true);
                    editText_currentDistrict.setEnabled(true);
                    editText_currentState.setEnabled(true);
                    editText_currentCountry.setEnabled(true);
                    editText_currentPostalCode.setEnabled(true);

                }
            }
        });



        textChangeListener();

onClickListener();



        list = new ArrayList<MyItem>();


        dataFetcher = new DataFetcher("State",getContext());

        showPopupSDT(editText_state, "State", null);
        showPopupSDT(editText_district, "District", textView_stateId);
        showPopupSDT(editText_taluka, "Taluka", textView_districtId);
        FieldValidation.textChangedListenerForSDT(editText_state, editText_district, editText_taluka,
                textView_stateId, textView_districtId, textView_talukaId);


        showPopupSDT(editText_birthState, "BirthState", null);
        showPopupSDT(editText_birthDistrict, "BirthDistrict", textView_birthStateId);
        showPopupSDT(editText_birthTaluka, "BirthTaluka", textView_birthDistrictId);
        FieldValidation.textChangedListenerForSDT(editText_birthState, editText_birthDistrict, editText_birthTaluka,
                textView_birthStateId, textView_birthDistrictId, textView_birthTalukaId);

        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");



            }
        });

        return view;

    }

    private void textChangeListener() {

        sameAddressListener(editText_address, editText_currentAddress);
        sameAddressListener(editText_village, editText_currentVillage);
        sameAddressListener(editText_taluka, editText_currentTaluka);
        sameAddressListener(editText_district, editText_currentDistrict);
        sameAddressListener(editText_state, editText_currentState);
        sameAddressListener(editText_postalCode, editText_currentPostalCode);

    }

    private void onClickListener() {


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
    }

    private void sameAddressListener(final EditText editText, final EditText editText_current)
    {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                //if(editText.hasFocus())
                {
                    if(checkBox_isAddressSame.isChecked())
                    {
                        editText_current.setText(editText.getText().toString());
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void init()
    {

        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

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
        editText_village = view.findViewById(R.id.editText_village);

        editText_country = view.findViewById(R.id.editText_country);
        editText_state= view.findViewById(R.id.editText_state);
        editText_postalCode = view.findViewById(R.id.editText_postalCode);
        editText_taluka = view.findViewById(R.id.editText_taluka);
        editText_district = view.findViewById(R.id.editText_district);
        textView_countryId = view.findViewById(R.id.textView_countryId);
        textView_stateId = view.findViewById(R.id.textView_stateId);
        textView_districtId= view.findViewById(R.id.textView_districtId);
        textView_talukaId = view.findViewById(R.id.textView_talukaId);

        checkBox_isAddressSame = view.findViewById(R.id.checkBox_isAddressSame);

        editText_currentAddress= view.findViewById(R.id.editText_currentAddress);
        editText_currentVillage= view.findViewById(R.id.editText_currentVillage);
        editText_country= view.findViewById(R.id.editText_country);
        editText_currentState= view.findViewById(R.id.editText_currentState);
        editText_currentPostalCode = view.findViewById(R.id.editText_currentPostalCode);
        editText_currentTaluka = view.findViewById(R.id.editText_currentTaluka);
        editText_currentDistrict = view.findViewById(R.id.editText_currentDistrict);
        textView_currentCountryId = view.findViewById(R.id.textView_currentCountryId);
        textView_currentStateId = view.findViewById(R.id.textView_currentStateId);
        textView_currentDistrictId = view.findViewById(R.id.textView_currentDistrictId);
        textView_currentTalukaId = view.findViewById(R.id.textView_currentTalukaId);

        textView_birthStateId= view.findViewById(R.id.textView_birthStateId);
        textView_birthDistrictId = view.findViewById(R.id.textView_birthDistrictId);
        textView_birthTalukaId = view.findViewById(R.id.textView_birthTalukaId);


        imageView_back=((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Basic Details");
        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

    }

    public void showPopupSDT(EditText editText, final String urlFor, final TextView textView_id) {

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = FieldValidation.onClickListenerForSDT(urlFor, textView_id, getContext());
                /*String id = "0";
                if (textView_id != null) {
                    id = textView_id.getText().toString();

                    if (!id.equals("0")) {
                        AsyncTaskLoad runner = new AsyncTaskLoad();
                        runner.execute(urlFor, id);

                    }
                }*/
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute(urlFor, id);

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
        postalCode = editText_postalCode.getText().toString().trim();
        talukaId = textView_talukaId.getText().toString().trim();
        districtId = textView_districtId.getText().toString().trim();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URLs.URL_POST_BASICDETAILS,
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
                params.put("Pincode",postalCode);
                params.put("LanguageType",userModel.getLanguage());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                                    editText_postalCode.setText(jsonObject.getString("Pincode"));
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
                                Toast.makeText(getContext(),"Please enter your details! ",Toast.LENGTH_SHORT).show();
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
