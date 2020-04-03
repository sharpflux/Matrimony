package com.example.matrimonyapp.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.matrimonyapp.fragment.ViewInterestFragment;
import com.example.matrimonyapp.fragment.ViewQualificationDetailsFragment;

public class InterestTabLayoutAdapter extends FragmentPagerAdapter {

    Context context;
    int totalTabs;



    public  InterestTabLayoutAdapter (Context context, FragmentManager fragmentManager, int totalTabs)
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
                ViewInterestFragment viewInterestReceivedFragment =
                        new ViewInterestFragment(context,"Received");

                return viewInterestReceivedFragment;


            case 1:
                ViewInterestFragment viewInterestSentFragment =
                        new ViewInterestFragment(context,"Sent");

                return viewInterestSentFragment;


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
