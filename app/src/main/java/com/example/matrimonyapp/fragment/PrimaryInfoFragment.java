package com.example.matrimonyapp.fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.activity.SignUp;
import com.example.matrimonyapp.activity.VerifyOtpActivity;
import com.example.matrimonyapp.customViews.CustomDialogAccountExists;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.AppSignatureHashHelper;
import com.example.matrimonyapp.utils.SMSReceiver;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PrimaryInfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PrimaryInfoFragment extends Fragment  implements
        SMSReceiver.OTPReceiveListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    CustomDialogAccountExists customDialogAccountExists;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editText_fullName, editText_mobileNo, editText_emailId,editText_birthdate,editText_age,editText_password,editText_confirmPassword;
    TextView txt_saveAndContinue;
    RadioGroup radioGroup_gender;

    ImageView password_show, password_hide,password_show1,password_hide1;
    FrameLayout frame_eye,frame_eye1;
    private int passwordNotVisible = 1;
    private int ConfirmpasswordNotVisible = 1;
    String TAG = "PhoneActivityTAG";
    String fullName, gender, birthdate, mobileNo,
            emailId, age, password;
    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;

    Context context;

    private UserModel userModel;

    View view;
    private ImageView imageView_back;
    public PrimaryInfoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PrimaryInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PrimaryInfoFragment newInstance(String param1, String param2) {
        PrimaryInfoFragment fragment = new PrimaryInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_primary_info, container, false);

        editText_fullName = view.findViewById(R.id.editText_fullName);
        editText_mobileNo = view.findViewById(R.id.editText_mobileNo);
        editText_emailId = view.findViewById(R.id.editText_emailId);
        editText_birthdate = view.findViewById(R.id.editText_birthdate);
        editText_age= view.findViewById(R.id.editText_age);
        radioGroup_gender= view.findViewById(R.id.radioGroup_gender);
        txt_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        editText_password = view.findViewById(R.id.editText_password);
        editText_confirmPassword= view.findViewById(R.id.editText_confirmPassword);
        frame_eye = (FrameLayout) view.findViewById(R.id.frame_eye);
        password_show = (ImageView) view.findViewById(R.id.password_show);
        password_hide = (ImageView) view.findViewById(R.id.password_hide);

        context = getContext();
        frame_eye1 = (FrameLayout) view.findViewById(R.id.frame_eye1);
        password_show1 = (ImageView) view.findViewById(R.id.password_show1);
        password_hide1 = (ImageView) view.findViewById(R.id.password_hide1);

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);

        frame_eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passwordNotVisible == 1) {
                    password_show.setVisibility(View.VISIBLE);
                    password_hide.setVisibility(View.GONE);
                    editText_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    passwordNotVisible = 0;
                } else {
                    password_show.setVisibility(View.GONE);
                    password_hide.setVisibility(View.VISIBLE);
                    editText_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    passwordNotVisible = 1;
                }
                editText_password.setSelection(editText_password.length());
            }
        });


        frame_eye1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConfirmpasswordNotVisible == 1) {
                    password_show1.setVisibility(View.VISIBLE);
                    password_hide1.setVisibility(View.GONE);
                    editText_confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ConfirmpasswordNotVisible = 0;
                } else {
                    password_show1.setVisibility(View.GONE);
                    password_hide1.setVisibility(View.VISIBLE);
                    editText_confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ConfirmpasswordNotVisible = 1;
                }
                editText_confirmPassword.setSelection(editText_confirmPassword.length());
            }
        });



        editText_fullName.addTextChangedListener(change);
        editText_emailId.addTextChangedListener(change);
        editText_birthdate.addTextChangedListener(change);
        editText_age.addTextChangedListener(change);
        editText_password.addTextChangedListener(change);
        editText_confirmPassword.addTextChangedListener(change);

        ShowCalendar();


        imageView_back=((MainActivity)getActivity()).findViewById(R.id.imageView_back);
        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText(getContext().getResources().getString(R.string.primary_details));



        txt_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(editText_password.getText().toString().equals(editText_confirmPassword.getText().toString()))
                {
                    if(checkRequiredFields(editText_fullName, editText_mobileNo, editText_birthdate, editText_password, editText_confirmPassword)) {
                        // verifyMobileNo();
                        AsyncTaskLoad runner = new AsyncTaskLoad();
                        runner.execute("verifyMobileNo");
                    }

                }
                else
                {
                    Toast.makeText(context, getResources().getString(R.string.password_confirm_password_doesnt_match), Toast.LENGTH_SHORT).show();
                }

            }
        });


        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(getContext());

        // This code requires one time to get Hash keys do comment and share key
        Log.i("TAG", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

        UserModel user = CustomSharedPreference.getInstance(getContext()).getUser();

        if (user != null) {
            editText_fullName.setText(user.getFullName());
            editText_emailId.setText(user.getEmailId());
        }

        //startSMSListener();
        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            Log.d(TAG, "Phone number: " + getPhone());
        }
        return  view;
    }

    private boolean checkRequiredFields(EditText ...editText) {
        boolean flag = true;
        for (EditText temp_editText: editText) {

            if(temp_editText.getText().toString().equals(""))
            {
                temp_editText.setError("This is required field", getResources().getDrawable(R.drawable.ic_error_outline_black_24dp));
                flag = false;

            }

        }
        return flag;

    }

    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);



        fullName = editText_fullName.getText().toString().trim();
        mobileNo = editText_mobileNo.getText().toString().trim();
        emailId = editText_emailId.getText().toString().trim();
        birthdate = editText_birthdate.getText().toString().trim();
        age = editText_age.getText().toString().trim();
        password = editText_password.getText().toString().trim();
        gender = ((RadioButton)getActivity().findViewById(radioGroup_gender.getCheckedRadioButtonId()))
                .getText().toString();

        Bundle bundle = new Bundle();
//        intent.putExtra("OTP",obj.getString("OTP"));
        bundle.putString("fullName",fullName);
        bundle.putString("mobileNo",mobileNo);
        bundle.putString("emailId",emailId);
        bundle.putString("birthdate",birthdate);
        bundle.putString("age",age);
        bundle.putString("gender",gender);
        bundle.putString("password",password);
        destFragment.setArguments(bundle);
        fragmentTransaction.commit();
    }

    private String getPhone() {
        TelephonyManager phoneMgr = (TelephonyManager) getContext(). getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), wantPermission) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        return phoneMgr.getLine1Number();
    }

    private void requestPermission(String permission){
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
            Toast.makeText(getActivity(), "Phone state permission allows us to get phone number. Please allow it for additional functionality.", Toast.LENGTH_LONG).show();
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{permission},PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "Phone number: " + getPhone());
                } else {
                    Toast.makeText(getActivity(),"Permission Denied. We can't get phone number.", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private boolean checkPermission(String permission){
        if (Build.VERSION.SDK_INT >= 23) {
            int result = ContextCompat.checkSelfPermission(getActivity(), permission);
            if (result == PackageManager.PERMISSION_GRANTED){
                return true;
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    void getOtp()
    {
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


                               replaceFragment(new OtpFragment());


                            }
                            else{
                               Toast.makeText(context, "Sorry for inconvenience, \nPlease try again!", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       /* */

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

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }
    void verifyMobileNo()
    {

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

                                customDialogAccountExists = new CustomDialogAccountExists(context);

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
                                        editText_confirmPassword.setText("");
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
                        Toast.makeText(context,"Error in SignUP verifyMobile"+error.toString(),Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("OTPMobileNo", mobileNo);
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);



    }

    private  void  ShowCalendar(){
        editText_birthdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();

                final DatePickerDialog dialog = new DatePickerDialog(getContext(),R.style.DialogTheme,
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
    TextWatcher change = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String mobile = editText_fullName.getText().toString().trim();
            String email = editText_emailId.getText().toString().trim();
            String birthdate = editText_birthdate.getText().toString().trim();
            String age = editText_age.getText().toString().trim();
            String password = editText_password.getText().toString().trim();
            String confirmpassword = editText_confirmPassword.getText().toString().trim();


            if (!mobile.isEmpty() && !email.isEmpty() && !birthdate.isEmpty()  && !age.isEmpty()&& !confirmpassword.isEmpty()&& !password.isEmpty()&& confirmpassword.equals(password)) {
                txt_saveAndContinue.setEnabled(true);
               // txt_saveAndContinue.setBackgroundColor(Color.parseColor("#cb2470"));
            } else {
                txt_saveAndContinue.setEnabled(true);
               // txt_saveAndContinue.setBackgroundColor(Color.parseColor("#bdc0c6"));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onOTPReceived(String otp) {

    }

    @Override
    public void onOTPTimeOut() {

    }

    @Override
    public void onOTPReceivedError(String error) {

    }

    private class AsyncTaskLoad extends AsyncTask<String, String, String> {


        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {

                //customDialogLoadingProgressBar.dismiss();

                if(params[0].equals("verifyMobileNo"))
                {
                    verifyMobileNo();

                }
                else if(params[0].equals("getOTP"))
                {
                   getOtp();
                }




                return params[0];


            } catch (Exception e) {
                e.printStackTrace();
                //resp = e.getMessage();
            }
            return params[0];
        }


        @Override
        protected void onPostExecute(String result) {
            customDialogLoadingProgressBar.dismiss();
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