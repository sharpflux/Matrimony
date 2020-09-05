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

import com.abdallahalaraby.blink.Screenshot;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;
import java.util.Map;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

public class CustomDialogAddSibling extends Dialog {



    public Context context;

    public EditText editText_siblingRelation, editText_occupation, editText_qualification,
            editText_siblingName, editText_siblingMobileNo, editText_fatherInLawName, editText_fatherInLawMobileNo,
            editText_fatherInLawVillage;

    public TextView textView_title, textView_login, textView_createNew, textView_relationId, textView_occupationId, textView_qualificationId;
    public RadioGroup radioGroup_marriageStatus;
    public RadioButton radioButton_married, radioButton_unmarried;

    public ImageView imageView_back;

    private LinearLayout linearLayout_fatherInLaw;
    private String marriageSatus;

    public PopupMenu popupMenu;

    Map<String, Integer> list;

    private TextView textView_stateId, textView_districtId, textView_talukaId, textView_addSibling;
    private EditText editText_state, editText_taluka, editText_district;

    private SQLiteSiblingDetails sqLiteSiblingDetails;

    DataFetcher dataFetcher;

    String id, sibling_details_id;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    PopupFetcher popupFetcher;
    UserModel userModel;
    public String urlFor;
    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;
    CardView cardView_sibling_relation;

    public CustomDialogAddSibling(Context context, String id, String sibling_details_id, AddPersonAdapter addPersonAdapter,
                                  ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.sibling_details_id = sibling_details_id;
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



        setContentView(R.layout.custom_dialog_add_sibling);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        sqLiteSiblingDetails = new SQLiteSiblingDetails(context);


        setCanceledOnTouchOutside(true);
        //getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textView_title = findViewById(R.id.textView_title);
        imageView_back = findViewById(R.id.imageView_back);
        editText_siblingRelation = findViewById(R.id.editText_siblingRelation);
        textView_relationId = findViewById(R.id.textView_relationId);
        editText_siblingName = findViewById(R.id.editText_siblingName);
        editText_siblingMobileNo = findViewById(R.id.editText_siblingMobileNo);
        editText_qualification = findViewById(R.id.editText_siblingQualification);
        textView_qualificationId = findViewById(R.id.textView_qualificationId);
        editText_occupation = findViewById(R.id.editText_siblingOccupation);
        textView_occupationId = findViewById(R.id.textView_occupationId);
        radioGroup_marriageStatus = findViewById(R.id.radioGroup_marriageStatus);
        radioButton_married = findViewById(R.id.radioButton_married);
        radioButton_unmarried = findViewById(R.id.radioButton_unmarried);
        linearLayout_fatherInLaw = findViewById(R.id.linearLayout_fatherInLaw);
        editText_fatherInLawName = findViewById(R.id.editText_fatherInLawName);
        editText_fatherInLawMobileNo = findViewById(R.id.editText_fatherInLawMobileNo);
        editText_fatherInLawVillage = findViewById(R.id.editText_fatherInLawVillage);


        textView_addSibling = findViewById(R.id.textView_addSibling);

        editText_state= findViewById(R.id.editText_state);
        editText_taluka = findViewById(R.id.editText_taluka);
        editText_district = findViewById(R.id.editText_district);

        textView_stateId = findViewById(R.id.textView_stateId);
        textView_districtId= findViewById(R.id.textView_districtId);
        textView_talukaId = findViewById(R.id.textView_talukaId);
        cardView_sibling_relation = findViewById(R.id.cardView_sibling_relation);


        textView_title.setText("Sibling Details");
        imageView_back.setVisibility(View.GONE);


        marriageSatus = ((RadioButton)findViewById(radioGroup_marriageStatus.getCheckedRadioButtonId())).getText().toString();
        radioGroup_marriageStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

            /*    int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = findViewById(id);
                marriageSatus= rb.getText().toString();*/

                if(id==R.id.radioButton_married){
                    //radioButton_unmarried.setChecked(false);
                    linearLayout_fatherInLaw.setVisibility(View.VISIBLE);
                    editText_fatherInLawName.requestFocus();
                }
                else{
                    //radioButton_married.setChecked(false);
                    linearLayout_fatherInLaw.setVisibility(View.GONE);
                    editText_fatherInLawName.setText("");
                    editText_fatherInLawName.clearFocus();
                    editText_fatherInLawMobileNo.setText("");
                    editText_fatherInLawVillage.setText("");
                    editText_state.setText("");
                    textView_stateId.setText("");
                    editText_district.setText("");
                    textView_districtId.setText("");
                    editText_taluka.setText("");
                    textView_talukaId.setText("");

                }

            }
        });


        dataFetcher = new DataFetcher("Address",context);


        showPopUp(editText_state, "State");
        showPopUp(editText_district, "District");
        showPopUp(editText_taluka, "Taluka");


        popupFetcher = new PopupFetcher(context);


        AsyncTaskLoad runner = new AsyncTaskLoad();
        runner.execute("SiblingRelation", textView_relationId.getText().toString());


        textChangedListener();

        onClickListener();

        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteSiblingDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                editText_siblingName.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.NAME)));
                editText_siblingMobileNo.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.MOBILE_NO)));
                textView_qualificationId.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.EDUCATION_ID)));
                editText_qualification.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.EDUCATION_NAME)));
                textView_occupationId.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.OCCUPATION_ID)));
                editText_occupation.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.OCCUPATION_NAME)));
                editText_fatherInLawName.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_NAME)));
                editText_fatherInLawMobileNo.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_MOBILE_NO)));
                editText_fatherInLawVillage.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_VILLAGE)));
                textView_stateId.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_STATE_ID)));
                editText_state.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_STATE_NAME)));
                textView_districtId.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_DISTRICT_ID)));
                editText_district.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_DISTRICT_NAME)));
                textView_talukaId.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_TALUKA_ID)));
                editText_taluka.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_TALUKA_NAME)));

                FieldValidation.setRadioButtonAccToValue(radioGroup_marriageStatus,cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.MARITAL_STATUS)));

                if(radioButton_unmarried.isChecked())
                {
                    linearLayout_fatherInLaw.setVisibility(View.GONE);
                }
                else
                {
                    linearLayout_fatherInLaw.setVisibility(View.VISIBLE);
                }


                textView_relationId.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.RELATION_ID)));
                editText_siblingRelation.setText(cursor.getString(cursor.getColumnIndex(SQLiteSiblingDetails.RELATION)));

            }

            cursor.close();

        }

    }


    private void onClickListener()
    {
        editText_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("QualificationName");
            }
        });

        editText_occupation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("Occupation");
            }
        });


        textView_addSibling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name = editText_siblingName.getText().toString().trim();
                String mobileNo = editText_siblingMobileNo.getText().toString().trim();
                String qualificationId = textView_qualificationId.getText().toString().trim();
                String qualificationName = editText_qualification.getText().toString().trim();
                String occupationId = textView_occupationId.getText().toString().trim();
                String occupationName = editText_occupation.getText().toString().trim();
                String maritalStatus = ((RadioButton)findViewById(radioGroup_marriageStatus.getCheckedRadioButtonId())).getText().toString();;//getText().toString().trim();
                String relationId = textView_relationId.getText().toString().trim();
                String relation = editText_siblingRelation.getText().toString().trim();
                String fil_name = editText_fatherInLawName.getText().toString().trim();
                String fil_mobileNo = editText_fatherInLawMobileNo.getText().toString().trim();
                String fil_village = editText_fatherInLawVillage.getText().toString().trim();
                String fil_state_id = textView_stateId.getText().toString().trim();
                String fil_district_id = textView_districtId.getText().toString().trim();
                String fil_taluka_id = textView_talukaId.getText().toString().trim();
                String fil_state_name = editText_state.getText().toString().trim();
                String fil_district_name = editText_district.getText().toString().trim();
                String fil_taluka_name = editText_taluka.getText().toString().trim();




                if(id.equals("0")) {
                    long res = sqLiteSiblingDetails.insertSibling("0", name, mobileNo, qualificationId, qualificationName,
                            occupationId, occupationName, maritalStatus,
                            relationId, relation, fil_name, fil_mobileNo, fil_village,
                            fil_state_id, fil_district_id, fil_taluka_id,
                            fil_state_name, fil_district_name, fil_taluka_name);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res), "0", name, qualificationName));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteSiblingDetails.updateSibling(id, sibling_details_id, name, mobileNo, qualificationId, qualificationName,
                        occupationId, occupationName, maritalStatus,
                        relationId, relation, fil_name, fil_mobileNo, fil_village,
                        fil_state_id, fil_district_id, fil_taluka_id,
                        fil_state_name, fil_district_name, fil_taluka_name);

                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), sibling_details_id, name, qualificationName));
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

                if(params[0]=="SiblingRelation")
                {
                    int id = Integer.parseInt(params[1].toString());


                    popupFetcher.loadList(URLs.URL_GET_SIBLINGSLIST+"Language="+userModel.getLanguage(),
                            "SiblingListId","SiblingListName",editText_siblingRelation,
                            textView_relationId,context, R.style.MyCustomPopupMenu2, id);
                }
                else if(params[0].toString().equals("QualificationName"))
                {
                    dataFetcher.loadList(URLs.URL_GET_QUALIFICATIONNAME+"QualificationLevelId=1"+
                                    "&Language="+userModel.getLanguage(),"QualificationId",
                            "Qualification", editText_qualification, textView_qualificationId,context,
                            customDialogLoadingProgressBar);

                }

                else if(params[0].toString().equals("Occupation"))
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId",
                            "OccupationName", editText_occupation,
                            textView_occupationId, context, customDialogLoadingProgressBar);

                }

               /* else if(params[0].toString()=="State")
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),"StatesID",
                            "StatesName", editText_state, textView_stateId,context, customDialogLoadingProgressBar);


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




}
