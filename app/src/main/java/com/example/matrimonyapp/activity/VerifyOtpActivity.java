package com.example.matrimonyapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

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

    private TextView textView_timeOut, textView_verifyOtp;
    private EditText editText_num1, editText_num2, editText_num3, editText_num4;
    private String currentLanguage;

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

        handler = new Handler();

/*        asynTaskTimer = new AsynTaskTimer();
        asynTaskTimer.execute();*/


        onNumClick(editText_num1, editText_num2);
        onNumClick(editText_num2, editText_num3);
        onNumClick(editText_num3, editText_num4);

        if(bundle!=null)
        {
            otpSent = bundle.getString("OTP");
            parentActivity = bundle.getString("parentActivity");
        }

        textView_verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //registerUser();
                otpEntered = editText_num1.getText().toString().trim()+editText_num2.getText().toString().trim()+editText_num3.getText().toString().trim()+editText_num4.getText().toString().trim();
                if(parentActivity.equals("GetOtp"))
                {
                    Intent intent = new Intent(VerifyOtpActivity.this, ForgotPassword.class);
                    intent.putExtra("userId",bundle.getString("userId"));
                    startActivity(intent);
                }
//
                if(otpSent.equals(otpEntered))
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


                else{
                    Toast.makeText(getApplicationContext(),"WRONG OTP!",Toast.LENGTH_SHORT).show();
                }


            }
        });


       // timeLimit();

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


                                    Toast.makeText(getApplicationContext(), "You have successfully registered!", Toast.LENGTH_SHORT).show();


                                        Intent intent = new Intent(VerifyOtpActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        //

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


    public  class AsynTaskTimer extends AsyncTask{

        @Override
        protected Object doInBackground(Object[] objects) {

            try{




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
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            finish();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Object o) {
            super.onCancelled(o);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }


    }


}
