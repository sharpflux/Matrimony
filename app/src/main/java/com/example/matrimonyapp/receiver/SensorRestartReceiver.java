package com.example.matrimonyapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.matrimonyapp.service.ChatService;

public class SensorRestartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestartReceiver.class.getSimpleName(), "Service Stopped!");
        context.startService(new Intent(context, ChatService.class));
    }

}