package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.content.SharedPreferences;

import huce.edu.vn.appdocsach.AppContext;
import huce.edu.vn.appdocsach.constants.AuthConstant;

public class TokenStorageManager {
    private final Context context = AppContext.getContext();
    private final SharedPreferences sharedPreferences = context.getSharedPreferences(AuthConstant.shareKey, Context.MODE_PRIVATE);

    public String getAccessToken() {
        return sharedPreferences.getString(AuthConstant.tokenKey, "");
    }

    public boolean getIsFirstTime() {
        return sharedPreferences.getBoolean(AuthConstant.isSignInFirstTimeKey, true);
    }

    public void save(String token, boolean isFirstLogin) {
        sharedPreferences.edit()
                .putString(AuthConstant.tokenKey, token)
                .putBoolean(AuthConstant.isSignInFirstTimeKey, isFirstLogin)
                .apply();
    }

    public void clearAllTokens() {

        sharedPreferences.edit()
                .remove(AuthConstant.tokenKey)
                .remove(AuthConstant.isSignInFirstTimeKey)
                .apply();
    }
}
