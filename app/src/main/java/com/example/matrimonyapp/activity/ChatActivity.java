package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ChatAdapter;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.volley.URLs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    LinearLayout linearLayout_message;
    TextView textView_userName;
    ImageView imageView_back, imageView_sendMessage, imageView_profilePic;
    EditText editText_message;
    String message;
    private String currentLanguage;
    ArrayList<ChatModel> chatModelsList;
    RecyclerView recyclerView_chat;
    ChatAdapter chatAdapter;
    Context context;
    private FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    private Intent intent;

    private ScrollView scrollView;
    private ValueEventListener seenListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        intent = getIntent();

        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        scrollView = findViewById(R.id.scrollView);
        linearLayout_message = findViewById(R.id.linearLayout_message);
        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        editText_message = findViewById(R.id.editText_message);
        textView_userName = findViewById(R.id.textView_userName);
        imageView_back = findViewById(R.id.imageView_back);
        imageView_profilePic = findViewById(R.id.imageView_profilePic);
        imageView_sendMessage = findViewById(R.id.imageView_sendMessage);

        firebaseInit();


        context = getApplicationContext();

        chatModelsList = new ArrayList<ChatModel>();

        chatAdapter = new ChatAdapter(this,chatModelsList);
        recyclerView_chat.setAdapter(chatAdapter);
        recyclerView_chat.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_chat.setLayoutManager(mLayoutManager);
        recyclerView_chat.scrollToPosition(chatModelsList.size());




        context=this;

        imageView_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = editText_message.getText().toString().trim();

                if(!message.equals(""))
                {
                    /*View relativeLayout_textMessage = findViewById(R.id.relativeLayout_textMessage);
                    linearLayout_message.addView(relativeLayout_textMessage);*/
                    sendMessage();
                    /*chatModelsList.add(new ChatModel(message,"8.00 pm"));
                    chatAdapter.notifyDataSetChanged();
                    recyclerView_chat.scrollToPosition(chatModelsList.size());
                    editText_message.setText("");*/

                    hideSoftKeyboard((Activity)ChatActivity.this);
                    recyclerView_chat.smoothScrollToPosition(chatModelsList.size()-1);

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

    private void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }

    private void readMessage(final String myFirebaseId, final String friendFirebaseId)
    {


        chatModelsList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatModelsList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if(chatModel.getReceiverId().equals(myFirebaseId) && chatModel.getSenderId().equals(friendFirebaseId)
                        || chatModel.getReceiverId().equals(friendFirebaseId) && chatModel.getSenderId().equals(myFirebaseId))
                    {
                        chatModelsList.add(chatModel);
                    }

                }

                chatAdapter = new ChatAdapter(ChatActivity.this, chatModelsList);
                recyclerView_chat.setAdapter(chatAdapter);
                recyclerView_chat.setHasFixedSize(true);

                scrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // recyclerView_chat.smoothScrollToPosition(recyclerView_chat.getAdapter().getItemCount());
                        scrollView.smoothScrollTo(0,scrollView.getHeight());
                        recyclerView_chat.smoothScrollToPosition(recyclerView_chat.getAdapter().getItemCount()-1);

                    }
                }, 1000);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void messageStatus(final String userId)
    {

        databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        seenListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);
                    if(chatModel.getReceiverId().equals(firebaseUser.getUid())
                            && chatModel.getSenderId().equals(userId))
                    {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("messageStatus", "seen");
                        snapshot.getRef().updateChildren(hashMap);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userActivityStatus(String activityStatus)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("activityStatus", activityStatus);
        databaseReference.updateChildren(hashMap);


    }




    private void sendMessage() {


        final String friendfirebaseUserId = intent.getStringExtra("firebaseUserId");
        String myfirebaseUserId = firebaseUser.getUid();

        String message = editText_message.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("message", message);
        hashMap.put("senderId", myfirebaseUserId);
        hashMap.put("receiverId", friendfirebaseUserId);
        hashMap.put("messageTime", "8:00am");
        hashMap.put("messageStatus", "delivered");

        reference.child("Chats").push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });


        final DatabaseReference databaseReference_chatSender = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(firebaseUser.getUid())
                .child(friendfirebaseUserId);

        databaseReference_chatSender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    databaseReference_chatSender.child("id").setValue(friendfirebaseUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        final DatabaseReference databaseReference_chatReceiver = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(friendfirebaseUserId)
                .child(firebaseUser.getUid());

        databaseReference_chatReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists())
                {
                    databaseReference_chatReceiver.child("id").setValue(firebaseUser.getUid());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void firebaseInit() {

        final String friendfirebaseUserId = intent.getStringExtra("firebaseUserId");

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(friendfirebaseUserId);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DirectMessagesModel directMessagesModel = snapshot.getValue(DirectMessagesModel.class);

                textView_userName.setText(directMessagesModel.getUserName());

                Glide.with(context)
                        .load(directMessagesModel.getProfilePic())
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .placeholder(R.color.codeGray)
                        .into(imageView_profilePic);

                readMessage(firebaseUser.getUid(), friendfirebaseUserId);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        messageStatus(friendfirebaseUserId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        userActivityStatus("online");

        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        databaseReference.removeEventListener(seenListener);
        userActivityStatus("offline");
    }



}
