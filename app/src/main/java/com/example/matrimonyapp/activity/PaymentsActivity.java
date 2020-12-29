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

public class PaymentsActivity extends Activity implements View.OnClickListener {

    EditText cvv_txt;
    TextView payment_txt;
    ImageView correct, correct_tick;
    LinearLayout click_linear, linear_cash, addNewCard_linear;

    String message;
    TextView cardNo_Txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*---------fetch card number  the data previous activity----------*/
        cardNo_Txt = findViewById(R.id.cardNo_Txt);
        Intent intent = getIntent();
        message = intent.getStringExtra("cardno");
        cardNo_Txt.setText(message);

        correct = findViewById(R.id.correct);
        correct_tick = findViewById(R.id.correct_tick);
        click_linear = findViewById(R.id.click_linear);
        linear_cash = findViewById(R.id.linear_cash);
        addNewCard_linear = findViewById(R.id.addNewCard_linear);

//        addNewCard_linear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getBaseContext(), AddNewCardActivity.class);
//                startActivity(i);
//            }
//        });

        click_linear.setOnClickListener(this);
        linear_cash.setOnClickListener(this);

        cvv_txt = (EditText) findViewById(R.id.cvv_txt);
        cvv_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cvv_txt.setBackgroundResource(R.drawable.rectangle_pista);
            }
        });

        payment_txt = findViewById(R.id.payment_txt);

        cvv_txt.addTextChangedListener(change);

        cardNo_Txt.setHint("1234-5678-9010-1234");
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
                    cvv_txt.setBackgroundResource(R.drawable.rectangle_pista);
                } else {
                    payment_txt.setEnabled(true);
                    payment_txt.setBackgroundColor(Color.parseColor("#bdc0c6"));
                    cvv_txt.setBackgroundResource(R.drawable.rectangle_gray_border);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.click_linear:
                correct.setImageResource(R.drawable.ic_verified_green);
                correct_tick.setImageResource(R.drawable.ic_verified_gray);
                break;

            case R.id.linear_cash:
                correct.setImageResource(R.drawable.ic_verified_gray);
                correct_tick.setImageResource(R.drawable.ic_verified_green);
                payment_txt.setBackgroundColor(Color.parseColor("#bdc0c6"));
                cvv_txt.setBackgroundResource(R.drawable.rectangle_gray_border);
                cvv_txt.setText("");
                break;
        }
    }
}