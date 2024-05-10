package huce.edu.vn.appdocsach.configurations;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

import java.io.IOException;

import huce.edu.vn.appdocsach.R;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenStorageManager implements Interceptor, TokenStorage {
    private final SharedPreferences localStore;
    private final SharedPreferences.Editor editor;
    private final String accessKey = "token";

    public TokenStorageManager() {
        Context c = AppContext.getContext();
        localStore = c.getSharedPreferences(c.getString(R.string.pref_app_name), Context.MODE_PRIVATE);
        editor = localStore.edit();
    }

    @Override
    public String getAccessToken() {
        return localStore.getString(accessKey, "");
    }

    @Override
    public boolean save(String token) {
        editor.putString(accessKey, token);
        return editor.commit();
    }

    @Override
    public boolean clearAllTokens() {
        return editor.remove(accessKey).commit();
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = getAccessToken();
        Request request = chain.request().newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();
        return chain.proceed(request);
    }
}
