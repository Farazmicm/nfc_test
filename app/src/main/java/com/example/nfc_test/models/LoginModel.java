package com.example.nfc_test.models;

public class LoginModel {

    private String TokenID;
    private Boolean Success;
    private Integer StatusCode;
    private String ResponseMessage;
    private com.example.nfc_test.models.User User;
    private com.example.nfc_test.models.UserProfile UserProfile;
    private Integer SchoolID;
    private Integer YearMasterID;
    private SchoolListModel SchoolList;

    public String getTokenID() {
        return TokenID;
    }

    public void setTokenID(String tokenID) {
        this.TokenID = tokenID;
    }

    public Boolean getSuccess() {
        return Success;
    }

    public void setSuccess(Boolean success) {
        this.Success = success;
    }

    public Integer getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.StatusCode = statusCode;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.ResponseMessage = responseMessage;
    }

    public User getUser() {
        return User;
    }

    public void setUser(User user) {
        this.User = user;
    }

    public UserProfile getUserProfile() {
        return UserProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.UserProfile = userProfile;
    }

    public Integer getSchoolID() {
        return SchoolID;
    }

    public void setSchoolID(Integer schoolID) {
        this.SchoolID = schoolID;
    }

    public Integer getYearMasterID() {
        return YearMasterID;
    }

    public void setYearMasterID(Integer yearMasterID) {
        this.YearMasterID = yearMasterID;
    }

    public SchoolListModel getSchoolList() {
        return SchoolList;
    }

    public void setSchoolList(SchoolListModel schoolList) {
        this.SchoolList = schoolList;
    }

}

