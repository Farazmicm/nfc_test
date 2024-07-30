package com.example.nfc_test;

import android.content.Context;


public class BackgroundTask implements Runnable {
    static boolean START_THREAD = true;
    Context context;

    public BackgroundTask(Context context2) {
        this.context = context2;
    }

    public void run() {
        /*Cursor cursor = null;
        try {
            SQLiteDatabase database = TransactionTable.getDatabase(this.context);
            database.enableWriteAheadLogging();
            while (START_THREAD) {
                cursor = database.query(TransactionTable.table_name, (String[]) null, (String) null, (String[]) null, (String) null, (String) null, (String) null);
                cursor.moveToFirst();
                if (cursor.getCount() > 0) {
                    StringTokenizer stringTokenizer = new StringTokenizer(cursor.getString(cursor.getColumnIndex(TransactionTable.trans)).trim().trim());
                    stringTokenizer.setDelimiters("$");
                    if (callWebService(MyVariables.namespace, "Insert_StudentTransaction", new String[]{"StudentUID", "AttendUID", "BusUID", "IMEI", "EventFlag", "Lat", "Lon", "TransDate", "Online", "sFacilityCode", "sIssueLevel", "bFacilityCode", "bIssueLevel", "aFacilityCode", "aIssueLevel"}, new String[]{stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim(), stringTokenizer.nextToken().trim()}, "https://gps.da-is.org/4rschooltest/PostData.svc?wsdl").trim().equals("YES")) {
                        database.delete(TransactionTable.table_name, "id='" + cursor.getString(cursor.getColumnIndex("id")) + "'", (String[]) null);
                    }
                }
                cursor.close();
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            if (cursor != null) {
                if (!cursor.isClosed()) {
                    cursor.close();
                    cursor = null;
                }
            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            throw th;
        }*/
    }

    /*private String callWebService(String namespace, String methode_name, String[] key, String[] value, String url) {
        SoapObject result = null;
        try {
            String SOAP_ACTION1 = "http://tempuri.org/IPostData/" + methode_name;
            SoapObject request = new SoapObject(namespace, methode_name);
            for (int i = 0; i < key.length; i++) {
                request.addProperty(key[i], value[i]);
            }
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            try {
                new HttpTransportSE(url).call(SOAP_ACTION1, envelope);
                SoapObject result2 = (SoapObject) envelope.bodyIn;
                if (result2 != null) {
                    try {
                        MyVariables.writeFile(this.context, MyVariables.FILE_NAME, new MyVariables.GetFormattedValues(key, value, result2.getProperty(0).toString().trim(), "No Exception").toString());
                    } catch (Exception e) {
                    }
                    return result2.getProperty(0).toString().trim();
                }
                try {
                    MyVariables.writeFile(this.context, MyVariables.FILE_NAME, new MyVariables.GetFormattedValues(key, value, result2.getProperty(0).toString().trim(), "No Exception").toString());
                } catch (Exception e2) {
                }
                return "S_ERROR";
            } catch (Exception e3) {
                try {
                    MyVariables.writeFile(this.context, MyVariables.FILE_NAME, new MyVariables.GetFormattedValues(key, value, result.getProperty(0).toString().trim(), e3.toString()).toString());
                } catch (Exception e4) {
                }
                return "NT_ERROR";
            }
        } catch (Exception e5) {
            try {
                MyVariables.writeFile(this.context, MyVariables.FILE_NAME, new MyVariables.GetFormattedValues(key, value, result.getProperty(0).toString().trim(), e5.toString()).toString());
            } catch (Exception e6) {
            }
            return "NT_ERROR";
        }
    }*/
}
