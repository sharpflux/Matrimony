package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteIsProfileComplete extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyIsProfileComplete.db";
    public static final String TABLE_NAME = "is_profile_complete";
    public static final String  ID = "id";
    public static final String  IS_PROFILE_COMPLETE_DETAILS_ID = "is_profile_complete_details_id";
    public static final String  SCREEN = "screen";
    public static final String  STATUS = "status";


    public SQLiteIsProfileComplete(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + IS_PROFILE_COMPLETE_DETAILS_ID + " int, "
                    + SCREEN + " text, "
                    + STATUS + " text "
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
    public Cursor getDataById(int id)
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+ID+" = "+id, null);
        return res;

    }

    // fetch all data
    public Cursor getAllData()
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null);
        return res;

    }

    public String containsDetails(String table_details_id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+IS_PROFILE_COMPLETE_DETAILS_ID+" = "+table_details_id, null);

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

    public long insertIsProfileCompleteDetails(String table_details_id, String screen, String status)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(IS_PROFILE_COMPLETE_DETAILS_ID, table_details_id);
        contentValues.put(SCREEN, screen);
        contentValues.put(STATUS, status);

        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateFarmDetails( String id, String table_details_id, String screen, String status)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(IS_PROFILE_COMPLETE_DETAILS_ID, table_details_id);
        contentValues.put(SCREEN, screen);
        contentValues.put(STATUS, status);


        return sqLiteDatabase.update(TABLE_NAME, contentValues, IS_PROFILE_COMPLETE_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{table_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured
        //update returns no. of rows updated
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteIsProfileCompleteDetails(Integer id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete method returns no. of rows deleted or 0 otherwise
        return sqLiteDatabase.delete(TABLE_NAME,ID+" = ?", new String[]{Integer.toString(id)});


    }


    public int deleteAll()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME,"1",null);

    }


}
