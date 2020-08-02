package com.example.matrimonyapp.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteSetPreference extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MatrimonySetPreference.db";
    public static final String TABLE_NAME = "setPreference";
    public static final String  ID = "id";
    public static final String  USER_ID = "farm_details_id";
    public static final String  GENDER = "gender";
    public static final String  STATE_ID = "state_id";
    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID = "taluka_id";
    public static final String  AGE_MIN = "age_min";
    public static final String  AGE_MAX = "age_max";
    public static final String  RELIGION_ID = "religion_id";
    public static final String  CASTE_ID = "caste_id";
    public static final String  SUB_CASTE_ID = "sub_caste_id";
    public static final String  HIGHEST_QUALIFICATION_LEVEL_ID = "highest_qualification_level_id";
    public static final String  HIGHEST_QUALIFICATION_ID = "highest_qualification_id";
    public static final String  MARITAL_STATUS_ID = "marital_status_id";
    public static final String  DIET_ID = "diet_id";
    public static final String  OCCUPATION_ID = "occupation_id";
    public static final String  EXPECTED_INDIVIDUAL_INCOME = "expected_individual_income";
    public static final String  EXPECTED_FAMILY_INCOME = "expected_family_income";
    public static final String  FAMILY_TYPE = "family_type";
    public static final String  FAMILY_VALUE = "family_value";
    public static final String  HEIGHT_MIN = "height_min";
    public static final String  HEIGHT_MAX = "height_max";
    public static final String  SKIN_COLOR_ID = "skin_color_id";



    public SQLiteSetPreference(Context context)
    {
        super(context, DATABASE_NAME, null, 2);
    }


    // create table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        try {

            sqLiteDatabase.execSQL("create table " + TABLE_NAME + " ( "
                    + ID + " INTEGER PRIMARY KEY autoincrement, "
                    + USER_ID + " int, "
                    + GENDER + " text, "
                    + STATE_ID + " text, "
                    + DISTRICT_ID + " text, "
                    + TALUKA_ID + " text, "
                    + AGE_MIN + " text, "
                    + AGE_MAX + " text, "
                    + RELIGION_ID + " text, "
                    + CASTE_ID + " text, "
                    + SUB_CASTE_ID + " text, "
                    + HIGHEST_QUALIFICATION_LEVEL_ID + " text, "
                    + HIGHEST_QUALIFICATION_ID + " text, "
                    + MARITAL_STATUS_ID + " text, "
                    + DIET_ID + " text, "
                    + OCCUPATION_ID + " text, "
                    + EXPECTED_INDIVIDUAL_INCOME + " text, "
                    + EXPECTED_FAMILY_INCOME + " text, "
                    + FAMILY_TYPE + " text, "
                    + FAMILY_VALUE + " text, "
                    + HEIGHT_MIN + " text, "
                    + HEIGHT_MAX + " text, "
                    + SKIN_COLOR_ID + " text "
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
    public Cursor getDataByUserId(String userId)
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+USER_ID+" = "+userId, null);
        return res;

    }


    public boolean isPreferenceExistByUserId(String userId)
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+USER_ID+" = "+userId, null);

        if(res.getCount()>0)
        {
            return true;
        }
        return false;

    }



    // fetch all data
    public Cursor getAllData()
    {

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME, null);
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
            String id = res.getString(res.getColumnIndex(SQLiteMamaDetails.ID));
            res.close();

            return id;
        }

    }

    public long insertSetPreference(String user_id, String gender, String stateId, String districtId,
                                    String talukaId, String ageMin, String ageMax, String religionId,
                                    String casteId, String subCasteId, String highestQualificationLevelId,
                                    String highestQualificationId, String maritalStatusId, String dietId,
                                    String occupationId, String expectedIndividualIncome, String expectedFamilyIncome,
                                    String familyType, String familyValue, String heightMin, String heightMax,
                                    String skinColorId)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(USER_ID, user_id);
        contentValues.put(GENDER, gender);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(TALUKA_ID, talukaId);
        contentValues.put(AGE_MIN, ageMin);
        contentValues.put(AGE_MAX, ageMax);
        contentValues.put(RELIGION_ID, religionId);
        contentValues.put(CASTE_ID, casteId);
        contentValues.put(SUB_CASTE_ID, subCasteId);
        contentValues.put(HIGHEST_QUALIFICATION_LEVEL_ID, highestQualificationLevelId);
        contentValues.put(HIGHEST_QUALIFICATION_ID, highestQualificationId);
        contentValues.put(MARITAL_STATUS_ID,maritalStatusId );
        contentValues.put(DIET_ID, dietId);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(EXPECTED_INDIVIDUAL_INCOME, expectedIndividualIncome);
        contentValues.put(EXPECTED_FAMILY_INCOME, expectedFamilyIncome);
        contentValues.put(FAMILY_TYPE, familyType);
        contentValues.put(FAMILY_VALUE, familyValue);
        contentValues.put(HEIGHT_MIN, heightMin);
        contentValues.put(HEIGHT_MAX, heightMax);
        contentValues.put(SKIN_COLOR_ID, skinColorId);


        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateSetPreference( String userId, String gender, String stateId, String districtId,
                                    String talukaId, String ageMin, String ageMax, String religionId,
                                    String casteId, String subCasteId, String highestQualificationLevelId,
                                    String highestQualificationId, String maritalStatusId, String dietId,
                                    String occupationId, String expectedIndividualIncome, String expectedFamilyIncome,
                                    String familyType, String familyValue, String heightMin, String heightMax,
                                    String skinColorId) //String id,
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, userId);
        contentValues.put(GENDER, gender);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(TALUKA_ID, talukaId);
        contentValues.put(AGE_MIN, ageMin);
        contentValues.put(AGE_MAX, ageMax);
        contentValues.put(RELIGION_ID, religionId);
        contentValues.put(CASTE_ID, casteId);
        contentValues.put(SUB_CASTE_ID, subCasteId);
        contentValues.put(HIGHEST_QUALIFICATION_LEVEL_ID, highestQualificationLevelId);
        contentValues.put(HIGHEST_QUALIFICATION_ID, highestQualificationId);
        contentValues.put(MARITAL_STATUS_ID,maritalStatusId );
        contentValues.put(DIET_ID, dietId);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(EXPECTED_INDIVIDUAL_INCOME, expectedIndividualIncome);
        contentValues.put(EXPECTED_FAMILY_INCOME, expectedFamilyIncome);
        contentValues.put(FAMILY_TYPE, familyType);
        contentValues.put(FAMILY_VALUE, familyValue);
        contentValues.put(HEIGHT_MIN, heightMin);
        contentValues.put(HEIGHT_MAX, heightMax);
        contentValues.put(SKIN_COLOR_ID, skinColorId);


        return sqLiteDatabase.update(TABLE_NAME, contentValues, USER_ID+" = ? ",
                new String[]{userId});
        //insert returns rowId of newly inserted row or -1 if an errror occured
    }


    public  int numberOfRows()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(sqLiteDatabase, TABLE_NAME);

    }


    public int deleteSetPreferenceById(Integer id)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        //delete method returns no. of rows deleted or 0 otherwise
        return sqLiteDatabase.delete(TABLE_NAME,SQLiteSetPreference.ID+" = ?", new String[]{Integer.toString(id)});


    }


    public int deleteAll()
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        return sqLiteDatabase.delete(TABLE_NAME,"1",null);

    }


}
