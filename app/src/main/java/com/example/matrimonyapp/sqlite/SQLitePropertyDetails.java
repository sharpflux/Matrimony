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
    public static final String  PROPERTY_TYPE = "property_type";
    public static final String  PROPERTY_TYPE_ID = "property_type_id";
    public static final String  OWNERSHIP_TYPE = "ownership_type";
    public static final String  BHK_TYPE = "bhk_type";
    public static final String  BHK_TYPE_ID = "bhk_type_id";
    public static final String  CARPET_AREA = "carpet_area";
    public static final String  ADDRESS = "address";
    public static final String  COUNTRY_ID = "country_id";
    public static final String  COUNTRY_NAME = "country_name";
    public static final String  STATE_ID = "state_id";
    public static final String  STATE_NAME = "state_name";
    public static final String  CITY_ID = "city_id";
    public static final String  CITY_NAME = "city_name";

    /*    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID =  "taluka_id";
    public static final String  DISTRICT_NAME = "district_name";
    public static final String  TALUKA_NAME =  "taluka_name";*/

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
                    + PROPERTY_TYPE + " text, "
                    + PROPERTY_TYPE_ID + " text, "
                    + OWNERSHIP_TYPE + " text, "
                    + BHK_TYPE + " text, "
                    + BHK_TYPE_ID + " text, "
                    + CARPET_AREA + " text, "
                    + ADDRESS + " text, "
                    + COUNTRY_ID + " int, "
                    + COUNTRY_NAME + " text, "
                    + STATE_ID + " int, "
                    + STATE_NAME + " text, "
                    + CITY_ID + " int, "
                    + CITY_NAME + " text"
/*                    + DISTRICT_ID + " int, "
                    + DISTRICT_NAME + " text, "
                    + TALUKA_ID + " int, "
                    + TALUKA_NAME + " text"*/
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

    public long insertPropertyDetails(String property_details_id, String propertyType, String propertyTypeId,
                                      String ownershipType, String bhkType, String bhkTypeId,
                                      String carpetArea, String address,  String countryName, String countryId,
                                      String stateName, String stateId, String cityName, String cityId)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(PROPERTY_DETAILS_ID, property_details_id);
        contentValues.put(PROPERTY_TYPE, propertyType);
        contentValues.put(PROPERTY_TYPE_ID, propertyTypeId);
        contentValues.put(OWNERSHIP_TYPE, ownershipType);
        contentValues.put(BHK_TYPE, bhkType);
        contentValues.put(BHK_TYPE_ID, bhkTypeId);
        contentValues.put(CARPET_AREA, carpetArea);
        contentValues.put(ADDRESS, address);
        contentValues.put(COUNTRY_ID, countryId);
        contentValues.put(COUNTRY_NAME, countryName);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(STATE_NAME, stateName);
        contentValues.put(CITY_ID, cityId);
        contentValues.put(CITY_NAME, cityName);

/*        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(DISTRICT_NAME, districtName);
        contentValues.put(TALUKA_ID, talukaId);
        contentValues.put(TALUKA_NAME, talukaName);*/
        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updatePropertyDetails(String id, String property_details_id,
                                     String propertyType, String propertyTypeId,
                                     String ownershipType, String bhkType, String bhkTypeId,
                                     String carpetArea, String address, String countryName, String countryId,
                                     String stateName, String stateId, String cityName, String cityId) //String districtName, String districtId, String talukaName,String talukaId
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(PROPERTY_DETAILS_ID, property_details_id);
        contentValues.put(PROPERTY_TYPE, propertyType);
        contentValues.put(PROPERTY_TYPE_ID, propertyTypeId);
        contentValues.put(OWNERSHIP_TYPE, ownershipType);
        contentValues.put(BHK_TYPE, bhkType);
        contentValues.put(BHK_TYPE_ID, bhkTypeId);
        contentValues.put(CARPET_AREA, carpetArea);
        contentValues.put(ADDRESS, address);
        contentValues.put(COUNTRY_ID, countryId);
        contentValues.put(COUNTRY_NAME, countryName);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(STATE_NAME, stateName);
        contentValues.put(CITY_ID, cityId);
        contentValues.put(CITY_NAME, cityName);
/*        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(DISTRICT_NAME, districtName);
        contentValues.put(TALUKA_ID, talukaId);
        contentValues.put(TALUKA_NAME, talukaName);*/

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
        return sqLiteDatabase.delete(TABLE_NAME,ID+" = ?", new String[]{Integer.toString(id)});


    }


    public int deleteAll()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME,"1",null);

    }

}
