package com.example.matrimonyapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.fragment.BasicDetailsFragment;
import com.example.matrimonyapp.fragment.FamilyDetailsFragment;
import com.example.matrimonyapp.fragment.PersonalDetailsFragment;
import com.example.matrimonyapp.fragment.ProfessionalDetailsFragment;
import com.example.matrimonyapp.fragment.QualificationDetailsFragment;
import com.example.matrimonyapp.fragment.ReligiousDetailsFragment;

public class MainActivity extends AppCompatActivity {

    private String currentLanguage;
    private String fragmentName="Basic";
    private Fragment fragment;
    ImageView imageView_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView_back=findViewById(R.id.imageView_back);

        setTitle("Registration");
        RelativeLayout relativeLayout = findViewById(R.id.dynamic_fragment_frame_layout);
        relativeLayout.getClass().getName();
        FragmentTransaction fragmentTransaction = this.getSupportFragmentManager().beginTransaction();
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null)
        {
            fragmentName = bundle.getString("fragmentName");

         if( bundle.getString("ShowBackButton").equals("No")){
                imageView_back.setVisibility(View.GONE);
            }
          else {
                imageView_back.setVisibility(View.VISIBLE);
            }

            switch (fragmentName)
            {
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

    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


}
