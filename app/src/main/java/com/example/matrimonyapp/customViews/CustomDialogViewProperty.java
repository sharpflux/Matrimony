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

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;
import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class CustomDialogViewProperty extends Dialog {



    public Context context;


    private TextView textView_title, textView_cancel, textView_propertyType, textView_ownershipType,
            textView_bhkType, textView_carpetArea, textView_address, textView_country, textView_state,
            textView_city;

    private ImageView imageView_back;



    private Map<String, Integer> list;

    private SQLitePropertyDetails sqLiteDetails;
    private DataFetcher dataFetcher;

    private String id, detailsId;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private ViewMultipleDetailsAdapter viewMultipleDetailsAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogViewProperty(Context context, String id, String detailsId,
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



        setContentView(R.layout.custom_dialog_view_property_details);

//        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
//            context.startActivity(new Intent(context, LoginActivity.class));
//        }

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteDetails = new SQLitePropertyDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);



        textView_propertyType = findViewById(R.id.textView_propertyType);
        textView_ownershipType = findViewById(R.id.textView_ownershipType);
        textView_bhkType = findViewById(R.id.textView_bhkType);
        textView_carpetArea = findViewById(R.id.textView_carpetArea);
        textView_address = findViewById(R.id.textView_address);
        textView_country = findViewById(R.id.textView_country);
        textView_state = findViewById(R.id.textView_state);
        textView_city = findViewById(R.id.textView_city);


        textView_cancel = findViewById(R.id.textView_cancel);

        dataFetcher = new DataFetcher("Address",context);
        textView_title.setText(context.getResources().getString(R.string.property_details));
        imageView_back.setVisibility(View.GONE);



        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                textView_propertyType.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.PROPERTY_TYPE)));
                textView_ownershipType.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.OWNERSHIP_TYPE)));
                textView_bhkType.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.BHK_TYPE)));
                textView_carpetArea.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CARPET_AREA)));
                textView_address.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.ADDRESS)));
                /*textView_country.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.COUNTRY_NAME)));
                textView_state.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_NAME)));
                textView_city.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CITY_NAME)));
*/

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
