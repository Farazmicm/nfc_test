package com.example.nfc_test.models;

public class ResponseModel {
    private String ResponseData;
    private String ResponseMessage;
    private boolean Success;

    public String getResponseData() {
        return ResponseData;
    }

    public void setResponseData(String responseData) {
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
