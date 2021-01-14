package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.adapter.ViewMultipleDetailsAdapter;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteLanguageKnownDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;
import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class CustomDialogViewFarm extends Dialog {



    public Context context;


    private TextView textView_title, textView_cancel, textView_farmingArea, textView_areaFarmed,
            textView_cropsTaken, textView_farmType;

    private ImageView imageView_back;



    private Map<String, Integer> list;

    private SQLiteFarmDetails sqLiteDetails;
    private DataFetcher dataFetcher;

    private String id, detailsId;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private ViewMultipleDetailsAdapter viewMultipleDetailsAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogViewFarm(Context context, String id, String detailsId,
                                    ViewMultipleDetailsAdapter viewMultipleDetailsAdapter,
                                    ArrayList<AddPersonModel> addPersonModelArrayList, int position) {

        super(context);
        this.context = context;
        this.id = id;
        this.detailsId = detailsId;
        this.viewMultipleDetailsAdapter = viewMultipleDetailsAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_view_farm_details);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteDetails = new SQLiteFarmDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);


        textView_farmingArea = findViewById(R.id.textView_farmingArea);
        textView_areaFarmed = findViewById(R.id.textView_areaFarmed);
        textView_cropsTaken = findViewById(R.id.textView_cropsTaken);
        textView_farmType = findViewById(R.id.textView_farmType);


        textView_cancel = findViewById(R.id.textView_cancel);

        dataFetcher = new DataFetcher("Address",context);
        textView_title.setText(context.getResources().getString(R.string.farm_details));
        imageView_back.setVisibility(View.GONE);



        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                textView_farmingArea.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.AREA)));
                textView_areaFarmed.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE)));
                textView_cropsTaken.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.CROPS)));
                textView_farmType.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.IRRIGATION_TYPE)));

            }


            cursor.close();
        }



        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });




    }


}
