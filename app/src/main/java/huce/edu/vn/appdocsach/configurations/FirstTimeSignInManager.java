package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.content.SharedPreferences;

import huce.edu.vn.appdocsach.AppContext;
import huce.edu.vn.appdocsach.constants.AuthConstant;

public class FirstTimeSignInManager {
    private static final SharedPreferences sharedPreferences = AppContext.getContext().getSharedPreferences(AuthConstant.shareKey, Context.MODE_PRIVATE);

    public static boolean getFirstTimeSignedIn() {
        return sharedPreferences.getBoolean(AuthConstant.isSignInFirstTimeKey, false);
    }

    public static void setSignedIn() {
        sharedPreferences.edit().putBoolean(AuthConstant.isSignInFirstTimeKey, true).apply();
    }
}
