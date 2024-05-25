package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSettingActivity extends AppCompatActivity {
    Button btnUserSettingChangePassword, btnUserSettingUpdateProfile, btnUserSettingSignOut;
    ImageView ivUserSettingAvatar;
    TextView tvUserSettingUsername;
    AuthService authService = AuthService.authService;
    ImageLoader imageLoader;
    AppLogger appLogger = AppLogger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        btnUserSettingChangePassword = findViewById(R.id.btnUserSettingChangePassword);
        btnUserSettingUpdateProfile = findViewById(R.id.btnUserSettingUpdateProfile);
        btnUserSettingSignOut = findViewById(R.id.btnUserSettingSignOut);
        ivUserSettingAvatar = findViewById(R.id.ivUserSettingAvatar);
        tvUserSettingUsername = findViewById(R.id.tvUserSettingUsername);
        imageLoader = new ImageLoader(UserSettingActivity.this);

        authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
            @Override
            public void onResponse(@NonNull Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                AuthInfoModel model = response.body();
                if (!response.isSuccessful() || model == null) {
                    return;
                }
                imageLoader.renderWithCache(model.getAvatar(), ivUserSettingAvatar);
            }

            @Override
            public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
                DialogUtils.errorUserSee(UserSettingActivity.this, R.string.error_load_user_info);
                appLogger.error(throwable);
            }
        });

    }
}