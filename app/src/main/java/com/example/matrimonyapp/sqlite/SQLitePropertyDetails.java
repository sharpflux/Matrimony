package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLitePropertyDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyProperty.db";
    public static final String TABLE_NAME = "property";
    public static final String  ID = "id";
    public static final String  PROPERTY_DETAILS_ID = "property_details_id";
    public static final String  AREA = "name";
    public static final String  ADDRESS = "address";
    public static final String  STATE_ID = "state_id";
    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID =  "taluka_id";
    public static final String  STATE_NAME = "state_name";
    public static final String  DISTRICT_NAME = "district_name";
    public static final String  TALUKA_NAME =  "taluka_name";

    public SQLitePropertyDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + PROPERTY_DETAILS_ID + " int, "
                    + AREA + " text, "
                    + ADDRESS + " text, "
                    + STATE_ID + " int, "
                    + STATE_NAME + " text, "
                    + DISTRICT_ID + " int, "
                    + DISTRICT_NAME + " text, "
                    + TALUKA_ID + " int, "
                    + TALUKA_NAME + " text"
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

    public long insertPropertyDetails(String property_details_id, String area, String address,
                                      String state_id, String district_id, String taluka_id,
                                      String state_name, String district_name, String taluka_name)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(PROPERTY_DETAILS_ID, property_details_id);
        contentValues.put(AREA, area);
        contentValues.put(ADDRESS, address);
        contentValues.put(STATE_ID, state_id);
        contentValues.put(STATE_NAME, state_name);
        contentValues.put(DISTRICT_ID, district_id);
        contentValues.put(DISTRICT_NAME, district_name);
        contentValues.put(TALUKA_ID, taluka_id);
        contentValues.put(TALUKA_NAME, taluka_name);
        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updatePropertyDetails(String id, String property_details_id, String area, String address, String state_id, String district_id, String taluka_id,
                                 String state_name, String district_name, String taluka_name)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(AREA, area);
        contentValues.put(ADDRESS, address);
        contentValues.put(STATE_ID, state_id);
        contentValues.put(STATE_NAME, state_name);
        contentValues.put(DISTRICT_ID, district_id);
        contentValues.put(DISTRICT_NAME, district_name);
        contentValues.put(TALUKA_ID, taluka_id);
        contentValues.put(TALUKA_NAME, taluka_name);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, PROPERTY_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{property_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deletePropertyDetails(Integer id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete method returns no. of rows deleted or 0 otherwise
        return sqLiteDatabase.delete(TABLE_NAME,SQLiteMamaDetails.ID+" = ?", new String[]{Integer.toString(id)});


    }


    public int deleteAll()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME,"1",null);

    }

}
