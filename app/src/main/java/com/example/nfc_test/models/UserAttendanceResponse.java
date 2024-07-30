package com.example.nfc_test.models;

import java.util.ArrayList;
import java.util.List;

public class UserAttendanceResponse {
    private List<UserAttendanceDataPushResult> ResponseData;
    private String ResponseMessage;
    private boolean Success;

    public List<UserAttendanceDataPushResult> getResponseData() {
        return ResponseData;
    }

    public void setResponseData(List<UserAttendanceDataPushResult> responseData) {
        if(responseData == null) responseData = new ArrayList<UserAttendanceDataPushResult>();
        ResponseData = responseData;
    }

    public String getResponseMessage() {
        return ResponseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        ResponseMessage = responseMessage;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }
}
