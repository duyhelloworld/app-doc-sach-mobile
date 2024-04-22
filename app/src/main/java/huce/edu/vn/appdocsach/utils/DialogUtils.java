package huce.edu.vn.appdocsach.utils;

import android.app.AlertDialog;
import android.content.Context;

import huce.edu.vn.appdocsach.R;

public class DialogUtils {

    public static void show(AlertType alertType, Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        switch (alertType) {
            case DEBUG:
                builder.setTitle("Kiểm thử")
                        .setIcon(R.drawable.debug)
                        .create().show();
                break;
            case NOTIFICATION:
                builder.setTitle("Thông báo")
                    .setIcon(R.drawable.info)
                    .create().show();
                break;
            case ERROR:
                builder.setTitle("Lỗi")
                        .setIcon(R.drawable.error)
                        .create().show();
                break;
        }
    }
}
