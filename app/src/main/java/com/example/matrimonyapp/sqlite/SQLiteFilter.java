package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Sagar Hatikat on 10 September 2019
 */
public class SQLiteFilter extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Matrimony.db";
    public static final String TABLE_NAME = "Filter";
    private static final String TABLE_CATEGORY= "CategoryTable";
    public static final String ID = "ID";
    public static final String FILTERBY = "FilterBy";
    public static final String COORESPONDENCEID = "CorespondenceId";

    public static final String CATEGORYID = "CategoryId";
    public static final String ITEMTYPEID = "ItemTypeId";


    public SQLiteFilter(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,FILTERBY TEXT,CORESPONDENCEID TEXT)");
        db.execSQL("create table " + TABLE_CATEGORY +" (ID INTEGER PRIMARY KEY AUTOINCREMENT,CATEGORYID TEXT,ITEMTYPEID TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_CATEGORY);
        onCreate(db);
    }

    public boolean FilterInsert(String FilterBy, String FilterId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FILTERBY,FilterBy);
        contentValues.put(COORESPONDENCEID,FilterId);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


    public boolean CategoryInsert(String CategoryId, String ItemTypeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CATEGORYID,CategoryId);
        contentValues.put(ITEMTYPEID,ItemTypeId);
        long result = db.insert(TABLE_CATEGORY,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }
    public Cursor GetAllCategory() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT CATEGORYID,ITEMTYPEID FROM " + TABLE_CATEGORY ;
        Cursor cursor =  db.rawQuery(query,null);
        return cursor;

    }
    public Integer DeleteRecordCategory () {
        SQLiteDatabase db = this.getWritableDatabase();
        return   db.delete(TABLE_CATEGORY, null, null);
    }
    public Cursor FilterGetByFilterName(String FilterBy) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT CORESPONDENCEID FROM " + TABLE_NAME + " WHERE FILTERBY = ?";
        Cursor cursor =  db.rawQuery(query, new String[] {FilterBy});
        return cursor;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public Integer DeleteRecord (String FilterBy, String CorrespondanceId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "FILTERBY = ? AND CORESPONDENCEID = ?",new String[] {FilterBy,CorrespondanceId});
    }
    public Integer DeleteRecordAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        return   db.delete(TABLE_CATEGORY, null, null);
    }
    public String GETExist(String itemId){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT ITEMID FROM " + TABLE_NAME + " WHERE ITEMID = ?";
        Cursor cursor =  db.rawQuery(query, new String[] {itemId});
        String Id="0";
        if(cursor.moveToFirst()){
            Id = cursor.getString(0);
        }
        return  Id;
    }
}