package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
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
import android.widget.Toast;

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
//import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


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

        try {
            if(myReceiver!=null)
             unregisterReceiver(myReceiver);

            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onStop();
    }

    private boolean isCheckUserOnlineSelf(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            mBound = true;
            chatService.GetAllMessages(userModel.getUserId(),toUserId,"1","500");
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


        myReceiver = new MyReceiver();



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



        context=this;

        imageView_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = editText_message.getText().toString().trim();

                if(isCheckUserOnlineSelf(ChatService.class)){
                    if(!message.equals(""))
                    {
                        if(!connectionId.equals("0")) {
                            chatService.Send(connectionId, toUserId, editText_message.getText().toString());
                            ChatModel chatModel = new ChatModel();
                            chatModel.setMessage(message);
                            chatModel.setSenderId(userModel.getUserId());
                            chatModel.setReceiverId(toUserId);
                            chatModelsList.add(0, chatModel);
                            chatAdapter.notifyItemInserted(0);
                            editText_message.setText("");
                        }
                        else {
                            Toast.makeText(SignalRMessagesActivity.this,"User is offline", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    connect();
                    if(!message.equals(""))
                    {
                        if(!connectionId.equals("0")) {
                            chatService.Send(connectionId, toUserId, editText_message.getText().toString());
                            ChatModel chatModel = new ChatModel();
                            chatModel.setMessage(message);
                            chatModel.setSenderId(userModel.getUserId());
                            chatModel.setReceiverId(toUserId);
                            chatModelsList.add(0, chatModel);
                            chatAdapter.notifyItemInserted(0);
                            editText_message.setText("");
                        }
                        else {
                            Toast.makeText(SignalRMessagesActivity.this,"User is offline", Toast.LENGTH_LONG).show();
                        }
                    }
                }



            }
        });


        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignalRMessagesActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });



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

        connect();

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
                    MediaPlayer mPlayer = MediaPlayer.create(SignalRMessagesActivity.this, R.raw.message_tone);
                    mPlayer.start();

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
    private void connect() {
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notifyAdapter");
        intentFilter.addAction("getallMessages");
        registerReceiver(myReceiver, intentFilter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(SignalRMessagesActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }



    private void hideSoftKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager)activity
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }


    @Override
    protected void onResume() {
        super.onResume();
        connect();
        if(!currentLanguage.equals(getResources().getConfiguration().locale.getLanguage())){
            currentLanguage = getResources().getConfiguration().locale.getLanguage();
            recreate();
        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        try {
            if(myReceiver!=null)
                unregisterReceiver(myReceiver);

            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
