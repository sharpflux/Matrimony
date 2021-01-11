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

public class CustomDialogViewLanguageKnown extends Dialog {



    public Context context;

    private EditText editText_languageKnown;
    private TextView textView_title, textView_addLanguage, textView_languageKnownId;

    private ImageView imageView_back;

    private RadioGroup radioGroup_fluency;

    private Map<String, Integer> list;

    private SQLiteLanguageKnownDetails sqLiteLanguageKnownDetails;
    private DataFetcher dataFetcher;

    private String id, languageKnownDetailsId;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private ViewMultipleDetailsAdapter viewMultipleDetailsAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogViewLanguageKnown(Context context, String id, String languageKnownDetailsId,
                                         ViewMultipleDetailsAdapter viewMultipleDetailsAdapter,
                                         ArrayList<AddPersonModel> addPersonModelArrayList, int position) {

        super(context);
        this.context = context;
        this.id = id;
        this.languageKnownDetailsId = languageKnownDetailsId;
        this.viewMultipleDetailsAdapter = viewMultipleDetailsAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_view_language_known);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteLanguageKnownDetails = new SQLiteLanguageKnownDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);
        editText_languageKnown = findViewById(R.id.editText_languageKnown);
        radioGroup_fluency = findViewById(R.id.radioGroup_fluency);
        textView_languageKnownId = findViewById(R.id.textView_languageKnownId);
        textView_addLanguage = findViewById(R.id.textView_addLanguageKnown);

        dataFetcher = new DataFetcher("Address",context);


        textView_title.setText(context.getResources().getString(R.string.language_known));
        imageView_back.setVisibility(View.GONE);



        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteLanguageKnownDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                editText_languageKnown.setText(cursor.getString(cursor.getColumnIndex(SQLiteLanguageKnownDetails.LANGUAGE)));
                textView_languageKnownId.setText(cursor.getString(cursor.getColumnIndex(SQLiteLanguageKnownDetails.LANGUAGE_ID)));
                FieldValidation.setRadioButtonAccToValue(radioGroup_fluency,
                        cursor.getString(cursor.getColumnIndex(SQLiteLanguageKnownDetails.FLUENCY)));

                //Toast.makeText(context, cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE))+"---",Toast.LENGTH_SHORT).show();


            }


            cursor.close();
        }

        editText_languageKnown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("language");
            }
        });

/*

        textView_addLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String language = editText_languageKnown.getText().toString().trim();
                String languageId = textView_languageKnownId.getText().toString().trim();
                String fluency = ((RadioButton)findViewById(radioGroup_fluency.getCheckedRadioButtonId())).getText().toString();

                if(id.equals("0")) {
                    long res = sqLiteLanguageKnownDetails.insertLanguageKnownDetails("0",
                            language, languageId, fluency);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res),"0", language, fluency));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteLanguageKnownDetails.updateLanguageKnownDetails(id, languageKnownDetailsId,
                            language, languageId, fluency);
                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), languageId, language, fluency));
                        addPersonAdapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(context, "Error in sqlite updation", Toast.LENGTH_SHORT).show();

                    }


                }
                dismiss();

            }
        });
*/


        onClickListener();


    }


    private void onClickListener()
    {




    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            if(params[0].equals("language"))
            {
                dataFetcher.loadList(URLs.URL_GET_MOTHERTONGUE+"Language="+userModel.getLanguage(),"MotherTongueId",
                        "MotherTongueName", editText_languageKnown, textView_languageKnownId, context, customDialogLoadingProgressBar);


            }

            return null;
        }


    }



}
