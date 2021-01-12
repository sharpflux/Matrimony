package com.example.matrimonyapp.modal;

public class CandidateEducationModel {

    String LowerQualificationDetailId, QualificationLevelId,  QualificationId, QualificationLevelName, Qualification,NameOfInstitute,Percentage,PassingYear;



    public CandidateEducationModel(String lowerQualificationDetailId, String qualificationLevelId, String qualificationId, String qualificationLevelName,
                                   String qalification,String nameOfInstitute,String percentage,String passingYear )
    {
        this.LowerQualificationDetailId = lowerQualificationDetailId;
        this.QualificationLevelId = qualificationLevelId;
        this.QualificationId = qualificationId;
        this.QualificationLevelName = qualificationLevelName;
        this.Qualification = qalification;
        this.NameOfInstitute = nameOfInstitute;
        this.Percentage = percentage;
        this.PassingYear = passingYear;
    }


    public String getLowerQualificationDetailId() {
        return LowerQualificationDetailId;
    }

    public void setLowerQualificationDetailId(String lowerQualificationDetailId) {
        LowerQualificationDetailId = lowerQualificationDetailId;
    }

    public String getQualificationLevelId() {
        return QualificationLevelId;
    }

    public void setQualificationLevelId(String qualificationLevelId) {
        QualificationLevelId = qualificationLevelId;
    }

    public String getQualificationId() {
        return QualificationId;
    }

    public void setQualificationId(String qualificationId) {
        QualificationId = qualificationId;
    }

    public String getQualificationLevelName() {
        return QualificationLevelName;
    }

    public void setQualificationLevelName(String qualificationLevelName) {
        QualificationLevelName = qualificationLevelName;
    }

    public String getQualification() {
        return Qualification;
    }

    public void setQualification(String qualification) {
        Qualification = qualification;
    }

    public String getNameOfInstitute() {
        return NameOfInstitute;
    }

    public void setNameOfInstitute(String nameOfInstitute) {
        NameOfInstitute = nameOfInstitute;
    }

    public String getPercentage() {
        return Percentage;
    }

    public void setPercentage(String percentage) {
        Percentage = percentage;
    }

    public String getPassingYear() {
        return PassingYear;
    }

    public void setPassingYear(String passingYear) {
        PassingYear = passingYear;
    }
}
