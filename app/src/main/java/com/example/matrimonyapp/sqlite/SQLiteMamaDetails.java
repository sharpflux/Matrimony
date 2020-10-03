package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteMamaDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyMama.db";
    public static final String TABLE_NAME = "mama";
    public static final String  ID = "id"; // 1
    public static final String  MAMA_DETAILS_ID = "mama_details_id";
    public static final String  NAME = "name";
    public static final String  MOBILE_NO = "mobileno";
    public static final String  OCCUPATION_ID = "occupation_id";
    public static final String  OCCUPATION_NAME = "occupation_name";
    public static final String  ADDRESS = "address";
    public static final String  STATE_ID = "state_id";
    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID =  "taluka_id";
    public static final String  STATE_NAME = "state_name";
    public static final String  DISTRICT_NAME = "district_name";
    public static final String  TALUKA_NAME =  "taluka_name";
    public static final String  IS_ALIVE =  "is_alive";

    public SQLiteMamaDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + MAMA_DETAILS_ID + " int, "
                    + NAME + " text, "
                    + MOBILE_NO + " text, "
                    + OCCUPATION_ID + " int, "
                    + OCCUPATION_NAME + " text, "
                    + ADDRESS + " text, "
                    + STATE_ID + " int, "
                    + STATE_NAME + " text, "
                    + DISTRICT_ID + " int, "
                    + DISTRICT_NAME + " text, "
                    + TALUKA_ID + " int, "
                    + TALUKA_NAME + " text,"
                    + IS_ALIVE+ " int"
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

    public int getNoOfMama()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        int res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null).getCount();
        return res;
    }

    public long insertMamaDetails(String mama_details_id, String name, String mobileNo, String occupationId, String occupationName,
                              String address, String state_id, String district_id, String taluka_id,
                              String state_name, String district_name, String taluka_name, String is_alive)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(MAMA_DETAILS_ID, mama_details_id);
        contentValues.put(NAME, name);
        contentValues.put(MOBILE_NO, mobileNo);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(OCCUPATION_NAME, occupationName);
        contentValues.put(ADDRESS, address);
        contentValues.put(STATE_ID, state_id);
        contentValues.put(STATE_NAME, state_name);
        contentValues.put(DISTRICT_ID, district_id);
        contentValues.put(DISTRICT_NAME, district_name);
        contentValues.put(TALUKA_ID, taluka_id);
        contentValues.put(TALUKA_NAME, taluka_name);
        contentValues.put(IS_ALIVE, is_alive);
        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateMamaDetails(String id, String mama_details_id, String name, String mobileNo, String occupationId, String occupationName,
                             String address, String state_id, String district_id, String taluka_id,
                             String state_name, String district_name, String taluka_name, String is_alive)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, name);
        contentValues.put(MOBILE_NO, mobileNo);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(OCCUPATION_NAME, occupationName);
        contentValues.put(ADDRESS, address);
        contentValues.put(STATE_ID, state_id);
        contentValues.put(STATE_NAME, state_name);
        contentValues.put(DISTRICT_ID, district_id);
        contentValues.put(DISTRICT_NAME, district_name);
        contentValues.put(TALUKA_ID, taluka_id);
        contentValues.put(TALUKA_NAME, taluka_name);
        contentValues.put(IS_ALIVE, is_alive);


        return sqLiteDatabase.update(TABLE_NAME, contentValues, MAMA_DETAILS_ID+" = ? and "+ID+" = ? ",
                new String[]{mama_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteMamaDetails(Integer id)
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
