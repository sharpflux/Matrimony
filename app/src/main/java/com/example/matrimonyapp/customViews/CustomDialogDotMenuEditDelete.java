package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.adapter.AddPersonAdapter;
import com.example.matrimonyapp.modal.AddPersonModel;
import com.example.matrimonyapp.sqlite.SQLiteEducationDetails;
import com.example.matrimonyapp.sqlite.SQLiteFarmDetails;
import com.example.matrimonyapp.sqlite.SQLiteLanguageKnownDetails;
import com.example.matrimonyapp.sqlite.SQLiteMamaDetails;
import com.example.matrimonyapp.sqlite.SQLitePropertyDetails;
import com.example.matrimonyapp.sqlite.SQLiteSiblingDetails;
import com.example.matrimonyapp.sqlite.SQLiteVehicleDetails;

import java.util.ArrayList;

import jp.wasabeef.blurry.internal.Blur;
import jp.wasabeef.blurry.internal.BlurFactor;

public class CustomDialogDotMenuEditDelete extends Dialog {

    public Context context;
    String id, details_id;
    public TextView textView_edit, textView_delete;
    private AddPersonAdapter addPersonAdapter;
    private ArrayList<AddPersonModel> addPersonModelArrayList;
    private int position;
    String relation ;

    public CustomDialogDotMenuEditDelete(Context context, String id, String details_id, AddPersonAdapter addPersonAdapter,
                                         ArrayList<AddPersonModel> addPersonModelArrayList, int position, String relation) {
        super(context);
        this.context = context;
        this.id = id;
        this.details_id = details_id;
        this.addPersonAdapter = addPersonAdapter;
        this.addPersonModelArrayList = addPersonModelArrayList;
        this.position = position;
        this.relation = relation;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //blurBackground();

        setContentView(R.layout.custom_dialog_dot_menu_edit_delete);

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        textView_edit= findViewById(R.id.textView_edit);
        textView_delete = findViewById(R.id.textView_delete);

        textView_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(relation.equalsIgnoreCase("Mama"))
                {
                    CustomDialogAddMama customDialogAddMama = new CustomDialogAddMama(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogAddMama.show();

                }
                else if(relation.equalsIgnoreCase("Sibling"))
                {
                    CustomDialogAddSibling customDialogAddSibling = new CustomDialogAddSibling(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogAddSibling.show();

                }
                else if(relation.equalsIgnoreCase("Property"))
                {
                    CustomDialogAddProperty customDialogAddProperty = new CustomDialogAddProperty(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogAddProperty.show();
                }
                else if(relation.equalsIgnoreCase("Farm"))
                {
                    CustomDialogAddFarm customDialogAddFarm = new CustomDialogAddFarm(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogAddFarm.show();
                }
                else if(relation.equalsIgnoreCase("Vehicle"))
                {
                    CustomDialogAddVehicle customDialogAddVehicle = new CustomDialogAddVehicle(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogAddVehicle.show();
                }
                else if(relation.equalsIgnoreCase("Education"))
                {
                    CustomDialogAddEducation customDialogAddEducation= new CustomDialogAddEducation(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position,null);
                    customDialogAddEducation.show();
                }
                else if(relation.equalsIgnoreCase("Language"))
                {
                    CustomDialogAddLanguageKnown customDialogAddLanguageKnown= new CustomDialogAddLanguageKnown(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogAddLanguageKnown.show();
                }
                else if(relation.equalsIgnoreCase("ViewLanguage"))
                {
                    /*CustomDialogViewLanguageKnown customDialogviewLanguageKnown= new CustomDialogViewLanguageKnown(context, id, details_id, addPersonAdapter, addPersonModelArrayList, position);
                    customDialogviewLanguageKnown.show();*/
                }

                dismiss();
                //Intent intent = new Intent(context, LoginActivity.class);
                //context.startActivity(intent);
            }
        });

        textView_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(relation.equalsIgnoreCase("Mama"))
                {
                    SQLiteMamaDetails sqLiteMamaDetails = new SQLiteMamaDetails(context);
                    int deletedCount = sqLiteMamaDetails.deleteMamaDetails(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
                else if(relation.equalsIgnoreCase("Sibling")) {

                    SQLiteSiblingDetails sqLiteSiblingDetails = new SQLiteSiblingDetails(context);
                    int deletedCount = sqLiteSiblingDetails.deleteSibling(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
                else if(relation.equalsIgnoreCase("Property")) {

                    SQLitePropertyDetails sqLitePropertyDetails = new SQLitePropertyDetails(context);
                    int deletedCount = sqLitePropertyDetails.deletePropertyDetails(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
                else if(relation.equalsIgnoreCase("Farm")) {

                    SQLiteFarmDetails sqLiteFarmDetails = new SQLiteFarmDetails(context);
                    int deletedCount = sqLiteFarmDetails.deleteFarmDetails(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
                else if(relation.equalsIgnoreCase("Vehicle")) {

                    SQLiteVehicleDetails sqLiteVehicleDetails = new SQLiteVehicleDetails(context);
                    int deletedCount = sqLiteVehicleDetails.deleteVehicleDetails(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
                else if(relation.equalsIgnoreCase("Language")) {

                    SQLiteLanguageKnownDetails sqLiteLanguageKnownDetails= new SQLiteLanguageKnownDetails(context);
                    int deletedCount = sqLiteLanguageKnownDetails.deleteLanguageKnownDetails(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }
                else if(relation.equalsIgnoreCase("Education")) {

                    SQLiteEducationDetails sqLiteEducationDetails= new SQLiteEducationDetails(context);
                    int deletedCount = sqLiteEducationDetails.deleteEducationDetails(Integer.parseInt(id));
                    if (deletedCount > 0) {

                        addPersonModelArrayList.remove(position);
                        addPersonAdapter.notifyDataSetChanged();

                        Toast.makeText(context, deletedCount + " values deleted ", Toast.LENGTH_SHORT).show();
                        dismiss();
                    }

                }

            }
        });

    }

    private void blurBackground()
    {
/*
        //take Screenshot
        Bitmap bitmap = Screenshot.getInstance().takeScreenshotForScreen((Activity)context);

        //set blurring factor and heighth width of screenshot
        BlurFactor blurFactor = new BlurFactor();
        blurFactor.height = bitmap.getHeight();
        blurFactor.width = bitmap.getWidth();
        blurFactor.color = context.getResources().getColor(R.color.transparent_bg);

        //blurred image
        Bitmap blurBitmap = Blur.of(context, bitmap, blurFactor);
        //convert blurred image into drawable
        Drawable drawable = new BitmapDrawable(context.getResources(), blurBitmap);

        //set blurred screenshot to background
        getWindow().setBackgroundDrawable(drawable);*/

    }

}
