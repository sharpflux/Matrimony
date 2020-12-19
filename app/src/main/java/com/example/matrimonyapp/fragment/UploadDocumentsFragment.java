package com.example.matrimonyapp.fragment;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AadharCard;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.DataAttributes;
import com.example.matrimonyapp.utils.QRCodeException;
import com.example.matrimonyapp.utils.SecureQRCode;
import com.example.matrimonyapp.utils.Storage;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kinda.alert.KAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.BreakIterator;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UploadDocumentsFragment extends Fragment {

    private EditText editText_birthdate, editText_age, editText_birthTime, editText_firstName,
            editText_altMobileNo, editText_altEmailId, editText_mobileNo, editText_address, editText_emailId,
            editText_birthState, editText_birthTaluka, editText_birthPlace, editText_birthDistrict,
            editText_bloodGroup, editText_state, editText_postalCode, editText_taluka, editText_district,
            editText_currentState, editText_currentDistrict, editText_currentTaluka, editText_currentPostalCode,
            editText_currentAddress, editText_currentVillage, editText_village, editText_currentCountry, editText_country;

    private ImageView imageView_back;

    private RadioGroup radioGroup_gender, radioGroup_birthTimeType;

    private TextView textView_saveAndContinue;
    private TextView textView_stateId, textView_districtId, textView_talukaId, textView_birthStateId,
            textView_birthDistrictId, textView_birthTalukaId, textView_bloodGroupId, textView_currentStateId,
            textView_currentDistrictId, textView_currentTalukaId, textView_countryId, textView_currentCountryId;

    private View view;

    private CheckBox checkBox_isAddressSame;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    private int basicDetailsId=0;

    private int mHour, mMinute;

    private String timeHrs="00:00:00", timeMin;
    private AlertDialog.Builder builder;
    private ProgressDialog progressDialog;
    private Context context;

    private String fullName, gender, birthdate, birthTime, birthTimeType, birthPlace, birthState, birthTaluka,
            birthDistrict, mobileNo, altMobileNo, altEmailId, emailId, address, state, postalCode, taluka, district,
            birthStateId, birthTalukaId, birthDistrictId, stateId, talukaId, districtId;

    //private ArrayList<MyItem> list;

    private DataFetcher dataFetcher;

    private UserModel userModel;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    private EditText editText_name;
    private TextView textView_upload;
    private ImageView imageView_aadharCard;

    Storage storage;

    private static final int MY_CAMERA_REQUEST_CODE = 100;
    AadharCard aadharData;
    private EditText editText_uid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_upload_documents, container, false);

        context = getContext();

        init();


        

        onClickListener();

        
        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");
*/

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);

                ReligiousDetailsFragment religiousDetailsFragment = new ReligiousDetailsFragment();

                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, religiousDetailsFragment);

                /*UploadDocumentsFragment uploadDocumentsFragment = new UploadDocumentsFragment();

                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, uploadDocumentsFragment);*/
                fragmentTransaction.commit();


            }
        });

        return view;

    }

    public boolean checkCameraPermission (){
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions((Activity) getContext(), new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
            return false;
        }
        return true;
    }

    /**
     * onclick handler for scan new card
     * @param view
     */
    public void scanNow( View view){
        // we need to check if the user has granted the camera permissions
        // otherwise scanner will not work
        if(!checkCameraPermission()){return;}

        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(UploadDocumentsFragment.this);//new IntentIntegrator((Activity) getContext());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);

        integrator.setPrompt("Scan a Aadharcard QR Code");
        integrator.setResultDisplayDuration(500);
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.initiateScan();
    }





   /* public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // process received data
            if(scanContent != null && !scanContent.isEmpty()){
                processScannedData(scanContent);
            }else{
                showWarningPrompt("Scan Cancelled");
            }

        }else{
            showWarningPrompt("No scan data received!");
        }
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        //super.onActivityResult(requestCode, resultCode, intent);

        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);

        if (scanningResult != null) {
            //we have a result
            String scanContent = scanningResult.getContents();
            String scanFormat = scanningResult.getFormatName();

            // process received data
            if(scanContent != null && !scanContent.isEmpty()){

                    processScannedData(scanContent, scanningResult.getRawBytes());

            }else{
                showWarningPrompt("Scan Cancelled");
            }

        }else{
            showWarningPrompt("No scan data received!");
        }
    }

    /**
     * process encoded string received from aadhaar card QR code
     * @param scanData
     */
    protected void processScannedData(String scanData, byte [] rawBytes)  {
        // check if the scanned string is XML
        // This is to support old QR codes


        ObjectMapper xmlMapper = new XmlMapper();
        try {
            JsonNode jsonNode = xmlMapper.readTree(scanData.getBytes());

            ObjectMapper objectMapper = new ObjectMapper();

            String value = objectMapper.writeValueAsString(jsonNode);

            JSONObject jsonObject = new JSONObject(value);
            editText_uid.setText(jsonObject.getString("uid"));
            editText_name.setText(jsonObject.getString("name"));
            editText_birthdate.setText(jsonObject.getString("loc"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (JSONException e) {
            e.printStackTrace();
        }


       // JSONObject jsonObject = XML.toJSONObject(scanData);




/*
        if(isXml(scanData)){
            XmlPullParserFactory pullParserFactory;

            try {
                // init the parserfactory
                pullParserFactory = XmlPullParserFactory.newInstance();
                // get the parser
                XmlPullParser parser = pullParserFactory.newPullParser();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(new StringReader(scanData));
                aadharData = new AadharCard();

                // parse the XML
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if(eventType == XmlPullParser.START_DOCUMENT) {
                        Log.d("Rajdeol","Start document");
                    } else if(eventType == XmlPullParser.START_TAG && DataAttributes.AADHAAR_DATA_TAG.equals(parser.getName())) {
                        // extract data from tag
                        //uid
                        aadharData.setUuid(parser.getAttributeValue(null, DataAttributes.AADHAR_UID_ATTR));
                        //name
                        aadharData.setName(parser.getAttributeValue(null,DataAttributes.AADHAR_NAME_ATTR));
                        //gender
                        aadharData.setGender(parser.getAttributeValue(null,DataAttributes.AADHAR_GENDER_ATTR));
                        // year of birth
                        aadharData.setDateOfBirth(parser.getAttributeValue(null,DataAttributes.AADHAR_DOB_ATTR));
                        // care of
                        aadharData.setCareOf(parser.getAttributeValue(null,DataAttributes.AADHAR_CO_ATTR));
                        // village Tehsil
                        aadharData.setVtc(parser.getAttributeValue(null,DataAttributes.AADHAR_VTC_ATTR));
                        // Post Office
                        aadharData.setPostOffice(parser.getAttributeValue(null,DataAttributes.AADHAR_PO_ATTR));
                        // district
                        aadharData.setDistrict(parser.getAttributeValue(null,DataAttributes.AADHAR_DIST_ATTR));
                        // state
                        aadharData.setState(parser.getAttributeValue(null,DataAttributes.AADHAR_STATE_ATTR));
                        // Post Code
                        aadharData.setPinCode(parser.getAttributeValue(null,DataAttributes.AADHAR_PC_ATTR));

                    } else if(eventType == XmlPullParser.END_TAG) {
                        Log.d("Rajdeol","End tag "+parser.getName());

                    } else if(eventType == XmlPullParser.TEXT) {
                        Log.d("Rajdeol","Text "+parser.getText());

                    }
                    // update eventType
                    eventType = parser.next();
                }

                // display the data on screen
                displayScannedData();
                return;
            } catch (XmlPullParserException e) {
                showErrorPrompt("Error in processing QRcode XML");
                e.printStackTrace();
                return;
            } catch (IOException e) {
                showErrorPrompt(e.toString());
                e.printStackTrace();
                return;
            }
        }

        // process secure QR code
        processEncodedScannedData(scanData, rawBytes); //scanData*/
    }// EO function


    protected void processEncodedScannedData(String scanData, byte[] rawBytes){
        try {
            SecureQRCode decodedData = new SecureQRCode(getContext(),scanData, rawBytes);
            aadharData = decodedData.getScannedAadharCard();
            // display the Aadhar Data
            showSuccessPrompt("Scanned Aadhar Card Successfully");
            displayScannedData();
        } catch (QRCodeException e) {
            showErrorPrompt(e.toString());
            e.printStackTrace();
        }
    }

    /**
     * show scanned information
     */
    public void displayScannedData(){

        // update UI Elements
        editText_uid.setText(aadharData.getUuid());
        editText_name.setText(aadharData.getName());
        editText_birthdate.setText(aadharData.getDateOfBirth());
       /* tv_sd_yob.setText(aadharData.getDateOfBirth());
        tv_sd_co.setText(aadharData.getCareOf());
        tv_sd_vtc.setText(aadharData.getVtc());
        tv_sd_po.setText(aadharData.getPostOffice());
        tv_sd_dist.setText(aadharData.getDistrict());
        tv_sd_state.setText(aadharData.getState());
        tv_sd_pc.setText(aadharData.getPinCode());*/
    }


    /**
     * Function to check if string is xml
     * @param testString
     * @return boolean
     */
    protected boolean isXml (String testString){
        Pattern pattern;
        Matcher matcher;
        boolean retBool = false;

        // REGULAR EXPRESSION TO SEE IF IT AT LEAST STARTS AND ENDS
        // WITH THE SAME ELEMENT
        final String XML_PATTERN_STR = "<(\\S+?)(.*?)>(.*?)</\\1>";

        // IF WE HAVE A STRING
        if (testString != null && testString.trim().length() > 0) {

            // IF WE EVEN RESEMBLE XML
            if (testString.trim().startsWith("<")) {

                pattern = Pattern.compile(XML_PATTERN_STR,
                        Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE);

                // RETURN TRUE IF IT HAS PASSED BOTH TESTS
                matcher = pattern.matcher(testString);
                retBool = matcher.matches();
            }
            // ELSE WE ARE FALSE
        }

        return retBool;
    }



    public void showErrorPrompt(String message){
        new KAlertDialog(getContext(), KAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(message)
                .show();


    }

    public void showSuccessPrompt(String message){
        new KAlertDialog(getContext(), KAlertDialog.SUCCESS_TYPE)
                .setTitleText("Success")
                .setContentText(message)
                .show();
    }

    public void showWarningPrompt(String message){
        new KAlertDialog(getContext(), KAlertDialog.WARNING_TYPE)
                .setContentText(message)
                .show();
    }

    private void onClickListener()
    {

        textView_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                scanNow(v);
            }
        });
      
        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStack();
                }
                else
                    getActivity().finish();
            }
        });
    }

    private void init()
    {

        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        editText_uid = (EditText) view.findViewById(R.id.editText_uid);
        editText_name = (EditText) view.findViewById(R.id.editText_name);
        editText_birthdate = view.findViewById(R.id.editText_birthdate);
        imageView_aadharCard = view.findViewById(R.id.imageView_aadharCard);

        textView_upload = view.findViewById(R.id.textView_upload);

        imageView_back=((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText(context.getResources().getString(R.string.basic_details));
        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

    }

    private String getAge(int year, int month, int day)
    {

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if(today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR))
        {
            age--;
        }

        //Integer ageInt = new Integer(age);

        return String.valueOf(age);

    }

    @Override
    public void onResume() {
        super.onResume();

     /*   AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");
*/
    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String functionFor;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                //customDialogLoadingProgressBar.dismiss();

                if(params[0].equals("getDetails"))
                {
                    //getDetails();

                }
                else if(params[0].equals("insertDetails"))
                {
                    //insertDetails();
                }




            } catch (Exception e) {
                e.printStackTrace();
                //resp = e.getMessage();
            }
            return functionFor;
        }


        @Override
        protected void onPostExecute(String result) {


        }


        @Override
        protected void onPreExecute() {


            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

}
