package com.example.matrimonyapp.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.HomeActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.ViewMultipleDetailsAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteLanguageKnownDetails;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPersonalDetailsFragment extends Fragment {


    View view;
    private Bundle bundle;
    private String userId;

    TextView textView_name, textView_gender, textView_birthdate, textView_age, textView_bloodGroup,
            textView_permanentAddress, textView_permanentVillage, textView_permanentPostalCode,
            textView_permanentCountryName, textView_permanentStateName, textView_permanentCityName,
            textView_currentAddress, textView_currentVillage, textView_currentPostalCode,
            textView_currentCountryName, textView_currentStateName, textView_currentCityName,
            textView_birthTime, textView_birthTimeType, textView_birthPlace, textView_birthCountryName,
            textView_birthStateName, textView_birthCityName, textView_mobileNo,
            textView_alternetMobileNo, textView_emailId, textView_alternetEmailId,
            textView_motherTongue, textView_height, textView_weight, textView_colour,
            textView_maritalStatus, textView_familyStatus, textView_familyType, textView_familyValues,
            textView_disability, textView_disabilityType, textView_diet, textView_livesWithFamily,
            textView_religion, textView_caste, textView_subCaste, textView_gothram, textView_dosh;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    UserModel userModel;


    private RecyclerView recyclerView_addLanguageKnown;
    private ViewMultipleDetailsAdapter viewDetails_languageKnown;
    private ArrayList<AddPersonModel> addPersonModelArrayList_languageKnown;
    private SQLiteLanguageKnownDetails sqLiteLanguageKnownDetails;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_view_personal_details, container, false);

        init();

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");

        return view;

    }

    private void init() {

        bundle = this.getArguments();
        userId = bundle.getString("userId");

        textView_name = view.findViewById(R.id.textView_name);
        textView_gender = view.findViewById(R.id.textView_gender);
        textView_birthdate = view.findViewById(R.id.textView_birthdate);
        textView_age = view.findViewById(R.id.textView_age);
        textView_bloodGroup = view.findViewById(R.id.textView_bloodGroup);
        textView_motherTongue = view.findViewById(R.id.textView_motherTongue);
        textView_height = view.findViewById(R.id.textView_height);
        textView_weight = view.findViewById(R.id.textView_weight);
        textView_colour = view.findViewById(R.id.textView_colour);
        textView_maritalStatus = view.findViewById(R.id.textView_maritalStatus);
        textView_familyStatus = view.findViewById(R.id.textView_familyStatus);
        textView_familyType = view.findViewById(R.id.textView_familyType);
        textView_familyValues = view.findViewById(R.id.textView_familyValues);
        textView_disability = view.findViewById(R.id.textView_disability);
        textView_disabilityType = view.findViewById(R.id.textView_disabilityType);
        textView_diet = view.findViewById(R.id.textView_diet);
        textView_livesWithFamily = view.findViewById(R.id.textView_livesWithFamily);
        textView_religion = view.findViewById(R.id.textView_religion);
        textView_caste = view.findViewById(R.id.textView_caste);
        textView_subCaste = view.findViewById(R.id.textView_subCaste);
        textView_gothram = view.findViewById(R.id.textView_gothram);
        textView_dosh = view.findViewById(R.id.textView_dosh);

        textView_permanentAddress = view.findViewById(R.id.textView_permanentAddress);
        textView_permanentVillage = view.findViewById(R.id.textView_permanentVillage);
        textView_permanentPostalCode = view.findViewById(R.id.textView_permanentPostalCode);
        textView_permanentCountryName = view.findViewById(R.id.textView_permanentCountryName);
        textView_permanentStateName = view.findViewById(R.id.textView_permanentStateName);
        textView_permanentCityName = view.findViewById(R.id.textView_permanentCityName);

        textView_currentAddress = view.findViewById(R.id.textView_currentAddress);
        textView_currentVillage = view.findViewById(R.id.textView_currentVillage);
        textView_currentPostalCode = view.findViewById(R.id.textView_currentPostalCode);
        textView_currentCountryName = view.findViewById(R.id.textView_currentCountryName);
        textView_currentStateName = view.findViewById(R.id.textView_currentStateName);
        textView_currentCityName = view.findViewById(R.id.textView_currentCityName);

        textView_birthTime = view.findViewById(R.id.textView_birthTime);
        textView_birthTimeType = view.findViewById(R.id.textView_birthTimeType);
        textView_birthPlace = view.findViewById(R.id.textView_birthPlace);
        textView_birthCountryName = view.findViewById(R.id.textView_birthCountryName);
        textView_birthStateName = view.findViewById(R.id.textView_birthStateName);
        textView_birthCityName = view.findViewById(R.id.textView_birthCityName);

        textView_mobileNo = view.findViewById(R.id.textView_mobileNo);
        textView_alternetMobileNo = view.findViewById(R.id.textView_alternetMobileNo);
        textView_emailId = view.findViewById(R.id.textView_emailId);
        textView_alternetEmailId = view.findViewById(R.id.textView_alternetEmailId);

        recyclerView_addLanguageKnown = view.findViewById(R.id.recyclerView_addLanguageKnown);

        //textView_dosh = view.findViewById(R.id.textView_dosh);

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        addPersonModelArrayList_languageKnown = new ArrayList<>();
        viewDetails_languageKnown = new ViewMultipleDetailsAdapter(getContext(), addPersonModelArrayList_languageKnown, "ViewLanguage");
        recyclerView_addLanguageKnown.setAdapter(viewDetails_languageKnown);
        recyclerView_addLanguageKnown.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_education = new LinearLayoutManager(getContext());
        recyclerView_addLanguageKnown.setLayoutManager(linearLayoutManager_education);

        sqLiteLanguageKnownDetails= new SQLiteLanguageKnownDetails(getContext());



    }


    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {

            if (params[0].equals("InsertDetails")) {
                //insertDetails();
            } else if (params[0].equals("getDetails")) {
                getBasicDetails();
                getReligiousDetails();
                getPersonalDetails();
            }


            return null;
        }


        @Override
        protected void onPreExecute() {
            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
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


    void getBasicDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_BASICDETAILS + "UserId=" +userId+ "&Language=" + userModel.getLanguage(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {


                                    textView_name.setText(jsonObject.getString("FullName"));
                                    textView_bloodGroup.setText(jsonObject.getString("BloodGroupName"));

                                    textView_mobileNo.setText(jsonObject.getString("MobileNo"));
                                    textView_alternetMobileNo.setText(jsonObject.getString("AlternateNo"));
                                    textView_emailId.setText(jsonObject.getString("EmailId"));
                                    textView_age.setText(jsonObject.getString("Age"));
                                    textView_alternetEmailId.setText(jsonObject.getString("AlternateEmail"));

                                    textView_permanentAddress.setText(jsonObject.getString("PermanantAddress"));
                                    textView_permanentVillage.setText(jsonObject.getString("PermanantVillage"));
                                    textView_permanentPostalCode.setText(jsonObject.getString("PermanantPostCode"));
                                    textView_permanentCountryName.setText(jsonObject.getString("PermanantCountryName"));
                                    textView_permanentStateName.setText(jsonObject.getString("PermanantState"));
                                    textView_permanentCityName.setText(jsonObject.getString("PermanantCity"));



                                    textView_currentAddress.setText(jsonObject.getString("CurrentAddress"));
                                    textView_currentVillage.setText(jsonObject.getString("CurrentVillage"));
                                    textView_currentPostalCode.setText(jsonObject.getString("CurrentPostCode"));
                                    textView_currentCountryName.setText(jsonObject.getString("CurrentCountryName"));
                                    textView_currentStateName.setText(jsonObject.getString("CurrentStateName"));
                                    textView_currentCityName.setText(jsonObject.getString("CurrentCityName"));


                                    textView_birthdate.setText(jsonObject.getString("Birthdate"));
                                    textView_birthTimeType.setText(jsonObject.getString("AccuracyOfTime"));
                                    textView_birthPlace.setText(jsonObject.getString("BirthPlace"));
                                    textView_birthCountryName.setText(jsonObject.getString("BirthCountryName"));
                                    textView_birthStateName.setText(jsonObject.getString("BirthStateName"));
                                    textView_birthCityName.setText(jsonObject.getString("BirthCityName"));


                                    String birthTimeString = jsonObject.getString("BirthTimeString");
                                    String []t = birthTimeString.split(":");

                                    int hr = Integer.parseInt(t[0]);
                                    int min = Integer.parseInt(t[1]);


                                    String time="";
                                    String timeHrs="00:00:00";

                                    timeHrs = hr+":"+min+":00";

                                    if(hr>12)
                                    {
                                        hr = hr%12;

                                        time = String.format("%02d",hr)+":"+String.format("%02d",min)+"pm";
                                    }
                                    else if(hr==12)
                                    {
                                        time = String.format("%02d",hr)+":"+String.format("%02d",min)+"pm";
                                    }
                                    else
                                    {
                                        time = String.format("%02d",hr)+":"+String.format("%02d",min)+"am";
                                    }

                                    textView_birthTime.setText(time);

                                    /*basicDetailsId = jsonObject.getInt("BasicDetailsId");


                                    //editText_birthTime.setText(jsonObject.getString("BirthTimeString"));
                                    textView_birthdate.setText(jsonObject.getString("BirthPlace"));

                                    .setText(jsonObject.getString("BirthCountryName"));
                                    editText_birthState.setText(jsonObject.getString("BirthStateName"));
                                    editText_birthCity.setText(jsonObject.getString("BirthCityName"));

                                    textView_birthCountryId.setText(jsonObject.getString("BirthCountryId"));
                                    textView_birthStateId.setText(jsonObject.getString("BirthStateId"));
                                    textView_birthCityId.setText(jsonObject.getString("BirthCityId"));


                                    .setText(jsonObject.getString("BloodGroupName"));
                                    textView_bloodGroupId.setText(jsonObject.getString("BloodGroupId"));

                                    .setText(jsonObject.getString("AlternateNo"));
                                    editText_altEmailId.setText(jsonObject.getString("AlternateEmail"));

                                    .setText(jsonObject.getString("PermanantAddress"));
                                    editText_village.setText(jsonObject.getString("PermanantVillage"));

                                    editText_country.setText(jsonObject.getString("PermanantCountryName"));
                                    editText_state.setText(jsonObject.getString("PermanantState"));
                                    editText_city.setText(jsonObject.getString("PermanantCity"));

                                    textView_countryId.setText(jsonObject.getString("PermanantCountryId"));
                                    textView_stateId.setText(jsonObject.getString("PermanantStateId"));
                                    textView_countryId.setText(jsonObject.getString("PermanantCityId"));

                                    editText_currentAddress.setText(jsonObject.getString("CurrentAddress"));
                                    editText_currentVillage.setText(jsonObject.getString("CurrentVillage"));

                                    editText_currentCountry.setText(jsonObject.getString("CurrentCountryName"));
                                    editText_currentState.setText(jsonObject.getString("CurrentStateName"));
                                    editText_currentCity.setText(jsonObject.getString("CurrentCityName"));


                                    textView_currentCountryId.setText(jsonObject.getString("CurrentCountryId"));
                                    textView_currentStateId.setText(jsonObject.getString("CurrentStateId"));
                                    textView_currentCityId.setText(jsonObject.getString("CurrentCityId"));

*/
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();

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




    void getReligiousDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_RELIGIONDETAIL + "UserId=" + userId + "&Language=" + userModel.getLanguage(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {
                                JSONObject jsonObject = jsonArray.getJSONObject(0);

                                if (!jsonObject.getBoolean("error")) {
                                    // religionDetailsId = jsonObject.getInt("ReligiousDetailsId");

/*                                        textView_religionId.setText(jsonObject.getString("ReligionId"));
                                        textView_casteId.setText(jsonObject.getString("CasteId"));
                                        textView_subCasteId.setText(jsonObject.getString("SubCasteId"));
                                        textView_motherTongueId.setText(jsonObject.getString("MotherTongueId"));*/

                                    textView_religion.setText(jsonObject.getString("ReligionName"));
                                    textView_caste.setText(jsonObject.getString("CasteName"));
                                    textView_subCaste.setText(jsonObject.getString("SubCasteName"));
                                    //.setChecked(jsonObject.getBoolean("OtherCommunity"));
                                    textView_gothram.setText(jsonObject.getString("Gothram"));
                                    textView_dosh.setText(jsonObject.getString("Dosh"));
                                    textView_motherTongue.setText(jsonObject.getString("MotherTongueName"));


                                }


                            } else {
                                //  religionDetailsId = 0;
                                Toast.makeText(getContext(), "Invalid Details GET! ", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();

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


    void getPersonalDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_PERSONALDETAILS+"UserId="+userId+"&Language="+userModel.getLanguage(),
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
                                   // personalDetailsId = jsonObject.getInt("PersonalDetailsId");


                                    textView_height.setText(jsonObject.getString("Height")+"cms.");
                                    textView_weight.setText(jsonObject.getString("Weight")+"Kgs.");


/*                                    textView_skinColor.setText(jsonObject.getString("SkinColourId"));
                                    textView_maritalStatus.setText(jsonObject.getString("MaritalStatusId"));
                                    textView_familyStatus.setText(jsonObject.getString("FamilyStatusId"));
                                    textView_familyType.setText(jsonObject.getString("FamilyTypeId"));
                                    textView_familyValues.setText(jsonObject.getString("FamilyValuesId"));
                                    textView_dietId.setText(jsonObject.getString("DietId"));*/


                                    textView_diet.setText(jsonObject.getString("DietName"));

                                    textView_disabilityType.setText(jsonObject.getString("DisabilityType"));
                                    String livesWithFam = "";
                                    if(jsonObject.getBoolean("Disability"))
                                    {
                                        textView_disability.setText(R.string.yes);
                                    }
                                    else
                                    {
                                        textView_disability.setText(R.string.no);
                                    }
                                    if(jsonObject.getBoolean("LivesWithFamily"))
                                    {
                                        textView_livesWithFamily.setText(R.string.yes);
                                    }
                                    else
                                    {
                                        textView_livesWithFamily.setText(R.string.no);
                                    }


                                    textView_colour.setText(jsonObject.getString("SkinColourName"));
                                    textView_maritalStatus.setText(jsonObject.getString("MaritalStatusName"));
                                    textView_familyStatus.setText(jsonObject.getString("FamilyStatusName"));
                                    textView_familyType.setText(jsonObject.getString("FamilyTypeName"));
                                    textView_familyValues.setText(jsonObject.getString("FamilyValuesName"));

                                    sqLiteLanguageKnownDetails.deleteAll();
                                    JSONArray knownLanguagesArray = jsonObject.getJSONArray("KnowLanguageLST");

                                    for (int i=0; i<knownLanguagesArray.length(); i++)
                                    {
                                        JSONObject knownLanguageObject = knownLanguagesArray.getJSONObject(i);

                                        String fluency = "";
                                        if(knownLanguageObject.getBoolean("IsFluent"))
                                        {
                                            fluency = getActivity().getResources().getString(R.string.fluent);
                                        }
                                        else
                                        {
                                            fluency = getActivity().getResources().getString(R.string.non_fluent);
                                        }

                                        long id = sqLiteLanguageKnownDetails.insertLanguageKnownDetails(
                                                knownLanguageObject.getString("KnownLanguageId"),
                                                knownLanguageObject.getString("MotherTongueName"),
                                                knownLanguageObject.getString("MotherTongueId"),
                                                fluency
                                        );
                                        AddPersonModel addPersonModel = new AddPersonModel(String.valueOf(id),
                                                knownLanguageObject.getString("KnownLanguageId"),
                                                knownLanguageObject.getString("MotherTongueName"),
                                                fluency);


                                        addPersonModelArrayList_languageKnown.add(addPersonModel);

                                    }

                                    viewDetails_languageKnown.notifyDataSetChanged();



                                    /*sqLiteLanguageKnownDetails.deleteAll();
                                    addPersonModelArrayList_languageKnown.clear();
                                    addPersonAdapter_languageKnown.notifyDataSetChanged();*/



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
                                //personalDetailsId = 0;
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


}