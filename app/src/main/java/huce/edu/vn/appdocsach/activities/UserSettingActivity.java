package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.utils.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSettingActivity extends AppCompatActivity {
    Button btnUserSettingChangePassword, btnUserSettingUpdateProfile, btnUserSettingSignOut;
    ImageView ivUserSettingAvatar;
    TextView tvUserSettingUsername;
    AuthService authService = AuthService.authService;
    ImageLoader imageLoader = ImageLoader.getInstance();
    AppLogger appLogger = AppLogger.getInstance();
    String avatar, fullname;
    TokenStorageManager tokenStorageManager = new TokenStorageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        btnUserSettingChangePassword = findViewById(R.id.btnUserSettingChangePassword);
        btnUserSettingUpdateProfile = findViewById(R.id.btnUserSettingUpdateProfile);
        btnUserSettingSignOut = findViewById(R.id.btnUserSettingSignOut);
        ivUserSettingAvatar = findViewById(R.id.ivUserSettingAvatar);
        tvUserSettingUsername = findViewById(R.id.tvUserSettingUsername);

        Intent intent = getIntent();
        avatar = intent.getStringExtra(IntentKey.USER_AVATAR);
        fullname = intent.getStringExtra(IntentKey.USER_FULLNAME);
        if (StringUtils.isNullOrEmpty(avatar) || StringUtils.isNullOrEmpty(fullname)) {
            authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
                @Override
                public void onResponse(@NonNull Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                    AuthInfoModel model = response.body();
                    if (!response.isSuccessful() || model == null) {
                        ivUserSettingAvatar.setImageResource(R.drawable.default_avatar);
                        tvUserSettingUsername.setText(R.string.default_fullname);
                        return;
                    }
                    avatar = model.getAvatar();
                    fullname = model.getFullname();
                }

                @Override
                public void onFailure(@NonNull Call<AuthInfoModel> call, @NonNull Throwable throwable) {
                    DialogUtils.errorUserSee(UserSettingActivity.this, R.string.error_load_user_info);
                    appLogger.error(throwable);
                }
            });
        }
        imageLoader.renderWithCache(avatar, ivUserSettingAvatar);
        tvUserSettingUsername.setText(fullname);

        btnUserSettingSignOut.setOnClickListener(v -> {
            authService.signOut().enqueue(new Callback<Void>() {
                @Override
                public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                    if (!response.isSuccessful()) {
                        DialogUtils.errorUserSee(UserSettingActivity.this, R.string.error_logout);
                        return;
                    }
                    tokenStorageManager.clearAllTokens();
                    Toast.makeText(UserSettingActivity.this, R.string.logout_success, Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(UserSettingActivity.this, MainActivity.class);
                    startActivity(i);
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {
                }
            });
        });

        btnUserSettingChangePassword.setOnClickListener(v -> {
            DialogUtils.infoUserSee(this, R.string.feature_not_supported);
        });

        btnUserSettingUpdateProfile.setOnClickListener(v -> {
            DialogUtils.infoUserSee(this, R.string.feature_not_supported);
        });
    }
}