package huce.edu.vn.appdocsach.utils;

import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.StringRes;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.utils.serializers.JsonSerializer;

public class DialogUtils {
    private static boolean isConfirmed = false;
    private static void show(AlertType alertType, Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        switch (alertType) {
            case DEBUG:
                builder.setTitle("Debug")
                        .setIcon(R.drawable.debug)
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .create().show();
                break;
            case INFO:
                builder.setTitle("Thông báo")
                        .setIcon(R.drawable.info)
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .create().show();
                break;
            case ERROR:
                builder.setTitle("Lỗi")
                        .setIcon(R.drawable.error)
                        .setPositiveButton("Ok", (dialog, which) -> dialog.dismiss())
                        .create().show();
                break;
            case CONFIRM:
                builder.setPositiveButton("OK", (dialog, which) -> isConfirmed = true)
                        .setNegativeButton("Hủy", (dialog, which) -> isConfirmed = false)
                        .setTitle("Xác nhận").create().show();
                break;
            case WARN:
                builder.setTitle("Cảnh báo")
                        .create().show();
        }
    }

    public static boolean confirm(Context context, String message) {
        show(AlertType.CONFIRM, context, message);
        return isConfirmed;
    }

    public static void infoUserSee(Context context, @StringRes int id) {
        show(AlertType.INFO, context, context.getString(id));
    }

    public static void errorUserSee(Context context, @StringRes int id) {
        show(AlertType.ERROR, context, context.getString(id));
    }

    public static void errorUserSee(Context context, String message) {
        show(AlertType.ERROR, context, message);
    }

    public static void errorDevSee(Context context, String tag, Throwable throwable) {
        show(AlertType.DEBUG, context, tag + ":" + throwable.getMessage());
    }

    public static void infoDevSee(Context context, String tag, Object obj) {
        show(AlertType.DEBUG, context, tag + ":\n" + obj.getClass().getName() + " : " + JsonSerializer.getInstance().toJson(obj));
    }
}
