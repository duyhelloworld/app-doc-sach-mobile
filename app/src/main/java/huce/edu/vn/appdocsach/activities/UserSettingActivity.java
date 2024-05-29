package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
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
    ImageLoader imageLoader = ImageLoader.getInstance();
    AppLogger appLogger = AppLogger.getInstance();
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
        String avatar = intent.getStringExtra(IntentKey.USER_AVATAR), fullname = intent.getStringExtra(IntentKey.USER_FULLNAME);

        imageLoader.show(avatar, ivUserSettingAvatar);
        tvUserSettingUsername.setText(fullname);

        btnUserSettingSignOut.setOnClickListener(v -> authService.signOut().enqueue(new Callback<Void>() {
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
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                appLogger.error(throwable);
            }
        }));

        btnUserSettingChangePassword.setOnClickListener(v -> {
            Intent i = new Intent(UserSettingActivity.this, ChangePasswordActivity.class);
            startActivity(i);
        });

        btnUserSettingUpdateProfile.setOnClickListener(v -> {

        });
    }
}