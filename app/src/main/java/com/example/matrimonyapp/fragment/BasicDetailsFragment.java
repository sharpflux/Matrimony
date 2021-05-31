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
import android.os.Handler;
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
import com.example.matrimonyapp.customViews.CustomDialogAddMama;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class BasicDetailsFragment extends Fragment  {

    private EditText editText_birthdate, editText_age, editText_birthTime, editText_firstName,
            editText_altMobileNo, editText_altEmailId, editText_mobileNo, editText_address, editText_emailId,
            editText_birthState, editText_birthCity, editText_birthPlace, editText_bloodGroup, editText_state, editText_postalCode,
            editText_currentState,  editText_currentPostalCode, editText_birthCountry,
            editText_currentAddress, editText_currentVillage, editText_village, editText_currentCountry, editText_country,
            editText_city, editText_currentCity,editText_time;
// editText_taluka, editText_district,editText_currentDistrict, editText_currentTaluka,editText_birthDistrict,editText_birthTaluka,
    private ImageView imageView_back;

    private RadioGroup radioGroup_gender, radioGroup_birthTimeType;

    private TextView textView_saveAndContinue;
    private TextView textView_stateId,  textView_birthStateId, textView_bloodGroupId, textView_currentStateId,
            textView_birthCityId, textView_countryId, textView_currentCountryId, textView_birthCountryId,
            textView_cityId, textView_currentCityId;
//textView_districtId, textView_talukaId, textView_currentDistrictId, textView_currentTalukaId,textView_birthDistrictId, textView_birthTalukaId,
    private View view;

    private CheckBox checkBox_isAddressSame;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private int basicDetailsId=0;
    private Handler mHandler;
    private int mHour, mMinute;
    private int intCurrentCountryId =0, intCurrentStateId=0, newCountryId=0, newStateId=0,
            currentBirthCountryId=0, currentBirthStateId=0, newBirthCountryId=0, newBirthStateId=0,
            currentCurrentCountryId=0, currentCurrentStateId=0, newCurrentCountryId=0, newCurrentStateId=0;

    private String timeHrs="00:00:00", timeMin;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private Context context;

    private String fullName, gender, birthdate, birthTime, birthTimeType, birthPlace, mobileNo, altMobileNo,
            altEmailId, emailId, address, village, currentAddress, currentVillage, state, postalCode, birthCountryId,
            birthStateId, birthCityId, countryId, stateId, cityId, currentCountryId, currentStateId, currentCityId,
            currentPostalCode, bloodGroupId;
    // birthState, birthTaluka,birthDistrict,birthTalukaId, birthDistrictId,talukaId, districtId,taluka, district,
    //private ArrayList<MyItem> list;

    private DataFetcher dataFetcher;

    private UserModel userModel;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_basic_details, container, false);

        context = getContext();
        mHandler = new Handler();
        init();
        ((RadioButton)radioGroup_birthTimeType.getChildAt(0)).setChecked(true);

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


                    editText_currentState.setText(editText_state.getText());
                    editText_currentCountry.setText(editText_country.getText());
                    editText_currentCity.setText(editText_city.getText());
                    editText_currentPostalCode.setText(editText_postalCode.getText().toString());


                    textView_currentStateId.setText(textView_stateId.getText().toString());
                    textView_currentCountryId.setText(textView_countryId.getText().toString());
                    textView_currentCityId.setText(textView_cityId.getText().toString());


                    textView_currentStateId.setEnabled(false);
                    textView_currentCountryId.setEnabled(false);
                    textView_currentCityId.setEnabled(false);

                    editText_currentAddress.setEnabled(false);
                    editText_currentVillage.setEnabled(false);


                    editText_currentState.setEnabled(false);
                    editText_currentCountry.setEnabled(false);
                    editText_currentPostalCode.setEnabled(false);
                    editText_currentCity.setEnabled(false);


/*                    textView_currentTalukaId.setText(textView_talukaId.getText().toString());
                    textView_currentDistrictId.setText(textView_districtId.getText().toString());
                    textView_currentTalukaId.setEnabled(false);
                    textView_currentDistrictId.setEnabled(false);
                    editText_currentTaluka.setEnabled(false);
                    editText_currentDistrict.setEnabled(false);
                    editText_currentTaluka.setText(editText_taluka.getText().toString());
                    editText_currentDistrict.setText(editText_district.getText().toString());*/

                }
                else
                {

                    editText_currentAddress.setText("");
                    editText_currentVillage.setText("");


                    editText_currentState.setText("");
                    editText_currentCountry.setText("");
                    editText_currentCity.setText("");
                    editText_currentPostalCode.setText("");

                    textView_currentStateId.setText("0");
                    textView_currentCountryId.setText("0");
                    textView_currentCityId.setText("0");


                    textView_currentStateId.setEnabled(true);
                    textView_currentCountryId.setEnabled(true);
                    textView_currentCityId.setEnabled(true);

                    editText_currentAddress.setEnabled(true);
                    editText_currentVillage.setEnabled(true);


                    editText_currentState.setEnabled(true);
                    editText_currentCountry.setEnabled(true);
                    editText_currentCity.setEnabled(true);
                    editText_currentPostalCode.setEnabled(true);

/*                    editText_currentTaluka.setText("");
                    editText_currentDistrict.setText("");
                    textView_currentTalukaId.setText("0");
                    textView_currentDistrictId.setText("0");
                    textView_currentTalukaId.setEnabled(true);
                    textView_currentDistrictId.setEnabled(true);
                    editText_currentTaluka.setEnabled(true);
                    editText_currentDistrict.setEnabled(true);
*/

                }
            }
        });



        textChangeListener();

        onClickListener();



       // list = new ArrayList<MyItem>();


        dataFetcher = new DataFetcher("State",getContext());

        showPopUp(editText_country, "Country");
        showPopUp(editText_state, "State");
        showPopUp(editText_city, "City");


        showPopUp(editText_birthCountry, "BirthCountry");
        showPopUp(editText_birthState, "BirthState");
        showPopUp(editText_birthCity, "BirthCity");


        showPopUp(editText_currentCountry, "CurrentCountry");
        showPopUp(editText_currentState, "CurrentState");
        showPopUp(editText_currentCity, "CurrentCity");


/*
        showPopupSDT(editText_country, "Country", null);
        showPopupSDT(editText_state, "State", textView_countryId);
        showPopupSDT(editText_city, "City", textView_stateId);
        FieldValidation.textChangedListenerForSDT(editText_country, editText_state, editText_city,
                textView_countryId, textView_stateId, textView_cityId);



        showPopupSDT(editText_currentCountry, "CurrentCountry", null);
        showPopupSDT(editText_currentState, "CurrentState", textView_currentCountryId);
        showPopupSDT(editText_currentCity, "CurrentCity", textView_currentStateId);
        FieldValidation.textChangedListenerForSDT(editText_currentCountry, editText_currentState,  editText_currentCity,
                textView_currentCountryId, textView_currentStateId, textView_currentCityId);



        showPopupSDT(editText_birthCountry, "BirthCountry", null);
        showPopupSDT(editText_birthState, "BirthState", textView_birthCountryId);
        showPopupSDT(editText_birthCity, "BirthCity", textView_birthStateId);
        FieldValidation.textChangedListenerForSDT(editText_birthCountry, editText_birthState, editText_birthCity,
                textView_birthCountryId, textView_birthStateId, textView_birthCityId);*/





        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");



            }
        });

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


    private void textChangeListener() {

        sameAddressListener(editText_address, editText_currentAddress);
        sameAddressListener(editText_village, editText_currentVillage);
/*        sameAddressListener(editText_taluka, editText_currentTaluka);
        sameAddressListener(editText_district, editText_currentDistrict);*/
        sameAddressListener(editText_state, editText_currentState);
        sameAddressListener(editText_country, editText_currentCountry);
        sameAddressListener(editText_city, editText_currentCity);
        sameAddressListener(editText_postalCode, editText_currentPostalCode);

    }

    private void onClickListener() {


        editText_bloodGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("BloodGroup");
            }
        });

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
        editText_time=view.findViewById(R.id.editText_time);

        radioGroup_gender = (RadioGroup) view.findViewById(R.id.radioGroup_gender);

        editText_birthdate=view.findViewById(R.id.editText_birthdate);
        editText_birthTime=view.findViewById(R.id.editText_time);
        editText_age=view.findViewById(R.id.editText_age);

        radioGroup_birthTimeType= view.findViewById(R.id.radioGroup_birthTimeType);

        editText_birthPlace = view.findViewById(R.id.editText_birthPlace);
        editText_birthCountry = view.findViewById(R.id.editText_birthCountry);
        editText_birthState = view.findViewById(R.id.editText_birthState);
        editText_birthCity = view.findViewById(R.id.editText_birthCity);
        /*editText_birthTaluka = view.findViewById(R.id.editText_birthTaluka);
        editText_birthDistrict = view.findViewById(R.id.editText_birthDistrict);*/
        editText_bloodGroup = view.findViewById(R.id.editText_bloodGroup);
        editText_mobileNo = view.findViewById(R.id.editText_mobileNo);
        editText_emailId= view.findViewById(R.id.editText_emailId);
        editText_address = view.findViewById(R.id.editText_address);
        editText_village = view.findViewById(R.id.editText_village);

        textView_birthCountryId = view.findViewById(R.id.textView_birthCountryId);
        textView_birthStateId = view.findViewById(R.id.textView_birthStateId);
        textView_birthCityId = view.findViewById(R.id.textView_birthCityId);
       /* textView_birthDistrictId = view.findViewById(R.id.textView_birthDistrictId);
        textView_birthTalukaId = view.findViewById(R.id.textView_birthTalukaId);*/
        textView_bloodGroupId = view.findViewById(R.id.textView_bloodGroupId);


        editText_country = view.findViewById(R.id.editText_country);
        editText_state = view.findViewById(R.id.editText_state);
        editText_city = view.findViewById(R.id.editText_city);
        editText_postalCode = view.findViewById(R.id.editText_postalCode);
        textView_countryId = view.findViewById(R.id.textView_countryId);
        textView_stateId = view.findViewById(R.id.textView_stateId);
        textView_cityId = view.findViewById(R.id.textView_cityId);



        /*textView_districtId= view.findViewById(R.id.textView_districtId);
        textView_talukaId = view.findViewById(R.id.textView_talukaId);
        editText_taluka = view.findViewById(R.id.editText_taluka);
        editText_district = view.findViewById(R.id.editText_district);*/

        checkBox_isAddressSame = view.findViewById(R.id.checkBox_isAddressSame);

        editText_currentAddress = view.findViewById(R.id.editText_currentAddress);
        editText_currentVillage = view.findViewById(R.id.editText_currentVillage);
        editText_currentCountry = view.findViewById(R.id.editText_currentCountry);
        editText_currentState = view.findViewById(R.id.editText_currentState);
        editText_currentCity = view.findViewById(R.id.editText_currentCity);
        editText_currentPostalCode = view.findViewById(R.id.editText_currentPostalCode);

        textView_currentCountryId = view.findViewById(R.id.textView_currentCountryId);
        textView_currentStateId = view.findViewById(R.id.textView_currentStateId);
        textView_currentCityId = view.findViewById(R.id.textView_currentCityId);

/*
        editText_currentTaluka = view.findViewById(R.id.editText_currentTaluka);
        editText_currentDistrict = view.findViewById(R.id.editText_currentDistrict);
        textView_currentDistrictId = view.findViewById(R.id.textView_currentDistrictId);
        textView_currentTalukaId = view.findViewById(R.id.textView_currentTalukaId);
*/

        imageView_back=((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText(context.getResources().getString(R.string.basic_details));
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
        if (editText_firstName.getText().toString().isEmpty()) {
            editText_firstName.setError("Name is Required");
            customDialogLoadingProgressBar.dismiss();
           return;
        }

        fullName = editText_firstName.getText().toString().trim();

        birthdate = editText_birthdate.getText().toString().trim();

        if (editText_birthdate.getText().toString().isEmpty()) {
            editText_birthdate.setError("Date of Birth Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        if( ((RadioButton)view.findViewById(radioGroup_birthTimeType.getCheckedRadioButtonId()))==null){
            birthTimeType="Accurate";
        }
        else {
            birthTimeType= ((RadioButton)view.findViewById(radioGroup_birthTimeType.getCheckedRadioButtonId()))
                    .getText().toString();
        }
        birthTime = timeHrs;
        if (birthTime.equals("00:00:00")) {
            editText_time.setError("Birth time Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }



        birthPlace = editText_birthPlace.getText().toString().trim();
        if (editText_birthPlace.getText().toString().isEmpty()) {
            editText_birthPlace.setError("Birth place Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


/*        birthTalukaId = textView_birthTalukaId.getText().toString().trim();
        birthDistrictId = textView_birthDistrictId.getText().toString().trim();*/
        birthCountryId = textView_birthCountryId.getText().toString().trim();
        birthStateId = textView_birthStateId.getText().toString().trim();
        birthCityId = textView_birthCityId.getText().toString().trim();
        bloodGroupId = textView_bloodGroupId.getText().toString().trim();
        if (birthCountryId.equals("0")) {
            editText_birthCountry.setError("Country Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        if (birthStateId.equals("0")) {
            editText_birthState.setError("State Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        if (birthCityId.equals("0")) {
            editText_birthCity.setError("State Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        if (bloodGroupId.equals("0")) {
            editText_bloodGroup.setError("Blood group Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }




        emailId = editText_emailId.getText().toString().trim();
        if (editText_emailId.getText().toString().isEmpty()) {
            editText_emailId.setError("Email Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        altEmailId = editText_altEmailId.getText().toString().trim();
        mobileNo = editText_mobileNo.getText().toString().trim();
        if (editText_mobileNo.getText().toString().isEmpty()) {
            editText_mobileNo.setError("Mobile Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        altMobileNo = editText_altMobileNo.getText().toString().trim();


        countryId = textView_countryId.getText().toString().trim();
        if (countryId.equals("0")) {
            editText_country.setError("Country Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        stateId = textView_stateId.getText().toString().trim();
        if (stateId.equals("0")) {
            editText_state.setError("State Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        cityId = textView_cityId.getText().toString().trim();

        village = editText_village.getText().toString().trim();
        if (editText_village.getText().toString().isEmpty()) {
            editText_village.setError("Village Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        address = editText_address.getText().toString().trim();
        if (editText_address.getText().toString().isEmpty()) {
            editText_address.setError("Address Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        postalCode = editText_postalCode.getText().toString().trim();
        if (editText_postalCode.getText().toString().isEmpty()) {
            editText_postalCode.setError("Post Code Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        currentAddress = editText_currentAddress.getText().toString().trim();
        if (editText_currentAddress.getText().toString().isEmpty()) {
            editText_currentAddress.setError("Address Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        currentVillage = editText_currentVillage.getText().toString().trim();
        if (editText_currentVillage.getText().toString().isEmpty()) {
            editText_currentVillage.setError("Current Address Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
        currentCountryId = textView_currentCountryId.getText().toString().trim();

        if (currentCountryId.equals("0")) {
            editText_currentCountry.setError("Country Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }


        currentStateId = textView_currentStateId.getText().toString().trim();
        currentCityId = textView_currentCityId.getText().toString().trim();
        currentPostalCode = editText_currentPostalCode.getText().toString().trim();

        if (currentStateId.equals("0")) {
            editText_currentState.setError("State Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        if (currentCityId.equals("0")) {
            editText_currentCity.setError("State Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }

        if (editText_currentPostalCode.getText().toString().isEmpty()) {
            editText_currentPostalCode.setError("Current Address Required");
            customDialogLoadingProgressBar.dismiss();
            return;
        }
/*
        talukaId = textView_talukaId.getText().toString().trim();
        districtId = textView_districtId.getText().toString().trim();
*/



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

                                getActivity().finish();


                                /*FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);

                                ReligiousDetailsFragment religiousDetailsFragment = new ReligiousDetailsFragment();
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, religiousDetailsFragment);
                                fragmentTransaction.commit() ;*/

                            }
                            else
                            {
                                customDialogLoadingProgressBar.dismiss();
                                Toast.makeText(getContext(),"Sorry for the inconvenience \nPlease try again!",Toast.LENGTH_SHORT).show();
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
                params.put("BirthCountryId",birthCountryId);
                //params.put("BirthDistrictId",birthDistrictId);
                params.put("BirthStateId",birthStateId);
                params.put("BirthCityId", birthCityId);
                params.put("BloodGroupId", bloodGroupId);
                //params.put("StatesID",stateId);
                //params.put("DistrictId",districtId);
                //params.put("TalukasId",talukaId);
                params.put("PermanantAddress",address);
                params.put("PermanantVillage", village);
                params.put("PermanantPostCode",postalCode);
                params.put("PermanantCountryId", countryId);
                params.put("PermanantStateId", stateId);
                params.put("PermanantCityId", cityId);
                params.put("CurrentAddress", currentAddress);
                params.put("CurrentVillage", currentVillage);
                params.put("CurrentPostCode", currentPostalCode);
                params.put("CurrentCountryId", currentCountryId);
                params.put("CurrentStateId", currentStateId);
                params.put("CurrentCityId", currentCityId);

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


                                    //editText_birthTime.setText(jsonObject.getString("BirthTimeString"));
                                    editText_birthPlace.setText(jsonObject.getString("BirthPlace"));

                                    editText_birthCountry.setText(jsonObject.getString("BirthCountryName"));
                                    editText_birthState.setText(jsonObject.getString("BirthStateName"));
                                    editText_birthCity.setText(jsonObject.getString("BirthCityName"));

                                    textView_birthCountryId.setText(jsonObject.getString("BirthCountryId"));
                                    textView_birthStateId.setText(jsonObject.getString("BirthStateId"));
                                    textView_birthCityId.setText(jsonObject.getString("BirthCityId"));


                                    editText_bloodGroup.setText(jsonObject.getString("BloodGroupName"));
                                    textView_bloodGroupId.setText(jsonObject.getString("BloodGroupId"));

                                    editText_altMobileNo.setText(jsonObject.getString("AlternateNo"));
                                    editText_altEmailId.setText(jsonObject.getString("AlternateEmail"));

                                    editText_address.setText(jsonObject.getString("PermanantAddress"));
                                    editText_village.setText(jsonObject.getString("PermanantVillage"));

                                    editText_country.setText(jsonObject.getString("PermanantCountryName"));
                                    editText_state.setText(jsonObject.getString("PermanantState"));
                                    editText_city.setText(jsonObject.getString("PermanantCity"));

                                    textView_countryId.setText(jsonObject.getString("PermanantCountryId"));
                                    textView_stateId.setText(jsonObject.getString("PermanantStateId"));
                                    textView_cityId.setText(jsonObject.getString("PermanantCityId"));

                                    editText_currentAddress.setText(jsonObject.getString("CurrentAddress"));
                                    editText_currentVillage.setText(jsonObject.getString("CurrentVillage"));

                                    editText_currentCountry.setText(jsonObject.getString("CurrentCountryName"));
                                    editText_currentState.setText(jsonObject.getString("CurrentStateName"));
                                    editText_currentCity.setText(jsonObject.getString("CurrentCityName"));


                                    textView_currentCountryId.setText(jsonObject.getString("CurrentCountryId"));
                                    textView_currentStateId.setText(jsonObject.getString("CurrentStateId"));
                                    textView_currentCityId.setText(jsonObject.getString("CurrentCityId"));

                                    //editText_address.setText(jsonObject.getString("CurrentAddress"));

/*
                                    textView_birthDistrictId.setText(jsonObject.getString("BirthDistrictId"));
                                    textView_birthTalukaId.setText(jsonObject.getString("BirthTalukaId"));
                                    textView_districtId.setText(jsonObject.getString("DistrictId"));
                                    textView_talukaId.setText(jsonObject.getString("TalukasId"));
                                    editText_birthDistrict.setText(jsonObject.getString("DistrictNameBirth"));
                                    editText_birthTaluka.setText(jsonObject.getString("TalukaNameBirth"));
                                    editText_district.setText(jsonObject.getString("DistrictName"));
                                    editText_taluka.setText(jsonObject.getString("TalukaName"));
*/

                                    editText_postalCode.setText(jsonObject.getString("PermanantPostCode"));
                                    editText_currentPostalCode.setText(jsonObject.getString("CurrentPostCode"));

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


    private String getAge(int year, int month, int day)
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


        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                //customDialogLoadingProgressBar.dismiss();

                if(params[0].equals("getDetails"))
                {
                    getDetails();

                }
                else if(params[0].equals("insertDetails"))
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            insertDetails();
                        }
                    });


/*                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    fragmentTransaction.addToBackStack(null);

                    UploadDocumentsFragment uploadDocumentsFragment = new UploadDocumentsFragment();

                    fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, uploadDocumentsFragment);
                    fragmentTransaction.commit();*/
                }

                else if(params[0].equals("BloodGroup"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dataFetcher.loadList(URLs.URL_GET_BLOODGROUP+"Language="+userModel.getLanguage(),"BloodTypeId",
                                    "BloodType", editText_bloodGroup, textView_bloodGroupId,getContext(),
                                    customDialogLoadingProgressBar);
                            editText_bloodGroup.setError(null);
                        }
                    });





                }

                if(params[0].equals("Country"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                                    "Name", editText_country, textView_countryId,getContext(),
                                    customDialogLoadingProgressBar);
                            editText_country.setError(null);
                        }
                    });




                }
                else if(params[0].equals("State"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {

                            String id = textView_countryId.getText().toString();

                            dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage()
                                            + "&CountryID="+id,"StatesID",
                                    "StatesName", editText_state, textView_stateId,getContext(),
                                    customDialogLoadingProgressBar);


                            editText_state.setError(null);
                        }
                    });






                }
                else if(params[0].equals("City"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            String id = textView_stateId.getText().toString();

                            dataFetcher.loadList(URLs.URL_GET_CITY+"Language="+userModel.getLanguage()
                                            + "&StateID="+id,"ID",
                                    "Name", editText_city, textView_cityId,getContext(),
                                    customDialogLoadingProgressBar);


                            editText_city.setError(null);
                        }
                    });





                }


/*                else if(params[0].equals("District"))
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_district,
                            textView_districtId,getContext() ,customDialogLoadingProgressBar);

                }
                else if(params[0].equals("Taluka"))
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_taluka,
                            textView_talukaId,getContext() ,customDialogLoadingProgressBar);
                }*/


                if(params[0].equals("CurrentCountry"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            dataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                                    "Name", editText_currentCountry, textView_currentCountryId,getContext(),
                                    customDialogLoadingProgressBar);


                            editText_currentCountry.setError(null);
                        }
                    });






                }
                else if(params[0].equals("CurrentState"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            String id = textView_currentCountryId.getText().toString();

                            dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage()
                                            + "&CountryID="+id,"StatesID",
                                    "StatesName", editText_currentState, textView_currentStateId,getContext(),
                                    customDialogLoadingProgressBar);


                            editText_currentState.setError(null);
                        }
                    });





                }
                else if(params[0].equals("CurrentCity"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {


                            String id = textView_currentStateId.getText().toString();

                            dataFetcher.loadList(URLs.URL_GET_CITY+"Language="+userModel.getLanguage()
                                            + "&StateID="+id,"ID",
                                    "Name", editText_currentCity, textView_currentCityId,getContext(),
                                    customDialogLoadingProgressBar);


                            editText_currentCity.setError(null);
                        }
                    });





                }



/*                else if(params[0].equals("CurrentDistrict"))
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_currentDistrict,
                            textView_currentDistrictId,getContext() ,customDialogLoadingProgressBar);

                }
                else if(params[0].equals("CurrentTaluka"))
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_currentTaluka,
                            textView_currentTalukaId,getContext() ,customDialogLoadingProgressBar);
                }*/

                if(params[0].equals("BirthCountry"))
                {

                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                                    "Name", editText_birthCountry, textView_birthCountryId,getContext(),
                                    customDialogLoadingProgressBar);

                            editText_birthCountry.setError(null);
                        }
                    });


                }
                else if(params[0].equals("BirthState"))
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String id = textView_birthCountryId.getText().toString();

                            dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage()
                                            + "&CountryID="+id,"StatesID",
                                    "StatesName", editText_birthState, textView_birthStateId,getContext(),
                                    customDialogLoadingProgressBar);
                            editText_birthState.setError(null);
                        }
                    });



                }
                else if(params[0].equals("BirthCity"))
                {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            String id = textView_birthStateId.getText().toString();

                            dataFetcher.loadList(URLs.URL_GET_CITY+"Language="+userModel.getLanguage()
                                            + "&StateID="+id,"ID",
                                    "Name", editText_birthCity, textView_birthCityId,getContext(),
                                    customDialogLoadingProgressBar);

                            editText_birthCity.setError(null);
                        }
                    });



                }

                return params[0];

/*                else if(params[0].equals("BirthDistrict"))
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_birthDistrict,
                            textView_birthDistrictId,getContext() ,customDialogLoadingProgressBar);

                }
                else if(params[0].equals("BirthTaluka"))
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_birthTaluka,
                            textView_birthTalukaId,getContext() ,customDialogLoadingProgressBar);
                }*/



            } catch (Exception e) {
                e.printStackTrace();
                //resp = e.getMessage();
            }
            return params[0];
        }


        @Override
        protected void onPostExecute(String result) {
            if(result.equals("Country"))
            {
                newCountryId = Integer.parseInt(textView_countryId.getText().toString());
                if (intCurrentCountryId != newCountryId) {
                    intCurrentCountryId = newCountryId;
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
                if (intCurrentStateId != newStateId) {
                    intCurrentStateId = newStateId;
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
            if(result.equals("BirthCountry"))
            {
                newBirthCountryId = Integer.parseInt(textView_birthCountryId.getText().toString());
                if (currentBirthCountryId != newBirthCountryId) {
                    currentBirthCountryId = newBirthCountryId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_birthStateId.setText("0");
                            editText_birthState.setText("");
                            textView_birthCityId.setText("0");
                            editText_birthCity.setText("");
                        }
                    });

                }
            }
            else if(result.equals("BirthState"))
            {
                newBirthStateId = Integer.parseInt(textView_birthStateId.getText().toString());
                if (currentBirthStateId != newBirthStateId) {
                    currentBirthStateId = newBirthStateId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_birthCityId.setText("0");
                            editText_birthCity.setText("");
                        }
                    });

                }
            }
            if(result.equals("CurrentCountry"))
            {
                currentCurrentCountryId = Integer.parseInt(textView_currentCountryId.getText().toString());
                if (currentCurrentCountryId != newCurrentCountryId) {
                    currentCurrentCountryId = newCurrentCountryId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_currentStateId.setText("0");
                            editText_currentState.setText("");
                            textView_currentCityId.setText("0");
                            editText_currentCity.setText("");
                        }
                    });

                }
            }
            else if(result.equals("CurrentState"))
            {
                newCurrentStateId = Integer.parseInt(textView_currentStateId.getText().toString());
                if (currentCurrentStateId != newCurrentStateId) {
                    currentCurrentStateId = newCurrentStateId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_currentCityId.setText("0");
                            editText_currentCity.setText("");
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
