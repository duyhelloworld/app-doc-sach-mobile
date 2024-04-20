package huce.edu.vn.appdocsach.utils;

import android.app.AlertDialog;
import android.content.Context;

import huce.edu.vn.appdocsach.R;

public class DialogUtils {
    private static AlertDialog notification, error, debug;

    public static void notification(Context context, String message) {
        if (notification == null) {
            notification = error = new AlertDialog.Builder(context)
                    .setTitle("Thông báo")
                    .setMessage(message)
                    .setIcon(R.drawable.info)
                    .create();
        }
        notification.show();
    }

    public static void error(Context context, String message) {
        if (error == null) {
            error = new AlertDialog.Builder(context)
                    .setTitle("Lỗi")
                    .setMessage(message)
                    .setIcon(R.drawable.error)
                    .create();
        }
        error.show();
    }

    public static void error(Context context, Throwable throwable) {
        if (error == null) {
            error = new AlertDialog.Builder(context)
                    .setTitle("Lỗi")
                    .setMessage(throwable.getMessage())
                    .setIcon(R.drawable.error)
                    .create();
        }
        error.show();
    }

    public static void debug(Context context, Throwable throwable) {
        if (debug == null) {
            debug = new AlertDialog.Builder(context)
                    .setTitle("Kiểm thử")
                    .setMessage(throwable.getMessage())
                    .setIcon(R.drawable.debug)
                    .create();
        }
        debug.show();
    }

    public static void debug(Context context, String message) {
        if (debug == null) {
            debug = new AlertDialog.Builder(context)
                    .setTitle("Kiểm thử")
                    .setMessage(message)
                    .setIcon(R.drawable.debug)
                    .create();
        }
        debug.show();
    }
}
