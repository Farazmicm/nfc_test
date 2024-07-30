package com.example.nfc_test.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.nfc_test.models.AttendanceModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "edusprint_attendance_db";
    private static final String TABLE_ATTENDANCE = "attendanceList";
    private static final String TABLE_ATTENDANCE_LOG = "attendanceLog";
    private static final String KEY_ID = "id";
    private static final String KEY_DATETIME = "date_time";
    private static final String KEY_SCANNED_CARD = "scanned_card";
    private static final String KEY_CLASSNAME = "classname";
    private static final String KEY_NAME = "name";
    private static final String KEY_TYPE = "user_type";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATA = "data";
    private static final String KEY_SYNC_TYPE = "key_sync_type";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_ATTENDANCE_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE
                + "("
                + KEY_ID + " INTEGER ,"
                + KEY_DATETIME + " TEXT,"
                + KEY_SCANNED_CARD + " TEXT,"
                + KEY_CLASSNAME + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_TYPE + " TEXT,"
                + KEY_STATUS + " TEXT,"
                + KEY_SYNC_TYPE + " TEXT,"
                + KEY_DATA + " TEXT"
                + ")";
        db.execSQL(CREATE_ATTENDANCE_TABLE);

//        String CREATE_ATTENDANCE_LOG_TABLE = "CREATE TABLE " + TABLE_ATTENDANCE_LOG + "("
//                + KEY_ID + " INTEGER ,"
//                + KEY_DATETIME + " TEXT,"
//                + KEY_SCANNED_CARD + " TEXT,"
//                + KEY_NAME + " TEXT"
//                + ")";
//        db.execSQL(CREATE_ATTENDANCE_LOG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ATTENDANCE_LOG);
        onCreate(db);
    }

    public void addAttendance(AttendanceModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, contact.getId());
        values.put(KEY_DATETIME, contact.getDateTime());
        values.put(KEY_SCANNED_CARD, contact.getScannedCard());
        values.put(KEY_CLASSNAME, contact.getClassName());
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_TYPE, contact.getType());
        values.put(KEY_STATUS, contact.getStatus());
        values.put(KEY_SYNC_TYPE, contact.getSyncType());
        values.put(KEY_DATA, contact.getData());

        db.insert(TABLE_ATTENDANCE, null, values);
        db.close();
    }

//    public void addAttendanceLog(AttendanceModel contact) {
//        SQLiteDatabase db = this.getWritableDatabase();
//
//        ContentValues values = new ContentValues();
//        values.put(KEY_ID, contact.getId());
//        values.put(KEY_DATETIME, contact.getDateTime());
//        values.put(KEY_SCANNED_CARD, contact.getScannedCard());
//        values.put(KEY_NAME, contact.getName());
//
//        db.insert(TABLE_ATTENDANCE_LOG, null, values);
//        db.close();
//    }

    public List<AttendanceModel> getAllAttendanceList() {
        List<AttendanceModel> modelList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                AttendanceModel model = new AttendanceModel();
                model.setId(Integer.parseInt(cursor.getString(0)));
                model.setDateTime(cursor.getString(1));
                model.setScannedCard(cursor.getString(2));
                model.setClassName(cursor.getString(3));
                model.setName(cursor.getString(4));
                model.setType(cursor.getString(5));
                model.setStatus(cursor.getString(6));
                model.setSyncType(cursor.getString(7));
                model.setData(cursor.getString(8));
                modelList.add(model);
            } while (cursor.moveToNext());
        }
        return modelList;
    }

//    public List<AttendanceModel> getAllAttendanceLogList() {
//        List<AttendanceModel> modelList = new ArrayList<>();
//        String selectQuery = "SELECT  * FROM " + TABLE_ATTENDANCE_LOG;
//        SQLiteDatabase db = this.getWritableDatabase();
//        Cursor cursor = db.rawQuery(selectQuery, null);
//
//        if (cursor.moveToFirst()) {
//            do {
//                AttendanceModel model = new AttendanceModel();
//                model.setId(Integer.parseInt(cursor.getString(0)));
//                model.setDateTime(cursor.getString(1));
//                model.setScannedCard(cursor.getString(2));
//                model.setName(cursor.getString(3));
//                modelList.add(model);
//            } while (cursor.moveToNext());
//        }
//        return modelList;
//    }

    AttendanceModel getAttendance(int id) {
        String query = "SELECT  * FROM " + TABLE_ATTENDANCE + " WHERE ID=" + ++id;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);

        AttendanceModel model = null;

        if (cursor.moveToFirst()) {
            model = new AttendanceModel();
            model.setId(Integer.parseInt(cursor.getString(0)));
            model.setDateTime(cursor.getString(1));
            model.setScannedCard(cursor.getString(2));
            model.setClassName(cursor.getString(3));
            model.setName(cursor.getString(4));
            model.setType(cursor.getString(5));
            model.setStatus(cursor.getString(6));
            model.setSyncType(cursor.getString(7));
            model.setData(cursor.getString(8));
        }

        return model;
    }

    public int updateAttendanceList(AttendanceModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, model.getId());
        values.put(KEY_DATETIME, model.getDateTime());
        values.put(KEY_SCANNED_CARD, model.getScannedCard());
        values.put(KEY_CLASSNAME, model.getClassName());
        values.put(KEY_NAME, model.getName());
        values.put(KEY_TYPE, model.getType());
        values.put(KEY_STATUS, model.getStatus());
        values.put(KEY_SYNC_TYPE, model.getSyncType());
        values.put(KEY_DATA, model.getData());

        return db.update(TABLE_ATTENDANCE, values, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
    }

    public void deleteAttendanceList(AttendanceModel model) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTENDANCE, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
        db.close();
    }

//    public void deleteAttendanceLogList(AttendanceModel model) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ATTENDANCE_LOG, KEY_ID + "=?", new String[]{String.valueOf(model.getId())});
//        db.close();
//    }

    public int getAttendanceListCount() {
        try {
            String countQuery = "SELECT  * FROM " + TABLE_ATTENDANCE;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            cursor.close();
            return cursor.getCount();
        } catch (Exception e) {
            return 0;
        }
    }

    public void deleteAttendanceAllRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ATTENDANCE, null, null);
        db.close();
    }

//    public void deleteAttendanceLogAllRecords() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_ATTENDANCE_LOG, null, null);
//        db.close();
//    }
}