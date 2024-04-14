package huce.edu.vn.appdocsach.utils;

import android.util.JsonReader;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;

import java.util.Objects;

import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;

public class LoggerUtil {
    private static LoggerUtil util;
    private final Gson gson = GsonCustom.getInstance();

    private LoggerUtil() {}
    public static LoggerUtil getInstance() {
        if (util == null) {
            util = new LoggerUtil();
        }
        return  util;
    }

    public void info(Object... objects) {

        Log.i("InfoObject", gson.toJson(objects));
    }

    public void error(@NonNull Exception e) {

        Log.e(gson.toJson(e.getCause()), Objects.requireNonNull(e.getMessage()), e);
    }
}
