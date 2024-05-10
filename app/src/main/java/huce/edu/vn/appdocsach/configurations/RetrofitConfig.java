package huce.edu.vn.appdocsach.configurations;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import huce.edu.vn.appdocsach.utils.NetworkUtils;
import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(2, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(new TokenStorageManager())
                .build();
    }

    public static <T> T getService(Class<T> clazz, String path) {
        return new Retrofit.Builder()
                .baseUrl("http://" + NetworkUtils.getLocalHostIp() + ":8080/" + path + "/")
                .addConverterFactory(GsonConverterFactory.create(GsonCustom.getInstance()))
                .client(getClient())
                .build()
                .create(clazz);
    }
}
