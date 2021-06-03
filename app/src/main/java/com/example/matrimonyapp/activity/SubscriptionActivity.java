package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

/*import com.braintreepayments.api.BraintreeFragment;*/
/*import com.braintreepayments.api.GooglePayment;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.interfaces.BraintreeResponseListener;*/
//import com.braintreepayments.api.models.GooglePaymentRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ImageSliderAdapter;
import com.example.matrimonyapp.adapter.SubscriptionAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.fragment.AboutEditProfileFragment;
import com.example.matrimonyapp.modal.SubscriptionModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.PayPalClientIdConfiguration;
//import com.google.android.gms.wallet.TransactionInfo;
//import com.google.android.gms.wallet.WalletConstants;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.razorpay.Checkout;

import com.razorpay.Order;
import com.razorpay.Payment;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultListener;
import com.razorpay.PaymentResultWithDataListener;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
/*
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
*/

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionActivity extends Activity implements PaymentResultWithDataListener {

    View view_toolbar;

    private TextView textView_title;

    private ArrayList<SubscriptionModel> arrayList_subscriptionModel;
    private ViewPager2 viewPager2_subscription;
    private TabLayout tabLayout_subscription;
    RazorpayClient razorpayClient;
    private String TAG = "Payment Error";
    SubscriptionModel subscriptionModel;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    private Handler mHandler;
    private UserModel userModel;
    SubscriptionAdapter subscriptionAdapter;
    TextView textView_toolbar;
    String SelectedPackageId;
    AlertDialog.Builder builder;
    JSONObject orderRequest;
    public static final String TRACER = "tracer";
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        mHandler = new Handler();
        Checkout.preload(SubscriptionActivity.this);
        SelectedPackageId = "1";

        userModel = CustomSharedPreference.getInstance(this).getUser();
        builder = new AlertDialog.Builder(this);

        textView_toolbar = findViewById(R.id.textView_toolbar);

        textView_toolbar.setText("Packages");
        viewPager2_subscription = findViewById(R.id.viewPager2_subscription);
        tabLayout_subscription = findViewById(R.id.tabLayout_subscription);
        view_toolbar = findViewById(R.id.toolbar);

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(SubscriptionActivity.this);
        customDialogLoadingProgressBar.setCancelable(false);
//        findViewById(R.id.imageView_menu).setVisibility(View.GONE);


        arrayList_subscriptionModel = new ArrayList<SubscriptionModel>();

        subscriptionAdapter = new SubscriptionAdapter(arrayList_subscriptionModel, viewPager2_subscription, new SubscriptionAdapter.SelectPlanClickListener() {
            @Override
            public void onPlanSelect(SubscriptionModel data) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                SelectedPackageId = data.getPackageId();

                runner.execute("Payment", data.getAmount(), data.getPackageId().toString());
            }
        });


        viewPager2_subscription.setAdapter(subscriptionAdapter);

        viewPager2_settings();

        //tabLayout_subscription.setupWithViewPager(viewPager2_subscription, true);
        new TabLayoutMediator(tabLayout_subscription, viewPager2_subscription, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getPackages");


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void orderAPI(final String amount, final String packageId) {
        orderRequest = new JSONObject();
        try {
            razorpayClient = new RazorpayClient("rzp_live_CMT7eyRNO7UCnQ", "Vm9BZntrswJr9jZeChmPb6nr");
            double Paise = Double.parseDouble("1");
            orderRequest.put("amount", Paise * 100);
            orderRequest.put("currency", "INR");
            orderRequest.put("receipt", userModel.getUserId());
            Order order = razorpayClient.Orders.create(orderRequest);
            orderRequest.put("order_id", order.toJson().getString("id"));
            Checkout checkout = new Checkout();
            JSONObject options = new JSONObject();
            options.put("name", "Jodidar Maza");
            options.put("description", "App Charges");
            options.put("image", "https://www.sharpflux.com/HolarLogo.png");
            options.put("order_id", order.toJson().getString("id"));//from response of step 3.
            options.put("currency", "INR");
            options.put("theme.color", "#DD137B");
            //  options.put("theme.color", getResources().getColor(R.color.black));

            JSONObject preFill = new JSONObject();
            preFill.put("email", userModel.getEmailId());
            preFill.put("contact", userModel.getMobileNo());
            options.put("prefill", preFill);
            checkout.setKeyID("rzp_live_CMT7eyRNO7UCnQ");
            checkout.open(SubscriptionActivity.this, options);

        } catch (JSONException | RazorpayException e) {
            e.printStackTrace();
        } catch (Exception exception) {
            exception.printStackTrace();
            Log.e("ERROR", "\n\n--------------------------------------\n"
                    + exception.getMessage() + "\n--------------------------------------------\n\n");
        }
    }


    private class AsyncTaskRunner extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(final String... params) {

            if (params[0].equals("getPackages")) {

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        Thread t = new Thread(new Runnable() {

                            @Override
                            public void run() {
                                getDetails();
                            }
                        });
                        t.start();

                      /*  new Thread(new Runnable() {
                            public void run(){
                                getDetails();
                            }
                        }).start();*/

                    }
                });


            }

            if (params[0].equals("Payment")) {
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try  {
                            orderAPI(params[1].toString(), params[2].toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                t.start();

            }

            if (params[0].equals("Membership")) {
                Thread t = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try  {
                            saveMembership(params[1].toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                });
                t.start();

            }
            return params[0];
        }

        @Override
        protected void onPreExecute() {
            customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s) {

        }
    }


    void saveMembership(final String TransactionId) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_POST_MEMBERSHIP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONObject jsonArray = new JSONObject(response);
                            builder.setMessage("Thanks for the payment !")
                                    .setCancelable(false)
                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //  Action for 'NO' Button
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert = builder.create();
                            alert.setTitle("Payment Received");
                            alert.show();


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubscriptionActivity.this, "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("MembershipId", "0");
                params.put("UserId", userModel.getUserId());
                params.put("PackageId", SelectedPackageId);
                params.put("TransactionId", TransactionId);
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SubscriptionActivity.this).addToRequestQueue(stringRequest);
    }

    void getDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_PACKAGES,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject userJson = jsonArray.getJSONObject(i);
                                    subscriptionModel = new SubscriptionModel();
                                    subscriptionModel.setPackageId(String.valueOf(userJson.getInt("PackageId")));
                                    subscriptionModel.setPackName(userJson.getString("PackageName"));
                                    subscriptionModel.setAmount(String.valueOf(userJson.getDouble("Amount")));
                                    arrayList_subscriptionModel.add(subscriptionModel);

                                }
                                subscriptionAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(SubscriptionActivity.this, "Please enter your details! ", Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SubscriptionActivity.this, "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SubscriptionActivity.this).addToRequestQueue(stringRequest);
    }



    public void viewPager2_settings() {
        viewPager2_subscription.setCurrentItem(1);

        viewPager2_subscription.setClipToPadding(false);
        viewPager2_subscription.setClipChildren(false);
        viewPager2_subscription.setOffscreenPageLimit(6);
        viewPager2_subscription.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });


        viewPager2_subscription.setPageTransformer(compositePageTransformer);

        /*viewPager2_singleImage.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,3000);
            }
        });*/

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        //Toast.makeText(SubscriptionActivity.this, s, Toast.LENGTH_SHORT).show();


        try {
            customDialogLoadingProgressBar.dismiss();
            saveMembership(paymentData.getPaymentId());
            Payment payment = razorpayClient.Payments.capture(paymentData.getPaymentId(), orderRequest);

        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        customDialogLoadingProgressBar.dismiss();

        Toast.makeText(SubscriptionActivity.this, "ON RAZORPAY ERROR - " + s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

        Toast.makeText(SubscriptionActivity.this, "CAPTURED - " + hasCapture, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
       // notify("onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
       // notify("onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
     //   notify("onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // notify("onDestroy");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        String PackageId = savedInstanceState.getString("PackageId");
        SelectedPackageId=PackageId;
        //notify("onRestoreInstanceState");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("PackageId",SelectedPackageId);
        //notify("onSaveInstanceState");
    }
    private void notify(String methodName) {
        String name = this.getClass().getName();
        String[] strings = name.split("\\.");
        Notification.Builder notificationBuilder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder = new Notification.Builder(this, TRACER);
        } else {
            //noinspection deprecation
            notificationBuilder = new Notification.Builder(this);
        }

        Notification notification = notificationBuilder
                .setContentTitle(methodName + " " + strings[strings.length - 1])
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.icon1)
                .setContentText(name).build();
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), notification);
    }



}
