package com.example.matrimonyapp.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.activity.SignUp;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.AppSignatureHashHelper;
import com.example.matrimonyapp.utils.SMSReceiver;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    EditText editText_fullName, email_et,editText_birthdate,editText_age,editText_password,editText_confirmPassword;
    TextView txt_saveAndContinue;

    ImageView password_show, password_hide,password_show1,password_hide1;
    FrameLayout frame_eye,frame_eye1;
    private int passwordNotVisible = 1;
    private int ConfirmpasswordNotVisible = 1;
    String TAG = "PhoneActivityTAG";

    String wantPermission = Manifest.permission.READ_PHONE_STATE;
    private static final int PERMISSION_REQUEST_CODE = 1;

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
        email_et = view.findViewById(R.id.editText_emailId);
        editText_birthdate = view.findViewById(R.id.editText_birthdate);
        editText_age= view.findViewById(R.id.editText_age);
        txt_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        editText_password = view.findViewById(R.id.editText_password);
        editText_confirmPassword= view.findViewById(R.id.editText_confirmPassword);
        frame_eye = (FrameLayout) view.findViewById(R.id.frame_eye);
        password_show = (ImageView) view.findViewById(R.id.password_show);
        password_hide = (ImageView) view.findViewById(R.id.password_hide);


        frame_eye1 = (FrameLayout) view.findViewById(R.id.frame_eye1);
        password_show1 = (ImageView) view.findViewById(R.id.password_show1);
        password_hide1 = (ImageView) view.findViewById(R.id.password_hide1);



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
        email_et.addTextChangedListener(change);
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
                replaceFragment(new OtpFragment());
            }
        });


        AppSignatureHashHelper appSignatureHashHelper = new AppSignatureHashHelper(getContext());

        // This code requires one time to get Hash keys do comment and share key
        Log.i("TAG", "HashKey: " + appSignatureHashHelper.getAppSignatures().get(0));

        UserModel user = CustomSharedPreference.getInstance(getContext()).getUser();

        if (user != null) {
            editText_fullName.setText(user.getFullName());
            email_et.setText(user.getEmailId());
        }

        //startSMSListener();
        if (!checkPermission(wantPermission)) {
            requestPermission(wantPermission);
        } else {
            Log.d(TAG, "Phone number: " + getPhone());
        }
        return  view;
    }


    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
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
            String email = email_et.getText().toString().trim();
            String birthdate = editText_birthdate.getText().toString().trim();
            String age = editText_age.getText().toString().trim();
            String password = editText_password.getText().toString().trim();
            String confirmpassword = editText_confirmPassword.getText().toString().trim();


            if (!mobile.isEmpty() && !email.isEmpty() && !birthdate.isEmpty()  && !age.isEmpty()&& !confirmpassword.isEmpty()&& !password.isEmpty()&& confirmpassword.equals(password)) {
                txt_saveAndContinue.setEnabled(true);
                txt_saveAndContinue.setBackgroundColor(Color.parseColor("#cb2470"));
            } else {
                txt_saveAndContinue.setEnabled(true);
                txt_saveAndContinue.setBackgroundColor(Color.parseColor("#bdc0c6"));
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
}