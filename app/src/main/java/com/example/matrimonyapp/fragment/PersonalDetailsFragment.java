package com.example.matrimonyapp.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.kevalpatel2106.rulerpicker.RulerValuePicker;
import com.kevalpatel2106.rulerpicker.RulerValuePickerListener;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonalDetailsFragment extends Fragment {


    View view;
    private TextView textView_saveAndContinue;
    int PICK_IMAGE_REQUEST=1;


    ImageView imageView_back;

    EditText editText_colour, editText_maritalStatus, editText_familyStatus,
            editText_familyType, editText_familyValues, editText_disabilityType, editText_diet;

    SwitchButton switchButton_disability, switchButton_livesWithFamily;

    //RadioGroup radioGroup_diet;

    com.kevalpatel2106.rulerpicker.RulerValuePicker rulerValuePicker_height, rulerValuePicker_weight;

    NumberPicker numberPicker_heightFt, numberPicker_heightInch, numberPicker_weight;

    TextView textView_heightValue, textView_weightValue, textView_skinColor, textView_maritalStatus,
            textView_familyStatus, textView_familyType, textView_familyValues, textView_dietId;

    String  disabilityType, diet;

    Bundle bundle;
    DataFetcher dataFetcher;

    UserModel userModel;

    TextView textView_height;
    int personalDetailsId = 0;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    public PersonalDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_personal_details, container, false);

        //bundle = getArguments();


        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        textView_height= view.findViewById(R.id.textView_height);
        //editText_weight= view.findViewById(R.id.editText_weight);

        textView_heightValue= view.findViewById(R.id.textView_heightValue);
        textView_weightValue= view.findViewById(R.id.textView_weightValue);
        editText_colour= view.findViewById(R.id.editText_colour);
        editText_maritalStatus= view.findViewById(R.id.editText_maritalStatus);
        editText_familyStatus= view.findViewById(R.id.editText_familyStatus);
        editText_familyType= view.findViewById(R.id.editText_familyType);
        editText_familyValues= view.findViewById(R.id.editText_familyValues);
        editText_disabilityType= view.findViewById(R.id.editText_disabilityType);
        textView_skinColor= view.findViewById(R.id.textView_skinColor);
        textView_maritalStatus= view.findViewById(R.id.textView_maritalStatus);
        textView_familyStatus= view.findViewById(R.id.textView_familyStatus);
        textView_familyType= view.findViewById(R.id.textView_familyType);
        textView_familyValues= view.findViewById(R.id.textView_familyValues);
        rulerValuePicker_height= view.findViewById(R.id.rulerValuePicker_height);
        rulerValuePicker_weight= view.findViewById(R.id.rulerValuePicker_weight);
        //numberPicker_heightFt= view.findViewById(R.id.numberPicker_heightFt);
        //numberPicker_heightInch= view.findViewById(R.id.numberPicker_heightInch);
        //numberPicker_weight= view.findViewById(R.id.numberPicker_weight);

        editText_diet = view.findViewById(R.id.editText_diet);
        textView_dietId = view.findViewById(R.id.textView_dietId);

        switchButton_disability = view.findViewById(R.id.switchButton_disability);
        switchButton_livesWithFamily = view.findViewById(R.id.switchButton_livesWithFamily);
        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);

        tv.setText("Personal Details");

        dataFetcher = new DataFetcher("PopUp",getContext());

        //diet = FieldValidation.radioGroupValidation(radioGroup_diet);

/*        radioGroup_diet.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int id = radioGroup.getCheckedRadioButtonId();

                RadioButton rb = view.findViewById(id);

                diet = rb.getText().toString();
                //Toast.makeText(getContext(),gender,Toast.LENGTH_LONG).show();
            }
        });*/


        onValuePickerListener(rulerValuePicker_height, "height");
        onValuePickerListener(rulerValuePicker_weight, "weight");



        /*numberPicker_weight.setMinValue(30);
        numberPicker_weight.setMaxValue(200);

        numberPicker_heightFt.setMinValue(1);
        numberPicker_heightFt.setMaxValue(7);

        numberPicker_heightInch.setMinValue(0);
        numberPicker_heightInch.setMaxValue(11);

        numberPicker_heightFt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    numberPicker_heightFt.setTextColor(R.color.project_color);
                }
                else
                    numberPicker_heightFt.setTextColor(R.color.text_gray);
            }
        });*/

        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        onClickListener();


        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());

        AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");

        return view;


    }


    private void onValuePickerListener(RulerValuePicker rulerValuePicker, final String forRuler)
    {

        rulerValuePicker.setValuePickerListener(new RulerValuePickerListener() {
            @Override
            public void onValueChange(int selectedValue) {

            }

            @Override
            public void onIntermediateValueChange(int selectedValue) {

                if (forRuler.equals("height")) {
                    float inch = 0.394f * selectedValue;

                    double value = new BigDecimal(inch).setScale(0,
                            BigDecimal.ROUND_HALF_UP).doubleValue();

                    int quo = (int)value/12;
                    int rem = (int)value%12;

                    textView_heightValue.setText(quo + "."+rem+"\"");
                } else if (forRuler.equals("weight"))
                {
                    textView_weightValue.setText(selectedValue+" kgs.");
                }
            }
        });

    }

    void onClickListener()
    {

        showPopUp(editText_colour,"Colour");
        showPopUp(editText_maritalStatus,"MaritalStatus");
        showPopUp(editText_familyStatus,"FamilyStatus");
        showPopUp(editText_familyType,"FamilyType");
        showPopUp(editText_familyValues,"FamilyValues");
        showPopUp(editText_diet,"Diet");



        switchButton_disability.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(!isChecked)
                {
                    editText_disabilityType.setText("NA");
                    editText_disabilityType.setEnabled(false);
                }
                else{
                    editText_disabilityType.setText("");
                    editText_disabilityType.setEnabled(true);
                }
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
            }
        });



        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {

/*
                height = textView_height.getText().toString();
                weight = editText_weight.getText().toString();
                colour = editText_colour.getText().toString();
                maritalStatus = editText_maritalStatus.getText().toString();
                familyStatus = editText_familyStatus.getText().toString();
                familyType = editText_familyType.getText().toString();
                familyValues = editText_familyValues.getText().toString();
                disabilityType = editText_disabilityType.getText().toString();
                disability = String.valueOf(switchButton_disability.isChecked());
                livesWithFamily = String.valueOf(switchButton_livesWithFamily.isChecked());
                //= .getText().toString();

                bundle.putString("height",height);
                bundle.putString("weight",weight);
                bundle.putString("colour",colour);
                bundle.putString("maritalStatus",maritalStatus);
                bundle.putString("familyStatus",familyStatus);
                bundle.putString("familyType",familyType);
                bundle.putString("familyValues",familyValues);
                bundle.putString("disability",disability);
                bundle.putString("disabilityType",disabilityType);
                bundle.putString("diet",diet);
                bundle.putString("livesWithFamily",livesWithFamily);
*/
                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");
/*
                UploadImageFragment uploadImageFragment = new UploadImageFragment();
                uploadImageFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, uploadImageFragment);
                fragmentTransaction.commit() ;*/



            }
        });
    }

    void showPopUp(EditText editText, final String urlFor)
    {

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute(urlFor);

            }
        });
    }




    void insertDetails()
    {
        /*final String diet = ((RadioButton)view.findViewById(radioGroup_diet.getCheckedRadioButtonId()))
                .getText().toString();
*/
        disabilityType = editText_disabilityType.getText().toString();

        if(editText_disabilityType.getText().toString().equals(""))
        {
            disabilityType = "";
        }


        final String finalDisabilityType = disabilityType;
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_PERSONALDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object

                            JSONObject jsonObject = new JSONObject(response);

                            if(!jsonObject.getBoolean("error"))
                            {
                                getDetails();

                                Toast.makeText(getContext(),"Personal details saved successfully!", Toast.LENGTH_SHORT).show();

                                /*UploadImageFragment uploadImageFragment = new UploadImageFragment();
                                //uploadImageFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, uploadImageFragment);
                                fragmentTransaction.commit();*/

/*                                UploadImageFragment uploadImageFragment= new UploadImageFragment();
                                // personalDetailsFragment.setArguments(bundle);
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, uploadImageFragment);
                                fragmentTransaction.commit() ;*/

                                getActivity().finish();


                                //getActivity().finish();
                            }
                            else
                            {
                                Toast.makeText(getContext(),"Invalid Details POST ! ",Toast.LENGTH_SHORT).show();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("PersonalDetailsId",String.valueOf(personalDetailsId));
                params.put("UserId",userModel.getUserId());
                params.put("Height",String.valueOf(rulerValuePicker_height.getCurrentValue()));
                params.put("Weight",String.valueOf(rulerValuePicker_weight.getCurrentValue()));
                params.put("SkinColourId",  textView_skinColor.getText().toString());
                params.put("MaritalStatusId",textView_maritalStatus.getText().toString());
                params.put("FamilyStatusId",textView_familyStatus.getText().toString());
                params.put("FamilyTypeId",textView_familyType.getText().toString());
                params.put("FamilyValuesId",textView_familyValues.getText().toString());
                params.put("Disability",String.valueOf(switchButton_disability.isChecked()));
                params.put("DisabilityType", disabilityType);
                params.put("Diet",textView_dietId.getText().toString());
                params.put("LivesWithFamily",String.valueOf(switchButton_livesWithFamily.isChecked()));
                params.put("LanguageType",userModel.getLanguage());


                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);



    }



    void getDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PERSONALDETAILS+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if(jsonArray.length()>0)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if(!jsonObject.getBoolean("error"))
                                {
                                    personalDetailsId = jsonObject.getInt("PersonalDetailsId");


                                    rulerValuePicker_height.selectValue(jsonObject.getInt("Height"));
                                    rulerValuePicker_weight.selectValue(jsonObject.getInt("Weight"));


                                    textView_skinColor.setText(jsonObject.getString("SkinColourId"));
                                    textView_maritalStatus.setText(jsonObject.getString("MaritalStatusId"));
                                    textView_familyStatus.setText(jsonObject.getString("FamilyStatusId"));
                                    textView_familyType.setText(jsonObject.getString("FamilyTypeId"));
                                    textView_familyValues.setText(jsonObject.getString("FamilyValuesId"));


                                    switchButton_disability.setChecked(jsonObject.getBoolean("Disability"));
                                    editText_disabilityType.setText(jsonObject.getString("DisabilityType"));
                                    switchButton_livesWithFamily.setChecked(jsonObject.getBoolean("LivesWithFamily"));
                                    editText_colour.setText(jsonObject.getString("SkinColourName"));
                                    editText_maritalStatus.setText(jsonObject.getString("MaritalStatusName"));
                                    editText_familyStatus.setText(jsonObject.getString("FamilyStatusName"));
                                    editText_familyType.setText(jsonObject.getString("FamilyTypeName"));
                                    editText_familyValues.setText(jsonObject.getString("FamilyValuesName"));


/*                                    String diet = jsonObject.getString("Diet");
                                    if(diet.toLowerCase().contains("non".toLowerCase()))
                                        diet = getResources().getString(R.string.non_veg);
                                    else
                                        diet = getResources().getString(R.string.veg);
                                    FieldValidation.setRadioButtonAccToValue(radioGroup_diet,
                                            diet);*/

                                }



                            }
                            else
                            {
                                personalDetailsId = 0;
                                customDialogLoadingProgressBar.dismiss();
                                Toast.makeText(getContext(),"Sorry for the inconvenience \nPlease try again!" ,Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);




    }




    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;



        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


                if(params[0].toString().equals("getDetails"))
                {
                    getDetails();

                }
                else if(params[0].toString().equals("insertDetails"))
                {
                    insertDetails();
                }

                if(params[0].toString()=="Colour")
                {
                    dataFetcher.loadList(URLs.URL_GET_SKINCOLOR+"Language="+userModel.getLanguage(),"SkinColourId",
                            "SkinColourName", editText_colour, textView_skinColor,getContext(), customDialogLoadingProgressBar);


                }
                else if(params[0].toString()=="MaritalStatus")
                {
                    //String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_MARITALSTATUS+"Language="+userModel.getLanguage(),"MaritalStatusId",
                            "MaritalStatusName", editText_maritalStatus, textView_maritalStatus,getContext(), customDialogLoadingProgressBar);

                }
                else if(params[0].toString()=="FamilyStatus")
                {

                    //String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_FAMILYSTATUS+"Language="+userModel.getLanguage(),"FamilyStatusId",
                            "FamilyStatusName", editText_familyStatus, textView_familyStatus,getContext(), customDialogLoadingProgressBar);
                }
                else if(params[0].toString()=="FamilyType")
                {

                    //String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_FAMILYTYPE+"Language="+userModel.getLanguage(),"FamilyTypeId",
                            "FamilyTypeName", editText_familyType, textView_familyType,getContext(), customDialogLoadingProgressBar);
                }
                else if(params[0].toString()=="FamilyValues")
                {

                    //String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_FAMILYVALUES+"Language="+userModel.getLanguage(),"FamilyValuesId",
                            "FamilyValuesName", editText_familyValues, textView_familyValues,getContext(), customDialogLoadingProgressBar);
                }
                else if(params[0].equals("Diet"))
                {

                    dataFetcher.loadList(URLs.URL_GET_DIET+"Language="+userModel.getLanguage(),"DietId",
                            "DietName", editText_diet, textView_dietId, getContext(), customDialogLoadingProgressBar);
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

            //customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }

}
