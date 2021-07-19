package com.example.matrimonyapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.SliderAdapter;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.modal.sliderModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.Register;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//http://www.tutorialsface.com/2020/05/integrating-google-sign-in-into-android-app-sample-example-tutorial/

public class LoginOptionActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout indicator;
    SliderAdapter sliderAdapter;
    ArrayList<sliderModel> sliderModelArrayList;
    Timer timer;
    Handler handler = new Handler();
    Button btnLogin,btnReg;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;



    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;
    private ProgressDialog mProgressDialog;

    LinearLayout linearGoogleLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        sliderModelArrayList=new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        btnLogin=findViewById(R.id.btnLogin);
        btnReg=findViewById(R.id.btnReg);
        linearGoogleLogin=findViewById(R.id.linearGoogleLogin);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        linearGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginOptionActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        setDynamicslider();
        autoScroll();
    }
    private void signIn() {
        //  Toast.makeText(this,"signIn method called",Toast.LENGTH_SHORT).show();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //  Toast.makeText(this,"OnActivityResultCalled"+requestCode,Toast.LENGTH_SHORT).show();
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }
    private void handleSignInResult( Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String personName = account.getDisplayName();
            String personPhotoUrl = account.getPhotoUrl().toString();
            String email = account.getEmail();



            UserModel userModel = new UserModel();
            userModel.setUserId( account.getId());
            userModel.setFullName(account.getDisplayName());
            userModel.setAge("");
            userModel.setMobileNo("");
            userModel.setEmailId(account.getEmail());
            userModel.setBirthdate("");
            userModel.setGender("");
            userModel.setProfilePic( account.getPhotoUrl().toString());
            CustomSharedPreference.getInstance(getApplicationContext()).saveUser(userModel);

            Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
            startActivity(intent);

            //.updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode()+"\n"+e.getLocalizedMessage()+"\nMEssg: "+e.getMessage());
            updateUI(null);
        }
    }
    private void updateUI(GoogleSignInAccount account) {

        if(account==null)
        {
            Toast.makeText(this, "not signed in", Toast.LENGTH_SHORT).show();
           // signInButton.setVisibility(View.VISIBLE);
           // btnSignOut.setVisibility(View.GONE);
           // imgProfilePic.setVisibility(View.GONE);
         //   txtEmail.setVisibility(View.GONE);
           // txtName.setVisibility(View.GONE);
            Toast.makeText(this,"please sign in",Toast.LENGTH_SHORT).show();
        }
        else {
           // signInButton.setVisibility(View.GONE);
         //   imgProfilePic.setVisibility(View.VISIBLE);
         //   txtEmail.setVisibility(View.VISIBLE);
          //  txtName.setVisibility(View.VISIBLE);
           // btnSignOut.setVisibility(View.VISIBLE);
            Toast.makeText(this,"you are signed in",Toast.LENGTH_SHORT).show();
        }

    }
    private void signOut() {
        if(mGoogleSignInClient != null) {
            mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    startActivity(new Intent(LoginOptionActivity.this,LoginActivity.class));
                    finish();
                }
            });
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }
    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // [START_EXCLUDE]
                        updateUI(null);
                        // [END_EXCLUDE]
                    }
                });
    }

    private void setDynamicslider() { StringRequest stringRequest = new StringRequest(Request.Method.GET,
            URLs.URL_SLIDER_IMG,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {

                        JSONArray obj = new JSONArray(response);
                        for (int i = 0; i < obj.length(); i++)
                        {
                            //
                            JSONObject userJson = obj.getJSONObject(i);
                            if (!userJson.getBoolean("error")) {
                                sliderModel sellOptions=
                                        new sliderModel
                                                (URLs.MainURL+userJson.getString("SliderImgUrl"),
                                                        userJson.getInt("SliderImgId")
                                                );

                                sliderModelArrayList.add(sellOptions);
                            } else {
                                Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            }

                            sliderAdapter = new SliderAdapter(getApplicationContext(), sliderModelArrayList);
                            viewPager.setAdapter(sliderAdapter);
                            indicator.setupWithViewPager(viewPager, true);



                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {

                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                            // mShimmerViewContainer.stopShimmerAnimation();
                            //mShimmerViewContainer.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //mShimmerViewContainer.stopShimmerAnimation();
                        ///mShimmerViewContainer.setVisibility(View.GONE);
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    // mShimmerViewContainer.stopShimmerAnimation();
                    // mShimmerViewContainer.setVisibility(View.GONE);
                }
            }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<>();

            return params;
        }
    };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
    }
    private void autoScroll() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (viewPager.getCurrentItem() < sliderModelArrayList.size() - 1) {
                    viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                } else {
                    viewPager.setCurrentItem(0);
                }
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(runnable);
            }
        }, 4000, 6000);

    }

}