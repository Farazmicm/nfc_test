package com.example.nfc_test;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.Html;
import android.util.Log;

import com.android.volley.Request;
import com.example.nfc_test.models.ScannedUserDetailsResult;
import com.example.nfc_test.models.TokenModel;
import com.example.nfc_test.models.UserDetailsResult;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MyVariables extends Application {
    public static String ACTIVITY = "00";
    public static BackgroundTask BACKGROUNDTASK = null;
    public static String BUS_FACILITY_CODE = XmlPullParser.NO_NAMESPACE;
    public static String BUS_ID = XmlPullParser.NO_NAMESPACE;
    public static String BUS_ISSUE_LEVEL = XmlPullParser.NO_NAMESPACE;

    public static String ESCORT_FACILITY_CODE = XmlPullParser.NO_NAMESPACE;
    public static String ESCORT_ID = XmlPullParser.NO_NAMESPACE;
    public static String ESCORT_ISSUE_LEVEL = XmlPullParser.NO_NAMESPACE;
    public static String FILE = XmlPullParser.NO_NAMESPACE;
    public static final String FILE_NAME = "log.txt";
    public static final String FOLDER_NAME = "dais";
    public static String HOST = XmlPullParser.NO_NAMESPACE;
    public static String IMEI = XmlPullParser.NO_NAMESPACE;
    public static String LATITUDE = "00.0000";
    public static String LONGITUDE = "00.0000";
    public static String NETWORK_MODE = "true";
    public static String PORT = "443";
    public static boolean READING_TAG = false;

    public static String STUDENT_FACILITY_CODE = XmlPullParser.NO_NAMESPACE;
    public static String STUDENT_ID = XmlPullParser.NO_NAMESPACE;
    public static String STUDENT_ISSUE_LEVEL = XmlPullParser.NO_NAMESPACE;
    public static Thread THREAD = null;
    public static int TIMEOUT = 60000;
    static final String WEB_URL = "https://gps.da-is.org/4rschooltest/PostData.svc?wsdl";

    public static String deviceID = "";
    public static boolean IsProduction = false;

    String KEY1 = "987654321987";
    String KEY2 = "543215432154";
    String KEY3 = "765432176543"; //000241414d59 - 000e45434d59 // 0e45434d59594a
    //public static byte[] CARD_AUTH_KEY = {(byte)0x4a, (byte)0x59, (byte)0x59, (byte)0x4d, (byte)0x43, (byte)0x45}; //new byte[6]
    //public static int SECTOR_NUMBER = 14; //0

    //public static byte[] CARD_AUTH_KEY = {(byte)0x00, (byte)0x0e, (byte)0x45, (byte)0x43, (byte)0x4d, (byte)0x59}; //new byte[6]
    //public static int SECTOR_NUMBER = 5; //0

    //M-3 : a9b8c7d6e0ff : Sector: 14

    public static byte[] DEFAULT_CARD_AUTH_KEY = {(byte) 0xa9, (byte) 0xb8, (byte) 0xc7, (byte) 0xd6, (byte) 0xe0, (byte) 0xff};
    public static int DEFAULT_SECTOR_NUMBER = 14;

    public static byte[] CARD_AUTH_KEY = new byte[6];
    public static int SECTOR_NUMBER = 0;

    public static boolean isDefaultCardUse = true;
    public static boolean isMasterCardRead = false;

    public static ArrayList<String> scanedCard = new ArrayList<String>();
    public static ArrayList<ScannedUserDetailsResult> lstScannedCard = new ArrayList<ScannedUserDetailsResult>();

    /* renamed from: bt */
    public static final byte[] key1 = {-104, 118, 84, 50, 25, -121};
    public static final byte[] key2 = {84, 50, 21, 67, 33, 84};
    public static final byte[] key3 = {118, 84, 50, 23, 101, 67};

    /*public static final byte[] key1 = {(byte)0x98, (byte)0x76, (byte)0x54, (byte)0x32, (byte)0x19, (byte)0x87};
    public static final byte[] key2 = {(byte)0x54, (byte)0x32, (byte)0x15, (byte)0x43, (byte)0x21, (byte)0x54};
    public static final byte[] key3 = {(byte)0x76, (byte)0x54, (byte)0x32, (byte)0x17, (byte)0x65, (byte)0x43};*/

    public static String SCHOOL_GROUP_Name = "";
    public static String SCHOOL_GROUP_CODE = "";
    public static String MOBILE_API_URL = "https://mobileapisecure.edusprint.in/";// "http://mobileapiv2.edusprint.in/";
    public static String SCHOOL_WEB_URL = "";
    public static String MCAMPUS_TOKENID = "";
    public static int USER_ID = 0;
    public static int SCHOOL_GROUP_ID = 0;
    public static String USER_FULL_NAME = "";

    //For Captcha verification
    public static String CAPTCH_LOAD_URL = "/Uploader/Captcha?key=LoginCaptcha&w=300";
    public static String CAPTCH_RELOAD_URL = "/Home/ResetCaptchaValue?key=LoginCaptcha";

    public static final byte[][] f17bt = {key1, key2, key3};

    public static List<UserDetailsResult> lstScannedUsers = new ArrayList<UserDetailsResult>();
    public static List<UserDetailsResult> lstSuccessUsersList = new ArrayList<UserDetailsResult>();
    public static List<UserDetailsResult> lstFailedUserList = new ArrayList<UserDetailsResult>();

    public static String DEVICE_TYPE = "CAR"; //CAR or GATE

    public static final String namespace = "http://tempuri.org/";

    public void onCreate() {
        super.onCreate();
        String s1 = WEB_URL.substring(WEB_URL.indexOf("://") + 3);
        HOST = s1.substring(0, s1.indexOf("/"));
        FILE = s1.substring(s1.indexOf(HOST) + HOST.length());

    }

    public static String getSystemDateTime() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(1);
            int dateformat = calendar.get(5);
            int hours = calendar.get(11);
            int minutes = calendar.get(12);
            int sec = calendar.get(13);
            String da = Integer.toString(dateformat);
            String mont = Integer.toString(calendar.get(2) + 1);
            String hor = Integer.toString(hours);
            String min = Integer.toString(minutes);
            String secs = Integer.toString(sec);
            if (da.trim().length() == 1) {
                da = "0" + da;
            }
            if (mont.trim().length() == 1) {
                mont = "0" + mont;
            }
            if (hor.trim().length() == 1) {
                hor = "0" + hor;
            }
            if (min.trim().length() == 1) {
                min = "0" + min;
            }
            if (secs.trim().length() == 1) {
                secs = "0" + secs;
            }
            return Integer.toString(year) + "-" + mont + "-" + String.valueOf(da) + " " + hor + ":" + min + ":" + secs;
        } catch (Exception e) {
            return "01011970000000";
        }
    }

    public static String getSystemTime() {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            int year = calendar.get(1);
            int dateformat = calendar.get(5);
            int hours = calendar.get(11);
            int minutes = calendar.get(12);
            int sec = calendar.get(13);
            String da = Integer.toString(dateformat);
            String mont = Integer.toString(calendar.get(2) + 1);
            String hor = Integer.toString(hours);
            String min = Integer.toString(minutes);
            String secs = Integer.toString(sec);
            if (da.trim().length() == 1) {
                da = "0" + da;
            }
            if (mont.trim().length() == 1) {
                mont = "0" + mont;
            }
            if (hor.trim().length() == 1) {
                hor = "0" + hor;
            }
            if (min.trim().length() == 1) {
                min = "0" + min;
            }
            if (secs.trim().length() == 1) {
                secs = "0" + secs;
            }
            return hor + ":" + min + ":" + secs;
        } catch (Exception e) {
            return "01011970000000";
        }
    }

    public static void trustAllHttpsCertificates() throws Exception {
        TrustManager[] trustAllCerts = {new miTM()};
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init((KeyManager[]) null, trustAllCerts, (SecureRandom) null);
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

    public static class miTM implements TrustManager, X509TrustManager {
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] certs) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] certs) {
            return true;
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
        }
    }

    public static void writeFile(Context context, String FILE_NAME2, String data) {
        String path = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + File.separator + FOLDER_NAME;
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            String path2 = String.valueOf(path) + File.separator + FILE_NAME2;
            File file2 = new File(path2);
            try {
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                MediaScannerConnection.scanFile(context, new String[]{path2.toString()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
                FileOutputStream stream = new FileOutputStream(file2, true);
                stream.write((String.valueOf(data) + "\n").getBytes());
                stream.flush();
                stream.close();
                File file3 = file2;
            } catch (Exception e) {
                File file4 = file2;
            }
        } catch (Exception e2) {
        }
    }

    public static void writeFile(Context context, String FILE_NAME2, String[] data) {
        String path = String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath()) + File.separator + FOLDER_NAME;
        File file = new File(path);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
            String path2 = String.valueOf(path) + File.separator + FILE_NAME2;
            File file2 = new File(path2);
            try {
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                MediaScannerConnection.scanFile(context, new String[]{path2.toString()}, (String[]) null, (MediaScannerConnection.OnScanCompletedListener) null);
                FileOutputStream stream = new FileOutputStream(file2, true);
                for (int i = 0; i < data.length; i++) {
                    stream.write((String.valueOf(data[i]) + "\r\n").getBytes());
                    stream.flush();
                }
                stream.close();
                File file3 = file2;
            } catch (Exception e) {
                File file4 = file2;
            }
        } catch (Exception e2) {
        }
    }

    public static class GetFormattedValues {
        String error;
        String[] key;
        String resp = XmlPullParser.NO_NAMESPACE;
        String[] value;

        public GetFormattedValues(String[] key2, String[] value2, String resp2, String error2) {
            this.key = key2;
            this.value = value2;
            this.resp = resp2;
            this.error = error2;
        }

        public String toString() {
            String temp = XmlPullParser.NO_NAMESPACE;
            for (int i = 0; i < this.key.length; i++) {
                temp = String.valueOf(this.key[i]) + "=" + this.value[i] + ",";
            }
            return String.valueOf(temp) + "Response=" + this.resp + "Error" + this.error;
        }
    }


    public static String getMCampusToken(Context context) {
        try {

            /*String pass = encryptPassword("pass");

            String password123 = encryptPassword("password123");

            Log.v("pass-enc", pass);
            Log.v("123-enc", password123);

            Log.v("pass-dec", decryptPassword(pass));
            Log.v("123-dec", decryptPassword(password123));*/

            WebService webService = new WebService();
            webService.URL = SCHOOL_WEB_URL + SCHOOL_GROUP_CODE;
            Map<String, String> params = new HashMap<String, String>();
            //Log.v("URL:",webService.URL);
            webService.sendWebRequest(context, "/Mobile/GetNewToken?IMR=1&Cookies=IMR=1", Request.Method.GET, params, new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    Gson gson = new Gson();
                    try{
                        if (!result.isEmpty()) {
                            TokenModel tokenModel = gson.fromJson(result, TokenModel.class);
                            if (tokenModel.isSuccess()) {
                                MCAMPUS_TOKENID = tokenModel.getResponseData();
                                Log.e("Response", result);
                            }
                        }
                    }catch (Exception e){
                        Log.e("Token Error", "Token Not genrated");
                    }
                }
            });
            Thread.sleep(2000);

        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.e("Token Error", ex.toString());
            }
        }
        return MCAMPUS_TOKENID;
    }

    public static androidx.appcompat.app.AlertDialog getDefaultDialog(Context context, String title, String message) {
        try {
            androidx.appcompat.app.AlertDialog alertDialog1 = new androidx.appcompat.app.AlertDialog.Builder(context).create();

            // Setting Dialog Title
            alertDialog1.setTitle(title.isEmpty() ? context.getResources().getString(R.string.app_name) : title);

            // Setting Dialog Message

            alertDialog1.setMessage(message.isEmpty() ? Html.fromHtml("<span style=color:black>" + "Please wait.." + "</span>") : Html.fromHtml("<span style=color:black>" + message + "</span>"));

            // Setting Icon to Dialog
            alertDialog1.setIcon(R.drawable.splashlogo);

            return alertDialog1;
        } catch (Exception ex) {
            if (!MyVariables.IsProduction) {
                Log.i("Alert Dialog", ex.toString());
            }
            return null;
        }
    }


    public enum DEFAULT_ENUM {
        SCHOOL_GROUP_MODEL,
        LOGIN_DATA,
        USERNAME,
        PASSWORD,
        SCHOOL_GROUP_ID,
        SCHOOL_GROUP_CODE,
        SCHOOL_WEB_URL,
        MCAMPUS_TOKEN_ID,
        MASTER_CARD_DATA,
        SECTOR_NUMBER,
        CARD_AUTH_KEY,
        ALL_USER_DATA,
        SCANNED_USERS_LIST,
        SUCCESS_SCANNED_USERS_LIST,
        FAILED_SCANNED_USERS_LIST,
        ISDEFAULTMASTERCARDUSE,
        USER_RFID,
        DEVICE_TYPE,
    }

    public static Drawable getCaptchaImage() {
        try {

            if (!MyVariables.MCAMPUS_TOKENID.isEmpty()) {
                String url = SCHOOL_WEB_URL + SCHOOL_GROUP_CODE + CAPTCH_LOAD_URL + "&_c=xcYYY01223";
                Log.v("captchaIURL", url);
                URL url1 = new URL(url);
                URLConnection connection = url1.openConnection();
                Log.v("MCampusTOjken: ", MyVariables.MCAMPUS_TOKENID);
                connection.setRequestProperty("MCampusTokenID", MyVariables.MCAMPUS_TOKENID);
                connection.setRequestProperty("Cookies", "MCampusTokenID=" + MyVariables.MCAMPUS_TOKENID);
                connection.connect();
                InputStream urlStream = connection.getInputStream();

                Drawable d = Drawable.createFromStream(urlStream, "srcCompact");

                return d;
            } else return null;

        } catch (Exception e) {
            if (!MyVariables.IsProduction) {
                Log.e("Captcha", e.toString());
            }
            return null;
        }
    }

    public static boolean isValidPassword(final String password) {
        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[*.!@$%^&(){}:;<>,?/_=|]).{8,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);
        return matcher.matches();
    }


}

//class AESEnc {
//    //Password Encryption
//    //"AN8S6C", "CPTAK4E"
//    private static String SALT = "s68ayjd1m6RNkvHZ";
//    private static String SECRET_KEY = "cEwLVkXKHVr8ngm4";
//
//    @SuppressLint("NewApi")
//    public static String encryptPassword(String password) {
//        try {
//            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//            IvParameterSpec ivspec = new IvParameterSpec(iv);
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
//            return Base64.getEncoder()
//                    .encodeToString(cipher.doFinal(password.getBytes(StandardCharsets.UTF_8)));
//        } catch (Exception e) {
//            System.out.println("Error while encrypting: " + e.toString());
//        }
//        return "";
//    }
//
//    @SuppressLint("NewApi")
//    public static String decryptPassword(String strToDecrypt) {
//        try {
//            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//            IvParameterSpec ivspec = new IvParameterSpec(iv);
//
//            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
//            KeySpec spec = new PBEKeySpec(SECRET_KEY.toCharArray(), SALT.getBytes(), 65536, 256);
//            SecretKey tmp = factory.generateSecret(spec);
//            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//
//            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
//            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
//            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
//        } catch (Exception e) {
//            System.out.println("Error while decrypting: " + e.toString());
//        }
//        return "";
//    }
//}






