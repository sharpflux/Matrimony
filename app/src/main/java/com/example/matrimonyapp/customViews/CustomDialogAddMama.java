package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
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
import android.widget.CheckBox;
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
//import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.Map;

//import jp.wasabeef.blurry.Blurry;
//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;
//import jp.wasabeef.blurry.internal.BlurTask;

public class CustomDialogAddMama extends Dialog {



    public Context context;

    public EditText editText_mamaOccupation, editText_mamaName, editText_mamaMobileNo, editText_mamaAddress;

    public TextView textView_title, textView_login, textView_createNew, textView_mamaOccupationId ;

    private ImageView imageView_back;

    public PopupMenu popupMenu;

    Map<String, Integer> list;

    private TextView textView_countryId, textView_stateId, textView_cityId, textView_addMama;
    // textView_mamaDistrictId, textView_mamaTalukaId,
    private EditText  editText_country, editText_state, editText_city ;
    //editText_mamaTaluka, editText_mamaDistrict
    private CheckBox checkBox_mamaIsAlive;
    //private SwitchButton switchButton_mamaIsAlive;

    private SQLiteMamaDetails sqLiteMamaDetails;
    DataFetcher dataFetcher;

    String id, mama_details_id;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    PopupFetcher popupFetcher;
    UserModel userModel;
    public String urlFor;
    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;
    private int currentCountryId =0, currentStateId=0, newCountryId=0, newStateId=0;

    public CustomDialogAddMama(Context context, String id, String mama_details_id,  AddPersonAdapter addPersonAdapter,
                                  ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.mama_details_id = mama_details_id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.custom_dialog_add_mama);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteMamaDetails = new SQLiteMamaDetails(context);


        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        textView_title = findViewById(R.id.textView_title);
        imageView_back = findViewById(R.id.imageView_back);
        checkBox_mamaIsAlive = findViewById(R.id.checkBox_mamaIsAlive);
        editText_mamaName = findViewById(R.id.editText_mamaName);
        editText_mamaMobileNo = findViewById(R.id.editText_mamaMobileNo);
        editText_mamaOccupation = findViewById(R.id.editText_mamaOccupation);
        textView_mamaOccupationId = findViewById(R.id.textView_mamaOccupationId);
        editText_mamaAddress = findViewById(R.id.editText_mamaAddress);
        editText_country = findViewById(R.id.editText_country);
        textView_countryId = findViewById(R.id.textView_countryId);
        editText_state = findViewById(R.id.editText_state);
        textView_stateId = findViewById(R.id.textView_stateId);
        editText_city = findViewById(R.id.editText_city);
        textView_cityId = findViewById(R.id.textView_cityId);
        /*editText_mamaState = findViewById(R.id.editText_mamaState);
        textView_mamaStateId = findViewById(R.id.textView_mamaStateId);
        editText_mamaDistrict = findViewById(R.id.editText_mamaDistrict);
        textView_mamaDistrictId = findViewById(R.id.textView_mamaDistrictId);
        editText_mamaTaluka = findViewById(R.id.editText_mamaTaluka);
        textView_mamaTalukaId = findViewById(R.id.textView_mamaTalukaId);*/
        textView_addMama = findViewById(R.id.textView_addMama);

        textView_title.setText("Mama Details");
        imageView_back.setVisibility(View.GONE);

        dataFetcher = new DataFetcher("Address",context);

/*        showPopUp(editText_mamaState, "State");
        showPopUp(editText_mamaDistrict, "District");
        showPopUp(editText_mamaTaluka, "Taluka");*/
        showPopUp(editText_country, "Country");
        showPopUp(editText_state, "State");
        showPopUp(editText_city, "City");


        popupFetcher = new PopupFetcher(context);


        onClickListener();

        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteMamaDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {
                editText_mamaName.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.NAME)));
                editText_mamaMobileNo.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.MOBILE_NO)));
                textView_mamaOccupationId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.OCCUPATION_ID)));
                editText_mamaOccupation.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.OCCUPATION_NAME)));
                editText_mamaAddress.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.ADDRESS)));
                textView_countryId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.COUNTRY_ID)));
                editText_country.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.COUNTRY_NAME)));
                textView_stateId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_ID)));
                editText_state.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.STATE_NAME)));
                textView_cityId.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CITY_ID)));
                editText_city.setText(cursor.getString(cursor.getColumnIndex(SQLitePropertyDetails.CITY_NAME)));
                /*textView_mamaStateId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.STATE_ID)));
                editText_mamaState.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.STATE_NAME)));
                textView_mamaDistrictId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.DISTRICT_ID)));
                editText_mamaDistrict.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.DISTRICT_NAME)));
                textView_mamaTalukaId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.TALUKA_ID)));
                editText_mamaTaluka.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.TALUKA_NAME)));*/
                checkBox_mamaIsAlive.setChecked(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.IS_ALIVE)).equals("1"));


            }

            cursor.close();

        }

    }


    private void onClickListener()
    {

        editText_mamaOccupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("Occupation");
            }
        });


        textView_addMama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String isAlive = checkBox_mamaIsAlive.isChecked()? "1" : "0";
                String name = editText_mamaName.getText().toString().trim();
                String mobileNo = editText_mamaMobileNo.getText().toString().trim();
                String occupationId = textView_mamaOccupationId.getText().toString().trim();
                String occupationName = editText_mamaOccupation.getText().toString().trim();
                String address = editText_mamaAddress.getText().toString().trim();
                String country_id = textView_countryId.getText().toString().trim();
                String country_name = editText_country.getText().toString().trim();
                String state_id = textView_stateId.getText().toString().trim();
                String state_name = editText_state.getText().toString().trim();
                String city_id = textView_cityId.getText().toString().trim();
                String city_name = editText_city.getText().toString().trim();
/*
                String state_id = textView_mamaStateId.getText().toString().trim();
                String district_id = textView_mamaDistrictId.getText().toString().trim();
                String taluka_id = textView_mamaTalukaId.getText().toString().trim();
                String state_name = editText_mamaState.getText().toString().trim();
                String district_name = editText_mamaDistrict.getText().toString().trim();
                String taluka_name = editText_mamaTaluka.getText().toString().trim();*/


                if(id.equals("0")) {
                    long res = sqLiteMamaDetails.insertMamaDetails("0", name, mobileNo, occupationId, occupationName, address,
                            country_name, country_id, state_name, state_id, city_name, city_id, isAlive);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res), "0", name, address));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteMamaDetails.updateMamaDetails(id, mama_details_id, name, mobileNo, occupationId, occupationName, address,
                            country_name, country_id, state_name, state_id, city_name, city_id, isAlive);

                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), mama_details_id, name, address));
                        addPersonAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Error in sqlite updation", Toast.LENGTH_SHORT).show();

                    }


                }
                dismiss();

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
/*    private void showPopUp(EditText editText, final String urlFor)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();

                String id="";

                if(urlFor.equals("District"))
                {
                    id =  textView_mamaStateId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select State first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(urlFor.equals("Taluka"))
                {
                    id = textView_mamaDistrictId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select District first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                runner.execute(urlFor, id);
            }
        });


    }*/
/*
    protected void getSiblingsList()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_SIBLINGSLIST+"Language="+userModel.getLanguage(),
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {


                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {

                                list = new HashMap<String, Integer>();

                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    list.put(jsonObject.getString("SiblingListName"),
                                            new Integer(jsonObject.getInt("SiblingListId")));

                                    popupMenu.getMenu().add(jsonObject.getString("SiblingListName"));

                                }


                            } else {
                                Toast.makeText(context,"Invalid Request",Toast.LENGTH_SHORT).show();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong!\nPlease try again later",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);






    }
*/

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


                if(params[0].toString().equals("Occupation"))
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId",
                            "OccupationName", editText_mamaOccupation,
                            textView_mamaOccupationId, context, customDialogLoadingProgressBar);

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
/*                else if(params[0].toString()=="District")
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_mamaDistrict, textView_mamaDistrictId,
                            context, customDialogLoadingProgressBar);

                }
                else if(params[0].toString()=="Taluka")
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_mamaTaluka, textView_mamaTalukaId,
                            context, customDialogLoadingProgressBar);
                }*/

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
