package huce.edu.vn.appdocsach.utils;

import android.text.Editable;

public class StringUtils {
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNullOrEmpty(Editable editable) {
        return editable == null || editable.toString().isEmpty();
    }
}
