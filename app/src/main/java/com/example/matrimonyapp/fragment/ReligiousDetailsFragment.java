package com.example.matrimonyapp.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.matrimonyapp.activity.HomeActivity;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReligiousDetailsFragment extends Fragment {

    View view;
    private TextView textView_saveAndContinue, textView_religionId, textView_casteId, textView_subCasteId,
    textView_motherTongueId;

    private EditText editText_caste, editText_subCaste, editText_religion, editText_gothram, editText_dosh,
    editText_motherTongue;
    public Context context;
    private ImageView imageView_back;
    private boolean isLoggedIn;

    String caste, subCaste, religion, otherCaste, gothram, dosh;
    CheckBox checkBox_otherCaste;
    Bundle bundle;

    DataFetcher dataFetcher;
    UserModel userModel;
    int religionDetailsId =0 ;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

    public ReligiousDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_religious_details, container, false);

        context = getContext();



        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        imageView_back =((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv =((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText(context.getResources().getString(R.string.religious_details));

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentManager fragmentManager = getFragmentManager();

                //fragmentManager.popBackStackImmediate();
                if(fragmentManager.getBackStackEntryCount()>0)
                {
                    fragmentManager.popBackStackImmediate();
                }
                else
                {
                    getActivity().finish();
                }
            }
        });


        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        editText_caste = view.findViewById(R.id.editText_caste);
        editText_subCaste = view.findViewById(R.id.editText_subCaste);
        editText_religion = view.findViewById(R.id.editText_religion);
        editText_gothram = view.findViewById(R.id.editText_gothram);
        editText_dosh = view.findViewById(R.id.editText_dosh);
        editText_motherTongue = view.findViewById(R.id.editText_motherTongue);
        textView_religionId = view.findViewById(R.id.textView_religionId);
        textView_casteId = view.findViewById(R.id.textView_casteId);
        textView_subCasteId = view.findViewById(R.id.textView_subCasteId);
        textView_motherTongueId = view.findViewById(R.id.textView_motherTongueId);
        checkBox_otherCaste = view.findViewById(R.id.checkBox_otherCaste);

        bundle=getArguments();
        isLoggedIn = CustomSharedPreference.getInstance((MainActivity)getActivity()).isLoggedIn();
        if(isLoggedIn)
        {
            AsyncTaskLoad getTask = new AsyncTaskLoad();
            getTask.execute("getDetails");
        }


        //Toast.makeText(getContext()," -- "+bundle.getString("fullName"))


        dataFetcher = new DataFetcher("Religion",getContext());

        editText_religion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                /*editText_district.setText("");
                textView_districtId.setText("0");
                editText_taluka.setText("");
                textView_talukaId.setText("0");*/
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        onClickListener();

        return view;
    }

    void onClickListener()
    {
        showPopUp(editText_religion,"Religion",null);
        showPopUp(editText_caste,"Caste",textView_religionId);
        showPopUp(editText_subCaste,"SubCaste",textView_casteId);
        showPopUp(editText_motherTongue,"MotherTongue",null);

        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AsyncTaskLoad insertTask = new AsyncTaskLoad();
                insertTask.execute("insertDetails");


            }
        });


    }

    void replaceFragment(Fragment destFragment)
    {
//        Intent intent = new Intent(getActivity(), HomeActivity.class);
//        getActivity().startActivity(intent);
//        getActivity().finish();

        if(userModel.getUserType().equals("OldUser")){
            getActivity().onBackPressed();
        }
        else {
            //FamilyDetailsFragment familyDetailsFragment = new FamilyDetailsFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
            fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
            fragmentTransaction.commit();
        }
    }

    void showPopUp(EditText editText, final String urlFor, final TextView textView_id)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AsyncTaskLoad runner = new AsyncTaskLoad();
                if(textView_id==null)
                {
                    runner.execute(urlFor);
                }
                else
                {
                    runner.execute(urlFor,textView_id.getText().toString());
                }
            }
        });

    }

    void insertDetails()
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST,  URLs.URL_POST_RELIGIONDETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject.getString("message").equals("Success") && !jsonObject.getBoolean("error"))
                            {
                                getDetails();
                                Toast.makeText(getContext(),"Religious details saved successfully!", Toast.LENGTH_SHORT).show();
                                //getActivity().finish();

                                replaceFragment(new FamilyDetailsFragment());
                                /*PersonalDetailsFragment personalDetailsFragment = new PersonalDetailsFragment();

                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.addToBackStack(null);

                                fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, personalDetailsFragment);
                                fragmentTransaction.commit() ;*/
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
                params.put("ReligiousDetailsId",String.valueOf(religionDetailsId));
                params.put("UserId",userModel.getUserId());
                params.put("ReligionId",textView_religionId.getText().toString());
                params.put("CasteId",textView_casteId.getText().toString());
                params.put("SubCasteId",textView_subCasteId.getText().toString());
                params.put("OtherCommunity",String.valueOf(checkBox_otherCaste.isChecked()));
                params.put("Gothram",editText_gothram.getText().toString());
                params.put("Dosh","0");
                params.put("MotherTongueId",textView_motherTongueId.getText().toString());
                params.put("LanguageType",userModel.getLanguage());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    void getDetails()
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,URLs.URL_GET_RELIGIONDETAIL+"UserId="+userModel.getUserId()+"&Language="+userModel.getLanguage(),
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
                                    religionDetailsId = jsonObject.getInt("ReligiousDetailsId");

                                    textView_religionId.setText(jsonObject.getString("ReligionId"));
                                    textView_casteId.setText(jsonObject.getString("CasteId"));
                                    textView_subCasteId.setText(jsonObject.getString("SubCasteId"));
                                    textView_motherTongueId.setText(jsonObject.getString("MotherTongueId"));
                                    editText_religion.setText(jsonObject.getString("ReligionName"));
                                    editText_caste.setText(jsonObject.getString("CasteName"));
                                    editText_subCaste.setText(jsonObject.getString("SubCasteName"));
                                    checkBox_otherCaste.setChecked(jsonObject.getBoolean("OtherCommunity"));
                                    editText_gothram.setText(jsonObject.getString("Gothram"));
                                    editText_dosh.setText(jsonObject.getString("Dosh"));
                                    editText_motherTongue.setText(jsonObject.getString("MotherTongueName"));
                                }

                            }
                            else
                            {
                                religionDetailsId = 0;
                                //Toast.makeText(getContext(),"Invalid Details GET! ",Toast.LENGTH_SHORT).show();
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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;
        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                if(params[0].toString()=="getDetails")
                {
                    getDetails();
                }
                else if(params[0].toString()=="insertDetails")
                {
                    insertDetails();
                }

                if(params[0].toString()=="Religion")
                {
                    dataFetcher.loadList(URLs.URL_GET_RELIGION+"Language="+userModel.getLanguage(),
                            "ReligionID", "ReligionName", editText_religion,
                            textView_religionId,getContext(), customDialogLoadingProgressBar);
                }
                else if(params[0].toString()=="Caste")
                {
                    String religionId = params[1];
                    dataFetcher.loadList(URLs.URL_GET_CASTE+"ReligionId="+religionId+"&Language="+userModel.getLanguage(),
                            "CasteId","CasteName", editText_caste, textView_casteId,
                            getContext(), customDialogLoadingProgressBar);
                }
                else if(params[0].toString()=="SubCaste")
                {
                    String casteId = params[1];
                    dataFetcher.loadList(URLs.URL_GET_SUBCASTE+"CasteId="+casteId+"&Language="+userModel.getLanguage(),
                            "SubCasteId","SubCasteName", editText_subCaste, textView_subCasteId,
                            getContext(), customDialogLoadingProgressBar);
                }
                else if(params[0].toString()=="MotherTongue")
                {
                    dataFetcher.loadList(URLs.URL_GET_MOTHERTONGUE+"Language="+userModel.getLanguage(),
                            "MotherTongueId","MotherTongueName", editText_motherTongue, textView_motherTongueId,
                            getContext(), customDialogLoadingProgressBar);
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {
            customDialogLoadingProgressBar.dismiss();
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
