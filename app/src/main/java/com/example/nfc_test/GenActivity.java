package com.example.nfc_test;


import static com.example.nfc_test.MyVariables.SCHOOL_GROUP_CODE;
import static com.example.nfc_test.Utility.getDateDiff;
import static com.example.nfc_test.Utility.toDec;
import static com.example.nfc_test.Utility.toHex;
import static com.example.nfc_test.Utility.toReversedDec;
import static com.example.nfc_test.Utility.toReversedHex;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.example.nfc_test.db.DatabaseHandler;
import com.example.nfc_test.models.AttendanceModel;
import com.example.nfc_test.models.ParentRFID;
import com.example.nfc_test.models.ResponseModel;
import com.example.nfc_test.models.ScannedUserDetailsResult;
import com.example.nfc_test.models.UserAttendanceDataPushResult;
import com.example.nfc_test.models.UserAttendanceExternalAPIVM;
import com.example.nfc_test.models.UserAttendanceResponse;
import com.example.nfc_test.models.UserDetailsResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.ChipGroup;
import com.google.gson.Gson;
import com.opencsv.CSVWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class GenActivity extends AppCompatActivity {

    boolean flag = false;
    IntentFilter[] intentFiltersArray;
    IntentFilter mifare;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    String[][] techListsArray;
    Button resetMCard;
    WebService webService;
    ListView listViewStudentData;

    ProgressDialog progressD;
    TextView api_data;
    ResponseModel responseModel2;
    List<UserDetailsResult> finaluserDetailsResults;
    List<UserDetailsResult> participantJsonList;
    Toolbar toolbar;


    StudentDataRecyclerViewBind recyclerViewBind;
    ScannedDataRecyclerViewBind scannedDataRecyclerViewBind;
    RecyclerView recyclerView, scannedRCV;
    ClickListener listener;
    boolean isShowStudentData = false;

    ToastHandler mToastHandler;
    LinearLayout lySearchContainer;
    TextView reading_data, lblDataTitle;
    AppCompatEditText editTextSearchText;


    //Location
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;

    String lattitude, longtitude = "";


    //Transport Mode
    ChipGroup chipGroup;
    String deviceType = "bus";
    Spinner drpTripRoute;
    Switch busSwitch;
    Switch gateSwitch;
    DatabaseHandler db;
    ArrayList<AttendanceModel> getAttendanceList = new ArrayList<>();
    ArrayList<AttendanceModel> getAttendanceLogList = new ArrayList<>();
    private static final int REQUEST_PERMISSION_CODE = 1;
    private static final int REQUEST_CODE_MANAGE_EXTERNAL_STORAGE = 101;
    AlertDialog tapAlertDialog;
    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gen);
        db = new DatabaseHandler(GenActivity.this);
        tapAlertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "");
////        db.deleteAttendanceAllRecords();
//        Gson gson = new Gson();
//        List<UserAttendanceExternalAPIVM> externalAPIVMList = new ArrayList<UserAttendanceExternalAPIVM>();
//        UserAttendanceExternalAPIVM externalAPIVM = new UserAttendanceExternalAPIVM("1", MyVariables.deviceID, "2023-10-10", "10:10:00", "Default", "Car", lattitude, longtitude, "10", true, true, "In", false, "reason");
//        externalAPIVMList.add(externalAPIVM);
//        String json = gson.toJson(externalAPIVMList);
//        List<UserAttendanceExternalAPIVM> getExternalAPIVMList = gson.fromJson(json, new TypeToken<List<UserAttendanceExternalAPIVM>>() {
//        }.getType());//
//        db.addAttendance(new AttendanceModel(1, "2023-10-10", "strCardNumber1", "sds1", "sds1", "BUS", "200", json));
//        db.addAttendance(new AttendanceModel(2, "2023-10-18", "strCardNumber2", "sds2", "sds2", "BUS", "Error", json));
//        db.addAttendance(new AttendanceModel(3, "2023-10-12", "strCardNumber3", "sds3", "sds3", "GATE", "200", json));
//        db.addAttendance(new AttendanceModel(4, "2023-10-18", "strCardNumber4", "sds4", "sds4", "BUS", "Error", json));
//
////        db.addAttendanceLog(new AttendanceModel(1, "2023-10-10", "200", "200"));
////        db.addAttendanceLog(new AttendanceModel(2, "2023-10-13", "201", "200"));
////        db.addAttendanceLog(new AttendanceModel(3, "2023-10-18","202", "Error"));

        getScannedAttendanceList();
//      getScannedAttendanceLogList();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            this.pendingIntent = PendingIntent.getActivity(GenActivity.this, 0, new Intent(GenActivity.this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_MUTABLE);
            this.intentFiltersArray = new IntentFilter[]{new IntentFilter("android.nfc.action.TECH_DISCOVERED")};
            this.techListsArray = new String[][]{new String[]{NfcA.class.getName()}, new String[]{MifareClassic.class.getName()}};
            this.resetMCard = findViewById(R.id.resetMCard);
            this.api_data = findViewById(R.id.api_data);
            this.reading_data = findViewById(R.id.reading_data);
            recyclerView = (RecyclerView) findViewById(R.id.rcvStudentData);
            this.lySearchContainer = findViewById(R.id.lySearchContainer);
            this.lblDataTitle = findViewById(R.id.lblDataTitle);
            listViewStudentData = findViewById(R.id.listViewStudentData);
            responseModel2 = new ResponseModel();
            finaluserDetailsResults = new ArrayList<UserDetailsResult>();
            this.editTextSearchText = findViewById(R.id.editTextSearchText);

            this.scannedRCV = (RecyclerView) findViewById(R.id.rcvScannedData);

            LinearLayout lnUserInformation = findViewById(R.id.lnUserInformation);
            TextView scanned_data = findViewById(R.id.scanned_data);
            webService = new WebService();

            mToastHandler = new ToastHandler(this);
            if (!MyVariables.USER_FULL_NAME.isEmpty()) {
                String displayText = MyVariables.USER_FULL_NAME;
                if (!MyVariables.deviceID.isEmpty())
                    displayText += "\n(" + MyVariables.deviceID + ")";
                ((TextView) findViewById(R.id.userName)).setText(displayText);
            }

            SharedPreferences mSharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), MODE_PRIVATE);
            String cardData = mSharedPreferences.getString(MyVariables.DEFAULT_ENUM.ISDEFAULTMASTERCARDUSE.toString(), "");
            //Log.v("cardddddd2222222222", cardData);
            if (!cardData.isEmpty()) {
                MyVariables.isMasterCardRead = cardData.equals("true");
            }
            //Log.v("MyVariables", "" + MyVariables.isMasterCardRead);
            //Log.v("MyVariables", "" + MyVariables.SECTOR_NUMBER);
            if (MyVariables.isMasterCardRead) {
                reading_data.setText("Start taking attendance.");
            } else {
                reading_data.setText("Tap Master Card to Proceed for attendance");
            }
//            checkUserRFIDAvailable();
            clearRFID();

            if (!MyVariables.lstScannedUsers.isEmpty()) {
                displayScannedDetails();
            }
            findViewById(R.id.fabViewData).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //MyVariables.CARD_AUTH_KEY == temp || MyVariables.SECTOR_NUMBER == 0
                    if (Utility.checkInternet(GenActivity.this)) {
                        //isShowStudentData = true;
                        new StudentDataCall(GenActivity.this, (StudentDataCall) null).execute();
                        //LinearLayout studentDataContainer = (LinearLayout) findViewById(R.id.studentDataContainer);
                        //studentDataContainer.setVisibility(View.VISIBLE);
                       /*if (studentDataContainer.getVisibility() == View.VISIBLE) {
                           studentDataContainer.setVisibility(View.GONE);
                       } else {
                           studentDataContainer.setVisibility(View.VISIBLE);
                       }*/
                    } else {
                        Utility.openInternetNotAvailable(GenActivity.this, "");
                    }
                }
            });

            findViewById(R.id.fabQrScan).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(GenActivity.this, QrScanActivity.class));
                }
            });

            findViewById(R.id.btnUserSearch).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSearchedUsers(editTextSearchText.getText().toString());
                }
            });

            editTextSearchText.addTextChangedListener(new TextWatcher() {
                public void afterTextChanged(Editable s) {
                    if (s.length() >= 2) {
                        showSearchedUsers(s.toString());
                        if (!MyVariables.IsProduction) {
                            Log.v("afterTextChanged", s.toString());
                        }
                    }
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }
            });

           /*findViewById(R.id.fabRefreshMasterCard).setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   MyVariables.CARD_AUTH_KEY = new byte[6];
                   MyVariables.SECTOR_NUMBER = 0;
                   reading_data.setText("Data reset successfully. Now tap Master Card.");
                   new KeyGenerator().isStudent = false;
                   lnUserInformation.setVisibility(View.GONE);
                   MyVariables.scanedCard = new ArrayList<String>();
                   scanned_data.setText("");
               }
           });*/

            busSwitch = findViewById(R.id.busSwitch);
            gateSwitch = findViewById(R.id.gateSwitch);
            SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.DEVICE_TYPE.toString(), MODE_PRIVATE);
            String dType = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.DEVICE_TYPE.toString(), "");
            deviceType = !dType.isEmpty() ? dType : "bus";
            saveDeviceType();

            if (Objects.equals(deviceType, "bus")) {
                busSwitch.setChecked(true);
            } else {
                gateSwitch.setChecked(true);
            }

            busSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Utility.checkInternet(GenActivity.this)) {
                    if (isChecked) {
                        deviceType = "bus";
                        gateSwitch.setChecked(false);
                        saveDeviceType();
                    } else {
                        deviceType = "gate";
                        gateSwitch.setChecked(true);
                        saveDeviceType();
                    }
                    new StudentDataCall(GenActivity.this, (StudentDataCall) null).execute();
                } else {
                    busSwitch.setChecked(!isChecked);
                    Utility.openInternetNotAvailable(GenActivity.this, "");
                }
            });

            gateSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (Utility.checkInternet(GenActivity.this)) {
                    if (isChecked) {
                        deviceType = "gate";
                        busSwitch.setChecked(false);
                        saveDeviceType();
                    } else {
                        deviceType = "bus";
                        busSwitch.setChecked(true);
                        saveDeviceType();
                    }
                    new StudentDataCall(GenActivity.this, (StudentDataCall) null).execute();
                } else {
                    gateSwitch.setChecked(!isChecked);
                    Utility.openInternetNotAvailable(GenActivity.this, "");
                }
            });

            if (Utility.checkInternet(GenActivity.this)) {
                new StudentDataCall(this, (StudentDataCall) null).execute();
            } else {
                Utility.openInternetNotAvailable(GenActivity.this, "");
            }

            try {
                //Location
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                if (mFusedLocationClient != null) {
                    getLastLocation();
                }
            } catch (Exception ex) {

            }


//            chipGroup = findViewById(R.id.chipGroup);
//            chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(ChipGroup group, int checkedId) {
//                    Chip selectedCHip = group.findViewById(chipGroup.getCheckedChipId());
//                    //Log.v("Selected chipGroup", selectedCHip.getText() + "");
//                    deviceType = selectedCHip.getText().toString().toLowerCase();
//
//                   if (Utility.checkInternet(GenActivity.this)) {
//                        new GenActivity.StudentDataCall(GenActivity.this, (GenActivity.StudentDataCall) null).execute();
//                    } else {
//                        Utility.openInternetNotAvailable(GenActivity.this,"");
//                    }
//                }
//            });






           /*drpTripRoute = findViewById(R.id.drpTripRoute);
           String[] items = new String[]{"1", "2", "three"};
           ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
           drpTripRoute.setAdapter(adapter);*/

        } catch (Exception e) {
            //showDialog("Message", e.toString());
            if (!MyVariables.IsProduction) {
                e.printStackTrace();
                Log.v("OnCreate", e.toString());
            }
        }
    }

    private void getScannedAttendanceList() {
        try {
            Log.d("Reading", "Reading all Attendance...");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            getAttendanceList.clear();
            for (AttendanceModel c : db.getAllAttendanceList()) {
                try {
                    int value = (int) getDateDiff(format, c.getDateTime(), currentDate);
                    if (value < 7) {
                        getAttendanceList.add(c);
                    } else {
                        db.deleteAttendanceList(c);
                    }
                } catch (Exception e) {
                }
            }
        } catch (Exception e) {
        }
        Log.e("db Attendance list", db.getAllAttendanceList().toString());
    }

//    private void getScannedAttendanceLogList() {
//        try {
//            Log.d("Reading", "Reading all AttendanceLog...");
//            List<AttendanceModel> contacts = db.getAllAttendanceLogList();
//
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HHmmss", Locale.getDefault());
//            String currentDate = sdf.format(new Date());
//            getAttendanceLogList.clear();
//            for (AttendanceModel c : contacts) {
//                try {
//                    int value = (int) getDateDiff(format, c.getDateTime(), currentDate);
//                    Log.e("value", String.valueOf(value));
//
//                    if (value < 7) {
//                        getAttendanceLogList.add(c);
//                    }else {
//                        db.deleteAttendanceLogList(c);
//                    }
//                } catch (Exception e) {
//
//                }
//            }
//        } catch (Exception e) {
//        }
//
//        Log.e("Attendance Log list", getAttendanceLogList.toString());
//        Log.e("db Attendance Log list", db.getAllAttendanceLogList().toString());
//    }


    //#region LocationCode
//    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        try {
            if (checkPermissions()) {

                // check if location is enabled
                if (isLocationEnabled()) {

                    // getting last
                    // location from
                    // FusedLocationClient
                    // object
                    if (mFusedLocationClient == null) {
                        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(GenActivity.this);
                    }
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            Location location = task.getResult();
                            if (location == null) {
                                requestNewLocationData();
                            } else {
                                lattitude = location.getLatitude() + "";
                                longtitude = location.getLongitude() + "";
                                if (!MyVariables.IsProduction) {
                                    Log.v("Locationssss", "lat: " + lattitude + " longt:" + longtitude);
                                }
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            } else {
                // if permissions aren't available,
                // request for permissions
                requestPermissions();
            }
        } catch (Exception ex) {

        }
    }

    //    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(0);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            lattitude = mLastLocation.getLatitude() + "";
            longtitude = mLastLocation.getLongitude() + "";
            if (!MyVariables.IsProduction) {
                Log.v("LocationCallBack", "lat: " + lattitude + " longt:" + longtitude);
            }
            //latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            //longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        } else if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, perform your actions
                exportToCSVFile();
            } else {
                // Permissions denied, handle accordingly (e.g., show a message to the user)
            }
        }
    }
    //#endregion LocationCode

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu_container, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_logout) {
            doLogout();
        } else if (itemId == R.id.menu_info) {
            showUserInformation();
        } else if (itemId == R.id.view_student_data) {
            if (Utility.checkInternet(GenActivity.this)) {
                isShowStudentData = true;
                new StudentDataCall(GenActivity.this, (StudentDataCall) null).execute();
                LinearLayout studentDataContainer = (LinearLayout) findViewById(R.id.studentDataContainer);
                studentDataContainer.setVisibility(View.VISIBLE);
                       /*if (studentDataContainer.getVisibility() == View.VISIBLE) {
                           studentDataContainer.setVisibility(View.GONE);
                       } else {
                           studentDataContainer.setVisibility(View.VISIBLE);
                       }*/
            } else {
                Utility.openInternetNotAvailable(GenActivity.this, "");
            }
        } else if (itemId == R.id.menu_setting) {
            AlertDialog alertDialog = MyVariables.getDefaultDialog(this, "", "Are you sure, you want to reset Master Card ?");
            assert alertDialog != null;
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES, RESET", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    MyVariables.CARD_AUTH_KEY = new byte[6];
                    MyVariables.SECTOR_NUMBER = 0;
                    reading_data.setText("Data reset successfully. Now tap Master Card.");
                    new KeyGenerator().isStudent = false;
                    //lnUserInformation.setVisibility(View.GONE);
                    MyVariables.scanedCard = new ArrayList<String>();
                    //scanned_data.setText("");
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();
        } else if (itemId == R.id.download_attendance_report) {
            try {
                if (!db.getAllAttendanceList().isEmpty()) {
                    // Check if permissions are already granted
                    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                        if (checkStoragePermission()) {
                            // Permissions are already granted, perform your actions here
                            exportToCSVFile();
                        } else {
                            // Request permissions
                            requestReadWritePermissions();
                        }
                    } else {
                        exportToCSVFile();
                    }
                } else {
                    Toast.makeText(this, "Attendance report is empty", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Attendance report is empty", Toast.LENGTH_SHORT).show();
            }
        } else if (itemId == R.id.menu_sync_data) {
            try {
                boolean isErrorDataAvailable = false;

                if (!db.getAllAttendanceList().isEmpty()) {
                    for (AttendanceModel attendance : db.getAllAttendanceList()) {
                        if (!attendance.getStatus().isEmpty() && attendance.getStatus().contains("Error")) {
                            isErrorDataAvailable = true;
                            Log.e("attendance", attendance.getData());
                        }
                    }
                    if (isErrorDataAvailable) {
                        showAlert(this);
                    } else {
                        Toast.makeText(GenActivity.this, "All Data Already Synced Successfully", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(GenActivity.this, "No data available for manual sync", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(GenActivity.this, "No data available for manual sync", Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showAlert(Context context) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // Set the title and message for the dialog
        alertDialogBuilder.setTitle("Manual Sync");
        alertDialogBuilder.setMessage(Html.fromHtml("<span style=color:black>" + "Do you want to sync manually?" + "</span>"));

        // Set a positive button and its click listener
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when the OK button is clicked
                dialog.dismiss(); //
                // Close the dialog
                if (!db.getAllAttendanceList().isEmpty()) {
                    for (AttendanceModel attendance : db.getAllAttendanceList()) {
                        if (!attendance.getStatus().isEmpty() && attendance.getStatus().contains("Error")) {
                            //TODO Call Attendance manual Api
                            if (Utility.checkInternet(GenActivity.this)) {
                                new StudentManualAttendancePushCall(GenActivity.this, (StudentManualAttendancePushCall) null).execute(attendance);
                            } else {
                                Utility.openInternetNotAvailable(GenActivity.this, "");
                                break;
                            }
                            Log.e("attendance", attendance.getData());
                        }
                    }
                }
            }
        });

        // Set a negative button and its click listener
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when the Cancel button is clicked
                dialog.dismiss(); // Close the dialog
            }
        });

        // Create and show the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void hideLastScanned() {
        try {

            boolean _active = true;
            int _splashTime = 8000;
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
                        GenActivity.this.findViewById(R.id.lnUserInformation).setVisibility(View.GONE);
                    }
                }
            };
            splashTread.start();
        } catch (Exception ex) {
            Log.v("hideLastScanned2", ex.toString());
        }
    }

    public void showUserInformation() {
        try {

            getLastLocation();

            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Username: " + MyVariables.USER_FULL_NAME);
            stringBuilder.append("\nDeviceID: " + MyVariables.deviceID);
            stringBuilder.append("\n\nLatitude: " + lattitude);
            stringBuilder.append("\n\nLongitude: " + longtitude);

            AlertDialog alertDialog = MyVariables.getDefaultDialog(this, "User Information", stringBuilder.toString());
            assert alertDialog != null;
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                }
            });
            alertDialog.show();


        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.v("Error Infor", ex.toString());
            }
        }
    }

    public void doLogout() {
        try {
            AlertDialog alertDialog = MyVariables.getDefaultDialog(this, getResources().getString(R.string.app_name), "Are you sure, you want to logout?");
            assert alertDialog != null;
            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {

                    if (Utility.checkInternet(GenActivity.this)) {
                        new LogoutCall(GenActivity.this, (LogoutCall) null).execute();
                    } else {
                        Utility.openInternetNotAvailable(GenActivity.this, "");
                    }
                }
            });

            alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            alertDialog.show();

        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.v("Error: doLogout", ex.toString());
            }
        }
    }

    public void onPause() {
        super.onPause();
        if (this.nfcAdapter != null) {
            this.nfcAdapter.disableForegroundDispatch(this);
        }
    }

    public void onResume() {
        super.onResume();
//        if (!Utility.checkInternet(GenActivity.this)) {
//            Utility.openInternetNotAvailable(GenActivity.this, "");
//            return;
//        }
        if (this.nfcAdapter != null) {
            this.nfcAdapter.enableForegroundDispatch(this, this.pendingIntent, this.intentFiltersArray, this.techListsArray);
        }

        getLastLocation();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        AlertDialog alertDialog = MyVariables.getDefaultDialog(this, getResources().getString(R.string.app_name), "Are you sure, you want to exit?");
        assert alertDialog != null;
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialog.show();
    }

    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        new KeyGenerator(GenActivity.this, (KeyGenerator) null).execute(new Intent[]{intent});
    }


    private void showSearchedUsers(String searchTerm) {
        try {
            if (searchTerm.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                listener = new ClickListener() {
                    @Override
                    public void click(int index, UserDetailsResult detailsResult) {
                        try {
                            if (!detailsResult.getRFIDNumbers().isEmpty()) {
                                if (Utility.checkInternet(GenActivity.this)) {
                                    String strCardNumber = detailsResult.getRFIDNumbers().split(",")[0];
                                    new StudentAttendancePushCall(GenActivity.this, (StudentAttendancePushCall) null).execute(strCardNumber);
                                } else {
                                    Utility.openInternetNotAvailable(GenActivity.this, "");
                                }
                            }
                        } catch (Exception ex) {
                            if (!MyVariables.IsProduction) {
                                Log.e("Error:WebCall", ex.toString());
                            }
                        }
                    }
                };
                recyclerViewBind = new StudentDataRecyclerViewBind(finaluserDetailsResults, getApplication(), listener);
                recyclerView.setAdapter(recyclerViewBind);
                recyclerView.setLayoutManager(new LinearLayoutManager(GenActivity.this));
                recyclerView.setVisibility(View.VISIBLE);
                lblDataTitle.setVisibility(View.VISIBLE);
                lySearchContainer.setVisibility(View.VISIBLE);
            } else {
                if (!finaluserDetailsResults.isEmpty()) {
                    searchTerm = searchTerm.toLowerCase();
                    List<UserDetailsResult> lstSearchData = new ArrayList<UserDetailsResult>();
                    for (UserDetailsResult us : finaluserDetailsResults) {
                        if (us.RFIDNumbers.toLowerCase().contains(searchTerm) || us.UserFullName.toLowerCase().contains(searchTerm) || us.ClassName.toLowerCase().contains(searchTerm)) {
                            lstSearchData.add(us);
                        }
                    }
                    if (!lstSearchData.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        listener = new ClickListener() {
                            @Override
                            public void click(int index, UserDetailsResult detailsResult) {
                                try {
                                    if (!detailsResult.getRFIDNumbers().isEmpty()) {
                                        if (Utility.checkInternet(GenActivity.this)) {
                                            String strCardNumber = detailsResult.getRFIDNumbers().split(",")[0];
                                            new StudentAttendancePushCall(GenActivity.this, (StudentAttendancePushCall) null).execute(strCardNumber);
                                        } else {
                                            Utility.openInternetNotAvailable(GenActivity.this, "");
                                        }
                                    }
                                } catch (Exception ex) {
                                    if (!MyVariables.IsProduction) {
                                        Log.e("Error:WebCall", ex.toString());
                                    }
                                }
                            }
                        };
                        recyclerViewBind = new StudentDataRecyclerViewBind(lstSearchData, getApplication(), listener);
                        recyclerView.setAdapter(recyclerViewBind);
                        recyclerView.setLayoutManager(new LinearLayoutManager(GenActivity.this));
                        recyclerView.setVisibility(View.VISIBLE);
                        lblDataTitle.setVisibility(View.VISIBLE);
                        lySearchContainer.setVisibility(View.VISIBLE);
                    } else {
                        Utility.openInternetNotAvailable(GenActivity.this, "No user(s) found, try other search.");
                    }
                }
            }


        } catch (Exception ex) {
            Utility.openInternetNotAvailable(GenActivity.this, "Err: " + ex.toString());
        }
    }

//    @Override
//    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//        switch (buttonView.getId()) {
//            case R.id.busSwitch: {
//                if (isChecked) {
//                    deviceType = "bus";
//                    gateSwitch.setChecked(false);
//                } else {
//                    deviceType = "gate";
//                    gateSwitch.setChecked(true);
//                }
////               if (Utility.checkInternet(GenActivity.this)) {
////                    new GenActivity.StudentDataCall(GenActivity.this, (GenActivity.StudentDataCall) null).execute();
////                } else {
////                    Utility.openInternetNotAvailable(GenActivity.this,"");
////                }
//                Log.e("bus", String.valueOf(isChecked));
//            }
//            case R.id.gateSwitch: {
//                if (isChecked) {
//                    deviceType = "gate";
//                    busSwitch.setChecked(false);
//                } else {
//                    deviceType = "bus";
//                    busSwitch.setChecked(true);
//                }
////                if (isChecked) {
////                    deviceType = "gate";
////                    if (busSwitch.isChecked()) {
////                        busSwitch.setChecked(false);
////                    }
////                }
////                else {
////                    deviceType = "bus";
////                    busSwitch.setChecked(!busSwitch.isChecked());
////                }
////
////               if (Utility.checkInternet(GenActivity.this)) {
////                    new GenActivity.StudentDataCall(GenActivity.this, (GenActivity.StudentDataCall) null).execute();
////                } else {
////                    Utility.openInternetNotAvailable(GenActivity.this,"");
////                }
//                Log.e("gate", String.valueOf(isChecked));
//            }
//        }
//    }

    //todo rfid card reader
    //#region BackGroundTask
    private class KeyGenerator extends AsyncTask<Intent, Void, String> {
        ProgressDialog progressDialog;

        byte[] newKey = new byte[]{-114, 74, 14, 74, 6, -62};
        boolean isStudent = false;
        TextView reading_data, txtName, txtFacCode, txtCardNumber, txtIssueLevel, txtEmpcode, scanned_data;
        StringBuilder sb;
        LinearLayout lnUserInformation, lnEmpCode;

        String strName, strFacCode, strCardNumber, strIssueLevel, strEmpCode;

        private KeyGenerator() {
            this.progressDialog = null;
        }

        /* synthetic */ KeyGenerator(GenActivity keyGeneratorActivity, KeyGenerator keyGenerator) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(GenActivity.this, "Please wait...", "Don't remove the card.");
            this.reading_data = GenActivity.this.findViewById(R.id.reading_data);
            this.lnUserInformation = GenActivity.this.findViewById(R.id.lnUserInformation);
            this.lnEmpCode = GenActivity.this.findViewById(R.id.lnEmpCode);
            this.scanned_data = GenActivity.this.findViewById(R.id.scanned_data);
            this.txtName = GenActivity.this.findViewById(R.id.txtName);
            this.txtFacCode = GenActivity.this.findViewById(R.id.txtFacCode);
            this.txtCardNumber = GenActivity.this.findViewById(R.id.txtCardNumber);
            this.txtIssueLevel = GenActivity.this.findViewById(R.id.txtIssueLevel);
            this.txtEmpcode = GenActivity.this.findViewById(R.id.txtEmpCode);
            this.sb = new StringBuilder();
            strName = "";
            strFacCode = "";
            strCardNumber = "";
            strIssueLevel = "";
            strEmpCode = "";
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(Intent... params) {
            if (SCHOOL_GROUP_CODE != null && SCHOOL_GROUP_CODE.contains("dais")) {

                if (MyVariables.SECTOR_NUMBER != 0) {
                    //#region StudentCardRead

                    byte[][] result = new byte[3][];
                    Intent intent = params[0];
                    if (!"android.nfc.action.TECH_DISCOVERED".equals(intent.getAction())) {
                        if (!MyVariables.IsProduction) {
                            Log.d("Error:", "CARD_ERROR");
                        }
                        sb.append("Error: CARD_ERROR");
                        return "CARD_ERROR";
                    }
                    MifareClassic mfc = MifareClassic.get((Tag) intent.getParcelableExtra("android.nfc.extra.TAG"));
                    try {
                        mfc.connect();

                        if (mfc.authenticateSectorWithKeyA(MyVariables.SECTOR_NUMBER, MyVariables.CARD_AUTH_KEY)) {
                            byte[] data = mfc.readBlock(mfc.sectorToBlock(MyVariables.SECTOR_NUMBER));
                            if (!MyVariables.IsProduction) {
                                Log.v("Sector number", String.valueOf(MyVariables.SECTOR_NUMBER));
                            }
                            if (!MyVariables.IsProduction) {
                                Log.v("card auth key", Arrays.toString(MyVariables.CARD_AUTH_KEY));
                            }
                            String hexData = Utility.toReversedHex(data);
                            if (!MyVariables.IsProduction) {
                                Log.v("Rev. Hex Data", hexData);
                            }
                            //sb.append("Rev. Hex Data:\n" + hexData);

                            String finalDataToProcess = hexData.substring(4, hexData.length() - 1).substring(0, 10);
                            if (!MyVariables.IsProduction) {
                                Log.v("Final Hex Data", finalDataToProcess);
                            }
                            //sb.append("Final Hex Data: \n" + finalDataToProcess);

                            String binaryData = Utility.hexToBinary(finalDataToProcess);
                            if (!MyVariables.IsProduction) {
                                Log.v("To Binary", binaryData);
                            }
                            //sb.append("Final Hex Data: \n" + finalDataToProcess);

                            String finalBinaryData = binaryData.substring(0, 37);
                            if (!MyVariables.IsProduction) {
                                Log.v("Final Binary", finalBinaryData + " length: " + finalBinaryData.length());
                            }
                            //sb.append("Final Binary: \n" + finalDataToProcess);

                            String ccode = finalBinaryData.substring(0, 12);
                            String fCode = new StringBuilder().append(ccode).reverse().toString();
                            int facilityCode = Integer.parseInt(fCode, 2);
                            if (!MyVariables.IsProduction) {
                                Log.v("facilityCode", ccode + " length: " + ccode.length() + " - " + fCode + " - " + facilityCode);
                            }
                            sb.append("\nUser Card read successfully.\n");
                            //sb.append("\nFacility Code: " + facilityCode);
                            strFacCode = "" + facilityCode;

                            String ccnumber = finalBinaryData.substring(13, 33);
                            String cNumber = new StringBuilder().append(ccnumber).reverse().toString();
                            int cardNumber = Integer.parseInt(cNumber, 2);
                            if (!MyVariables.IsProduction) {
                                Log.v("cardNumber", ccnumber + " length: " + ccnumber.length() + " - " + cNumber + " - " + cardNumber);
                            }
                            //sb.append("\nCard Number: " + cardNumber);
                            strCardNumber = "" + cardNumber;
                            if (!MyVariables.IsProduction) {
                                if (cardNumber == 1048572) {
                                    strName = "Ashish Sharma";
                                } else if (cardNumber == 1047575) {
                                    strName = "Manish Sharma";
                                } else if (cardNumber == 1048574) {
                                    strName = "Archana Sharma";
                                } else if (cardNumber == 100002) {
                                    strEmpCode = "12345679";
                                    strName = "Testing Card";
                                } else if (cardNumber == 100001) {
                                    strName = "Testing Card";
                                    strEmpCode = "12345678";
                                }
                                Log.v("strName", strName);
                            }
                            String aa = finalBinaryData.substring(34, 37);
                            String iLevel = new StringBuilder().append(aa).reverse().toString();
                            int issueLevel = Integer.parseInt(iLevel, 2);

                            if (!MyVariables.IsProduction) {
                                Log.v("issueLevel", aa + " - " + iLevel + " - " + issueLevel);
                            }
                            //sb.append("\nIssue Level: "  + issueLevel);
                            strIssueLevel = "" + issueLevel;

                            isStudent = true;
                            mfc.close();
                        } else {
                            if (!MyVariables.IsProduction) {
                                Log.v("AUTH", "Fail with Private Key");
                            }

                            sb.append("Authentication Failed with Private Key.\n");
                        }
                        mfc.close();

                        return "ERROR";
                    } catch (Exception ex) {
                        String str = "EXCEPTION: " + ex.toString();
                        if (!MyVariables.IsProduction) {
                            Log.d("Error:", str);
                        }
                        sb.append("Something went wrong, please try again.");
                        if (!mfc.isConnected()) {
                            return str;
                        }
                        try {
                            mfc.close();
                            return str;
                        } catch (Exception e4) {
                            return str;
                        }
                    } catch (Throwable th) {
                        if (mfc.isConnected()) {
                            try {
                                mfc.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th;
                    }
                    //#endregion StudentCardRead
                } else {
                    //#region MasterCardRead

                    byte[][] result = new byte[3][];
                    Intent intent = params[0];

                    if (!"android.nfc.action.TECH_DISCOVERED".equals(intent.getAction())) {

                        if (!MyVariables.IsProduction) {
                            Log.d("Error:", "CARD_ERROR");
                        }
                        sb.append("Error: CARD_ERROR");
                        return "CARD_ERROR";
                    }
                    MifareClassic mfc = MifareClassic.get((Tag) intent.getParcelableExtra("android.nfc.extra.TAG"));
                    try {
                        mfc.connect();
                        for (int k = 0; k < 16; k++) {
                            boolean isAuthenticate = false;

                            if (mfc.authenticateSectorWithKeyA(k, MifareClassic.KEY_DEFAULT)) {
                                isAuthenticate = true;
                                if (!MyVariables.IsProduction) {
                                    Log.d("Auth-A:", "KEY_DEFAULT");
                                    sb.append("AuthenticateSectorWithKey - A: KEY_DEFAULT");
                                }
                            }

                            if (isAuthenticate) {
                                if (k == 2) {
                                    byte[] data = mfc.readBlock(mfc.sectorToBlock(k));
                                    result[0] = data;
                                    if (!MyVariables.IsProduction) {
                                        Log.v("Sector:", "S-" + k + " Data:" + Utility.toHex(data));
                                    }
                                } else if (k == 5) {
                                    byte[] data = mfc.readBlock(mfc.sectorToBlock(k));
                                    result[1] = data;
                                    if (!MyVariables.IsProduction) {
                                        Log.v("Sector:", "S-" + k + " Data:" + Utility.toHex(data));
                                    }
                                } else if (k == 8) {
                                    byte[] data = mfc.readBlock(mfc.sectorToBlock(k));
                                    result[2] = data;
                                    if (!MyVariables.IsProduction) {
                                        Log.v("Sector:", "S-" + k + " Data:" + Utility.toHex(data));
                                    }
                                }
                            }
                        }
                        if (result[0] == null || result[1] == null || result[2] == null) {
                            if (mfc.isConnected()) {
                                try {
                                    mfc.close();
                                } catch (Exception e) {
                                }
                            }
                            if (!MyVariables.IsProduction) {
                                Log.d("Error:", "MASTER_CARD_ERROR");
                            }
                            sb.append("Error - MASTER_CARD_ERROR");
                            return "MASTER_CARD_ERROR";
                        }
                        byte[] bt = Utility.calculateResult(result);
                        byte[] result1 = new byte[8];
                        byte[] result2 = new byte[8];
                        System.arraycopy(bt, 0, result1, 0, 8);
                        System.arraycopy(bt, 8, result2, 0, 8);
                        if (Arrays.equals(result1, result2)) {
                            byte[] key = new byte[6];
                            System.arraycopy(result1, 0, key, 0, 6);
                            System.arraycopy(result1, 0, MyVariables.CARD_AUTH_KEY, 0, 6);

                            MyVariables.SECTOR_NUMBER = result1[6];
                            if (mfc.isConnected()) {
                                try {
                                    mfc.close();
                                } catch (Exception e2) {
                                }
                            }
                            if (!MyVariables.IsProduction) {
                                Log.d("SUCCESS:", "SUCCESS");
                                //reading_data.setText("Master Card read successfully.");
                                Log.d("Data1:", Utility.toHex(result[0]));
                                Log.d("Data2:", Utility.toHex(result[1]));
                                Log.d("Data3:", Utility.toHex(result[2]));
                                Log.d("Final:", Utility.toHex(bt));

                                Log.d("Result1:", Utility.toHex(result1) + " - " + Utility.toDec(result1) + " - " + Utility.toReversedDec(result1) + " - " + new String(result1).trim());
                                Log.d("Result2:", Utility.toHex(result2) + " - " + Utility.toDec(result2) + " - " + Utility.toReversedDec(result2) + " - " + new String(result2).trim());
                                Log.d("KEY:", Utility.toReversedHex(MyVariables.CARD_AUTH_KEY));
                                Log.d("SECTOR", "" + MyVariables.SECTOR_NUMBER);
                            }
                            if (Utility.toDec(MyVariables.CARD_AUTH_KEY) == 0) {
                                sb.append("This is not Master Card. Please tap/scan Master Card");
                                MyVariables.SECTOR_NUMBER = 0;
                                MyVariables.CARD_AUTH_KEY = new byte[6];
                            } else {
                                sb.append("Master Card read successfully. Now You Can Scan/Tap User Card");
                            }

                            flag = true;
                            return "SUCCESS";
                        }
                        if (mfc.isConnected()) {
                            try {
                                mfc.close();
                            } catch (Exception e3) {
                            }
                        }
                        Log.d("Error:", "ERROR");
                        sb.append("Error: ERROR");
                        return "ERROR";
                    } catch (Exception ex) {
                        String str = "EXCEPTION: " + ex.toString();
                        sb.append("Error: " + str);
                        Log.d("Error:", str);
                        if (!mfc.isConnected()) {
                            return str;
                        }
                        try {
                            mfc.close();
                            return str;
                        } catch (Exception e4) {
                            return str;
                        }
                    } catch (Throwable th) {
                        if (mfc.isConnected()) {
                            try {
                                mfc.close();
                            } catch (Exception e5) {
                            }
                        }
                        throw th;
                    }
                    //#endregion MasterCardRead
                }
            } else {
                return otherDoInBackground(params);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String r12) {
            this.progressDialog.dismiss();
            this.reading_data.setText(this.sb.toString());
            if (isStudent) {
                this.lnUserInformation.setVisibility(View.VISIBLE);
                this.txtName.setText(strName);
                this.txtFacCode.setText(strFacCode);
                this.txtCardNumber.setText(strCardNumber);
                this.txtIssueLevel.setText(strIssueLevel);
                if (!strEmpCode.isEmpty()) {
                    lnEmpCode.setVisibility(View.VISIBLE);
                    this.txtEmpcode.setText(strEmpCode);
                }

                try {
                    //doStudentVerification();
                    new StudentAttendancePushCall(GenActivity.this, (StudentAttendancePushCall) null).execute(strCardNumber);
                } catch (Exception ex) {
                    Log.e("Error:WebCall", ex.toString());
                }

                //MyVariables.scanedCard.add(strName + " | " + strCardNumber);
                //this.scanned_data.setText("");
                //this.scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                //for (String scancard : MyVariables.scanedCard) {
                //  this.scanned_data.append("\n" + scancard);
                //}
            } else {
                if (MyVariables.SECTOR_NUMBER != 0) {
                    SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.MASTER_CARD_DATA.toString(), MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
                    myEdit.putString(MyVariables.DEFAULT_ENUM.CARD_AUTH_KEY.toString(), toReversedHex(MyVariables.CARD_AUTH_KEY));
                    myEdit.putString(MyVariables.DEFAULT_ENUM.SECTOR_NUMBER.toString(), "" + MyVariables.SECTOR_NUMBER);
                    myEdit.commit();
                }
            }
        }

        String otherDoInBackground(Intent... params) {

            try {
                if (MyVariables.SECTOR_NUMBER != 0) {

                    //#region StudentCardRead

                    byte[][] result = new byte[3][];
                    Intent intent = params[0];


                    String action = intent.getAction();
                    if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                            || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
                        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                        String payload = detectTagData(tag);

                        if (!MyVariables.IsProduction) {
                            Log.e("Payload: ", payload);
                        }
                        String newStrCardNumber = String.valueOf(toDec(tag.getId()));
                        if (!MyVariables.IsProduction) {
                            Log.e("finalStrcardnumber: ", newStrCardNumber);
                        }
                        if (!finaluserDetailsResults.isEmpty()) {
                            SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), MODE_PRIVATE);
                            String getStudentRFID = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), "");
                            if (getStudentRFID.isEmpty()) {
                                UserDetailsResult detailsResult = findUserFromList(newStrCardNumber);
                                if (!detailsResult.getRFIDNumbers().isEmpty()) {
                                    if (detailsResult.getParentRFIDCheckRequired()) {
                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                                        myEdit.putString(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), newStrCardNumber);
                                        myEdit.apply();
                                        showAddAnotherUserPopup(newStrCardNumber, "Add Another card for attendance User Name : " + detailsResult.getUserName() + " User Id : " + detailsResult.getUID() + " Card Number : " + detailsResult.getRFIDNumbers());
                                    } else {
                                        strCardNumber = newStrCardNumber;
                                        callAttendancePushCallApi();
                                    }
                                } else {
                                    Toast.makeText(GenActivity.this, "Please use Valid Card for Attendance", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                otherCardTapMethod(getStudentRFID, newStrCardNumber);
                            }
                        }
//                    StringBuilder sb = new StringBuilder();
//                    byte[] id = tag.getId();

//                    sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');

//                    strCardNumber = toReversedHex(id);
                        //readData(tag);
                    }


                    if (!NfcAdapter.ACTION_TECH_DISCOVERED.equals(intent.getAction())) {
                        if (!MyVariables.IsProduction) {
                            Log.d("Error:", "CARD_ERROR");
                        }
                        sb.append("Error: CARD_ERROR");
                        return "CARD_ERROR";
                    }

//                MifareClassic mfc = MifareClassic.get((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
//                try {
//                    mfc.connect();
//
//                    if (mfc.authenticateSectorWithKeyA(MyVariables.SECTOR_NUMBER, MyVariables.CARD_AUTH_KEY)) {
//                        byte[] data = mfc.readBlock(mfc.sectorToBlock(MyVariables.SECTOR_NUMBER));
//
//                        String hexData = toReversedHex(data);
//                        if (!MyVariables.IsProduction) {
//                            Log.v("Rev. Hex Data", hexData);
//                        }
//                        //sb.append("Rev. Hex Data:\n" + hexData);
//
//                        String finalDataToProcess = hexData.substring(4, hexData.length() - 1).substring(0, 10);
//                        if (!MyVariables.IsProduction) {
//                            Log.v("Final Hex Data", finalDataToProcess);
//                        }
//                        //sb.append("Final Hex Data: \n" + finalDataToProcess);
//
//                        String binaryData = Utility.hexToBinary(finalDataToProcess);
//                        if (!MyVariables.IsProduction) {
//                            Log.v("To Binary", binaryData);
//                        }
//                        //sb.append("Final Hex Data: \n" + finalDataToProcess);
//
//                        String finalBinaryData = binaryData.substring(0, 37);
//                        if (!MyVariables.IsProduction) {
//                            Log.v("Final Binary", finalBinaryData + " length: " + finalBinaryData.length());
//                        }
//                        //sb.append("Final Binary: \n" + finalDataToProcess);
//
//                        String ccode = finalBinaryData.substring(0, 12);
//                        String fCode = new StringBuilder().append(ccode).reverse().toString();
//                        int facilityCode = Integer.parseInt(fCode, 2);
//                        if (!MyVariables.IsProduction) {
//                            Log.v("facilityCode", ccode + " length: " + ccode.length() + " - " + fCode + " - " + facilityCode);
//                        }
//                        sb.append("\nUser Card read successfully.\n");
//                        //sb.append("\nFacility Code: " + facilityCode);
//                        strFacCode = "" + facilityCode;
//
//                        String ccnumber = finalBinaryData.substring(13, 33);
//                        String cNumber = new StringBuilder().append(ccnumber).reverse().toString();
//                        int cardNumber = Integer.parseInt(cNumber, 2);
//                        if (!MyVariables.IsProduction) {
//                            Log.v("cardNumber", ccnumber + " length: " + ccnumber.length() + " - " + cNumber + " - " + cardNumber);
//                        }
//                        //sb.append("\nCard Number: " + cardNumber);
//                        strCardNumber = "" + cardNumber;
//                        if (!MyVariables.IsProduction) {
//                            if (cardNumber == 1048572) {
//                                strName = "Ashish Sharma";
//                            } else if (cardNumber == 1047575) {
//                                strName = "Manish Sharma";
//                            } else if (cardNumber == 1048574) {
//                                strName = "Archana Sharma";
//                            } else if (cardNumber == 100002) {
//                                strEmpCode = "12345679";
//                                strName = "Testing Card";
//                            } else if (cardNumber == 100001) {
//                                strName = "Testing Card";
//                                strEmpCode = "12345678";
//                            }
//                            Log.v("strName", strName);
//                        }
//                        String aa = finalBinaryData.substring(34, 37);
//                        String iLevel = new StringBuilder().append(aa).reverse().toString();
//                        int issueLevel = Integer.parseInt(iLevel, 2);
//
//                        if (!MyVariables.IsProduction) {
//                            Log.v("issueLevel", aa + " - " + iLevel + " - " + issueLevel);
//                        }
//                        //sb.append("\nIssue Level: "  + issueLevel);
//                        strIssueLevel = "" + issueLevel;
//
//                        isStudent = true;
//                        mfc.close();
//                    } else {
//                        if (!MyVariables.IsProduction) {
//                            Log.v("AUTH", "Fail with Private Key");
//                        }
//
//                        sb.append("Authentication Failed with Private Key.\n");
//                    }
//                    mfc.close();
//
//                    return "ERROR";
//                } catch (Exception ex) {
//                    String str = "EXCEPTION: " + ex.toString();
//                    if (!MyVariables.IsProduction) {
//                        Log.d("Error:", str);
//                    }
//                    sb.append("Something went wrong, please try again.");
//                    if (!mfc.isConnected()) {
//                        return str;
//                    }
//                    try {
//                        mfc.close();
//                        return str;
//                    } catch (Exception e4) {
//                        return str;
//                    }
//                } catch (Throwable th) {
//                    if (mfc.isConnected()) {
//                        try {
//                            mfc.close();
//                        } catch (Exception e5) {
//                        }
//                    }
//                    throw th;
//                }
                    //#endregion StudentCardRead
                } else {
                    //#region MasterCardRead
//                byte[][] result = new byte[3][];
//                Intent intent = params[0];
//
//                if (!"android.nfc.action.TECH_DISCOVERED".equals(intent.getAction())) {
//
//                    if (!MyVariables.IsProduction) {
//                        Log.d("Error:", "CARD_ERROR");
//                    }
//                    sb.append("Error: CARD_ERROR");
//                    return "CARD_ERROR";
//                }
//                MifareClassic mfc = MifareClassic.get((Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
//                try {
//                    mfc.connect();
//                    for (int k = 0; k < 16; k++) {
//                        boolean isAuthenticate = false;
//
//                        if (mfc.authenticateSectorWithKeyA(k, MifareClassic.KEY_DEFAULT)) {
//                            isAuthenticate = true;
//                            if (!MyVariables.IsProduction) {
//                                Log.d("Auth-A:", "KEY_DEFAULT");
//                                sb.append("AuthenticateSectorWithKey - A: KEY_DEFAULT");
//                            }
//                        }
//
//                        if (isAuthenticate) {
//                            if (k == 2) {
//                                byte[] data = mfc.readBlock(mfc.sectorToBlock(k));
//                                result[0] = data;
//                                if (!MyVariables.IsProduction) {
//                                    Log.v("Sector:", "S-" + k + " Data:" + toHex(data));
//                                }
//                            } else if (k == 5) {
//                                byte[] data = mfc.readBlock(mfc.sectorToBlock(k));
//                                result[1] = data;
//                                if (!MyVariables.IsProduction) {
//                                    Log.v("Sector:", "S-" + k + " Data:" + toHex(data));
//                                }
//                            } else if (k == 8) {
//                                byte[] data = mfc.readBlock(mfc.sectorToBlock(k));
//                                result[2] = data;
//                                if (!MyVariables.IsProduction) {
//                                    Log.v("Sector:", "S-" + k + " Data:" + toHex(data));
//                                }
//                            }
//                        }
//                    }
//                    if (result[0] == null || result[1] == null || result[2] == null) {
//                        if (mfc.isConnected()) {
//                            try {
//                                mfc.close();
//                            } catch (Exception e) {
//                            }
//                        }
//                        if (!MyVariables.IsProduction) {
//                            Log.d("Error:", "MASTER_CARD_ERROR");
//                        }
//                        sb.append("Error - MASTER_CARD_ERROR");
//                        return "MASTER_CARD_ERROR";
//                    }
//                    byte[] bt = Utility.calculateResult(result);
//                    byte[] result1 = new byte[8];
//                    byte[] result2 = new byte[8];
//                    System.arraycopy(bt, 0, result1, 0, 8);
//                    System.arraycopy(bt, 8, result2, 0, 8);
//                    if (Arrays.equals(result1, result2)) {
//                        byte[] key = new byte[6];
//                        System.arraycopy(result1, 0, key, 0, 6);
//                        System.arraycopy(result1, 0, MyVariables.CARD_AUTH_KEY, 0, 6);
//
//                        MyVariables.SECTOR_NUMBER = result1[6];
//                        if (mfc.isConnected()) {
//                            try {
//                                mfc.close();
//                            } catch (Exception e2) {
//                            }
//                        }
//                        if (!MyVariables.IsProduction) {
//                            Log.d("SUCCESS:", "SUCCESS");
//                            //reading_data.setText("Master Card read successfully.");
//                            Log.d("Data1:", toHex(result[0]));
//                            Log.d("Data2:", toHex(result[1]));
//                            Log.d("Data3:", toHex(result[2]));
//                            Log.d("Final:", toHex(bt));
//
//                            Log.d("Result1:", toHex(result1) + " - " + toDec(result1) + " - " + toReversedDec(result1) + " - " + new String(result1).trim());
//                            Log.d("Result2:", toHex(result2) + " - " + toDec(result2) + " - " + toReversedDec(result2) + " - " + new String(result2).trim());
//                            Log.d("KEY:", toReversedHex(MyVariables.CARD_AUTH_KEY));
//                            Log.d("SECTOR", "" + MyVariables.SECTOR_NUMBER);
//                        }
//                        if (toDec(MyVariables.CARD_AUTH_KEY) == 0) {
//                            sb.append("This is not Master Card. Please tap/scan Master Card");
//                            MyVariables.SECTOR_NUMBER = 0;
//                            MyVariables.CARD_AUTH_KEY = new byte[6];
//                        } else {
//                            sb.append("Master Card read successfully. Now You Can Scan/Tap User Card");
//                        }
//
//                        flag = true;
//                        return "SUCCESS";
//                    }
//                    if (mfc.isConnected()) {
//                        try {
//                            mfc.close();
//                        } catch (Exception e3) {
//                        }
//                    }
//                    Log.d("Error:", "ERROR");
//                    sb.append("Error: ERROR");
//                    return "ERROR";
//                } catch (Exception ex) {
//                    String str = "EXCEPTION: " + ex.toString();
//                    sb.append("Error: " + str);
//                    Log.d("Error:", str);
//                    if (!mfc.isConnected()) {
//                        return str;
//                    }
//                    try {
//                        mfc.close();
//                        return str;
//                    } catch (Exception e4) {
//                        return str;
//                    }
//                } catch (Throwable th) {
//                    if (mfc.isConnected()) {
//                        try {
//                            mfc.close();
//                        } catch (Exception e5) {
//                        }
//                    }
//                    throw th;
//                }
                    //#endregion MasterCardRead
                }
            } catch (Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(GenActivity.this, "Please Tap Valid Card", Toast.LENGTH_LONG).show();
                    }
                });
            }

            return "CARD_ERROR";
        }

        String otherCardTapMethod(String firstCardNumber, String secondCardNumber) {

            UserDetailsResult detailsResult = findUserFromList(firstCardNumber);
            if (!MyVariables.IsProduction) {
                Log.e("detailsResult: ", detailsResult.toString());
            }
            if (!detailsResult.getParentRFIDs().isEmpty()) {

                boolean isRefIdAvailable = false;

                for (ParentRFID parentRFID : detailsResult.getParentRFIDs()) {

                    //#2. Check if user id is matching
                    if (Objects.equals(parentRFID.getRFID(), secondCardNumber)) {
                        //#3. Matches, user id exists, get the record and out from loop
                        isRefIdAvailable = true;
                        break;
                    }
                }
                if (isRefIdAvailable) {
                    strCardNumber = firstCardNumber;
                    callAttendancePushCallApi();
                } else {
                    showAddAnotherUserPopup(firstCardNumber, "Add Another card for attendance User Name : " + detailsResult.getUserName() + " User Id : " + detailsResult.getUID() + " Card Number : " + detailsResult.getRFIDNumbers());
                }
            } else {
                showAddAnotherUserPopup(firstCardNumber, "Add Another card for attendance User Name : " + detailsResult.getUserName() + " User Id : " + detailsResult.getUID() + " Card Number : " + detailsResult.getRFIDNumbers());
            }
            return this.strCardNumber;
        }

        // old attendance push api call
        void callAttendancePushCallApi() {
            AttendanceModel model = new AttendanceModel();

            UserDetailsResult detailsResult = findUserFromList(this.strCardNumber);

            if (detailsResult.getUserID() > 0) {

                boolean isAllowed = detailsResult.isDenied();
                String oDeniedReason = detailsResult.getDeniedReason();

                oDeniedReason = (oDeniedReason.isEmpty()) ? "Manual" : oDeniedReason;

                if (isAllowed) {
                    final String deniedReason = oDeniedReason;
                    progressDialog.dismiss();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            AlertDialog alertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "This user is denied because of this reason\n" + deniedReason);
                            assert alertDialog != null;
                            alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });
                            alertDialog.show();
                        }
                    });
                }
                //else {
                Map<String, String> params1 = new HashMap<>();
                List<UserAttendanceExternalAPIVM> externalAPIVMList = new ArrayList<UserAttendanceExternalAPIVM>();
                String attendanceTakeTime = MyVariables.getSystemDateTime();
                UserAttendanceExternalAPIVM externalAPIVM = new UserAttendanceExternalAPIVM(this.strCardNumber, MyVariables.deviceID, attendanceTakeTime, attendanceTakeTime, "Default", Objects.equals(deviceType, "bus") ? "Bus" : "Car", lattitude, longtitude, "10", true, Objects.equals(deviceType, "bus"), "In", !isAllowed, oDeniedReason);
                externalAPIVMList.add(externalAPIVM);
                Gson gson = new Gson();

                params1.put("pRequestCriteria", "insert");
                params1.put("pRequestJsonData", gson.toJson(externalAPIVMList));

                // TODO
                UserDetailsResult userD = new UserDetailsResult();
                //find user from list
                userD = findUserFromList(this.strCardNumber);

                model.setId(userD.getUserID());
                model.setDateTime(attendanceTakeTime);
                model.setScannedCard(this.strCardNumber);
                model.setClassName(userD.getClassName() + " - " + detailsResult.getDivisionName());
                model.setName(userD.getUserName());
                model.setType(deviceType);
                model.setData(gson.toJson(externalAPIVMList));
                Log.d("AttendanceNFCMobilePush", externalAPIVMList.toString());
                webService.sendPostRequest("/ExternalAPI/UserAttendanceNFCMobileDataPush", params1, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.v("Data222", result);

                        String responseMessage = "";
                        boolean success = false;
                        UserAttendanceResponse responseModel = new UserAttendanceResponse();
                        try {
                            responseModel = gson.fromJson(result, UserAttendanceResponse.class);
                            responseMessage = responseModel.getResponseMessage();
                            success = responseModel.isSuccess();
                        } catch (Exception ex) {
                            ResponseModel responseModel2 = gson.fromJson(result, ResponseModel.class);
                            success = responseModel2.isSuccess();
                            if (responseModel2.isSuccess()) {
                            } else {
                                responseMessage = responseModel2.getResponseMessage();
                            }
                        }
                        //ResponseModel responseModel = gson.fromJson(result, ResponseModel.class);

                        //Log.v("ResponseData", gson.toJson(responseModel.getResponseData()));
                        //Log.v("ResponseMessage", responseModel.getResponseMessage());
                        //Log.v("Success", "" + responseModel.isSuccess());
                        progressDialog.dismiss();
                        if (success) {
                            clearRFID();
                            setStatus("200 - " + responseMessage, model, false);
                            List<UserAttendanceDataPushResult> resultList = new ArrayList<UserAttendanceDataPushResult>();
                            resultList = responseModel.getResponseData();
                            UserAttendanceDataPushResult dataPushResult = resultList.get(0);
                            List<UserDetailsResult> userDetailsResults = new ArrayList<UserDetailsResult>();
                            UserDetailsResult detailsResult = new UserDetailsResult();

                            //find user from list
                            detailsResult = findUserFromList(KeyGenerator.this.strCardNumber);
                            if (dataPushResult.RFID == KeyGenerator.this.strCardNumber && dataPushResult.Result == 1) {

                                if (detailsResult.getUserID() > 0) {
                                    UserDetailsResult finalDetailsResult = detailsResult;
                                    boolean ss = success;
                                    String msg = responseMessage;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (ss) {
                                                TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                //userName.setText("");

                                                if (finalDetailsResult.UserID > 0) {
                                                    ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(KeyGenerator.this.strCardNumber);
                                                    String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                    TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                    MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + KeyGenerator.this.strCardNumber + " | " + attendanceTakeTime);

                                                    ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                    se.userDetailsResult = finalDetailsResult;
                                                    se.scannedCard = KeyGenerator.this.strCardNumber;
                                                    se.isAttendanceTaken = true;
                                                    se.attendanceTakenTime = attendanceTakeTime;

                                                    MyVariables.lstScannedCard.add(se);

                                                    MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                    MyVariables.lstSuccessUsersList.add(finalDetailsResult);
                                                           /*scanned_data.setText("");
                                                           scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                           for (String scancard : MyVariables.scanedCard) {
                                                               scanned_data.append("\n" + scancard);
                                                           }*/
                                                    displayScannedDetails();
                                                }
                                                mToastHandler.showToast(msg, Toast.LENGTH_LONG);
                                            }
                                        }
                                    });
                                } else {
                                    MyVariables.lstScannedUsers.add(detailsResult);
                                    MyVariables.lstFailedUserList.add(detailsResult);

                                    UserDetailsResult finalDetailsResult = detailsResult;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                            rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                            //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                            //userName.setText("");

                                            if (finalDetailsResult.UserID > 0) {
                                                ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(KeyGenerator.this.strCardNumber);
                                                String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + KeyGenerator.this.strCardNumber + " | " + attendanceTakeTime);

                                                ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                se.userDetailsResult = finalDetailsResult;
                                                se.scannedCard = KeyGenerator.this.strCardNumber;
                                                se.isAttendanceTaken = true;
                                                se.attendanceTakenTime = attendanceTakeTime;

                                                MyVariables.lstScannedCard.add(se);
                                                MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                MyVariables.lstSuccessUsersList.add(finalDetailsResult);
                                                       /*scanned_data.setText("");
                                                       scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                       for (String scancard : MyVariables.scanedCard) {
                                                           scanned_data.append("\n" + scancard);
                                                       }*/
                                                displayScannedDetails();
                                            }
                                            mToastHandler.showToast("Attendance not taken for this user.", Toast.LENGTH_LONG);
                                        }
                                    });


                                }
                            } else if (Objects.equals(dataPushResult.RFID, KeyGenerator.this.strCardNumber) && dataPushResult.Result == 2) {
                                MyVariables.lstScannedUsers.add(detailsResult);
                                MyVariables.lstFailedUserList.add(detailsResult);

                                ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                se.userDetailsResult = detailsResult;
                                se.scannedCard = KeyGenerator.this.strCardNumber;
                                se.isAttendanceTaken = false;
                                se.attendanceTakenTime = attendanceTakeTime;

                                MyVariables.lstScannedCard.add(se);

                            } else {
                                UserDetailsResult finalDetailsResult = detailsResult;
                                boolean isSss = success;
                                String msg = responseMessage;
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (isSss) {
                                            TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                            rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                            //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                            //userName.setText("");

                                            if (finalDetailsResult.UserID > 0) {
                                                ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(KeyGenerator.this.strCardNumber);
                                                String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + KeyGenerator.this.strCardNumber + " | " + attendanceTakeTime);

                                                MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                se.userDetailsResult = finalDetailsResult;
                                                se.scannedCard = KeyGenerator.this.strCardNumber;
                                                se.isAttendanceTaken = true;
                                                se.attendanceTakenTime = attendanceTakeTime;

                                                MyVariables.lstScannedCard.add(se);
                                                       /*scanned_data.setText("");
                                                       scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                       for (String scancard : MyVariables.scanedCard) {
                                                           scanned_data.append("\n" + scancard);
                                                       }*/

                                                displayScannedDetails();
                                            }
                                            mToastHandler.showToast(msg, Toast.LENGTH_LONG);
                                        }
                                    }
                                });
                                //GenActivity.this.hideLastScanned();
                            }
                        } else {
                            setStatus("Error - " + responseMessage, model, false);
                            String msg = responseMessage;
                            progressDialog.dismiss();
                            mToastHandler.showToast(msg, Toast.LENGTH_LONG);

                            UserDetailsResult detailsResult = findUserFromList(KeyGenerator.this.strCardNumber);
                            final UserAttendanceResponse res = responseModel;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (res.getResponseData() != null) {
                                        TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                        rd.setText("Attendance not recorded of this user.");

                                        //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                        //userName.setText("");

                                        if (detailsResult.UserID > 0) {
                                            ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                            ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(detailsResult.UserFullName);
                                            ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(KeyGenerator.this.strCardNumber);
                                            String uid = detailsResult.UID.isEmpty() ? "" : detailsResult.UID;
                                            ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                            TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                            MyVariables.scanedCard.add(detailsResult.UserFullName + " | " + KeyGenerator.this.strCardNumber + " | " + "Not taken");

                                            MyVariables.lstScannedUsers.add(detailsResult);


                                            ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                            se.userDetailsResult = detailsResult;
                                            se.scannedCard = KeyGenerator.this.strCardNumber;
                                            se.isAttendanceTaken = false;
                                            se.attendanceTakenTime = attendanceTakeTime;

                                            MyVariables.lstScannedCard.add(se);

                                                   /*scanned_data.setText("");
                                                   scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                   for (String scancard : MyVariables.scanedCard) {
                                                       scanned_data.append("\n" + scancard);
                                                   }*/
                                            displayScannedDetails();
                                        }

                                        mToastHandler.showToast(msg, Toast.LENGTH_LONG);

                                    } else {

                                        ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                        se.userDetailsResult = detailsResult;
                                        se.scannedCard = KeyGenerator.this.strCardNumber;
                                        se.isAttendanceTaken = false;
                                        se.attendanceTakenTime = attendanceTakeTime;

                                        MyVariables.lstScannedCard.add(se);

                                        TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                        rd.setText("Attendance not recorded of this user.");
                                    }
                                }
                            });
                            //GenActivity.this.hideLastScanned();
                        }
                    }

                });
                // }
            } else {
                setStatus("Error - Details not found.", model, false);
                Log.v("NoDetails", "Details not found.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "This scanned card is not found in the system.");
                        assert alertDialog != null;
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                        alertDialog.show();
                    }
                });
            }
        }

        void showAddAnotherUserPopup(String newStrCardNumber, String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!tapAlertDialog.isShowing())
                        tapAlertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), msg);

                    assert tapAlertDialog != null;
                    tapAlertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            clearRFID();
                        }
                    });
                    tapAlertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Manual Authorize", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            strCardNumber = newStrCardNumber;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    clearRFID();
                                    // Background operation
                                    callAttendancePushCallApi();
                                }
                            }).start();
//                            Handler mainHandler = new Handler(GenActivity.this.getMainLooper());
//                            Runnable myRunnable = new Runnable() {
//                                @Override
//                                public void run() {
//                                 callAttendancePushCallApi();
//                                }
//                            };
//                            mainHandler.post(myRunnable);

                        }
                    });
                    tapAlertDialog.setCancelable(false);
                    tapAlertDialog.setOnKeyListener((dialog, keyCode, event) -> {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                            // Handle the back button press here
                            return true; // Indicate that the event is handled
                        }
                        return false;
                    });
                    tapAlertDialog.show();
                }
            });
        }

        public void doStudentVerification() {
            try {
                Map<String, String> params = new HashMap<>();
                List<UserAttendanceExternalAPIVM> externalAPIVMList = new ArrayList<UserAttendanceExternalAPIVM>();
                UserAttendanceExternalAPIVM externalAPIVM = new UserAttendanceExternalAPIVM(strCardNumber, "", MyVariables.getSystemTime(), "", "Default", "School", "", "", "10", true, Objects.equals(deviceType, "bus"), "", true, "manual");
                externalAPIVMList.add(externalAPIVM);
                Gson gson = new Gson();

                params.put("pRequestCriteria", "insert");
                params.put("pRequestJsonData", gson.toJson(externalAPIVMList));
                Log.d("UserAttendanceNFCMobile", params.toString());

                webService.sendPostRequest("/ExternalAPI/UserAttendanceNFCMobileDataPush", params, new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        Log.v("Data", result);
                        ResponseModel responseModel = gson.fromJson(result, ResponseModel.class);
                        Log.v("ResponseData", responseModel.getResponseData());
                        Log.v("ResponseMessage", responseModel.getResponseMessage());
                        Log.v("Success", "" + responseModel.isSuccess());
                        progressD.dismiss();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                clearRFID();
                                if (!responseModel.getResponseData().isEmpty()) {
                                    KeyGenerator.this.reading_data.setText("Attendance Taken Successfully.");
                                    KeyGenerator.this.reading_data.append("\n\nData: " + responseModel.getResponseData());
                                    KeyGenerator.this.reading_data.append("\n\nMessage: " + responseModel.getResponseMessage());
                                }
                            }
                        });
                    }
                });
            } catch (Exception ex) {
                Log.v("Error", ex.toString());
            }
        }
    }

//    private class WebCall extends AsyncTask<Intent, Void, String> {
//
//        TextView api_data1;
//
//        private WebCall() {
//
//        }
//
//        /* synthetic */ WebCall(GenActivity webCallActivity, GenActivity.WebCall webCall) {
//            this();
//        }
//
//        public void onPreExecute() {
//            super.onPreExecute();
//            this.api_data1 = findViewById(R.id.api_data);
//
//        }
//
//        public void onPostExecute(java.lang.String r12) {
//
//        }
//
//        public String doInBackground(Intent... params) {
//            String data = "";
//            try {
//                Map<String, String> paramsList = new HashMap<>();
//                UserAttendanceExternalAPIVM externalAPIVM = new UserAttendanceExternalAPIVM("", "", "", "", "", "", "", "", "", false, false, "", true, "manual");
//                Gson gson = new Gson();
//
//                paramsList.put("pRequestCriteria", "testurl");
//                paramsList.put("pRequestJsonData", gson.toJson(externalAPIVM));
//                webService.sendPostRequest("/ExternalAPI/UserAttendanceNFCMobileDataPush", paramsList, new VolleyCallback() {
//                    @Override
//                    public void onSuccess(String result) {
//                        GenActivity.WebCall.this.setData(result);
//                        //GenActivity.WebCall.this.data = result;
//                    }
//                });
//            } catch (Exception ex) {
//                ex.printStackTrace();
//                Toast.makeText(GenActivity.this, "Error: " + ex.toString(), Toast.LENGTH_LONG);
//            }
//            return "";
//        }
//
//        public void setData(String result) {
//            try {
//                Gson gson = new Gson();
//                Log.v("Data", result);
//                ResponseModel responseModel = gson.fromJson(result, ResponseModel.class);
//                Log.v("ResponseData", responseModel.getResponseData());
//                Log.v("ResponseMessage", responseModel.getResponseMessage());
//                Log.v("Success", "" + responseModel.isSuccess());
//                GenActivity.this.progressD.dismiss();
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (!responseModel.getResponseData().isEmpty()) {
//                            api_data1.append(responseModel.getResponseData());
//                        }
//                    }
//                });
//            } catch (Exception ex) {
//                Log.e("Error::", ex.toString());
//            }
//        }
//    }

    private class StudentDataCall extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        private StudentDataCall() {
            this.progressDialog = null;
        }

        /* synthetic */ StudentDataCall(GenActivity genActivity, StudentDataCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(GenActivity.this, "Please wait...", "We are fetching data..");
            super.onPreExecute();
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... str) {
            Map<String, String> params = new HashMap<String, String>();
            params.put("DeviceType", deviceType);
            Random r = new Random();

            String result = webService.sendWebRequest(getApplicationContext(), "/ExternalAPI/GetAllUsers?DeviceType=" + deviceType + "&_c=" + r.nextInt(), Request.Method.GET, params, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        StudentDataCall.this.setResultData(result);
                        if (!MyVariables.IsProduction) {
                            Log.i("Api Data", result);
                        }
                    }catch (Exception e){
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

                if (result.contains("You are not Authorized")) {
                    mToastHandler.showToast(result, Toast.LENGTH_LONG);
                    startActivity(new Intent(GenActivity.this, SplashScreen.class));
                } else if (result.contains("Some Error Occured")) {
                    mToastHandler.showToast(result, Toast.LENGTH_LONG);
                    startActivity(new Intent(GenActivity.this, SplashScreen.class));
                } else {
                    JSONArray jsonArr = new JSONArray(result);
                    participantJsonList = new ArrayList<>();
                    try {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            UserDetailsResult data = new UserDetailsResult();
//                           "UserID": 14208,
//                           "UserName": "AA104",
//                           "UserFullName": "Amit Hasmukhlal Shah",
//                           "ContactNo": "7045231788",
//                           "UID": "AA104",
//                           "ClassName": "1",
//                           "DivisionName": "B",
//                           "UserType": "Student",
//                           "RFIDNumbers": "",
//                           "IsActive": false,
//                           "IsDenied": false,
//                           "DeniedReason": "",
//                           "IsParentRFIDCheckRequired": false,
                            if (jsonObj.has("UserID")) data.setUserID(jsonObj.getInt("UserID"));
                            if (jsonObj.has("UserName"))
                                data.setUserName(jsonObj.getString("UserName"));
                            if (jsonObj.has("UserFullName"))
                                data.setUserFullName(jsonObj.getString("UserFullName"));
                            if (jsonObj.has("UserProfileImage"))
                                data.setUserProfileImage(jsonObj.getString("UserProfileImage"));
                            if (jsonObj.has("ContactNo"))
                                data.setContactNo(jsonObj.getString("ContactNo"));
                            if (jsonObj.has("UID")) data.setUID(jsonObj.getString("UID"));
                            if (jsonObj.has("ClassName"))
                                data.setClassName(jsonObj.getString("ClassName"));
                            if (jsonObj.has("DivisionName"))
                                data.setDivisionName(jsonObj.getString("DivisionName"));
                            if (jsonObj.has("UserType"))
                                data.setUserType(jsonObj.getString("UserType"));
                            if (jsonObj.has("RFIDNumbers"))
                                data.setRFIDNumbers(jsonObj.getString("RFIDNumbers"));
                            if (jsonObj.has("IsActive"))
                                data.setActive(jsonObj.getBoolean("IsActive"));
                            if (jsonObj.has("IsDenied"))
                                data.setDenied(jsonObj.getBoolean("IsDenied"));
                            if (jsonObj.has("DeniedReason"))
                                data.setDeniedReason(jsonObj.getString("DeniedReason"));
                            if (jsonObj.has("IsParentRFIDCheckRequired"))
                                data.setParentRFIDCheckRequired(jsonObj.getBoolean("IsParentRFIDCheckRequired"));

                            if (jsonObj.has("ParentRFIDs")) {
                                JSONArray arr = jsonObj.getJSONArray("ParentRFIDs");
                                List<ParentRFID> parentRFIDs = new ArrayList<ParentRFID>();
                                for (int k = 0; k < arr.length(); k++) {
                                    JSONObject jsonObj1 = arr.getJSONObject(k);
                                    ParentRFID datap = new ParentRFID();
                                    datap.setUserRFIDID(jsonObj1.getInt("UserRFIDID"));
                                    datap.setUserID(jsonObj1.getInt("UserID"));
                                    datap.setRFID(jsonObj1.getString("RFID"));
                                    datap.setIsDeActive(jsonObj1.getBoolean("IsDeActive"));
                                    parentRFIDs.add(datap);
                                }

                                data.setParentRFIDs(parentRFIDs);
                            }
                            if ((data.getUserName() != null && !data.getUserName().isEmpty()) || !data.getUserName().equalsIgnoreCase("mobileverifieduser")) {
                                participantJsonList.add(data);
                            }
                        }


                        finaluserDetailsResults = participantJsonList;
//                    }
                        //StudentDataCall.this.student_data.setText("Total: " + participantJsonList.size());

                        SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(), MODE_PRIVATE);
                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
                        myEdit.putString(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(), jsonArr.toString());
                        myEdit.apply();

                        if (isShowStudentData) {
                            listener = new ClickListener() {
                                @Override
                                public void click(int index, UserDetailsResult detailsResult) {
                                    try {
                                        if (!detailsResult.getRFIDNumbers().isEmpty()) {
                                            if (Utility.checkInternet(GenActivity.this)) {
                                                String strCardNumber = detailsResult.getRFIDNumbers().split(",")[0];
                                                new StudentAttendancePushCall(GenActivity.this, (StudentAttendancePushCall) null).execute(strCardNumber);
                                            } else {
                                                Utility.openInternetNotAvailable(GenActivity.this, "");
                                            }
                                        }
                                    } catch (Exception ex) {
                                        if (!MyVariables.IsProduction) {
                                            Log.e("Error:WebCall", ex.toString());
                                        }
                                    }
                                }
                            };
                            recyclerViewBind = new StudentDataRecyclerViewBind(finaluserDetailsResults, getApplication(), listener);
                            recyclerView.setAdapter(recyclerViewBind);
                            recyclerView.setLayoutManager(new LinearLayoutManager(GenActivity.this));
                            recyclerView.setVisibility(View.VISIBLE);
                            lblDataTitle.setVisibility(View.VISIBLE);
                            lySearchContainer.setVisibility(View.VISIBLE);
                            isShowStudentData = false;
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            lblDataTitle.setVisibility(View.GONE);
                            lySearchContainer.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    // Log arrUserDetailsResult
//                    participantJsonList = JsonList;
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        participantJsonList.removeIf(filter -> filter.UserName.equalsIgnoreCase("mobileverifieduser") || Objects.equals(filter.getUserName(), "") || filter.getUserName() == null);

                    //StudentData.this.student_data.append(result);

               /*StudentDataAdapter adapter = new StudentDataAdapter(getApplicationContext(), R.id.title, participantJsonList);
               listViewStudentData.setAdapter(adapter);

               listViewStudentData.setVisibility(View.GONE);
               ((Button) findViewById(R.id.buttonShowData)).setVisibility(View.VISIBLE); */


                }
            } catch (Exception ex) {
                if (!MyVariables.IsProduction) {
                    Log.e("Errrrr:", ex.toString());
                }
                Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    //TODO card read
    private class StudentAttendancePushCall extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        private StudentAttendancePushCall() {
            this.progressDialog = null;
        }

        /* synthetic */ StudentAttendancePushCall(GenActivity genActivity, StudentAttendancePushCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(GenActivity.this, "Please wait...", "We are recording data..");
            super.onPreExecute();
            getLastLocation();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(String... str) {
            AttendanceModel model = new AttendanceModel();
            try {
                String strCardNumber = str[0];
                //check user is allowed for taking attendance or not
                if (!strCardNumber.isEmpty()) {
                    UserDetailsResult detailsResult = new UserDetailsResult();
                    //find user from list
                    detailsResult = findUserFromList(strCardNumber);
                    if (detailsResult.getUserID() > 0) {

                        boolean isAllowed = detailsResult.isDenied();
                        String oDeniedReason = detailsResult.getDeniedReason();

                        oDeniedReason = (oDeniedReason.isEmpty()) ? "Manual" : oDeniedReason;

                        if (isAllowed) {
                            final String deniedReason = oDeniedReason;
                            progressDialog.dismiss();
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    AlertDialog alertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "This user is denied because of this reason\n" + deniedReason);
                                    assert alertDialog != null;
                                    alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface arg0, int arg1) {

                                        }
                                    });
                                    alertDialog.show();
                                }
                            });
                        }
                        //else {
                        Map<String, String> params = new HashMap<>();
                        List<UserAttendanceExternalAPIVM> externalAPIVMList = new ArrayList<UserAttendanceExternalAPIVM>();
                        String attendanceTakeTime = MyVariables.getSystemDateTime();
                        UserAttendanceExternalAPIVM externalAPIVM = new UserAttendanceExternalAPIVM(strCardNumber, MyVariables.deviceID, attendanceTakeTime, attendanceTakeTime, "Default", Objects.equals(deviceType, "bus") ? "Bus" : "Car", lattitude, longtitude, "10", true, Objects.equals(deviceType, "bus"), "In", !isAllowed, oDeniedReason);
                        externalAPIVMList.add(externalAPIVM);
                        Gson gson = new Gson();

                        params.put("pRequestCriteria", "insert");
                        params.put("pRequestJsonData", gson.toJson(externalAPIVMList));

                        // TODO
                        UserDetailsResult userD = new UserDetailsResult();
                        //find user from list
                        userD = findUserFromList(strCardNumber);

                        model.setId(userD.getUserID());
                        model.setDateTime(attendanceTakeTime);
                        model.setScannedCard(strCardNumber);
                        model.setClassName(userD.getClassName() + " - " + detailsResult.getDivisionName());
                        model.setName(userD.getUserName());
                        model.setType(deviceType);
                        model.setData(gson.toJson(externalAPIVMList));
                        Log.d("AttendanceNFCMobilePush", externalAPIVMList.toString());
                        webService.sendPostRequest("/ExternalAPI/UserAttendanceNFCMobileDataPush", params, new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.v("Data222", result);

                                String responseMessage = "";
                                boolean success = false;
                                UserAttendanceResponse responseModel = new UserAttendanceResponse();
                                try {
                                    responseModel = gson.fromJson(result, UserAttendanceResponse.class);
                                    responseMessage = responseModel.getResponseMessage();
                                    success = responseModel.isSuccess();
                                } catch (Exception ex) {
                                    ResponseModel responseModel2 = gson.fromJson(result, ResponseModel.class);
                                    success = responseModel2.isSuccess();
                                    if (responseModel2.isSuccess()) {
                                    } else {
                                        responseMessage = responseModel2.getResponseMessage();
                                    }
                                }
                                //ResponseModel responseModel = gson.fromJson(result, ResponseModel.class);

                                //Log.v("ResponseData", gson.toJson(responseModel.getResponseData()));
                                //Log.v("ResponseMessage", responseModel.getResponseMessage());
                                //Log.v("Success", "" + responseModel.isSuccess());
                                progressDialog.dismiss();
                                if (success) {
//                                    setStatus("200 - " + responseMessage, model, false);
                                    setStatus("Error - " + responseMessage, model, false);
                                    List<UserAttendanceDataPushResult> resultList = new ArrayList<UserAttendanceDataPushResult>();
                                    resultList = responseModel.getResponseData();
                                    UserAttendanceDataPushResult dataPushResult = resultList.get(0);
                                    List<UserDetailsResult> userDetailsResults = new ArrayList<UserDetailsResult>();
                                    UserDetailsResult detailsResult = new UserDetailsResult();

                                    //find user from list
                                    detailsResult = findUserFromList(strCardNumber);
                                    if (dataPushResult.RFID == strCardNumber && dataPushResult.Result == 1) {

                                        if (detailsResult.getUserID() > 0) {
                                            UserDetailsResult finalDetailsResult = detailsResult;
                                            boolean ss = success;
                                            String msg = responseMessage;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (ss) {
                                                        TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                        rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                        //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                        //userName.setText("");

                                                        if (finalDetailsResult.UserID > 0) {
                                                            ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                            ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                            ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(strCardNumber);
                                                            String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                            ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                            TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                            MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + strCardNumber + " | " + attendanceTakeTime);

                                                            ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                            se.userDetailsResult = finalDetailsResult;
                                                            se.scannedCard = strCardNumber;
                                                            se.isAttendanceTaken = true;
                                                            se.attendanceTakenTime = attendanceTakeTime;

                                                            MyVariables.lstScannedCard.add(se);

                                                            MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                            MyVariables.lstSuccessUsersList.add(finalDetailsResult);
                                                           /*scanned_data.setText("");
                                                           scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                           for (String scancard : MyVariables.scanedCard) {
                                                               scanned_data.append("\n" + scancard);
                                                           }*/
                                                            displayScannedDetails();
                                                        }
                                                        mToastHandler.showToast(msg, Toast.LENGTH_LONG);
                                                    }
                                                }
                                            });
                                        } else {
                                            MyVariables.lstScannedUsers.add(detailsResult);
                                            MyVariables.lstFailedUserList.add(detailsResult);

                                            UserDetailsResult finalDetailsResult = detailsResult;
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                    rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                    //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                    //userName.setText("");

                                                    if (finalDetailsResult.UserID > 0) {
                                                        ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(strCardNumber);
                                                        String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                        TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                        MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + strCardNumber + " | " + attendanceTakeTime);

                                                        ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                        se.userDetailsResult = finalDetailsResult;
                                                        se.scannedCard = strCardNumber;
                                                        se.isAttendanceTaken = true;
                                                        se.attendanceTakenTime = attendanceTakeTime;

                                                        MyVariables.lstScannedCard.add(se);
                                                        MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                        MyVariables.lstSuccessUsersList.add(finalDetailsResult);
                                                       /*scanned_data.setText("");
                                                       scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                       for (String scancard : MyVariables.scanedCard) {
                                                           scanned_data.append("\n" + scancard);
                                                       }*/
                                                        displayScannedDetails();
                                                    }
                                                    mToastHandler.showToast("Attendance not taken for this user.", Toast.LENGTH_LONG);
                                                }
                                            });


                                        }
                                    } else if (dataPushResult.RFID == strCardNumber && dataPushResult.Result == 2) {
                                        MyVariables.lstScannedUsers.add(detailsResult);
                                        MyVariables.lstFailedUserList.add(detailsResult);

                                        ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                        se.userDetailsResult = detailsResult;
                                        se.scannedCard = strCardNumber;
                                        se.isAttendanceTaken = false;
                                        se.attendanceTakenTime = attendanceTakeTime;

                                        MyVariables.lstScannedCard.add(se);

                                    } else {
                                        UserDetailsResult finalDetailsResult = detailsResult;
                                        boolean isSss = success;
                                        String msg = responseMessage;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (isSss) {
                                                    TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                    rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                    //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                    //userName.setText("");

                                                    if (finalDetailsResult.UserID > 0) {
                                                        ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(strCardNumber);
                                                        String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                        TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                        MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + strCardNumber + " | " + attendanceTakeTime);

                                                        MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                        ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                        se.userDetailsResult = finalDetailsResult;
                                                        se.scannedCard = strCardNumber;
                                                        se.isAttendanceTaken = true;
                                                        se.attendanceTakenTime = attendanceTakeTime;

                                                        MyVariables.lstScannedCard.add(se);
                                                       /*scanned_data.setText("");
                                                       scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                       for (String scancard : MyVariables.scanedCard) {
                                                           scanned_data.append("\n" + scancard);
                                                       }*/

                                                        displayScannedDetails();
                                                    }
                                                    mToastHandler.showToast(msg, Toast.LENGTH_LONG);
                                                }
                                            }
                                        });
                                        //GenActivity.this.hideLastScanned();
                                    }
                                } else {
                                    setStatus("Error - " + responseMessage, model, false);
                                    String msg = responseMessage;
                                    progressDialog.dismiss();
                                    mToastHandler.showToast(msg, Toast.LENGTH_LONG);

                                    UserDetailsResult detailsResult = findUserFromList(strCardNumber);
                                    final UserAttendanceResponse res = responseModel;

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (res.getResponseData() != null) {
                                                TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                rd.setText("Attendance not recorded of this user.");

                                                //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                //userName.setText("");

                                                if (detailsResult.UserID > 0) {
                                                    ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(detailsResult.UserFullName);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(strCardNumber);
                                                    String uid = detailsResult.UID.isEmpty() ? "" : detailsResult.UID;
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                    TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                    MyVariables.scanedCard.add(detailsResult.UserFullName + " | " + strCardNumber + " | " + "Not taken");

                                                    MyVariables.lstScannedUsers.add(detailsResult);


                                                    ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                    se.userDetailsResult = detailsResult;
                                                    se.scannedCard = strCardNumber;
                                                    se.isAttendanceTaken = false;
                                                    se.attendanceTakenTime = attendanceTakeTime;

                                                    MyVariables.lstScannedCard.add(se);

                                                   /*scanned_data.setText("");
                                                   scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                   for (String scancard : MyVariables.scanedCard) {
                                                       scanned_data.append("\n" + scancard);
                                                   }*/
                                                    displayScannedDetails();
                                                }

                                                mToastHandler.showToast(msg, Toast.LENGTH_LONG);

                                            } else {

                                                ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                se.userDetailsResult = detailsResult;
                                                se.scannedCard = strCardNumber;
                                                se.isAttendanceTaken = false;
                                                se.attendanceTakenTime = attendanceTakeTime;

                                                MyVariables.lstScannedCard.add(se);

                                                TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                rd.setText("Attendance not recorded of this user.");
                                            }
                                        }
                                    });
                                    //GenActivity.this.hideLastScanned();
                                }
                            }

                        });
                        // }
                    } else {
                        setStatus("Error - Details not found.", model, false);
                        Log.v("NoDetails", "Details not found.");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                AlertDialog alertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "This scanned card is not found in the system.");
                                assert alertDialog != null;
                                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
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
                setStatus("Error - " + ex.getMessage(), model, false);
                progressDialog.dismiss();
                Log.v("Error", ex.toString());
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AlertDialog alertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "This scanned card is not found or not configured in the system.");
//                        assert alertDialog != null;
//                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface arg0, int arg1) {
//
//                            }
//                        });
//                        alertDialog.show();
//                    }
//                });

            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            this.progressDialog.dismiss();
        }

        public void setResultData(String result) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e("Errrrr:", ex.toString());
                Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void displayScannedDetails() {
        TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));
        scanned_data.setText("");
        scanned_data.setText("\nYou have scanned below Card(s), today. (" + MyVariables.lstScannedCard.size() + ")");
       /*for (String scancard : MyVariables.scanedCard) {
           scanned_data.append("\n" + scancard);
       }*/

        scannedDataRecyclerViewBind = new ScannedDataRecyclerViewBind(MyVariables.lstScannedCard, getApplication());
        scannedRCV.setAdapter(scannedDataRecyclerViewBind);
        scannedRCV.setLayoutManager(new LinearLayoutManager(GenActivity.this));
        scannedRCV.setVisibility(View.VISIBLE);
    }

    public UserDetailsResult findUserFromList(String strCardNumber) {
        UserDetailsResult detailsResult = new UserDetailsResult();
        try {
            List<UserDetailsResult> userDetailsResults = new ArrayList<UserDetailsResult>();

            if (finaluserDetailsResults.size() > 0) {
                Log.v("Data2:", "Datata");
                for (UserDetailsResult us : finaluserDetailsResults) {
                    if (us.RFIDNumbers.contains(strCardNumber)) {
                        detailsResult = us;
                        break;
                    }
                }
            } else {
                SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(), MODE_PRIVATE);
                String result = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(), "");
                if (!result.isEmpty()) {
                    Log.v("Data2:", result);

                    JSONArray jsonArr = new JSONArray(result);

                    try {
                        for (int i = 0; i < jsonArr.length(); i++) {
                            JSONObject jsonObj = jsonArr.getJSONObject(i);
                            UserDetailsResult data = new UserDetailsResult();
                            if (jsonObj.has("UserID")) data.setUserID(jsonObj.getInt("UserID"));
                            if (jsonObj.has("UserName"))
                                data.setUserName(jsonObj.getString("UserName"));
                            if (jsonObj.has("UserFullName"))
                                data.setUserFullName(jsonObj.getString("UserFullName"));
                            if (jsonObj.has("UserProfileImage"))
                                data.setUserProfileImage(jsonObj.getString("UserProfileImage"));
                            if (jsonObj.has("ContactNo"))
                                data.setContactNo(jsonObj.getString("ContactNo"));
                            if (jsonObj.has("UID")) data.setUID(jsonObj.getString("UID"));
                            if (jsonObj.has("ClassName"))
                                data.setClassName(jsonObj.getString("ClassName"));
                            if (jsonObj.has("DivisionName"))
                                data.setDivisionName(jsonObj.getString("DivisionName"));
                            if (jsonObj.has("UserType"))
                                data.setUserType(jsonObj.getString("UserType"));
                            if (jsonObj.has("RFIDNumbers"))
                                data.setRFIDNumbers(jsonObj.getString("RFIDNumbers"));
                            if (jsonObj.has("IsActive"))
                                data.setActive(jsonObj.getBoolean("IsActive"));
                            if (jsonObj.has("IsDenied"))
                                data.setDenied(jsonObj.getBoolean("IsDenied"));
                            if (jsonObj.has("DeniedReason"))
                                data.setDeniedReason(jsonObj.getString("DeniedReason"));
                            if (jsonObj.has("IsParentRFIDCheckRequired"))
                                data.setParentRFIDCheckRequired(jsonObj.getBoolean("IsParentRFIDCheckRequired"));

                            if (jsonObj.has("ParentRFIDs")) {
                                JSONArray arr = jsonObj.getJSONArray("ParentRFIDs");
                                List<ParentRFID> parentRFIDs = new ArrayList<ParentRFID>();
                                for (int k = 0; k < arr.length(); k++) {
                                    JSONObject jsonObj1 = arr.getJSONObject(k);
                                    ParentRFID datap = new ParentRFID();
//                              "UserRFIDID": 0,
//                              "UserID": 59253,
//                              "RFID": "1421422489",
//                              "IsDeActive": false
                                    datap.setUserRFIDID(jsonObj1.getInt("UserRFIDID"));
                                    datap.setUserID(jsonObj1.getInt("UserID"));
                                    datap.setRFID(jsonObj1.getString("RFID"));
                                    datap.setIsDeActive(jsonObj1.getBoolean("IsDeActive"));
                                    parentRFIDs.add(datap);
                                }

                                data.setParentRFIDs(parentRFIDs);
                            }

                            if ((data.getUserName() != null && !data.getUserName().isEmpty())) {
                                userDetailsResults.add(data);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    finaluserDetailsResults = userDetailsResults;

                    for (UserDetailsResult us : userDetailsResults) {
                        if (us.RFIDNumbers.contains(strCardNumber)) {
                            detailsResult = us;
                            break;
                        }
                    }

                }
            }

        } catch (Exception ex) {
            Log.e("Find user", ex.toString());
        }
        return detailsResult;
    }


    private class LogoutCall extends AsyncTask<String, Void, Void> {
        ProgressDialog progressDialog;

        private LogoutCall() {
            this.progressDialog = null;
        }

        /* synthetic */ LogoutCall(GenActivity genActivity, LogoutCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(GenActivity.this, "Please wait...", "We are recording data..");
            super.onPreExecute();
            //getLastLocation();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(String... str) {
            try {

                Map<String, String> params = new HashMap<>();
                webService.sendGetRequest("/Security/Login/SignOut", params, new VolleyCallback() {

                    @Override
                    public void onSuccess(String result) {
                        if (!result.isEmpty()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.LOGIN_DATA.toString(), MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.SCHOOL_GROUP_MODEL.toString(), MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.MCAMPUS_TOKEN_ID.toString(), MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();


                                    sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.ALL_USER_DATA.toString(), MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.DEVICE_TYPE.toString(), MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.FRONT_CAMERA_ENABLE.toString(), MODE_PRIVATE);
                                    editor = sharedPreferences.edit();
                                    editor.clear();
                                    editor.commit();

                                    MyVariables.MCAMPUS_TOKENID = "";
                                    //MyVariables.CARD_AUTH_KEY = new byte[6];
                                    //MyVariables.SECTOR_NUMBER = 0;
                                    MyVariables.SCHOOL_GROUP_CODE = "";
                                    MyVariables.SCHOOL_WEB_URL = "";
                                    MyVariables.SCHOOL_GROUP_ID = 0;
                                    MyVariables.SCHOOL_GROUP_Name = "";
                                    MyVariables.lstScannedUsers.clear();

                                    startActivity(new Intent(GenActivity.this, SplashScreen.class));
                                    finish();
                                }
                            });
                        }
                    }
                });

            } catch (Exception ex) {
                progressDialog.dismiss();

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog alertDialog = MyVariables.getDefaultDialog(GenActivity.this, getResources().getString(R.string.app_name), "Something went wrong, Please try after sometime.");
                        assert alertDialog != null;
                        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });
                        alertDialog.show();
                    }
                });
            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            this.progressDialog.dismiss();
        }

    }

    //#endregion BackGroundTask

    private boolean checkStoragePermission() {
        int readPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return readPermission == PackageManager.PERMISSION_GRANTED && writePermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestReadWritePermissions() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_PERMISSION_CODE);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
//                    || checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        REQUEST_PERMISSION_CODE);
//            }
//        }
    }

    private void exportToCSVFile() {
        try {
            exportDataToCSV(GenActivity.this, db);
        } catch (Exception e) {
            Toast.makeText(this, "Failed to download Attendance report", Toast.LENGTH_SHORT).show();
        }
    }

    private class StudentManualAttendancePushCall extends AsyncTask<AttendanceModel, Void, Void> {
        ProgressDialog progressDialog;

        private StudentManualAttendancePushCall() {
            this.progressDialog = null;
        }

        /* synthetic */ StudentManualAttendancePushCall(GenActivity genActivity, StudentManualAttendancePushCall webCall) {
            this();
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            this.progressDialog = ProgressDialog.show(GenActivity.this, "Please wait...", "We are recording data..");
            super.onPreExecute();
            getLastLocation();
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(AttendanceModel... attendanceModels) {
            AttendanceModel model = attendanceModels[0];
            try {
                //check user is allowed for taking attendance or not
                if (!model.getScannedCard().isEmpty()) {
                    //find user from list
                    Map<String, String> params = new HashMap<>();
                    Gson gson = new Gson();
                    params.put("pRequestCriteria", "insert");
                    params.put("pRequestJsonData", model.getData());
                    // TODO
                    Log.d("AttendanceNFCMobilePush", model.getData().toString());

                    webService.sendPostRequest("/ExternalAPI/UserAttendanceNFCMobileDataPush", params, new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            Log.v("Data222", result);

                            String responseMessage = "";
                            boolean success = false;
                            UserAttendanceResponse responseModel = new UserAttendanceResponse();
                            try {
                                responseModel = gson.fromJson(result, UserAttendanceResponse.class);
                                responseMessage = responseModel.getResponseMessage();
                                success = responseModel.isSuccess();
                            } catch (Exception ex) {
                                ResponseModel responseModel2 = gson.fromJson(result, ResponseModel.class);
                                success = responseModel2.isSuccess();
                                if (responseModel2.isSuccess()) {
                                } else {
                                    responseMessage = responseModel2.getResponseMessage();
                                }
                            }

                            progressDialog.dismiss();
                            if (success) {
                                model.setSyncType("Manual");
                                setStatus("200 - " + responseMessage, model, true);
                                List<UserAttendanceDataPushResult> resultList = new ArrayList<UserAttendanceDataPushResult>();
                                resultList = responseModel.getResponseData();
                                UserAttendanceDataPushResult dataPushResult = resultList.get(0);
                                List<UserDetailsResult> userDetailsResults = new ArrayList<UserDetailsResult>();
                                UserDetailsResult detailsResult = new UserDetailsResult();

                                //find user from list
                                detailsResult = findUserFromList(model.getScannedCard());
                                if (dataPushResult.RFID == model.getScannedCard() && dataPushResult.Result == 1) {

                                    if (detailsResult.getUserID() > 0) {
                                        UserDetailsResult finalDetailsResult = detailsResult;
                                        boolean ss = success;
                                        String msg = responseMessage;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (ss) {
                                                    TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                    rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Manual Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                    //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                    //userName.setText("");

                                                    if (finalDetailsResult.UserID > 0) {
                                                        ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(model.getScannedCard());
                                                        String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                        ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                        TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                        MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + model.getScannedCard() + " | " + model.getDateTime());

                                                        ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                        se.userDetailsResult = finalDetailsResult;
                                                        se.scannedCard = model.getScannedCard();
                                                        se.isAttendanceTaken = true;
                                                        se.attendanceTakenTime = model.getDateTime();

                                                        MyVariables.lstScannedCard.add(se);

                                                        MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                        MyVariables.lstSuccessUsersList.add(finalDetailsResult);
                                                           /*scanned_data.setText("");
                                                           scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                           for (String scancard : MyVariables.scanedCard) {
                                                               scanned_data.append("\n" + scancard);
                                                           }*/
                                                        displayScannedDetails();
                                                    }
                                                    mToastHandler.showToast(msg, Toast.LENGTH_LONG);
                                                }
                                            }
                                        });
                                    } else {
                                        MyVariables.lstScannedUsers.add(detailsResult);
                                        MyVariables.lstFailedUserList.add(detailsResult);

                                        UserDetailsResult finalDetailsResult = detailsResult;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Manual Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                //userName.setText("");

                                                if (finalDetailsResult.UserID > 0) {
                                                    ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(model.getScannedCard());
                                                    String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                    TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                    MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + model.getScannedCard() + " | " + model.getDateTime());

                                                    ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                    se.userDetailsResult = finalDetailsResult;
                                                    se.scannedCard = model.getScannedCard();
                                                    se.isAttendanceTaken = true;
                                                    se.attendanceTakenTime = model.getDateTime();

                                                    MyVariables.lstScannedCard.add(se);
                                                    MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                    MyVariables.lstSuccessUsersList.add(finalDetailsResult);
                                                       /*scanned_data.setText("");
                                                       scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                       for (String scancard : MyVariables.scanedCard) {
                                                           scanned_data.append("\n" + scancard);
                                                       }*/
                                                    displayScannedDetails();
                                                }
//                                                mToastHandler.showToast("Attendance not taken for this user.", Toast.LENGTH_LONG);
                                            }
                                        });


                                    }
                                } else if (dataPushResult.RFID == model.getScannedCard() && dataPushResult.Result == 2) {
                                    MyVariables.lstScannedUsers.add(detailsResult);
                                    MyVariables.lstFailedUserList.add(detailsResult);

                                    ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                    se.userDetailsResult = detailsResult;
                                    se.scannedCard = model.getScannedCard();
                                    se.isAttendanceTaken = false;
                                    se.attendanceTakenTime = model.getDateTime();

                                    MyVariables.lstScannedCard.add(se);

                                } else {
                                    UserDetailsResult finalDetailsResult = detailsResult;
                                    boolean isSss = success;
                                    String msg = responseMessage;
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (isSss) {
                                                TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
                                                rd.setText(finalDetailsResult.getDeniedReason().isEmpty() ? "Manual Attendance recorded successfully." : finalDetailsResult.getDeniedReason());

                                                //TextView userName = ((TextView) GenActivity.this.findViewById(R.id.userName));
                                                //userName.setText("");

                                                if (finalDetailsResult.UserID > 0) {
                                                    ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(finalDetailsResult.UserFullName);
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(model.getScannedCard());
                                                    String uid = finalDetailsResult.UID.isEmpty() ? "" : finalDetailsResult.UID;
                                                    ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                    TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                    MyVariables.scanedCard.add(finalDetailsResult.UserFullName + " | " + model.getScannedCard() + " | " + model.getDateTime());

                                                    MyVariables.lstScannedUsers.add(finalDetailsResult);
                                                    ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                    se.userDetailsResult = finalDetailsResult;
                                                    se.scannedCard = model.getScannedCard();
                                                    se.isAttendanceTaken = true;
                                                    se.attendanceTakenTime = model.getDateTime();

                                                    MyVariables.lstScannedCard.add(se);
                                                       /*scanned_data.setText("");
                                                       scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                       for (String scancard : MyVariables.scanedCard) {
                                                           scanned_data.append("\n" + scancard);
                                                       }*/

                                                    displayScannedDetails();
                                                }
                                                mToastHandler.showToast(msg, Toast.LENGTH_LONG);
                                            }
                                        }
                                    });
                                    //GenActivity.this.hideLastScanned();
                                }
                            } else {
                                setStatus("Error -" + responseMessage, model, true);
                                String msg = responseMessage;
                                progressDialog.dismiss();
                                mToastHandler.showToast(msg, Toast.LENGTH_LONG);

                                UserDetailsResult detailsResult = findUserFromList(model.getScannedCard());
                                final UserAttendanceResponse res = responseModel;

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (res.getResponseData() != null) {
//                                            TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
//                                            rd.setText("Attendance not recorded of this user.");

                                            if (detailsResult.UserID > 0) {
                                                ((LinearLayout) GenActivity.this.findViewById(R.id.lnUserInformation)).setVisibility(View.VISIBLE);
                                                ((TextView) GenActivity.this.findViewById(R.id.txtName)).setText(detailsResult.UserFullName);
                                                ((TextView) GenActivity.this.findViewById(R.id.txtCardNumber)).setText(model.getScannedCard());
                                                String uid = detailsResult.UID.isEmpty() ? "" : detailsResult.UID;
                                                ((TextView) GenActivity.this.findViewById(R.id.txtUID)).setText(uid);

                                                TextView scanned_data = ((TextView) GenActivity.this.findViewById(R.id.scanned_data));

                                                MyVariables.scanedCard.add(detailsResult.UserFullName + " | " + model.getScannedCard() + " | " + "Not taken");

                                                MyVariables.lstScannedUsers.add(detailsResult);


                                                ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                                se.userDetailsResult = detailsResult;
                                                se.scannedCard = model.getScannedCard();
                                                se.isAttendanceTaken = false;
                                                se.attendanceTakenTime = model.getDateTime();

                                                MyVariables.lstScannedCard.add(se);

                                                   /*scanned_data.setText("");
                                                   scanned_data.setText("\nYou have scan below Card, today. (" + MyVariables.scanedCard.size() + ")");
                                                   for (String scancard : MyVariables.scanedCard) {
                                                       scanned_data.append("\n" + scancard);
                                                   }*/
                                                displayScannedDetails();
                                            }

                                            mToastHandler.showToast(msg, Toast.LENGTH_LONG);

                                        } else {

                                            ScannedUserDetailsResult se = new ScannedUserDetailsResult();
                                            se.userDetailsResult = detailsResult;
                                            se.scannedCard = model.getScannedCard();
                                            se.isAttendanceTaken = false;
                                            se.attendanceTakenTime = model.getDateTime();

                                            MyVariables.lstScannedCard.add(se);

//                                            TextView rd = ((TextView) GenActivity.this.findViewById(R.id.reading_data));
//                                            rd.setText("Attendance not recorded of this user.");
                                        }
                                    }
                                });
                                //GenActivity.this.hideLastScanned();
                            }
                        }

                    });
                    // }
                } else {
                    setStatus("Error - Details not found.", model, true);
                    Log.v("NoDetails", "Details not found.");
                }

            } catch (
                    Exception ex) {
                setStatus("Error - " + ex.getMessage(), model, true);
                progressDialog.dismiss();
                Log.v("Error", ex.toString());

            }
            return null;
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            this.progressDialog.dismiss();
        }

        public void setResultData(String result) {
            try {
                this.progressDialog.dismiss();
            } catch (Exception ex) {
                Log.e("Errrrr:", ex.toString());
                Toast.makeText(getApplicationContext(), "Error: " + ex.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }

    void setStatus(String msg, AttendanceModel model, boolean isUpdate) {
        model.setStatus(msg);
        if (isUpdate) {
            db.updateAttendanceList(model);
        } else {
            db.addAttendance(model);
        }
    }

    public String detectTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');

        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {

            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }

        }


        //finalString.append(sb.toString());


        return sb.toString();
    }

    public void exportDataToCSV(Context context, DatabaseHandler db) {
        // Sample data (replace with your actual data)
//        File documentsFolder;
//        String downloadsPath;

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
////                // For Android 10 (Q) and higher, use the Downloads directory
//                documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
//                downloadsPath = Environment.DIRECTORY_DOWNLOADS;
//
//            } else {
//                // For Android versions lower than 10, use the external storage directory
//                documentsFolder = Environment.getExternalStorageDirectory();
//                downloadsPath = Environment.getExternalStorageDirectory() + "/Download";
//            }

        //for Attendance Report
        if (!db.getAllAttendanceList().isEmpty()) {
            //old
//              File file = new File(documentsFolder, "attendance_sheet.csv");
////            File file = new File(downloadsPath, fileName);
//
//                FileWriter fileWriter = new FileWriter(file);
//                CSVWriter csvWriter = new CSVWriter(fileWriter);
//
//                if (!file.exists()) {
//                    String[] header = new String[]{"Id", "DateTime", "Scanned Card", "Class Name", "Name", "Device Type", "Sync Type", "Status"};
//                    csvWriter.writeNext(header);
//                }
//                // Write data
//                for (AttendanceModel model : db.getAllAttendanceList()) {
//                    String[] data = {String.valueOf(model.getId()), model.getDateTime(), model.getScannedCard(), model.getClassName(), model.getName(), model.getType(), model.getSyncType(), model.getStatus()};
//                    csvWriter.writeNext(data);
//                }
//                csvWriter.close();

            //new
            List<String[]> data = new ArrayList<>();
            data.add(new String[]{"Id", "DateTime", "Scanned Card", "Class Name", "Name", "Device Type", "Sync Type", "Status"});

            for (AttendanceModel model : db.getAllAttendanceList()) {
                data.add(new String[]{String.valueOf(model.getId()), model.getDateTime(), model.getScannedCard(), model.getClassName(), model.getName(), model.getType(), model.getSyncType(), model.getStatus()});
            }

            try {
                writeDataToCSV("attendance_sheet.csv", data);
                Toast.makeText(GenActivity.this, "CSV file saved to Downloads folder", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void requestManageExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_CODE_MANAGE_EXTERNAL_STORAGE);
            }
        }
    }

    public void writeDataToCSV(String fileName, List<String[]> data) throws IOException {
        File outputFile;
        if (isExternalStorageAvailable() && !isExternalStorageReadOnly()) {
            // External storage is available, store in Download folder
            File outputDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            File folder = new File(outputDirectory, "AttendenceApp"); // Create a new folder
            if (!folder.exists()) {
                folder.mkdirs(); // Create the folder if it doesn't exist
            }
            outputFile = new File(folder, fileName);
        } else {
            // External storage is not available, store in internal storage
            outputFile = new File(getCacheDir(), fileName);
        }

        try (FileWriter fileWriter = new FileWriter(outputFile);
             CSVWriter writer = new CSVWriter(fileWriter)) {
            writer.writeAll(data);
        }
    }

    // Create a method to check if the external storage is available and not read-only
    private boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(extStorageState);
    }

    private boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState);
    }


//    void checkUserRFIDAvailable() {
//        SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), MODE_PRIVATE);
//        String getStudentRFID = sharedPreferences.getString(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), "");
//        if (!getStudentRFID.isEmpty()) {
//            UserDetailsResult detailsResult = findUserFromList(getStudentRFID);
//            if (!detailsResult.getRFIDNumbers().isEmpty()) {
//                showAddAnotherUserPopup("Add Another card for attendance User Name : " + detailsResult.getUserName() + " User Id : " + detailsResult.getUID() + " Card Number : " + detailsResult.getRFIDNumbers());
//            } else {
//                Toast.makeText(GenActivity.this, "\"Please use Valid Card", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

    void clearRFID() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.USER_RFID.toString(), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        tapAlertDialog.dismiss();
    }

    void saveDeviceType() {
        SharedPreferences sharedPreferences = getSharedPreferences(MyVariables.DEFAULT_ENUM.DEVICE_TYPE.toString(), MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        myEdit.putString(MyVariables.DEFAULT_ENUM.DEVICE_TYPE.toString(), deviceType);
        myEdit.apply();
    }
}