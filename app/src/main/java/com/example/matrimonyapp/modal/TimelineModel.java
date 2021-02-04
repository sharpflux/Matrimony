package com.example.matrimonyapp.modal;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.example.matrimonyapp.R;

public class TimelineModel {

    String userId;
    String userName;
    String userQualification;
    String userAge;
    String userBio;
    String userBirthday;
    String userMobileNo;
    String userOccupation;
    String userCompany;
    String userEmail;
    String userHeight;
    String userCity;
    String userReligion;
    String userMaritalStatus;
    String interested;
    String rejected;
    String favorites;

    //Uri profilePic;
    String profilePic;
    Context context;

    public TimelineModel()
    {}

    public TimelineModel(Context context) {
        //this.context = context;
        this.userId = "";
        this.userName = "";
        this.userAge = "";
        this.userQualification = "";
        this.userBio = "";
        //this.profilePic = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+context.getResources().getResourcePackageName(R.drawable.flower2)
        //        +"/"+context.getResources().getResourceTypeName(R.drawable.flower2)
        //        +"/"+context.getResources().getResourceEntryName(R.drawable.flower2));
    }

    public TimelineModel(String userId, String userName, String userQualification, String userAge,
                         String userBio, String userBirthday, String userMobileNo, String userOccupation,
                         String userCompany, String userEmail, String userHeight, String userCity,
                         String userReligion, String userMaritalStatus, String profilePic, Context context) {
        this.userId = userId;
        this.userName = userName;
        this.userQualification = userQualification;
        this.userAge = userAge;
        this.userBio = userBio;
        this.userBirthday = userBirthday;
        this.userMobileNo = userMobileNo;
        this.userOccupation = userOccupation;
        this.userCompany = userCompany;
        this.userEmail = userEmail;
        this.userHeight = userHeight;
        this.userCity = userCity;
        this.userReligion = userReligion;
        this.userMaritalStatus = userMaritalStatus;
        this.profilePic = profilePic;
        this.context = context;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserQualification() {
        return userQualification;
    }

    public void setUserQualification(String userQualification) {
        this.userQualification = userQualification;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserBio() {
        return userBio;
    }

    public void setUserBio(String userBio) {
        this.userBio = userBio;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserMobileNo() {
        return userMobileNo;
    }

    public void setUserMobileNo(String userMobileNo) {
        this.userMobileNo = userMobileNo;
    }

    public String getUserOccupation() {
        return userOccupation;
    }

    public void setUserOccupation(String userOccupation) {
        this.userOccupation = userOccupation;
    }

    public String getUserCompany() {
        return userCompany;
    }

    public void setUserCompany(String userCompany) {
        this.userCompany = userCompany;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUserHeight() {
        return userHeight;
    }

    public void setUserHeight(String userHeight) {
        this.userHeight = userHeight;
    }

    public String getUserCity() {
        return userCity;
    }

    public void setUserCity(String userCity) {
        this.userCity = userCity;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUserReligion() {
        return userReligion;
    }

    public void setUserReligion(String userReligion) {
        this.userReligion = userReligion;
    }

    public String getUserMaritalStatus() {
        return userMaritalStatus;
    }

    public void setUserMaritalStatus(String userMaritalStatus) {
        this.userMaritalStatus = userMaritalStatus;
    }

    public String getInterested() {
        return interested;
    }

    public void setInterested(String interested) {
        this.interested = interested;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getFavorites() {
        return favorites;
    }

    public void setFavorites(String favorites) {
        this.favorites = favorites;
    }
}
