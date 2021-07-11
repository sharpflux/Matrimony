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
import com.example.matrimonyapp.modal.MultipleHierarchyModel;
import com.example.matrimonyapp.modal.MultipleSelectionDataFetcher;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteSetPreference;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
//import com.suke.widget.SwitchButton;
import com.yahoo.mobile.client.android.util.rangeseekbar.RangeSeekBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SetPreferencesActivity extends AppCompatActivity {

    private com.appyvet.materialrangebar.RangeBar rangeBar_salaryRange, rangeBar_ageRange, rangeBar_heightRange;

    private View include_toolbar;

    private Context context;
    private  int setPreferencesId=0;
    private TextView textView_toolbarHeader, textView_ageRange, textView_salaryRange, textView_heightRange,
            textView_qualificationId, textView_maritalStatusId, textView_familyTypeId, textView_familyValuesId,
            textView_colorId, textView_occupationId, textView_religonId, textView_stateId, textView_cityId,
            textView_setPreferences, textView_resetPreference;// textView_workingStateId, textView_workingDistrictId, textView_workingTalukaId;

    private EditText editText_highestQualificationLevel, editText_qualification, editText_maritalStatus, editText_familyType, editText_familyValues,
            editText_color, editText_occupation, editText_religon, editText_city, editText_country, editText_state,
            editText_caste, editText_subCaste, editText_diet, editText_individualIncome, editText_familyIncome,
            editText_workingState, editText_workingCountry, editText_workingCity;

    private RadioGroup radioGroup_gender, radioGroup_serviceType, radioGroup_workingLocation, radioGroup_jobType;

    private CardView cardView_religion, cardView_caste,cardView_subcaste;

    private ImageView imageView_back;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    private String diet;

    UserModel userModel;

    MultipleSelectionDataFetcher multipleSelectionDataFetcher;
    PopupFetcher popupFetcher;
    String gender, serviceType, workingLocation, jobType;
    LinearLayout lr_state, lr_country, lr_city;
    CustomDialogLocationRec customDialogLocationRec;
    DataFetcherLocation dataFetcherLocation;
    AddLOcationModal lOcationModal;
    ArrayList<AddLOcationModal> list;
    //SwitchButton switchButton_otherCaste;
    SQLiteSetPreference sqLiteSetPreference;
    StringBuilder state_builder_id;
    String StateId="";

    ArrayList arrayList_residentialCountryIds, arrayList_residentialStateIds, arrayList_residentialCityIds,
            arrayList_workingCountryIds, arrayList_workingStateIds,arrayList_workingCityIds, arrayList_religion,
            arrayList_caste, arrayList_SubCaste, arrayList_maritalStatus, arrayList_familyType,
            arrayList_diet, arrayList_occupation, arrayList_individualIncome, arrayList_familyIncome,
            arrayList_familyValues, arrayList_color, arrayList_highestQualificationLevel, arrayList_qualification;


    ArrayList<MultipleHierarchyModel> arrayList_religionHierarchy, arrayList_locationHierarchy;
    private String currentLanguage;


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



        //getSQLitedetails();


    }


    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }
    private void init() {

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        context = getApplicationContext();
        //  list = new ArrayList<AddLOcationModal>();
        list = new ArrayList<>();
        dataFetcherLocation = new DataFetcherLocation(lOcationModal, customDialogLocationRec, list, SetPreferencesActivity.this);
        multipleSelectionDataFetcher = new MultipleSelectionDataFetcher("", context);
        arrayList_residentialCountryIds = new ArrayList();
        arrayList_residentialStateIds = new ArrayList();
        arrayList_residentialCityIds = new ArrayList();
        arrayList_workingCountryIds = new ArrayList();
        arrayList_workingStateIds = new ArrayList();
        arrayList_workingCityIds = new ArrayList();

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


        arrayList_religionHierarchy = new ArrayList<MultipleHierarchyModel>();
        arrayList_locationHierarchy = new ArrayList<MultipleHierarchyModel>();

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
        lr_country = findViewById(R.id.lr_country);
        lr_city = findViewById(R.id.lr_city);
        cardView_religion = findViewById(R.id.cardView_religion);
        cardView_caste = findViewById(R.id.cardView_caste);
        cardView_subcaste = findViewById(R.id.cardView_subcaste);

        editText_country = findViewById(R.id.editText_country);
        editText_city = findViewById(R.id.editText_city);
        editText_state = findViewById(R.id.editText_state);

        textView_stateId = findViewById(R.id.textView_stateId);
        textView_cityId = findViewById(R.id.textView_cityId);

        editText_workingState = findViewById(R.id.editText_workingState);
        editText_workingCountry = findViewById(R.id.editText_workingCountry);
        editText_workingCity = findViewById(R.id.editText_workingCity);


        textView_setPreferences = findViewById(R.id.textView_setPreferences);
        textView_resetPreference = findViewById(R.id.textView_resetPreference);

        radioGroup_serviceType = findViewById(R.id.radioGroup_serviceType);
        radioGroup_workingLocation = findViewById(R.id.radioGroup_workingLocation);
        radioGroup_jobType = findViewById(R.id.radioGroup_jobType);


     //   include_toolbar.setBackgroundColor(Color.WHITE);
        textView_toolbarHeader.setText("Set Preferences");
        state_builder_id = new StringBuilder();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");

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

        textView_resetPreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                resetPreferences();
            }
        });

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

        multipleSelecttionMenu(editText_state, "State", "0");
        multipleSelecttionMenu(editText_country, "Country", "0");
        multipleSelecttionMenu(editText_city, "City", "0");


        multipleSelecttionMenu(editText_workingState, "WorkingState", "0");
        multipleSelecttionMenu(editText_workingCountry, "WorkingCountry", "0");
        multipleSelecttionMenu(editText_workingCity, "WorkingCity", "0");



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
            else if(params[0].equals("getDetails"))
            {
                getDetails();
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


            if(params[0].equals("Country"))
            {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                        "Name", editText_country, arrayList_residentialCountryIds, SetPreferencesActivity.this,
                        customDialogLoadingProgressBar);


            }
            else if(params[0].equals("State"))
            {
                //String id = textView_countryId.getText().toString();
                String countryId = arrayList_residentialCountryIds.toString().substring(1,arrayList_residentialCountryIds.toString().length()-1).replaceAll(" ","");

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_STATE+"CountryId="+countryId
                                +",&Language="+userModel.getLanguage(),"StatesID",
                        "StatesName", editText_state, arrayList_residentialStateIds, SetPreferencesActivity.this,
                        customDialogLoadingProgressBar);


            }
            else if(params[0].equals("City"))
            {
                //String id = textView_stateId.getText().toString();
                String statesId = arrayList_residentialStateIds.toString().substring(1,arrayList_residentialStateIds.toString().length()-1).replaceAll(" ","");

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_CITY +"StatesId="+statesId
                                +",&Language="+userModel.getLanguage(),"DistrictId",
                        "DistrictName", editText_city, arrayList_residentialCityIds, SetPreferencesActivity.this,
                        customDialogLoadingProgressBar);


            }


            else if(params[0].equals("WorkingCountry"))
            {

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                        "Name", editText_workingCountry, arrayList_workingCountryIds, SetPreferencesActivity.this,
                        customDialogLoadingProgressBar);


            }
            else if(params[0].equals("WorkingState"))
            {
                //String id = textView_countryId.getText().toString();
                String countryId = arrayList_workingCountryIds.toString().substring(1,arrayList_workingCountryIds.toString().length()-1).replaceAll(" ","");

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_STATE+"CountryId="+countryId
                                +",&Language="+userModel.getLanguage(),"StatesID",
                        "StatesName", editText_workingState, arrayList_workingStateIds, SetPreferencesActivity.this,
                        customDialogLoadingProgressBar);


            }
            else if(params[0].equals("WorkingCity"))
            {
                //String id = textView_stateId.getText().toString();
                String statesId = arrayList_workingStateIds.toString().substring(1,arrayList_workingStateIds.toString().length()-1).replaceAll(" ","");

                multipleSelectionDataFetcher.loadList(URLs.URL_GET_MULTIPLE_CITY +"StatesId="+statesId
                                +",&Language="+userModel.getLanguage(),"DistrictId",
                        "DistrictName", editText_workingCity, arrayList_workingCityIds, SetPreferencesActivity.this,
                        customDialogLoadingProgressBar);


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

    void getDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_GETPREFERENCES,  //+ "&Language=" + userModel.getLanguage()
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.length()>0) {

                                JSONObject jsonParent = jsonObject.getJSONObject("Root").getJSONObject("Parent");

                                /*rangeBar_ageRange.setRangePinsByValue(Float.parseFloat(jsonParent.getString("AgeMin")),
                                        Float.parseFloat(jsonParent.getString("AgeMax")));

                                rangeBar_heightRange.setRangePinsByValue(Float.parseFloat(jsonParent.getString("HeightMin")),
                                        Float.parseFloat(jsonParent.getString("HeightMax")));*/



/*
                                checkIfEmpty(textView_serviceType, jsonParent.getString("ServiceType"));
                                checkIfEmpty(textView_workingIn, jsonParent.getString("WorkingIn"));
                                checkIfEmpty(textView_currentService, jsonParent.getString("CurrentService"));

*/



                                getJsonData(jsonParent,"Relegions", "Relegions","ReligionId", "ReligionName", editText_religon, arrayList_religion);
                                getJsonData(jsonParent,"Caste","Caste","CasteId", "CasteName", editText_caste, arrayList_caste);
                                getJsonData(jsonParent,"SubCaste", "SubCaste","SubCasteId", "SubCasteName", editText_subCaste, arrayList_SubCaste);
                                getJsonData(jsonParent,"ResidentialCountry", "ResidentialCountry","ID", "Name", editText_country, arrayList_residentialCountryIds);
                                getJsonData(jsonParent,"ResidentialState", "ResidentialState", "ID", "Name", editText_state, arrayList_residentialStateIds);
                                getJsonData(jsonParent,"ResidentialCity", "CityMaster","ID", "Name", editText_city, arrayList_residentialCityIds);
                                getJsonData(jsonParent,"WorkingCountry", "WorkingCountry", "ID", "Name", editText_workingCountry, arrayList_workingCountryIds);
                                getJsonData(jsonParent,"WorkingStateId", "WorkingStateId", "ID", "Name", editText_workingState, arrayList_workingStateIds);
                                getJsonData(jsonParent,"WorkingCity", "WorkingCity", "ID", "Name", editText_workingCity, arrayList_workingCityIds);
                                getJsonData(jsonParent,"QualificationLevel", "QualificationLevel", "QualificationLevelId", "QualificationLevelName", editText_highestQualificationLevel, arrayList_highestQualificationLevel);
                                getJsonData(jsonParent,"Qualification", "Qualification", "QualificationId", "Qualification", editText_qualification, arrayList_qualification);
                                getJsonData(jsonParent,"Occupation", "Occupation", "OccupationId", "OccupationName", editText_occupation, arrayList_occupation);
                                getJsonData(jsonParent,"ExpectedIndividualIncome", "ExpectedIndividualIncome", "SalaryPackageId", "SalaryPackageName", editText_individualIncome, arrayList_individualIncome);
                                getJsonData(jsonParent,"ExpectedFamilyIncome", "ExpectedFamilyIncome", "SalaryPackageId", "SalaryPackageName", editText_familyIncome, arrayList_familyIncome);
                                getJsonData(jsonParent,"FamilyType", "FamilyType", "FamilyTypeId", "FamilyTypeName", editText_familyType, arrayList_familyType);
                                getJsonData(jsonParent,"FamilyValues", "FamilyValues", "FamilyValuesId", "FamilyValuesName", editText_familyValues, arrayList_familyValues);
                                getJsonData(jsonParent,"Diet", "Diet", "DietId", "DietName", editText_diet, arrayList_diet);
                                getJsonData(jsonParent,"MaritalStatus", "MaritalStatus", "MaritalStatusId", "MaritalStatusName", editText_maritalStatus, arrayList_maritalStatus);
                                //getJsonData(jsonParent,"MaritalStatus", "MaritalStatus", "MaritalStatusId", "MaritalStatusName", editText_maritalStatus, arrayList_maritalStatus);




                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SetPreferencesActivity.this, "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId",userModel.getUserId());

                return params;
            }
        };

        VolleySingleton.getInstance(SetPreferencesActivity.this).addToRequestQueue(stringRequest);


    }

    private void getJsonData(JSONObject jsonObject, String parentKeyName, String childKeyName,
                             String columnId, String columnName, EditText textView, ArrayList arrayList) {

        try {
            textView.setText("");
            String names="";
            arrayList.clear();

            if(jsonObject.has(parentKeyName))
            {

                Object object = new JSONTokener(jsonObject.getJSONObject(parentKeyName).getString(childKeyName)).nextValue();



                if (object instanceof JSONArray)
                {
                    JSONArray jsonArray = new JSONArray(jsonObject.getJSONObject(parentKeyName).getString(childKeyName));
                    for (int i=0; i<jsonArray.length(); i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        names = names + jsonObject1.getString(columnName)+", ";
                        arrayList.add(jsonObject1.getString(columnId));
                    }
                    names = names.substring(0,names.length()-2);
                    //stringIds = stringIds.substring(0,stringIds.length()-1);
                }

                else if(object instanceof JSONObject)
                {
                    JSONObject json = new JSONObject(jsonObject.getJSONObject(parentKeyName).getString(childKeyName));

                    names = json.getString(columnName);
                    arrayList.add(json.getString((columnId)));

                }


                textView.setText(names);


            }






        } catch (JSONException jsonException) {
            jsonException.printStackTrace();
        }


    }



    private void insertSQLDetails()
    {
        RadioButton radioButton_gender, radioButton_serviceType, radioButton_workingLocation, radioButton_jobType;

        radioButton_gender = (RadioButton)findViewById(radioGroup_gender.getCheckedRadioButtonId());
        radioButton_serviceType = (RadioButton)findViewById(radioGroup_serviceType.getCheckedRadioButtonId());
        radioButton_workingLocation = (RadioButton)findViewById(radioGroup_workingLocation.getCheckedRadioButtonId());
        radioButton_jobType = (RadioButton)findViewById(radioGroup_jobType.getCheckedRadioButtonId());

        if(radioButton_gender!=null)
        {
            gender = radioButton_gender.getText().toString();
        }

        if(radioButton_serviceType!=null)
        {
            serviceType = radioButton_serviceType.getText().toString();
        }

        if(radioButton_workingLocation!=null)
        {
            workingLocation = radioButton_workingLocation.getText().toString();
        }

        if(radioButton_jobType!=null)
        {
            jobType = radioButton_jobType.getText().toString();
        }

        final String state = editText_state.getText().toString();
        final String country = editText_country.getText().toString();
        final String city = editText_city.getText().toString();
        final String stateIds = arrayList_residentialStateIds.toString().substring(1,arrayList_residentialStateIds.toString().length()-1).replaceAll(" ","");
        final String countryIds = arrayList_residentialCountryIds.toString().substring(1,arrayList_residentialCountryIds.toString().length()-1).replaceAll(" ","");
        final String cityIds = arrayList_residentialCityIds.toString().substring(1,arrayList_residentialCityIds.toString().length()-1).replaceAll(" ","");
        final String workingState = editText_workingState.getText().toString();
        final String workingCountry = editText_workingCountry.getText().toString();
        final String workingCity = editText_workingCity.getText().toString();
        final String workingStateIds = arrayList_workingStateIds.toString().substring(1,arrayList_workingStateIds.toString().length()-1).replaceAll(" ","");
        final String workingCountryIds = arrayList_workingCountryIds.toString().substring(1,arrayList_workingCountryIds.toString().length()-1).replaceAll(" ","");
        final String workingCityIds = arrayList_workingCityIds.toString().substring(1,arrayList_workingCityIds.toString().length()-1).replaceAll(" ","");
        final String maritalStatus = editText_maritalStatus.getText().toString();
        final String maritalStatusIds = arrayList_maritalStatus.toString().substring(1,arrayList_maritalStatus.toString().length()-1).replaceAll(" ","");
        final String familyType = editText_familyType.getText().toString();
        final String familyTypeIds = arrayList_familyType.toString().substring(1,arrayList_familyType.toString().length()-1).replaceAll(" ","");
        final String familyValue= editText_familyValues.getText().toString();
        final String familyValueIds= arrayList_familyValues.toString().substring(1,arrayList_familyValues.toString().length()-1).replaceAll(" ","");
        final String familyIncome = editText_familyIncome.getText().toString();;
        final String familyIncomeIds = arrayList_familyIncome.toString().substring(1,arrayList_familyIncome.toString().length()-1).replaceAll(" ","");
        final String individualIncome = editText_individualIncome.getText().toString();
        final String individualIncomeIds = arrayList_individualIncome.toString().substring(1,arrayList_individualIncome.toString().length()-1).replaceAll(" ","");
        final String qualificationLevel = editText_highestQualificationLevel.getText().toString();
        final String qualificationLevelIds = arrayList_highestQualificationLevel.toString().substring(1,arrayList_highestQualificationLevel.toString().length()-1).replaceAll(" ","");
        final String qualification = editText_qualification.getText().toString();
        final String qualificationIds = arrayList_qualification.toString().substring(1,arrayList_qualification.toString().length()-1).replaceAll(" ","");
        final String diet = editText_diet.getText().toString();
        final String dietIds = arrayList_diet.toString().substring(1,arrayList_diet.toString().length()-1).replaceAll(" ","");
        final String color = editText_color.getText().toString();
        final String colorIds = arrayList_color.toString().substring(1,arrayList_color.toString().length()-1).replaceAll(" ","");
        final String occupation = editText_occupation.getText().toString();
        final String occupationIds = arrayList_occupation.toString().substring(1,arrayList_occupation.toString().length()-1).replaceAll(" ","");
        final String religion = editText_religon.getText().toString();
        final String religionIds = arrayList_religion.toString().substring(1,arrayList_religion.toString().length()-1).replaceAll(" ","");
        final String caste = editText_caste.getText().toString();
        final String casteIds = arrayList_caste.toString().substring(1,arrayList_caste.toString().length()-1).replaceAll(" ","");
        final String subCaste = editText_subCaste.getText().toString();
        final String subCasteIds = arrayList_SubCaste.toString().substring(1,arrayList_SubCaste.toString().length()-1).replaceAll(" ","");

        String ageMin = rangeBar_ageRange.getLeftPinValue();
        String ageMax = rangeBar_ageRange.getRightPinValue();
        String heightMin = rangeBar_heightRange.getLeftPinValue();
        String heightMax = rangeBar_heightRange.getRightPinValue();

        long res=0;

        if(sqLiteSetPreference.isPreferenceExistByUserId(userModel.getUserId()))
        {
            res = sqLiteSetPreference.updateSetPreference(userModel.getUserId(), gender, country, state, city,
                    countryIds, stateIds, cityIds, workingCountry, workingState, workingCity,
                    workingCountryIds, workingStateIds, workingCityIds, ageMin, ageMax, religion,
                    religionIds, caste, casteIds, subCaste,subCasteIds, qualificationLevel,qualificationLevelIds,
                    qualification,qualificationIds, maritalStatus, maritalStatusIds, diet, dietIds,
                    serviceType, workingLocation, jobType, occupation, occupationIds, individualIncome,
                    individualIncomeIds, familyIncome, familyIncomeIds, familyType,familyTypeIds,
                    familyValue,familyValueIds, heightMin, heightMax, color,colorIds);
        }
        else {
            res = sqLiteSetPreference.insertSetPreference(userModel.getUserId(), gender, country, state, city,
                    countryIds, stateIds, cityIds, workingCountry, workingState, workingCity,
                    workingCountryIds, workingStateIds, workingCityIds, ageMin, ageMax, religion,
                    religionIds, caste, casteIds, subCaste,subCasteIds, qualificationLevel,qualificationLevelIds,
                    qualification,qualificationIds, maritalStatus, maritalStatusIds, diet, dietIds,
                    serviceType, workingLocation, jobType, occupation, occupationIds, individualIncome,
                    individualIncomeIds, familyIncome, familyIncomeIds, familyType,familyTypeIds,
                    familyValue,familyValueIds, heightMin, heightMax, color,colorIds);
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


        private void insertDetails()
        {
            RadioButton radioButton_gender, radioButton_serviceType, radioButton_workingLocation, radioButton_jobType;

            radioButton_gender = (RadioButton)findViewById(radioGroup_gender.getCheckedRadioButtonId());
            radioButton_serviceType = (RadioButton)findViewById(radioGroup_serviceType.getCheckedRadioButtonId());
            radioButton_workingLocation = (RadioButton)findViewById(radioGroup_workingLocation.getCheckedRadioButtonId());
            radioButton_jobType = (RadioButton)findViewById(radioGroup_jobType.getCheckedRadioButtonId());


            if(radioButton_gender!=null)
            {
                gender = radioButton_gender.getText().toString();
            }

            if(radioButton_serviceType!=null)
            {
                serviceType = radioButton_serviceType.getText().toString();
            }

            if(radioButton_workingLocation!=null)
            {
                workingLocation = radioButton_workingLocation.getText().toString();
            }

            if(radioButton_jobType!=null)
            {
                jobType = radioButton_jobType.getText().toString();
            }

            final String state = editText_state.getText().toString();
            final String residentialCountryIds = arrayList_residentialCountryIds.toString().substring(1,arrayList_residentialCountryIds.toString().length()-1).replaceAll(" ","");
            final String residentialStateIds = arrayList_residentialStateIds.toString().substring(1,arrayList_residentialStateIds.toString().length()-1).replaceAll(" ","");
            final String residentialCityIds = arrayList_residentialCityIds.toString().substring(1,arrayList_residentialCityIds.toString().length()-1).replaceAll(" ","");

            final String workingCountryIds = arrayList_workingCountryIds.toString().substring(1,arrayList_residentialCountryIds.toString().length()-1).replaceAll(" ","");
            final String workingStateIds = arrayList_residentialStateIds.toString().substring(1,arrayList_residentialStateIds.toString().length()-1).replaceAll(" ","");
            final String workingCityIds = arrayList_residentialCityIds.toString().substring(1,arrayList_residentialCityIds.toString().length()-1).replaceAll(" ","");

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




            StringRequest stringRequest = new StringRequest(Request.Method.POST,
                    URLs.URL_POST_FILTER,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d("RESPONSE","-----------------------------\n"+response);
                            /*try {


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
*/
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


                    params.put("SetPreferenceId","0");                 //1
                    params.put("UserId",userModel.getUserId());                 //1

                    params.put("LookingFor", gender);                               //4
                    params.put("RelegionIds", religionIds);                      //10
                    params.put("CasteIds", casteIds);                            //11
                    params.put("SubCasteIds", subCasteIds);
                    params.put("ResidentialCountryIds", residentialCountryIds);                            //5
                    params.put("ResidentialStateIds", residentialStateIds);                     //6
                    params.put("ResidentialCityIds", residentialCityIds);                         //7
                    params.put("WorkingCountryIds", workingCountryIds);                         //7
                    params.put("WorkingStateIds", workingStateIds);                         //7
                    params.put("WorkingCityIds", workingCityIds);                         //7


                    params.put("AgeMin", rangeBar_ageRange.getLeftPinValue());  //8
                    params.put("AgeMax", rangeBar_ageRange.getRightPinValue()); //9
                                      //12
                    params.put("HeightMin", rangeBar_heightRange.getLeftPinValue());//22
                    params.put("HeightMax", rangeBar_heightRange.getRightPinValue());   //23

                    params.put("QualificationLevelIds", qualificationLevelIds);  //13
                    params.put("QualificationIds", qualificationIds);    //14
                    params.put("ServiceType", serviceType);
                    params.put("WorkingIn", workingLocation);
                    params.put("CurrentService", jobType);
                    params.put("OccupationIds", occupationIds);                //17

                    params.put("ExpectedFamilyIncomeIds", familyIncomeIds);        //18
                    params.put("ExpectedIndividualIncomeIds", individualIncomeIds);//19
                    params.put("FamilyTypeIds", familyTypeIds);                    //20
                    params.put("FamilyValuesIds", familyValueIds);                  //21
                    params.put("DietIds", dietIds);                            //16
                    params.put("ColorIds", colorIds);                     //24
                    params.put("MaritalStatusIds", maritalStatusIds);            //15

                    return params;
                }
            };

            VolleySingleton.getInstance(SetPreferencesActivity.this).addToRequestQueue(stringRequest);


        }

    /*private void getSQLitedetails(){


        Cursor cursor = sqLiteSetPreference.getDataByUserId(userModel.getUserId());
        if(cursor!=null)
        {
            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {
                arrayList_religion = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.RELIGION_ID)).split(","))));
                editText_religon.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.RELIGION)));

                arrayList_caste = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.CASTE_ID)).split(","))));
                editText_caste.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.CASTE)));

                arrayList_SubCaste =stringToInt( new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.SUB_CASTE_ID)).split(","))));
                editText_subCaste.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.SUB_CASTE)));

                arrayList_stateId = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.STATE_ID)).split(","))));
                editText_state.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.STATE)));

                arrayList_countryId = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.COUNTRY_ID)).split(","))));
                editText_country.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.COUNTRY)));

                arrayList_cityId = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.CITY_ID)).split(","))));
                editText_city.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.CITY)));

                arrayList_workingStateId = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_STATE_ID)).split(","))));
                editText_workingState.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_STATE)));

                arrayList_workingCountryId = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_COUNTRY_ID)).split(","))));
                editText_workingCountry.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_COUNTRY)));

                arrayList_workingCityId = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_CITY_ID)).split(","))));
                editText_workingCity.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_CITY)));

                arrayList_maritalStatus = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.MARITAL_STATUS_ID)).split(","))));
                editText_maritalStatus.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.MARITAL_STATUS)));

                arrayList_highestQualificationLevel = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.HIGHEST_QUALIFICATION_LEVEL_ID)).split(","))));
                editText_highestQualificationLevel.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.HIGHEST_QUALIFICATION_LEVEL)));

                arrayList_qualification = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.HIGHEST_QUALIFICATION_ID)).split(","))));
                editText_qualification.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.HIGHEST_QUALIFICATION)));

                arrayList_occupation = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.OCCUPATION_ID)).split(","))));
                editText_occupation.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.OCCUPATION)));

                arrayList_individualIncome =stringToInt( new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.EXPECTED_INDIVIDUAL_INCOME_ID)).split(","))));
                editText_individualIncome.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.EXPECTED_INDIVIDUAL_INCOME)));

                arrayList_familyIncome = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.EXPECTED_FAMILY_INCOME_ID)).split(","))));
                editText_familyIncome.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.EXPECTED_FAMILY_INCOME)));

                arrayList_familyType = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.FAMILY_TYPE_ID)).split(","))));
                editText_familyType.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.FAMILY_TYPE)));

                arrayList_familyValues = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.FAMILY_VALUE_ID)).split(","))));
                editText_familyValues.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.FAMILY_VALUE)));

                arrayList_diet = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.DIET_ID)).split(","))));
                editText_diet.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.DIET)));

                arrayList_color = stringToInt(new ArrayList<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.SKIN_COLOR_ID)).split(","))));
                editText_color.setText(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.SKIN_COLOR)));



                rangeBar_ageRange.setRangePinsByValue(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.AGE_MIN))),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.AGE_MAX))));

                rangeBar_heightRange.setRangePinsByValue(Float.parseFloat(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.HEIGHT_MIN))),
                        Float.parseFloat(cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.HEIGHT_MAX))));

                FieldValidation.setRadioButtonAccToValue(radioGroup_gender, cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.GENDER)));
                FieldValidation.setRadioButtonAccToValue(radioGroup_serviceType, cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.SERVICE_TYPE)));
                FieldValidation.setRadioButtonAccToValue(radioGroup_workingLocation, cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.WORKING_LOCATION)));
                FieldValidation.setRadioButtonAccToValue(radioGroup_jobType, cursor.getString(cursor.getColumnIndex(SQLiteSetPreference.JOB_TYPE)));



            }
        }
        cursor.close();

    }*/


    private ArrayList stringToInt(ArrayList arrayList)
    {
        ArrayList intArrayList = new ArrayList();
        for(int i=0; i<arrayList.size(); i++)
        {
            intArrayList.add(Integer.parseInt((String) arrayList.get(i)));

        }
        return intArrayList;

    }


    private void resetPreferences()
    {
        radioGroup_gender.clearCheck();
        radioGroup_serviceType.clearCheck();
        radioGroup_workingLocation.clearCheck();
        radioGroup_jobType.clearCheck();

        arrayList_religion.clear();
        editText_religon.setText("");
        arrayList_caste.clear();
        editText_caste.setText("");
        arrayList_SubCaste.clear();
        editText_subCaste.setText("");


        arrayList_residentialStateIds.clear();
        editText_state.setText("");
        arrayList_residentialCountryIds.clear();
        editText_country.setText("");
        arrayList_residentialCityIds.clear();
        editText_city.setText("");

        arrayList_workingStateIds.clear();
        editText_workingState.setText("");
        arrayList_workingCountryIds.clear();
        editText_workingCountry.setText("");
        arrayList_workingCityIds.clear();
        editText_workingCity.setText("");

        arrayList_maritalStatus.clear();
        editText_maritalStatus.setText("");
        arrayList_highestQualificationLevel.clear();
        editText_highestQualificationLevel.setText("");
        arrayList_qualification.clear();
        editText_qualification.setText("");
        arrayList_occupation.clear();
        editText_occupation.setText("");
        arrayList_individualIncome.clear();
        editText_individualIncome.setText("");
        arrayList_familyIncome.clear();
        editText_familyIncome.setText("");
        arrayList_diet.clear();
        editText_diet.setText("");
        arrayList_color.clear();
        editText_color.setText("");
        arrayList_familyType.clear();
        editText_familyType.setText("");
        arrayList_familyValues.clear();
        editText_familyValues.setText("");

        rangeBar_heightRange.setRangePinsByValue(rangeBar_heightRange.getTickStart(),rangeBar_heightRange.getTickEnd());
        rangeBar_ageRange.setRangePinsByValue(rangeBar_ageRange.getTickStart(),rangeBar_ageRange.getTickEnd());



    }


}
