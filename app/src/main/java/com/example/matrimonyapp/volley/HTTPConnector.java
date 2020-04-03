package com.example.matrimonyapp.volley;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class HTTPConnector {
    String url;
    JSONObject jsonResponse;
    Context context;
    IMyInterface  mListener;
    public interface IMyInterface
    {
        void sendResponse(JSONObject obj);
    }

    public HTTPConnector(String url, Context context, IMyInterface pListener){
        this.url=url;
        this.context=context;
        mListener=pListener;
    }


    public void makeQuery() {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                jsonResponse = response;
                if (mListener != null) {
                    mListener.sendResponse(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERROR", Toast.LENGTH_SHORT).show();
                Log.d("ERROR: ", error.toString());
                if (mListener != null) {
                    mListener.sendResponse(null);
                }
            }
        });
        VolleySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(request);
    }
}
