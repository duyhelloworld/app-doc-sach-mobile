package huce.edu.vn.appdocsach.utils;

import android.text.Editable;
import android.text.TextUtils;

public class StringUtils {

    public static boolean isNullOrEmpty(Editable editable) {
        return editable == null || editable.toString().isEmpty();
    }

    public static boolean isNullOrEmpty(String string) {
        return TextUtils.isEmpty(string);
    }
}
