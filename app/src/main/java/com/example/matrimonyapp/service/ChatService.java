package com.example.matrimonyapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import androidx.annotation.Nullable;

import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.helpers.User;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.MessageViewModel;
import com.example.matrimonyapp.modal.RoomViewModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler2;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class ChatService extends Service {

    private final IBinder mBinder = new LocalBinder();

    private HubConnection connection;
    private HubProxy proxy;
    private Handler handler;

    private String serverUrl = "http://sam.sharpflux.com/signalr";
    private String hubName = "chatHub";
    UserModel userModel;
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        userModel= CustomSharedPreference.getInstance(getApplicationContext()).getUser();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // We are using binding. do we really need this...?
        if (!StartHubConnection()) {
            ExitWithMessage("Chat Service failed to start!");
        }
        if (!RegisterEvents()) {
            ExitWithMessage("End-point error: Failed to register Events!");
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
       connection.stop();
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (!StartHubConnection()) {
            //ExitWithMessage("Chat Service failed to start!");
            StartHubConnection();
        }

        if (!RegisterEvents()) {
            RegisterEvents();
            //ExitWithMessage("End-point error: Failed to register Events!");
        }

        return mBinder;
    }

    // https://developer.android.com/guide/components/bound-services.html
    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }

    private boolean StartHubConnection() {
        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        // Create Connection
        connection = new HubConnection(serverUrl);
        Credentials credentials = new Credentials() {
            @Override
            public void prepareRequest(Request request) {
                request.addHeader("DisplayName", userModel.getFullName());
                request.addHeader("FromUserId", userModel.getUserId());
                request.addHeader("ProfilePic", userModel.getProfilePic());

            }
        };

        connection.setCredentials(credentials);


        // Create Proxy
        proxy = connection.createHubProxy(hubName);

        // Establish Connection
        ClientTransport clientTransport = new ServerSentEventsTransport(connection.getLogger());
        SignalRFuture<Void> signalRFuture = connection.start(clientTransport);

        try {
            signalRFuture.get();
        } catch (InterruptedException e) {
            return false;
        } catch (ExecutionException e) {
            return false;
        }

        return true;
    }

    private boolean RegisterEvents() {

       final Handler mHandler = new Handler(Looper.getMainLooper());
        try {

            proxy.on("SendMessage", new SubscriptionHandler2<String ,String>() {

                @Override
                public void run(final String s, final String s2) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                           // send_message.setText(send_message.getText()+"\n"+s2+" : "+s);

                            Globals.NewMessage=s;
                            sendBroadcast(new Intent().setAction("notifyAdapter"));
                        }
                    });
                }
            },String.class,String.class);



            proxy.on("getUserList", new SubscriptionHandler1<String>() {
                @Override
                public void run(String s) {
                    try { // we added the list of connected users
                        JSONArray jsonArray = new JSONArray(s);

                        Globals.onlineUserlist=jsonArray;
                        sendBroadcast(new Intent().setAction("OnlineUsers"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, String.class);



            proxy.on("getallMessages", new SubscriptionHandler2<String, String>() {

                @Override
                public void run(final String s, String s2) {

                   // Log.e("MESSAGES", "\n--------------------------------------\n"+s +"\n--------------------------------------\n");

                    mHandler.post(new Runnable() {


                        @Override
                        public void run() {

                            Globals.allMessages = s;
                            sendBroadcast(new Intent().setAction("getallMessages"));


                        }
                    });
                }
            },String.class, String.class);


            proxy.on("iamOnline", new SubscriptionHandler2<String, String>() {

                @Override
                public void run(final String s,final String s2) {

                    // Log.e("MESSAGES", "\n--------------------------------------\n"+s +"\n--------------------------------------\n");

                    mHandler.post(new Runnable() {


                        @Override
                        public void run() {

                            Globals.UserId = s;
                            Globals.status = s2;
                            sendBroadcast(new Intent().setAction("MyStatus"));


                        }
                    });
                }
            },String.class, String.class);


            proxy.on("getRecentChatUsers", new SubscriptionHandler1<String>() {

                @Override
                public void run(final String s) {

                   // Log.e("MESSAGES", "\n--------------------------------------\n"+s +"\n--------------------------------------\n");

                    mHandler.post(new Runnable() {


                        @Override
                        public void run() {

                            JSONArray jsonArray = null;
                            try {

                                jsonArray = new JSONArray(s);
                                Globals.userlist=jsonArray;
                                sendBroadcast(new Intent().setAction("UserList"));

                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            }



                        }
                    });
                }
            },String.class);







        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public void Send(final String ConnectionId,final String toUserId,final String message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                proxy.invoke("sendMessage", message, toUserId,ConnectionId);
                return null;
            }
        }.execute();
    }




    public void GetAllMessages(final String fromUserId,final  String toUserId,final  String StartIndex, final  String  PageSize ) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                proxy.invoke("GetMessage", fromUserId,toUserId,StartIndex,PageSize);
                return null;
            }
        }.execute();
    }



    public void GetRecentChats(final String fromUserId,final  String StartIndex, final  String  PageSize ) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                proxy.invoke("GetRecentChatAndOnlineUsers", fromUserId,StartIndex,PageSize);
                return null;
            }
        }.execute();
    }


    public void GetMessageHistory(final String roomName) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                proxy.invoke(MessageViewModel[].class, "GetMessageHistory", roomName).done(
                        new Action<MessageViewModel[]>() {
                            @Override
                            public void run(MessageViewModel[] messageViewModels) throws Exception {
                                Globals.Messages.clear();
                                Globals.Messages.addAll(Arrays.asList(messageViewModels));

                                for (MessageViewModel m : Globals.Messages) {
                                    m.IsMine = m.From.equals(User.DisplayName) ? 1 : 0;
                                }
                                sendBroadcast(new Intent().setAction("notifyAdapter"));
                            }
                        });
                return null;
            }
        }.execute();
    }



    private void ExitWithMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopSelf();
            }
        }, 3000);
    }

}
