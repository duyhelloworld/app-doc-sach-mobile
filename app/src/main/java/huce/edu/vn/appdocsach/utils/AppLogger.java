package huce.edu.vn.appdocsach.utils;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;

public class AppLogger {
    private static AppLogger util;
    private final Gson gson = GsonCustom.getInstance();

    private AppLogger() {}
    public static AppLogger getInstance() {
        if (util == null) {
            util = new AppLogger();
        }
        return  util;
    }

    public void info(Object... objects) {
        Log.i("InfoObject", gson.toJson(objects));
    }

    public void error(@NonNull Throwable throwable) {
        Log.e(gson.toJson(throwable.getCause()), "Error -> " + throwable.getMessage(), throwable);
    }
}
