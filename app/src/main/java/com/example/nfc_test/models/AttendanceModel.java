package com.example.nfc_test.models;

public class AttendanceModel {
    int id;
    String dateTime;
    String scannedCard;
    String className;
    String name;
    String type;
    String status;
    String data;
    String syncType;

    public AttendanceModel() {

    }

    public AttendanceModel(int id, String dateTime, String scannedCard, String className, String name, String type, String status, String data) {
        this.id = id;
        this.dateTime = dateTime;
        this.scannedCard = scannedCard;
        this.className = className;
        this.name = name;
        this.type = type;
        this.status = status;
        this.data = data;
    }

    public AttendanceModel(int id, String dateTime, String scannedCard, String name) {
        this.id = id;
        this.dateTime = dateTime;
        this.scannedCard = scannedCard;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getScannedCard() {
        return scannedCard;
    }

    public void setScannedCard(String scannedCard) {
        this.scannedCard = scannedCard;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getSyncType() {
        return syncType;
    }

    public void setSyncType(String syncType) {
        this.syncType = syncType;
    }
}