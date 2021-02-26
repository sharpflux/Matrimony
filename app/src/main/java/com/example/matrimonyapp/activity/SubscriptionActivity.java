package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ImageSliderAdapter;
import com.example.matrimonyapp.adapter.SubscriptionAdapter;
import com.example.matrimonyapp.modal.SubscriptionModel;
import com.example.matrimonyapp.utils.PayPalClientIdConfiguration;
//import com.google.android.gms.wallet.TransactionInfo;
//import com.google.android.gms.wallet.WalletConstants;
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

public class SubscriptionActivity extends Activity implements PaymentResultWithDataListener {

    View view_toolbar;

    private TextView textView_title;

    private ArrayList<SubscriptionModel> arrayList_subscriptionModel;
    private ViewPager2 viewPager2_subscription;
    private TabLayout tabLayout_subscription;
    RazorpayClient razorpayClient;
    private String TAG = "Payment Error";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        Checkout.preload(SubscriptionActivity.this);

        viewPager2_subscription = findViewById(R.id.viewPager2_subscription);
        tabLayout_subscription = findViewById(R.id.tabLayout_subscription);
        view_toolbar = findViewById(R.id.toolbar);


        findViewById(R.id.imageView_menu).setVisibility(View.GONE);


        arrayList_subscriptionModel = new ArrayList<SubscriptionModel>();

        SubscriptionModel subscriptionModel = new SubscriptionModel();
        subscriptionModel.setPackName("Weekly Plan");
        subscriptionModel.setAmount("1");
        arrayList_subscriptionModel.add(subscriptionModel);

        subscriptionModel = new SubscriptionModel();
        subscriptionModel.setPackName("Monthly Plan");
        subscriptionModel.setAmount("2");
        arrayList_subscriptionModel.add(subscriptionModel);

        subscriptionModel = new SubscriptionModel();
        subscriptionModel.setPackName("Yearly Plan");
        subscriptionModel.setAmount("3");
        arrayList_subscriptionModel.add(subscriptionModel);


        viewPager2_subscription.setAdapter(new SubscriptionAdapter(arrayList_subscriptionModel, viewPager2_subscription,
                new SubscriptionAdapter.SelectPlanClickListener() {
            @Override
            public void onPlanSelect(SubscriptionModel data) {
                orderAPI();
                //startPayment(data.getAmount());

            }
        }));
        viewPager2_settings();

        //tabLayout_subscription.setupWithViewPager(viewPager2_subscription, true);
        new TabLayoutMediator(tabLayout_subscription, viewPager2_subscription, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);




    }

    public void orderAPI() {


        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                //Your code goes here




                JSONObject orderRequest = new JSONObject();
                try {
                    razorpayClient = new RazorpayClient("rzp_live_WyOXzpKM7aU75s", "ytHyLATItR2CwNXMWTgwMjy8");
                    orderRequest.put("amount", 100);
                    orderRequest.put("currency", "INR");
                    orderRequest.put("receipt", "ORDER_2");
                    /*JSONObject payment = new JSONObject();
                    payment.put("capture", "automatic");

                    JSONObject captureOptions = new JSONObject();
                    captureOptions.put("automatic_expiry_period", 12);
                    captureOptions.put("manual_expiry_period", 7200);
                    captureOptions.put("refund_speed", "optimum");
                    payment.put("capture_options", captureOptions);
                    orderRequest.put("payment", payment);*/
                    Order order = razorpayClient.Orders.create(orderRequest);
                    orderRequest.put("order_id", order.toJson().getString("id"));
                    Checkout checkout = new Checkout();
                  //  checkout.setImage(R.drawable.icon1);

                    checkout.setKeyID("rzp_live_WyOXzpKM7aU75s");
                    checkout.open(SubscriptionActivity.this,orderRequest);


                } catch (JSONException | RazorpayException e) {
                    e.printStackTrace();
                } catch (Exception exception) {
                    exception.printStackTrace();
                    Log.e("ERROR", "\n\n--------------------------------------\n"
                            + exception.getMessage() + "\n--------------------------------------------\n\n");
                }
            }
        });

        thread.start();


    }





    @Override
    protected void onDestroy() {

        super.onDestroy();
    }


    public void viewPager2_settings()
    {
        viewPager2_subscription.setCurrentItem(1);

        viewPager2_subscription.setClipToPadding(false);
        viewPager2_subscription.setClipChildren(false);
        viewPager2_subscription.setOffscreenPageLimit(3);
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
        });
*/

    }



    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        //Toast.makeText(SubscriptionActivity.this, s, Toast.LENGTH_SHORT).show();


        try {
            JSONObject captureRequest = new JSONObject();
            captureRequest.put("amount",100);
            captureRequest.put("currency","INR");

            Payment payment = razorpayClient.Payments.capture( paymentData.getPaymentId(),captureRequest);

        } catch (JSONException | RazorpayException e) {
            e.printStackTrace();
        }

        //

    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        Toast.makeText(SubscriptionActivity.this, "ON RAZORPAY ERROR - "+s,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

        Toast.makeText(SubscriptionActivity.this, "CAPTURED - "+hasCapture,Toast.LENGTH_SHORT).show();
    }
}
