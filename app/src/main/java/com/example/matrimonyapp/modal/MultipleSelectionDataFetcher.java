package com.example.matrimonyapp.modal;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.CompoundButton;
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
import com.example.matrimonyapp.adapter.MultipleSelectionAdapter;
import com.example.matrimonyapp.adapter.PromptAdapter;
import com.example.matrimonyapp.customViews.CustomDialog;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.customViews.CustomDialogMultipleSelection;
import com.example.matrimonyapp.modal.MyItem;
import com.example.matrimonyapp.volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MultipleSelectionDataFetcher {

    String url_type;
    Context context;

    public CustomDialogMultipleSelection customDialogMultipleSelection;


    public MultipleSelectionDataFetcher (String url_type,Context context){//MyItem Items, ArrayList<MyItem> list) {
        this.url_type = url_type;
        this.context = context;

    }

    public void loadList(final String url, final String columnId, final String columnName,
                         final EditText edit_text, final ArrayList arrayList_id, final Context context,
                         final CustomDialogLoadingProgressBar customDialogLoadingProgressBar)
    {


        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    public void onResponse(String response) {

                        try {




                            JSONArray obj = new JSONArray(response);

                            if (obj.length() != 0) {

                                customDialogLoadingProgressBar.dismiss();

                                final ArrayList<MyItem> list = new ArrayList<>();
                                MyItem []mil = new MyItem[obj.length()];

                                for (int i = 0; i < obj.length(); i++) {

                                    JSONObject item = obj.getJSONObject(i);

                                    MyItem mi = new MyItem(item.getInt(columnId),item.getString(columnName),false);

                                    mil[i]=mi;
                                    list.add(mi);
                                }

                                for(int i=0; i<arrayList_id.size(); i++)
                                {
                                    for (int j=0; j<list.size(); j++) {

                                        if((int)arrayList_id.get(i)==list.get(j).getId())
                                        {
                                            list.get(j).setChecked(true);
                                        }
                                    }
                                }



                                final MultipleSelectionAdapter adapter = new MultipleSelectionAdapter(list, edit_text, new MultipleSelectionAdapter.RecyclerViewCheckedChangekListener() {
                                    @Override
                                    public void checkedChange(boolean b) {

                                            boolean flag = true;    //flag = b
                                            for (int i = 0; i < list.size(); i++) {
                                                if (!list.get(i).isChecked()) { // !b
                                                    flag = false;   // flag = !b
                                                }
                                            }
                                            if(flag == true  ){ //flag!=b
                                                customDialogMultipleSelection.checkBox_selectAll.setChecked(true);
                                            }
                                            else
                                            {
                                                customDialogMultipleSelection.checkBox_selectAll.setChecked(false);
                                            }



                                    }
                                });



                                customDialogMultipleSelection = new CustomDialogMultipleSelection(context,adapter);
                                customDialogMultipleSelection.show();

                                if(arrayList_id.size()==list.size())
                                {
                                    customDialogMultipleSelection.checkBox_selectAll.setChecked(true);
                                }

                                customDialogMultipleSelection.checkBox_selectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                    @Override
                                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                                        if(compoundButton.isPressed()) {

                                            for (int i = 0; i < list.size(); i++) {
                                                list.get(i).setChecked(b);
                                            }

                                            adapter.notifyDataSetChanged();
                                        }
                                    }
                                });

                                customDialogMultipleSelection.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                    @Override
                                    public void onDismiss(DialogInterface dialogInterface) {

                                        edit_text.setText("");
                                        arrayList_id.clear();
                                        String names = "";

                                        for(int i=0; i<list.size(); i++)
                                        {
                                            if(list.get(i).isChecked())
                                            {
                                                arrayList_id.add(list.get(i).getId());

                                                names = names + list.get(i).getName().trim()+", ";
                                            }

                                        }
                                        if(names.length()>1) {
                                            names = names.substring(0, names.length() - 2);
                                        }
                                        edit_text.setText(names);
                                        adapter.notifyDataSetChanged();

                                        //Toast.makeText(context,"ArrayList"+ arrayList_id.toString(),Toast.LENGTH_SHORT).show();

                                    }
                                });


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
                        error.printStackTrace();
                        Toast.makeText(context,"VolleyError---"+error.getMessage(),Toast.LENGTH_SHORT).show();
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