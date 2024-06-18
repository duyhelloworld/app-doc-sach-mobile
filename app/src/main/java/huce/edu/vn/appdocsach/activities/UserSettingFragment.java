package huce.edu.vn.appdocsach.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

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

public class UserSettingFragment extends Fragment {
    CardView cvUserSettingChangePassword, cvUserSettingUpdateProfile, cvUserSettingSignOut;
    NavigationBarView bottomNavigationView;
    ImageView ivUserSettingAvatar;
    TextView tvUserSettingUsername, txtChangeProfile, txtChangePassword, txtLogout;
    AuthService authService = AuthService.authService;
    ImageLoader imageLoader = ImageLoader.getInstance();
    AppLogger appLogger = AppLogger.getInstance();
    TokenStorageManager tokenStorageManager = new TokenStorageManager();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_user_setting, container, false);

        cvUserSettingChangePassword = view.findViewById(R.id.cvUserSettingChangePassword);
        cvUserSettingUpdateProfile = view.findViewById(R.id.cvUserSettingUpdateProfile);
        cvUserSettingSignOut = view.findViewById(R.id.cvUserSettingSignOut);
        ivUserSettingAvatar = view.findViewById(R.id.ivUserSettingAvatar);
        tvUserSettingUsername = view.findViewById(R.id.tvUserSettingUsername);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.navigation_user);

        Bundle bundle = getArguments();
        assert bundle != null;
        String avatar = bundle.getString(IntentKey.USER_AVATAR), fullname = bundle.getString(IntentKey.USER_FULLNAME);

        imageLoader.show(avatar, ivUserSettingAvatar);
        tvUserSettingUsername.setText(fullname);
        authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
            @Override
            public void onResponse(Call<AuthInfoModel> call, @NonNull Response<AuthInfoModel> response) {
                AuthInfoModel model = response.body();
                if (!response.isSuccessful()) {
                    Intent loginIntent = new Intent(getContext(), LoginActivity.class);
                    startActivity(loginIntent);
                    return;
                }
                assert model != null;
                Intent intent = new Intent(getContext(), UserSettingFragment.class);
                intent.putExtra(IntentKey.USER_AVATAR, model.getAvatar());
                intent.putExtra(IntentKey.USER_FULLNAME, model.getFullname());
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<AuthInfoModel> call, Throwable throwable) {
                DialogUtils.errorUserSee(getContext(), R.string.error_login);
                appLogger.error(throwable);
            }
        });

        cvUserSettingSignOut.setOnClickListener(v -> authService.signOut().enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (!response.isSuccessful()) {
                    DialogUtils.errorUserSee(getContext(), R.string.error_logout);
                    return;
                }
                tokenStorageManager.clearAllTokens();
                Toast.makeText(getContext(), R.string.logout_success, Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getContext(), MainActivity.class);
                startActivity(i);
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable throwable) {
                appLogger.error(throwable);
            }
        }));

        cvUserSettingChangePassword.setOnClickListener(v -> {
            Intent i = new Intent(getContext(), ChangePasswordActivity.class);
            startActivity(i);
        });

        cvUserSettingUpdateProfile.setOnClickListener(v -> {
            Intent UpdateProfIntent = new Intent(getContext(), ChangeProfile.class);
            startActivity(UpdateProfIntent);
        });
        return view;
    }
}
