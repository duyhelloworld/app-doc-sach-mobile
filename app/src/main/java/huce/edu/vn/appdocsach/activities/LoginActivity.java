package huce.edu.vn.appdocsach.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;

import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import app.rive.runtime.kotlin.RiveAnimationView;
import app.rive.runtime.kotlin.core.ExperimentalAssetLoader;
import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.RiveConstants;
import huce.edu.vn.appdocsach.models.auth.AuthResponseModel;
import huce.edu.vn.appdocsach.models.auth.SignInRequestModel;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.utils.StringUtils;
import huce.edu.vn.appdocsach.utils.serializers.HttpErrorSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText edtLoginUsername, edtLoginPassword;
    Button btnLoginSignIn;
    RiveAnimationView riveLoginIcon;
    TextView tvRegister, tvSkip;

    TokenStorageManager tokenStorage = new TokenStorageManager();
    AuthService authService = AuthService.authService;
    AppLogger appLogger = AppLogger.getInstance();

    @ExperimentalAssetLoader
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        riveLoginIcon = findViewById(R.id.riveLoginIcon);

        edtLoginUsername = findViewById(R.id.edtLoginUsername);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLoginSignIn = findViewById(R.id.btnLoginSignIn);

        TextInputLayout tilLoginUsername = findViewById(R.id.tilLoginUsername);
        TextInputLayout tilLoginPassword = findViewById(R.id.tilLoginPassword);

        edtLoginUsername.setOnFocusChangeListener((v, hasFocus) -> riveLoginIcon.setBooleanState(RiveConstants.RIVE_STATE_MACHINE, RiveConstants.IS_FOCUS_STATE, hasFocus));

        edtLoginPassword.setOnFocusChangeListener((v, hasFocus) -> riveLoginIcon.setBooleanState(RiveConstants.RIVE_STATE_MACHINE, RiveConstants.IS_PASSWORD_STATE, hasFocus));

        tvRegister = findViewById(R.id.tvRegister);
        tvSkip = findViewById(R.id.tvSkip);

        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        btnLoginSignIn.setOnClickListener(l -> {
            Editable user = edtLoginUsername.getText(), pass = edtLoginPassword.getText();
            if (StringUtils.isNullOrEmpty(user)) {
                tilLoginUsername.setError(getString(R.string.missing_username_message_error));
                return;
            } else {
                tilLoginUsername.setError(null);
            }

            if (StringUtils.isNullOrEmpty(pass)) {
                tilLoginPassword.setError(getString(R.string.missing_password_message_error));
                return;
            } else {
                tilLoginPassword.setError(null);
            }

            SignInRequestModel signInRequestModel = new SignInRequestModel(user.toString().trim(), pass.toString());
            authService.signIn(signInRequestModel).enqueue(new Callback<AuthResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<AuthResponseModel> call, @NonNull Response<AuthResponseModel> response) {
                    AuthResponseModel authResponse = response.body();
                    if (!response.isSuccessful() || authResponse == null) {
                        DialogUtils.errorUserSee(LoginActivity.this, HttpErrorSerializer.extractErrorMessage(response.errorBody()));
                        riveLoginIcon.fireState(RiveConstants.RIVE_STATE_MACHINE, RiveConstants.LOGIN_FAIL_STATE);
                        return;
                    }
                    tokenStorage.clearAllTokens();
                    tokenStorage.save(authResponse.getJwt(), true);
                    riveLoginIcon.fireState(RiveConstants.RIVE_STATE_MACHINE, RiveConstants.LOGIN_SUCCESS_STATE);

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }, 1000);
                }

                @Override
                public void onFailure(@NonNull Call<AuthResponseModel> call, @NonNull Throwable throwable) {
                    DialogUtils.errorUserSee(LoginActivity.this, R.string.error_login);
                    appLogger.error(throwable);
                }
            });
        });

        tvSkip.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        });
    }
}
