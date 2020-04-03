package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

public class CustomDialogLoadingProgressBar extends Dialog {


    public Context context;

    UserModel userModel;

    public CustomDialogLoadingProgressBar(Context context) {
        super(context);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.custom_dialog_loading_progress_bar);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        setCanceledOnTouchOutside(true);


//        BlurBackground blurBackground = new BlurBackground(context, 10);

        //Bitmap map = blurBackground.takeScreenShot((Activity)context);

//        blurBackground.blur();
        //Bitmap fast = blurBackground.fastblur(map, 10);

//        final Drawable draw=new BitmapDrawable(context.getResources(),blurBackground.sentBitmap);

//        getWindow().setBackgroundDrawable(draw);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


    }

}
