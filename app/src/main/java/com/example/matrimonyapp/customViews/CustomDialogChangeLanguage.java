package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.abdallahalaraby.blink.Screenshot;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.LanguageAdapter;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import java.util.ArrayList;
import java.util.Locale;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

public class CustomDialogChangeLanguage extends Dialog {

    public Context context;
    private ArrayList<String> arrayList_languages;
    public TextView textView_english, textView_marathi, textView_hindi;
    private RecyclerView recyclerView_language;
    private LanguageAdapter languageAdapter;



    public CustomDialogChangeLanguage(Context context) {
        super(context);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

 /*       //take Screenshot
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

        setContentView(R.layout.custom_dialog_change_language);



        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

/*        textView_english = findViewById(R.id.textView_english);
        textView_marathi = findViewById(R.id.textView_marathi);
        textView_hindi = findViewById(R.id.textView_hindi);*/
        recyclerView_language = findViewById(R.id.recyclerView_language);

        arrayList_languages = new ArrayList<>();
        arrayList_languages.add(context.getResources().getString(R.string.english));
        arrayList_languages.add(context.getResources().getString(R.string.marathi));
        arrayList_languages.add(context.getResources().getString(R.string.hindi));

        recyclerView_language = findViewById(R.id.recyclerView_language);
        languageAdapter = new LanguageAdapter(context, arrayList_languages, this);

        recyclerView_language.setAdapter(languageAdapter);
        recyclerView_language.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_language.setLayoutManager(mLayoutManager);





        /*textView_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "English", Toast.LENGTH_SHORT).show();
            }
        });*/





    }






}
