package com.tepav.reader.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.tepav.reader.R;

/**
 * Created with IntelliJ IDEA.
 * User: Umut Ozan Yıldırım
 * Date: 06/02/14
 * Time: 21:48
 */
public class Util {

    public static String getSetterMethodName(String field) {
        return "set" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null);
    }


    public static  void createAlertDialog(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(context.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
