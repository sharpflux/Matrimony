package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.matrimonyapp.fragment.AboutEditProfileFragment;
import com.example.matrimonyapp.fragment.ViewFamilyDetailsFragment;
import com.example.matrimonyapp.fragment.ViewPersonalDetailsFragment;
import com.example.matrimonyapp.fragment.ViewPreferencesFragment;
import com.example.matrimonyapp.fragment.ViewQualificationDetailsFragment;

public class ProfileTabLayoutAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;
    String userId;

    private static String activityName;


    public  ProfileTabLayoutAdapter(Context context, FragmentManager fragmentManager, int totalTabs, String userId, String activityName)
    {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
        this.userId = userId;
        this.activityName = activityName;

    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        if (activityName.equals("ViewDetails")) {

            switch (position) {

                case 0:
                    ViewPersonalDetailsFragment viewPersonalDetailsFragment =
                            new ViewPersonalDetailsFragment();
                    bundle.putString("activityName", activityName);
                    viewPersonalDetailsFragment.setArguments(bundle);
                    return viewPersonalDetailsFragment;


                case 1:
                    ViewFamilyDetailsFragment viewFamilyDetailsFragment =
                            new ViewFamilyDetailsFragment();
                    bundle.putString("activityName", activityName);
                    viewFamilyDetailsFragment.setArguments(bundle);
                    return viewFamilyDetailsFragment;

                case 2:
                    ViewPreferencesFragment viewPreferencesFragment =
                            new ViewPreferencesFragment();
                    bundle.putString("activityName", activityName);
                    viewPreferencesFragment.setArguments(bundle);
                    return viewPreferencesFragment;

                default:
                    return null;
            }

        }
        else if (activityName.equals("EditProfile")){
            switch (position) {

                case 0:
                    AboutEditProfileFragment aboutEditProfileFragment =
                            new AboutEditProfileFragment();
                    bundle.putString("activityName", activityName);
                    aboutEditProfileFragment.setArguments(bundle);
                    return aboutEditProfileFragment;


                case 1:
                    ViewPreferencesFragment viewPreferencesFragment =
                            new ViewPreferencesFragment();
                    bundle.putString("activityName", activityName);
                    viewPreferencesFragment.setArguments(bundle);
                    return viewPreferencesFragment;

                default:
                    return null;
            }

        }
        return null;

    }

    @Override
    public int getCount() {
        return totalTabs;
    }
}
