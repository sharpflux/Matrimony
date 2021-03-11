package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import java.util.ArrayList;
import java.util.Calendar;

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;

public class CustomDialogAddYear extends Dialog {


    public Context context;

    public EditText editText;

    private TextView textView_ok, textView_cancel;

    private com.shawnlin.numberpicker.NumberPicker numberPicker_year;

    public String year;

    String values [];

    UserModel userModel;

    public CustomDialogAddYear(Context context, EditText editText) {
        super(context);
        this.context = context;
        this.editText = editText;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //blurBackground();

        setContentView(R.layout.custom_dialog_add_year);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        numberPicker_year = findViewById(R.id.numberPicker_year);
        textView_ok = findViewById(R.id.textView_ok);
        textView_cancel = findViewById(R.id.textView_cancel);



        int nowYear=0;
        ArrayList<String> arrayList_string = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();


        for(int i=1950, j=0; i<calendar.get(Calendar.YEAR)+5; i++, j++)
        {
            arrayList_string.add(String.valueOf(i));
            if(i==calendar.get(Calendar.YEAR))
            {
                nowYear = j;

            }

        }

        values = new String[arrayList_string.size()];

        for(int i=0; i<arrayList_string.size(); i++)
        {
            values[i] = arrayList_string.get(i);
        }

        numberPicker_year.setMinValue(0);
        numberPicker_year.setMaxValue(values.length-1);
        numberPicker_year.setDisplayedValues(values);
        numberPicker_year.setValue(nowYear);

        textView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year = values[numberPicker_year.getValue()];
                editText.setText(year);
                dismiss();
            }
        });

        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dismiss();

            }
        });

    }



}
