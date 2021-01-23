package com.example.matrimonyapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
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
    ImageView imageView_back, imageView_sendMessage, imageView_profilePic;
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

    String connectionId, toUserId;

    Handler mHandler=new Handler(); //listener
    private ScrollView scrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_r_messages);

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

        userModel = CustomSharedPreference.getInstance(this).getUser();

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

                    sendMessage();
                    hideSoftKeyboard((Activity)SignalRMessagesActivity.this);
//                    recyclerView_chat.smoothScrollToPosition(chatModelsList.size()-1);

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

        Bundle bundle =intent.getExtras();
        if (bundle!=null)
        {
            connectionId = bundle.getString("connectionId");
            toUserId = bundle.getString("toUserId");
        }
        connect();

    }

    private void sendMessage() {

        // WebApi Method

        hubProxy.invoke("sendMessage", new Object[]{editText_message.getText().toString().trim(), toUserId,
            connectionId});//we have parameterized what we want in the web API method


    }


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

        hubProxy = SignalRUserChatsActivity.hubProxy;


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
                       chatModelsList.add(chatModel);
                       chatAdapter.notifyDataSetChanged();
                    }
                });
            }
        },String.class,String.class);


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
