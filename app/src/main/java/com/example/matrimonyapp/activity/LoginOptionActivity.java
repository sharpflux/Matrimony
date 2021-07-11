package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.SliderAdapter;
import com.example.matrimonyapp.modal.sliderModel;
import com.example.matrimonyapp.volley.Register;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class LoginOptionActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout indicator;
    SliderAdapter sliderAdapter;
    ArrayList<sliderModel> sliderModelArrayList;
    Timer timer;
    Handler handler = new Handler();
    Button btnLogin,btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        sliderModelArrayList=new ArrayList<>();

        viewPager = findViewById(R.id.viewPager);
        indicator = findViewById(R.id.indicator);
        btnLogin=findViewById(R.id.btnLogin);
        btnReg=findViewById(R.id.btnReg);

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