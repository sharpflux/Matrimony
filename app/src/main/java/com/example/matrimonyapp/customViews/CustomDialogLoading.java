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

public class CustomDialogLoading extends Dialog {



    public Context context;

    UserModel userModel;

    public CustomDialogLoading(Context context) {
        super(context);
        this.context = context;


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_add_percentage);

//        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
//            context.startActivity(new Intent(context, LoginActivity.class));
//        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));








    }


}
