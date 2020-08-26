package com.example.matrimonyapp.volley;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.style.AlignmentSpan;

import com.example.matrimonyapp.activity.LoginActivity;
import com.example.matrimonyapp.modal.UserModel;

import java.io.File;

public class CustomSharedPreference {

    private static final String SHARED_PREF_NAME = "MATRIMONYAPP";
    private static final String KEY_FULLNAME= "FULLNAME";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_MOBILE = "MOBILENO";
    private static final String KEY_BIRTHDATE= "BIRTHDATE";
    private static final String KEY_AGE = "AGE";
    private static final String KEY_GENDER = "GENDER";
    private static final String KEY_USERID = "USERID";
    private static final String KEY_PROFILEPIC= "PROFILEPIC";
    private static final String KEY_LANGUAGE = "LANGUAGE";

    private static CustomSharedPreference instance;

    private  static Context context;


    public CustomSharedPreference(Context temp_context) {
        context = temp_context;

    }

    public static synchronized CustomSharedPreference getInstance(Context t_context) {
        context =  t_context;
        if (instance == null) {
            instance = new CustomSharedPreference(context);
        }
        return instance;
    }


    public void saveUser(UserModel userModel)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERID,userModel.getUserId());
        editor.putString(KEY_FULLNAME,userModel.getFullName());
        editor.putString(KEY_MOBILE,userModel.getMobileNo());
        editor.putString(KEY_EMAIL,userModel.getEmailId());
        editor.putString(KEY_BIRTHDATE,userModel.getBirthdate());
        editor.putString(KEY_AGE,userModel.getAge());
        editor.putString(KEY_GENDER,userModel.getGender());
        editor.putString(KEY_PROFILEPIC,userModel.getProfilePic());
        editor.putString(KEY_LANGUAGE,userModel.getLanguage());

        editor.apply();

    }

    //this method will checker whether user is already logged in or not
    public boolean isLoggedIn() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(KEY_USERID, null) != null;
    }

        //this method will give the logged in user
    public UserModel getUser() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new UserModel(
                sharedPreferences.getString(KEY_USERID, null),
                sharedPreferences.getString(KEY_FULLNAME, null),
                sharedPreferences.getString(KEY_MOBILE, null),
                sharedPreferences.getString(KEY_EMAIL, null),
                sharedPreferences.getString(KEY_BIRTHDATE, null),
                sharedPreferences.getString(KEY_AGE, null),
                sharedPreferences.getString(KEY_GENDER, null),
                sharedPreferences.getString(KEY_PROFILEPIC, null),
                sharedPreferences.getString(KEY_LANGUAGE, null)
        );
    }


    public void logout() {

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        //editor.apply();
        editor.commit();

        deleteCache(context);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        ((Activity)context).finish();
    }


    public static void deleteCache(Context context)
    {

        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static boolean deleteDir(File dir)
    {
        if(dir!=null && dir.isDirectory())
        {
            String []children = dir.list();

            for (int i=0; i< children.length; i++)
            {
                boolean success = deleteDir(new File(dir, children[i]));

                if(!success)
                {
                    return false;
                }
            }
            return dir.delete();
        }
        else if (dir!=null && dir.isFile())
        {

            return dir.delete();
        }
        else
        {
            return false;
        }
    }


}
