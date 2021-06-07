package com.example.matrimonyapp.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.DirectMessagesAdapter;
import com.example.matrimonyapp.adapter.HorizontalImageAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatListModel;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    ArrayList<DirectMessagesModel> directMessagesModelList, onlineUsersModelList;
    RecyclerView recyclerView_directMessage;
    DirectMessagesAdapter directMessagesAdapter, onlineUsersAdapter;
    Context context;
    Handler mHandler=new Handler();
    ArrayList<ChatListModel> arrayList_chatListModel ;

    private ArrayList<String> arrayList_chatUsers;

    public static String FromConnectionId="0";
    MyReceiver myReceiver;

    // Chat Service
    ChatService chatService;
    boolean mBound = false;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
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


    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            // chatService.GetRooms();
            chatService.GetRecentChats(userModel.getUserId(),"1","50");
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

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(SignalRUserChatsActivity.this);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userModel = CustomSharedPreference.getInstance(this).getUser();

        //recyclerView_onlineUsers = findViewById(R.id.recyclerView_onlineUsers);
        recyclerView_directMessage = findViewById(R.id.recyclerView_directMessages);

        context = getApplicationContext();
        directMessagesModelList = new ArrayList<DirectMessagesModel>();
        onlineUsersModelList = new ArrayList<DirectMessagesModel>();

        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        //Register events we want to receive from Chat Service
        myReceiver = new  MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("UserList");
        intentFilter.addAction("MyStatus");
        intentFilter.addAction("OnlineUsers");
        intentFilter.addAction("offlineUser");
        registerReceiver(myReceiver, intentFilter);

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("GetOnlineUsers");


     //   readOnlineUsers();

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


    private void GetOnlineUsers() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_ONLINEUSERSGETRECENTCHAT+userModel.getUserId(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                                customDialogLoadingProgressBar.dismiss();

                            JSONArray jsonArray = new JSONArray(response);
                            directMessagesModelList = new ArrayList<DirectMessagesModel>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String username = jsonObject.getString("FullName");
                                String connection_id = jsonObject.getString("ConnectionId");
                                DirectMessagesModel directMessagesModel = new DirectMessagesModel();
                                directMessagesModel.setUserId(jsonObject.getString("ToUserId"));
                                directMessagesModel.setProfilePic(jsonObject.getString("ProfileImage"));
                                directMessagesModel.setOnline(jsonObject.getBoolean("IsOnline"));
                                directMessagesModel.setFirebaseUserId(connection_id);
                                directMessagesModel.setFromUserId(String.valueOf(jsonObject.getInt("UserId")));
                                directMessagesModel.setUserName(username);
                                directMessagesModelList.add(directMessagesModel);

                            }

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignalRUserChatsActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("UserId :",userModel.getUserId());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SignalRUserChatsActivity.this).addToRequestQueue(stringRequest);
    }



    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {


            if(params[0].equals("GetOnlineUsers"))
            {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        GetOnlineUsers();
                    }
                });

            }

            return null;
        }


        @Override
        protected void onPreExecute() {

            if (!isFinishing() && customDialogLoadingProgressBar != null) {
                customDialogLoadingProgressBar.setCancelable(false);
                customDialogLoadingProgressBar.show();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(String s) {
            super.onCancelled(s);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "notifyAdapter":
                    //adapter.notifyDataSetChanged();
                    break;

                case "OnlineUsers":
                    JSONArray jsonArrayOnline =(JSONArray) Globals.onlineUserlist;

                    for (int i = 0; i < jsonArrayOnline.length(); i++) {
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = jsonArrayOnline.getJSONObject(i);
                            for (int d=0; d<directMessagesModelList.size(); d++)
                            {
                                DirectMessagesModel dm = directMessagesModelList.get(d);
                                if (dm.getUserId().equals(jsonObject.getString("FromUserId")))
                                {
                                    dm.setFirebaseUserId(jsonObject.getString("connectionID"));
                                    dm.setOnline(true);
                                }

                                directMessagesModelList.set(d,dm);
                                directMessagesAdapter.notifyItemChanged(d);
                            }

                        } catch ( JSONException e) {
                            e.printStackTrace();
                        }
                        catch ( Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case "offlineUser":
                    JSONObject jsonObject =(JSONObject) Globals.offlineUser;

                        try {
                            for (int d=0; d<directMessagesModelList.size(); d++)
                            {
                                DirectMessagesModel dm = directMessagesModelList.get(d);
                                if (dm.getUserId().equals(jsonObject.getString("FromUserId")))
                                {
                                    dm.setFirebaseUserId("0");
                                    dm.setOnline(false);

                                }
                                directMessagesModelList.set(d,dm);
                                directMessagesAdapter.notifyItemChanged(d);
                            }

                        } catch ( JSONException e) {
                            e.printStackTrace();
                        }
                        catch ( Exception e) {
                            e.printStackTrace();
                        }


                    break;

                case "MyStatus":
                    for (int i=0; i<directMessagesModelList.size(); i++)
                    {
                        DirectMessagesModel dm = directMessagesModelList.get(i);
                        if (dm.getUserId().equals("4") && Globals.status.equals("true"))
                        {
                            dm.setActivityStatus("true");

                            directMessagesModelList.set(i,dm);
                            directMessagesAdapter.notifyItemChanged(i);
                        }
                        else {
                            dm.setActivityStatus("false");

                            directMessagesModelList.set(i,dm);
                            directMessagesAdapter.notifyItemChanged(i);
                        }

                    }

                    break;
            }
        }
    } // MyReceiver
}
