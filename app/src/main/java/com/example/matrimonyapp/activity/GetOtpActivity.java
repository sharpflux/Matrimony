package com.example.matrimonyapp.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetOtpActivity extends AppCompatActivity {

    EditText editText_mobileNo;
    TextView textView_getOtp, textView_signIn, textView_signUp;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    private String currentLanguage;
    String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        editText_mobileNo = findViewById(R.id.editText_mobileNo);
        textView_getOtp = findViewById(R.id.textView_getOtp);
        textView_signUp = findViewById(R.id.textView_signUp);
        textView_signIn = findViewById(R.id.textView_signIn);

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(GetOtpActivity.this);

        textView_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent(GetOtpActivity.this, VerifyOtpActivity.class);
                startActivity(intent);*/
               verifyMobileNo();
                /*AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("verifyMobileNo");*/

            }
        });

        textView_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                /*Intent intent = new Intent(GetOtpActivity.this, LoginActivity.class);
                startActivity(intent);*/

            }
        });

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(GetOtpActivity.this, SignUp.class);
                startActivity(intent);

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


    void verifyMobileNo()
    {


        mobileNo = editText_mobileNo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_VERIFYMOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (obj.getInt("UserId")>0) {

                                getOtp(obj.getString("UserId"));

                            }
                            else{

                                Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Something went wrong please try again",Toast.LENGTH_LONG).show();
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


    class AsyncTaskRunner extends AsyncTask<String, String, String>
    {

        @Override
        protected String doInBackground(String... params) {

            if(params[0].equals("verifyMobileNo"))
            {
                verifyMobileNo();
            }
            else if(params[0].equals(""))
            {

            }


            return "";
        }

        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            customDialogLoadingProgressBar.dismiss();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    void getOtp(final String userId)
    {


        mobileNo = "+91"+editText_mobileNo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_OTP+"MobileNo="+mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (!obj.getBoolean("error") && obj.getString("message").equals("Success")) {


                                Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                                intent.putExtra("OTP",obj.getString("OTP"));
                                intent.putExtra("parentActivity","GetOtp");
                                intent.putExtra("mobileNo",mobileNo);
                                intent.putExtra("userId",userId);
                                startActivity(intent);


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


}
