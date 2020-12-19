package com.example.matrimonyapp.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.customViews.CustomDialog;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.fragment.BasicDetailsFragment;
import com.example.matrimonyapp.modal.MyItem;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataFetcher {

    String url_type;
    Context context;
    //final MyItem []myItemsList;

    CustomDialog customDialog;


    public DataFetcher(String url_type,Context context){//MyItem Items, ArrayList<MyItem> list) {
        this.url_type = url_type;
        this.context = context;
        //myItemsList = new MyItem[37];

    }

    public void loadList(final String url, final String columnId, final String columnName,
                         final EditText edit_text, final TextView textViewId, final Context context,
                         final CustomDialogLoadingProgressBar customDialogLoadingProgressBar)
    {
        edit_text.setClickable(false);


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {


                            JSONArray obj = new JSONArray(response);

                            if (obj.length() != 0) {

                                customDialogLoadingProgressBar.dismiss();

                                ArrayList<MyItem> list = new ArrayList<>();
                                MyItem []mil = new MyItem[obj.length()];

                                for (int i = 0; i < obj.length(); i++) {

                                    JSONObject item = obj.getJSONObject(i);

                                    MyItem mi = new MyItem(item.getInt(columnId),item.getString(columnName));

                                    mil[i]=mi;
                                    list.add(mi);
                                }

                                //= Arrays.asList(mil);

                                /*final Dialog dialog = new Dialog(context);
                                dialog.setContentView(R.layout.prompts);
                                dialog.setTitle("Search by "+url_type+"...");

                                TextView txt_ok = dialog.findViewById(R.id.txt_ok);
                                TextView txt_cancel = dialog.findViewById(R.id.txt_cancel);
                                // final SearchView searchView_search = dialog.findViewById(R.id.searchView_search);
                                final EditText editText_search = dialog.findViewById(R.id.editText_search);

                                RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView_prompt);
                                recyclerView.setHasFixedSize(true);*/




                                final PromptAdapter adapter = new PromptAdapter( list,edit_text,textViewId, new PromptAdapter.RecyclerViewItemClickListener() {
                                    @Override
                                    public void clickOnItem(MyItem data) {

                                        edit_text.setText(data.getName());
                                        textViewId.setText(String.valueOf(data.getId()));
                                        if(customDialog!=null)
                                        {
                                            customDialog.dismiss();

                                        }

                                    }
                                });


                                customDialog = new CustomDialog(context,adapter);
                                customDialog.show();

                                edit_text.setClickable(true);

                            }
                            else {
                                customDialogLoadingProgressBar.dismiss();
                                Toast.makeText(context, "No Items", Toast.LENGTH_SHORT).show();
                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


    }

    void getItems(final Dialog dialog, final String url, final String columnId, final String columnName, final EditText edit_text )
    {

        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    Context context;
                    public void onResponse(String response) {

                        try {


                            JSONArray obj = new JSONArray(response);

                            if (obj.length() != 0) {


                                MyItem []mil = new MyItem[obj.length()];

                                for (int i = 0; i < obj.length(); i++) {

                                    JSONObject item = obj.getJSONObject(i);

                                    MyItem mi = new MyItem(item.getInt(columnId),item.getString(columnName));

                                    mil[i]=mi;

                                }
                                RecyclerView recyclerView = (RecyclerView) dialog.findViewById(R.id.recyclerView_prompt);
                                recyclerView.setHasFixedSize(true);
                                /*PromptAdapter adapter = new PromptAdapter(mil, new PromptAdapter.RecyclerViewItemClickListener() {
                                    @Override
                                    public void clickOnItem(MyItem data) {

                                        edit_text.setText(data.getName());
                                        if(dialog!=null)
                                        {
                                            dialog.dismiss();
                                        }
                                    }
                                });*/
                                //recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                //recyclerView.setAdapter(adapter);



                            } else {

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);

    }


}



