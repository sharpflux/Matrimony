package com.example.matrimonyapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.appyvet.materialrangebar.RangeBar;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.customViews.CustomDialogAddLocation;
import com.example.matrimonyapp.customViews.CustomDialogAddPercentage;
import com.example.matrimonyapp.customViews.CustomDialogAddSibling;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomDialogLocationRec;
import com.example.matrimonyapp.fragment.ProfessionalDetailsFragment;
import com.example.matrimonyapp.modal.AddLOcationModal;
import com.example.matrimonyapp.modal.DataFetcherLocation;
import com.example.matrimonyapp.modal.MultipleSelectionDataFetcher;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteSetpreference;
import com.example.matrimonyapp.utils.Constant;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.suke.widget.SwitchButton;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import java.util.ArrayList;

public class SetPreferencesActivity extends AppCompatActivity {

    private com.appyvet.materialrangebar.RangeBar rangeBar_salaryRange, rangeBar_ageRange, rangeBar_heightRange;

    private View include_toolbar;

    private Context context;

    private TextView textView_toolbarHeader, textView_ageRange, textView_salaryRange, textView_heightRange,
            textView_qualificationId, textView_maritalStatusId, textView_familyTypeId, textView_familyValuesId,
            textView_colorId, textView_occupationId, textView_religonId, textView_stateId, textView_cityId;

    private EditText editText_qualification, editText_maritalStatus, editText_familyType, editText_familyValues,
            editText_color, editText_occupation, editText_religon, editText_district, editText_stateNames,editText_caste;

    private RadioGroup radioGroup_diet;

    private CardView cardView_religion, cardView_caste;

    private ImageView imageView_back;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    private String diet;

    UserModel userModel;

    MultipleSelectionDataFetcher multipleSelectionDataFetcher;
    PopupFetcher popupFetcher;
    String gender;
    LinearLayout lr_state, lr_district;
    CustomDialogLocationRec customDialogLocationRec;
    DataFetcherLocation dataFetcherLocation;
    AddLOcationModal lOcationModal;
    ArrayList<AddLOcationModal> list;
    SwitchButton switchButton_otherCaste;
    SQLiteSetpreference sqLiteSetpreference;
    StringBuilder state_builder_id;
    String StateId="";

    ArrayList arrayList_stateId, arrayList_districtId, arrayList_talukaId, arrayList_religion, arrayList_caste;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);

        //Initializations
        context = getApplicationContext();
      //  list = new ArrayList<AddLOcationModal>();
        list = new ArrayList<>();
        dataFetcherLocation = new DataFetcherLocation(lOcationModal, customDialogLocationRec, list, SetPreferencesActivity.this);
        multipleSelectionDataFetcher = new MultipleSelectionDataFetcher("", context);
        arrayList_stateId = new ArrayList();
        arrayList_districtId = new ArrayList();
        arrayList_religion = new ArrayList();
        arrayList_caste = new ArrayList();

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(context).getUser();

        gender = userModel.getGender();
        sqLiteSetpreference = new SQLiteSetpreference(getApplicationContext());

        // Toolbar views initialization
        include_toolbar = findViewById(R.id.include_toolbar);
        textView_toolbarHeader = findViewById(R.id.textView_toolbarHeader);
        imageView_back = findViewById(R.id.imageView_back);
        textView_salaryRange = findViewById(R.id.textView_salaryRange);

        textView_stateId = findViewById(R.id.textView_stateId);
        editText_stateNames = findViewById(R.id.editText_stateNames);
        editText_qualification = findViewById(R.id.editText_qualification);
        editText_maritalStatus = findViewById(R.id.editText_maritalStatus);
        radioGroup_diet = findViewById(R.id.radioGroup_diet);
        editText_familyType = findViewById(R.id.editText_familyType);
        editText_familyValues = findViewById(R.id.editText_familyValues);
        editText_color = findViewById(R.id.editText_color);
        rangeBar_ageRange = findViewById(R.id.rangeBar_ageRange);
        rangeBar_salaryRange = findViewById(R.id.rangeBar_salaryRange);
        rangeBar_heightRange = findViewById(R.id.rangeBar_heightRange);
        textView_ageRange = findViewById(R.id.textView_ageRange);
        editText_caste = findViewById(R.id.editText_caste);

        textView_heightRange = findViewById(R.id.textView_heightRange);



        textView_qualificationId = findViewById(R.id.textView_qualificationId);
        textView_maritalStatusId = findViewById(R.id.textView_maritalStatusId);
        textView_familyTypeId = findViewById(R.id.textView_familyTypeId);
        textView_familyValuesId = findViewById(R.id.textView_familyValuesId);
        textView_colorId = findViewById(R.id.textView_colorId);
        editText_occupation = findViewById(R.id.editText_occupation);
        textView_occupationId = findViewById(R.id.textView_occupationId);
        editText_religon = findViewById(R.id.editText_religon);
        textView_religonId = findViewById(R.id.textView_religonId);
        lr_state = findViewById(R.id.lr_state);
        lr_district = findViewById(R.id.lr_district);
        cardView_religion = findViewById(R.id.cardView_religion);
        cardView_caste = findViewById(R.id.cardView_caste);
        //txt_state = findViewById(R.id.txt_state);
        //textView_addStateId = findViewById(R.id.textView_addStateId);
        editText_district = findViewById(R.id.editText_district);
        textView_cityId = findViewById(R.id.textView_cityId);
        switchButton_otherCaste = findViewById(R.id.switchButton_otherCaste);


        include_toolbar.setBackgroundColor(Color.WHITE);
        textView_toolbarHeader.setText("Set Preferences");
        state_builder_id = new StringBuilder();

        rangeBarChangeListener("Age", rangeBar_ageRange, textView_ageRange);
        rangeBarChangeListener("Salary ", rangeBar_salaryRange, textView_salaryRange);
        rangeBarChangeListener("Height", rangeBar_heightRange, textView_heightRange);



        popupFetcher = new PopupFetcher(context);


        editText_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("SiblingRelation");
            }
        });


        popupMenuEditText(editText_maritalStatus, "MaritalStatus", textView_maritalStatusId.getText().toString());
        popupMenuEditText(editText_familyType, "FamilyType", textView_familyTypeId.getText().toString());
        popupMenuEditText(editText_familyValues, "FamilyValues", textView_familyValuesId.getText().toString());
        popupMenuEditText(editText_color, "Color", textView_colorId.getText().toString());
        popupMenuEditText(editText_occupation, "Occupation", textView_occupationId.getText().toString());
        //popupMenuEditText(editText_religon, "Religion", textView_religonId.getText().toString());
        popupMenuEditText(editText_qualification, "Qualification", textView_qualificationId.getText().toString());

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        lr_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("State");

            }
        });



        lr_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("District");
            }
        });

        cardView_religion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("Religion");
            }
        });


        cardView_caste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("Caste");
            }
        });

    }

    public void rangeBarChangeListener(final String bar, RangeBar rangeBar, final TextView textView) {
        rangeBar.setSelected(true);

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                if (bar.equalsIgnoreCase("Age")) {
                    textView.setText(leftPinValue + " - " + rightPinValue);
                } else if (bar.equalsIgnoreCase("Salary")) {
                    textView.setText(leftPinValue + "L - " + rightPinValue + "L");
                } else if (bar.equals("Height")) {
                    textView.setText(leftPinValue + "ft - " + rightPinValue + "ft");
                }
            }

            @Override
            public void onTouchStarted(RangeBar rangeBar) {

            }

            @Override
            public void onTouchEnded(RangeBar rangeBar) {

            }
        });
    }

    public void popupMenuEditText(final EditText editText, final String urlFor, String id) {


        AsyncTaskLoad runner = new AsyncTaskLoad();
        runner.execute(urlFor, id);




    }


    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {



                if (params[0] == "MaritalStatus") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_MARITALSTATUS + "Language=" + userModel.getLanguage(),
                            "MaritalStatusId", "MaritalStatusName", editText_maritalStatus,
                            textView_maritalStatusId, context, R.style.MyCustomPopupMenu, id);
                } else if (params[0] == "FamilyType") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_FAMILYTYPE + "Language=" + userModel.getLanguage(),
                            "FamilyTypeId", "FamilyTypeName", editText_familyType,
                            textView_familyTypeId, context, R.style.MyCustomPopupMenu, id);
                } else if (params[0] == "FamilyValues") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_FAMILYVALUES + "Language=" + userModel.getLanguage(),
                            "FamilyValuesId", "FamilyValuesName", editText_familyValues,
                            textView_familyValuesId, context, R.style.MyCustomPopupMenu, id);
                } else if (params[0] == "Color") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_SKINCOLOR + "Language=" + userModel.getLanguage(),
                            "SkinColourId", "SkinColourName", editText_color,
                            textView_colorId, context, R.style.MyCustomPopupMenu, id);
                } else if (params[0] == "Occupation") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_OCCUPATION + "Language=" + userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_occupation,
                            textView_occupationId, context, R.style.MyCustomPopupMenu, id);
                }  else if (params[0] == "Qualification") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_QUALIFICATIONLEVEL + "Language=" + userModel.getLanguage(),
                            "QualificationLevelId", "QualificationLevelName", editText_qualification,
                            textView_qualificationId, context, R.style.MyCustomPopupMenu, id);
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

        }


        @Override
        protected void onProgressUpdate(String... text) {
        }

    }

    class AsyncTaskRunner extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String[] params) {

            if (params[0] == "Religion") {
                    /*int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_RELIGION + "Language=" + userModel.getLanguage(),
                            "ReligionID", "ReligionName", editText_religon,
                            textView_religonId, context, R.style.MyCustomPopupMenu, id);*/
                multipleSelectionDataFetcher.loadList(URLs.URL_GET_RELIGION+"Language="+userModel.getLanguage(),
                        "ReligionID", "ReligionName", editText_religon, arrayList_religion,
                        SetPreferencesActivity.this, customDialogLoadingProgressBar);


            }

            else if (params[0].equals("Caste")) {

                String religionId = arrayList_religion.toString().substring(1,arrayList_religion.toString().length()-1).replaceAll(" ","");


                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_CASTE+"ReligionId="+religionId+"&Language="
                                +userModel.getLanguage(), "CasteId", "CasteName", editText_caste,
                        arrayList_caste, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }




            else if (params[0].equals("State")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_STATE + "Language=" + userModel.getLanguage(),
                        "StatesID", "StatesName",editText_stateNames, arrayList_stateId,
                        SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("District")) {

                String statesId = arrayList_stateId.toString().substring(1,arrayList_stateId.toString().length()-1).replaceAll(" ","");


                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_DISTRICT+"StatesId="+statesId+"&Language="
                                +userModel.getLanguage(), "DistrictId", "DistrictName", editText_district,
                        arrayList_districtId, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }




            return null;
        }


        @Override
        protected void onPreExecute() {
            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(SetPreferencesActivity.this);
            customDialogLoadingProgressBar.show();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }



}
