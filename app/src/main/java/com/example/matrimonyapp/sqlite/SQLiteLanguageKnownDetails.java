package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteLanguageKnownDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyLanguageKnown.db";
    public static final String TABLE_NAME = "language_known";
    public static final String  ID = "id";
    public static final String  LANGUAGE_KNOWN_DETAILS_ID = "language_known_details_id";
    public static final String  LANGUAGE = "language";
    public static final String  LANGUAGE_ID = "language_id";
    public static final String  FLUENCY = "fluency";

    public SQLiteLanguageKnownDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + LANGUAGE_KNOWN_DETAILS_ID+ " int, "
                    + LANGUAGE + " text, "
                    + LANGUAGE_ID + " text, "
                    + FLUENCY + " text "
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

    public String containsDetails(String languageKnownDetailsId)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+LANGUAGE_KNOWN_DETAILS_ID+" = "+languageKnownDetailsId, null);

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

    public long insertLanguageKnownDetails(String languageKnownDetailsId, String language,
                                           String languageId, String fluency)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(LANGUAGE_KNOWN_DETAILS_ID, languageKnownDetailsId);
        contentValues.put(LANGUAGE, language);
        contentValues.put(LANGUAGE_ID, languageId);
        contentValues.put(FLUENCY, fluency);

        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateLanguageKnownDetails( String id, String languageKnownDetailsId, String language,
                                  String languageId, String fluency)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(LANGUAGE_KNOWN_DETAILS_ID, languageKnownDetailsId);
        contentValues.put(LANGUAGE, language);
        contentValues.put(LANGUAGE_ID, languageId);
        contentValues.put(FLUENCY, fluency);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, LANGUAGE_KNOWN_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{languageKnownDetailsId, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured
        //update returns no. of rows updated
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteLanguageKnownDetails(Integer id)
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
