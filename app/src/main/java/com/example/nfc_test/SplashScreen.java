package com.example.nfc_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

//import com.example.nfc_test.R;
import com.example.nfc_test.models.LoginModel;
import com.example.nfc_test.models.SchoolGroupModel;
import com.google.gson.Gson;
import com.scottyab.rootbeer.RootBeer;

import java.util.HashMap;
import java.util.Map;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 5000;
    protected boolean _active = true;
    protected int _splashTime = 3000;
    String username = "", password = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try{
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash_screen);

            //Log.v("Rooted","" + RootDetection.isRooted());
            RootBeer rootBeer = new RootBeer(this);
            if (false) {//rootBeer.isRooted() == false
                //we found indication of root
                androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(this, "", "Your device is rooted or some insecure settings are on.\nPlease turn it off.");
                assert alertDialog != null;
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        finish();
                    }
                });
                alertDialog.show();
            } else {
                //we didn't find indication of root
                if (checkInternet()) {
                    SpScreen();
                } else {
                    open();
                }
            }
        }
        catch (Exception e)
        {

        }
    }

    public boolean checkInternet() {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected;
    }

    public void open() {
        androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(this, "", "Internet connection is not available. Please connect to internet.");
        assert alertDialog != null;
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finish();
            }
        });
        alertDialog.show();
    }

    public void SpScreen() {
        Thread splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime)) {
                        sleep(100);
                        if (_active) {
                            waited += 100;
                        }
                    }
                } catch (Exception e) {

                } finally {

                    try {
                        String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                                Settings.Secure.ANDROID_ID);
                        MyVariables.deviceID = android_id;
                        if(!MyVariables.IsProduction){
                            Log.v("Device ID", android_id + "-> Length: " + android_id.length());
                        }
                    } catch (Exception ex) {

                    }


                    try{
                        SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_MODEL.toString(), Context.MODE_PRIVATE);
                        String SCHOOL_GROUP_MODEL = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_MODEL.toString(), "");
                        if (!SCHOOL_GROUP_MODEL.isEmpty()) {
                            String SCHOOL_GROUP_CODE = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_CODE.toString(), "");
                            String SCHOOL_GROUP_ID = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_ID.toString(), "");
                            String SCHOOL_WEB_URL = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.SCHOOL_WEB_URL.toString(), "");
                            Gson gson = new Gson();
                            SchoolGroupModel schoolGroupModel = gson.fromJson(SCHOOL_GROUP_MODEL, SchoolGroupModel.class);


                            MyVariables.SCHOOL_WEB_URL = SCHOOL_WEB_URL;
                            MyVariables.SCHOOL_GROUP_CODE = SCHOOL_GROUP_CODE;
                            MyVariables.SCHOOL_GROUP_ID = Integer.parseInt(SCHOOL_GROUP_ID);
                            MyVariables.SCHOOL_GROUP_Name = schoolGroupModel.getClientName();

                            if (MyVariables.MCAMPUS_TOKENID.isEmpty()) {
                                sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), Context.MODE_PRIVATE);
                                String sToken = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), "");

                                if (sToken == "" || sToken == null) {
                                    String mcampusToken = MyVariables.getMCampusToken(getApplicationContext());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putString(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), mcampusToken);
                                    editor.commit();
                                } else {
                                    MyVariables.MCAMPUS_TOKENID = sToken;
                                }


                                /*  Login Check  */
                                SharedPreferences loginsharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(), Context.MODE_PRIVATE);
                                String loginData = loginsharedPreferences.getString(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(), "");
                                //Log.v("loginData", loginData);
                                if (!loginData.isEmpty()) {

                                    //Perform Login Operation
                                    String uname = loginsharedPreferences.getString(MyVariables.DEFAULT_ENUM.USERNAME.toString(), "");
                                    String pword = loginsharedPreferences.getString(MyVariables.DEFAULT_ENUM.PASSWORD.toString(), "");
                                    if (uname.isEmpty() || pword.isEmpty()) {
                                        LoginModel loginModel = gson.fromJson(loginData, LoginModel.class);
                                        MyVariables.USER_ID = loginModel.getUser().getUserID();
                                        MyVariables.USER_FULL_NAME = loginModel.getUserProfile().getUserFullName();


                                        //get Default Master Cards
                                        SharedPreferences mSharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), Context.MODE_PRIVATE);
                                        String cardData = mSharedPreferences.getString(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), "");
                                        //Log.v("cardddddd", cardData);
                                        if (!cardData.isEmpty()) {
                                            MyVariables.isDefaultCardUse = true;

                                            if (cardData.equals("true")) {
                                                MyVariables.CARD_AUTH_KEY = MyVariables.DEFAULT_CARD_AUTH_KEY;
                                                MyVariables.SECTOR_NUMBER = MyVariables.DEFAULT_SECTOR_NUMBER;
                                                MyVariables.isDefaultCardUse = true;
                                                MyVariables.isMasterCardRead = true;
                                            } else if (cardData.equals("false")) {
                                                MyVariables.isDefaultCardUse = false;
                                                SharedPreferences kSharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.MASTER_CARD_DATA.toString(), Context.MODE_PRIVATE);
                                                String kCardData = kSharedPreferences.getString(MyVariables.DEFAULT_ENUM.CARD_AUTH_KEY.toString(), "");
                                                String kSectorNo = kSharedPreferences.getString(MyVariables.DEFAULT_ENUM.SECTOR_NUMBER.toString(), "");
                                                if (!kCardData.isEmpty()) {
                                                    byte[] key = new byte[6];
                                                    key = Utility.binaryToBytes(Utility.hexToBinary(kCardData));
                                                    MyVariables.CARD_AUTH_KEY = key;
                                                    MyVariables.SECTOR_NUMBER = Integer.parseInt(kSectorNo);
                                                    MyVariables.isMasterCardRead = true;
                                                } else {
                                                    MyVariables.isMasterCardRead = false;
                                                }
                                            }
                                        } else {
                                            MyVariables.CARD_AUTH_KEY = MyVariables.DEFAULT_CARD_AUTH_KEY;
                                            MyVariables.SECTOR_NUMBER = MyVariables.DEFAULT_SECTOR_NUMBER;
                                            MyVariables.isDefaultCardUse = true;
                                            SharedPreferences.Editor editData = mSharedPreferences.edit();
                                            editData.putString(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), "true");
                                            editData.commit();
                                            MyVariables.isMasterCardRead = true;
                                            //Log.v("KEY", Utility.toReversedHex(MyVariables.CARD_AUTH_KEY));
                                        }
                                        startActivity(new Intent(SplashScreen.this, GenActivity.class));
                                        finish();
                                    } else {
                                        //Log.v("Username:",uname);
                                        //Log.v("Password:",pword);
                                        username = uname;
                                        password = pword;
                                        //loginThread.start();
                                        try {
                                            //this.stop();
                                            Handler mainHandler = new Handler(SplashScreen.this.getMainLooper());
                                            Runnable myRunnable = new Runnable() {
                                                @Override
                                                public void run() {
                                                    new LoginCall(SplashScreen.this, (LoginCall) null).execute();
                                                }
                                            };
                                                mainHandler.post(myRunnable);

                                        }catch (Exception e) {
                                            if(!MyVariables.IsProduction) {
                                                Log.v("Exxx654646546465465:", e.toString());
                                            }
                                        }
                                    }
                                } else {
                                    startActivity(new Intent(SplashScreen.this, LoginActivity.class));
                                    finish();
                                }
                            }

                        } else {
                            startActivity(new Intent(SplashScreen.this, SchoolGroupLogin.class));
                            finish();
                        }
                    }catch (Exception ex){
                        if(!MyVariables.IsProduction) {
                            Log.v("Exceptionsss:", ex.toString());
                        }
                    }

                }
            }
        };
        splashTread.start();


    }

    private class LoginCall extends AsyncTask<String, Void, String> {
        WebService webService = new WebService();
        ProgressDialog progressDialog;
        Map<String, String> params = new HashMap<String, String>();
        String errorMessage = "";

        private LoginCall() {
            this.progressDialog = null;
        }

        /* synthetic */ LoginCall(SplashScreen loginActivity, LoginCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            //this.progressDialog = ProgressDialog.show(SplashScreen.this, "Please wait...", "We are authenticating you..");
            super.onPreExecute();

            username = SplashScreen.this.username;
            password = SplashScreen.this.password;
            //Log.v("Token:",MyVariables.MCAMPUS_TOKENID);
            params.put("SiteUserName", username);
            params.put("SitePassword", password);
            params.put("RememberMe", "false");
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            try {
                webService.sendPostRequest("/Security/Login/MCampusLoginVerify", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            LoginCall.this.setResultData(result);
                        }
                        catch(Exception e)
                        {

                        }
                    }
                });
                return "";
            } catch (Exception ex) {
                if(!MyVariables.IsProduction) {
                    Log.v("Error", ex.toString());
                    errorMessage = ex.toString();
                }

                String result = webService.sendPostRequest("/Security/Login/MCampusLoginVerify", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            LoginCall.this.setResultData(result);
                        }
                        catch(Exception e)
                        {

                        }
                    }
                });
                return result;
            }

        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String r12) {
            super.onPostExecute(r12);
        }

        public void setResultData(String result) {

            try {
                //this.progressDialog.dismiss();
                if (errorMessage != "") {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(SplashScreen.this, "", "Error: " + errorMessage);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            alertDialog.show();
                        }
                    });
                    return;
                }
                //Log.i("Login Api Data", result);
                if (result.contains("Exception")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(SplashScreen.this, "", "Alert: " + result);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                }
                            });
                            alertDialog.show();
                        }
                    });
                    startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                    finish();
                } else {
                    Gson gson = new Gson();
                    LoginModel loginModel= gson.fromJson(result, LoginModel.class);
                    String responseMessage = "";
                    responseMessage = loginModel.getResponseMessage();
                    if(loginModel.getSuccess()) {
                        if(loginModel.getTokenID() != null) {
                            //Log.i("Model", gson.toJson(loginModel));
                            MyVariables.USER_ID = loginModel.getUser().getUserID();
                            MyVariables.USER_FULL_NAME = loginModel.getUserProfile().getUserFullName();
                            SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(),gson.toJson(loginModel));
                            myEdit.putString(MyVariables.DEFAULT_ENUM.USERNAME.toString(),username);
                            myEdit.putString(MyVariables.DEFAULT_ENUM.PASSWORD.toString(),password);
                            myEdit.commit();

                            SharedPreferences mSharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), Context.MODE_PRIVATE);
                            String cardData = mSharedPreferences.getString(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(),"");

                            if(!cardData.isEmpty()) {
                                MyVariables.isDefaultCardUse = true;
                                if(cardData.equals("true")) {
                                    MyVariables.CARD_AUTH_KEY = MyVariables.DEFAULT_CARD_AUTH_KEY;
                                    MyVariables.SECTOR_NUMBER = MyVariables.DEFAULT_SECTOR_NUMBER;
                                    MyVariables.isDefaultCardUse = true;
                                    MyVariables.isMasterCardRead = true;
                                } else if(cardData.equals("false")) {
                                    MyVariables.isDefaultCardUse = false;
                                    SharedPreferences kSharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.MASTER_CARD_DATA.toString(), Context.MODE_PRIVATE);
                                    String kCardData = kSharedPreferences.getString(MyVariables.DEFAULT_ENUM.CARD_AUTH_KEY.toString(),"");
                                    String kSectorNo = kSharedPreferences.getString(MyVariables.DEFAULT_ENUM.SECTOR_NUMBER.toString(),"");
                                    if(!kCardData.isEmpty()) {
                                        byte[] key = new byte[6];
                                        key = Utility.binaryToBytes(Utility.hexToBinary(kCardData));
                                        MyVariables.CARD_AUTH_KEY = key;
                                        MyVariables.SECTOR_NUMBER = Integer.parseInt(kSectorNo);
                                        MyVariables.isMasterCardRead = true;
                                    } else {
                                        MyVariables.isMasterCardRead = false;
                                    }
                                }
                            } else {
                                MyVariables.CARD_AUTH_KEY = MyVariables.DEFAULT_CARD_AUTH_KEY;
                                MyVariables.SECTOR_NUMBER = MyVariables.DEFAULT_SECTOR_NUMBER;
                                MyVariables.isDefaultCardUse = true;
                                SharedPreferences.Editor editData =  mSharedPreferences.edit();
                                editData.putString(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(),"true");
                                editData.commit();
                                MyVariables.isMasterCardRead = true;
                                //Log.v("KEY",Utility.toReversedHex(MyVariables.CARD_AUTH_KEY));
                            }

                            startActivity(new Intent(SplashScreen.this,GenActivity.class));
                            finish();
                        } else {
                            if(!MyVariables.IsProduction){
                                Log.i("Model","null");
                            }
                        }
                    } else {
                        String resMessage = responseMessage;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                androidx.appcompat.app.AlertDialog alertDialog =  MyVariables.getDefaultDialog(SplashScreen.this,"","Alert: "+ resMessage );
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE,"Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }
                }
            } catch (Exception ex) {
                if(!MyVariables.IsProduction){
                    Log.e("Errrrr:", ex.toString());
                }
                startActivity(new Intent(SplashScreen.this, LoginActivity.class));
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(SplashScreen.this, "", "Errrrr: " + ex.toString());
//                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//                            }
//                        });
//                        alertDialog.show();
//                    }
//                });
                //startActivity(new Intent(SplashScreen.this, SplashScreen.class));
                //finish();
            }
        }
    }
}