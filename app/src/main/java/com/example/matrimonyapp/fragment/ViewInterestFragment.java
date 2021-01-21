package com.example.matrimonyapp.fragment;


import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.InterestListAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.modal.UsersConnectionModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewInterestFragment extends Fragment {

    Context context;
    String interestType;
    View view;
    UserModel userModel;

    RecyclerView recyclerView_interest;

    CustomDialogLoadingProgressBar customDialogLoadingProgressBar;

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
        userModel = CustomSharedPreference.getInstance(getContext()).getUser();
        timelineModelList = new ArrayList<TimelineModel>();



        interestListAdapter = new InterestListAdapter(context,timelineModelList);
        recyclerView_interest.setAdapter(interestListAdapter);
        recyclerView_interest.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_interest.setLayoutManager(mLayoutManager);


        if(interestType.equals("Sent"))
        {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(String.valueOf(UsersConnectionModel.INTERESTED),"null");
        }
        else if(interestType.equals("Accepted"))
        {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(String.valueOf(UsersConnectionModel.INTERESTED),"true");
        }
        else if(interestType.equals("Favorites"))
        {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(String.valueOf(UsersConnectionModel.FAVORITE),"null");
        }
        else if(interestType.equals("Rejected"))
        {
            AsyncTaskRunner runner = new AsyncTaskRunner();
            runner.execute(String.valueOf(UsersConnectionModel.REJECTED),"null");
        }



        return view;
    }

    class AsyncTaskRunner extends AsyncTask<String, String, String>
    {
        @Override
        protected void onPreExecute() {

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(getContext());
            customDialogLoadingProgressBar.show();

        }

        @Override
        protected void onPostExecute(String s) {

        }

        @Override
        protected String doInBackground(String... params) {


            getDetails(params[0], params[1]);


            return null;
        }
    }

    private void getDetails(String connectionTypeId, final String acceptedStatus) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URLs.URL_GET_USER_CONNECTIONS+"UserId="+userModel.getUserId()
                        +"&pageIndex=1&pageSize=20&Search=&ConntectionTypeId="+ connectionTypeId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                           // customDialogLoadingProgressBar.dismiss();

                            //converting response to json object
                            JSONArray jsonArray = new JSONArray(response);

                            if(jsonArray.length()>0)
                            {
                                for (int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);


                                    TimelineModel timelineModel = new TimelineModel();
                                    timelineModel.setUserName(jsonObject.getString("FullName"));
                                    timelineModel.setProfilePic(jsonObject.getString("ImageUrl"));
                                    timelineModel.setUserAge(jsonObject.getString("Age"));
                                    timelineModel.setUserMobileNo(jsonObject.getString("Mobile"));
                                    timelineModel.setUserBirthday(jsonObject.getString("DateofBirth"));
                                    timelineModel.setUserId(jsonObject.getString("ToUserId"));

                                    if(interestType.equals("Accepted"))
                                    {
                                        if (jsonObject.getString("IsAccepted").equals(acceptedStatus)) {
                                            timelineModelList.add(timelineModel);
                                        }


                                    }
                                    else if(interestType.equals("Sent"))
                                    {
                                        if (jsonObject.getString("IsAccepted").equals(acceptedStatus)) {
                                            timelineModelList.add(timelineModel);
                                        }

                                    }else
                                    {
                                        timelineModelList.add(timelineModel);
                                    }



                                }

                                interestListAdapter.notifyDataSetChanged();

                            }
                            else
                            {

                                Toast.makeText(getContext(),"Invalid Details GET! ",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(),"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);





    }


}
