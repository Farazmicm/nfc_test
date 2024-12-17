package com.example.nfc_test.models;

public  class ParentRFID {
        public Integer userRFIDID;
        public Integer userID;
        public String rFID;
        public boolean isDeActive;
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