package com.example.matrimonyapp.activity;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.DirectMessagesAdapter;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DirectMessagesActivity extends AppCompatActivity {

    ImageView imageView_back;

    private String currentLanguage;

    ArrayList<DirectMessagesModel> directMessagesModelList;
    RecyclerView recyclerView_directMessage;
    DirectMessagesAdapter directMessagesAdapter;
    Context context;

    FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ArrayList<String> arrayList_chatUsers;

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

/*
        for(int i=0; i<15; i++)
        {
            DirectMessagesModel directMessagesModel = new DirectMessagesModel("#yourUserId"+(i+100),
                    "User Name", "last message", "8.00 pm","default");
            directMessagesModelList.add(directMessagesModel);

        }
*/
        directMessagesAdapter = new DirectMessagesAdapter(DirectMessagesActivity.this,directMessagesModelList, true);
        recyclerView_directMessage.setAdapter(directMessagesAdapter);
        recyclerView_directMessage.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView_directMessage.setLayoutManager(mLayoutManager);


        userActivityStatus("online");

        readAllUsers();
        //readChatUsers();


    }

    private void userActivityStatus(String activityStatus)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("activityStatus", activityStatus);
        databaseReference.updateChildren(hashMap);


    }

    private void readChatUsers()
    {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Chats");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList_chatUsers = new ArrayList<>();

                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    ChatModel chatModel = snapshot.getValue(ChatModel.class);

                    if(chatModel.getSenderId().equals(firebaseUser.getUid()))
                    {
                        arrayList_chatUsers.add(chatModel.getReceiverId());
                    }
                    if(chatModel.getReceiverId().equals(firebaseUser.getUid()))
                    {
                        arrayList_chatUsers.add(chatModel.getSenderId());
                    }

                    readChats();

                }


/*
                directMessagesAdapter = new DirectMessagesAdapter(DirectMessagesActivity.this,directMessagesModelList, true);
                recyclerView_directMessage.setAdapter(directMessagesAdapter);
                recyclerView_directMessage.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView_directMessage.setLayoutManager(mLayoutManager);
*/

                directMessagesAdapter.notifyDataSetChanged();

            }
        });

    }

    private void readChats() {

        directMessagesModelList.clear();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                directMessagesModelList.clear();


                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    DirectMessagesModel directMessagesModel = snapshot.getValue(DirectMessagesModel.class);

                    for(String id : arrayList_chatUsers)
                    {
                        if(directMessagesModel.getFirebaseUserId().equals(id))
                        {
                            if(directMessagesModelList.size()!=0)
                            {
                                for(DirectMessagesModel directMessagesModel1 : directMessagesModelList)
                                {
                                    if(!directMessagesModel.getFirebaseUserId().equals(directMessagesModel1.getFirebaseUserId()))
                                    {
                                        directMessagesModelList.add(directMessagesModel);
                                    }
                                }
                            }
                            else
                            {
                                directMessagesModelList.add(directMessagesModel);
                            }
                        }

                    }

                }



                directMessagesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void readAllUsers()
    {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                directMessagesModelList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    DirectMessagesModel directMessagesModel = snapshot.getValue(DirectMessagesModel.class);
                    assert directMessagesModel != null;
                    assert firebaseUser !=null;
                    if(!directMessagesModel.getFirebaseUserId().equals(firebaseUser.getUid()))
                    {
                        directMessagesModelList.add(directMessagesModel);
                    }

                }
/*                directMessagesAdapter = new DirectMessagesAdapter(DirectMessagesActivity.this,directMessagesModelList, true);
                recyclerView_directMessage.setAdapter(directMessagesAdapter);
                recyclerView_directMessage.setHasFixedSize(true);
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                recyclerView_directMessage.setLayoutManager(mLayoutManager);*/


                directMessagesAdapter.notifyDataSetChanged();

            }
        });

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

        userActivityStatus("offline");
    }
}
