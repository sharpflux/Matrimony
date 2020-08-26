package com.example.matrimonyapp.customViews;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.SetPreferencesActivity;

public class CustomDialogAddLocation extends Dialog {
    LinearLayout lr_city,lr_state;
    CustomDialogLocationRec customDialogLocationRec;
    public CustomDialogAddLocation(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.custom_dialog_add_location);
        lr_city = findViewById(R.id.lr_city);
        lr_state = findViewById(R.id.lr_state);

        lr_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* customDialogLocationRec = new CustomDialogLocationRec(getContext());
                customDialogLocationRec.show();*/

            }
        });


        lr_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*customDialogLocationRec = new CustomDialogLocationRec(getContext());
                customDialogLocationRec.show();*/
            }
        });
    }
}
