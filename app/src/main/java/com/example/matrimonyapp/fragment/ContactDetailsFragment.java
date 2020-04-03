package com.example.matrimonyapp.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailsFragment extends Fragment {


    View view;
    TextView textView_save;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_contact_details, container, false);

        TextView tv=((MainActivity)getActivity()).findViewById(R.id.textView_toolbar);
        tv.setText("Contact Details");


        textView_save = view.findViewById(R.id.txt_saveAndContinue);

        textView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FragmentTransaction transection = getFragmentManager().beginTransaction();
                transection.replace(R.id.dynamic_fragment_frame_layout, new FamilyDetailsFragment());
                transection.commit() ;
            }
        });
        return view;

    }

}
