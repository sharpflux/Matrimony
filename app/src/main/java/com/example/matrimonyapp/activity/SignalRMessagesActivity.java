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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.matrimonyapp.R;
import com.example.matrimonyapp.adapter.ChatAdapter;
import com.example.matrimonyapp.adapter.DirectMessagesAdapter;
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.DirectMessagesModel;
import com.example.matrimonyapp.modal.TimelineModel;
import com.example.matrimonyapp.modal.UserChat;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.service.SensorService;
import com.example.matrimonyapp.utils.EndlessRecyclerViewScrollListener;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
//import com.fasterxml.jackson.annotation.JsonInclude;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;
import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
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
    boolean isLoading = false;
    LinearLayout linearLayout_toolbar;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    String connectionId="0", toUserId="";

    String PageSize="15";

    Handler mHandler=new Handler(); //listener
    private ScrollView scrollView;

    MyReceiver myReceiver;
    ChatService chatService;
    boolean mBound = false;
    private LinearLayoutManager mLayoutManager;
    private boolean isAtBottom=false;
    private Bundle bundle;

    CheckBox mCheckBox;
    EmojiconEditText emojiconEditText, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton;
    ImageView submitButton;
    View rootView;
    EmojIconActions emojIcon;

    FloatingActionButton voiceRecordingOrSend;

    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    private static final String TAG = MainActivity.class.getSimpleName();

    public Context getCtx() {
        return ctx;
    }



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
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signal_r_messages);

        intent = getIntent();

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(SignalRMessagesActivity.this);

        myReceiver = new MyReceiver();


        voiceRecordingOrSend=findViewById(R.id.voiceRecordingOrSend);
        currentLanguage = getResources().getConfiguration().locale.getLanguage();
        scrollView = findViewById(R.id.scrollView);
        linearLayout_toolbar = findViewById(R.id.linearLayout_toolbar);
        linearLayout_message = findViewById(R.id.linearLayout_message);
        recyclerView_chat = findViewById(R.id.recyclerView_chat);
        //editText_message = findViewById(R.id.messageInput);
        textView_userName = findViewById(R.id.textView_userName);
        imageView_back = findViewById(R.id.imageView_back);
        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);
        //imageView_sendMessage = findViewById(R.id.imageView_sendMessage);
        rootView = findViewById(R.id.root_view);
        emojiButton = (ImageView) findViewById(R.id.addEmoticon);

        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, emojiButton);
        //emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }
            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });


        emojIcon.setUseSystemEmoji(false);
//        textView.setUseSystemDefault(true);




        userModel = CustomSharedPreference.getInstance(this).getUser();

        context = getApplicationContext();

        chatModelsList = new ArrayList<ChatModel>();


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





        mLayoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerView_chat.setLayoutManager(mLayoutManager);
        recyclerView_chat.scrollToPosition(chatModelsList.size());

        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("1");


        initAdapter();




      /*  recyclerView_chat.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                isLoading = true;
                Toast.makeText(SignalRMessagesActivity.this,String.valueOf(page+1) +" Is Page!", Toast.LENGTH_LONG).show();
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = String.valueOf(page+1);
                runner.execute(sleepTime);
            }
        });*/





        context=this;

        voiceRecordingOrSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                message = emojiconEditText.getText().toString().trim();

                if(chatService!=null){
                    if(!message.equals(""))
                    {
                        if(!connectionId.equals("0")) {
                            chatService.Send(connectionId, toUserId, emojiconEditText.getText().toString());
                            ChatModel chatModel = new ChatModel();
                            chatModel.setMessage(message);
                            chatModel.setSenderId(userModel.getUserId());
                            chatModel.setReceiverId(toUserId);
                            chatModelsList.add(0, chatModel);
                            chatAdapter.notifyItemInserted(0);
                            emojiconEditText.setText("");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatAdapter.notifyDataSetChanged();
                                    recyclerView_chat.scrollToPosition(0);
                                }
                            });
                        }
                        else {
                            Toast.makeText(SignalRMessagesActivity.this,"User is offline", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else {
                    Toast.makeText(SignalRMessagesActivity.this,"You are offline", Toast.LENGTH_LONG).show();

                }

            }
        });

/*        imageView_sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




            }
        });*/


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


    private void initAdapter() {

        chatAdapter = new ChatAdapter(this,null,null,null);
        recyclerView_chat.setAdapter(chatAdapter);
        recyclerView_chat.setHasFixedSize(true);
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
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.notifyDataSetChanged();
                            recyclerView_chat.scrollToPosition(0);
                        }
                    });
                    MediaPlayer mPlayer = MediaPlayer.create(SignalRMessagesActivity.this, R.raw.message_tone);
                    mPlayer.start();
                    break;
                case "getallMessages":
                    chatModelsList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(Globals.allMessages);
                        chatAdapter.notifyItemInserted(chatModelsList.size() - 1);
                        for (int i=0; i<jsonArray.length(); i++)
                        {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ChatModel chatModel1 = new ChatModel();
                            chatModel1.setMessage(jsonObject.getString("Message"));
//                            chatModel1.setMessageTime(jsonObject.getString("MessageDateTime"));
                            chatModel1.setSenderId(jsonObject.getString("FromUserId"));
                            chatModel1.setReceiverId(jsonObject.getString("ToUserId"));
                            chatModelsList.add(chatModel1);

                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                chatAdapter.notifyDataSetChanged();
                                recyclerView_chat.scrollToPosition(0);
                            }
                        });

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
        //Intent intent = new Intent(this, ChatService.class);
        //bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        ctx = this;
        mSensorService = new SensorService(getCtx());
        mServiceIntent = new Intent(getCtx(), mSensorService.getClass());
        if( ! isMyServiceRunning(mSensorService.getClass())) {
            startService(mServiceIntent);
        }

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notifyAdapter");
        intentFilter.addAction("getallMessages");
        registerReceiver(myReceiver, intentFilter);
    }

    private boolean isMyServiceRunning(Class <?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if(serviceClass.getName().equals(service.service.getClassName())) {
                Log.i(TAG, "isMyServiceRunning? " + true + "");
                return true;
            }
        }

        Log.i(TAG, "isMyServiceRunning? " + false + "");
        return false;
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
       /* try {
            if(myReceiver!=null)
                unregisterReceiver(myReceiver);

            if (mBound) {
                unbindService(mConnection);
                mBound = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }


    private void GetAllMessagesApi(Integer PageIndex) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET,URLs.URL_GET_GETALLMESSAGES +"fromUserId="+userModel.getUserId()+"&toUserId="+toUserId+"&StartIndex="+PageIndex.toString()+"&PageSize="+PageSize,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        //chatModelsList.clear();
                        customDialogLoadingProgressBar.dismiss();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);
                            chatAdapter.notifyItemInserted(chatModelsList.size() - 1);
                            for (int i=0; i<jsonArray.length(); i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ChatModel chatModel1 = new ChatModel();
                                chatModel1.setMessage(jsonObject.getString("Message"));
                              //  chatModel1.setMessageTime(jsonObject.getString("MessageDateTime"));
                                chatModel1.setSenderId(jsonObject.getString("FromUserId"));
                                chatModel1.setReceiverId(jsonObject.getString("ToUserId"));
                                chatModelsList.add(chatModel1);

                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    chatAdapter.notifyDataSetChanged();
                                    recyclerView_chat.scrollToPosition(0);
                                }
                            });

                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignalRMessagesActivity.this,"Something went wrong POST ! ",Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("PageIndex :","1");
                params.put("PageSize :","50");
                params.put("UserId :",userModel.getUserId());


                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(SignalRMessagesActivity.this).addToRequestQueue(stringRequest);




    }



    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {




            try {
                GetAllMessagesApi(Integer.valueOf( params[0]));
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
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


}
