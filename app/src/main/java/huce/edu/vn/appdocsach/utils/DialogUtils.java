package huce.edu.vn.appdocsach.utils;

import android.app.AlertDialog;
import android.content.Context;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;

public class DialogUtils {

    private static void show(AlertType alertType, Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setMessage(message);
        switch (alertType) {
            case DEBUG:
                builder.setTitle("Debug")
                        .setIcon(R.drawable.debug)
                        .setPositiveButton("Ok", (dialog, which) -> dialog.cancel())
                        .create().show();
                break;
            case INFO:
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

    public static void error(Context context, Throwable throwable) {
        show(AlertType.ERROR, context, throwable.getMessage());
    }

    public static void error(Context context, String prefix, Throwable throwable) {
        show(AlertType.ERROR, context, prefix + throwable.getMessage());
    }

    public static void info(Context context, String message) {
        show(AlertType.INFO, context, message);
    }

    public static void debug(Context context, Object obj) {
        show(AlertType.DEBUG, context, obj.getClass().getName() + " : " + GsonCustom.getInstance().toJson(obj));
    }
}
