package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteFarmDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyFarm.db";
    public static final String TABLE_NAME = "farm";
    public static final String  ID = "id";
    public static final String  AREA = "area";
    public static final String  TYPE = "type";
    public static final String  CROPS = "crops";
/*    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID =  "taluka_id";
    public static final String  STATE_NAME = "state_name";
    public static final String  DISTRICT_NAME = "district_name";
    public static final String  TALUKA_NAME =  "taluka_name";*/

    public SQLiteFarmDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + AREA + " text, "
                    + TYPE + " text, "
                    + CROPS + " text "
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

    public long insertFarmDetails(String area, String type, String crops)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(AREA, area);
        contentValues.put(TYPE, type);
        contentValues.put(CROPS, crops);

        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateFarmDetails(String id, String area, String type, String crops)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(AREA, area);
        contentValues.put(TYPE, type);
        contentValues.put(CROPS, crops);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, ID+" = ?", new String[]{id});
        //insert returns rowId of newly inserted row or -1 if an errror occured
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteFarmDetails(Integer id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete method returns no. of rows deleted or 0 otherwise
        return sqLiteDatabase.delete(TABLE_NAME,SQLiteFarmDetails.ID+" = ?", new String[]{Integer.toString(id)});


    }


}
