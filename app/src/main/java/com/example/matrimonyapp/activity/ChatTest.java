package com.example.matrimonyapp.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.example.matrimonyapp.customViews.CustomDialogLoadingProgressBar;
import com.example.matrimonyapp.helpers.Globals;
import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.ChatModelObject;
import com.example.matrimonyapp.modal.DateItem;
import com.example.matrimonyapp.modal.DateObject;
import com.example.matrimonyapp.modal.ListItem;
import com.example.matrimonyapp.modal.ListObject;
import com.example.matrimonyapp.modal.MineItem;
import com.example.matrimonyapp.modal.OtherItem;
import com.example.matrimonyapp.modal.UserModel;
import com.example.matrimonyapp.service.ChatJobService;
import com.example.matrimonyapp.service.ChatService;
import com.example.matrimonyapp.utils.DateParser;
import com.example.matrimonyapp.utils.EndlessRecyclerViewScrollListener;
import com.example.matrimonyapp.volley.CustomSharedPreference;
import com.example.matrimonyapp.volley.URLs;
import com.example.matrimonyapp.volley.VolleySingleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import de.hdodenhof.circleimageview.CircleImageView;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import hani.momanii.supernova_emoji_library.Helper.EmojiconTextView;


// REF
//https://stackoverflow.com/questions/41447044/divide-elements-on-groups-in-recyclerview-or-grouping-recyclerview-items-say-by

//https://www.journaldev.com/12372/android-recyclerview-example

//https://medium.com/@saber.solooki/sticky-header-for-recyclerview-c0eb551c3f68


public class ChatTest extends AppCompatActivity {

    private RecyclerView recyclerView_chat;
    private ArrayList<ChatModel> chatModelsList;
    private ChatAdapter chatAdapter;
    private CustomDialogLoadingProgressBar customDialogLoadingProgressBar;
    UserModel userModel;

    EmojiconEditText emojiconEditText, emojiconEditText2;
    EmojiconTextView textView;
    ImageView emojiButton, imageView_back;
    ImageView submitButton;
    View rootView;
    EmojIconActions emojIcon;
    private Bundle bundle;
    FloatingActionButton voiceRecordingOrSend;
    private Intent intent;
    CircleImageView circleImageView_profilePic;
    TextView textView_userName, tvStatus;
    String connectionId = "0", toUserId = "";

    //Chat
    MyReceiver myReceiver;
    ChatService chatService;
    boolean mBound = false;
    List<ListItem> consolidatedList;
    androidx.appcompat.widget.Toolbar chatToolbar;

    private void connect() {
        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("notifyAdapter");
        intentFilter.addAction("getallMessages");
        registerReceiver(myReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    @Override
    protected void onResume() {
        try {
            connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    @Override
    protected void onStop() {

        try {
            if (myReceiver != null) {
                unregisterReceiver(myReceiver);
                tvStatus.setText("");
            }

            if (mBound) {

                unbindService(mConnection);
                tvStatus.setText("");
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
            mBound = true;
            tvStatus.setText("Online");
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
        setContentView(R.layout.activity_chat_test);

        intent = getIntent();

        myReceiver = new MyReceiver();

        connect();

        circleImageView_profilePic = findViewById(R.id.circleImageView_profilePic);
        userModel = CustomSharedPreference.getInstance(this).getUser();
        textView_userName = findViewById(R.id.textView_userName);
        imageView_back = findViewById(R.id.imageView_back);
        tvStatus = findViewById(R.id.tvStatus);

        chatToolbar = findViewById(R.id.chatToolbar);

        bundle = intent.getExtras();
        if (bundle != null) {
            connectionId = bundle.getString("connectionId");
            toUserId = bundle.getString("toUserId");
            textView_userName.setText(bundle.getString("toUserName"));

            Glide.with(ChatTest.this)
                    .load(URLs.MainURL + bundle.getString("toUserProfilePic"))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .circleCrop()
                    .placeholder(R.color.quantum_grey100)
                    .into(circleImageView_profilePic);

        }

        chatModelsList = new ArrayList<ChatModel>();
        consolidatedList = new ArrayList<>();

        recyclerView_chat = findViewById(R.id.productListRecyView);

     /*   LinearLayoutManager mLayoutManager= new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerView_chat.setLayoutManager(mLayoutManager);*/

        customDialogLoadingProgressBar = new CustomDialogLoadingProgressBar(ChatTest.this);

        bindRecyclerView();


        rootView = findViewById(R.id.root_view);
        voiceRecordingOrSend = findViewById(R.id.voiceRecordingOrSend);

        emojiButton = (ImageView) findViewById(R.id.addEmoticon);

        emojiconEditText = (EmojiconEditText) findViewById(R.id.emojicon_edit_text);
        emojIcon = new EmojIconActions(this, rootView, emojiconEditText, emojiButton);
        emojIcon.closeEmojIcon();
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

        emojiButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                emojIcon.ShowEmojIcon();
                return false;
            }
        });


        emojIcon.setUseSystemEmoji(false);

        imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatTest.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        });


        AsyncTaskRunner runner = new AsyncTaskRunner();
        runner.execute("1");

        voiceRecordingOrSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = emojiconEditText.getText().toString().trim();
                if (chatService != null) {
                    if (!message.equals("")) {
                        //if(!connectionId.equals("0")) {


                        Date c = Calendar.getInstance().getTime();
                        System.out.println("Current time => " + c);

                        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
                        String time1 = sdf.format(c);

                        //  SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.getDefault());
                        String formattedDate = df.format(c);

                        SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");


                        chatService.Send(connectionId, toUserId, emojiconEditText.getText().toString());
                        String date2 = format.format(Date.parse(formattedDate.toString()));
                        if (containsDate(chatModelsList, formattedDate)) {
                            System.out.println("Data Exist(s)");
                        } else {

                            if (!containsDate(chatModelsList, date2)) {
                                DateItem dateItem = new DateItem();
                                dateItem.setDate(date2);
                                consolidatedList.add(0, dateItem);
                            }
                        }


                        ChatModel chatModel = new ChatModel();
                        chatModel.setMessage(message);
                        chatModel.setSenderId(userModel.getUserId());
                        chatModel.setReceiverId(toUserId);
                        chatModel.setMessageTime(formattedDate);
                        chatModel.setTime(time1);
                        chatModelsList.add(0, chatModel);

                        emojiconEditText.setText("");
                        MineItem mineItem = new MineItem();
                        mineItem.setChatModelArray(chatModel);//setBookingDataTabs(bookingDataTabs);
                        consolidatedList.add(0, mineItem);
                        chatAdapter.notifyItemInserted(0);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // chatAdapter.setDataChange(consolidatedList);
                                recyclerView_chat.scrollToPosition(0);
                            }
                        });
                     /*  }
                        else {
                            Toast.makeText(ChatTest.this,"User is offline", Toast.LENGTH_LONG).show();
                        }*/
                    }
                } else {
                    Toast.makeText(ChatTest.this, "You are offline", Toast.LENGTH_LONG).show();

                }

            }
        });

    }

    boolean containsDate(ArrayList<ChatModel> list, String Date) {
        for (ChatModel item : list) {
            try {
                if (item.getMessageTime().equals(Date)) {
                    return true;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    private void bindRecyclerView() {
        chatAdapter = new ChatAdapter(getApplicationContext(), consolidatedList, chatToolbar, chatModelsList);
        // RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, true);
        mLayoutManager.setStackFromEnd(true);
        mLayoutManager.setReverseLayout(true);
        recyclerView_chat.setLayoutManager(mLayoutManager);
        recyclerView_chat.setItemAnimator(new DefaultItemAnimator());
        recyclerView_chat.setAdapter(chatAdapter);
        recyclerView_chat.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager, EndlessRecyclerViewScrollListener.LoadOnScrollDirection.TOP) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
               // mViewModel.fetchMessages(page);
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = String.valueOf(page+1);
                runner.execute(sleepTime);
            }
        });

       /* recyclerView_chat.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {


                    AsyncTaskRunner runner = new AsyncTaskRunner();
                    String sleepTime = String.valueOf(page + 1);
                    runner.execute(sleepTime);


                // chatAdapter.notifyItemInserted(chatModelsList.size() - 1);

            }
        });*/


/*        recyclerView_chat.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                AsyncTaskRunner runner = new AsyncTaskRunner();
                String sleepTime = String.valueOf(page + 1);
                runner.execute(sleepTime);
            }
        });*/
    }


    private void initAdapter() {
        chatAdapter = new ChatAdapter(getApplicationContext(), consolidatedList, chatToolbar, chatModelsList);
        recyclerView_chat.setAdapter(chatAdapter);
    }

    private void GetAllMessagesApi(final Integer PageIndex) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.URL_GET_GETALLMESSAGES + "fromUserId=" + userModel.getUserId() + "&toUserId=" + toUserId + "&StartIndex=" + PageIndex.toString() + "&PageSize=" + "20",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //chatModelsList.clear();
                        //    customDialogLoadingProgressBar.dismiss();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = new JSONArray(response);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                ChatModel chatModel1 = new ChatModel();
                                chatModel1.setMessage(jsonObject.getString("Message"));
                                chatModel1.setMessageTime(jsonObject.getString("MessageDateTime"));
                                chatModel1.setTime(jsonObject.getString("MessageTime"));
                                chatModel1.setSenderId(jsonObject.getString("FromUserId"));
                                chatModel1.setReceiverId(jsonObject.getString("ToUserId"));
                                chatModelsList.add(chatModel1);

                            }

                            final Map<Date, List<ChatModel>> groupedHashMap = getUnSortedMap(chatModelsList);

                            Map<Date, List<ChatModel>> reverseSortedMap = new TreeMap<Date, List<ChatModel>>(Collections.reverseOrder());
                            reverseSortedMap.putAll(groupedHashMap);

                            //  final HashMap<String, List<ChatModel>> groupedHashMap = groupDataIntoHashMap(chatModelsList);
                            // Map<String, List<ChatModel>> sortedMap = new TreeMap<String, List<ChatModel>>(groupedHashMap);
                            // Map<String, List<ChatModel>> reverseSortedMap = new TreeMap<String, List<ChatModel>>(Collections.reverseOrder());
                            //reverseSortedMap.putAll(groupedHashMap);

                            consolidatedList.clear();
                          //  chatAdapter.notifyItemInserted(consolidatedList.size() - 1);
                            for (Date date : reverseSortedMap.keySet()) {

                                for (ChatModel chat : reverseSortedMap.get(date)) {
                                    if (chat.getSenderId().equals(userModel.getUserId())) {
                                        MineItem mineItem = new MineItem();
                                        mineItem.setChatModelArray(chat);
                                        consolidatedList.add(mineItem);
                                    } else {
                                        OtherItem generalItem = new OtherItem();
                                        generalItem.setPojoOfJsonArray(chat);
                                        consolidatedList.add(generalItem);
                                    }
                                }

                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String date2 = format.format(Date.parse(date.toString()));
                                DateItem dateItem = new DateItem();
                                dateItem.setDate(date2);
                                consolidatedList.add(dateItem);
                            }
                            chatAdapter.setDataChange(consolidatedList);
                            if(PageIndex.equals(1)){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        recyclerView_chat.scrollToPosition(0);
                                    }
                                });
                            }



                        } catch (JSONException jsonException) {
                            jsonException.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ChatTest.this, "Something went wrong POST ! ", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("PageIndex :", "1");
                params.put("PageSize :", "50");
                params.put("UserId :", userModel.getUserId());
                return params;
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(ChatTest.this).addToRequestQueue(stringRequest);
    }

    class AsyncTaskRunner extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String[] params) {
            try {
                GetAllMessagesApi(Integer.valueOf(params[0]));
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPreExecute() {

          /*  if (!isFinishing() && customDialogLoadingProgressBar != null) {
                customDialogLoadingProgressBar.setCancelable(false);
                customDialogLoadingProgressBar.show();
            }*/
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


    private Map<Date, List<ChatModel>> getUnSortedMap(List<ChatModel> listOfPojosOfJsonArray) {
        Map<Date, List<ChatModel>> groupedHashMap = new HashMap<>();
        for (ChatModel pojoOfJsonArray : listOfPojosOfJsonArray) {
            String DATE_FORMAT_2 = "MM/dd/yyyy";
            String dtStart = pojoOfJsonArray.getMessageTime();
            SimpleDateFormat fDate = new SimpleDateFormat(DATE_FORMAT_2, Locale.US);
            try {
                Date hashMapKey = fDate.parse(dtStart);
                if (groupedHashMap.containsKey(hashMapKey)) {
                    groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
                } else {
                    List<ChatModel> list = new ArrayList<>();
                    list.add(pojoOfJsonArray);
                    groupedHashMap.put(hashMapKey, list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groupedHashMap;
    }

    private HashMap<String, List<ChatModel>> groupDataIntoHashMap(List<ChatModel> listOfPojosOfJsonArray) {

        HashMap<String, List<ChatModel>> groupedHashMap = new HashMap<>();
        String DATE_FORMAT_2 = "dd-MMM-yyyy";
        SimpleDateFormat formatter4 = new SimpleDateFormat("E, MMM dd yyyy");
        for (ChatModel pojoOfJsonArray : listOfPojosOfJsonArray) {
            String dtStart = pojoOfJsonArray.getMessageTime();
            SimpleDateFormat fDate = new SimpleDateFormat(DATE_FORMAT_2, Locale.US);
            try {
                // Date date = fDate.parse(dtStart);

                String hashMapKey = dtStart;//   DateParser.convertDateToString(date);

                if (groupedHashMap.containsKey(hashMapKey)) {
                    // The key is already in the HashMap; add the pojo object
                    // against the existing key.
                    groupedHashMap.get(hashMapKey).add(pojoOfJsonArray);
                } else {
                    // The key is not there in the HashMap; create a new key-value pair
                    List<ChatModel> list = new ArrayList<>();
                    list.add(pojoOfJsonArray);
                    groupedHashMap.put(hashMapKey, list);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        return groupedHashMap;
    }

    private class MyReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case "notifyAdapter":
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                    String currentDateandTime = sdf.format(new Date());

                    ChatModel chatModel = new ChatModel();
                    chatModel.setMessage(Globals.NewMessage);
                    chatModel.setSenderId(toUserId);
                    chatModel.setReceiverId(userModel.getUserId());
                    chatModel.setMessageTime(currentDateandTime);
                    chatModelsList.add(0, chatModel);

                    OtherItem otherItem = new OtherItem();
                    otherItem.setPojoOfJsonArray(chatModel);//setBookingDataTabs(bookingDataTabs);
                    consolidatedList.add(0, otherItem);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            chatAdapter.notifyDataSetChanged();
                            recyclerView_chat.scrollToPosition(0);
                        }
                    });
                    MediaPlayer mPlayer = MediaPlayer.create(ChatTest.this, R.raw.message_tone);
                    mPlayer.start();
                    break;
                case "getallMessages":
                    chatModelsList.clear();
                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(Globals.allMessages);
                        chatAdapter.notifyItemInserted(chatModelsList.size() - 1);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            ChatModel chatModel1 = new ChatModel();
                            chatModel1.setMessage(jsonObject.getString("Message"));
                            chatModel1.setMessageTime(jsonObject.getString("MessageDateTime"));
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
                    JSONArray jsonArray1 = (JSONArray) Globals.userlist;
                    try {

                        for (int i = 0; i < jsonArray1.length(); i++) {
                            JSONObject jsonObject = null;
                            jsonObject = jsonArray1.getJSONObject(i);
                            if (jsonObject.getString("FromUserId").equals(toUserId)) {
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

}