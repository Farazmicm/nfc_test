package com.example.nfc_test.models;

public class UserDetailsResult {
    public int UserID;
    public String UserName;
    public String UserFullName;
    public String ContactNo;
    public String UID;
    public String ClassName;
    public String DivisionName;
    public String UserProfileImage;
    public String UserType;
    public String RFIDNumbers;
    public boolean IsActive;
    public boolean IsDenied;
    public String DeniedReason;

    public boolean isDenied() {
        return IsDenied;
    }

    public void setDenied(boolean denied) {
        IsDenied = denied;
    }

    public String getDeniedReason() {
        return DeniedReason;
    }

    public void setDeniedReason(String deniedReason) {
        DeniedReason = deniedReason;
    }

    public String getUserProfileImage() {
        if(UserProfileImage == null) UserProfileImage = "";
        return UserProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        UserProfileImage = userProfileImage;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public int getUserID() {
        return UserID;
    }

    public void setUserID(int userID) {
        UserID = userID;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserFullName() {
        return UserFullName;
    }

    public void setUserFullName(String userFullName) {
        UserFullName = userFullName;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getDivisionName() {
        return DivisionName;
    }

    public void setDivisionName(String divisionName) {
        DivisionName = divisionName;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String userType) {
        UserType = userType;
    }

    public String getRFIDNumbers() {
        return RFIDNumbers;
    }

    public void setRFIDNumbers(String RFIDNumbers) {
        this.RFIDNumbers = RFIDNumbers;
    }

}