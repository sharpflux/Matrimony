package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;

public class CustomDialogChangeProfilePic extends Dialog {


    public Context context;

    public TextView textView_title, textView_addFromGallery, textView_addFromCamera;


    public CustomDialogChangeProfilePic(Context context) {
        super(context);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_change_profile_pic);



        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textView_title = findViewById(R.id.textView_title);
        textView_addFromGallery = findViewById(R.id.textView_addFromGallery);
        textView_addFromCamera = findViewById(R.id.textView_addFromCamera);



        onClickListener();

       /* Shader shader= new LinearGradient(0f,0f,0f, textView_title.getTextSize(),Color.RED,context.getResources(context.getColor(R.color.projectColor)), Shader.TileMode.CLAMP);;

        textView_title.getPaint().setShader(shader);
*/
    }

    private void onClickListener() {




    }



}
