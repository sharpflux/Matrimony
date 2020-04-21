package com.example.matrimonyapp.customViews;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.MultipleSelectionAdapter;
import com.example.matrimonyapp.adapter.PromptAdapter;

public class CustomDialogMultipleSelection extends Dialog implements View.OnClickListener {


    public Context context;
    public Dialog dialog;

    public CheckBox checkBox_selectAll;

    RecyclerView recyclerView;

    public RelativeLayout relativeLayout_progressBar;

    public ScrollView scrollView;

    private RecyclerView.LayoutManager mLayoutManager;
    MultipleSelectionAdapter adapter;


    public CustomDialogMultipleSelection(Context context, MultipleSelectionAdapter adapter) {
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

        setContentView(R.layout.custom_dialog_multiple_selection);
        setTitle("Search ... ");

        relativeLayout_progressBar = findViewById(R.id.relativeLayout_progressBar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_multipleSelection);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);

        setCanceledOnTouchOutside(true);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));




        TextView txt_cancel = findViewById(R.id.textView_search);
        checkBox_selectAll = findViewById(R.id.checkBox_selectAll);
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
