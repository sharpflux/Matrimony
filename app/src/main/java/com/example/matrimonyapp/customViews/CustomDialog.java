package com.example.matrimonyapp.customViews;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.PromptAdapter;

//import jp.wasabeef.blurry.internal.Blur;
//import jp.wasabeef.blurry.internal.BlurFactor;

public class CustomDialog extends Dialog implements View.OnClickListener {

/*
    public CustomRecyclerViewDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public CustomRecyclerViewDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }*/

    public Context context;
    public Dialog dialog;

    RecyclerView recyclerView;

    public RelativeLayout relativeLayout_progressBar;

    public ScrollView scrollView;

    private RecyclerView.LayoutManager mLayoutManager;
    PromptAdapter adapter;


    public CustomDialog(Context context, PromptAdapter adapter) {
        super(context);
        this.context = context;
        this.adapter = adapter;
        setupLayout();
    }

    private void setupLayout() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));



        setContentView(R.layout.prompts);



        setTitle("Search ... ");

        relativeLayout_progressBar = findViewById(R.id.relativeLayout_progressBar);
        scrollView = findViewById(R.id.scrollView);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_prompt);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);



        //TextView txt_ok = findViewById(R.id.txt_ok);
        TextView txt_cancel = findViewById(R.id.txt_cancel);
        // final SearchView searchView_search = dialog.findViewById(R.id.searchView_search);
        final EditText editText_search = findViewById(R.id.editText_search);



        editText_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String string = charSequence.toString().trim();
                adapter.getFilter().filter(charSequence);
                //Toast.makeText(getContext(),charSequence.length()+"--"+charSequence.toString(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });



/*      SearchView searchView_search = findViewById(R.id.searchView_search);
        searchView_search.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                adapter.getFilter().filter(s);
                return false;
            }
        });*/


        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel();
            }
        });




        recyclerView.setAdapter(adapter);



    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_cancel:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }



}
