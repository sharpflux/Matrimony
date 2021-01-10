package com.example.matrimonyapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VerifyOtpActivity extends AppCompatActivity {

/*    int timer=20;
    Timer timer_dec;

    AsynTaskTimer asynTaskTimer;*/

    private Handler handler;

    private Bundle bundle;

    private String otpSent, otpEntered, parentActivity;

    private String fullName, gender, birthdate, mobileNo,
            emailId, age, password, language="en";


    AlertDialog.Builder builder;

    private CountDownTimer countDownTimer;

    private TextView textView_timeOut, textView_verifyOtp, textView_resendOTP;
    private EditText editText_num1, editText_num2, editText_num3, editText_num4;
    private String currentLanguage;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        bundle = getIntent().getExtras();

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        editText_num1 = findViewById(R.id.editText_num1);
        editText_num2 = findViewById(R.id.editText_num2);
        editText_num3 = findViewById(R.id.editText_num3);
        editText_num4 = findViewById(R.id.editText_num4);
        textView_verifyOtp = findViewById(R.id.textView_verifyOtp);
        textView_timeOut = findViewById(R.id.textView_timeOut);
        textView_resendOTP = findViewById(R.id.textView_resendOTP);

        handler = new Handler();

        firebaseAuth = FirebaseAuth.getInstance();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(VerifyOtpActivity.this);

/*        asynTaskTimer = new AsynTaskTimer();
        asynTaskTimer.execute();*/

        startTimer(30, textView_timeOut);
        onNumClick(editText_num1, editText_num2);
        onNumClick(editText_num2, editText_num3);
        onNumClick(editText_num3, editText_num4);

        if(bundle!=null)
        {
            otpSent = bundle.getString("OTP");
            parentActivity = bundle.getString("parentActivity");
            mobileNo = bundle.getString("mobileNo");
        }


        textView_resendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsynTaskTimer runner = new AsynTaskTimer();
                runner.execute("resendOTP");
            }
        });

        textView_verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //registerUser();
                otpEntered = editText_num1.getText().toString().trim()+editText_num2.getText().toString().trim()+editText_num3.getText().toString().trim()+editText_num4.getText().toString().trim();

               /* if(parentActivity.equals("GetOtp"))
                {
                    Intent intent = new Intent(VerifyOtpActivity.this, ForgotPassword.class);
                    intent.putExtra("userId",bundle.getString("userId"));
                    startActivity(intent);
                }
//*/
                //if(otpSent.equals(otpEntered))
                {
//
                    if(parentActivity.equals("SignUp"))
                    {
                        fullName = bundle.getString("fullName");
                        mobileNo = bundle.getString("mobileNo");
                        emailId = bundle.getString("emailId");
                        birthdate = bundle.getString("birthdate");
                        age = bundle.getString("age");
                        gender = bundle.getString("gender");
                        password = bundle.getString("password");

                        registerUser();

                    }
                    else if(parentActivity.equals("GetOtp"))
                    {
                        Intent intent = new Intent(VerifyOtpActivity.this, ForgotPassword.class);
                        intent.putExtra("userId",bundle.getString("userId"));
                        startActivity(intent);
                    }


                }


            }
        });


    }


    void getOtp() //final String userId
    {


        if(bundle!=null)
        {
            otpSent = bundle.getString("OTP");
            parentActivity = bundle.getString("parentActivity");
            mobileNo = bundle.getString("mobileNo");
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_OTP+"MobileNo="+mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (!obj.getBoolean("error") && obj.getString("message").equals("Success")) {

                                editText_num1.setText("");
                                editText_num2.setText("");
                                editText_num3.setText("");
                                editText_num4.setText("");

                                countDownTimer.cancel();
                                startTimer(30, textView_timeOut);

                            }
                            else{

                                Toast.makeText(getApplicationContext(),"Something went wrong!",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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


    public void startTimer(final int seconds, final TextView textView)
    {
        countDownTimer = new CountDownTimer(seconds * 1000 + 1000, 1000) {
            int sec=0, minutes=0;
            @Override
            public void onTick(long millisUntilFinished) {
                sec = seconds;
                sec = (int)(millisUntilFinished/1000);
                minutes = sec/60;
                sec = sec % 60;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(String.format("%02d",minutes) + ":" + String.format("%02d",sec));
                    }
                });


            }

            @Override
            public void onFinish() {

                AsynTaskTimer runner = new AsynTaskTimer();
                runner.execute("timeUp");

            }
        };

        countDownTimer.start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }



    public void registerUser()
    {

        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(false);

        fullName = bundle.getString("fullName");
        mobileNo = bundle.getString("mobileNo");
        emailId = bundle.getString("emailId");
        birthdate = bundle.getString("birthdate");
        age = bundle.getString("age");
        gender = bundle.getString("gender");
        password = bundle.getString("password");



            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URLs.URL_POST_REGISTRATION,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                //converting response to json object
                                JSONObject jsonObject = new JSONObject(response);


                                if(jsonObject.getString("message").equals("Success")) {

                                    UserModel userModel = new UserModel(jsonObject.getString("UserId"),
                                            fullName,
                                            mobileNo,
                                            emailId,
                                            birthdate,
                                            age,
                                            gender,
                                            "0",
                                            language);

                                    CustomSharedPreference.getInstance(getApplicationContext()).saveUser(userModel);


                                    registerFirebaseUser(fullName, mobileNo, birthdate, age, gender, emailId,
                                            password, jsonObject.getString("UserId"));



                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Sorry for the inconvenience \n Please try again!",Toast.LENGTH_SHORT).show();

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
                    params.put("UserId", "0");
                    params.put("FullName", fullName);
                    params.put("MobileNo", mobileNo);
                    params.put("EmailId", emailId);
                    params.put("Gender", gender);
                    params.put("DateOfBirth", birthdate);
                    params.put("Age", age);
                    params.put("AgentId", String.valueOf("0"));
                    params.put("RegisterBy", "Self");
                    //params.put("MembershipId", "1");
                    params.put("ProfileImage", "0");
                    params.put("Password", password);
                    params.put("LanguageType", language);
                    return params;


                }
            };

            VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);





    }

    private void registerFirebaseUser(final String username, final String mobileNo, final String birthdate,
                                      final String age, final String gender, final String email, String password, final String userId)
    {
        password="123456";

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String firebaseUserId = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUserId);

                            HashMap<String, String>  hashMap = new HashMap<>();
                            hashMap.put("firebaseUserId", firebaseUserId);
                            hashMap.put("userId", userId);
                            hashMap.put("userName", username);
                            hashMap.put("emailId", email);
                            hashMap.put("mobileNo", mobileNo);
                            hashMap.put("birthdate", birthdate);
                            hashMap.put("age", age);
                            hashMap.put("gender", gender);
                            hashMap.put("profilePic","default");
                            hashMap.put("activityStatus","offline");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(VerifyOtpActivity.this,"You have successfully registered!",Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(VerifyOtpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else
                                    {
                                        Toast.makeText(VerifyOtpActivity.this,"Sorry for inconvenience caused\n Please try again",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(getApplicationContext(),"BACK Pressed",Toast.LENGTH_SHORT).show();
        finish();
    }

/*    @Override
    protected void onResume() {
        super.onResume();
        handler = new Handler();
        //asynTaskTimer = new AsynTaskTimer();
        //asynTaskTimer.execute();
    }*/

    void onNumClick(final EditText editText1, final EditText editText2)
    {

        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(charSequence.length()>0)
                {
                    editText1.clearFocus();
                    editText2.requestFocus();
                    editText2.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    public  class AsynTaskTimer extends AsyncTask<String , String, String>{

        @Override
        protected String doInBackground(String[] params) {

            try{

                if(params[0].equals("timeUp"))
                {
                    timeUp();
                }
                else if(params[0].equals("resendOTP"))
                {
                    getOtp();
                }

            return params[0];
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }




            return null;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            customDialogLoadingProgressBar.show();

        }

        @Override
        protected void onPostExecute(String o) {
            super.onPostExecute(o);
            customDialogLoadingProgressBar.dismiss();

                if(o.equals("timeUp"))
                {

                }
               // finish();

        }

        @Override
        protected void onProgressUpdate(String[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String o) {
            super.onCancelled(o);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
        finish();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
        if(countDownTimer!=null)
        {
            countDownTimer.cancel();
        }

    }

    private void timeUp() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(VerifyOtpActivity.this, "OTP expired \nPlease try again", Toast.LENGTH_SHORT).show();

            }
        });

    }


}
