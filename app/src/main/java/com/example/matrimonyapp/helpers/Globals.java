package com.example.matrimonyapp.helpers;


import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.MessageViewModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Globals {
    public static ArrayList<MessageViewModel> Messages = new ArrayList<com.example.matrimonyapp.modal.MessageViewModel>();
    public static ArrayList<String> Rooms = new ArrayList<>();
    public static JSONArray userlist = new JSONArray();
    public static JSONArray onlineUserlist = new JSONArray();
    public static JSONObject offlineUser = new JSONObject();
    public static String NewMessage = new String();
    public static String allMessages = new String();
    public static ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();

    public static String UserId = new String();
    public static String status = new String();
}
