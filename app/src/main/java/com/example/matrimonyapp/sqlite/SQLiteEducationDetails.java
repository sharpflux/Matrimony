package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteEducationDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyEducation.db";
    public static final String TABLE_NAME = "education";
    public static final String  ID = "id";
    public static final String  EDUCATION_DETAILS_ID = "education_details_id";
    public static final String  QUALIFICATION_LEVEL = "qualification_level";        //23
    public static final String  QUALIFICATION_LEVEL_ID = "qualification_level_id";  //24
    public static final String  INSTITUTE_NAME = "institute_name";
    public static final String  ADDRESS = "address";
    public static final String  STATE_NAME = "state_name";
    public static final String  STATE_ID = "state_id";
    public static final String  DISTRICT_NAME = "district_name";
    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_NAME =  "taluka_name";
    public static final String  TALUKA_ID =  "taluka_id";

    public SQLiteEducationDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + EDUCATION_DETAILS_ID + " text, "
                    + QUALIFICATION_LEVEL + " text, "
                    + QUALIFICATION_LEVEL_ID + " text, "
                    + INSTITUTE_NAME + " text, "
                    + ADDRESS + " text, "
                    + STATE_NAME + " text, "
                    + STATE_ID + " text, "
                    + DISTRICT_NAME + " text, "
                    + DISTRICT_ID + " text, "
                    + TALUKA_NAME + " text, "
                    + TALUKA_ID + " text "
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

    public String containsDetails(String education_details_id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+EDUCATION_DETAILS_ID+" = "+education_details_id, null);

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


    public long insertEducationDetails(String education_details_id, String qualificationLevel, String qualificationLevelId,
                                  String instituteName, String instituteAddress,
                                  String stateName, String stateId, String districtName, String districtId,
                                  String talukaName, String talukaId)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(EDUCATION_DETAILS_ID, education_details_id);
        contentValues.put(QUALIFICATION_LEVEL, qualificationLevel);
        contentValues.put(QUALIFICATION_LEVEL_ID, qualificationLevelId);
        contentValues.put(INSTITUTE_NAME, instituteName);
        contentValues.put(ADDRESS, instituteAddress);
        contentValues.put(STATE_NAME, stateName);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(DISTRICT_NAME, districtName);
        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(TALUKA_NAME, talukaName);
        contentValues.put(TALUKA_ID, talukaId);

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateEducationDetails( String id, String education_details_id, String qualificationLevel, String qualificationLevelId,
                                  String instituteName, String instituteAddress,
                                  String stateName, String stateId, String districtName, String districtId,
                                  String talukaName, String talukaId)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(EDUCATION_DETAILS_ID, education_details_id);
        contentValues.put(QUALIFICATION_LEVEL, qualificationLevel);
        contentValues.put(QUALIFICATION_LEVEL_ID, qualificationLevelId);
        contentValues.put(INSTITUTE_NAME, instituteName);
        contentValues.put(ADDRESS, instituteAddress);
        contentValues.put(STATE_NAME, stateName);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(DISTRICT_NAME, districtName);
        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(TALUKA_NAME, talukaName);
        contentValues.put(TALUKA_ID, talukaId);


        return sqLiteDatabase.update(TABLE_NAME, contentValues, EDUCATION_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{education_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured
        //update returns no. of rows updated
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteEducationDetails(Integer id)
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
