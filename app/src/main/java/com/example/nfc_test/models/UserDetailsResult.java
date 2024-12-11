package com.example.nfc_test.models;

import java.util.ArrayList;
import java.util.List;

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
    public boolean isParentRFIDCheckRequired;
    public List<ParentRFID> parentRFIDs = new ArrayList<ParentRFID>();

    public boolean getParentRFIDCheckRequired() {
        return isParentRFIDCheckRequired;
    }

    public void setParentRFIDCheckRequired(boolean parentRFIDCheckRequired) {
        isParentRFIDCheckRequired = parentRFIDCheckRequired;
    }

    public List<ParentRFID> getParentRFIDs() {
        return parentRFIDs;
    }

    public void setParentRFIDs(List<ParentRFID> parentRFIDs) {
        this.parentRFIDs = parentRFIDs;
    }

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

    public class ParentRFID {
        private Integer userRFIDID;
        private Integer userID;
        private String rFID;
        private boolean isDeActive;
        public Integer getUserRFIDID() {
            return userRFIDID;
        }
        public void setUserRFIDID(Integer userRFIDID) {
            this.userRFIDID = userRFIDID;
        }
        public Integer getUserID() {
            return userID;
        }
        public void setUserID(Integer userID) {
            this.userID = userID;
        }
        public String getRFID() {
            return rFID;
        }
        public void setRFID(String rFID) {
            this.rFID = rFID;
        }
        public boolean getIsDeActive() {
            return isDeActive;
        }
        public void setIsDeActive(boolean isDeActive) {
            this.isDeActive = isDeActive;
        }
    }
}