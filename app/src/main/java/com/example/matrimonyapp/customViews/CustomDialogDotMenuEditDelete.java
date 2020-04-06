package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;

import java.util.ArrayList;

public class CustomDialogDotMenuEditDelete extends Dialog {

    public Context context;
    String id;
    public TextView textView_edit, textView_delete;
    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;

    public CustomDialogDotMenuEditDelete(Context context, String id, AddPersonAdapter addPersonAdapter, ArrayList<AddPersonModel> addPersonModelArrayList, int position) {
        super(context);
        this.context = context;
        this.id = id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.custom_dialog_dot_menu_edit_delete);

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textView_edit= findViewById(R.id.textView_edit);
        textView_delete = findViewById(R.id.textView_delete);

        textView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CustomDialogAddSibling customDialogAddSibling = new CustomDialogAddSibling(context, id, addPersonAdapter, addPersonModelArrayList, position);
                customDialogAddSibling.show();

                dismiss();
                //Intent intent = new Intent(context, LoginActivity.class);
                //context.startActivity(intent);
            }
        });

        textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SQLiteSiblingDetails sqLiteSiblingDetails = new SQLiteSiblingDetails(context);
                int deletedCount = sqLiteSiblingDetails.deleteSibling(Integer.parseInt(id));
                if(deletedCount>0)
                {

                    addPersonModelArrayList.remove(position);
                    addPersonAdapter.notifyDataSetChanged();

                    Toast.makeText(context, deletedCount+" values deleted ", Toast.LENGTH_SHORT).show();
                    dismiss();
                }

            }
        });

    }

}
