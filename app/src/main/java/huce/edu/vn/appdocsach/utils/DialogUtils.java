package huce.edu.vn.appdocsach.utils;

import android.app.AlertDialog;
import android.content.Context;

import huce.edu.vn.appdocsach.R;

public class DialogUtils {

    private static AlertDialog notification, error;

    public static void notification(Context context, String message) {
        if (notification == null) {
            notification = new AlertDialog.Builder(context)
//                    .setIcon(R.drawable.)
                    .setTitle("Thông báo")
                    .setMessage(message)
                    .create();
        }
        notification.show();
    }

    public static void error(Context context, String message) {
        if (error == null) {
            error = new AlertDialog.Builder(context)
                    .setTitle("Lỗi")
                    .setMessage(message)
                    .create();
        }
        error.show();
    }


}
