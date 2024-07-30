package com.example.nfc_test;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import com.example.nfc_test.db.DatabaseHandler;
import com.example.nfc_test.models.AttendanceModel;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

public class Utility {

    public static byte[] calculateResult(byte[][] bt) {
        byte[] b1 = bt[0];
        byte[] b2 = bt[1];
        byte[] b3 = bt[2];
        byte[] r1 = new byte[b1.length];
        byte[] r2 = new byte[b3.length];
        for (int i = 0; i < b1.length; i++) {
            r1[i] = (byte) (b1[i] & b2[i]);
        }
        for (int i2 = 0; i2 < b3.length; i2++) {
            r2[i2] = (byte) (r1[i2] | b3[i2]);
        }
        return r2;
    }

    public static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append("");
            }
        }
        return sb.toString();
    }

    public static String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append("");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    public static long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public static long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    public static String hexToBinary(String s) {
        return new BigInteger(s, 16).toString(2);
    }

    public static byte[] binaryToBytes(String b) {
        short a = Short.parseShort(b, 2);
        ByteBuffer bytes = ByteBuffer.allocate(2).putShort(a);

        byte[] array = bytes.array();
        return array;
    }

    public static void exportDataToCSV(Context context, DatabaseHandler db) {
        // Sample data (replace with your actual data)
        try {
            File documentsFolder;
            String downloadsPath;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//                // For Android 10 (Q) and higher, use the Downloads directory
                documentsFolder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                downloadsPath = Environment.DIRECTORY_DOWNLOADS;

            } else {
                // For Android versions lower than 10, use the external storage directory
                documentsFolder = Environment.getExternalStorageDirectory();
                downloadsPath = Environment.getExternalStorageDirectory() + "/Download";
            }

            //for Attendance Report
            if (!db.getAllAttendanceList().isEmpty()) {
                File file = new File(documentsFolder, "attendance_sheet.csv");
//            File file = new File(downloadsPath, fileName);

                FileWriter fileWriter = new FileWriter(file);
                CSVWriter csvWriter = new CSVWriter(fileWriter);

                String[] header = new String[]{"Id", "DateTime", "Scanned Card", "Class Name", "Name", "Device Type", "Sync Type", "Status"};
                csvWriter.writeNext(header);

                // Write data
                for (AttendanceModel model : db.getAllAttendanceList()) {
                    String[] data = {String.valueOf(model.getId()), model.getDateTime(), model.getScannedCard(), model.getClassName(), model.getName(), model.getType(), model.getSyncType(), model.getStatus()};
                    csvWriter.writeNext(data);
                }
                csvWriter.close();
            }
            //for Attendence Log
//            if (!db.getAllAttendanceLogList().isEmpty()) {
//                File attendanceLogFile = new File(documentsFolder, "attendance_log.csv");
//
//                FileWriter fileWriter1 = new FileWriter(attendanceLogFile);
//                CSVWriter csvWriter1 = new CSVWriter(fileWriter1);
//
//                String[] header1 = new String[]{"Id", "DateTime", "Scanned Card", "Status"};
//                csvWriter1.writeNext(header1);
//
//                // Write data
//                for (AttendanceModel model : db.getAllAttendanceLogList()) {
//                    String[] data = {String.valueOf(model.getId()), model.getDateTime(),model.getScannedCard(), model.getName()};
//                    csvWriter1.writeNext(data);
//                }
//                csvWriter1.close();
//            }
            Toast.makeText(context, "CSV file saved to Downloads folder", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }

}