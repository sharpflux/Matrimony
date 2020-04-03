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

public class ForgotPassword extends AppCompatActivity {

    Bundle bundle;

    private EditText editText_password, editText_confirmPassword;

    private TextView textView_updatePassword, textView_backToSignIn, textView_signUp;
    private String userId, password, confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        editText_password = findViewById(R.id.editText_newPassword);
        editText_confirmPassword = findViewById(R.id.editText_confirmPassword);
        textView_updatePassword = findViewById(R.id.textView_updatePassword);
        textView_backToSignIn = findViewById(R.id.textView_backToSignIn);
        textView_signUp = findViewById(R.id.textView_signUp);

        bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            userId = bundle.getString("userId");
        }


        textView_updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                password = editText_password.getText().toString().trim();
                confirmPassword = editText_confirmPassword.getText().toString().trim();

                if(password.equals(confirmPassword)) {
                    updatePassword();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Password and Confirm Password fields don't match!",Toast.LENGTH_SHORT).show();
                    editText_confirmPassword.setText("");
                    editText_password.setText("");
                    editText_password.requestFocus();
                }
            }
        });

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, SignUp.class);
                startActivity(intent);
            }
        });

        textView_backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

    }

    public void updatePassword()
    {

        password = editText_password.getText().toString();



        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_UPDATEPASSWORD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject jsonObject = new JSONObject(response);


                            if(jsonObject.getString("message").equals("Success") && !jsonObject.getBoolean("error")) {

                                Toast.makeText(getApplicationContext(),"Password Updated Successfully!",Toast.LENGTH_LONG).show();
                                //CustomSharedPreference customSharedPreference = new CustomSharedPreference(getApplicationContext());
                                Intent intent = new Intent(ForgotPassword.this, LoginActivity.class);
                                startActivity(intent);

                            }
                            else{
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

                        Toast.makeText(getApplicationContext(),"Something went wrong please try again",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId", userId);
                params.put("Passwords", password);
                return params;


            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);





    }

}
