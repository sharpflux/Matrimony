package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import java.util.Map;

public class CustomDialogAddSibling extends Dialog {



    public Context context;

    public EditText editText_siblingRelation;

    public TextView textView_login, textView_createNew, textView_relationId;
    public RadioGroup radioGroup_marriageStatus;
    public RadioButton radioButton_married, radioButton_unmarried;

    private LinearLayout linearLayout_fatherInLaw;
    private String marriageSatus;

    public PopupMenu popupMenu;

    Map<String, Integer> list;

    private TextView textView_stateId, textView_districtId, textView_talukaId;
    private EditText editText_state, editText_taluka, editText_district;

    DataFetcher dataFetcher;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    PopupFetcher popupFetcher;
    UserModel userModel;
    public String urlFor;

    public CustomDialogAddSibling(Context context) {
        super(context);
        this.context = context;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_add_sibling);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        editText_siblingRelation = findViewById(R.id.editText_siblingRelation);
        textView_relationId = findViewById(R.id.textView_relationId);
        radioGroup_marriageStatus = findViewById(R.id.radioGroup_marriageStatus);
        radioButton_married = findViewById(R.id.radioButton_married);
        radioButton_unmarried = findViewById(R.id.radioButton_unmarried);
        linearLayout_fatherInLaw = findViewById(R.id.linearLayout_fatherInLaw);

        editText_state= findViewById(R.id.editText_state);
        editText_taluka = findViewById(R.id.editText_taluka);
        editText_district = findViewById(R.id.editText_district);

        textView_stateId = findViewById(R.id.textView_stateId);
        textView_districtId= findViewById(R.id.textView_districtId);
        textView_talukaId = findViewById(R.id.textView_talukaId);


        FieldValidation.radioGroupValidation(radioGroup_marriageStatus);
        radioGroup_marriageStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

            /*    int id = radioGroup.getCheckedRadioButtonId();
                RadioButton rb = findViewById(id);
                marriageSatus= rb.getText().toString();*/

                if(id==R.id.radioButton_married){
                    //radioButton_unmarried.setChecked(false);
                    linearLayout_fatherInLaw.setVisibility(View.VISIBLE);
                }
                else{
                    //radioButton_married.setChecked(false);
                    linearLayout_fatherInLaw.setVisibility(View.GONE);
                }

            }
        });


        dataFetcher = new DataFetcher("Address",context);


        showPopUp(editText_state, "State");
        showPopUp(editText_district, "District");
        showPopUp(editText_taluka, "Taluka");


        popupFetcher = new PopupFetcher(context);


        AsyncTaskLoad runner = new AsyncTaskLoad();
        runner.execute("SiblingRelation");


        textChangedListener();




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
                }

                if(urlFor.equals("Taluka"))
                {
                    id = textView_districtId.getText().toString();
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
                    popupFetcher.loadList(URLs.URL_GET_SIBLINGSLIST+"Language="+userModel.getLanguage(),
                            "SiblingListId","SiblingListName",editText_siblingRelation,
                            textView_relationId,context, R.style.MyCustomPopupMenu2);
                }
                else if(params[0].toString()=="State")
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
