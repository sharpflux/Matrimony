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
import android.icu.util.CurrencyAmount;
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
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.sqlite.SQLiteVehicleDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;
import java.util.Currency;
import java.util.Map;

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;
import me.abhinay.input.CurrencyEditText;
import me.abhinay.input.CurrencySymbols;

public class CustomDialogAddVehicle extends Dialog {



    public Context context;

    private EditText editText_vehicleType, editText_brandName, editText_modelName, editText_year;
    private CurrencyEditText editText_cost;
    private TextView textView_title, textView_vehicleTypeId, textView_brandNameId, textView_modelNameId,
            textView_addVehicle;

    private ImageView imageView_back;

    private Map<String, Integer> list;

    private SQLiteVehicleDetails sqLiteVehicleDetails;
    private DataFetcher dataFetcher;

    private String id, vehicle_details_id;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogAddVehicle(Context context, String id, String vehicle_details_id,  AddPersonAdapter addPersonAdapter,
                               ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.vehicle_details_id = vehicle_details_id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.custom_dialog_add_vehicle);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteVehicleDetails = new SQLiteVehicleDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);
        editText_vehicleType = findViewById(R.id.editText_vehicleType);
        textView_vehicleTypeId = findViewById(R.id.textView_vehicleTypeId);
        editText_brandName = findViewById(R.id.editText_brandName);
        textView_brandNameId = findViewById(R.id.textView_brandNameId);
        editText_modelName = findViewById(R.id.editText_modelName);
        textView_modelNameId = findViewById(R.id.textView_modelNameId);
        editText_year = findViewById(R.id.editText_year);
        editText_cost = findViewById(R.id.editText_cost);
        textView_addVehicle = findViewById(R.id.textView_addVehicle);

        dataFetcher = new DataFetcher("Address",context);


        editText_cost.setCurrency(CurrencySymbols.NONE);
        editText_cost.setDecimals(false);

        textView_title.setText("Vehicle Details");
        imageView_back.setVisibility(View.GONE);


        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteVehicleDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                editText_vehicleType.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.VEHICLE_TYPE)));
                textView_vehicleTypeId.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.VEHICLE_TYPE_ID)));
                editText_brandName.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.BRAND_NAME)));
                textView_brandNameId.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.BRAND_NAME_ID)));
                editText_modelName.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.MODEL_NAME)));
                textView_modelNameId.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.MODEL_NAME_ID)));
                editText_year.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.YEAR)));
                editText_cost.setText(cursor.getString(cursor.getColumnIndex(SQLiteVehicleDetails.COST)));



            }


            cursor.close();
        }

        textView_addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String vehicleType = editText_vehicleType.getText().toString().trim();
                String vehicleTypeId = textView_vehicleTypeId.getText().toString().trim();
                String brandName = editText_brandName.getText().toString().trim();
                String brandNameId = textView_brandNameId.getText().toString().trim();
                String modelName = editText_modelName.getText().toString().trim();
                String modelNameId = textView_modelNameId.getText().toString().trim();
                String year = editText_year.getText().toString().trim();
                String cost = editText_cost.getText().toString().trim();


                if(id.equals("0")) {
                    long res = sqLiteVehicleDetails.insertVehicleDetails("0", vehicleType,
                            vehicleTypeId, brandName, brandNameId, modelName, modelNameId, year, cost);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res),"0", brandName, modelName));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteVehicleDetails.updateVehicleDetails(id, "0", vehicleType,
                            vehicleTypeId, brandName, brandNameId, modelName, modelNameId, year, cost);
                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), vehicle_details_id, brandName, modelName));
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

            onClick(editText_vehicleType,"VehicleType", "0");
            onClick(editText_brandName,"BrandName", textView_vehicleTypeId.getText().toString());
            onClick(editText_modelName,"ModelName", textView_brandNameId.getText().toString());


        editText_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialogAddYear customDialogAddYear = new CustomDialogAddYear(context,editText_year);
                customDialogAddYear.show();
            }
        });

    }

    private void onClick(EditText editText, final String stringFor, final String id) {

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute(stringFor, id);

            }
        });

    }


    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


                if(params[0].equals("VehicleType"))
                {
                    dataFetcher.loadList(URLs.URL_GET_VEHICLETYPE+"Type=Type&CorrespondenceId=0&Language="+userModel.getLanguage(),"ID",
                            "Name", editText_vehicleType, textView_vehicleTypeId, context, customDialogLoadingProgressBar);

                }

                else if(params[0].equals("BrandName"))
                {
                    String id = textView_vehicleTypeId.getText().toString();
                    dataFetcher.loadList(URLs.URL_GET_VEHICLETYPE+"Type=Make&CorrespondenceId="+id+"&Language="+userModel.getLanguage(),
                            "ID", "Name",
                            editText_brandName, textView_brandNameId, context, customDialogLoadingProgressBar);

                }

                else if(params[0].equals("ModelName"))
                {
                    String id = textView_brandNameId.getText().toString();
                    dataFetcher.loadList(URLs.URL_GET_VEHICLETYPE+"Type=Modal&CorrespondenceId="+id+"&Language="+userModel.getLanguage(),"ID",
                            "Name", editText_modelName, textView_modelNameId, context, customDialogLoadingProgressBar);

                }

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


}
