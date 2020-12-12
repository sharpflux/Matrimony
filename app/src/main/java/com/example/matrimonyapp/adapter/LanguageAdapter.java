package com.example.matrimonyapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.HomeActivity;
import com.example.matrimonyapp.customViews.CustomDialogChangeLanguage;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import java.util.ArrayList;
import java.util.Locale;

//import afu.org.checkerframework.checker.nullness.qual.NonNull;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.SingleViewHolder>{

    private Context context;
    private ArrayList<String> arrayList_languages;
    private int checkedPosition = -1;
    private CustomDialogChangeLanguage customDialogChangeLanguage;
    private UserModel userModel;

    Locale myLocale;
   // String currentLanguage = "en";

    public LanguageAdapter(Context context, ArrayList<String> arrayList_languages, CustomDialogChangeLanguage customDialogChangeLanguage) {
        this.context = context;
        this.arrayList_languages = arrayList_languages;
        this.customDialogChangeLanguage = customDialogChangeLanguage;

        userModel = CustomSharedPreference.getInstance(context).getUser();


    }


    @NonNull
    @Override
    public SingleViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_view_language, viewGroup, false);
        return new SingleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SingleViewHolder holder, final int position) {

        holder.textView.setText(arrayList_languages.get(position));

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(position==0)
                {
                    setLocale("en");
                }
                else if(position==1)
                {
                    setLocale("mr");
                }
                else if(position==2)
                {
                    setLocale("hi");
                }

            }
        });




    }

    public void setLocale(String localeName) {
        //if (!localeName.equals(currentLanguage))
        {
            myLocale = new Locale(localeName);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = myLocale;

            res.updateConfiguration(conf, dm);

           /* SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
            finish();*/
/*            Intent refresh = new Intent(context, HomeActivity.class);
            refresh.putExtra(currentLang, localeName);
            startActivity(refresh);


            finish();*/
            ((Activity)context).recreate();
            userModel.setLanguage(localeName);

            CustomSharedPreference.getInstance(context).saveUser(userModel);
            customDialogChangeLanguage.dismiss();

        }/* else
            {
            Toast.makeText(context, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
*/    }

    @Override
    public int getItemCount() {
        return arrayList_languages.size();
    }


    class SingleViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;
        //private ImageView imageView;

        SingleViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView_language);
            //imageView = itemView.findViewById(R.id.imageView);
        }

    }
}