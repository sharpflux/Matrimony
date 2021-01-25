package com.example.matrimonyapp.helpers;

import android.content.Context;
import android.content.SharedPreferences;

import microsoft.aspnet.signalr.client.http.CookieCredentials;

public class PrefsManager {

    public static void saveAuthCookie(Context context, CookieCredentials cookieCredentials) {
        SharedPreferences.Editor editor = context.getSharedPreferences("AuthCookie", Context.MODE_PRIVATE).edit();
        editor.putString("cookieCredentials", cookieCredentials.toString());
        editor.commit();
    }

    public static CookieCredentials loadAuthCookie(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("AuthCookie", Context.MODE_PRIVATE);
        String cookie = prefs.getString("cookieCredentials", null);

        CookieCredentials cc = new CookieCredentials(cookie);
        return cookie == null ? null : cc;
    }

    public static void clearPrefs(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("AuthCookie", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }
}
