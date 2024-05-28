package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.content.SharedPreferences;

import huce.edu.vn.appdocsach.AppContext;
import huce.edu.vn.appdocsach.constants.AuthConstant;

public class TokenStorageManager {
    private final Context context = AppContext.getContext();
    private final SharedPreferences sharedPreferences = context.getSharedPreferences(AuthConstant.shareKey, Context.MODE_PRIVATE);

    // Lấy access token
    public String getAccessToken() {
        return sharedPreferences.getString(AuthConstant.tokenKey, "");
    }

    // Kiểm tra lần đầu đăng nhập
    public boolean getIsFirstTime() {
        return sharedPreferences.getBoolean(AuthConstant.isSignInFirstTimeKey, true);
    }

    // Lưu access token và trạng thái đăng nhập lần đầu
    public void save(String token, boolean isFirstLogin) {
        sharedPreferences.edit()
                .putString(AuthConstant.tokenKey, token)
                .putBoolean(AuthConstant.isSignInFirstTimeKey, isFirstLogin)
                .apply();
    }

    // Xóa tất cả các token
    public void clearAllTokens() {
        sharedPreferences.edit()
                .remove(AuthConstant.tokenKey)
                .remove(AuthConstant.isSignInFirstTimeKey)
                .apply();
    }

    // Lấy signup token
    public String getSignUpToken() {
        return sharedPreferences.getString(AuthConstant.signupTokenKey, "");
    }

    // Kiểm tra lần đầu đăng ký
    public boolean getIsFirstSignUp() {
        return sharedPreferences.getBoolean(AuthConstant.isSignUpFirstTimeKey, true);
    }

    // Lưu signup token và trạng thái đăng ký lần đầu
    
}
