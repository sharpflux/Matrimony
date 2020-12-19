package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.adapter.DataFetcher;
import com.example.matrimonyapp.adapter.PopupFetcher;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.validation.FieldValidation;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;

import java.util.ArrayList;
import java.util.Map;

public class CustomDialogProfilePic extends Dialog {



    public Context context;

    private EditText editText_farmArea, editText_crops;
    private TextView textView_addFarm;

    private RadioGroup radioGroup_type;

    private Map<String, Integer> list;

    private SQLiteFarmDetails sqLiteFarmDetails;
    private DataFetcher dataFetcher;

    private String id, farm_details_id;

    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;


    private UserModel userModel;

    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogProfilePic(Context context, String id, String farm_details_id,  AddPersonAdapter addPersonAdapter,
                               ArrayList<AddPersonModel> addPersonModelArrayList, int position)
    {
        super(context);
        this.context = context;
        this.id = id;
        this.farm_details_id = farm_details_id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);



        setContentView(R.layout.custom_dialog_profile_pic);

        if (!CustomSharedPreference.getInstance(context).isLoggedIn()) {
            context.startActivity(new Intent(context, LoginActivity.class));
        }

        userModel = CustomSharedPreference.getInstance(getContext()).getUser();

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

/*
        editText_farmArea = findViewById(R.id.editText_farmArea);
        radioGroup_type = findViewById(R.id.radioGroup_type);
        editText_crops = findViewById(R.id.editText_crops);
        textView_addFarm = findViewById(R.id.textView_addFarm);

        dataFetcher = new DataFetcher("Address",context);



        if(!id.equals("0"))
        {

            Cursor cursor = sqLiteFarmDetails.getDataById(Integer.parseInt(id));

            for (boolean hasItem = cursor.moveToFirst(); hasItem; hasItem = cursor.moveToNext())
            {

                editText_farmArea.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.AREA)));
                editText_crops.setText(cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.CROPS)));
                FieldValidation.setRadioButtonAccToValue(radioGroup_type,
                        cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE)));
                Toast.makeText(context, cursor.getString(cursor.getColumnIndex(SQLiteFarmDetails.TYPE))+"---",Toast.LENGTH_SHORT).show();


            }


            cursor.close();
        }*/

       /* textView_addFarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String area = editText_farmArea.getText().toString().trim();
                String crops = editText_crops.getText().toString().trim();
                String type = ((RadioButton)findViewById(radioGroup_type.getCheckedRadioButtonId())).getText().toString();
                String irrigation = ((RadioButton)findViewById(radioGroup_type.getCheckedRadioButtonId())).getText().toString();


                if(id.equals("0")) {
                    long res = sqLiteFarmDetails.insertFarmDetails("0", area, type, crops);

                    if (res != -1) {
                        Toast.makeText(context, "Value added & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.add(new AddPersonModel(String.valueOf(res),"0", area, crops));
                        addPersonAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Error in sqlite insertion", Toast.LENGTH_SHORT).show();

                    }
                }
                else
                {
                    int res = sqLiteFarmDetails.updateFarmDetails(id, farm_details_id,area, type, crops);
                    if (res != -1) {
                        Toast.makeText(context, "Value Updated & id is " + res, Toast.LENGTH_SHORT).show();
                        addPersonModelArrayList.set(position, new AddPersonModel(String.valueOf(id), farm_details_id, area, crops));
                        addPersonAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(context, "Error in sqlite updation", Toast.LENGTH_SHORT).show();

                    }


                }
                dismiss();

            }
        });
*/

        onClickListener();


    }


    private void onClickListener()
    {




    }




    private class AsyncTaskLoad extends AsyncTask<String, String, String> {

        private String resp;

        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()

            try {





            } catch (Exception e) {
                e.printStackTrace();
                resp = e.getMessage();
            }
            return resp;
        }


        @Override
        protected void onPostExecute(String result) {

        }


        @Override
        protected void onPreExecute() {

            customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(context);
            customDialogLoadingProgressBar.show();

        }


        @Override
        protected void onProgressUpdate(String... text) {

        }

    }


}
