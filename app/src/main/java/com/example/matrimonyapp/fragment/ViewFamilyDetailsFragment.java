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
import com.example.matrimonyapp.adapter.ViewMultipleDetailsAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteLanguageKnownDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
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
public class ViewFamilyDetailsFragment extends Fragment {

    View view;

    private TextView textView_isFatherAlive, textView_fatherName, textView_fatherMobileNo, textView_fatherQualification,
            textView_fatherOccupation, textView_familyAnnualIncome, textView_fatherAddress,
            textView_fatherCountry, textView_fatherState, textView_fatherCity,
            textView_isMotherAlive, textView_motherName, textView_motherOccupation,
            textView_motherQualification, textView_motherOccupationType, textView_motherMobileNo,
            textView_relative1, textView_relative2, textView_relative3, textView_relative4,
            textView_noOfMama, textView_noOfSiblings;

    private RecyclerView recyclerView_siblingDetails, recyclerView_mamaDetails, recyclerView_propertyDetails,
            recyclerView_farmDetails;

    private SQLiteSiblingDetails sqLiteSiblingDetails;
    private SQLiteFarmDetails sqLiteFarmDetails;
    private SQLitePropertyDetails sqLitePropertyDetails;
    private SQLiteMamaDetails sqLiteMamaDetails;

    private ViewMultipleDetailsAdapter viewMultipleDetailsAdapter_sibling, viewMultipleDetailsAdapter_mama,
            viewMultipleDetailsAdapter_farm, viewMultipleDetailsAdapter_property;

    private ArrayList<AddPersonModel> arrayList_sibling, arrayList_mama, arrayList_property, arrayList_farm;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    UserModel userModel;

    private Bundle bundle;
    private String userId;

    public ViewFamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_view_family_details, container, false);

        init();


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("getDetails");
        return view;
    }

    private void init() {

        bundle = this.getArguments();
        userId = bundle.getString("userId");


        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();


        textView_isFatherAlive = view.findViewById(R.id.textView_isFatherAlive);
        textView_fatherName = view.findViewById(R.id.textView_fatherName);
        textView_fatherMobileNo = view.findViewById(R.id.textView_fatherMobileNo);
        textView_fatherQualification = view.findViewById(R.id.textView_fatherQualification);
        textView_fatherOccupation = view.findViewById(R.id.textView_fatherOccupation);
        textView_familyAnnualIncome = view.findViewById(R.id.textView_familyAnnualIncome);
        textView_fatherAddress = view.findViewById(R.id.textView_fatherAddress);
        textView_fatherCountry = view.findViewById(R.id.textView_fatherCountry);
        textView_fatherState = view.findViewById(R.id.textView_fatherState);
        textView_fatherCity = view.findViewById(R.id.textView_fatherCity);


        textView_isMotherAlive = view.findViewById(R.id.textView_isMotherAlive);
        textView_motherName = view.findViewById(R.id.textView_motherName);
        textView_motherOccupation = view.findViewById(R.id.textView_motherOccupation);
        textView_motherQualification = view.findViewById(R.id.textView_motherQualification);
        textView_motherOccupationType = view.findViewById(R.id.textView_motherOccupationType);
        textView_motherMobileNo = view.findViewById(R.id.textView_motherMobileNo);


        textView_noOfMama = view.findViewById(R.id.textView_noOfMama);
        textView_noOfSiblings = view.findViewById(R.id.textView_noOfSiblings);
        recyclerView_siblingDetails = view.findViewById(R.id.recyclerView_siblingDetails);
        recyclerView_mamaDetails = view.findViewById(R.id.recyclerView_mamaDetails);
        recyclerView_propertyDetails = view.findViewById(R.id.recyclerView_propertyDetails);
        recyclerView_farmDetails = view.findViewById(R.id.recyclerView_farmDetails);

        textView_relative1 = view.findViewById(R.id.textView_relative1);
        textView_relative2 = view.findViewById(R.id.textView_relative2);
        textView_relative3 = view.findViewById(R.id.textView_relative3);
        textView_relative4 = view.findViewById(R.id.textView_relative4);


        arrayList_sibling = new ArrayList<>();
        arrayList_mama = new ArrayList<>();
        arrayList_property = new ArrayList<>();
        arrayList_farm = new ArrayList<>();

        sqLiteSiblingDetails = new SQLiteSiblingDetails(getContext());
        sqLiteMamaDetails = new SQLiteMamaDetails(getContext());
        sqLiteFarmDetails = new SQLiteFarmDetails(getContext());
        sqLitePropertyDetails = new SQLitePropertyDetails(getContext());


        viewMultipleDetailsAdapter_sibling = new ViewMultipleDetailsAdapter(getContext(), arrayList_sibling, "Sibling");
        recyclerView_siblingDetails.setAdapter(viewMultipleDetailsAdapter_sibling);
        recyclerView_siblingDetails.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_sibling = new LinearLayoutManager(getContext());
        recyclerView_siblingDetails.setLayoutManager(linearLayoutManager_sibling);


        viewMultipleDetailsAdapter_mama = new ViewMultipleDetailsAdapter(getContext(), arrayList_mama, "Mama");
        recyclerView_mamaDetails.setAdapter(viewMultipleDetailsAdapter_mama);
        recyclerView_mamaDetails.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_mama = new LinearLayoutManager(getContext());
        recyclerView_mamaDetails.setLayoutManager(linearLayoutManager_mama);


        viewMultipleDetailsAdapter_farm = new ViewMultipleDetailsAdapter(getContext(), arrayList_farm, "Farm");
        recyclerView_farmDetails.setAdapter(viewMultipleDetailsAdapter_farm);
        recyclerView_farmDetails.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_farm = new LinearLayoutManager(getContext());
        recyclerView_farmDetails.setLayoutManager(linearLayoutManager_farm);


        viewMultipleDetailsAdapter_property = new ViewMultipleDetailsAdapter(getContext(), arrayList_property, "Property");
        recyclerView_propertyDetails.setAdapter(viewMultipleDetailsAdapter_property);
        recyclerView_propertyDetails.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_property = new LinearLayoutManager(getContext());
        recyclerView_propertyDetails.setLayoutManager(linearLayoutManager_property);


        // textView_name = view.findViewById(R.id.textView_name);


    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {

            if (params[0].equals("InsertDetails")) {
                //insertDetails();
            } else if (params[0].equals("getDetails")) {
                getFamilyDetails();
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

    void getFamilyDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_FAMILYDETAILS + "UserId=" + userId + "&Language=" + userModel.getLanguage(),
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

                                    textView_fatherName.setText(jsonObject.getString("FullnameFather"));
                                    textView_fatherMobileNo.setText(jsonObject.getString("MobileNoFather"));
                                    textView_fatherAddress.setText(jsonObject.getString("AddressFather"));
                                    textView_fatherQualification.setText(jsonObject.getString("QualificationFather"));
                                    textView_fatherOccupation.setText(jsonObject.getString("OccupationNameFather"));
                                    textView_fatherCountry.setText(jsonObject.getString("FatherCountryName"));
                                    textView_fatherState.setText(jsonObject.getString("FatherStateName"));
                                    textView_fatherCity.setText(jsonObject.getString("FatherCityName"));

                                    textView_motherName.setText(jsonObject.getString("FullnameMother"));
                                    textView_motherMobileNo.setText(jsonObject.getString("MobileNoMother"));
                                    textView_motherQualification.setText(jsonObject.getString("QualificationMother"));
                                    textView_motherOccupation.setText(jsonObject.getString("OccupationNameMother"));

                                    textView_familyAnnualIncome.setText(jsonObject.getString("SalaryPackageId"));

                                    getSiblingDetails(jsonObject);
                                    getFarmDetailsXML(jsonObject);
                                    getMamaDetailsXML(jsonObject);
                                    getPropertyDetailsXML(jsonObject);



                                    if(jsonObject.getString("IsAliveFather").equals("1"))
                                    {
                                        textView_isFatherAlive.setText(getActivity().getResources().getString(R.string.yes));
                                    }
                                    else
                                    {
                                        textView_isFatherAlive.setText(getActivity().getResources().getString(R.string.no));
                                    }

                                    if(jsonObject.getString("IsAliveMother").equals("1"))
                                    {
                                        textView_isMotherAlive.setText(getActivity().getResources().getString(R.string.yes));
                                    }
                                    else
                                    {
                                        textView_isMotherAlive.setText(getActivity().getResources().getString(R.string.no));
                                    }


                                    textView_relative1.setText(jsonObject.getString("Surname1"));
                                    textView_relative2.setText(jsonObject.getString("Surname2"));
                                    textView_relative3.setText(jsonObject.getString("Surname3"));
                                    textView_relative4.setText(jsonObject.getString("Surname4"));
                                   /* .setText(jsonObject.getString(""));
                                    .setText(jsonObject.getString(""));*/





                                }


                            } else {
                                //qualificationDetailsId = 0;
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

    private void getPropertyDetailsXML(JSONObject jsonObject) {

        try {
            sqLitePropertyDetails.deleteAll();
            arrayList_property.clear();
            viewMultipleDetailsAdapter_property.notifyDataSetChanged();
            JSONArray jsonArray_propertyDetails = jsonObject.getJSONArray("HousePropertyDetailsLST");

            for(int j=0; j< jsonArray_propertyDetails.length(); j++)
            {

                JSONObject jsonObject_details = jsonArray_propertyDetails.getJSONObject(j);


                long id = sqLitePropertyDetails.insertPropertyDetails(
                        jsonObject_details.getString("HousePropertyDetailsId"),
                        jsonObject_details.getString("PropertyName"),
                        jsonObject_details.getString("PropertyTypeId"),
                        jsonObject_details.getString("PropertyOwnershipType"),
                        jsonObject_details.getString("PropertyBHKType"),
                        jsonObject_details.getString("PropertyBHKType"),
                        jsonObject_details.getString("PropertyArea"),
                        jsonObject_details.getString("PropertyAddress"),
                        jsonObject_details.getString("CountryName"),
                        jsonObject_details.getString("PropertyCountryId"),
                        jsonObject_details.getString("StateName"),
                        jsonObject_details.getString("PropertyStateId"),
                        jsonObject_details.getString("CityName"),
                        jsonObject_details.getString("PropertyCityId"));


                arrayList_property.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("HousePropertyDetailsId"),
                        jsonObject_details.getString("PropertyArea")+" sq. ft. "+jsonObject_details.getString("PropertyName"),
                        "in "+jsonObject_details.getString("PropertyAddress")));

            }

            viewMultipleDetailsAdapter_property.notifyDataSetChanged();


        }

        catch (JSONException jsonException)
        {

        }
    }

    private void getMamaDetailsXML(JSONObject jsonObject){


        try {
            sqLiteMamaDetails.deleteAll();
            arrayList_mama.clear();
            viewMultipleDetailsAdapter_mama.notifyDataSetChanged();
            JSONArray jsonArray_mamaDetails = jsonObject.getJSONArray("MamaDetailsLST");






            for(int j=0; j< jsonArray_mamaDetails.length(); j++)
            {
                JSONObject jsonObject_details = jsonArray_mamaDetails.getJSONObject(j);

                long id = sqLiteMamaDetails.insertMamaDetails(
                        jsonObject_details.getString("MamaDetailsId"),
                        jsonObject_details.getString("MamaFullnameAPI"),
                        jsonObject_details.getString("MamaMobileNoAPI"),
                        jsonObject_details.getString("OccupationIdMama"),
                        jsonObject_details.getString("OccupationNameMama"),
                        jsonObject_details.getString("MamaAddressAPI"),
                        jsonObject_details.getString("MamaCountryName"),
                        jsonObject_details.getString("MamaCountryId"),
                        jsonObject_details.getString("MamaStateName"),
                        jsonObject_details.getString("MamaStateId"),
                        jsonObject_details.getString("MamaCityName"),
                        jsonObject_details.getString("MamaCityId"),
                        jsonObject_details.getString("IsAliveMama"));

                arrayList_mama.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("MamaDetailsId"),
                        jsonObject_details.getString("MamaFullnameAPI"),
                        jsonObject_details.getString("MamaAddressAPI")));


            }

            viewMultipleDetailsAdapter_mama.notifyDataSetChanged();
            textView_noOfMama.setText(String.valueOf(jsonArray_mamaDetails.length()));

        }
        catch (JSONException jsonException)
        {

        }



    }

    private void getFarmDetailsXML(JSONObject jsonObject) {

        try {
            sqLiteFarmDetails.deleteAll();
            arrayList_farm.clear();
            viewMultipleDetailsAdapter_farm.notifyDataSetChanged();
            JSONArray jsonArray_farmDetails = jsonObject.getJSONArray("FarmDetailsLST");

            for(int j=0; j< jsonArray_farmDetails.length(); j++) {
                JSONObject jsonObject_details = jsonArray_farmDetails.getJSONObject(j);


                long id = sqLiteFarmDetails.insertFarmDetails(
                        jsonObject_details.getString("FarmPropertyDetailsId"),
                        jsonObject_details.getString("LandArea"),
                        jsonObject_details.getString("FullOrPart"),
                        jsonObject_details.getString("CropTaken"),"Irrigated","","");


                arrayList_farm.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("FarmPropertyDetailsId"),
                        jsonObject_details.getString("LandArea")+" sq. ft.",
                        jsonObject_details.getString("CropTaken")));



            }
            viewMultipleDetailsAdapter_farm.notifyDataSetChanged();
            //farmRecyclerView();

        }
        catch (JSONException jsonException)
        {}




    }

    private void getSiblingDetails(JSONObject jsonObject) {

        sqLiteSiblingDetails.deleteAll();
        arrayList_sibling.clear();
        viewMultipleDetailsAdapter_sibling.notifyDataSetChanged();

        try {
            JSONArray jsonArray_sibling = jsonObject.getJSONArray("SiblingsDetailsLST");

            for (int i = 0; i < jsonArray_sibling.length(); i++) {
                JSONObject jsonObject_details = jsonArray_sibling.getJSONObject(i);



                long id = sqLiteSiblingDetails.insertSibling(
                        jsonObject_details.getString("SiblingsDetailsId"),
                        jsonObject_details.getString("SiblingsFullname"),
                        jsonObject_details.getString("MobileNoSiblings"),
                        jsonObject_details.getString("QualificationIdSiblings"),
                        jsonObject_details.getString("QualificationSiblings"),
                        jsonObject_details.getString("OccupationIdSiblings"),
                        jsonObject_details.getString("OccupationNameSiblings"),
                        jsonObject_details.getString("MaritalStatus"),
                        jsonObject_details.getString("SiblingListIdAPI"),
                        "Brother", //jsonObject_details.getString("Brother")/relationName
                        jsonObject_details.getString("SiblingSpouseName"),
                        jsonObject_details.getString("InLawsFullNameAPI"),
                        jsonObject_details.getString("InLawsMobileNoAPI"),
                        jsonObject_details.getString("InLawsAddressAPI"),
                        jsonObject_details.getString("InLawsCountryId"),
                        jsonObject_details.getString("InLawsStateId"),
                        jsonObject_details.getString("InLawsCityId"),
                        jsonObject_details.getString("CountryName"),
                        jsonObject_details.getString("StateName"),
                        jsonObject_details.getString("StateName")

                );
                AddPersonModel addPersonModel = new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("SiblingsDetailsId"),
                        jsonObject_details.getString("SiblingsFullname"),
                        jsonObject_details.getString("QualificationSiblings")
                );


                arrayList_sibling.add(addPersonModel);

            }

            viewMultipleDetailsAdapter_sibling.notifyDataSetChanged();
            textView_noOfSiblings.setText(String.valueOf(jsonArray_sibling.length()));


        } catch (JSONException jsonException) {

        }

    }
}