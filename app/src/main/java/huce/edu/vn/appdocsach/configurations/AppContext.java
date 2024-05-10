package huce.edu.vn.appdocsach.configurations;

import android.app.Application;
import android.content.Context;

public class AppContext extends Application {
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
