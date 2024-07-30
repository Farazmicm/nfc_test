package com.example.nfc_test;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.example.nfc_test.models.UserDetailsResult;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentData extends AppCompatActivity {

    Button btnGetData;
    TextView student_data;
    WebService webService;
    ListView listViewStudentData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_data);

        student_data = findViewById(R.id.student_data);
        listViewStudentData = findViewById(R.id.listViewStudentData);
        webService = new WebService();

        findViewById(R.id.btnGetData).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new WebCall(StudentData.this, (WebCall) null).execute();
            }
        });
    }

    private class WebCall extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        private WebCall() {
            this.progressDialog = null;
        }

        /* synthetic */ WebCall(StudentData studentDataActivity, WebCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(StudentData.this, "Please wait...", "We are fetching data..");
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            Map<String,String> params = new HashMap<String, String>();
            String result = webService.sendWebRequest(getApplicationContext(), "/ExternalAPI/GetAllUsers", Request.Method.GET,params,new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    WebCall.this.setResultData(result);
                    if (!MyVariables.IsProduction) {
                        Log.i("Api Data", result);
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
                final ObjectMapper mapper = new ObjectMapper();
                List<UserDetailsResult> participantJsonList = mapper.readValue(result, new TypeReference<List<UserDetailsResult>>() {
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    participantJsonList.removeIf(filter -> filter.UserName.equalsIgnoreCase("mobileverifieduser") || filter.UserFullName == "" || filter.UserFullName == null);
                }
                StudentData.this.student_data.setText("Total: " + participantJsonList.size());

                SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(), Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(),result);
                myEdit.commit();
                //StudentData.this.student_data.append(result);

                //TODO
                StudentDataAdapter adapter = new StudentDataAdapter(getApplicationContext(), R.id.lblTItle, participantJsonList);
                listViewStudentData.setAdapter(adapter);

            } catch (Exception ex) {
                if (!MyVariables.IsProduction) {
                    Log.e("Errrrr:", ex.toString());
                    Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }

    }
}



