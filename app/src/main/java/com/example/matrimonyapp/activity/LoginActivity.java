package com.example.matrimonyapp.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.example.matrimonyapp.customViews.CustomDialogChangeLanguage;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    String password, mobileNo;
    private String currentLanguage;

    CustomDialogChangeLanguage customDialogChangeLanguage;

    private FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private Bundle bundle;


    @Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();



        editText_mobileNo = findViewById(R.id.editText_mobileNo);
        editText_password = findViewById(R.id.editText_password);
        textView_login = findViewById(R.id.textView_login);
        textView_forgotPassword = findViewById(R.id.textView_forgotPassword);
        textView_signUp = findViewById(R.id.textView_signUp);



        if (CustomSharedPreference.getInstance(this).isLoggedIn()) {
            finish();
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            finish();
        }

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_signUp.setEnabled(false);
                Intent intent = new Intent(LoginActivity.this, SignUp.class);
                startActivity(intent);
                finish();

            }
        });

        textView_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_forgotPassword.setEnabled(false);
                Intent intent = new Intent(LoginActivity.this, GetOtpActivity.class);
                startActivity(intent);
                finish();
            }
        });


        textView_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                view.setEnabled(false);
                view.setClickable(false);

                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("login");
                /*Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);*/
                view.setEnabled(true);
                view.setClickable(true);
            }
        });



        customDialogChangeLanguage = new CustomDialogChangeLanguage(LoginActivity.this);

        bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            if (bundle.getString("ActivityState").equals("started"))
            {
                customDialogChangeLanguage.show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(customDialogChangeLanguage.isShowing())
        {
            customDialogChangeLanguage.dismiss();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

/*        textView_login.setEnabled(true);
        textView_signUp.setEnabled(true);
        textView_forgotPassword.setEnabled(true);*/

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

    private void firebaseLogin(String emailId, String password)
    {
        firebaseAuth = FirebaseAuth.getInstance();

        firebaseAuth.signInWithEmailAndPassword(emailId, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {


                }

            }
        });
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
/*                                    registerFirebase(jsonObject.getString("FullName"),
                                            jsonObject.getString("EmailId"), password,
                                            jsonObject.getString("UserId"));*/
                                    UserModel userModel = new UserModel();

                                    userModel.setUserId(jsonObject.getString("UserId"));
                                    userModel.setFullName(jsonObject.getString("FullName"));
                                    userModel.setAge(jsonObject.getString("Age"));
                                    userModel.setMobileNo(jsonObject.getString("MobileNo"));
                                    userModel.setEmailId(jsonObject.getString("EmailId"));
                                    userModel.setBirthdate(jsonObject.getString("DateOfBirth"));
                                    userModel.setGender(jsonObject.getString("Gender"));
                                    userModel.setProfilePic(jsonObject.getString("ProfileImage"));


                                    if(bundle.containsKey("locale"))
                                    {
                                        userModel.setLanguage(bundle.getString("locale"));
                                    }
                                    else {
                                        userModel.setLanguage("en");
                                    }


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
                                    //firebaseLogin(jsonObject.getString("EmailId"), "123456");



                                    textView_login.setEnabled(false);
                                    CustomSharedPreference.getInstance(getApplicationContext()).saveUser(userModel);
                                    //CustomSharedPreference customSharedPreference = new CustomSharedPreference(getApplicationContext());

                                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
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


    private void registerFirebase(final String username, String email, String password, final String userId)
    {
        firebaseAuth = FirebaseAuth.getInstance();
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
                            hashMap.put("lastMessage", "Hi, "+username+" here...");
                            hashMap.put("lastMessageTime", "10.00pm");
                            hashMap.put("profilePic","default");

                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                       // Toast.makeText(LoginActivity.this,"Firebase Successful",Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(LoginActivity.this,"UnSuccessful Firebase",Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });

                        }
                    }
                });

    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String>
    {




        @Override
        protected String doInBackground(String... params) {


            if(params[0].equals("login"))
            {
                verifyLogin();
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(LoginActivity.this);
            customDialogLoadingProgressBar.show();
        }

        public AsyncTaskRunner() {
            super();
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

}
