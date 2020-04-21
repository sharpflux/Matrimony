package com.example.matrimonyapp.volley;


public class URLs {

    public static final String MainURL                          = "http://matrimonial.sharpflux.com/";

    public static final String URL_POST_REGISTRATION            = MainURL+"api/Registration/RegisterUser";
    public static final String URL_POST_LOGIN                   = MainURL+"api/Users/Login";
    public static final String URL_GET_LOGIN                    = MainURL+"api/Users/Login?";
    public static final String URL_POST_UPDATEPASSWORD          = MainURL+"api/UserUdatePasswordAPI/UpdatePasswordApi";


    public static final String URL_GET_STATE                    = MainURL+"api/States/GETSTATE?";
    public static final String URL_GET_DISTRICT                 = MainURL+"api/District/Get_DistrictAPI?";
    public static final String URL_GET_TALUKA                   = MainURL+"api/Talukas/Get_TalukaAPI?";
    public static final String URL_GET_RELIGION                 = MainURL+"api/ReligionAPI/GETReligion?";
    public static final String URL_GET_CASTE                    = MainURL+"api/CasteApi/Get_CasteAPI?";
    public static final String URL_GET_OCCUPATION               = MainURL+"api/OccupationAPI/GETOccupation?";
    public static final String URL_POST_VERIFYMOBILE            = MainURL+"/User_Authentication/GetUserFromMobileNo";
    public static final String URL_GET_OTP                      = MainURL+"User_Authentication/GetOTP?";
    public static final String URL_GET_SKINCOLOR                = MainURL+"api/SkinColourApi/Get_SkinColourAPI?";
    public static final String URL_GET_MARITALSTATUS            = MainURL+"api/MaritalStatusApi/Get_MaritalStatusAPI?";
    public static final String URL_GET_FAMILYSTATUS             = MainURL+"api/FamilyStatusApi/Get_FamilyStatusAPI?";
    public static final String URL_GET_FAMILYTYPE               = MainURL+"api/FamilyTypeApi/Get_FamilyTypeAPI?";
    public static final String URL_GET_FAMILYVALUES             = MainURL+"api/FamilyValuesApi/Get_FamilyValuesAPI?";
    public static final String URL_GET_SIBLINGSLIST             = MainURL+"api/SiblingListApi/Get_SiblingListAPI?";
    public static final String URL_GET_QUALIFICATIONNAME        = MainURL+"api/OccupationAPI/GETQualificationAPI?";
    public static final String URL_GET_QUALIFICATIONLEVEL       = MainURL+"api/QualificationLevelApi/Get_QualificationLevelAPI?";
    public static final String URL_GET_DESIGNATION              = MainURL+"api/DesignationApi/Get_DesignationAPI?";

    public static final String URL_GET_REGISTEREDUSER           = MainURL+"api/Registration/GetRegisterUserAPI?";

    public static final String URL_GET_BASICDETAILS             = MainURL+"api/Registration/GetUserBasicDetailsAPI?";
    public static final String URL_POST_BASICDETAILS            = MainURL+"/api/BasicDetailsAPI/BasicDetailsInsertUpdate";

    public static final String URL_GET_RELIGIONDETAIL           = MainURL+"api/ReligiousDetailsAPI/GetUserReligiousDetailsAPI?";
    public static final String URL_POST_RELIGIONDETAIL          = MainURL+"api/ReligiousDetailsAPI/ReligiousDetailsInsertUpdate";

    public static final String URL_GET_FAMILYDETAILS            = MainURL+"api/Registration/GetUserFamilyDetailsAPI?";
    public static final String URL_POST_FAMILYDETAILS            = MainURL+"api/FamilyDetailsAPI/FamilyDetailsInsertUpdate";

    public static final String URL_GET_QUALIFICATIONDETAILS     = MainURL+"api/QualificationDetailsAPI/GetUserQualificationDetailsAPI?";
    public static final String URL_POST_QUALIFICATIONDETAILS    = MainURL+"api/QualificationDetailsAPI/QualificationDetailsInsertUpdate";

    public static final String URL_GET_PROFESSIONALDETAILS      = MainURL+"api/ProfessionalDetailsAPI/GetUserProfessionalDetailsAPI?";
    public static final String URL_POST_PROFESSIONALDETAILS     = MainURL+"api/ProfessionalDetailsAPI/ProfessionalDetailsInsertUpdate";

    public static final String URL_GET_PERSONALDETAILS          = MainURL+"api/PersonalDetailsAPI/GetUserPersonalDetailsAPI?";
    public static final String URL_POST_PERSONALDETAILS         = MainURL+"api/PersonalDetailsAPI/PersonalDetailsInsertUpdate";

    public static final String URL_GET_ALL_QUALIFICATIONNAME    = MainURL+"api/OccupationAPI/GETAllQualificationAPI?";

    public static final String URL_GET_MULTIPLE_DISTRICT        = MainURL+"api/DistrictMultipleApi/Get_DistrictMultipleAPI?";


    public static final String URL_GET_MULTIPLE_CASTE       = MainURL+"api/CasteApi/Get_CasteMultipleAPI?";



}