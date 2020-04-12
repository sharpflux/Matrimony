package com.example.matrimonyapp.validation;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.matrimonyapp.fragment.FamilyDetailsFragment;


public class StateDistrictTalukaValidation {

    public  Context context;
    public  EditText editText_state, editText_district, editText_taluka;
    public  TextView textView_stateId, textView_districtId, textView_talukaID;

    public StateDistrictTalukaValidation() {
    }

    public StateDistrictTalukaValidation(Context context, EditText editText_state, EditText editText_district,
                                         EditText editText_taluka, TextView textView_stateId, TextView textView_districtId,
                                         TextView textView_talukaID) {
        this.context = context;
        this.editText_state = editText_state;
        this.editText_district = editText_district;
        this.editText_taluka = editText_taluka;
        this.textView_stateId = textView_stateId;
        this.textView_districtId = textView_districtId;
        this.textView_talukaID = textView_talukaID;



    }
    public void showPopUp(EditText editText, final String urlFor, final AsyncTask runner, final Fragment fragment)
    {
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                //FamilyDetailsFragment.AsyncTaskLoad runner = new FamilyDetailsFragment.AsyncTaskLoad();

                String id="";

                if(urlFor.equals("District"))
                {
                    id =  textView_stateId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select State first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                if(urlFor.equals("Taluka"))
                {
                    id = textView_districtId.getText().toString();
                    if(id.equals("0"))
                    {
                        Toast.makeText(context, "Please select District first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                runner.execute((String)urlFor, (String)id);
            }
        });




    }


    public void textChangedListener()
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
                textView_talukaID.setText("0");
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
                textView_talukaID.setText("0");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


    }



}
