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
import android.os.Handler;
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

public class CustomDialogAddProperty extends Dialog {



    public Context context;

    private EditText editText_propertyType, editText_bhkType, editText_propertyAddress,
            editText_country, editText_state, editText_city;
    //editText_district, editText_taluka

    private CurrencyEditText editText_propertyArea;

    private TextView textView_title, textView_propertyTypeId, textView_bhkTypeId, textView_addProperty,
            textView_countryId, textView_stateId, textView_cityId;
    //textView_districtId, textView_talukaId

    private ImageView imageView_back;

    private RadioGroup radioGroup_ownershipType;

    private Map<String, Integer> list;

    private SQLitePropertyDetails sqLitePropertyDetails;
    private DataFetcher dataFetcher;

    private String id, property_details_id;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    private int currentCountryId =0, currentStateId=0, newCountryId=0, newStateId=0;

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


        setContentView(R.layout.custom_dialog_add_property);

//        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
//            context.startActivity(new Intent(context, LoginActivity.class));
//        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLitePropertyDetails = new SQLitePropertyDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        imageView_back = findViewById(R.id.imageView_back);
        textView_title = findViewById(R.id.textView_title);
        editText_propertyType = findViewById(R.id.editText_propertyType);
        textView_propertyTypeId = findViewById(R.id.textView_propertyTypeId);
        radioGroup_ownershipType = findViewById(R.id.radioGroup_ownershipType);
        editText_bhkType = findViewById(R.id.editText_bhkType);
        textView_bhkTypeId = findViewById(R.id.textView_bhkTypeId);
        editText_propertyArea = findViewById(R.id.editText_propertyArea);
        editText_propertyAddress = findViewById(R.id.editText_propertyAddress);

        editText_country = findViewById(R.id.editText_country);
        textView_countryId = findViewById(R.id.textView_countryId);
        editText_state = findViewById(R.id.editText_state);
        textView_stateId = findViewById(R.id.textView_stateId);
        editText_city = findViewById(R.id.editText_city);
        textView_cityId = findViewById(R.id.textView_cityId);
/*        editText_district = findViewById(R.id.editText_district);
        textView_districtId = findViewById(R.id.textView_districtId);
        editText_taluka = findViewById(R.id.editText_taluka);
        textView_talukaId = findViewById(R.id.textView_talukaId);*/
        textView_addProperty = findViewById(R.id.textView_addProperty);

        dataFetcher = new DataFetcher("Address",context);

        textView_title.setText("Property Details");

        showPopUp(editText_country, "Country");
        showPopUp(editText_state, "State");
        showPopUp(editText_city, "City");
/*        showPopUp(editText_district, "District");
        showPopUp(editText_taluka, "Taluka");*/


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
                textView_countryId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.COUNTRY_ID)));
                editText_country.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.COUNTRY_NAME)));
                textView_stateId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_ID)));
                editText_state.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_NAME)));
                textView_cityId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CITY_ID)));
                editText_city.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CITY_NAME)));
/*                textView_districtId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.DISTRICT_ID)));
                editText_district.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.DISTRICT_NAME)));
                textView_talukaId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.TALUKA_ID)));
                editText_taluka.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.TALUKA_NAME)));*/

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
                String country_id = textView_countryId.getText().toString().trim();
                String country_name = editText_country.getText().toString().trim();
                String state_id = textView_stateId.getText().toString().trim();
                String state_name = editText_state.getText().toString().trim();
                String city_id = textView_cityId.getText().toString().trim();
                String city_name = editText_city.getText().toString().trim();


               /* String district_id = textView_districtId.getText().toString().trim();
                String taluka_id = textView_talukaId.getText().toString().trim();*/

                /*String district_name = editText_district.getText().toString().trim();
                String taluka_name = editText_taluka.getText().toString().trim();
*/

                if(id.equals("0")) {
                    long res = sqLitePropertyDetails.insertPropertyDetails( "0", propertyType,
                            propertyTypeId, ownershipType, bhkType, bhkTypeId, carpetArea, address,
                            country_name, country_id, state_name, state_id, city_name, city_id); //district_name, district_id, taluka_name, taluka_id

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
                            country_name, country_id, state_name, state_id, city_name, city_id);
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

        editText_country.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText_country.hasFocus())
                {

                    editText_state.setText("");
                    textView_stateId.setText("0");
                    editText_city.setText("");
                    textView_cityId.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_state.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText_state.isHovered())
                {
                    editText_city.setText("");
                    textView_cityId.setText("0");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }

    private void showPopUp(final EditText editText, final String urlFor)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();

                String id="";

                if(urlFor.equals("Country"))
                {

                }
                else if(urlFor.equals("State"))
                {
                    id =  textView_countryId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select Country first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }

                else if(urlFor.equals("City"))
                {
                    id = textView_stateId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select State first", Toast.LENGTH_SHORT).show();
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
                    dataFetcher.loadList(URLs.URL_GET_PROPERTYBHKTYPE+"Language="+userModel.getLanguage(),"BHKTypeId",
                            "BHK_Name", editText_bhkType, textView_bhkTypeId, context, customDialogLoadingProgressBar);


                }

                if(params[0].equals("Country"))
                {

                    dataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                            "Name", editText_country, textView_countryId,getContext(),
                            customDialogLoadingProgressBar);


                    return "Country";

                }
                else if(params[0].equals("State"))
                {
                    String id = textView_countryId.getText().toString();

                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage()
                    + "&CountryID="+id,"StatesID", "StatesName",
                    editText_state, textView_stateId,getContext(),
                    customDialogLoadingProgressBar);



                }
                else if(params[0].equals("City"))
                {
                    String id = textView_stateId.getText().toString();

                    dataFetcher.loadList(URLs.URL_GET_CITY+"Language="+userModel.getLanguage()
                                    + "&StateID="+id,"ID",
                            "Name", editText_city, textView_cityId,getContext(),
                            customDialogLoadingProgressBar);


                }

                return params[0];

            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

            if(result.equals("Country"))
            {
                newCountryId = Integer.parseInt(textView_countryId.getText().toString());
                if (currentCountryId != newCountryId) {
                    currentCountryId = newCountryId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_stateId.setText("0");
                            editText_state.setText("");
                            textView_cityId.setText("0");
                            editText_city.setText("");
                        }
                    });

                }
            }
            else if(result.equals("State"))
            {
                newStateId = Integer.parseInt(textView_stateId.getText().toString());
                if (currentStateId != newStateId) {
                    currentStateId = newStateId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_cityId.setText("0");
                            editText_city.setText("");
                        }
                    });

                }
            }

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
