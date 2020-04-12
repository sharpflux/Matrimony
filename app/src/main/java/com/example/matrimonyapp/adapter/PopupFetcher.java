package com.example.matrimonyapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.ContextThemeWrapper;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PopupFetcher {

    public Context context;

    private UserModel userModel;

    public PopupFetcher() {
    }

    public PopupFetcher(Context context) {

        this.context = context;
    }

    public void loadList(final String url, final String columnId, final String columnName,
                         final EditText editText, final TextView textViewId, final Context context, int customStyle, int id)
    {

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(context).getUser();


        Context wrapper = new ContextThemeWrapper(context, customStyle);

        final PopupMenu popupMenu = new PopupMenu(wrapper, editText);

        popupMenu.getMenu().add("-- Select --");
        //popupMenu.getMenu().getItem(0).setEnabled(false);

        editText.setText(popupMenu.getMenu().getItem(id).getTitle());

        final Map<String, Integer> list = getSiblingsList(url, columnId, columnName, editText, popupMenu, context);
        //



        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        editText.setText(menuItem.getTitle());
                        if(list.get(menuItem.getTitle().toString()).intValue()==-1)
                        {
                            textViewId.setText("");
                        }
                        else
                        {
                            textViewId.setText(list.get(menuItem.getTitle()).toString());
                        }
                        return true;
                    }
                });

                popupMenu.show();

            }
        });





    }

    public Map getSiblingsList(final String url, final String columnId, final String columnName,
                               final EditText editText, final PopupMenu popupMenu, final Context context)
    {
        final Map<String, Integer> list = new HashMap<String, Integer>();

        list.put("-- Select --",
                new Integer(-1));


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {


                            JSONArray jsonArray = new JSONArray(response);

                            if (jsonArray.length() > 0) {



                                for(int i=0; i<jsonArray.length(); i++)
                                {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    list.put(jsonObject.getString(columnName),
                                            new Integer(jsonObject.getInt(columnId)));



                                    popupMenu.getMenu().add(jsonObject.getString(columnName));

                                }


                            } else {
                                Toast.makeText(context,"Invalid Request",Toast.LENGTH_SHORT).show();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"Something went wrong!\nPlease try again later",Toast.LENGTH_SHORT).show();

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);


        //editText.setText(popupMenu.getMenu().getItem(0).getTitle());
        return list;


    }



}
