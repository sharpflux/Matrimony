package com.example.matrimonyapp.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.DirectMessagesAdapter;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatListModel;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.volley.CustomSharedPreference;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class SignalRUserChatsActivity extends AppCompatActivity {

    ImageView imageView_back;

    private String currentLanguage;
    private UserModel userModel;
    public static HubProxy hubProxy;
    public HubConnection hubConnection;
    ArrayList<DirectMessagesModel> directMessagesModelList;
    RecyclerView recyclerView_directMessage;
    DirectMessagesAdapter directMessagesAdapter;
    Context context;
    Handler mHandler=new Handler();
    ArrayList<ChatListModel> arrayList_chatListModel ;

    private ArrayList<String> arrayList_chatUsers;

    MyReceiver myReceiver;

    // Chat Service
    ChatService chatService;
    boolean mBound = false;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            // chatService.GetRooms();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_r_user_chats);

        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        imageView_back = findViewById(R.id.imageView_back);


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userModel = CustomSharedPreference.getInstance(this).getUser();

        recyclerView_directMessage = findViewById(R.id.recyclerView_directMessages);

        context = getApplicationContext();
        directMessagesModelList = new ArrayList<DirectMessagesModel>();

        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //Register events we want to receive from Chat Service
        myReceiver = new  MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UserList");
        registerReceiver(myReceiver, intentFilter);


     //   readOnlineUsers();

    }



    private void readOnlineUsers() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        Credentials credentials = new Credentials() {
            @Override
            public void prepareRequest(microsoft.aspnet.signalr.client.http.Request request) {
                request.addHeader("DisplayName", userModel.getFullName());
                request.addHeader("FromUserId", userModel.getUserId());
                request.addHeader("ProfilePic", userModel.getProfilePic());
            }
        };
        // connect to signalr server
        hubConnection = new HubConnection(URLs.URL_CONNECT_SIGNALR);
        hubConnection.setCredentials(credentials);
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
            }
        });


        String CLIENT_METHOD_BROADAST_MESSAGE = "getUserList"; // get webapi serv methods
        hubProxy = hubConnection.createHubProxy("chatHub"); // web api  necessary method name
        ClientTransport clientTransport = new ServerSentEventsTransport((hubConnection.getLogger()));
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);
        hubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE, new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try { // we added the list of connected users
                    JSONArray jsonArray = new JSONArray(s);
                    directMessagesModelList = new ArrayList<DirectMessagesModel>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("DisplayName");
                        String connection_id = jsonObject.getString("connectionID");
                        DirectMessagesModel directMessagesModel = new DirectMessagesModel();
                        directMessagesModel.setUserId(jsonObject.getString("FromUserId"));
                        directMessagesModel.setProfilePic(jsonObject.getString("ProfilePic"));
                        directMessagesModel.setUserName(username);
                        directMessagesModel.setFirebaseUserId(connection_id);
                        directMessagesModelList.add(directMessagesModel);

                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                    //        ArrayAdapter<String> adapter=new ArrayAdapter<String>(cx,android.R.layout.simple_list_item_1,user_names);
                      //      adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                        //    users.setAdapter(adapter);
                            directMessagesAdapter = new DirectMessagesAdapter(SignalRUserChatsActivity.this,directMessagesModelList, true);
                            recyclerView_directMessage.setAdapter(directMessagesAdapter);
                            recyclerView_directMessage.setHasFixedSize(true);
                            LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                            recyclerView_directMessage.setLayoutManager(mLayoutManager);


                            directMessagesAdapter.notifyDataSetChanged();

                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, String.class);

        hubProxy.on("sendMessage", new SubscriptionHandler2<String ,String>() {

            @Override
            public void run(final String s, final String s2) {
                /*mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                       // send_message.setText(send_message.getText()+"\n"+s2+" : "+s);
                    }
                });*/
            }
        },String.class,String.class);
        try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("SimpleSignalR", e.toString());
            return;
        }

    }


    @Override
    protected void onResume() {
        super.onResume();


        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "notifyAdapter":
                    //adapter.notifyDataSetChanged();
                    break;
                case "UserList":
                    JSONArray jsonArray =(JSONArray) Globals.userlist;
                    try {
                        directMessagesModelList.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;

                            jsonObject = jsonArray.getJSONObject(i);
                            String username = jsonObject.getString("DisplayName");
                            String connection_id = jsonObject.getString("connectionID");
                            DirectMessagesModel directMessagesModel = new DirectMessagesModel();
                            directMessagesModel.setUserId(jsonObject.getString("FromUserId"));
                            directMessagesModel.setProfilePic(jsonObject.getString("ProfilePic"));
                            directMessagesModel.setUserName(username);
                            directMessagesModel.setFirebaseUserId(connection_id);
                            directMessagesModelList.add(directMessagesModel);



                    }
                        directMessagesAdapter = new DirectMessagesAdapter(SignalRUserChatsActivity.this,directMessagesModelList, true);
                        recyclerView_directMessage.setAdapter(directMessagesAdapter);
                        recyclerView_directMessage.setHasFixedSize(true);
                        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
                        recyclerView_directMessage.setLayoutManager(mLayoutManager);
                        directMessagesAdapter.notifyDataSetChanged();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    } // MyReceiver
}
