package com.example.matrimonyapp.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
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
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.DataFetcherLocation;
import com.example.matrimonyapp.modal.MultipleSelectionDataFetcher;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteSetPreference;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.suke.widget.SwitchButton;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SetPreferencesActivity extends AppCompatActivity {

    private com.appyvet.materialrangebar.RangeBar rangeBar_salaryRange, rangeBar_ageRange, rangeBar_heightRange;

    private View include_toolbar;

    private Context context;

    private TextView textView_toolbarHeader, textView_ageRange, textView_salaryRange, textView_heightRange,
            textView_qualificationId, textView_maritalStatusId, textView_familyTypeId, textView_familyValuesId,
            textView_colorId, textView_occupationId, textView_religonId, textView_stateId, textView_cityId,
            textView_setPreferences;

    private EditText editText_highestQualificationLevel, editText_qualification, editText_maritalStatus, editText_familyType, editText_familyValues,
            editText_color, editText_occupation, editText_religon, editText_taluka, editText_district, editText_stateNames,
            editText_caste, editText_subCaste, editText_diet, editText_individualIncome, editText_familyIncome;

    private RadioGroup radioGroup_gender, radioGroup_serviceType, radioGroup_workingLocation, radioGroup_jobType;

    private CardView cardView_religion, cardView_caste,cardView_subcaste;

    private ImageView imageView_back;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    private String diet;

    UserModel userModel;

    MultipleSelectionDataFetcher multipleSelectionDataFetcher;
    PopupFetcher popupFetcher;
    String gender;
    LinearLayout lr_state, lr_district, lr_taluka;
    CustomDialogLocationRec customDialogLocationRec;
    DataFetcherLocation dataFetcherLocation;
    AddLOcationModal lOcationModal;
    ArrayList<AddLOcationModal> list;
    SwitchButton switchButton_otherCaste;
    SQLiteSetPreference sqLiteSetPreference;
    StringBuilder state_builder_id;
    String StateId="";

    ArrayList arrayList_stateId, arrayList_districtId, arrayList_talukaId, arrayList_religion,
            arrayList_caste, arrayList_SubCaste, arrayList_maritalStatus, arrayList_familyType,
            arrayList_diet, arrayList_occupation, arrayList_individualIncome, arrayList_familyIncome,
            arrayList_familyValues, arrayList_color, arrayList_highestQualificationLevel, arrayList_qualification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_preferences);


        //Initializations

        init();


        radioGroup_serviceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton_service)
                {
                    radioGroup_workingLocation.setVisibility(View.VISIBLE);
                }
                else
                {
                    radioGroup_jobType.setVisibility(View.GONE);
                    radioGroup_workingLocation.setVisibility(View.GONE);
                    radioGroup_workingLocation.clearCheck();
                    radioGroup_jobType.clearCheck();
                }
            }
        });

        radioGroup_workingLocation.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId==R.id.radioButton_workingInIndia)
                {
                    radioGroup_jobType.setVisibility(View.VISIBLE);
                }
                else
                {
                    radioGroup_jobType.setVisibility(View.GONE);
                    radioGroup_jobType.clearCheck();
                }
            }
        });


        rangeBarChangeListener("Age", rangeBar_ageRange, textView_ageRange);
      //  rangeBarChangeListener("Salary ", rangeBar_salaryRange, textView_salaryRange);
        rangeBarChangeListener("Height", rangeBar_heightRange, textView_heightRange);



        popupFetcher = new PopupFetcher(context);


        editText_qualification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("SiblingRelation");
            }
        });


        //popupMenuEditText(editText_maritalStatus, "MaritalStatus", textView_maritalStatusId.getText().toString());
        //popupMenuEditText(editText_familyType, "FamilyType", textView_familyTypeId.getText().toString());
        //popupMenuEditText(editText_familyValues, "FamilyValues", textView_familyValuesId.getText().toString());
        //popupMenuEditText(editText_color, "Color", textView_colorId.getText().toString());
        //popupMenuEditText(editText_occupation, "Occupation", textView_occupationId.getText().toString());
        //popupMenuEditText(editText_religon, "Religion", textView_religonId.getText().toString());
        //popupMenuEditText(editText_qualification, "Qualification", textView_qualificationId.getText().toString());


        onClickListener();






    }

    private void init() {


        context = getApplicationContext();
        //  list = new ArrayList<AddLOcationModal>();
        list = new ArrayList<>();
        dataFetcherLocation = new DataFetcherLocation(lOcationModal, customDialogLocationRec, list, SetPreferencesActivity.this);
        multipleSelectionDataFetcher = new MultipleSelectionDataFetcher("", context);
        arrayList_stateId = new ArrayList();
        arrayList_districtId = new ArrayList();
        arrayList_talukaId = new ArrayList();
        arrayList_religion = new ArrayList();
        arrayList_caste = new ArrayList();
        arrayList_SubCaste = new ArrayList();
        arrayList_maritalStatus = new ArrayList();
        arrayList_diet = new ArrayList();
        arrayList_individualIncome = new ArrayList();
        arrayList_familyIncome = new ArrayList();
        arrayList_familyType = new ArrayList();
        arrayList_familyValues = new ArrayList();
        arrayList_color = new ArrayList();
        arrayList_occupation = new ArrayList();
        arrayList_highestQualificationLevel = new ArrayList();
        arrayList_qualification = new ArrayList();

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(context).getUser();

        gender = userModel.getGender();
        sqLiteSetPreference = new SQLiteSetPreference(getApplicationContext());

        // Toolbar views initialization
        include_toolbar = findViewById(R.id.include_toolbar);
        textView_toolbarHeader = findViewById(R.id.textView_toolbarHeader);
        imageView_back = findViewById(R.id.imageView_back);
        //  textView_salaryRange = findViewById(R.id.textView_salaryRange);

        radioGroup_gender = findViewById(R.id.radioGroup_gender);
        textView_stateId = findViewById(R.id.textView_stateId);
        editText_stateNames = findViewById(R.id.editText_stateNames);
        editText_highestQualificationLevel = findViewById(R.id.editText_highestQualificationLevel);
        editText_qualification = findViewById(R.id.editText_qualification);
        editText_maritalStatus = findViewById(R.id.editText_maritalStatus);
        editText_diet = findViewById(R.id.editText_diet);
        editText_familyType = findViewById(R.id.editText_familyType);
        editText_familyValues = findViewById(R.id.editText_familyValues);
        editText_color = findViewById(R.id.editText_color);
        rangeBar_ageRange = findViewById(R.id.rangeBar_ageRange);
        editText_individualIncome = findViewById(R.id.editText_individualIncome);
        editText_familyIncome = findViewById(R.id.editText_familyIncome);
        //  rangeBar_salaryRange = findViewById(R.id.rangeBar_salaryRange);
        rangeBar_heightRange = findViewById(R.id.rangeBar_heightRange);
        textView_ageRange = findViewById(R.id.textView_ageRange);
        editText_caste = findViewById(R.id.editText_caste);

        textView_heightRange = findViewById(R.id.textView_heightRange);
        editText_subCaste = findViewById(R.id.editText_subCaste);



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
        lr_taluka = findViewById(R.id.lr_taluka);
        cardView_religion = findViewById(R.id.cardView_religion);
        cardView_caste = findViewById(R.id.cardView_caste);
        cardView_subcaste = findViewById(R.id.cardView_subcaste);

        editText_district = findViewById(R.id.editText_district);
        editText_taluka = findViewById(R.id.editText_taluka);
        textView_cityId = findViewById(R.id.textView_cityId);

        textView_setPreferences = findViewById(R.id.textView_setPreferences);
        radioGroup_serviceType = findViewById(R.id.radioGroup_serviceType);
        radioGroup_workingLocation = findViewById(R.id.radioGroup_workingLocation);
        radioGroup_jobType = findViewById(R.id.radioGroup_jobType);


        include_toolbar.setBackgroundColor(Color.WHITE);
        textView_toolbarHeader.setText("Set Preferences");
        state_builder_id = new StringBuilder();


    }

    public void rangeBarChangeListener(final String bar, RangeBar rangeBar, final TextView textView) {
        rangeBar.setSelected(true);

        rangeBar.setOnRangeBarChangeListener(new RangeBar.OnRangeBarChangeListener() {
            @Override
            public void onRangeChangeListener(RangeBar rangeBar, int leftPinIndex, int rightPinIndex, String leftPinValue, String rightPinValue) {
                if (bar.equalsIgnoreCase("Age")) {
                    textView.setText(leftPinValue + " - " + rightPinValue);
                }  else if (bar.equals("Height")) {
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

    private void onClickListener()
    {

        textView_setPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               /* AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("insertDetails");
*/

               insertDetails();
            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
       /* lr_state.setOnClickListener(new View.OnClickListener() {
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
        });*/

/*        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("");
            }
        });


        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("");
            }
        });

        .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute("");
            }
        });
*/
        multipleSelecttionMenu(editText_maritalStatus, "MaritalStatus","0");
        multipleSelecttionMenu(editText_color, "Color", "0");
        multipleSelecttionMenu(editText_familyValues, "FamilyValues", "0");
        multipleSelecttionMenu(editText_familyType, "FamilyType", "0");
        multipleSelecttionMenu(editText_occupation, "Occupation", "0");
        multipleSelecttionMenu(editText_diet, "Diet", "0");
        multipleSelecttionMenu(editText_individualIncome, "IndividualIncome", "0");
        multipleSelecttionMenu(editText_familyIncome, "FamilyIncome", "0");
        multipleSelecttionMenu(editText_highestQualificationLevel, "HighestQualificationLevel", "0");
        multipleSelecttionMenu(editText_qualification, "Qualification", "0");


        multipleSelecttionMenu(editText_subCaste, "SubCaste", "0");
        multipleSelecttionMenu(editText_caste, "Caste", "0");
        multipleSelecttionMenu(editText_religon, "Religion", "0");

        multipleSelecttionMenu(editText_stateNames, "State", "0");
        multipleSelecttionMenu(editText_district, "District", "0");
        multipleSelecttionMenu(editText_taluka, "Taluka", "0");

    }

    public void multipleSelecttionMenu(final View view, final String urlFor, final String id)
    {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute(urlFor, id);

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


/*
                if (params[0] == "MaritalStatus") {
                    int id = Integer.parseInt(params[1].toString());
                    popupFetcher.loadList(URLs.URL_GET_MARITALSTATUS + "Language=" + userModel.getLanguage(),
                            "MaritalStatusId", "MaritalStatusName", editText_maritalStatus,
                            textView_maritalStatusId, context, R.style.MyCustomPopupMenu, id);
                } else*/ if (params[0] == "FamilyType") {
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

            if(params[0].equals("insertDetails"))
            {
                insertDetails();
            }
            else if (params[0] == "Religion") {
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

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_CASTE+"ReligionId="+religionId+
                        "&Language=" +userModel.getLanguage(), "CasteId", "CasteName",
                        editText_caste, arrayList_caste, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }


            else if (params[0].equals("SubCaste")) {

                String casteId = arrayList_caste.toString().substring(1,arrayList_caste.toString().length()-1).replaceAll(" ","");


                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_SUBCASTE+"CasteId="+casteId+"&Language="
                        +userModel.getLanguage(), "SubCasteId", "SubCasteName",editText_subCaste,
                        arrayList_SubCaste, SetPreferencesActivity.this, customDialogLoadingProgressBar);

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
            else if (params[0].equals("Taluka")) {

                String districtsId = arrayList_districtId.toString().substring(1,arrayList_districtId.toString().length()-1).replaceAll(" ","");


                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_TALUKA+"DistrictId="+districtsId+",&Language="
                        +userModel.getLanguage(), "TalukasId", "TalukaName", editText_taluka,
                        arrayList_talukaId, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }

            else if (params[0].equals("MaritalStatus")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MARITALSTATUS + "Language=" +userModel.getLanguage(),
                        "MaritalStatusId", "MaritalStatusName", editText_maritalStatus,
                         arrayList_maritalStatus, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("FamilyType")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_FAMILYTYPE+ "Language=" +userModel.getLanguage(),
                        "FamilyTypeId", "FamilyTypeName", editText_familyType,
                        arrayList_familyType, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }

            else if (params[0].equals("FamilyValues")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_FAMILYVALUES+ "Language=" +userModel.getLanguage(),
                        "FamilyValuesId", "FamilyValuesName", editText_familyValues,
                        arrayList_familyValues, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("Occupation")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_OCCUPATION+ "Language=" +userModel.getLanguage(),
                        "OccupationId", "OccupationName", editText_occupation,
                        arrayList_occupation, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("Color")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_SKINCOLOR+ "Language=" +userModel.getLanguage(),
                        "SkinColourId", "SkinColourName", editText_color,
                        arrayList_color, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("Diet")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_DIET+ "Language=" +userModel.getLanguage(),
                        "DietId","DietName", editText_diet,
                        arrayList_diet, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("IndividualIncome")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_SALARY+ "Language=" +userModel.getLanguage(),
                        "SalaryPackageId", "SalaryPackageName", editText_individualIncome,
                        arrayList_individualIncome, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("FamilyIncome")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_SALARY+ "Language=" +userModel.getLanguage(),
                        "SalaryPackageId", "SalaryPackageName", editText_familyIncome,
                        arrayList_familyIncome, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("HighestQualificationLevel")) {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_QUALIFICATIONLEVEL+ "Language=" +userModel.getLanguage(),
                        "QualificationLevelId","QualificationLevelName", editText_highestQualificationLevel,
                        arrayList_highestQualificationLevel, SetPreferencesActivity.this, customDialogLoadingProgressBar);

            }
            else if (params[0].equals("Qualification")) {

                String levelId = arrayList_highestQualificationLevel.toString().substring(1,arrayList_highestQualificationLevel.toString().length()-1).replaceAll(" ","");
               // QualificationIds=1,2,&Language=en
                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_QUALIFICATION +
                        "QualificationIds=" + levelId+ ",&Language=" +userModel.getLanguage(),
                        "QualificationId","Qualification", editText_qualification,
                        arrayList_qualification, SetPreferencesActivity.this, customDialogLoadingProgressBar);

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

    private void insertDetails()
    {
        final String gender = ((RadioButton)findViewById(radioGroup_gender.getCheckedRadioButtonId())).getText().toString();

        final String stateIds = arrayList_stateId.toString().substring(1,arrayList_stateId.toString().length()-1).replaceAll(" ","");
        final String districtsIds = arrayList_districtId.toString().substring(1,arrayList_districtId.toString().length()-1).replaceAll(" ","");
        final String talukaIds = arrayList_talukaId.toString().substring(1,arrayList_talukaId.toString().length()-1).replaceAll(" ","");
        final String maritalStatusIds = arrayList_maritalStatus.toString().substring(1,arrayList_maritalStatus.toString().length()-1).replaceAll(" ","");
        final String familyTypeIds = arrayList_familyType.toString().substring(1,arrayList_familyType.toString().length()-1).replaceAll(" ","");
        final String familyValueIds= arrayList_familyValues.toString().substring(1,arrayList_familyValues.toString().length()-1).replaceAll(" ","");
        final String familyIncomeIds = arrayList_familyIncome.toString().substring(1,arrayList_familyIncome.toString().length()-1).replaceAll(" ","");
        final String individualIncomeIds = arrayList_individualIncome.toString().substring(1,arrayList_individualIncome.toString().length()-1).replaceAll(" ","");
        final String qualificationLevelIds = arrayList_highestQualificationLevel.toString().substring(1,arrayList_highestQualificationLevel.toString().length()-1).replaceAll(" ","");
        final String qualificationIds = arrayList_qualification.toString().substring(1,arrayList_qualification.toString().length()-1).replaceAll(" ","");
        final String dietIds = arrayList_diet.toString().substring(1,arrayList_diet.toString().length()-1).replaceAll(" ","");
        final String colorIds = arrayList_color.toString().substring(1,arrayList_color.toString().length()-1).replaceAll(" ","");
        final String occupationIds = arrayList_occupation.toString().substring(1,arrayList_occupation.toString().length()-1).replaceAll(" ","");
        final String religionIds = arrayList_religion.toString().substring(1,arrayList_religion.toString().length()-1).replaceAll(" ","");
        final String casteIds = arrayList_caste.toString().substring(1,arrayList_caste.toString().length()-1).replaceAll(" ","");
        final String subCasteIds = arrayList_SubCaste.toString().substring(1,arrayList_SubCaste.toString().length()-1).replaceAll(" ","");

        String ageMin = rangeBar_ageRange.getLeftPinValue();
        String ageMax = rangeBar_ageRange.getRightPinValue();
        String heightMin = rangeBar_heightRange.getLeftPinValue();
        String heightMax = rangeBar_heightRange.getRightPinValue();

        long res=0;

        if(sqLiteSetPreference.isPreferenceExistByUserId(userModel.getUserId()))
        {
            res = sqLiteSetPreference.updateSetPreference(userModel.getUserId(), gender, stateIds, districtsIds, talukaIds, ageMin, ageMax,
                    religionIds, casteIds, subCasteIds, qualificationLevelIds, qualificationIds, maritalStatusIds, dietIds, occupationIds,
                    individualIncomeIds, familyIncomeIds, familyTypeIds, familyValueIds, heightMin, heightMax, colorIds);
        }
        else {
            res = sqLiteSetPreference.insertSetPreference(userModel.getUserId(), gender, stateIds, districtsIds, talukaIds, ageMin, ageMax,
                    religionIds, casteIds, subCasteIds, qualificationLevelIds, qualificationIds, maritalStatusIds, dietIds, occupationIds,
                    individualIncomeIds, familyIncomeIds, familyTypeIds, familyValueIds, heightMin, heightMax, colorIds);
        }
        if (res != -1) {
            Toast.makeText(SetPreferencesActivity.this, "Value added/updated & id is " + res, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(SetPreferencesActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();

        /*     StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URLs.URL_POST_FILTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {

                                Log.d("RESPONSE",response);
                                //converting response to json object
                                JSONObject jsonObject = new JSONObject(response);

                                JSONArray jsonArray = jsonObject.getJSONArray("Registrations");

                                if(jsonArray.length()>0)
                                {
                                    //   getDetails();
                                    ///getProfilePic();

                                    Toast.makeText(SetPreferencesActivity.this,"Received successfully!", Toast.LENGTH_SHORT).show();

                                }
                                else
                                {
                                    Toast.makeText(SetPreferencesActivity.this,"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SetPreferencesActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                            error.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();

                    // params.put("UserId",userModel.getUserId());                 //1
                    params.put("PageIndex", "1");                               //2
                    params.put("PageSize", "100");                              //3
                    params.put("Gender", gender);                               //4
                    params.put("StateId", stateIds);                            //5
                    params.put("DistrictId", districtsIds);                     //6
                    params.put("TalukasId", talukaIds);                         //7
                    params.put("AgeMin", rangeBar_ageRange.getLeftPinValue());  //8
                    params.put("AgeMax", rangeBar_ageRange.getRightPinValue()); //9
                    params.put("ReligionId", religionIds);                      //10
                    params.put("CasteId", casteIds);                            //11
                    params.put("SubCastId", subCasteIds);                       //12
                    params.put("HightestQulificationLevelIds", qualificationLevelIds);  //13
                    params.put("HightestQulificationIds", qualificationIds);    //14
                    params.put("MaritalStatusIds", maritalStatusIds);            //15
                    params.put("DietsIds", dietIds);                            //16
                    params.put("OccupationsIds", occupationIds);                //17
                    params.put("ExpectedFamilyIncome", familyIncomeIds);        //18
                    params.put("ExpectedIndividualIncome", individualIncomeIds);//19
                    params.put("FamilyType", familyTypeIds);                    //20
                    params.put("FamilyValue", familyValueIds);                  //21
                    params.put("Height", rangeBar_heightRange.getLeftPinValue());//22
                    params.put("HeightMax", rangeBar_heightRange.getRightPinValue());   //23
                    params.put("SkinColourName", colorIds);                     //24

                    return params;
                }
            };

            VolleySingleton.getInstance(SetPreferencesActivity.this).addToRequestQueue(stringRequest);

*/


        } else {
            Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

        }

        }




}
