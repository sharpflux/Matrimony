package com.example.matrimonyapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.matrimonyapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewFamilyDetailsFragment extends Fragment {


    public ViewFamilyDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_family_details, container, false);
    }

}
