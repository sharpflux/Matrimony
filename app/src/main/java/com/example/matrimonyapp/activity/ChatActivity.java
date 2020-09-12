package com.example.matrimonyapp.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ChatAdapter;
import com.example.matrimonyapp.modal.ChatModel;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    LinearLayout linearLayout_message;
    ImageView imageView_back, imageView_sendMessage;
    EditText editText_message;
    String message;
    private String currentLanguage;
    ArrayList<ChatModel> chatModelsList;
    RecyclerView recyclerView_chat;
    ChatAdapter chatAdapter;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        linearLayout_message = findViewById(R.id.linearLayout_message);
        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        editText_message = findViewById(R.id.editText_message);
        imageView_back = findViewById(R.id.imageView_back);
        imageView_sendMessage = findViewById(R.id.imageView_sendMessage);


        context = getApplicationContext();

        chatModelsList = new ArrayList<ChatModel>();

        chatAdapter = new ChatAdapter(this,chatModelsList);
        recyclerView_chat.setAdapter(chatAdapter);
        recyclerView_chat.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_chat.setLayoutManager(mLayoutManager);
        recyclerView_chat.scrollToPosition(chatModelsList.size());


        imageView_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = editText_message.getText().toString();

                if(!message.equals(""))
                {
                    /*View relativeLayout_textMessage = findViewById(R.id.relativeLayout_textMessage);
                    linearLayout_message.addView(relativeLayout_textMessage);*/

                    chatModelsList.add(new ChatModel(message,"8.00 pm"));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView_chat.scrollToPosition(chatModelsList.size());
                    editText_message.setText("");
                }

            }
        });

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });




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
