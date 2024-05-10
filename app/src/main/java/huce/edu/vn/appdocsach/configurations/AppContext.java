package huce.edu.vn.appdocsach.configurations;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        AppContext.context = getApplicationContext();
    }

    public static Context getContext() {
        return AppContext.context;
    }
}
