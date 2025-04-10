package com.example.nfc_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;

import com.android.volley.Request;
import com.example.nfc_test.models.LoginModel;
import com.example.nfc_test.models.TokenModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button buttonShowData;
    AppCompatEditText txUserName, txPassword;
    WebService webService;
    TextView schoolGroupName;
    String encryptedPassword = "";
    AppCompatImageView captchaImage;
    AppCompatEditText edtCaptcha;
    TextView btnChangeSchoolGroup;
    AppCompatImageView button_password_toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txUserName = findViewById(R.id.editTextUserName);
        txPassword = findViewById(R.id.editTextPassword);
        edtCaptcha = findViewById(R.id.edtCaptcha);

//        txUserName.setText("CHSH250187");
//        txPassword.setText("Pass@1234");
//        edtCaptcha.setText("1234");
//        if (!MyVariables.IsProduction) {
//            txUserName.setText("liza.patel@micmindia.com,micmadmin");
//            txPassword.setText("L!#aP1910");
//        }

        webService = new WebService();
        schoolGroupName = findViewById(R.id.schoolGroupName);
        btnChangeSchoolGroup = findViewById(R.id.changeSchoolGroup);
        button_password_toggle = findViewById(R.id.button_password_toggle);

        button_password_toggle.setOnClickListener(v -> {
            if (txPassword.getTransformationMethod().getClass().getSimpleName() .equals("PasswordTransformationMethod")) {
                txPassword.setTransformationMethod(new SingleLineTransformationMethod());
                button_password_toggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_on));
            } else {
                txPassword.setTransformationMethod(new PasswordTransformationMethod());
                button_password_toggle.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off));
            }

            txPassword.setSelection(Objects.requireNonNull(txPassword.getText()).length());
        });

        btnChangeSchoolGroup.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SchoolGroupLogin.class));
            finish();
        });

        try {
            if (!MyVariables.SCHOOL_GROUP_Name.isEmpty()) {
                schoolGroupName.setText(MyVariables.SCHOOL_GROUP_Name);
            }
        } catch (Exception e) {
        }

        if (MyVariables.MCAMPUS_TOKENID.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), Context.MODE_PRIVATE);
            String sToken = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), "");
            if (sToken == "" || sToken == null) {
                String mcampusToken = MyVariables.getMCampusToken(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), mcampusToken);
                editor.commit();
            }
        }

        /*if(!MyVariables.MCAMPUS_TOKENID.isEmpty()){
            try{
                String captchaURL = MyVariables.getCaptchaKey(getApplicationContext());

            }catch (Exception ex){
                Log.e("Captcha",ex.toString());
            }
        }*/

//        findViewById(R.id.buttonShowData).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, StudentData.class));
//                finish();
//            }
//        });


        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txUserName.getText().toString().isEmpty())
                    txUserName.setError("Username is required.");

                if (txPassword.getText().toString().isEmpty())
                    txPassword.setError("Password is required.");

                if (edtCaptcha.getText().toString().isEmpty())
                    edtCaptcha.setError("Captcha is required.");

                if (txUserName.getText().toString().isEmpty() || txPassword.getText().toString().isEmpty() || edtCaptcha.getText().toString().isEmpty()) {
                    androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(LoginActivity.this, "", "Username, Password and Captcha are required. Please enter..");
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    });
                    alertDialog.show();
                } else {
                    if (!txPassword.getText().toString().isEmpty() && MyVariables.isValidPassword(txPassword.getText().toString())) {
                        try {
                            if (Utility.checkInternet(LoginActivity.this)) {
                                new WebCall(LoginActivity.this, (WebCall) null).execute();
                            } else {
                                Utility.openInternetNotAvailable(LoginActivity.this, "");
                            }
                        } catch (Exception e) {
                            if (!MyVariables.IsProduction) {
                                Log.v("error: ", e.toString());
                            }
                        }
                    } else {
                        androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(LoginActivity.this, "", "Password must be 8 characters long and at least one uppercase, one lowercase, one number and special characters allowed.");
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                            }
                        });
                        alertDialog.show();
                        txPassword.setError("Password must be 8 characters long and at least one uppercase, one lowercase, one number and special characters allowed");
                    }
                }
            }
        });
        captchaImage = (AppCompatImageView) findViewById(R.id.imageCaptcha);

        LoadCaptchaImage();
//        ResetCaptchaImage();
        findViewById(R.id.btnRefreshCaptcha).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utility.checkInternet(LoginActivity.this)) {
                    ResetCaptchaImage();
                } else {
                    Utility.openInternetNotAvailable(LoginActivity.this, "");
                }
            }
        });
    }

    private void LoadCaptchaImage() {
        try {
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Drawable d = MyVariables.getCaptchaImage();
                        this.setImage(d);
                    } catch (Exception e) {
                        if (!MyVariables.IsProduction) {
                            Log.e("LoadImageThread", e.toString());
                        }
                    }
                }

                private void setImage(Drawable d) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            captchaImage.setImageDrawable(d);
                        }
                    });
                }
            });
            Thread.sleep(1000);
            th.start();
        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.v("CaptchImage", ex.toString());
            }
        }
    }

    private void ResetCaptchaImage() {
        ProgressDialog progressDialog;
        progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "New Captcha is loading...");

        try {

            edtCaptcha.setText("");
            findViewById(R.id.btnRefreshCaptcha).setVisibility(View.GONE);
            Thread th = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Drawable d = MyVariables.getCaptchaImage();
                        this.setImage(d);
                    } catch (Exception e) {
                        if (!MyVariables.IsProduction) {
                            Log.e("LoadImageThread", e.toString());
                        }
                    }
                }

                private void setImage(Drawable d) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            captchaImage.setImageDrawable(d);
                            findViewById(R.id.btnRefreshCaptcha).setVisibility(View.VISIBLE);
                            progressDialog.dismiss();
                        }
                    });
                }
            });

            Thread th1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = MyVariables.CAPTCH_RELOAD_URL;

                    WebService webService = new WebService();

                    Map<String, String> params = new HashMap<String, String>();

                    webService.sendWebRequest(getApplicationContext(), url, Request.Method.GET, params, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Gson gson = new Gson();
                            TokenModel tokenModel = gson.fromJson(result, TokenModel.class);
                            if (tokenModel != null && tokenModel.isSuccess()) {
                                try {
                                    Thread.sleep(1000);
                                    th.start();
                                } catch (InterruptedException e) {
                                    if (!MyVariables.IsProduction) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                findViewById(R.id.btnRefreshCaptcha).setVisibility(View.VISIBLE);
                                progressDialog.dismiss();
                            }
                        }
                    });
                }
            });
            th1.start();
        } catch (Exception ex) {
            findViewById(R.id.btnRefreshCaptcha).setVisibility(View.VISIBLE);
            progressDialog.dismiss();
            if (!MyVariables.IsProduction) {
                Log.v("ResetCaptchImage", ex.toString());
            }
        }
    }

    //*
    // For Login method Call
    // *//
    private class WebCall extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Map<String, String> params = new HashMap<String, String>();
        String errorMessage = "";
        String username = "", password = "", captcha = "";

        private WebCall() {
            this.progressDialog = null;
        }

        /* synthetic */ WebCall(LoginActivity loginActivity, WebCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait...", "We are authenticating you..");
            super.onPreExecute();

            captcha = edtCaptcha.getText().toString();
            username = txUserName.getText().toString();
            password = txPassword.getText().toString();
            params.put("SiteUserName", username);
            params.put("SitePassword", password);
            params.put("LoginCaptcha", captcha);
            params.put("Package", "EdusprintTransport");
            /*try {
                params.put("SitePassword", URLEncoder.encode(encryptedPassword,"UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }*/
            //params.put("SitePassword", AESEnc.encryptPassword(password));
            params.put("RememberMe", "false");
            //params.put("SecureMobileLogin", "1");
            //Log.v("params",encryptedPassword);
            //Log.v("params",params.toString());
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            try {
                webService.sendPostRequest("/Security/Login/MCampusLoginVerify", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        WebCall.this.setResultData(result);
                    }
                });
                return "";
            } catch (Exception ex) {
                if (!MyVariables.IsProduction) {
                    Log.v("Error", ex.toString());
                    errorMessage = "119: " + ex.toString();
                }
                String result = webService.sendPostRequest("/Security/Login/MCampusLoginVerify", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        WebCall.this.setResultData(result);
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
                this.progressDialog.dismiss();
                if (errorMessage != "") {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(LoginActivity.this, "", "Error: " + errorMessage);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ResetCaptchaImage();
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
                            androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(LoginActivity.this, "", "Alert: " + result);
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    ResetCaptchaImage();
                                }
                            });
                            alertDialog.show();
                        }
                    });
                    startActivity(new Intent(LoginActivity.this, SplashScreen.class));
                    finish();
                } else {
                    Gson gson = new Gson();
                    LoginModel loginModel = gson.fromJson(result, LoginModel.class);
                    String responseMessage = "";
                    responseMessage = loginModel.getResponseMessage();
                    if (loginModel.getSuccess()) {
                        if (loginModel.getTokenID() != null) {
                            //Log.i("Model", gson.toJson(loginModel));
                            MyVariables.USER_ID = loginModel.getUser().getUserID();
                            MyVariables.USER_FULL_NAME = loginModel.getUserProfile().getUserFullName();
                            SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(), Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(), gson.toJson(loginModel));
                            myEdit.putString(MyVariables.DEFAULT_ENUM.USERNAME.toString(), username);
                            myEdit.putString(MyVariables.DEFAULT_ENUM.PASSWORD.toString(), password);
                            myEdit.commit();

                            SharedPreferences mSharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), Context.MODE_PRIVATE);
                            String cardData = mSharedPreferences.getString(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), "");

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
                                //Log.v("KEY",Utility.toReversedHex(MyVariables.CARD_AUTH_KEY));
                            }

                            startActivity(new Intent(LoginActivity.this, GenActivity.class));
                            finish();
                        } else {
                            ResetCaptchaImage();
                            if (!MyVariables.IsProduction) {
                                Log.i("Model", "null");
                            }
                        }
                    } else {
                        String resMessage = responseMessage;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(LoginActivity.this, "", "Alert: " + resMessage);
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface arg0, int arg1) {
                                        ResetCaptchaImage();
                                    }
                                });
                                alertDialog.show();
                            }
                        });
                    }
                }
            } catch (Exception ex) {
                if (!MyVariables.IsProduction) {
                    Log.e("Errrrr:", ex.toString());

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(LoginActivity.this, "", "Errrrr: " + ex.toString());
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                //ResetCaptchaImage();
                            }
                        });

                        alertDialog.show();
                    }
                });
                startActivity(new Intent(LoginActivity.this, SplashScreen.class));
                finish();
            }
        }
    }
}