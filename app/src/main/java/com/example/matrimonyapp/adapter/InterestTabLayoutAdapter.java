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
    String fragmentFor;


    public  InterestTabLayoutAdapter (Context context, FragmentManager fragmentManager, int totalTabs,String fragmentFor)
    {
        super(fragmentManager);
        this.context = context;
        this.totalTabs = totalTabs;
        this.fragmentFor = fragmentFor;

    }

    @Override
    public Fragment getItem(int position) {


        if(fragmentFor.equals("Interest")) {

            switch (position) {

                case 0:
                    ViewInterestFragment viewInterestReceivedFragment =
                            new ViewInterestFragment(context, "Receive");

                    return viewInterestReceivedFragment;


                case 1:
                    ViewInterestFragment viewInterestSentFragment =
                            new ViewInterestFragment(context, "Sent");

                    return viewInterestSentFragment;


                case 2:
                    ViewInterestFragment viewInterestAcceptedFragment =
                            new ViewInterestFragment(context, "Accepted");
                    return viewInterestAcceptedFragment;

                default:
                    return null;
            }
        }
        else if(fragmentFor.equals("Favorites"))
        {
            switch (position) {

                case 0:
                    ViewInterestFragment viewFavoritesFragment =
                            new ViewInterestFragment(context, "Favorites");

                    return viewFavoritesFragment;


                case 1:
                    ViewInterestFragment viewRejectedFragment =
                            new ViewInterestFragment(context, "Rejected");

                    return viewRejectedFragment;



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
