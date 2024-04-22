package huce.edu.vn.appdocsach.configurations;

import huce.edu.vn.appdocsach.utils.NetworkUtils;
import huce.edu.vn.appdocsach.utils.serializers.GsonCustom;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {
    public static <T> T getService(Class<T> clazz, String path) {
        return new Retrofit.Builder()
                .baseUrl("http://" + NetworkUtils.getLocalHostIp() + ":8080/" + path + "/")
                .addConverterFactory(GsonConverterFactory.create(GsonCustom.getInstance()))
                .build()
                .create(clazz);
    }
}
