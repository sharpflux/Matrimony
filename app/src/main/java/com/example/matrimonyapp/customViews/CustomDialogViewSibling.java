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

import androidx.cardview.widget.CardView;

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

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;
import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class CustomDialogViewSibling extends Dialog {



    public Context context;


    private TextView textView_title, textView_cancel, textView_relation, textView_siblingName, textView_mobileNo,
            textView_qualification, textView_occupation, textView_maritalStatus, textView_spouseName,
            textView_fatherInLawName, textView_fatherInLawMobileNo, textView_fatherInLawAddress,
            textView_fatherInLawCountry, textView_fatherInLawState, textView_fatherInLawCity;

    private ImageView imageView_back;

    private CardView cardView_fatherInLaw;

    private Map<String, Integer> list;

    private SQLiteSiblingDetails sqLiteSiblingDetails;
    private DataFetcher dataFetcher;

    private String id, detailsId;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private ViewMultipleDetailsAdapter viewMultipleDetailsAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogViewSibling(Context context, String id, String detailsId,
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



        setContentView(R.layout.custom_dialog_view_sibling_details);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteSiblingDetails = new SQLiteSiblingDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);

        textView_relation = findViewById(R.id.textView_relation);
        textView_siblingName = findViewById(R.id.textView_siblingName);
        textView_mobileNo = findViewById(R.id.textView_mobileNo);
        textView_qualification = findViewById(R.id.textView_qualification);
        textView_occupation = findViewById(R.id.textView_occupation);
        textView_maritalStatus = findViewById(R.id.textView_maritalStatus);
        textView_spouseName = findViewById(R.id.textView_spouseName);
        textView_fatherInLawName = findViewById(R.id.textView_fatherInLawName);
        textView_fatherInLawMobileNo = findViewById(R.id.textView_fatherInLawMobileNo);
        textView_fatherInLawAddress = findViewById(R.id.textView_fatherInLawAddress);
        textView_fatherInLawCountry = findViewById(R.id.textView_fatherInLawCountry);
        textView_fatherInLawState = findViewById(R.id.textView_fatherInLawState);
        textView_fatherInLawCity = findViewById(R.id.textView_fatherInLawCity);
        cardView_fatherInLaw = findViewById(R.id.cardView_fatherInLaw);


        textView_cancel = findViewById(R.id.textView_cancel);

        dataFetcher = new DataFetcher("Address",context);
        textView_title.setText(context.getResources().getString(R.string.siblings_details));
        imageView_back.setVisibility(View.GONE);



        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteSiblingDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {


                textView_siblingName.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.NAME)));
                textView_relation.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.RELATION)));
                textView_mobileNo.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.MOBILE_NO)));
                textView_qualification.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.EDUCATION_NAME)));
                textView_occupation.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.OCCUPATION_NAME)));
                textView_maritalStatus.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.MARITAL_STATUS)));
                textView_spouseName.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.SPOUSE_NAME)));
                textView_fatherInLawName.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_NAME)));
                textView_fatherInLawMobileNo.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_MOBILE_NO)));
                textView_fatherInLawAddress.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_VILLAGE)));
                textView_fatherInLawCountry.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_COUNTRY_NAME)));
                textView_fatherInLawState.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_STATE_NAME)));
                textView_fatherInLawCity.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_CITY_NAME)));


                if(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.MARITAL_STATUS)).equals(context.getResources().getString(R.string.unmarried)))
                {
                    cardView_fatherInLaw.setVisibility(View.GONE);
                }
                else
                {
                    cardView_fatherInLaw.setVisibility(View.VISIBLE);
                }


                //.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.)));

                //Toast.makeText(context, cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE))+"---",Toast.LENGTH_SHORT).show();


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
