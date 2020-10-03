package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteVehicleDetails extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonyVehicle.db";
    public static final String TABLE_NAME = "vehicle";
    public static final String  ID = "id";
    public static final String  VEHICLE_DETAILS_ID = "vehicle_details_id";
    public static final String  VEHICLE_TYPE = "vehicle_type";
    public static final String  VEHICLE_TYPE_ID = "vehicle_type_id";
    public static final String  BRAND_NAME = "brand_name";
    public static final String  BRAND_NAME_ID = "brand_name_id";
    public static final String  MODEL_NAME = "model_name";
    public static final String  MODEL_NAME_ID = "model_name_id";
    public static final String  YEAR = "year";
    public static final String  COST = "cost";

    public SQLiteVehicleDetails(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + VEHICLE_DETAILS_ID + " int, "
                    + VEHICLE_TYPE + " text, "
                    + VEHICLE_TYPE_ID + " text, "
                    + BRAND_NAME + " text, "
                    + BRAND_NAME_ID + " text, "
                    + MODEL_NAME + " text, "
                    + MODEL_NAME_ID + " text, "
                    + YEAR + " text, "
                    + COST + " text "
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

    public String containsDetails(String vehicle_details_id)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+VEHICLE_DETAILS_ID+" = "+vehicle_details_id, null);

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


    public long insertVehicleDetails(String vehicle_details_id, String vehicleType, String vehicleTypeId, String brandName,
                                     String brandNameId, String modelName, String modelNameId,
                                     String year, String cost)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(VEHICLE_DETAILS_ID, vehicle_details_id);
        contentValues.put(VEHICLE_TYPE, vehicleType);
        contentValues.put(VEHICLE_TYPE_ID, vehicleTypeId);
        contentValues.put(BRAND_NAME, brandName);
        contentValues.put(BRAND_NAME_ID, brandNameId);
        contentValues.put(MODEL_NAME, modelName);
        contentValues.put(MODEL_NAME_ID, modelNameId);
        contentValues.put(YEAR, year);
        contentValues.put(COST, cost);

        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateVehicleDetails(String id, String vehicle_details_id, String vehicleType, String vehicleTypeId, String brandName,
                                 String brandNameId, String modelName, String modelNameId,
                                 String year, String cost)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(VEHICLE_DETAILS_ID, vehicle_details_id);
        contentValues.put(VEHICLE_TYPE, vehicleType);
        contentValues.put(VEHICLE_TYPE_ID, vehicleTypeId);
        contentValues.put(BRAND_NAME, brandName);
        contentValues.put(BRAND_NAME_ID, brandNameId);
        contentValues.put(MODEL_NAME, modelName);
        contentValues.put(MODEL_NAME_ID, modelNameId);
        contentValues.put(YEAR, year);
        contentValues.put(COST, cost);

        return sqLiteDatabase.update(TABLE_NAME, contentValues, VEHICLE_DETAILS_ID+" = ? and "+ID+" = ?",
                new String[]{vehicle_details_id, id});
        //insert returns rowId of newly inserted row or -1 if an errror occured
        //update returns no. of rows updated
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteVehicleDetails(Integer id)
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
