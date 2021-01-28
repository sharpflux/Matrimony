package com.example.matrimonyapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.ChatAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.TimelineAdapter;
import com.example.matrimonyapp.customViews.CustomDialogAddFarm;
import com.example.matrimonyapp.customViews.CustomDialogAddMama;
import com.example.matrimonyapp.customViews.CustomDialogAddProperty;
import com.example.matrimonyapp.customViews.CustomDialogAddSibling;
import com.example.matrimonyapp.customViews.CustomDialogAddVehicle;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.sqlite.SQLiteVehicleDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.validation.StateDistrictTalukaValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.textfield.TextInputLayout;
import com.suke.widget.SwitchButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyDetailsFragment extends Fragment {

    View view;
    private Context context;
    private TextView textView_fatherQualificationId, textView_fatherOccupationId, textView_fatherStateId,
            textView_fatherCountryId, textView_fatherCityId, textView_saveAndContinue,
            textView_motherQualificationId, textView_motherOccupationId, textView_familyIncome;

    private EditText editText_fatherName, editText_fatherMobileNo, editText_fatherQualification,
            editText_fatherOccupation, editText_fatherState, editText_fatherCountry, editText_fatherCity,
            editText_fatherAddress, editText_motherName, editText_motherMobileNo, editText_motherQualification,
            editText_motherOccupation, editText_aliveMama, editText_totalMama, editText_expiredMama,
            editText_motherIncome, editText_fatherIncome, editText_relative1, editText_relative2,
            editText_relative3, editText_relative4, editText_familyIncome;


    private ImageView imageView_back, imageView_addProperty, imageView_addVehicle, imageView_addSibling, imageView_addMama, imageView_addFarm;
    private LinearLayout linearlayout_editDetails;
    private RadioGroup radioGroup_motherServiceType;
    private RadioButton radioButton_serviceWoman, radioButton_housewife;

    private String fatherName, fatherMobileNo, fatherOccupationId, fatherQualificationId, fatherAnnualIncome,
            fatherStateId, fatherCountryId, fatherCityId, fatherAddress, motherName, motherMobileNo, motherQualificationId,
            motherOccupation, familyIncome, relative1, relative2, relative3, relative4;

    //SwitchButton switchButton_haveFather, switchButton_haveMother;
    CheckBox checkBox_fatherIsAlive, checkBox_motherIsAlive, checkBox_ownFourWheeler, checkBox_ownTwoWheeler;

    Bundle bundle;

    private StringBuilder stringBuilder_sibling, stringBuilder_mama, stringBuilder_property,
            stringBuilder_farm, stringBuilder_vehicle, stringBuilder_extraInfo;

    private DataFetcher dataFetcher;

    private UserModel userModel;

    private ArrayList<AddPersonModel> addPersonModelArrayList_sibling, addPersonModelArrayList_mama,
            addPersonModelArrayList_property, addPersonModelArrayList_farm, addPersonModelArrayList_vehicle;


    private RecyclerView recyclerView_addSibling, recyclerView_addMama, recyclerView_addProperty,
            recyclerView_addFarm, recyclerView_addVehicle;

    private AddPersonAdapter addPersonAdapter_sibling, addPersonAdapter_mama, addPersonAdapter_property,
            addPersonAdapter_farm, addPersonAdapter_vehicle;

    private SQLiteSiblingDetails sqLiteSiblingDetails;
    private SQLiteMamaDetails sqLiteMamaDetails;
    private SQLitePropertyDetails sqLitePropertyDetails;
    private SQLiteFarmDetails sqLiteFarmDetails;
    private SQLiteVehicleDetails sqLiteVehicleDetails;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    private CustomDialogAddSibling customDialogAddSibling;
    private CustomDialogAddMama customDialogAddMama;
    //private CustomDialogAddVehicle customDialogAddVehicle;

    protected int familyDetailsId = 0;
    protected int fatherDetailsId = 0;
    protected int motherDetailsId = 0;
    private TextInputLayout textInputLayout_motherOccupation;
    private int currentCountryId = 0, currentStateId = 0, newCountryId = 0, newStateId = 0;

    public FamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_family_details, container, false);

        context = getContext();

        bundle = getArguments();


        if (!CustomSharedPreference.getInstance(getContext()).isLoggedIn()) {
            startActivity(new Intent(getContext(), LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();


        checkBox_fatherIsAlive = view.findViewById(R.id.checkBox_fatherIsAlive);
        editText_fatherName = view.findViewById(R.id.editText_fatherName);
        editText_fatherMobileNo = view.findViewById(R.id.editText_fatherMobileNo);
        editText_fatherQualification = view.findViewById(R.id.editText_fatherQualification);
        textView_fatherQualificationId = view.findViewById(R.id.textView_fatherQualificationId);
        editText_fatherOccupation = view.findViewById(R.id.editText_fatherOccupation);
        editText_fatherIncome = view.findViewById(R.id.editText_fatherIncome);

        editText_fatherState = view.findViewById(R.id.editText_fatherState);
        textView_fatherStateId = view.findViewById(R.id.textView_fatherStateId);
        editText_fatherCountry = view.findViewById(R.id.editText_fatherCountry);
        textView_fatherCountryId = view.findViewById(R.id.textView_fatherCountryId);
        editText_fatherCity = view.findViewById(R.id.editText_fatherCity);
        textView_fatherCityId = view.findViewById(R.id.textView_fatherCityId);


        editText_familyIncome = view.findViewById(R.id.editText_familyIncome);
        textView_familyIncome = view.findViewById(R.id.textView_familyIncome);
        //editText_fatherProperty = view.findViewById(R.id.editText_fatherProperty);
        editText_fatherAddress = view.findViewById(R.id.editText_fatherAddress);

        checkBox_motherIsAlive = view.findViewById(R.id.checkBox_motherIsAlive);
        editText_motherName = view.findViewById(R.id.editText_motherName);
        editText_motherMobileNo = view.findViewById(R.id.editText_motherMobileNo);
        editText_motherQualification = view.findViewById(R.id.editText_motherQualification);
        textView_motherQualificationId = view.findViewById(R.id.textView_motherQualificationId);
        radioGroup_motherServiceType = view.findViewById(R.id.radioGroup_motherServiceType);
        radioButton_serviceWoman = view.findViewById(R.id.radioButton_serviceWoman);
        radioButton_housewife = view.findViewById(R.id.radioButton_housewife);
        editText_motherOccupation = view.findViewById(R.id.editText_motherOccupation);
        editText_motherIncome = view.findViewById(R.id.editText_motherIncome);


        checkBox_ownFourWheeler = view.findViewById(R.id.checkBox_ownFourWheeler);
        checkBox_ownTwoWheeler = view.findViewById(R.id.checkBox_ownTwoWheeler);

        editText_relative1 = view.findViewById(R.id.editText_relative1);
        editText_relative2 = view.findViewById(R.id.editText_relative2);
        editText_relative3 = view.findViewById(R.id.editText_relative3);
        editText_relative4 = view.findViewById(R.id.editText_relative4);

        //textView_noOfSiblings = view.findViewById(R.id.textView_noOfSiblings);
        //imageView_add = view.findViewById(R.id.imageView_add);
        imageView_addSibling = view.findViewById(R.id.imageView_addSibling);
        imageView_addMama = view.findViewById(R.id.imageView_addMama);
        imageView_addProperty = view.findViewById(R.id.imageView_addProperty);
        imageView_addVehicle = view.findViewById(R.id.imageView_addVehicle);
        linearlayout_editDetails = view.findViewById(R.id.linearlayout_editDetails);

        recyclerView_addSibling = view.findViewById(R.id.recyclerView_addSibling);
        recyclerView_addMama = view.findViewById(R.id.recyclerView_addMama);
        recyclerView_addProperty = view.findViewById(R.id.recyclerView_addProperty);
        recyclerView_addFarm = view.findViewById(R.id.recyclerView_addFarm);
        recyclerView_addVehicle = view.findViewById(R.id.recyclerView_addVehicle);


        textView_fatherOccupationId = view.findViewById(R.id.textView_fatherOccupationId);
        textView_motherOccupationId = view.findViewById(R.id.textView_motherOccupationId);
        textInputLayout_motherOccupation = view.findViewById(R.id.textInputLayout_motherOccupation);


        editText_totalMama = view.findViewById(R.id.editText_totalMama);
        editText_expiredMama = view.findViewById(R.id.editText_expiredMama );
        editText_aliveMama = view.findViewById(R.id.editText_aliveMama);

        //switchButton_haveMama = view.findViewById(R.id.switchButton_haveMama);

        textView_saveAndContinue = ((MainActivity) getActivity()).findViewById(R.id.txt_saveAndContinue);
        imageView_back = ((MainActivity) getActivity()).findViewById(R.id.imageView_back);
        TextView tv = ((MainActivity) getActivity()).findViewById(R.id.textView_toolbar);
        //tv.setText("Family Details");
        tv.setText(context.getResources().getString(R.string.family_details));


        dataFetcher = new DataFetcher("State", getContext());

        stringBuilder_sibling = new StringBuilder();
        stringBuilder_mama = new StringBuilder();
        stringBuilder_property = new StringBuilder();
        stringBuilder_farm = new StringBuilder();
        stringBuilder_vehicle = new StringBuilder();
        stringBuilder_extraInfo = new StringBuilder();


        AsyncTaskLoad getTask = new AsyncTaskLoad();
        getTask.execute("getDetails");


        // Sibling recycleView
        addPersonModelArrayList_sibling = new ArrayList<>();

        sqLiteSiblingDetails = new SQLiteSiblingDetails(getContext());


        addPersonAdapter_sibling = new AddPersonAdapter(getContext(), addPersonModelArrayList_sibling, "Sibling");
        recyclerView_addSibling.setAdapter(addPersonAdapter_sibling);
        recyclerView_addSibling.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_sibling = new LinearLayoutManager(getContext());
        recyclerView_addSibling.setLayoutManager(linearLayoutManager_sibling);

        //textView_noOfSiblings.setText(String.valueOf(sqLiteSiblingDetails.numberOfRows()));


        // Mama recyclerView
        addPersonModelArrayList_mama = new ArrayList<>();

        sqLiteMamaDetails = new SQLiteMamaDetails(getContext());


        addPersonAdapter_mama = new AddPersonAdapter(getContext(), addPersonModelArrayList_mama, "Mama");
        recyclerView_addMama.setAdapter(addPersonAdapter_mama);
        recyclerView_addMama.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_mama = new LinearLayoutManager(getContext());
        recyclerView_addMama.setLayoutManager(linearLayoutManager_mama);


        // Property recyclerView
        addPersonModelArrayList_property = new ArrayList<>();

        sqLitePropertyDetails = new SQLitePropertyDetails(getContext());


        addPersonAdapter_property = new AddPersonAdapter(getContext(), addPersonModelArrayList_property,
                "Property");
        recyclerView_addProperty.setAdapter(addPersonAdapter_property);
        recyclerView_addProperty.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_property = new LinearLayoutManager(getContext());
        recyclerView_addProperty.setLayoutManager(linearLayoutManager_property);


        //VehicleRecyclerView();
        // Vehicle recyclerView
        addPersonModelArrayList_vehicle = new ArrayList<>();

        sqLiteVehicleDetails = new SQLiteVehicleDetails(getContext());


        addPersonAdapter_vehicle = new AddPersonAdapter(getContext(), addPersonModelArrayList_vehicle,
                "Vehicle");
        recyclerView_addVehicle.setAdapter(addPersonAdapter_vehicle);
        recyclerView_addVehicle.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_vehicle = new LinearLayoutManager(getContext());
        recyclerView_addVehicle.setLayoutManager(linearLayoutManager_vehicle);

        //farmRecyclerView();
        // Farm recyclerView
        addPersonModelArrayList_farm = new ArrayList<>();

        sqLiteFarmDetails = new SQLiteFarmDetails(getContext());


        addPersonAdapter_farm = new AddPersonAdapter(getContext(), addPersonModelArrayList_farm,
                "Farm");
        recyclerView_addFarm.setAdapter(addPersonAdapter_farm);
        recyclerView_addFarm.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager_farm = new LinearLayoutManager(getContext());
        recyclerView_addFarm.setLayoutManager(linearLayoutManager_farm);


        showPopup(editText_motherQualification, "MotherQualification");
        showPopup(editText_fatherQualification, "FatherQualification");
        showPopup(editText_fatherOccupation, "FatherOccupation");
        showPopup(editText_motherOccupation, "MotherOccupation");
        showPopup(editText_familyIncome, "FamilyIncome");


        onClickListener();

        showPopUpCSC(editText_fatherCountry, "FatherCountry");
        showPopUpCSC(editText_fatherState, "FatherState");
        showPopUpCSC(editText_fatherCity, "FatherCity");

        /*showPopupSDT(editText_fatherState, "FatherState", null);
        showPopupSDT(editText_fatherDistrict, "FatherDistrict", textView_fatherStateId);
        showPopupSDT(editText_fatherTaluka, "FatherTaluka", textView_fatherDistrictId);
        textChangedListener(editText_fatherState, editText_fatherDistrict, editText_fatherTaluka,
                textView_fatherStateId, textView_fatherDistrictId, textView_fatherTalukaId);
*/

        return view;
    }


    private void showPopUpCSC(final EditText editText, final String urlFor) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();

                String id = "";

                if (urlFor.equals("Country")) {

                } else if (urlFor.equals("State")) {
                    id = textView_fatherCountryId.getText().toString();
                    if (id.equals("0")) {
                        Toast.makeText(context, "Please select Country first", Toast.LENGTH_SHORT).show();
                        return;
                    }

                } else if (urlFor.equals("City")) {
                    id = textView_fatherStateId.getText().toString();
                    if (id.equals("0")) {
                        Toast.makeText(context, "Please select State first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                runner.execute(urlFor, id);

            }
        });


    }


    public void showPopupSDT(EditText editText, final String urlFor, final TextView textView_id) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String id = "0";

                if (textView_id != null) {
                    id = textView_id.getText().toString();

                    if (id.equals("0")) {
                        if (urlFor.contains("District")) {
                            Toast.makeText(getContext(), "Select State first", Toast.LENGTH_SHORT).show();
                        } else if (urlFor.contains("Taluka")) {
                            Toast.makeText(getContext(), "Select District first", Toast.LENGTH_SHORT).show();
                        }
                        return;
                    }

                }


                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute(urlFor, id);
            }
        });


    }


    private void textChangedListener(EditText editText_state, final EditText editText_district,
                                     final EditText editText_taluka, TextView textView_stateId,
                                     final TextView textView_districtId, final TextView textView_talukaId) {

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


    public void onClickListener() {

        radioGroup_motherServiceType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {

                if (id == R.id.radioButton_serviceWoman) {

                    textInputLayout_motherOccupation.setVisibility(View.VISIBLE);
                    textInputLayout_motherOccupation.requestFocus();
                } else if (id == R.id.radioButton_housewife) {

                    textInputLayout_motherOccupation.setVisibility(View.GONE);
                    editText_motherOccupation.setText("");
                    editText_motherOccupation.clearFocus();
                    textView_motherOccupationId.setText("0");

                }

            }
        });


        imageView_addVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogAddVehicle customDialogAddVehicle = new CustomDialogAddVehicle(getContext(), "0", "0",
                        addPersonAdapter_vehicle, addPersonModelArrayList_vehicle, 0);
                customDialogAddVehicle.show();

            }
        });


        imageView_addProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogAddProperty customDialogAddProperty = new CustomDialogAddProperty(getContext(), "0", "0",
                        addPersonAdapter_property, addPersonModelArrayList_property, 0);
                customDialogAddProperty.show();

            }
        });

        linearlayout_editDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogAddFarm customDialogAddFarm = new CustomDialogAddFarm(getContext(), CustomDialogAddFarm.id, "0",
                        addPersonAdapter_farm, addPersonModelArrayList_farm, 0);

                customDialogAddFarm.show();

            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                if (fragmentManager.getBackStackEntryCount() > 0)
                {
                    fragmentManager.popBackStack();
                }
                else
                {
                    getActivity().finish();
                }
            }
        });


        imageView_addSibling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialogAddSibling = new CustomDialogAddSibling(getContext(), "0", "0",
                        addPersonAdapter_sibling, addPersonModelArrayList_sibling, 0);
                customDialogAddSibling.show();

            }
        });


        imageView_addMama.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                customDialogAddMama = new CustomDialogAddMama(getContext(), "0", "0",
                        addPersonAdapter_mama, addPersonModelArrayList_mama, 0);
                customDialogAddMama.show();

            }
        });


        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute("insertDetails");
/*
                fatherName = editText_fatherName.getText().toString();
                fatherMobileNo = editText_fatherMobileNo.getText().toString();
                fatherOccupationId = editText_fatherOccupation.getText().toString();
                fatherAnnualIncome= editText_fatherAnnualIncome.getText().toString();
                //fatherProperty= editText_fatherProperty.getText().toString();
                fatherAddress= editText_fatherAddress.getText().toString();
                motherName= editText_motherName.getText().toString();
                motherMobileNo= editText_motherMobileNo.getText().toString();
                motherOccupation= editText_motherOccupation.getText().toString();
                motherAnnualIncome= editText_motherAnnualIncome.getText().toString();

                relative1= editText_relative1.getText().toString();*/
                /*relative2= editText_relative2.getText().toString();
                relative3= editText_relative3.getText().toString();
                relative4= editText_relative4.getText().toString();*/

                /*= view.findViewById(R.id.);
                = view.findViewById(R.id.);*/




/*                if(bundle!=null)
                {
                    bulinearlayout_addMoreFarmndle.putString("fatherName",fatherName);
                    bundle.putString("fatherMobileNo",fatherMobileNo);
                    bundle.putString("fatherOccupation",fatherOccupationId);
                    bundle.putString("fatherAnnualIncome",fatherAnnualIncome);


                }*/


/*                bundle.putString("",);
                bundle.putString("",);*/

/*                QualificationDetailsFragment qualificationDetailsFragment = new QualificationDetailsFragment();
                //qualificationDetailsFragment.setArguments(bundle);

                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, qualificationDetailsFragment);
                fragmentTransaction.commit();*/

                getActivity().finish();

            }
        });


    }


    private void showPopup(EditText editText, final String urlFor) {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad runner = new AsyncTaskLoad();
                runner.execute(urlFor);
            }
        });


    }

    void insertDetails() {
        final String father_isAlive = checkBox_fatherIsAlive.isChecked() ? "1" : "0";
        fatherName = editText_fatherName.getText().toString().trim();
        fatherMobileNo = editText_fatherMobileNo.getText().toString().trim();
        fatherQualificationId = textView_fatherQualificationId.getText().toString().trim();
        fatherOccupationId = textView_fatherOccupationId.getText().toString().trim();
        //fatherAnnualIncome = editText_fatherAnnualIncome.getText().toString().trim();
        //fatherProperty= editText_fatherProperty.getText().toString();
        fatherAddress = editText_fatherAddress.getText().toString().trim();
        fatherStateId = textView_fatherStateId.getText().toString().trim();
        fatherCountryId = textView_fatherCountryId.getText().toString().trim();
        fatherCityId = textView_fatherCityId.getText().toString().trim();

        final String mother_isAlive = checkBox_motherIsAlive.isChecked() ? "1" : "0";
        motherName = editText_motherName.getText().toString().trim();
        motherMobileNo = editText_motherMobileNo.getText().toString().trim();
        motherQualificationId = textView_motherQualificationId.getText().toString().trim();
        motherOccupation = textView_motherOccupationId.getText().toString().trim();
        familyIncome = textView_familyIncome.getText().toString().trim();
        //motherAnnualIncome= editText_motherAnnualIncome.getText().toString().trim();

        relative1 = editText_relative1.getText().toString().trim();
        relative2 = editText_relative2.getText().toString().trim();
        relative3 = editText_relative3.getText().toString().trim();
        relative4 = editText_relative4.getText().toString().trim();

        final int noOfBrothers = sqLiteSiblingDetails.getNoOfSibling("Brother");
        final int noOfSisters = sqLiteSiblingDetails.getNoOfSibling("Sister");
        final int noOfMama = sqLiteMamaDetails.getNoOfMama();


        createSiblingXML();
        createPropertyXML();
        createMamaXML();
        createFarmXML();
        createExtraInfoXML();
        createVehicleXML();


        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_FAMILYDETAILS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.getString("message").equals("Success") &&
                                    !jsonObject.getBoolean("error")) {
                                //getDetails();

                                Toast.makeText(context, "Family details saved successfully!", Toast.LENGTH_SHORT).show();

                                getActivity().finish();

/*                                QualificationDetailsFragment qualificationDetailsFragment = new QualificationDetailsFragment();
                                //qualificationDetailsFragment.setArguments(bundle);

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, qualificationDetailsFragment);
                                fragmentTransaction.commit();
*/


                            } else {
                                Toast.makeText(getContext(), jsonObject.getString("message").toString(), Toast.LENGTH_SHORT).show();
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
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("FamilyDetailsId", String.valueOf(familyDetailsId));
                params.put("UserId", userModel.getUserId());
                params.put("FatherDetailsId", String.valueOf(fatherDetailsId));
                params.put("FatherIsAlive", father_isAlive);
                params.put("FatherFullname", fatherName);
                params.put("FatherMobileNo", fatherMobileNo);
                params.put("FatherAddress", fatherAddress);
                params.put("FatherStateId", fatherStateId);
                params.put("FatherCountryId", fatherCountryId);
                params.put("FatherCityId", fatherCityId);
                params.put("FatherQualificationId", fatherQualificationId);
                params.put("FatherOccupationId", fatherOccupationId);
                //params.put("FatherAnnualIncome",fatherAnnualIncome);
                params.put("FatherLanguageType", userModel.getLanguage());
                params.put("MotherDetailsId", String.valueOf(motherDetailsId));
                params.put("MotherIsAlive", mother_isAlive);
                params.put("MotherFullname", motherName);
                params.put("MotherMobileNo", motherMobileNo);
                params.put("MotherQualificationId", motherQualificationId);
                params.put("MotherOccupationId", motherOccupation);
                //params.put("MotherAnnualIncome",motherAnnualIncome);
                params.put("MotherLanguageType", userModel.getLanguage());
                params.put("NoOfBrothers", String.valueOf(noOfBrothers));
                params.put("NoOfSisters", String.valueOf(noOfSisters));
                params.put("NoOfMama", String.valueOf(noOfMama));
                params.put("SalaryPackageId", textView_familyIncome.getText().toString());
                params.put("Surname1", relative1);
                params.put("Surname2", relative2);
                params.put("Surname3", relative3);
                params.put("Surname4", relative4);
                params.put("HousePropertyDetailsAPI", stringBuilder_property.toString());
                params.put("FarmPropertyDetailsAPI", stringBuilder_farm.toString());
                params.put("SiblingsDetailsAPI", stringBuilder_sibling.toString());
                params.put("MamaDetailsAPI", stringBuilder_mama.toString());
               // params.put("ExtraInfo",stringBuilder_extraInfo.toString());
                params.put("LanguageType", userModel.getLanguage());

                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

    }

    private void createExtraInfoXML() {


        //ExtraInfo XML
        stringBuilder_extraInfo.append("<?xml version=\"1.0\" ?>");
        stringBuilder_extraInfo.append("<Assign>");


        stringBuilder_extraInfo.append("<Functions>");

        stringBuilder_extraInfo.append("<ExtraInfoDetailsId>" + "0" + "</ExtraInfoDetailsId>");
        stringBuilder_extraInfo.append("<FatherIncome>" + editText_fatherIncome.getText().toString()+ "</FatherIncome>");
        stringBuilder_extraInfo.append("<MotherIncome>" + editText_motherIncome.getText().toString() + "</MotherIncome>");
        stringBuilder_extraInfo.append("<TotalNoOfMama>" + editText_totalMama.getText().toString() + "</TotalNoOfMama>");
        stringBuilder_extraInfo.append("<AliveMama>" + editText_aliveMama.getText().toString() + "</AliveMama>");
        stringBuilder_extraInfo.append("<ExpireMama>" + editText_expiredMama.getText().toString() + "</ExpireMama>");
        stringBuilder_extraInfo.append("<OwnFourWheeler>" + String.valueOf(checkBox_ownFourWheeler.isChecked()) + "</OwnFourWheeler>");
        stringBuilder_extraInfo.append("<OwnTwoWheeler>" + String.valueOf(checkBox_ownTwoWheeler.isChecked()) + "</OwnTwoWheeler>");

                //stringBuilder_farm.append("<FarmLanguage>" + userModel.getLanguage() + "</FarmLanguage>");

        stringBuilder_extraInfo.append("</Functions>");



            stringBuilder_extraInfo.append("</Assign>");







    }

    private void createVehicleXML() {


        //Vehicle XML
        stringBuilder_vehicle.append("<?xml version=\"1.0\" ?>");
        stringBuilder_vehicle.append("<Assign>");

        Cursor cursor_vehicle = sqLiteVehicleDetails.getAllData();


        if (cursor_vehicle != null) {

            for (boolean hasItem = cursor_vehicle.moveToFirst(); hasItem; hasItem = cursor_vehicle.moveToNext()) {

                stringBuilder_farm.append("<Functions>");

                stringBuilder_farm.append("<VehicleDetailsId>" + cursor_vehicle.getString(cursor_vehicle.getColumnIndex(SQLiteVehicleDetails.VEHICLE_DETAILS_ID)) + "</VehicleDetailsId>");
                stringBuilder_farm.append("<VehicleTypeId>" + cursor_vehicle.getString(cursor_vehicle.getColumnIndex(SQLiteVehicleDetails.VEHICLE_TYPE_ID)) + "</VehicleTypeId>");
                stringBuilder_farm.append("<VehicleBrandId>" + cursor_vehicle.getString(cursor_vehicle.getColumnIndex(SQLiteVehicleDetails.BRAND_NAME_ID)) + "</VehicleBrandId>");
                stringBuilder_farm.append("<VehicleModelId>" + cursor_vehicle.getString(cursor_vehicle.getColumnIndex(SQLiteVehicleDetails.MODEL_NAME_ID)) + "</VehicleModelId>");
                stringBuilder_farm.append("<Year>" + cursor_vehicle.getString(cursor_vehicle.getColumnIndex(SQLiteVehicleDetails.YEAR)) + "</Year>");
                stringBuilder_farm.append("<Cost>" + cursor_vehicle.getString(cursor_vehicle.getColumnIndex(SQLiteVehicleDetails.COST)) + "</Cost>");


                stringBuilder_farm.append("</Functions>");

            }

            stringBuilder_farm.append("</Assign>");

        }

        cursor_vehicle.close();


    }

    private void createFarmXML() {

        //Farm XML
        stringBuilder_farm.append("<?xml version=\"1.0\" ?>");
        stringBuilder_farm.append("<Assign>");

        Cursor cursor_farm = sqLiteFarmDetails.getAllData();
        if (cursor_farm != null) {

            for (boolean hasItem = cursor_farm.moveToFirst(); hasItem; hasItem = cursor_farm.moveToNext()) {

                stringBuilder_farm.append("<Functions>");

                stringBuilder_farm.append("<FarmDetailsId>" + cursor_farm.getString(cursor_farm.getColumnIndex(SQLiteFarmDetails.FARM_DETAILS_ID)) + "</FarmDetailsId>");
                stringBuilder_farm.append("<TotalLand>" + cursor_farm.getString(cursor_farm.getColumnIndex(SQLiteFarmDetails.TOTAL_AREA)) + "</TotalLand>");
                stringBuilder_farm.append("<IrrigatedArea>" + cursor_farm.getString(cursor_farm.getColumnIndex(SQLiteFarmDetails.IRRIGATED_AREA)) + "</IrrigatedArea>");
                stringBuilder_farm.append("<PartiallyIrrigatedArea>" + cursor_farm.getString(cursor_farm.getColumnIndex(SQLiteFarmDetails.PARTIALLY_IRRIGATED_AREA)) + "</PartiallyIrrigatedArea>");
                stringBuilder_farm.append("<NonIrrigatedArea>" + cursor_farm.getString(cursor_farm.getColumnIndex(SQLiteFarmDetails.NON_IRRIGATED_AREA)) + "</NonIrrigatedArea>");
                stringBuilder_farm.append("<CropsId>" + cursor_farm.getString(cursor_farm.getColumnIndex(SQLiteFarmDetails.CROPS_ID)) + "</CropsId>");

                //stringBuilder_farm.append("<FarmLanguage>" + userModel.getLanguage() + "</FarmLanguage>");

                stringBuilder_farm.append("</Functions>");

            }

            stringBuilder_farm.append("</Assign>");

        }

        cursor_farm.close();
    }

    private void createMamaXML() {


        //Mama XML
        stringBuilder_mama.append("<?xml version=\"1.0\" ?>");
        stringBuilder_mama.append("<Assign>");

        Cursor cursor_mama = sqLiteMamaDetails.getAllData();

        if (cursor_mama != null) {

            for (boolean hasItem = cursor_mama.moveToFirst(); hasItem; hasItem = cursor_mama.moveToNext()) {

                stringBuilder_mama.append("<Functions>");

                stringBuilder_mama.append("<MamaIsAlive>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.IS_ALIVE)) + "</MamaIsAlive>");
                stringBuilder_mama.append("<MamaFullname>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.NAME)) + "</MamaFullname>");
                stringBuilder_mama.append("<MamaMobileNo>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.MOBILE_NO)) + "</MamaMobileNo>");
                stringBuilder_mama.append("<MamaAddress>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.ADDRESS)) + "</MamaAddress>");
                stringBuilder_mama.append("<MamaCountryId>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.COUNTRY_ID)) + "</MamaCountryId>");
                stringBuilder_mama.append("<MamaStateId>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.STATE_ID)) + "</MamaStateId>");
                stringBuilder_mama.append("<MamaCityId>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.CITY_ID)) + "</MamaCityId>");
                stringBuilder_mama.append("<MamaOccupationId>" + cursor_mama.getString(cursor_mama.getColumnIndex(SQLiteMamaDetails.OCCUPATION_ID)) + "</MamaOccupationId>");
                stringBuilder_mama.append("<MamaLanguageType>" + userModel.getLanguage() + "</MamaLanguageType>");

                stringBuilder_mama.append("</Functions>");

            }


            stringBuilder_mama.append("</Assign>");

        }

        cursor_mama.close();

    }

    private void createPropertyXML() {


        // Property XML

        stringBuilder_property.append("<?xml version=\"1.0\" ?>");
        stringBuilder_property.append("<Assign>");

        Cursor cursor_property = sqLitePropertyDetails.getAllData();

        if (cursor_property != null) {

            for (boolean hasItem = cursor_property.moveToFirst(); hasItem; hasItem = cursor_property.moveToNext()) {

                stringBuilder_property.append("<Functions>");


                stringBuilder_property.append("<PropertyDetailsId>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.PROPERTY_DETAILS_ID)) + "</PropertyDetailsId>");
                stringBuilder_property.append("<PropertyTypeId>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.PROPERTY_TYPE_ID)) + "</PropertyTypeId>");
                stringBuilder_property.append("<PropertyOwnershipType>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.OWNERSHIP_TYPE)) + "</PropertPropertyTypeIdyOwnershipType>");
                stringBuilder_property.append("<PropertyBHKType>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.BHK_TYPE_ID)) + "</PropertyBHKType>");
                stringBuilder_property.append("<PropertyArea>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.CARPET_AREA)) + "</PropertyArea>");
                stringBuilder_property.append("<PropertyAddress>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.ADDRESS)) + "</PropertyAddress>");
                stringBuilder_property.append("<PropertyCountryId>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.COUNTRY_ID)) + "</PropertyCountryId>");
                stringBuilder_property.append("<PropertyStateId>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.STATE_ID)) + "</PropertyStateId>");
                stringBuilder_property.append("<PropertyCityId>" + cursor_property.getString(cursor_property.getColumnIndex(SQLitePropertyDetails.CITY_ID)) + "</PropertyCityId>");
                stringBuilder_property.append("<PropertyLanguage>" + userModel.getLanguage() + "</PropertyLanguage>");

                stringBuilder_property.append("</Functions>");

            }

            stringBuilder_property.append("</Assign>");

        }

        cursor_property.close();


    }

    private void createSiblingXML() {

        // Sibling XML
        stringBuilder_sibling.append("<?xml version=\"1.0\" ?>");

        stringBuilder_sibling.append("<Assign>");

        Cursor cursor_sibling = sqLiteSiblingDetails.getAllData();


        if (cursor_sibling != null) {
            for (boolean hasItem = cursor_sibling.moveToFirst(); hasItem; hasItem = cursor_sibling.moveToNext()) {

                stringBuilder_sibling.append("<Functions>");

                //stringBuilder_sibling.append("<UserId>"+userModel.getUserId()+"</UserId>");
                stringBuilder_sibling.append("<SiblingDetailsId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.SIBLING_DETAILS_ID)) + "</SiblingDetailsId>");
                stringBuilder_sibling.append("<SiblingListId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.RELATION_ID)) + "</SiblingListId>");
                stringBuilder_sibling.append("<SiblingName>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.NAME)) + "</SiblingName>");
                stringBuilder_sibling.append("<SiblingSpouseName>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.NAME)) + "</SiblingSpouseName>");
                stringBuilder_sibling.append("<SiblingMobileNo>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.MOBILE_NO)) + "</SiblingMobileNo>");
                stringBuilder_sibling.append("<SiblingQualificationId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.EDUCATION_ID)) + "</SiblingQualificationId>");
                stringBuilder_sibling.append("<SiblingOccupationId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.OCCUPATION_ID)) + "</SiblingOccupationId>");
                stringBuilder_sibling.append("<SiblingMaritalStatus>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.MARITAL_STATUS)) + "</SiblingMaritalStatus>");
                stringBuilder_sibling.append("<InLawsFullName>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_NAME)) + "</InLawsFullName>");
                stringBuilder_sibling.append("<InLawsMobileNo>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_MOBILE_NO)) + "</InLawsMobileNo>");
                stringBuilder_sibling.append("<InLawsAddress>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_VILLAGE)) + "</InLawsAddress>");
                stringBuilder_sibling.append("<InLawsCountryId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_COUNTRY_ID)) + "</InLawsCountryId>");
                stringBuilder_sibling.append("<InLawsStateId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_STATE_ID)) + "</InLawsStateId>");
                stringBuilder_sibling.append("<InLawsCityId>" + cursor_sibling.getString(cursor_sibling.getColumnIndex(SQLiteSiblingDetails.FATHER_IN_LAW_CITY_ID)) + "</InLawsCityId>");
                stringBuilder_sibling.append("<SiblingLanguage>" + userModel.getLanguage() + "</SiblingLanguage>");


                stringBuilder_sibling.append("</Functions>");

            }

            stringBuilder_sibling.append("</Assign>");


        }
        cursor_sibling.close();


    }


    void getDetails() {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_FAMILYDETAILS + "UserId=" + userModel.getUserId() + "&Language=" + userModel.getLanguage(),
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
                                    familyDetailsId = jsonObject.getInt("FamilyDetailsId");
                                    fatherDetailsId = jsonObject.getInt("FatherStatesIDAPI");
                                    motherDetailsId = jsonObject.getInt("MotherDetailsIdAPI");


                                    checkBox_fatherIsAlive.setChecked(jsonObject.getString("IsAliveFather").equals("1"));

                                    editText_fatherName.setText(jsonObject.getString("FullnameFather"));
                                    editText_fatherMobileNo.setText(jsonObject.getString("MobileNoFather"));
                                    editText_fatherAddress.setText(jsonObject.getString("AddressFather"));

                                    //editText_fatherAnnualIncome.setText(jsonObject.getString("AnnualIncomeFather"));

                                    // textView_fatherStateId.setText(jsonObject.getString("FatherStatesIDAPI"));
                                    //textView_fatherDistrictId.setText(jsonObject.getString("FatherDistrictIdAPI"));
                                    //textView_fatherTalukaId.setText(jsonObject.getString("FatherTalukasIdAPI"));

                                    editText_fatherState.setText(jsonObject.getString("StatesNameFather"));
                                    //editText_fatherDistrict.setText(jsonObject.getString("DistrictNameFather"));
                                    //editText_fatherTaluka.setText(jsonObject.getString("TalukaNameFather"));

                                    textView_fatherQualificationId.setText(jsonObject.getString("FatherQualificationIdAPI"));
                                    textView_fatherOccupationId.setText(jsonObject.getString("FatherOccupationIdAPI"));

                                    editText_fatherQualification.setText(jsonObject.getString("QualificationFather"));
                                    editText_fatherOccupation.setText(jsonObject.getString("OccupationNameFather"));


                                    checkBox_motherIsAlive.setChecked(jsonObject.getString("IsAliveMother").equals("1"));


                                    editText_motherName.setText(jsonObject.getString("FullnameMother"));
                                    editText_motherMobileNo.setText(jsonObject.getString("MobileNoMother"));
                                    editText_motherQualification.setText(jsonObject.getString("QualificationMother"));
                                    editText_motherOccupation.setText(jsonObject.getString("OccupationNameMother"));

                                    textView_motherQualificationId.setText(jsonObject.getString("MotherQualificationIdAPI"));
                                    textView_motherOccupationId.setText(jsonObject.getString("MotherOccupationIdAPI"));

                                    //editText_motherAnnualIncome.setText(jsonObject.getString("AnnualIncomeMother"));
                                    //   editText_familyIncome.setText(jsonObject.getString("SalaryPackageName"));
                                    textView_familyIncome.setText(jsonObject.getString("SalaryPackageId"));

                                    editText_relative1.setText(jsonObject.getString("Surname1"));
                                    editText_relative2.setText(jsonObject.getString("Surname2"));
                                    editText_relative3.setText(jsonObject.getString("Surname3"));
                                    editText_relative4.setText(jsonObject.getString("Surname4"));


                                    textView_fatherCountryId.setText(jsonObject.getString("FatherCountryId"));
                                    textView_fatherStateId.setText(jsonObject.getString("FatherStateId"));
                                    textView_fatherCityId.setText(jsonObject.getString("FatherCityId"));

                                    editText_fatherCountry.setText(jsonObject.getString("FatherCountryName"));
                                    editText_fatherState.setText(jsonObject.getString("FatherStateName"));
                                    editText_fatherCity.setText(jsonObject.getString("FatherCityName"));


                                    getMamaDetailsXML(jsonObject);
                                    getSiblingDetailsXML(jsonObject);
                                    //getFarmDetailsXML(jsonObject);
                                    getPropertyDetailsXML(jsonObject);



                                }

                            } else {
                                familyDetailsId = 0;
                                fatherDetailsId = 0;
                                motherDetailsId = 0;
                                customDialogLoadingProgressBar.dismiss();
                                Toast.makeText(getContext(), " Please enter your details! ", Toast.LENGTH_SHORT).show();
                            }


                        }catch(
                                JSONException e)

                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("Error", error.toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }


    private void getPropertyDetailsXML(JSONObject jsonObject) {

        try {
            sqLitePropertyDetails.deleteAll();
            addPersonModelArrayList_property.clear();
            addPersonAdapter_property.notifyDataSetChanged();
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


                addPersonModelArrayList_property.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("HousePropertyDetailsId"),
                        jsonObject_details.getString("PropertyArea")+" sq. ft. "+jsonObject_details.getString("PropertyName"),
                        "in "+jsonObject_details.getString("PropertyAddress")));

            }

            addPersonAdapter_property.notifyDataSetChanged();


        }

        catch (JSONException jsonException)
        {

        }
    }

    private void getFarmDetailsXML(JSONObject jsonObject) {

        try {
            sqLiteFarmDetails.deleteAll();
            addPersonModelArrayList_farm.clear();
            addPersonAdapter_farm.notifyDataSetChanged();
            JSONArray jsonArray_farmDetails = jsonObject.getJSONArray("FarmDetailsLST");

            for(int j=0; j< jsonArray_farmDetails.length(); j++) {
                JSONObject jsonObject_details = jsonArray_farmDetails.getJSONObject(j);


                long id = sqLiteFarmDetails.insertFarmDetails(
                        jsonObject_details.getString("FarmPropertyDetailsId"),
                        jsonObject_details.getString("LandArea"),
                        jsonObject_details.getString("FullOrPart"),
                        jsonObject_details.getString("CropTaken"),"Irrigated","","");


                addPersonModelArrayList_farm.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("FarmPropertyDetailsId"),
                        jsonObject_details.getString("LandArea"),
                        jsonObject_details.getString("CropTaken")));



            }
            addPersonAdapter_farm.notifyDataSetChanged();
            //farmRecyclerView();

        }
        catch (JSONException jsonException)
        {}




    }

    private void getSiblingDetailsXML(JSONObject jsonObject) {


        try {
            sqLiteSiblingDetails.deleteAll();
            addPersonModelArrayList_sibling.clear();
            addPersonAdapter_sibling.notifyDataSetChanged();

            JSONArray jsonArray_siblingDetails = jsonObject.getJSONArray("SiblingsDetailsLST");

            for (int j = 0; j < jsonArray_siblingDetails.length(); j++) {
                JSONObject jsonObject_details = jsonArray_siblingDetails.getJSONObject(j);


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
                        jsonObject_details.getString("SiblingSpouseName"),
                        jsonObject_details.getString("SiblingSpouseName"), //change-> should be replaced with spouse Name
                        jsonObject_details.getString("InLawsFullNameAPI"),
                        jsonObject_details.getString("InLawsMobileNoAPI"),
                        jsonObject_details.getString("InLawsAddressAPI"),
                        jsonObject_details.getString("SiblingListIdAPI"),
                        jsonObject_details.getString("SiblingListIdAPI"),
                        jsonObject_details.getString("SiblingListIdAPI"),
                        jsonObject_details.getString("SiblingListIdAPI"),
                        jsonObject_details.getString("SiblingListIdAPI"),
                        jsonObject_details.getString("SiblingListIdAPI"));


                addPersonModelArrayList_sibling.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("SiblingsDetailsId"),
                        jsonObject_details.getString("SiblingsFullname"),
                        jsonObject_details.getString("QualificationSiblings")));


            }

            addPersonAdapter_sibling.notifyDataSetChanged();


        } catch (JSONException jsonException) {


        }
    }

    private void getMamaDetailsXML(JSONObject jsonObject){


        try {
            sqLiteMamaDetails.deleteAll();
            addPersonModelArrayList_mama.clear();
            addPersonAdapter_mama.notifyDataSetChanged();
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

                addPersonModelArrayList_mama.add(new AddPersonModel(String.valueOf(id),
                        jsonObject_details.getString("MamaDetailsId"),
                        jsonObject_details.getString("MamaFullnameAPI"),
                        jsonObject_details.getString("MamaAddressAPI")));


            }

            addPersonAdapter_mama.notifyDataSetChanged();

        }
        catch (JSONException jsonException)
        {

        }



    }






     class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;



        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {


                if(params[0].equals("getDetails"))
                {
                    getDetails();

                }
                else if(params[0].equals("insertDetails"))
                {
                    insertDetails();
                    //customDialogLoadingProgressBar.dismiss(); //after uncommenting above line remove this line
                }



                if(params[0].equals("FatherCountry"))
                {

                    dataFetcher.loadList(URLs.URL_GET_COUNTRY+"Language="+userModel.getLanguage(),"Id",
                            "Name", editText_fatherCountry, textView_fatherCountryId,getContext(),
                            customDialogLoadingProgressBar);


                    return "Country";

                }
                else if(params[0].equals("FatherState"))
                {
                    String id = textView_fatherCountryId.getText().toString();

                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage()
                                    + "&CountryID="+id,"StatesID", "StatesName",
                            editText_fatherState, textView_fatherStateId,getContext(),
                            customDialogLoadingProgressBar);



                }
                else if(params[0].equals("FatherCity"))
                {
                    String id = textView_fatherStateId.getText().toString();

                    dataFetcher.loadList(URLs.URL_GET_CITY+"Language="+userModel.getLanguage()
                                    + "&StateID="+id,"ID",
                            "Name", editText_fatherCity, textView_fatherCityId,getContext(),
                            customDialogLoadingProgressBar);


                }
/*                if(params[0].equals("FatherState"))
                {
                    dataFetcher.loadList(URLs.URL_GET_STATE+"Language="+userModel.getLanguage(),"StatesID",
                            "StatesName", editText_fatherState, textView_fatherStateId,getContext(), customDialogLoadingProgressBar);


                }
                else if(params[0].equals("FatherDistrict"))
                {
                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_DISTRICT+"StatesID="+id+"&Language="+userModel.getLanguage(),
                            "DistrictId", "DistrictName", editText_fatherDistrict, textView_fatherDistrictId,
                            getContext(), customDialogLoadingProgressBar);

                }
                else if(params[0].equals("FatherTaluka"))
                {

                    String id = params[1];
                    dataFetcher.loadList(URLs.URL_GET_TALUKA+"DistrictId="+id+"&Language="+userModel.getLanguage(),
                            "TalukasId", "TalukaName", editText_fatherTaluka, textView_fatherTalukaId,
                            getContext(), customDialogLoadingProgressBar);
                }*/

                if(params[0].equals("FatherQualification"))
                {
                    dataFetcher.loadList(URLs.URL_GET_ALL_QUALIFICATIONNAME+"Language="+userModel.getLanguage(),
                            "QualificationId", "Qualification", editText_fatherQualification,
                            textView_fatherQualificationId,getContext(), customDialogLoadingProgressBar);


                }

                if(params[0].equals("MotherQualification"))
                {
                    dataFetcher.loadList(URLs.URL_GET_ALL_QUALIFICATIONNAME+"Language="+userModel.getLanguage(),
                            "QualificationId", "Qualification", editText_motherQualification,
                            textView_motherQualificationId,getContext(), customDialogLoadingProgressBar);


                }

                if(params[0].equals("FatherOccupation"))
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_fatherOccupation,
                            textView_fatherOccupationId,getContext(), customDialogLoadingProgressBar);


                }

                else if(params[0].equals("MotherOccupation"))
                {
                    dataFetcher.loadList(URLs.URL_GET_OCCUPATION+"Language="+userModel.getLanguage(),
                            "OccupationId", "OccupationName", editText_motherOccupation,
                            textView_motherOccupationId,getContext(), customDialogLoadingProgressBar);


                }

                else if(params[0].equals("FamilyIncome"))
                {
                    dataFetcher.loadList(URLs.URL_GET_SALARY+"Language="+userModel.getLanguage(),
                            "SalaryPackageId", "SalaryPackageName", editText_familyIncome,
                            textView_familyIncome, getContext(), customDialogLoadingProgressBar);


                }

                return params[0];

                        } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

            if(result.equals("Country"))
            {
                newCountryId = Integer.parseInt(textView_fatherCountryId.getText().toString());
                if (currentCountryId != newCountryId) {
                    currentCountryId = newCountryId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_fatherStateId.setText("0");
                            editText_fatherState.setText("");
                            textView_fatherCityId.setText("0");
                            editText_fatherCity.setText("");
                        }
                    });

                }
            }
            else if(result.equals("State"))
            {
                newStateId = Integer.parseInt(textView_fatherStateId.getText().toString());
                if (currentStateId != newStateId) {
                    currentStateId = newStateId;
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textView_fatherCityId.setText("0");
                            editText_fatherCity.setText("");
                        }
                    });

                }
            }

        }


        @Override
        protected void onPreExecute() {

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }



}
