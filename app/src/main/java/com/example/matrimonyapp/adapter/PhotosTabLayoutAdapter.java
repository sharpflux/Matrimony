package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.matrimonyapp.fragment.AboutEditProfileFragment;
import com.example.matrimonyapp.fragment.GallaryFragment;
import com.example.matrimonyapp.fragment.ViewFamilyDetailsFragment;
import com.example.matrimonyapp.fragment.ViewPersonalDetailsFragment;
import com.example.matrimonyapp.fragment.ViewPreferencesFragment;

public class PhotosTabLayoutAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public PhotosTabLayoutAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                GallaryFragment homeFragment = new GallaryFragment();
                return homeFragment;
            case 1:
                GallaryFragment homeFragment3 = new GallaryFragment();
                return homeFragment3;
               // SportFragment sportFragment = new SportFragment();
                //return sportFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}

