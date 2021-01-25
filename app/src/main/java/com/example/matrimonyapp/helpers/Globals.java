package com.example.matrimonyapp.helpers;


import com.example.matrimonyapp.modal.ChatModel;
import com.example.matrimonyapp.modal.MessageViewModel;

import org.json.JSONArray;

import java.util.ArrayList;

public class Globals {
    public static ArrayList<MessageViewModel> Messages = new ArrayList<com.example.matrimonyapp.modal.MessageViewModel>();
    public static ArrayList<String> Rooms = new ArrayList<>();
    public static JSONArray userlist = new JSONArray();
    public static String NewMessage = new String();
    public static ArrayList<ChatModel> chatModelArrayList = new ArrayList<>();
}
