package com.example.nfc_test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            // Internet is available, do nothing or handle accordingly
        } else {
            // No internet connection, show a dialog
            openInternetNotAvailable(context,"");
//            AlertDialog.Builder builder = new AlertDialog.Builder(context);
//            builder.setTitle("No Internet Connection")
//                    .setMessage("Please check your internet connection and try again.")
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .setCancelable(false);
//
//            AlertDialog alertDialog = builder.create();
//            alertDialog.show();
        }
    }

    public void openInternetNotAvailable(Context context, String message) {
        if (message.isEmpty()) {
            message = "Internet connection is not available. Please connect to internet.";
        }
        androidx.appcompat.app.AlertDialog alertDialog = MyVariables.getDefaultDialog(context, "", message);
        assert alertDialog != null;
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
            }
        });
        alertDialog.show();
    }
}
