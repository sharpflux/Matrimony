package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.LanguageAdapter;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import java.util.ArrayList;
import java.util.Locale;

public class SelectLanguageActivity extends AppCompatActivity {

    private String currentLanguage;
    private TextView textView_toolbar, textView_changeLanguage;
    private ArrayList<String> arrayList_languages;
    private RecyclerView recyclerView_language;
    private LanguageAdapter languageAdapter;
    private UserModel userModel;
    public Locale locale;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);


        init();

        textView_toolbar.setText(getResources().getString(R.string.language_settings));

        onClickListener();


    }

    private void onClickListener() {

        textView_changeLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setLocale(LanguageAdapter.localeName);

                Intent intent = new Intent(SelectLanguageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init() {

        userModel = CustomSharedPreference.getInstance(SelectLanguageActivity.this).getUser();
        currentLanguage = getResources().getConfiguration().locale.getLanguage();

        textView_toolbar = findViewById(R.id.textView_toolbar);
        textView_changeLanguage = findViewById(R.id.textView_changeLanguage);
        recyclerView_language = findViewById(R.id.recyclerView_language);

        arrayList_languages = new ArrayList<>();
        arrayList_languages.add(getResources().getString(R.string.english));
        arrayList_languages.add(getResources().getString(R.string.marathi));
        arrayList_languages.add(getResources().getString(R.string.hindi));

        recyclerView_language = findViewById(R.id.recyclerView_language);
        languageAdapter = new LanguageAdapter(SelectLanguageActivity.this, arrayList_languages);

        recyclerView_language.setAdapter(languageAdapter);
        recyclerView_language.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(SelectLanguageActivity.this);
        recyclerView_language.setLayoutManager(linearLayoutManager);


    }


    public void setLocale(String localeName) {
        if (!localeName.equals(currentLanguage))
        {
            locale = new Locale(localeName);
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = locale;

            res.updateConfiguration(conf, dm);


            recreate();
            userModel.setLanguage(localeName);

            CustomSharedPreference.getInstance(SelectLanguageActivity.this).saveUser(userModel);


        }/* else
            {
            Toast.makeText(context, "Language already selected!", Toast.LENGTH_SHORT).show();
        }
*/    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }


    }
}