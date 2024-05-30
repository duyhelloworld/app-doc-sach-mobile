package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.navigation.NavigationBarView;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.IntentKey;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserSettingActivity extends AppCompatActivity {
    CardView cvUserSettingChangePassword, cvUserSettingUpdateProfile, cvUserSettingSignOut;
    NavigationBarView bottomNavigationView;
    ImageView ivUserSettingAvatar;
    TextView tvUserSettingUsername, txtChangeProfile, txtChangePassword, txtLogout;
    AuthService authService = AuthService.authService;
    ImageLoader imageLoader = ImageLoader.getInstance();
    AppLogger appLogger = AppLogger.getInstance();
    TokenStorageManager tokenStorageManager = new TokenStorageManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        cvUserSettingChangePassword = findViewById(R.id.cvUserSettingChangePassword);
        cvUserSettingUpdateProfile = findViewById(R.id.cvUserSettingUpdateProfile);
        cvUserSettingSignOut = findViewById(R.id.cvUserSettingSignOut);
        ivUserSettingAvatar = findViewById(R.id.ivUserSettingAvatar);
        tvUserSettingUsername = findViewById(R.id.tvUserSettingUsername);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_user);


        Intent intent = getIntent();
        String avatar = intent.getStringExtra(IntentKey.USER_AVATAR), fullname = intent.getStringExtra(IntentKey.USER_FULLNAME);

        imageLoader.show(avatar, ivUserSettingAvatar);
        tvUserSettingUsername.setText(fullname);
        bottomNavigationView.setOnItemSelectedListener(menuItem -> {
            if(menuItem.getItemId() == R.id.navigation_home){
                Intent HomeIntent = new Intent(UserSettingActivity.this, MainActivity.class);
                startActivity(HomeIntent);
            }
            if(menuItem.getItemId() == R.id.navigation_categories){
                Intent CateIntent = new Intent(UserSettingActivity.this, CategoryTab.class);
                startActivity(CateIntent);
            }

            if(menuItem.getItemId() == R.id.navigation_user){
                authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
                    @Override
                    public void onResponse(Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                        AuthInfoModel model = response.body();
                        if(!response.isSuccessful()){
                            Intent loginIntent = new Intent(UserSettingActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            return;
                        }
                        Intent intent = new Intent(UserSettingActivity.this, UserSettingActivity.class);
                        intent.putExtra(IntentKey.USER_AVATAR, model.getAvatar());
                        intent.putExtra(IntentKey.USER_FULLNAME, model.getFullname());
                        startActivity(intent);
                    }
                    @Override
                    public void onFailure(Call<AuthInfoModel> call, Throwable throwable) {
                        DialogUtils.errorUserSee(UserSettingActivity.this, R.string.error_login);
                        appLogger.error(throwable);
                    }
                });
                return false;
            }
            return false;
        });

        cvUserSettingSignOut.setOnClickListener(v -> authService.signOut().enqueue(new Callback<Void>() {
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

        cvUserSettingChangePassword.setOnClickListener(v -> {
            Intent i = new Intent(UserSettingActivity.this, ChangePasswordActivity.class);
            startActivity(i);
        });

        cvUserSettingUpdateProfile.setOnClickListener(v -> {
            Intent UpdateProfIntent = new Intent(UserSettingActivity.this, ChangeProfile.class);
            startActivity(UpdateProfIntent);
        });
    }
}
