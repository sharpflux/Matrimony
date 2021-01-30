package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
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
import com.example.matrimonyapp.adapter.DirectMessagesAdapter;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.fasterxml.jackson.annotation.JsonInclude;
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

import de.hdodenhof.circleimageview.CircleImageView;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler3;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class SignalRMessagesActivity extends AppCompatActivity {

    LinearLayout linearLayout_message;
    TextView textView_userName;
    ImageView imageView_back, imageView_sendMessage;
    CircleImageView circleImageView_profilePic;
    EditText editText_message;
    String message;
    private String currentLanguage;
    ArrayList<ChatModel> chatModelsList;
    RecyclerView recyclerView_chat;
    ChatAdapter chatAdapter;
    Context context;
    UserModel userModel;
    HubConnection hubConnection; //Do the signalR definitions
    HubProxy hubProxy;
    private Intent intent;

    LinearLayout linearLayout_toolbar;

    String connectionId="0", toUserId="";

    Handler mHandler=new Handler(); //listener
    private ScrollView scrollView;

    MyReceiver myReceiver;
    ChatService chatService;
    boolean mBound = false;
    private LinearLayoutManager mLayoutManager;
    private boolean isAtBottom=false;
    private Bundle bundle;


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(myReceiver!=null)
         unregisterReceiver(myReceiver);

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
            mBound = true;
            chatService.GetAllMessages(userModel.getUserId(),toUserId,"1","50");

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_r_messages);

        intent = getIntent();

        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        scrollView = findViewById(R.id.scrollView);
        linearLayout_toolbar = findViewById(R.id.linearLayout_toolbar);
        linearLayout_message = findViewById(R.id.linearLayout_message);
        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        editText_message = findViewById(R.id.editText_message);
        textView_userName = findViewById(R.id.textView_userName);
        imageView_back = findViewById(R.id.imageView_back);
        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);
        imageView_sendMessage = findViewById(R.id.imageView_sendMessage);

        userModel = CustomSharedPreference.getInstance(this).getUser();

        context = getApplicationContext();

        chatModelsList = new ArrayList<ChatModel>();

        chatAdapter = new ChatAdapter(this,chatModelsList);
        recyclerView_chat.setAdapter(chatAdapter);
        recyclerView_chat.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(context);
        mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);
        recyclerView_chat.setLayoutManager(mLayoutManager);
        recyclerView_chat.scrollToPosition(chatModelsList.size());


        context=this;

        imageView_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = editText_message.getText().toString().trim();

                if(!message.equals(""))
                {


                    chatService.Send(connectionId,toUserId, editText_message.getText().toString());
                   // sendMessage();
                   // hideSoftKeyboard((Activity)SignalRMessagesActivity.this);


                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(message);
                    chatModel.setSenderId(userModel.getUserId());
                    chatModel.setReceiverId(toUserId);
                    chatModelsList.add(0,chatModel);
                    chatAdapter.notifyItemInserted(0);
                    editText_message.setText("");
                    //scrollView.scrollTo(0, scrollView.getBottom()+60);
                    //recyclerView_chat.smoothScrollToPosition(chatModelsList.size());

                }

            }
        });


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        bundle =intent.getExtras();
        if (bundle!=null)
        {
           connectionId = bundle.getString("connectionId");
            toUserId = bundle.getString("toUserId");
            textView_userName.setText(bundle.getString("toUserName"));

            Glide.with(context)
                    .load(URLs.MainURL+bundle.getString("toUserProfilePic"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .placeholder(R.color.quantum_grey100)
                    .into(circleImageView_profilePic);

        }


        linearLayout_toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (bundle!=null) {
                    Intent intent = new Intent(SignalRMessagesActivity.this, ViewProfileDetailsActivity.class);
                    intent.putExtra("userId", toUserId);
                    intent.putExtra("userName", bundle.getString("toUserName"));
                    intent.putExtra("userProfilePic", bundle.getString("toUserProfilePic"));
                    startActivity(intent);
                }
            }
        });

       // connect();
     //   hubProxy.invoke("GetMessage", new Object[]{userModel.getUserId(), toUserId, 1, 50});
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //Register events we want to receive from Chat Service
        myReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notifyAdapter");
        intentFilter.addAction("getallMessages");
       // intentFilter.addAction("UserList");
        registerReceiver(myReceiver, intentFilter);




        RecyclerView.AdapterDataObserver observer = new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);

                recyclerView_chat.smoothScrollToPosition(0);
                /*if (isAtBottom) {

                } else {
                }*/
            }
        };

        recyclerView_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    isAtBottom = true;
                } else {
                    isAtBottom = false;
                }
            }
        });

        //Assigns observer to adapter and LayoutManager to RecyclerView

        chatAdapter.registerAdapterDataObserver(observer);
       // recyclerView_chat.smoothScrollToPosition(0);

    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "notifyAdapter":

                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(Globals.NewMessage);
                    chatModel.setSenderId(toUserId);
                    chatModel.setReceiverId(userModel.getUserId());
                    chatModelsList.add(0,chatModel);
                    chatAdapter.notifyItemInserted(0);
                    //scrollView.scrollTo(0, scrollView.getBottom()+60);
                    //recyclerView_chat.smoothScrollToPosition(chatModelsList.size());

                    break;
                case "getallMessages":

                    chatModelsList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(Globals.allMessages);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                /*if ((jsonObject.getString("FromUserId").equals(userModel.getUserId()) && jsonObject.getString("ToUserId").equals(toUserId))
                                || (jsonObject.getString("FromUserId").equals(toUserId) && jsonObject.getString("ToUserId").equals(userModel.getUserId())))
                                {*/
                                    ChatModel chatModel1 = new ChatModel();
                                    chatModel1.setMessage(jsonObject.getString("Message"));
                                    chatModel1.setMessageTime(jsonObject.getString("MessageDateTime"));
                                    chatModel1.setSenderId(jsonObject.getString("FromUserId"));
                                    chatModel1.setReceiverId(jsonObject.getString("ToUserId"));
                                    chatModelsList.add(chatModel1);



                            //}

                        }


                        chatAdapter.notifyDataSetChanged();
                        recyclerView_chat.smoothScrollToPosition(chatModelsList.size());
                       // mLayoutManager.scrollToPositionWithOffset(0,100);
                       // scrollView.fullScroll(View.FOCUS_FORWARD);
                       // recyclerView_chat.smoothScrollToPosition(chatModelsList.size());
                        //scrollView.scrollTo(0, scrollView.getBottom()+60);

                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }


                    break;

                case "UserList":
                    JSONArray jsonArray1 =(JSONArray) Globals.userlist;
                    try {

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject = null;

                            jsonObject = jsonArray1.getJSONObject(i);
                            if (jsonObject.getString("FromUserId").equals(toUserId))
                            {
                                connectionId = jsonObject.getString("connectionID");
                                break;
                            }


                        }



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    break;
            }
        }
    } // MyReceiver

    void connect() {
/*        Platform.loadPlatformComponent(new AndroidPlatformComponent());
        Credentials credentials = new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("DisplayName", userModel.getFullName()); //get username
                request.addHeader("FromUserId", userModel.getUserId()); //get username
            }
        };
        String serverUrl="http://sam.sharpflux.com/signalr"; // connect to signalr server
        hubConnection = new HubConnection(serverUrl);
        hubConnection.setCredentials(credentials);
        hubConnection.connected(new Runnable() {
            @Override
            public void run() {
            }
        });

        String CLIENT_METHOD_BROADAST_MESSAGE = "getUserList"; // get webapi serv methods
        hubProxy = hubConnection.createHubProxy("chatHub"); // web api  necessary method name
        ClientTransport clientTransport = new ServerSentEventsTransport((hubConnection.getLogger()));
        SignalRFuture<Void> signalRFuture = hubConnection.start(clientTransport);*/

        //hubProxy = SignalRUserChatsActivity.hubProxy;


        /*hubProxy.on(CLIENT_METHOD_BROADAST_MESSAGE, new SubscriptionHandler1<String>() {
            @Override
            public void run(String s) {
                try { // we added the list of connected users
                    JSONArray jsonArray = new JSONArray(s);
                    userList = new ArrayList<UserChat>();
                    user_names = new ArrayList<String>();
                    user_names.add("Select User");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String username = jsonObject.getString("username");
                        String connection_id = jsonObject.getString("connectionID");
                        UserChat user = new UserChat(username, connection_id);
                        userList.add(user);
                        user_names.add(username);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(cx,android.R.layout.simple_list_item_1,user_names);
                            adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
                            users.setAdapter(adapter);
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, String.class);*/

        hubProxy.on("sendMessage", new SubscriptionHandler2<String, String>() {

            @Override
            public void run(final String s, final String s2) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                      //  send_message.setText(send_message.getText()+"\n"+s2+" : "+s);
                       ChatModel chatModel = new ChatModel();
                       chatModel.setMessage(s);
                       chatModel.setSenderId(toUserId);
                       chatModel.setReceiverId(userModel.getUserId());
                       chatModelsList.add(chatModel);
                       chatAdapter.notifyDataSetChanged();
                    }
                });
            }
        },String.class,String.class);


 hubProxy.on("getallMessages", new SubscriptionHandler1<String>() {

            @Override
            public void run(final String s) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            JSONArray jsonArray = new JSONArray(s);

                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                /*if ((jsonObject.getString("FromUserId").equals(userModel.getUserId()) && jsonObject.getString("ToUserId").equals(toUserId))
                                || (jsonObject.getString("FromUserId").equals(toUserId) && jsonObject.getString("ToUserId").equals(userModel.getUserId())))
                                {*/
                                    ChatModel chatModel = new ChatModel();
                                    chatModel.setMessage(jsonObject.getString("Message"));
                                    chatModel.setMessageTime(jsonObject.getString("MessageDateTime"));
                                    chatModel.setSenderId(jsonObject.getString("FromUserId"));
                                    chatModel.setReceiverId(jsonObject.getString("ToUserId"));
                                    chatModelsList.add(chatModel);

                                //}

                            }

                            chatAdapter.notifyDataSetChanged();

                        }
                        catch (JSONException jsonException)
                        {
                            jsonException.printStackTrace();
                        }


                        Log.e("MESSAGES", "\n------------------------\n"+s +"\n------------------------\n");

                    }
                });
            }
        },String.class);


        /*try {
            signalRFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            Log.e("SimpleSignalR", e.toString());
            return;
        }*/
    }


    private void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

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



}
