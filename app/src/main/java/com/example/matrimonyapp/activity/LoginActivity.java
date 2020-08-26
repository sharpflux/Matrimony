package com.example.matrimonyapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText editText_mobileNo, editText_password;
    TextView textView_login, textView_signUp, textView_forgotPassword;

    SharedPreferences sharedPreferences;

    AlertDialog.Builder builder;

    String password, mobileNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        editText_mobileNo = findViewById(R.id.editText_mobileNo);
        editText_password = findViewById(R.id.editText_password);
        textView_login = findViewById(R.id.textView_login);
        textView_forgotPassword = findViewById(R.id.textView_forgotPassword);
        textView_signUp = findViewById(R.id.textView_signUp);


        if (CustomSharedPreference.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        }

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_signUp.setEnabled(false);
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);

            }
        });

        textView_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_forgotPassword.setEnabled(false);
                Intent intent = new Intent(LoginActivity.this, GetOtpActivity.class);
                startActivity(intent);
            }
        });


        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.setEnabled(false);
                view.setClickable(false);

                verifyLogin();
                /*Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);*/
                view.setEnabled(true);
                view.setClickable(true);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();

        textView_login.setEnabled(true);
        textView_signUp.setEnabled(true);
        textView_forgotPassword.setEnabled(true);
    }

    void verifyLogin()
    {

        mobileNo = editText_mobileNo.getText().toString().trim();
        password = editText_password.getText().toString().trim();

        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(false);

        //mobileNo = editText_mobileNo.getText().toString().trim();



        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_LOGIN+"MobileNo="+mobileNo+"&UserPassword="+password,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONArray jsonArray = new JSONArray(response);

                            if(jsonArray.length()==1) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {

                                    UserModel userModel = new UserModel();

                                    userModel.setUserId(jsonObject.getString("UserId"));
                                    userModel.setFullName(jsonObject.getString("FullName"));
                                    userModel.setAge(jsonObject.getString("Age"));
                                    userModel.setMobileNo(jsonObject.getString("MobileNo"));
                                    userModel.setEmailId(jsonObject.getString("EmailId"));
                                    userModel.setBirthdate(jsonObject.getString("DateOfBirth"));
                                    userModel.setGender(jsonObject.getString("Gender"));
                                    userModel.setProfilePic(jsonObject.getString("ProfileImage"));
                                    userModel.setLanguage(jsonObject.getString("LanguageType"));
                                            /* new UserModel(jsonObject.getString("UserId"),
                                            jsonObject.getString("FullName"),
                                            jsonObject.getString("MobileNo"),
                                            jsonObject.getString("EmailId"),
                                            jsonObject.getString("DateOfBirth"),
                                            jsonObject.getString("Age"),
                                            jsonObject.getString("Gender"),
                                            jsonObject.getString("ProfileImage"),
                                            jsonObject.getString("LanguageType")
                                    );

*/


                                    textView_login.setEnabled(false);
                                    CustomSharedPreference.getInstance(getApplicationContext()).saveUser(userModel);
                                    //CustomSharedPreference customSharedPreference = new CustomSharedPreference(getApplicationContext());
                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);


                                }
                            }
                             else{
                                textView_login.setEnabled(true);
                                Toast.makeText(getApplicationContext(),"Invalid User",Toast.LENGTH_SHORT).show();


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
/*                        builder.setMessage("Something went wrong please try again !")
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
                        textView_login.setEnabled(true);
                        Toast.makeText(getApplicationContext(),"Something went wrong please try again",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MobileNo", mobileNo);
                params.put("UserPassword", password);


                return params;


            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);


    }

}
