package com.example.nfc_test.models;

//*
// {"MB_M_ClientKeyValuePair":[],"MB_M_BBPSHostToHostDetail":[],"ClientID":2024,"ClientName":"MICM Demo School Group","ClientShortCode":"mdsg","ClientWEBURL":"https://test.edusprint.in","ClientGroupImagePath":"https://test.edusprint.in/?imageFileHostedPath=mdsg/sgimg/c8c2b61d-f83c-4288-94fc-22019220c3e7_icon.png","CreatedBy":"SQLEntry","CreatedOn":"2017-10-25T05:07:40","UpdatedBy":"SQLEntry","UpdatedOn":"2021-06-03T12:43:46.4167817","DeletedOn":null,"ExtColBool":null,"ExtColInt":null,"ExtColDateTime":null,"ExtColTextS":null,"ExtColTextM":"https://test.edusprint.in/mdsg","ExtColTextL":null,"GeoLocationWEBURL":"geolocation.mobileapi.edusprint.in","IsMICMGeolocationServer":true,"ClientMobileAppCode":"mdsg","IsGoogleLogin":false}
// *//


public class SchoolGroupModel {
    public int ClientID;
    public String ClientName;
    public String ClientShortCode;
    public String ClientWEBURL;
    public String ClientGroupImagePath;
    public String ExtColTextM;
    public String GeoLocationWEBURL;
    public boolean IsMICMGeolocationServer;
    public String ClientMobileAppCode;
    public boolean IsGoogleLogin;



    public int getClientID() {
        return ClientID;
    }

    public void setClientID(int clientID) {
        ClientID = clientID;
    }

    public String getClientName() {
        return ClientName;
    }

    public void setClientName(String clientName) {
        ClientName = clientName;
    }

    public String getClientShortCode() {
        return ClientShortCode;
    }

    public void setClientShortCode(String clientShortCode) {
        ClientShortCode = clientShortCode;
    }

    public String getClientWEBURL() {
        return ClientWEBURL;
    }

    public void setClientWEBURL(String clientWEBURL) {
        ClientWEBURL = clientWEBURL;
    }

    public String getClientGroupImagePath() {
        return ClientGroupImagePath;
    }

    public void setClientGroupImagePath(String clientGroupImagePath) {
        ClientGroupImagePath = clientGroupImagePath;
    }

    public String getExtColTextM() {
        return ExtColTextM;
    }

    public void setExtColTextM(String extColTextM) {
        ExtColTextM = extColTextM;
    }

    public String getGeoLocationWEBURL() {
        return GeoLocationWEBURL;
    }

    public void setGeoLocationWEBURL(String geoLocationWEBURL) {
        GeoLocationWEBURL = geoLocationWEBURL;
    }

    public boolean isMICMGeolocationServer() {
        return IsMICMGeolocationServer;
    }

    public void setMICMGeolocationServer(boolean MICMGeolocationServer) {
        IsMICMGeolocationServer = MICMGeolocationServer;
    }

    public String getClientMobileAppCode() {
        return ClientMobileAppCode;
    }

    public void setClientMobileAppCode(String clientMobileAppCode) {
        ClientMobileAppCode = clientMobileAppCode;
    }

    public boolean isGoogleLogin() {
        return IsGoogleLogin;
    }

    public void setGoogleLogin(boolean googleLogin) {
        IsGoogleLogin = googleLogin;
    }



}
