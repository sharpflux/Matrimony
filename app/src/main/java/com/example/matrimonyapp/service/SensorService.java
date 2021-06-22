package com.example.matrimonyapp.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.helpers.User;
import com.example.matrimonyapp.modal.MessageViewModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.utils.TimerCounter;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Calendar;
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

/**
 * Created by arvi on 12/11/17.
 */

public class SensorService extends Service {
    private Context ctx;
    TimerCounter tc;
    private int counter = 0;
    private static final String TAG = SensorService.class.getSimpleName();

    private HubConnection connection;
    private HubProxy proxy;
    private Handler handler;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String serverUrl = "http://sam.sharpflux.com/signalr";
    private String hubName = "chatHub";
    UserModel userModel;
    public SensorService() {

    }

    public SensorService(Context applicationContext) {
        super();
        ctx = applicationContext;
        Log.i(TAG, "SensorService class");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate()");
        userModel= CustomSharedPreference.getInstance(getApplicationContext()).getUser();
        tc = new TimerCounter();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.i(TAG, "onStartCommand()");

        if (!StartHubConnection()) {
            ExitWithMessage("Chat Service failed to start!");
        }
        if (!RegisterEvents()) {
            ExitWithMessage("End-point error: Failed to register Events!");
        }
        Toast.makeText(SensorService.this, "Notification Service started by user.", Toast.LENGTH_LONG).show();
        tc.startTimer(counter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "serviceOnDestroy()");

        super.onDestroy();

        Intent broadcastIntent = new Intent("com.arvi.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);
        tc.stopTimerTask();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i(TAG, "serviceonTaskRemoved()");

        Intent broadcastIntent = new Intent("com.arvi.ActivityRecognition.RestartSensor");
        sendBroadcast(broadcastIntent);

        // workaround for kitkat: set an alarm service to trigger service again
        Intent intent = new Intent(getApplicationContext(), SensorService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 1, intent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, SystemClock.elapsedRealtime() + 5000, pendingIntent);

        super.onTaskRemoved(rootIntent);

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Log.i(TAG, "onLowMemory()");
    }




    // SERVICE
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

                            addNotification(s);
                            // send_message.setText(send_message.getText()+"\n"+s2+" : "+s);
                            Toast.makeText(SensorService.this, s, Toast.LENGTH_LONG).show();
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

            proxy.on("offlineUser", new SubscriptionHandler1<String>() {
                @Override
                public void run(String s) {
                    try { // we added the list of connected users
                        JSONObject jsonObj= new JSONObject(s);
                        Globals.offlineUser=jsonObj;
                        Toast.makeText(SensorService.this,jsonObj.getString("FromUserId") +" Is Disconnected!", Toast.LENGTH_LONG).show();
                        sendBroadcast(new Intent().setAction("offlineUser"));

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

    private void addNotification(String Message) {
        Intent resultIntent = new Intent(getApplicationContext() , MainActivity.class);
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0 /* Request code */, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.custom_push);
        contentView.setImageViewResource(R.id.image, R.mipmap.matrimony);
        contentView.setTextViewText(R.id.title, "TEST");
        contentView.setTextViewText(R.id.text,Message);


        mBuilder = new NotificationCompat.Builder(getApplicationContext(),"0");
        mBuilder.setSmallIcon(R.mipmap.notify);
        mBuilder.setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent)
                .setContent(contentView);


        mNotificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);






        Notification notification = mBuilder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_SOUND;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        mNotificationManager.notify(1, notification);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel("0", "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);

            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId("0");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());

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