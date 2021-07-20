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
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;

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

        //blurBackground();

        setContentView(R.layout.custom_dialog_add_percentage);

//        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
//            context.startActivity(new Intent(context, LoginActivity.class));
//        }

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

                percentage = numberPicker_per1.getValue()+"."+String.format("%02d",numberPicker_per2.getValue())+" %";
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

    private void blurBackground()
    {

       /* //take Screenshot
        Bitmap bitmap = Screenshot.getInstance().takeScreenshotForScreen((Activity)context);

        //set blurring factor and heighth width of screenshot
        BlurFactor blurFactor = new BlurFactor();
        blurFactor.height = bitmap.getHeight();
        blurFactor.width = bitmap.getWidth();
        blurFactor.color = context.getResources().getColor(R.color.transparent_bg);

        //blurred image
        Bitmap blurBitmap = Blur.of(context, bitmap, blurFactor);
        //convert blurred image into drawable
        Drawable drawable = new BitmapDrawable(context.getResources(), blurBitmap);

        //set blurred screenshot to background
        getWindow().setBackgroundDrawable(drawable);
*/


    }


}
