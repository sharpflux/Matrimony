package com.example.matrimonyapp.validation;

import android.text.TextUtils;
import android.widget.EditText;

public class EditTextValidation {


    void compulsory(EditText editText, String string_error)
    {

        if(TextUtils.isEmpty(editText.getText().toString()))
        {
            editText.setError(string_error);
            editText.requestFocus();
            return;
        }



    }



}
