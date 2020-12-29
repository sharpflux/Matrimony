package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.matrimonyapp.R;

import java.util.regex.Pattern;

public class AddNewCardActivity extends Activity {
    ImageView tick_img;
    boolean isSelected = true;
    static final Pattern CODE_PATTERN = Pattern.compile("([0-9]{0,4})|([0-9]{4}-)+|([0-9]{4}-[0-9]{0,4})+");
    LinearLayout tick_linear;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_card);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editText = findViewById(R.id.cardNumberEditText);

        tick_img = findViewById(R.id.tick_img);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                Log.w("", "input" + s.toString());

                if (s.length() > 0 && !CODE_PATTERN.matcher(s).matches()) {
                    String input = s.toString();
                    String numbersOnly = keepNumbersOnly(input);
                    String code = formatNumbersAsCode(numbersOnly);

                    Log.w("", "numbersOnly" + numbersOnly);
                    Log.w("", "code" + code);

                    editText.removeTextChangedListener(this);
                    editText.setText(code);
                    // You could also remember the previous position of the cursor
                    editText.setSelection(code.length());
                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            private String keepNumbersOnly(CharSequence s) {
                return s.toString().replaceAll("[^0-9]", ""); // Should of course be more robust
            }

            private String formatNumbersAsCode(CharSequence s) {
                int groupDigits = 0;
                String tmp = "";
                for (int i = 0; i < s.length(); ++i) {
                    tmp += s.charAt(i);
                    ++groupDigits;
                    if (groupDigits == 4) {
                        tmp += "-";
                        groupDigits = 0;
                    }
                }
                return tmp;
            }
        });
        tick_linear = findViewById(R.id.tick_linear);
        tick_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isSelected = true) {
                    tick_img.setImageResource(R.drawable.ic_verified_green);
                    isSelected = false;
                } else {
                    tick_img.setImageResource(R.drawable.ic_verified_gray);
                    isSelected = true;
                }
            }
        });
    }
}