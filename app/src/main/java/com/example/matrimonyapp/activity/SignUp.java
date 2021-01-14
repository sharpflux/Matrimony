package com.example.matrimonyapp.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialogAccountExists;
import com.example.matrimonyapp.customViews.CustomDialogChangeProfilePic;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    private EditText editText_birthdate, editText_age, editText_fullName,
            editText_mobileNo,  editText_emailId, editText_password, editText_confirmPassword;

    RadioGroup radioGroup_gender;

    CustomDialogAccountExists customDialogAccountExists;

    TextView textView_signUp, textView_backToSignIn, textView_showPassword, textView_showConfirmPassword;

    private DatePickerDialog.OnDateSetListener mDateSetListener;

    String fullName, gender, birthdate, mobileNo,
            emailId, age, password;

    AlertDialog.Builder builder;
    private String currentLanguage;
    private CustomDialogChangeProfilePic customDialogChangeProfilePic;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    Intent intent_camera;
    public static final int REQUEST_CAMERA = 1, SELECT_FILE = 0, ACTIVITY_CONSTANT = 2;
    public String selfieString="0";
    RelativeLayout relativeLayout_changeProfilePic;
    CircleImageView circleImageView_profilePic;
    public static final int RequestPermissionCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        editText_fullName = findViewById(R.id.editText_fullName);

        radioGroup_gender = (RadioGroup) findViewById(R.id.radioGroup_gender);

        editText_birthdate=findViewById(R.id.editText_birthdate);
        editText_age=findViewById(R.id.editText_age);

        editText_mobileNo = findViewById(R.id.editText_mobileNo);
        editText_emailId = findViewById(R.id.editText_emailId);
        editText_password = findViewById(R.id.editText_password);
        editText_confirmPassword = findViewById(R.id.editText_confirmPassword);
        textView_showPassword = findViewById(R.id.textView_showPassword);
        textView_showConfirmPassword = findViewById(R.id.textView_showConfirmPassword);
        textView_signUp = findViewById(R.id.textView_signUp);
        textView_backToSignIn = findViewById(R.id.textView_backToSignIn);
        FieldValidation.validateRadioGroup(radioGroup_gender);
        customDialogChangeProfilePic = new CustomDialogChangeProfilePic(SignUp.this);
        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(SignUp.this);
        relativeLayout_changeProfilePic = findViewById(R.id.relativeLayout_changeProfilePic);
        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);



        showPassword(textView_showPassword, editText_password);
        showPassword(textView_showConfirmPassword, editText_confirmPassword);


        relativeLayout_changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeProfilepicDialog();
            }
        });

        textView_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText_password.getText().toString().equals(editText_confirmPassword.getText().toString()))
                {
                    if(checkProfilePic() && checkRequiredFields(editText_fullName, editText_mobileNo, editText_birthdate, editText_password, editText_confirmPassword)) {
                        // verifyMobileNo();
                        AsyncTaskRunner runner = new AsyncTaskRunner();
                        runner.execute("verifyMobileNo");
                    }

                }
                else
                {
                    Toast.makeText(SignUp.this, getResources().getString(R.string.password_confirm_password_doesnt_match), Toast.LENGTH_SHORT).show();
                }



            }
        });

        textView_backToSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*finish();*/
                Intent intent = new Intent(SignUp.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        radioGroup_gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                int id = radioGroup.getCheckedRadioButtonId();

                RadioButton rb = findViewById(id);

                gender= rb.getText().toString();
                //Toast.makeText(getContext(),gender,Toast.LENGTH_LONG).show();
            }
        });

        editText_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();

                final DatePickerDialog dialog = new DatePickerDialog(SignUp.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0, int year, int month, int day_of_month) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, (month));
                        calendar.set(Calendar.DAY_OF_MONTH, day_of_month);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        editText_birthdate.setText(sdf.format(calendar.getTime()));
                        String age = getAge(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        editText_age.setText(age);
                        //dialog.cancel();

                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                // dialog.getDatePicker().setMinDate(calendar.getTimeInMillis());// TODO: used to hide previous date,month and year
                calendar.add(Calendar.YEAR, -20);

                dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());// TODO: used to hide future date,month and year
                dialog.show();


            }
        });

        EnableRuntimePermission();

    }

    private void showPassword(final TextView textView, final EditText editText) {

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textView.getText().equals(getResources().getString(R.string._show)))
                {
                    editText.setTransformationMethod(null);
                    textView.setText(getResources().getString(R.string._hide));
                }
                else
                {
                    editText.setTransformationMethod(new PasswordTransformationMethod());
                    textView.setText(getResources().getString(R.string._show));
                }
            }
        });


    }


    private void changeProfilepicDialog() {
        customDialogChangeProfilePic.show();

        customDialogChangeProfilePic.textView_addFromCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialogChangeProfilePic.dismiss();
                intent_camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent_camera, REQUEST_CAMERA);
            }
        });


        customDialogChangeProfilePic.textView_addFromGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customDialogChangeProfilePic.dismiss();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, requestCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode)
            {
                case SELECT_FILE:
                    onSelectFromGalleryResult(data);
                    break;
                case REQUEST_CAMERA:
                    onCaptureImageResult(data);
                    break;
            }


        }

    }

    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SignUp.this,
                Manifest.permission.CAMERA)) {

            Toast.makeText(SignUp.this, "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(SignUp.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);

        }
    }

    private void onSelectFromGalleryResult(Intent data)
    {
        Bitmap bm = null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Bitmap newBitmap = Bitmap.createBitmap(bm.getWidth(), bm.getHeight(), bm.getConfig());
            Canvas canvas = new Canvas(newBitmap);
            canvas.drawColor(Color.WHITE);
            canvas.drawBitmap(bm, 0, 0, null);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);

            selfieString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);

        }
        circleImageView_profilePic.setImageBitmap(bm);
/*
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertProfilePic");*/

    }


    private void onCaptureImageResult(Intent data) {


        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();


        Bitmap newBitmap = Bitmap.createBitmap(thumbnail.getWidth(), thumbnail.getHeight(), thumbnail.getConfig());
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(thumbnail, 0, 0, null);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        selfieString = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);


        /*thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        ImageUrl= Base64.encodeToString(bytes.toByteArray(), Base64.DEFAULT);*/
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;

        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // img_banner_profile_placeholder.setImageBitmap(thumbnail);

        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
        circleImageView_profilePic.setImageBitmap(imageBitmap);
/*
        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("InsertProfilePic");*/


    }



    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


    private boolean checkRequiredFields(EditText ...editText) {
        boolean flag = true;
        for (EditText temp_editText: editText
             ) {

            if(temp_editText.getText().toString().equals(""))
            {
                temp_editText.setError("This is required field", getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
                flag = false;

            }

        }
        return flag;

    }

    boolean checkProfilePic()
    {
        boolean flag = true;
        if(selfieString.equals("0"))
        {
            Toast.makeText(SignUp.this, "Please select Profile Picture", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        return flag;
    }

    void verifyMobileNo()
    {
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(false);

        mobileNo = editText_mobileNo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URLs.URL_POST_VERIFYMOBILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (obj.getBoolean("error")) {

                                getOtp();

                            }
                            else{

                                customDialogAccountExists = new CustomDialogAccountExists(SignUp.this);

                                customDialogAccountExists.show();

                                customDialogAccountExists.textView_createNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        editText_fullName.requestFocus();
                                        editText_fullName.setText("");
                                        editText_mobileNo.setText("");
                                        editText_emailId.setText("");
                                        editText_birthdate.setText("");
                                        editText_age.setText("");
                                        editText_password.setText("");
                                        radioGroup_gender.clearCheck();
                                        customDialogAccountExists.dismiss();

                                    }
                                });



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(),"Error in    SignUP verifyMobile"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OTPMobileNo", mobileNo);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //finish();
    }



    void getOtp()
    {
        builder = new AlertDialog.Builder(getApplicationContext());
        builder.setCancelable(false);

        mobileNo = editText_mobileNo.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_OTP+"MobileNo="+mobileNo,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);


                            if (!obj.getBoolean("error") && obj.getString("message").equals("Success")) {


                                fullName = editText_fullName.getText().toString().trim();
                                mobileNo = editText_mobileNo.getText().toString().trim();
                                emailId = editText_emailId.getText().toString().trim();
                                birthdate = editText_birthdate.getText().toString().trim();
                                age = editText_age.getText().toString().trim();
                                password = editText_password.getText().toString().trim();
                                gender = ((RadioButton)findViewById(radioGroup_gender.getCheckedRadioButtonId()))
                                        .getText().toString();


                                Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
                                intent.putExtra("OTP",obj.getString("OTP"));
                                intent.putExtra("parentActivity","SignUp");
                                intent.putExtra("fullName",fullName);
                                intent.putExtra("mobileNo",mobileNo);
                                intent.putExtra("emailId",emailId);
                                intent.putExtra("birthdate",birthdate);
                                intent.putExtra("age",age);
                                intent.putExtra("gender",gender);
                                intent.putExtra("password",password);
                                startActivity(intent);


                            }
                            else{
                                /*builder.setMessage("Please try again...")
                                        .setCancelable(false)

                                        .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                //  Action for 'NO' Button
                                                dialog.cancel();

                                            }
                                        });

                                AlertDialog alert = builder.create();
                                alert.setTitle("Sorry, Try again");
                                alert.show();*/
                                /*customDialogAccountExists = new CustomDialogAccountExists(SignUp.this);

                                customDialogAccountExists.show();

                                customDialogAccountExists.textView_createNew.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        editText_fullName.requestFocus();
                                        editText_fullName.setText("");
                                        editText_mobileNo.setText("");
                                        editText_emailId.setText("");
                                        editText_birthdate.setText("");
                                        editText_age.setText("");
                                        editText_password.setText("");
                                        radioGroup_gender.clearCheck();
                                        customDialogAccountExists.dismiss();

                                    }
                                });*/



                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* builder.setMessage("Something went wrong please try again !")
                                .setCancelable(false)

                                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });

                        AlertDialog alert = builder.create();
                        alert.setTitle("Error");
                        alert.show();*/

                        //  ---  progressDialog.dismiss();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //params.put("OTPMobileNo", mobileNo);
                return params;
            }
        };

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);



    }




    String getAge(int year, int month, int day)
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

    class AsyncTaskRunner extends AsyncTask<String , String, String>
    {
        public AsyncTaskRunner() {
            super();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            customDialogLoadingProgressBar.show();

        }

        @Override
        protected String doInBackground(String... params) {

            if(params[0].equals("verifyMobileNo"))
            {
                verifyMobileNo();

            }
            else if(params[0].equals("getOTP"))
            {
                getOtp();
            }

            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            customDialogLoadingProgressBar.dismiss();
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


}
