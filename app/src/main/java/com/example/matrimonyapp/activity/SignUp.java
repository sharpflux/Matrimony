package com.example.matrimonyapp.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogAccountExists;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private EditText editText_birthdate, editText_age, editText_fullName,
            editText_mobileNo,  editText_emailId, editText_password;

    RadioGroup radioGroup_gender;

    CustomDialogAccountExists customDialogAccountExists;

    TextView textView_signUp, textView_backToSignIn;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String fullName, gender, birthdate, mobileNo,
            emailId, age, password;

    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        editText_fullName = findViewById(R.id.editText_fullName);

        radioGroup_gender = (RadioGroup) findViewById(R.id.radioGroup_gender);

        editText_birthdate=findViewById(R.id.editText_birthdate);
        editText_age=findViewById(R.id.editText_age);

        editText_mobileNo = findViewById(R.id.editText_mobileNo);
        editText_emailId = findViewById(R.id.editText_emailId);
        editText_password = findViewById(R.id.editText_password);
        textView_signUp = findViewById(R.id.textView_signUp);
        textView_backToSignIn = findViewById(R.id.textView_backToSignIn);
        FieldValidation.validateRadioGroup(radioGroup_gender);


        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkRequiredFields(editText_fullName, editText_mobileNo, editText_emailId, editText_birthdate, editText_password)) {
                    verifyMobileNo();
                }

            }
        });

        textView_backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*finish();*/
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int id = radioGroup.getCheckedRadioButtonId();

                RadioButton rb = findViewById(id);

                gender= rb.getText().toString();
                //Toast.makeText(getContext(),gender,Toast.LENGTH_LONG).show();
            }
        });

        editText_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();

                final DatePickerDialog dialog = new DatePickerDialog(SignUp.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
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
                        //dialog.cancel();

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                // dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
                calendar.add(Calendar.YEAR, -20);

                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
                dialog.show();


            }
        });

    }

    private boolean checkRequiredFields(EditText ...editText) {
        boolean flag = true;
        for (EditText temp_editText: editText
             ) {

            if(temp_editText.getText().toString().equals(""))
            {
                temp_editText.setError("This is required field", getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
                flag = false;

            }

        }
        return flag;

    }


    void verifyMobileNo()
    {
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(false);

        mobileNo = editText_mobileNo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_VERIFYMOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (obj.getBoolean("error")) {

                                getOtp();

                            }
                            else{

                                customDialogAccountExists = new CustomDialogAccountExists(SignUp.this);

                                customDialogAccountExists.show();

                                customDialogAccountExists.textView_createNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        editText_fullName.requestFocus();
                                        editText_fullName.setText("");
                                        editText_mobileNo.setText("");
                                        editText_emailId.setText("");
                                        editText_birthdate.setText("");
                                        editText_age.setText("");
                                        editText_password.setText("");
                                        radioGroup_gender.clearCheck();
                                        customDialogAccountExists.dismiss();

                                    }
                                });



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Something went wrong please try again",Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OTPMobileNo", mobileNo);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }


    void getOtp()
    {
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(false);

        mobileNo = editText_mobileNo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_OTP+"MobileNo="+mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (!obj.getBoolean("error") && obj.getString("message").equals("Success")) {


                                fullName = editText_fullName.getText().toString().trim();
                                mobileNo = editText_mobileNo.getText().toString().trim();
                                emailId = editText_emailId.getText().toString().trim();
                                birthdate = editText_birthdate.getText().toString().trim();
                                age = editText_age.getText().toString().trim();
                                password = editText_password.getText().toString().trim();
                                gender = ((RadioButton)findViewById(radioGroup_gender.getCheckedRadioButtonId()))
                                        .getText().toString();


                                Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                                intent.putExtra("OTP",obj.getString("OTP"));
                                intent.putExtra("parentActivity","SignUp");
                                intent.putExtra("fullName",fullName);
                                intent.putExtra("mobileNo",mobileNo);
                                intent.putExtra("emailId",emailId);
                                intent.putExtra("birthdate",birthdate);
                                intent.putExtra("age",age);
                                intent.putExtra("gender",gender);
                                intent.putExtra("password",password);
                                startActivity(intent);


                            }
                            else{
                                /*builder.setMessage("Please try again...")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Sorry, Try again");
                                alert.show();*/
                                /*customDialogAccountExists = new CustomDialogAccountExists(SignUp.this);

                                customDialogAccountExists.show();

                                customDialogAccountExists.textView_createNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        editText_fullName.requestFocus();
                                        editText_fullName.setText("");
                                        editText_mobileNo.setText("");
                                        editText_emailId.setText("");
                                        editText_birthdate.setText("");
                                        editText_age.setText("");
                                        editText_password.setText("");
                                        radioGroup_gender.clearCheck();
                                        customDialogAccountExists.dismiss();

                                    }
                                });*/



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* builder.setMessage("Something went wrong please try again !")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Error");
                        alert.show();*/

                        //  ---  progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("OTPMobileNo", mobileNo);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



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
}
