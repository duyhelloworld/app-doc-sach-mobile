package huce.edu.vn.appdocsach.configurations;

import java.util.concurrent.TimeUnit;

import huce.edu.vn.appdocsach.utils.NetworkUtils;
import huce.edu.vn.appdocsach.utils.serializers.JsonSerializer;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    private static OkHttpClient getClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(1000, TimeUnit.SECONDS)
                .addInterceptor(new JwtInterceptor())
                .build();
    }

    public static <T> T getService(Class<T> clazz, String path) {
        return new Retrofit.Builder()
                .baseUrl("http://" + NetworkUtils.getLocalHostIp() + ":8080/" + path + "/")
                .addConverterFactory(GsonConverterFactory.create(JsonSerializer.getInstance()))
                .client(getClient())
                .build()
                .create(clazz);
    }
}
