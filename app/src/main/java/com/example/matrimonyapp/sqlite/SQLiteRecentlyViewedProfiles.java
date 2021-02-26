package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteTransactionListener;

import com.example.matrimonyapp.activity.HomeActivity;
import com.example.matrimonyapp.adapter.RecentlyViewedAdapter;
import com.example.matrimonyapp.listener.DatabaseObservable;
import com.example.matrimonyapp.modal.TimelineModel;

import java.util.ArrayList;

public class SQLiteRecentlyViewedProfiles extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyRecentlyViewedProfiles.db";
    public static final String TABLE_NAME = "recently_viewed_profiles";
    public static final String  ID = "id";
    //public static final String  RECENTLY_VIEWED_PROFILES_ID = "recently_viewed_profiles_id";
    public static final String  USER_ID = "user_id";
    public static final String  USER_NAME = "user_name";
    public static final String  USER_PROFILE_PIC = "user_profile_pic";
    public static final String  ADDRESS = "address";
    public static final int MAX_SIZE=3;
    public RecentlyViewedAdapter recentlyViewedAdapter;
    ArrayList<TimelineModel> arrayList_timelineModel;

    public SQLiteRecentlyViewedProfiles(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
        /*this.recentlyViewedAdapter= recentlyViewedAdapter;
        this.arrayList_timelineModel = arrayList_timelineModel;*/
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    //+ LANGUAGE_KNOWN_DETAILS_ID+ " int, "
                    + USER_ID + " text, "
                    + USER_NAME + " text, "
                    + USER_PROFILE_PIC + " text, "
                    + ADDRESS + " text "
                    + " )");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }


    }


    // drop table if already exists
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    // fetch data by id
    public Cursor getDataById(int userId)
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = "+userId, null);
        return res;

    }

    // fetch all data
    public Cursor getAllData()
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" order by "+ID+" desc", null);  //
        return res;

    }

    public String containsDetails(String userId)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+USER_ID+" = "+userId, null);

        if(res.getCount()<1)
        {
            res.close();
            return "0";
        }
        else
        {
            res.moveToFirst();
            String id = res.getString(res.getColumnIndex(ID));
            res.close();

            return id;
        }

    }

    public long insertRecentlyViewedProfiles(String userId, String userName, String userProfilePic, String address)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, userId);
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_PROFILE_PIC, userProfilePic);
        contentValues.put(ADDRESS, address);

        Cursor cursor = getAllData();
        if(cursor != null)
        {
             if (containsDetails(userId).equals("0")) {

                 if (cursor.getCount() >= MAX_SIZE) {
                     if (cursor.moveToLast()) {
                         String firstUserId = cursor.getString(1); // ID
                         for (int i=0; i<HomeActivity.arrayList_recentlyviewed.size(); i++)
                         {
                             TimelineModel timelineModel = HomeActivity.arrayList_recentlyviewed.get(i);
                             if (timelineModel.getUserId().equals(firstUserId))
                             {
                                 HomeActivity.arrayList_recentlyviewed.remove(i);
                                 deleteRecentlyViewedProfiles(new Integer(firstUserId));
                                 break;
                             }
                         }

                     }
                     cursor.close();
                 }

                 //databaseObserver.onDatabaseChanged();

                 TimelineModel timelineModel = new TimelineModel();
                 timelineModel.setUserId(userId);
                 timelineModel.setUserName(userName);
                 timelineModel.setProfilePic(userProfilePic);
                 timelineModel.setUserCity(address);
                 HomeActivity.arrayList_recentlyviewed.add(0,timelineModel);
                 HomeActivity.recentlyViewedAdapter.notifyDataSetChanged();
                 return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
                 //insert returns rowId of newly inserted row or -1 if an errror occured

             }
        }
        return  0;

    }


    public int updateRecentlyViewedProfiles( String userId, String userName, String userProfilePic, String address)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(USER_ID, userId);
        contentValues.put(USER_NAME, userName);
        contentValues.put(USER_PROFILE_PIC, userProfilePic);
        contentValues.put(ADDRESS, address);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, USER_ID+" = ?",
                new String[]{userId});
        //update returns no. of rows updated
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteRecentlyViewedProfiles(Integer userId)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete method returns no. of rows deleted or 0 otherwise
        return sqLiteDatabase.delete(TABLE_NAME,ID+" = ?", new String[]{Integer.toString(userId)});


    }


    public int deleteAll()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME,"1",null);

    }



    public interface DatabaseObserver {
        void onDatabaseChanged();
    }

}
