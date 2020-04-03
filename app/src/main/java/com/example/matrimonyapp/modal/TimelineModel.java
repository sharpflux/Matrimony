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
    Uri profilePic;
    Context context;


    public TimelineModel(Context context) {
        this.context = context;
        this.userId = "";
        this.userName = "";
        this.userAge = "";
        this.userQualification = "";
        this.userBio = "";
        this.profilePic = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+context.getResources().getResourcePackageName(R.drawable.flower2)
                +"/"+context.getResources().getResourceTypeName(R.drawable.flower2)
                +"/"+context.getResources().getResourceEntryName(R.drawable.flower2));
    }

    public TimelineModel(String userId, String userName, String userAge, String userQualification, String userBio, Uri profilePic, Context context) {
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userQualification = userQualification;
        this.userBio = userBio;
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

    public Uri getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(Uri profilePic) {
        this.profilePic = profilePic;
    }
}
