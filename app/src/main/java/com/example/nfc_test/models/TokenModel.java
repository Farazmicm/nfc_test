package com.example.nfc_test.models;

public class TokenModel {
    public boolean Success;
    public String ResponseData;
    public String ResponseMessage;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

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
}
