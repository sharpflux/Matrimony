package com.example.matrimonyapp.validation;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class FieldValidation {

    static Context context;
    private static String radioButton_value;

    public FieldValidation() {
    }

    public FieldValidation(Context context) {
        this.context = context;
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

}
