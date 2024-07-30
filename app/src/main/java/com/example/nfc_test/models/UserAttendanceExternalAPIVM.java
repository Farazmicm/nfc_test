package com.example.nfc_test.models;

public class UserAttendanceExternalAPIVM
{
    public UserAttendanceExternalAPIVM(String RFID, String deviceID, String attendanceTime, String syncDateTime,
                                       String tripName, String attendancePointName, String attendancePointLatitude, String attendancePointLongitude,
                                       String busNo, boolean isPickUpTrip, boolean isVehicle, String attendanceInOut,boolean IsAllowed, String description) {
        this.RFID = RFID;
        DeviceID = deviceID;
        AttendanceTime = attendanceTime;
        SyncDateTime = syncDateTime;
        TripName = tripName;
        AttendancePointName = attendancePointName;
        AttendancePointLatitude = attendancePointLatitude;
        AttendancePointLongitude = attendancePointLongitude;
        BusNo = busNo;
        IsPickUpTrip = isPickUpTrip;
        IsVehicle = isVehicle;
        AttendanceInOut = attendanceInOut;
        isAllowed = IsAllowed;
        Description = description;
    }

    private boolean isAllowed;
    private String Description;
    private String RFID;
    private String DeviceID;
    private String AttendanceTime;
    private String SyncDateTime;

    private String TripName;
    private String AttendancePointName;
    private String AttendancePointLatitude;
    private String AttendancePointLongitude;

    private String BusNo;
    private boolean IsPickUpTrip;
    private boolean IsVehicle;
    private String AttendanceInOut;

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public String getDeviceID() {
        return DeviceID;
    }

    public void setDeviceID(String deviceID) {
        DeviceID = deviceID;
    }

    public String getAttendanceTime() {
        return AttendanceTime;
    }

    public void setAttendanceTime(String attendanceTime) {
        AttendanceTime = attendanceTime;
    }

    public String getSyncDateTime() {
        return SyncDateTime;
    }

    public void setSyncDateTime(String syncDateTime) {
        SyncDateTime = syncDateTime;
    }

    public String getTripName() {
        return TripName;
    }

    public void setTripName(String tripName) {
        TripName = tripName;
    }

    public String getAttendancePointName() {
        return AttendancePointName;
    }

    public void setAttendancePointName(String attendancePointName) {
        AttendancePointName = attendancePointName;
    }

    public String getAttendancePointLatitude() {
        return AttendancePointLatitude;
    }

    public void setAttendancePointLatitude(String attendancePointLatitude) {
        AttendancePointLatitude = attendancePointLatitude;
    }

    public String getAttendancePointLongitude() {
        return AttendancePointLongitude;
    }

    public void setAttendancePointLongitude(String attendancePointLongitude) {
        AttendancePointLongitude = attendancePointLongitude;
    }

    public String getBusNo() {
        return BusNo;
    }

    public void setBusNo(String busNo) {
        BusNo = busNo;
    }

    public boolean isPickUpTrip() {
        return IsPickUpTrip;
    }

    public void setPickUpTrip(boolean pickUpTrip) {
        IsPickUpTrip = pickUpTrip;
    }

    public boolean isVehicle() {
        return IsVehicle;
    }

    public void setVehicle(boolean vehicle) {
        IsVehicle = vehicle;
    }

    public String getAttendanceInOut() {
        return AttendanceInOut;
    }

    public void setAttendanceInOut(String attendanceInOut) {
        AttendanceInOut = attendanceInOut;
    }


    public boolean isAllowed() {
        return isAllowed;
    }

    public String getDescription() {
        return Description;
    }

    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
