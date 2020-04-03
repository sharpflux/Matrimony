package com.example.matrimonyapp.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.matrimonyapp.fragment.ViewFamilyDetailsFragment;
import com.example.matrimonyapp.fragment.ViewPersonalDetailsFragment;
import com.example.matrimonyapp.fragment.ViewQualificationDetailsFragment;

public class ProfileTabLayoutAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;



    public  ProfileTabLayoutAdapter(Context context, FragmentManager fragmentManager, int totalTabs)
    {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;

    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {

            case 0:
                ViewPersonalDetailsFragment viewPersonalDetailsFragment =
                        new ViewPersonalDetailsFragment();

                return viewPersonalDetailsFragment;


            case 1:
                ViewFamilyDetailsFragment viewFamilyDetailsFragment =
                        new ViewFamilyDetailsFragment();
                return  viewFamilyDetailsFragment;

            case 2:
                ViewQualificationDetailsFragment viewQualificationDetailsFragment =
                        new ViewQualificationDetailsFragment();
                return viewQualificationDetailsFragment;

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
