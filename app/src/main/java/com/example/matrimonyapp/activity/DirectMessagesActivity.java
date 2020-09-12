package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.DirectMessagesAdapter;
import com.example.matrimonyapp.modal.DirectMessagesModel;

import java.util.ArrayList;

public class DirectMessagesActivity extends AppCompatActivity {

    ImageView imageView_back;

    private String currentLanguage;

    ArrayList<DirectMessagesModel> directMessagesModelList;
    RecyclerView recyclerView_directMessage;
    DirectMessagesAdapter directMessagesAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_messages);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        imageView_back = findViewById(R.id.imageView_back);


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        recyclerView_directMessage = findViewById(R.id.recyclerView_directMessages);

        context = getApplicationContext();
        directMessagesModelList = new ArrayList<DirectMessagesModel>();


        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+this.getResources().getResourcePackageName(R.drawable.flower2)
                +"/"+this.getResources().getResourceTypeName(R.drawable.flower2)
                +"/"+this.getResources().getResourceEntryName(R.drawable.flower2));

        for(int i=0; i<15; i++)
        {
            DirectMessagesModel directMessagesModel = new DirectMessagesModel("#yourUserId"+(i+100),
                    "User Name", "last message", "8.00 pm",uri);
            directMessagesModelList.add(directMessagesModel);

        }


        directMessagesAdapter = new DirectMessagesAdapter(this,directMessagesModelList);
        recyclerView_directMessage.setAdapter(directMessagesAdapter);
        recyclerView_directMessage.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_directMessage.setLayoutManager(mLayoutManager);





    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

}
