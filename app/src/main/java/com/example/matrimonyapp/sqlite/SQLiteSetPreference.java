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
    public static final String  STATE = "state";
    public static final String  DISTRICT = "district";
    public static final String  TALUKA = "taluka";
    public static final String  STATE_ID = "state_id";
    public static final String  DISTRICT_ID = "district_id";
    public static final String  TALUKA_ID = "taluka_id";
    public static final String  WORKING_STATE = "working_state";
    public static final String  WORKING_DISTRICT = "working_district";
    public static final String  WORKING_TALUKA = "working_taluka";
    public static final String  WORKING_STATE_ID = "working_state_id";
    public static final String  WORKING_DISTRICT_ID = "working_district_id";
    public static final String  WORKING_TALUKA_ID = "working_taluka_id";
    public static final String  AGE_MIN = "age_min";
    public static final String  AGE_MAX = "age_max";
    public static final String  RELIGION = "religion";
    public static final String  RELIGION_ID = "religion_id";
    public static final String  CASTE = "caste";
    public static final String  CASTE_ID = "caste_id";
    public static final String  SUB_CASTE = "sub_caste";
    public static final String  SUB_CASTE_ID = "sub_caste_id";
    public static final String  HIGHEST_QUALIFICATION_LEVEL = "highest_qualification_level";
    public static final String  HIGHEST_QUALIFICATION_LEVEL_ID = "highest_qualification_level_id";
    public static final String  HIGHEST_QUALIFICATION = "highest_qualification";
    public static final String  HIGHEST_QUALIFICATION_ID = "highest_qualification_id";
    public static final String  MARITAL_STATUS = "marital_status";
    public static final String  MARITAL_STATUS_ID = "marital_status_id";
    public static final String  DIET = "diet";
    public static final String  DIET_ID = "diet_id";
    public static final String  OCCUPATION = "occupation";
    public static final String  OCCUPATION_ID = "occupation_id";
    public static final String  EXPECTED_INDIVIDUAL_INCOME = "expected_individual_income";
    public static final String  EXPECTED_INDIVIDUAL_INCOME_ID = "expected_individual_income_id";
    public static final String  EXPECTED_FAMILY_INCOME = "expected_family_income";
    public static final String  EXPECTED_FAMILY_INCOME_ID = "expected_family_income_id";
    public static final String  FAMILY_TYPE = "family_type";
    public static final String  FAMILY_TYPE_ID = "family_type_id";
    public static final String  FAMILY_VALUE = "family_value";
    public static final String  FAMILY_VALUE_ID = "family_value_id";
    public static final String  HEIGHT_MIN = "height_min";
    public static final String  HEIGHT_MAX = "height_max";
    public static final String  SKIN_COLOR = "skin_color";
    public static final String  SKIN_COLOR_ID = "skin_color_id";
    public static final String  SERVICE_TYPE = "service_type";
    public static final String  WORKING_LOCATION = "working_location";
    public static final String  JOB_TYPE = "job_type";



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
                    + STATE + " text, "
                    + DISTRICT + " text, "
                    + TALUKA + " text, "
                    + STATE_ID + " text, "
                    + DISTRICT_ID + " text, "
                    + TALUKA_ID + " text, "
                    + WORKING_STATE + " text, "
                    + WORKING_DISTRICT + " text, "
                    + WORKING_TALUKA + " text, "
                    + WORKING_STATE_ID + " text, "
                    + WORKING_DISTRICT_ID + " text, "
                    + WORKING_TALUKA_ID + " text, "
                    + AGE_MIN + " text, "
                    + AGE_MAX + " text, "
                    + RELIGION + " text, "
                    + RELIGION_ID + " text, "
                    + CASTE + " text, "
                    + CASTE_ID + " text, "
                    + SUB_CASTE + " text, "
                    + SUB_CASTE_ID + " text, "
                    + HIGHEST_QUALIFICATION_LEVEL + " text, "
                    + HIGHEST_QUALIFICATION_LEVEL_ID + " text, "
                    + HIGHEST_QUALIFICATION + " text, "
                    + HIGHEST_QUALIFICATION_ID + " text, "
                    + MARITAL_STATUS + " text, "
                    + MARITAL_STATUS_ID + " text, "
                    + DIET + " text, "
                    + DIET_ID + " text, "
                    + SERVICE_TYPE + " text, "
                    + WORKING_LOCATION + " text, "
                    + JOB_TYPE + " text, "
                    + OCCUPATION + " text, "
                    + OCCUPATION_ID + " text, "
                    + EXPECTED_INDIVIDUAL_INCOME + " text, "
                    + EXPECTED_INDIVIDUAL_INCOME_ID + " text, "
                    + EXPECTED_FAMILY_INCOME + " text, "
                    + EXPECTED_FAMILY_INCOME_ID + " text, "
                    + FAMILY_TYPE + " text, "
                    + FAMILY_TYPE_ID + " text, "
                    + FAMILY_VALUE + " text, "
                    + FAMILY_VALUE_ID + " text, "
                    + HEIGHT_MIN + " text, "
                    + HEIGHT_MAX + " text, "
                    + SKIN_COLOR + " text, "
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

    public long insertSetPreference(String user_id, String gender, String state, String district,
                                    String taluka, String stateId, String districtId, String talukaId,
                                    String workingState, String workingDistrict, String workingTaluka,
                                    String workingStateId, String workingDistrictId, String workingTalukaId,
                                    String ageMin, String ageMax, String religion, String religionId,
                                    String caste, String casteId, String subCaste,String subCasteId,
                                    String highestQualificationLevel, String highestQualificationLevelId,
                                    String highestQualification, String highestQualificationId,
                                    String maritalStatus, String maritalStatusId, String diet, String dietId,
                                    String serviceType, String workingLocation, String jobType,
                                    String occupation, String occupationId, String expectedIndividualIncome,
                                    String expectedIndividualIncomeId, String expectedFamilyIncome,
                                    String expectedFamilyIncomeId, String familyType,String familyTypeId,
                                    String familyValue,String familyValueId, String heightMin, String heightMax,
                                    String skinColor,String skinColorId)
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        //contentValues.put(ID, null);
        contentValues.put(USER_ID, user_id);
        contentValues.put(GENDER, gender);
        contentValues.put(STATE, state);
        contentValues.put(DISTRICT, district);
        contentValues.put(TALUKA, taluka);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(TALUKA_ID, talukaId);
        contentValues.put(WORKING_STATE, workingState);
        contentValues.put(WORKING_DISTRICT, workingDistrict);
        contentValues.put(WORKING_TALUKA, workingTaluka);
        contentValues.put(WORKING_STATE_ID, workingStateId);
        contentValues.put(WORKING_DISTRICT_ID, workingDistrictId);
        contentValues.put(WORKING_TALUKA_ID, workingTalukaId);
        contentValues.put(AGE_MIN, ageMin);
        contentValues.put(AGE_MAX, ageMax);
        contentValues.put(RELIGION, religion);
        contentValues.put(RELIGION_ID, religionId);
        contentValues.put(CASTE, caste);
        contentValues.put(CASTE_ID, casteId);
        contentValues.put(SUB_CASTE, subCaste);
        contentValues.put(SUB_CASTE_ID, subCasteId);
        contentValues.put(HIGHEST_QUALIFICATION_LEVEL, highestQualificationLevel);
        contentValues.put(HIGHEST_QUALIFICATION_LEVEL_ID, highestQualificationLevelId);
        contentValues.put(HIGHEST_QUALIFICATION, highestQualification);
        contentValues.put(HIGHEST_QUALIFICATION_ID, highestQualificationId);
        contentValues.put(MARITAL_STATUS,maritalStatus);
        contentValues.put(MARITAL_STATUS_ID,maritalStatusId );
        contentValues.put(DIET, diet);
        contentValues.put(DIET_ID, dietId);
        contentValues.put(SERVICE_TYPE, serviceType);
        contentValues.put(WORKING_LOCATION, workingLocation);
        contentValues.put(JOB_TYPE, jobType);
        contentValues.put(OCCUPATION, occupation);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(EXPECTED_INDIVIDUAL_INCOME, expectedIndividualIncome);
        contentValues.put(EXPECTED_INDIVIDUAL_INCOME_ID, expectedIndividualIncomeId);
        contentValues.put(EXPECTED_FAMILY_INCOME, expectedFamilyIncome);
        contentValues.put(EXPECTED_FAMILY_INCOME_ID, expectedFamilyIncomeId);
        contentValues.put(FAMILY_TYPE, familyType);
        contentValues.put(FAMILY_TYPE_ID, familyTypeId);
        contentValues.put(FAMILY_VALUE, familyValue);
        contentValues.put(FAMILY_VALUE_ID, familyValueId);
        contentValues.put(HEIGHT_MIN, heightMin);
        contentValues.put(HEIGHT_MAX, heightMax);
        contentValues.put(SKIN_COLOR, skinColor);
        contentValues.put(SKIN_COLOR_ID, skinColorId);


        //contentValues.put(, );

        return sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        //insert returns rowId of newly inserted row or -1 if an errror occured


    }


    public int updateSetPreference( String userId, String gender, String state, String district,
                                    String taluka, String stateId, String districtId, String talukaId,
                                    String workingState, String workingDistrict, String workingTaluka,
                                    String workingStateId, String workingDistrictId, String workingTalukaId,
                                    String ageMin, String ageMax, String religion, String religionId,
                                    String caste, String casteId, String subCaste,String subCasteId,
                                    String highestQualificationLevel, String highestQualificationLevelId,
                                    String highestQualification, String highestQualificationId,
                                    String maritalStatus, String maritalStatusId, String diet, String dietId,
                                    String serviceType, String workingLocation, String jobType,
                                    String occupation, String occupationId, String expectedIndividualIncome,
                                    String expectedIndividualIncomeId, String expectedFamilyIncome,
                                    String expectedFamilyIncomeId, String familyType,String familyTypeId,
                                    String familyValue,String familyValueId, String heightMin, String heightMax,
                                    String skinColor,String skinColorId) //String id,
    {

        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_ID, userId);
        contentValues.put(GENDER, gender);
        contentValues.put(STATE, state);
        contentValues.put(DISTRICT, district);
        contentValues.put(TALUKA, taluka);
        contentValues.put(STATE_ID, stateId);
        contentValues.put(DISTRICT_ID, districtId);
        contentValues.put(TALUKA_ID, talukaId);
        contentValues.put(WORKING_STATE, workingState);
        contentValues.put(WORKING_DISTRICT, workingDistrict);
        contentValues.put(WORKING_TALUKA, workingTaluka);
        contentValues.put(WORKING_STATE_ID, workingStateId);
        contentValues.put(WORKING_DISTRICT_ID, workingDistrictId);
        contentValues.put(WORKING_TALUKA_ID, workingTalukaId);
        contentValues.put(AGE_MIN, ageMin);
        contentValues.put(AGE_MAX, ageMax);
        contentValues.put(RELIGION, religion);
        contentValues.put(RELIGION_ID, religionId);
        contentValues.put(CASTE, caste);
        contentValues.put(CASTE_ID, casteId);
        contentValues.put(SUB_CASTE, subCaste);
        contentValues.put(SUB_CASTE_ID, subCasteId);
        contentValues.put(HIGHEST_QUALIFICATION_LEVEL, highestQualificationLevel);
        contentValues.put(HIGHEST_QUALIFICATION_LEVEL_ID, highestQualificationLevelId);
        contentValues.put(HIGHEST_QUALIFICATION, highestQualification);
        contentValues.put(HIGHEST_QUALIFICATION_ID, highestQualificationId);
        contentValues.put(MARITAL_STATUS,maritalStatus);
        contentValues.put(MARITAL_STATUS_ID,maritalStatusId );
        contentValues.put(DIET, diet);
        contentValues.put(DIET_ID, dietId);
        contentValues.put(SERVICE_TYPE, serviceType);
        contentValues.put(WORKING_LOCATION, workingLocation);
        contentValues.put(JOB_TYPE, jobType);
        contentValues.put(OCCUPATION, occupation);
        contentValues.put(OCCUPATION_ID, occupationId);
        contentValues.put(EXPECTED_INDIVIDUAL_INCOME, expectedIndividualIncome);
        contentValues.put(EXPECTED_INDIVIDUAL_INCOME_ID, expectedIndividualIncomeId);
        contentValues.put(EXPECTED_FAMILY_INCOME, expectedFamilyIncome);
        contentValues.put(EXPECTED_FAMILY_INCOME_ID, expectedFamilyIncomeId);
        contentValues.put(FAMILY_TYPE, familyType);
        contentValues.put(FAMILY_TYPE_ID, familyTypeId);
        contentValues.put(FAMILY_VALUE, familyValue);
        contentValues.put(FAMILY_VALUE_ID, familyValueId);
        contentValues.put(HEIGHT_MIN, heightMin);
        contentValues.put(HEIGHT_MAX, heightMax);
        contentValues.put(SKIN_COLOR, skinColor);
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
