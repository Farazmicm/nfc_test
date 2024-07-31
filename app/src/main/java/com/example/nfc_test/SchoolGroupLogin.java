package com.example.nfc_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.android.volley.Request;
import com.example.nfc_test.models.SchoolGroupModel;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class SchoolGroupLogin extends AppCompatActivity {

    WebService webService;
    Button buttonSGC;
    AppCompatEditText txtSchoolGroupCode;
    String schoolGroupCode = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_group_login);

        webService = new WebService();
        buttonSGC = findViewById(R.id.buttonSGC);
        txtSchoolGroupCode = findViewById(R.id.editTextSGC);
        buttonSGC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                schoolGroupCode = txtSchoolGroupCode.getText().toString();
                if (schoolGroupCode.isEmpty()) {
                    txtSchoolGroupCode.setError("This field is required.");
                    androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(SchoolGroupLogin.this, "", "Please enter School Group Code");
                    // Setting OK Button
                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    if (Utility.checkInternet(SchoolGroupLogin.this)) {
                        new WebCall(SchoolGroupLogin.this, (WebCall) null).execute();
                    } else {
                        Utility.openInternetNotAvailable(SchoolGroupLogin.this, "");
                    }
                }
            }
        });
    }

    private class WebCall extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        Map<String, String> params = new HashMap<String, String>();

        private WebCall() {
            this.progressDialog = null;
        }

        /* synthetic */ WebCall(SchoolGroupLogin schoolGroupLogin, WebCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(SchoolGroupLogin.this, "Please wait...", "Processing request..");
            webService.URL = MyVariables.MOBILE_API_URL;
            super.onPreExecute();

        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            String result = webService.sendWebRequest(getApplicationContext(), "Home/GetSchoolGroupDetails?schoolshortcode=" + schoolGroupCode, Request.Method.GET, params, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    if (!result.isEmpty()) {
                        WebCall.this.setResultData(result);
                    } else {
                        progressDialog.dismiss();
                    }
                }
            });
            return result;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String r12) {
            super.onPostExecute(r12);
        }

        public void setResultData(String result) {
            try {
                this.progressDialog.dismiss();
                if (!MyVariables.IsProduction) {
                    Log.i("Api Data", result);
                }
                Gson gson = new Gson();
                SchoolGroupModel schoolGroupModel = gson.fromJson(result, SchoolGroupModel.class);

                if (schoolGroupModel.getClientWEBURL() != null) {
                    String clientUrl = schoolGroupModel.getClientWEBURL().endsWith("/") ? schoolGroupModel.getClientWEBURL() : schoolGroupModel.getClientWEBURL() + "/";
                    MyVariables.SCHOOL_WEB_URL = clientUrl;
                    MyVariables.SCHOOL_GROUP_CODE = schoolGroupModel.getClientShortCode();
                    MyVariables.SCHOOL_GROUP_Name = schoolGroupModel.getClientName();
                    SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_MODEL.toString(), Context.MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_MODEL.toString(), gson.toJson(schoolGroupModel));
                    myEdit.putString(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_CODE.toString(), schoolGroupModel.getClientShortCode());
                    myEdit.putString(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_ID.toString(), "" + schoolGroupModel.getClientID());
                    myEdit.putString(MyVariables.DEFAULT_ENUM.SCHOOL_WEB_URL.toString(), clientUrl);
                    myEdit.commit();
                    if (!MyVariables.IsProduction) {
                        Log.e("Data", gson.toJson(schoolGroupModel));
                    }

                    startActivity(new Intent(SchoolGroupLogin.this, LoginActivity.class));
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid School Group Code", Toast.LENGTH_LONG).show();

                }
            } catch (Exception ex) {
                if (!MyVariables.IsProduction) {
                    Log.e("Errrrr:", ex.toString());
                }
                Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
}