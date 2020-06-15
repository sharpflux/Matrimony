package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
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
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.suke.widget.SwitchButton;

import java.util.ArrayList;
import java.util.Map;

public class CustomDialogAddMama extends Dialog {



    public Context context;

    public EditText editText_mamaOccupation, editText_mamaName, editText_mamaMobileNo, editText_mamaAddress;

    public TextView textView_login, textView_createNew, textView_mamaOccupationId ;

    public PopupMenu popupMenu;

    Map<String, Integer> list;

    private TextView textView_mamaStateId, textView_mamaDistrictId, textView_mamaTalukaId, textView_addMama;
    private EditText editText_mamaState, editText_mamaTaluka, editText_mamaDistrict;
    private SwitchButton switchButton_mamaIsAlive;

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


        switchButton_mamaIsAlive = findViewById(R.id.switchButton_mamaIsAlive);
        editText_mamaName = findViewById(R.id.editText_mamaName);
        editText_mamaMobileNo = findViewById(R.id.editText_mamaMobileNo);
        editText_mamaOccupation = findViewById(R.id.editText_mamaOccupation);
        textView_mamaOccupationId = findViewById(R.id.textView_mamaOccupationId);
        editText_mamaAddress = findViewById(R.id.editText_mamaAddress);
        editText_mamaState = findViewById(R.id.editText_mamaState);
        textView_mamaStateId = findViewById(R.id.textView_mamaStateId);
        editText_mamaDistrict = findViewById(R.id.editText_mamaDistrict);
        textView_mamaDistrictId = findViewById(R.id.textView_mamaDistrictId);
        editText_mamaTaluka = findViewById(R.id.editText_mamaTaluka);
        textView_mamaTalukaId = findViewById(R.id.textView_mamaTalukaId);
        textView_addMama = findViewById(R.id.textView_addMama);

        dataFetcher = new DataFetcher("Address",context);

        showPopUp(editText_mamaState, "State");
        showPopUp(editText_mamaDistrict, "District");
        showPopUp(editText_mamaTaluka, "Taluka");


        popupFetcher = new PopupFetcher(context);

        textChangedListener();

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
                textView_mamaStateId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.STATE_ID)));
                editText_mamaState.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.STATE_NAME)));
                textView_mamaDistrictId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.DISTRICT_ID)));
                editText_mamaDistrict.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.DISTRICT_NAME)));
                textView_mamaTalukaId.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.TALUKA_ID)));
                editText_mamaTaluka.setText(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.TALUKA_NAME)));
                switchButton_mamaIsAlive.setChecked(cursor.getString(cursor.getColumnIndex(SQLiteMamaDetails.IS_ALIVE)).equals("1"));


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

                String isAlive = switchButton_mamaIsAlive.isChecked()? "1" : "0";
                String name = editText_mamaName.getText().toString().trim();
                String mobileNo = editText_mamaMobileNo.getText().toString().trim();
                String occupationId = textView_mamaOccupationId.getText().toString().trim();
                String occupationName = editText_mamaOccupation.getText().toString().trim();
                String address = editText_mamaAddress.getText().toString().trim();
                String state_id = textView_mamaStateId.getText().toString().trim();
                String district_id = textView_mamaDistrictId.getText().toString().trim();
                String taluka_id = textView_mamaTalukaId.getText().toString().trim();
                String state_name = editText_mamaState.getText().toString().trim();
                String district_name = editText_mamaDistrict.getText().toString().trim();
                String taluka_name = editText_mamaTaluka.getText().toString().trim();


                if(id.equals("0")) {
                    long res = sqLiteMamaDetails.insertMamaDetails("0", name, mobileNo, occupationId, occupationName, address,
                            state_id, district_id, taluka_id, state_name, district_name, taluka_name, isAlive);

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
                            state_id, district_id, taluka_id, state_name, district_name, taluka_name, isAlive);

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

    private void textChangedListener()
    {

        editText_mamaState.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_mamaDistrict.setText("");
                textView_mamaDistrictId.setText("0");
                editText_mamaTaluka.setText("");
                textView_mamaTalukaId.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editText_mamaDistrict.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                editText_mamaTaluka.setText("");
                textView_mamaTalukaId.setText("0");
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


    }
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

                else if(params[0].toString()=="State")
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),"StatesID",
                            "StatesName", editText_mamaState, textView_mamaStateId, context, customDialogLoadingProgressBar);


                }
                else if(params[0].toString()=="District")
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
