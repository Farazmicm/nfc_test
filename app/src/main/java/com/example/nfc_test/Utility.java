package com.example.nfc_test;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Objects;
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



    public static long getDateDiff(SimpleDateFormat format, String oldDate, String newDate) {
        try {
            return TimeUnit.DAYS.convert(format.parse(newDate).getTime() - format.parse(oldDate).getTime(), TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }


    public static boolean checkInternet(Context context) {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Objects.requireNonNull(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)).getState() == NetworkInfo.State.CONNECTED || connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        return connected;
    }

    public static void openInternetNotAvailable(Context context, String message) {
        if (message.isEmpty()) {
            message = "Internet connection is not available. Please connect to internet.";
        }
        AlertDialog alertDialog = MyVariables.getDefaultDialog(context, "", message);
        assert alertDialog != null;
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertDialog.show();
    }

}