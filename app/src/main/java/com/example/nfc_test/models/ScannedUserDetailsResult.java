package com.example.nfc_test.models;

public class ScannedUserDetailsResult {

    public UserDetailsResult userDetailsResult;
    public Boolean isAttendanceTaken;
    public String attendanceTakenTime;
    public String scannedCard;

    public ScannedUserDetailsResult() {
    }

    public UserDetailsResult getUserDetailsResult() {
        return userDetailsResult;
    }

    public void setUserDetailsResult(UserDetailsResult userDetailsResult) {
        this.userDetailsResult = userDetailsResult;
    }

    public Boolean getAttendanceTaken() {
        return isAttendanceTaken;
    }

    public void setAttendanceTaken(Boolean attendanceTaken) {
        isAttendanceTaken = attendanceTaken;
    }

    public String getAttendanceTakenTime() {
        return attendanceTakenTime;
    }

    public void setAttendanceTakenTime(String attendanceTakenTime) {
        this.attendanceTakenTime = attendanceTakenTime;
    }

    public String getScannedCard() {
        return scannedCard;
    }

    public void setScannedCard(String scannedCard) {
        this.scannedCard = scannedCard;
    }
}