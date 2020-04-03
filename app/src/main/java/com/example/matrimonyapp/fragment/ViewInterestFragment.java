package com.example.matrimonyapp.fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.InterestListAdapter;
import com.example.matrimonyapp.modal.TimelineModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewInterestFragment extends Fragment {

    Context context;
    String interestType;
    View view;

    RecyclerView recyclerView_interest;

    ArrayList<TimelineModel> timelineModelList;
    InterestListAdapter interestListAdapter;

    public ViewInterestFragment(Context context, String interestType) {
        this.context = context;
        this.interestType = interestType;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_interest, container, false);

        recyclerView_interest = view.findViewById(R.id.recyclerView_interest);

        timelineModelList = new ArrayList<TimelineModel>();


        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower2)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower2)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower2));

        for(int i=0; i<15; i++)
        {
            TimelineModel timelineModel = new TimelineModel("#yourUserId", "User Name", "25 Yrs", "MBA - Marketing, Chennai, India","Bio loading!...",uri, context);
            timelineModelList.add(timelineModel);

        }


        interestListAdapter = new InterestListAdapter(context,timelineModelList);
        recyclerView_interest.setAdapter(interestListAdapter);
        recyclerView_interest.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_interest.setLayoutManager(mLayoutManager);


        return view;
    }

}
