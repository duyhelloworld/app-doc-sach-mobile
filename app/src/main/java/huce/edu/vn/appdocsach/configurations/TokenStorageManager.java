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

    public void save(String token) {
        sharedPreferences.edit().putString(AuthConstant.tokenKey, token).apply();
    }

    public void clearAllTokens() {
        sharedPreferences.edit().remove(AuthConstant.tokenKey).apply();
    }
}
