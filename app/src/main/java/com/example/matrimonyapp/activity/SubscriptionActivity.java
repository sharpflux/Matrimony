package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ImageSliderAdapter;
import com.example.matrimonyapp.adapter.SubscriptionAdapter;
import com.example.matrimonyapp.modal.SubscriptionModel;
import com.example.matrimonyapp.utils.PayPalClientIdConfiguration;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;

import java.math.BigDecimal;
import java.util.ArrayList;

public class SubscriptionActivity extends Activity {

    View view_toolbar;

    private TextView textView_title;

    private ArrayList<SubscriptionModel> arrayList_subscriptionModel;
    private ViewPager2 viewPager2_subscription;
    private TabLayout tabLayout_subscription;
    private int PAYPAL_REQUEST_CODE = 10;
    private static PayPalConfiguration payPalConfiguration = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalClientIdConfiguration.CLIENT_ID);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

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

                paypalPaymentMethod( data.getAmount());
            }
        }));
        viewPager2_settings();

        //tabLayout_subscription.setupWithViewPager(viewPager2_subscription, true);
        new TabLayoutMediator(tabLayout_subscription, viewPager2_subscription, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        }).attach();


        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(intent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PAYPAL_REQUEST_CODE)
        {
            if (requestCode == Activity.RESULT_OK)
            {
                Toast.makeText(this, "Payment made Successfully!!!",Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Sorry!!!\n Please try again...",Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    public void paypalPaymentMethod(String amount)
    {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(Integer.parseInt(amount)),"USD",
                "Plan Activation", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(SubscriptionActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
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

}
