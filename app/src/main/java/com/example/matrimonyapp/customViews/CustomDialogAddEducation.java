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

import com.abdallahalaraby.blink.Screenshot;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteEducationDetails;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
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

public class CustomDialogAddEducation extends Dialog {



    public Context context;

    private EditText editText_educationLevel, editText_institue, editText_address, editText_state,
                     editText_district, editText_taluka;

    private TextView textView_title, textView_educationLevelId, textView_addEducation, textView_stateId,
                     textView_districtId, textView_talukaId;


    private ImageView imageView_back;


    private Map<String, Integer> list;

    private SQLiteEducationDetails sqLiteEducationDetails;
    private DataFetcher dataFetcher;

    private String id, education_details_id;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogAddEducation(Context context, String id, String education_details_id,  AddPersonAdapter addPersonAdapter,
                               ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.education_details_id = education_details_id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //take Screenshot
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



        setContentView(R.layout.custom_dialog_add_education);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteEducationDetails = new SQLiteEducationDetails(context);


        setCanceledOnTouchOutside(true);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);
        editText_educationLevel = findViewById(R.id.editText_educationLevel);
        textView_educationLevelId = findViewById(R.id.textView_educationLevelId);
        editText_institue = findViewById(R.id.editText_institue);
        editText_address = findViewById(R.id.editText_address);
        editText_state = findViewById(R.id.editText_state);
        textView_stateId = findViewById(R.id.textView_stateId);
        editText_district = findViewById(R.id.editText_district);
        textView_districtId = findViewById(R.id.textView_districtId);
        editText_taluka = findViewById(R.id.editText_taluka);
        textView_talukaId = findViewById(R.id.textView_talukaId);


        textView_addEducation = findViewById(R.id.textView_addEducation);

        dataFetcher = new DataFetcher("Address",context);


        textView_title.setText(context.getResources().getString(R.string.education_details));
        imageView_back.setVisibility(View.GONE);


        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteEducationDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                editText_educationLevel.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.QUALIFICATION_LEVEL)));
                textView_educationLevelId.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.QUALIFICATION_LEVEL_ID)));
                editText_institue.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.INSTITUTE_NAME)));
                editText_address.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.ADDRESS)));
                editText_state.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.STATE_NAME)));
                textView_stateId.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.STATE_ID)));
                editText_district.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.DISTRICT_NAME)));
                textView_districtId.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.DISTRICT_ID)));
                editText_taluka.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.TALUKA_NAME)));
                textView_talukaId.setText(cursor.getString(cursor.getColumnIndex(SQLiteEducationDetails.TALUKA_ID)));

                //Toast.makeText(context, cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE))+"---",Toast.LENGTH_SHORT).show();


            }


            cursor.close();
        }

        textView_addEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String educationLevel = editText_educationLevel.getText().toString().trim();
                String educationLevelId = textView_educationLevelId.getText().toString().trim();
                String instituteName = editText_institue.getText().toString().trim();
                String address = editText_address.getText().toString().trim();
                String stateName = editText_state.getText().toString().trim();
                String stateNameId = textView_stateId.getText().toString().trim();
                String districtName = editText_district.getText().toString().trim();
                String districtNameId = textView_districtId.getText().toString().trim();
                String talukaName = editText_taluka.getText().toString().trim();
                String talukaNameId = textView_talukaId.getText().toString().trim();


                if(id.equals("0")) {
                    long res = sqLiteEducationDetails.insertFarmDetails("0",
                            educationLevel, educationLevelId, instituteName, address, stateName, stateNameId,
                            districtName, districtNameId, talukaName, talukaNameId);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res),"0", educationLevel, instituteName));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteEducationDetails.updateFarmDetails(id, education_details_id,
                            educationLevel, educationLevelId, instituteName, address, stateName, stateNameId,
                            districtName, districtNameId, talukaName, talukaNameId);
                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), education_details_id, educationLevel, instituteName));
                        addPersonAdapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(context, "Error in sqlite updation", Toast.LENGTH_SHORT).show();

                    }


                }
                dismiss();

            }
        });


        onClickListener();


    }


    private void onClickListener()
    {




    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


              /*  if(params[0].equals("EducationLevel"))
                {
                    dataFetcher.loadList(URLs.URL_GET_QUALIFICATIONLEVEL+"Language="+userModel.getLanguage(),"VehicleTypeId",
                            "VehicleType", editText_vehicleType, textView_vehicleTypeId, context, customDialogLoadingProgressBar);

                }

                else if(params[0].equals("BrandName"))
                {
                    String id = textView_vehicleTypeId.getText().toString();
                    dataFetcher.loadList(URLs.URL_GET_VEHICLEBRANDNAME+"Language="+userModel.getLanguage()
                                    +"&VehicleTypeId="+id,"VehicalMakeId",
                            "VehicalMake", editText_brandName, textView_brandNameId, context, customDialogLoadingProgressBar);

                }

                else if(params[0].equals("ModelName"))
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_VEHICLETYPE+"Language="+userModel.getLanguage(),"VehicleTypeId",
                            "VehicleType", editText_modelName, textView_modelNameId, context, customDialogLoadingProgressBar);

                }*/

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

/*
    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {





            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }*/


}
