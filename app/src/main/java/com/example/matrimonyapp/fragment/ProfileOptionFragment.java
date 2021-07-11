package com.example.matrimonyapp.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileOptionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileOptionFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context context;
    private RadioGroup mFirstGroup;
    private RadioGroup mSecondGroup;
    private  RadioGroup mthirdGroup;
    private  RadioGroup mfourgroup;
    View view;
    private boolean isChecking = true;
    private TextView textView_saveAndContinue;
    private int mCheckedId = R.id.radioSelf;

    public ProfileOptionFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileOptionFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileOptionFragment newInstance(String param1, String param2) {
        ProfileOptionFragment fragment = new ProfileOptionFragment();
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
        context = getContext();
        TextView tv = ((MainActivity) getActivity()).findViewById(R.id.textView_toolbar);
        //tv.setText("Family Details");
        tv.setText(context.getResources().getString(R.string.profile_for));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile_option, container, false);

        mFirstGroup = (RadioGroup) view. findViewById(R.id.first_group);
        mSecondGroup = (RadioGroup) view.findViewById(R.id.second_group);
        mthirdGroup = (RadioGroup) view.findViewById(R.id.third_group);
        mfourgroup = (RadioGroup) view.findViewById(R.id.four_group);

        mFirstGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;


                    if(mthirdGroup.getCheckedRadioButtonId()!=-1){
                        mthirdGroup.clearCheck();
                    }

                    if(mSecondGroup.getCheckedRadioButtonId()!=-1){
                        mSecondGroup.clearCheck();
                    }
                    if(mfourgroup.getCheckedRadioButtonId()!=-1){
                        mfourgroup.clearCheck();
                    }
                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });

        mSecondGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;

                    if(mthirdGroup.getCheckedRadioButtonId()!=-1){
                        mthirdGroup.clearCheck();
                    }

                    if(mFirstGroup.getCheckedRadioButtonId()!=-1){
                        mFirstGroup.clearCheck();
                    }
                    if(mfourgroup.getCheckedRadioButtonId()!=-1){
                        mfourgroup.clearCheck();
                    }



                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });


        mthirdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;


                    if(mFirstGroup.getCheckedRadioButtonId()!=-1){
                        mFirstGroup.clearCheck();
                    }

                    if(mSecondGroup.getCheckedRadioButtonId()!=-1){
                        mSecondGroup.clearCheck();
                    }


                    if(mfourgroup.getCheckedRadioButtonId()!=-1){
                        mfourgroup.clearCheck();
                    }


                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });


        mfourgroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1 && isChecking) {
                    isChecking = false;


                    if(mFirstGroup.getCheckedRadioButtonId()!=-1){
                        mFirstGroup.clearCheck();
                    }

                    if(mSecondGroup.getCheckedRadioButtonId()!=-1){
                        mSecondGroup.clearCheck();
                    }


                    if(mthirdGroup.getCheckedRadioButtonId()!=-1){
                        mthirdGroup.clearCheck();
                    }


                    mCheckedId = checkedId;
                }
                isChecking = true;
            }
        });

        textView_saveAndContinue=((MainActivity)getActivity()).findViewById(R.id.txt_saveAndContinue);

        textView_saveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new BasicDetailsFragment());
            }
        });

        return  view;

    }

    public void replaceFragment(Fragment destFragment)
    {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.dynamic_fragment_frame_layout, destFragment);
        fragmentTransaction.commit();
    }
}