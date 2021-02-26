package com.example.matrimonyapp.listener;

import android.database.Cursor;

import com.example.matrimonyapp.sqlite.SQLiteRecentlyViewedProfiles;

public interface DatabaseObservable {
    //register the observer with this method
    void registerDbObserver(SQLiteRecentlyViewedProfiles.DatabaseObserver databaseObserver);
    //unregister the observer with this method
    void removeDbObserver(SQLiteRecentlyViewedProfiles.DatabaseObserver databaseObserver);
    //call this method upon database change
    void onDatabaseChanged();
}
