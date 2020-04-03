package com.example.matrimonyapp.activity;

import android.content.Intent;
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
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GetOtpActivity extends AppCompatActivity {

    EditText editText_mobileNo;
    TextView textView_getOtp, textView_signIn, textView_signUp;

    String mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_otp);

        editText_mobileNo = findViewById(R.id.editText_mobileNo);
        textView_getOtp = findViewById(R.id.textView_getOtp);
        textView_signUp = findViewById(R.id.textView_signUp);
        textView_signIn = findViewById(R.id.textView_signIn);

        textView_getOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               /* Intent intent = new Intent(GetOtpActivity.this, VerifyOtpActivity.class);
                startActivity(intent);*/
               verifyMobileNo();


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
