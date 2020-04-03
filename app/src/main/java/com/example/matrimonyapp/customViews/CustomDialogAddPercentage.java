package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

public class CustomDialogAddPercentage extends Dialog {



    public Context context;
    public EditText editText;

    private TextView textView_ok, textView_cancel;

    private com.shawnlin.numberpicker.NumberPicker numberPicker_per1, numberPicker_per2;

    public String percentage;

    UserModel userModel;

    public CustomDialogAddPercentage(Context context, EditText editText) {
        super(context);
        this.context = context;
        this.editText = editText;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_add_percentage);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        numberPicker_per1 = findViewById(R.id.numberPicker_per1);
        numberPicker_per2 = findViewById(R.id.numberPicker_per2);
        textView_ok = findViewById(R.id.textView_ok);
        textView_cancel = findViewById(R.id.textView_cancel);

        /*numberPicker_per1.setMinValue(35);
        numberPicker_per1.setMaxValue(99);

        numberPicker_per1.setValue(70);

        numberPicker_per2.setMinValue(0);
        numberPicker_per2.setMaxValue(99);
*/

        textView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                percentage = numberPicker_per1.getValue()+"."+numberPicker_per2.getValue()+" %";
                editText.setText(percentage);
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
