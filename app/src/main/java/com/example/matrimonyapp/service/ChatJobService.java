package com.example.matrimonyapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.matrimonyapp.R;
import com.example.matrimonyapp.activity.ChatTest;
import com.example.matrimonyapp.activity.MainActivity;
import com.example.matrimonyapp.activity.SignalRMessagesActivity;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.volley.CustomSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

public class ChatJobService extends JobService {
    // JobService thread
    private JobThread jobThread;
    private Handler handler;


    MyReceiver myReceiver;
    ChatService chatService;
    boolean mBound = false;

    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    private static final String TAG = "MyFirebaseMsgService";

    UserModel userModel;

    private void log(String msg) {
        Log.e("MyJobService", msg);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            mBound = true;
            //chatService.GetAllMessages(userModel.getUserId(),toUserId,"1","15");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    private void connect() {
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notifyAdapter");
        registerReceiver(myReceiver, intentFilter);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        log("onCreate();");
        userModel= CustomSharedPreference.getInstance(getApplicationContext()).getUser();


        handler = new Handler();
        jobThread = new JobThread();
        jobThread.start(); // start the thread when Service is created
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        log("onStartCommand();");

        // since onStartCommand is called only if the thread is already running
        // we just ignore this method, since we already started the thread in onCreate()
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        log("onStartJob();");

        // we just ignore this method,
        // since we already started the thread in onCreate()
        return true; // return true to let the service do the job
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        log("onStopJob();");
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ChatJobService.this, "Job is stopped ", Toast.LENGTH_LONG).show();
            }
        });
        // change the thread state to cancel the execution
        jobThread.stopThread = true;
        return false;
    }


    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "notifyAdapter":
                    addNotification(Globals.NewMessage);
                    MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.message_tone);
                    mPlayer.start();
                    break;


            }
        }
    } // MyReceiver
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


    class JobThread extends Thread{

        boolean stopThread = false;

        @Override
        public void run() {
            int count = 0;

             connect();

            while (count != 5 && !stopThread){
                count++;

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    stopSelf();
                    log("Thread interrupted");
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatJobService.this, "Counter is running ", Toast.LENGTH_LONG).show();
                    }
                });

                log("count: " +count);
            }
        }
    }
}

