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

public class CustomDialogAddProperty extends Dialog {



    public Context context;

    private EditText editText_propertyType, editText_bhkType, editText_propertyAddress,
            editText_state, editText_district, editText_taluka;

    private CurrencyEditText editText_propertyArea;

    private TextView textView_title, textView_propertyTypeId, textView_bhkTypeId, textView_addProperty,
            textView_stateId, textView_districtId, textView_talukaId;

    private ImageView imageView_back;

    private RadioGroup radioGroup_ownershipType;

    private Map<String, Integer> list;

    private SQLitePropertyDetails sqLitePropertyDetails;
    private DataFetcher dataFetcher;

    private String id, property_details_id;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogAddProperty(Context context, String id, String property_details_id, AddPersonAdapter addPersonAdapter,
                               ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.property_details_id = property_details_id;
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



        setContentView(R.layout.custom_dialog_add_property);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLitePropertyDetails = new SQLitePropertyDetails(context);


        setCanceledOnTouchOutside(true);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);
        editText_propertyType = findViewById(R.id.editText_propertyType);
        textView_propertyTypeId = findViewById(R.id.textView_propertyTypeId);
        radioGroup_ownershipType = findViewById(R.id.radioGroup_ownershipType);
        editText_bhkType = findViewById(R.id.editText_bhkType);
        textView_bhkTypeId = findViewById(R.id.textView_bhkTypeId);
        editText_propertyArea = findViewById(R.id.editText_propertyArea);
        editText_propertyAddress = findViewById(R.id.editText_propertyAddress);

        editText_state = findViewById(R.id.editText_state);
        textView_stateId = findViewById(R.id.textView_stateId);
        editText_district = findViewById(R.id.editText_district);
        textView_districtId = findViewById(R.id.textView_districtId);
        editText_taluka = findViewById(R.id.editText_taluka);
        textView_talukaId = findViewById(R.id.textView_talukaId);
        textView_addProperty = findViewById(R.id.textView_addProperty);

        dataFetcher = new DataFetcher("Address",context);

        textView_title.setText("Property Details");

        showPopUp(editText_state, "State");
        showPopUp(editText_district, "District");
        showPopUp(editText_taluka, "Taluka");


        textChangedListener();

        onClickListener();

        editText_propertyArea.setCurrency(CurrencySymbols.NONE);
        editText_propertyArea.setDecimals(false);

        if(!id.equals("0"))
        {

            Cursor cursor = sqLitePropertyDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                FieldValidation.setRadioButtonAccToValue(radioGroup_ownershipType, cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.OWNERSHIP_TYPE)));

                editText_propertyType.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.PROPERTY_TYPE)));
                textView_propertyTypeId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.PROPERTY_TYPE_ID)));
                editText_bhkType.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.BHK_TYPE)));
                textView_bhkTypeId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.BHK_TYPE_ID)));
                editText_propertyArea.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CARPET_AREA)));
                editText_propertyAddress.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.ADDRESS)));
                textView_stateId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_ID)));
                editText_state.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_NAME)));
                textView_districtId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.DISTRICT_ID)));
                editText_district.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.DISTRICT_NAME)));
                textView_talukaId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.TALUKA_ID)));
                editText_taluka.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.TALUKA_NAME)));

            }

            cursor.close();

        }

    }


    private void onClickListener()
    {

        imageView_back.setVisibility(View.GONE);

        editText_bhkType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("BHKType");
            }
        });

        editText_propertyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("PropertyType");
            }
        });

        textView_addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String ownershipType="";
                RadioButton radioButton_ownershipType =(RadioButton)findViewById(radioGroup_ownershipType.getCheckedRadioButtonId());
                if(radioButton_ownershipType!=null)
                {
                    ownershipType = radioButton_ownershipType.getText().toString();
                }

                String propertyType = editText_propertyType.getText().toString().trim();
                String propertyTypeId = textView_propertyTypeId.getText().toString().trim();
                String bhkType = editText_bhkType.getText().toString().trim();
                String bhkTypeId = textView_bhkTypeId.getText().toString().trim();

                String carpetArea = editText_propertyArea.getText().toString().trim();
                String address = editText_propertyAddress.getText().toString().trim();
                String state_id = textView_stateId.getText().toString().trim();
                String district_id = textView_districtId.getText().toString().trim();
                String taluka_id = textView_talukaId.getText().toString().trim();
                String state_name = editText_state.getText().toString().trim();
                String district_name = editText_district.getText().toString().trim();
                String taluka_name = editText_taluka.getText().toString().trim();


                if(id.equals("0")) {
                    long res = sqLitePropertyDetails.insertPropertyDetails( "0", propertyType,
                            propertyTypeId, ownershipType, bhkType, bhkTypeId, carpetArea, address,
                            state_name, state_id, district_name, district_id, taluka_name, taluka_id);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res), "0",
                                carpetArea+" sq. ft. "+propertyType, "in "+address));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLitePropertyDetails.updatePropertyDetails(id, property_details_id, propertyType,
                            propertyTypeId, ownershipType, bhkType, bhkTypeId, carpetArea, address,
                            state_name, state_id, district_name, district_id, taluka_name, taluka_id);
                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), property_details_id,
                                carpetArea+" sq. ft. "+propertyType, "in "+address));
                        addPersonAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Error in sqlite updation", Toast.LENGTH_SHORT).show();

                    }


                }
                dismiss();

            }
        });

    }

    private void textChangedListener()
    {

        editText_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_district.setText("");
                textView_districtId.setText("0");
                editText_taluka.setText("");
                textView_talukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_taluka.setText("");
                textView_talukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void showPopUp(EditText editText, final String urlFor)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();

                String id="";

                if(urlFor.equals("District"))
                {
                    id =  textView_stateId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select State first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(urlFor.equals("Taluka"))
                {
                    id = textView_districtId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select District first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                runner.execute(urlFor, id);
            }
        });


    }


    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                if(params[0].toString()=="PropertyType")
                {
                    dataFetcher.loadList(URLs.URL_GET_PROPERTYTYPE+"Language="+userModel.getLanguage(),"ProertyTypeId",
                            "PropertyName", editText_propertyType, textView_propertyTypeId, context, customDialogLoadingProgressBar);


                }
                else if(params[0].toString()=="BHKType")
                {
                    dataFetcher.loadList(URLs.URL_GET_PROPERTYTYPE+"Language="+userModel.getLanguage(),"ProertyTypeId",
                            "PropertyName", editText_bhkType, textView_bhkTypeId, context, customDialogLoadingProgressBar);


                }

                else if(params[0].toString()=="State")
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),"StatesID",
                            "StatesName", editText_state, textView_stateId, context, customDialogLoadingProgressBar);


                }
                else if(params[0].toString()=="District")
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_district, textView_districtId,
                            context, customDialogLoadingProgressBar);

                }
                else if(params[0].toString()=="Taluka")
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_taluka, textView_talukaId,
                            context, customDialogLoadingProgressBar);
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
