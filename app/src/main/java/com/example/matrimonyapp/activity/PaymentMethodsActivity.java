package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.matrimonyapp.R;

public class PaymentMethodsActivity extends Activity {

    EditText cvv_txt;
    TextView payment_txt;
    ImageView correct;
    String data;
    LinearLayout addCard_linear,tick_linear;
    boolean isSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_methods);

        addCard_linear= findViewById(R.id.addCard_linear);

        tick_linear = findViewById(R.id.tick_linear);
        tick_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isSelected == true){
                    correct.setImageResource(R.drawable.ic_verified_green);
                    isSelected = false;
                }else {
                    correct.setImageResource(R.drawable.ic_verified_gray);
                    isSelected = true;
                }
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        cvv_txt = (EditText) findViewById(R.id.cvv_txt);
        cvv_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvv_txt.setBackgroundResource(R.drawable.rectangle_pista);
            }
        });

        payment_txt = findViewById(R.id.payment_txt);
        cvv_txt.addTextChangedListener(change);

        correct = findViewById(R.id.correct);
    }

    private TextWatcher change = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String cvv_text = cvv_txt.getText().toString().trim();
            if (!cvv_text.isEmpty()) {
                if (cvv_txt.length() == 3) {
                    payment_txt.setEnabled(true);
                    payment_txt.setBackgroundColor(Color.parseColor("#60b243"));
                    correct.setImageResource(R.drawable.ic_verified_green);
                    cvv_txt.setBackgroundResource(R.drawable.rectangle_pista);

                } else {
                    payment_txt.setEnabled(true);
                    payment_txt.setBackgroundColor(Color.parseColor("#bdc0c6"));
                    correct.setImageResource(R.drawable.ic_verified_gray);
                    cvv_txt.setBackgroundResource(R.drawable.rectangle_gray_border);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}