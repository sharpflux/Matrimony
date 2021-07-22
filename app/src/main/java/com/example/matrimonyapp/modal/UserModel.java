package com.example.matrimonyapp.modal;

public class UserModel {

    private String userId, fullName, mobileNo, emailId, birthdate, age, gender, profilePic,
            language, userType;

    public UserModel() {
    }

    public UserModel(String userId, String fullName, String mobileNo, String emailId, String birthdate,
                     String age, String gender, String profilePic, String language, String userType) {
        this.userId = userId;
        this.fullName = fullName;
        this.mobileNo = mobileNo;
        this.emailId = emailId;
        this.birthdate = birthdate;
        this.age = age;
        this.gender = gender;
        this.profilePic = profilePic;
        this.language = language;
        this.userType = userType;

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
