package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.matrimonyapp.fragment.ViewFamilyDetailsFragment;
import com.example.matrimonyapp.fragment.ViewPersonalDetailsFragment;
import com.example.matrimonyapp.fragment.ViewQualificationDetailsFragment;

public class ProfileTabLayoutAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;
    String userId;



    public  ProfileTabLayoutAdapter(Context context, FragmentManager fragmentManager, int totalTabs, String userId)
    {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
        this.userId = userId;

    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);

        switch (position)
        {

            case 0:
                ViewPersonalDetailsFragment viewPersonalDetailsFragment =
                        new ViewPersonalDetailsFragment();
                viewPersonalDetailsFragment.setArguments(bundle);
                return viewPersonalDetailsFragment;


            case 1:
                ViewFamilyDetailsFragment viewFamilyDetailsFragment =
                        new ViewFamilyDetailsFragment();
                viewFamilyDetailsFragment.setArguments(bundle);
                return  viewFamilyDetailsFragment;

            case 2:
                ViewQualificationDetailsFragment viewQualificationDetailsFragment =
                        new ViewQualificationDetailsFragment();
                viewQualificationDetailsFragment.setArguments(bundle);
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
