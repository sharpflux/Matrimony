package com.example.matrimonyapp.validation;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class FieldValidation {

    static Context context;
    private static String radioButton_value;

    public FieldValidation() {
    }

    public FieldValidation(Context context) {
        this.context = context;
    }

    public static void validateRadioGroup(RadioGroup radioGroup)
    {

        for(int i=0; i<radioGroup.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
/*
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(((RadioButton)v).isChecked())
                    {
                        se
                    }
                }
            });*/
            if(rb.isChecked())
            {
                rb.setChecked(false);
            }
            else
            {
                rb.setChecked(true);
                radioButton_value = rb.getText().toString();
            }
        }
    }


    // returns value of checked button
    public static String radioGroupValidation(RadioGroup radioGroup)
    {
        //RadioButton rb1 = (RadioButton)((Activity)context).findViewById(radioGroup.getCheckedRadioButtonId());

        //radioButton_value = rb1.getText().toString();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                for(int i=0; i<radioGroup.getChildCount(); i++)
                {
                    RadioButton rb = (RadioButton) radioGroup.getChildAt(i);
                    if(rb.getId()!=id)
                    {
                        rb.setChecked(false);
                    }
                    else
                    {
                        rb.setChecked(true);
                        radioButton_value = rb.getText().toString();
                    }
                }



            }
        });
        return radioButton_value;
    }


    public static void setRadioButtonAccToValue(RadioGroup radioGroup, String value)
    {
        for(int i=0; i<radioGroup.getChildCount(); i++)
        {
            RadioButton rb = (RadioButton) radioGroup.getChildAt(i);

            if(value.equals(rb.getText().toString()))
            {
                rb.setChecked(true);
            }
            else
            {
                rb.setChecked(false);
            }

        }


    }


    public  static String onClickListenerForSDT( final String urlFor, final TextView textView_id, final Context ctx)
    {
        String id = "0";

        if (textView_id != null) {
            id = textView_id.getText().toString();

            if (id.equals("0")) {
                if (urlFor.contains("District")) {
                    Toast.makeText(ctx, "Select State first", Toast.LENGTH_SHORT).show();
                } else if (urlFor.contains("Taluka")) {
                    Toast.makeText(ctx, "Select District first", Toast.LENGTH_SHORT).show();
                }

            }

        }
        return id;
    }

    public static void textChangedListenerForSDT(final EditText editText_state, final EditText editText_district,
                                                 final EditText editText_taluka, TextView textView_stateId,
                                                 final TextView textView_districtId, final TextView textView_talukaId)
    {

        editText_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText_state.hasFocus()) {
                    editText_district.setText("");
                    textView_districtId.setText("0");
                    editText_taluka.setText("");
                    textView_talukaId.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText_district.hasFocus()) {
                    editText_taluka.setText("");
                    textView_talukaId.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }



}
