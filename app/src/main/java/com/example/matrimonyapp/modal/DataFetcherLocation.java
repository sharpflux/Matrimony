package com.example.matrimonyapp.modal;

import android.content.Context;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.AddLocationAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLocationRec;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataFetcherLocation {
    private AddLOcationModal lOcationModal;
    private CustomDialogLocationRec customDialogLocationRec;
    private ArrayList<AddLOcationModal> list;
    SearchView searchView;
    AddLocationAdapter dataAdapter;



    private Context context;


    public DataFetcherLocation(AddLOcationModal lOcationModal, CustomDialogLocationRec customDialogLocationRec, ArrayList<AddLOcationModal> list, Context context) {
        this.lOcationModal = lOcationModal;
        this.customDialogLocationRec = customDialogLocationRec;
        this.list = list;
        this.context = context;


    }




    public void loadList(final String ColumnName, final TextView textView, final  String URL, final String id, final TextView hiddenText,
                         final String ParameterName, final String ParameterValue,final String filterBy) {
        list.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {

                            JSONArray obj = new JSONArray(response);

                            for (int i = 0; i < obj.length(); i++) {
                                //getting the user from the response
                                JSONObject userJson = obj.getJSONObject(i);

                                if (!userJson.getBoolean("error")) {

                                    lOcationModal = new AddLOcationModal(
                                            userJson.getString(ColumnName),
                                            userJson.getString(id),filterBy

                                    );

                                    list.add(lOcationModal);

                                } else {
                                    Toast.makeText(context, response, Toast.LENGTH_SHORT).show();
                                }
                            }
                            dataAdapter = new AddLocationAdapter(context,list);
                            /*{
                                @Override
                                public void clickOnItem(AddLOcationModal data) {
                                    textView.setText(data.getName());
                                    hiddenText.setText(data.getId());

                                    if (customDialogLocationRec != null) {
                                        customDialogLocationRec.dismiss();
                                    }
                                }
                            });*/
                            customDialogLocationRec = new CustomDialogLocationRec(context, dataAdapter);
                            customDialogLocationRec.show();
                            customDialogLocationRec.setCanceledOnTouchOutside(false);
//


                            searchView =customDialogLocationRec.findViewById(R.id.searchView_customDialog);

                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                @Override
                                public boolean onQueryTextSubmit(String query) {

                                    return false;
                                }

                                @Override
                                public boolean onQueryTextChange(String newText) {

                                    dataAdapter.getFilter().filter(newText);
                                    return false;

                                }
                            });



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put(ParameterName, ParameterValue);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS *
                2, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

}
