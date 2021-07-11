package com.example.matrimonyapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.fragment.BasicDetailsFragment;
import com.example.matrimonyapp.fragment.FamilyDetailsFragment;
import com.example.matrimonyapp.fragment.PersonalDetailsFragment;
import com.example.matrimonyapp.fragment.ProfessionalDetailsFragment;
import com.example.matrimonyapp.fragment.ProfileOptionFragment;
import com.example.matrimonyapp.fragment.QualificationDetailsFragment;
import com.example.matrimonyapp.fragment.ReligiousDetailsFragment;

public class MainActivity extends AppCompatActivity {

    private String currentLanguage;
    private String fragmentName="Basic";
    private Fragment fragment;
    ImageView imageView_back;
    TextView txt_saveAndContinue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView_back=findViewById(R.id.imageView_back);

        setTitle("Registration");
        RelativeLayout relativeLayout = findViewById(R.id.dynamic_fragment_frame_layout);
        relativeLayout.getClass().getName();
        txt_saveAndContinue=findViewById(R.id.txt_saveAndContinue);



      final   FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        Bundle bundle = getIntent().getExtras();
        fragment = new ProfileOptionFragment();






        if (bundle!=null)
        {
            fragmentName = bundle.getString("fragmentName");

            if(bundle.getString("ShowBackButton")!=null) {
                if (bundle.getString("ShowBackButton").equals("No")) {
                    imageView_back.setVisibility(View.GONE);
                } else {
                    imageView_back.setVisibility(View.VISIBLE);
                }
            }

            switch (fragmentName)
            {

                case "Option" :
                    fragment = new ProfileOptionFragment();
                    break;

                case "Basic" :
                    fragment = new BasicDetailsFragment();
                    break;

                case "Religious" :
                    fragment = new ReligiousDetailsFragment();
                    break;

                case "Personal" :
                    fragment = new PersonalDetailsFragment();
                    break;


                case "Qualification" :
                    fragment = new QualificationDetailsFragment();
                    break;

                case "Professional" :
                    fragment = new ProfessionalDetailsFragment();
                    break;

                case "Family" :
                    fragment = new FamilyDetailsFragment();
                    break;



            }


        }


        fragmentTransaction.add(R.id.dynamic_fragment_frame_layout, fragment);
        fragmentTransaction.commit();
        currentLanguage = getResources().getConfiguration().locale.getLanguage();
    }
    public  void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radioSelf:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    view.setPadding(30,30,30,30);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);
                }

                break;

            case R.id.radioRelative:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    view.setPadding(30,30,30,30);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);

                }

                break;



            case R.id.radioSon:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));

                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);

                }

                break;
            case R.id.radioDaughter:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));

                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);
                }

                break;



            case R.id.radioBrother:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));

                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);
                }

                break;


            case R.id.radioSister:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));

                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);
                }

                break;

            case R.id.radioAgent:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));

                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);



                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioFriend)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioFriend)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioFriend)).setPadding(30,30,30,30);
                }

                break;


            case R.id.radioFriend:
                if(checked)
                {
                    view.setBackgroundResource(R.drawable.checkbox_selected);
                    view.setPadding(30,30,30,30);
                    ((RadioButton) view).setTextColor((getResources().getColor(R.color.white)));

                    ((RadioButton)findViewById(R.id.radioSelf)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSelf)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSelf)).setPadding(30,30,30,30);

                    ((RadioButton)findViewById(R.id.radioRelative)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioRelative)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioRelative)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioSon)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSon)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSon)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioDaughter)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioDaughter)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioDaughter)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioBrother)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioBrother)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioBrother)).setPadding(30,30,30,30);



                    ((RadioButton)findViewById(R.id.radioSister)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioSister)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioSister)).setPadding(30,30,30,30);


                    ((RadioButton)findViewById(R.id.radioAgent)) .setTextColor((getResources().getColor(R.color.black)));
                    ((RadioButton)findViewById(R.id.radioAgent)) .setBackgroundResource(R.drawable.border);
                    ((RadioButton)findViewById(R.id.radioAgent)).setPadding(30,30,30,30);
                }

                break;




        }


        // Check which radio button was clicked

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

}
