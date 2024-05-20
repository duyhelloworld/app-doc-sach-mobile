package huce.edu.vn.appdocsach.configurations;


import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class JwtInterceptor implements Interceptor {
    private final TokenStorageManager storageManager = new TokenStorageManager();

    @NonNull
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + storageManager.getAccessToken())
                .build();
        return chain.proceed(request);
    }
}
