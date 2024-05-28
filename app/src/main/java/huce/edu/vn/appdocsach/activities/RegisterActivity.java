package huce.edu.vn.appdocsach.activities;


import android.content.Intent;

import android.net.Uri;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import android.os.Handler;

import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import app.rive.runtime.kotlin.RiveAnimationView;
import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;
import huce.edu.vn.appdocsach.constants.RiveConstants;
import huce.edu.vn.appdocsach.models.auth.AuthResponseModel;
import huce.edu.vn.appdocsach.models.auth.SignUpRequestModel;
import huce.edu.vn.appdocsach.utils.AppLogger;
import huce.edu.vn.appdocsach.utils.DialogUtils;
import huce.edu.vn.appdocsach.utils.StringUtils;
import huce.edu.vn.appdocsach.utils.serializers.HttpErrorSerializer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

public class RegisterActivity extends AppCompatActivity {
    EditText edtRegisterFullName, edtRegisterUsername, edtRegisterEmail, edtRegisterPassword;
    Button btnRegister, btnSelectAvatar;
    ImageView imgAvatarPreview;
    RiveAnimationView riveRegisterIcon;
    TextView txtLogin;
    private ActivityResultLauncher<String> mGetContent;
    private Uri selectedImageUri;
    private ActivityResultLauncher<String> launcher;
    TokenStorageManager tokenStorage = new TokenStorageManager();
    AuthService authService = AuthService.authService;
    AppLogger appLogger = AppLogger.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        riveRegisterIcon = findViewById(R.id.riveRegisterIcon);

        edtRegisterFullName = findViewById(R.id.edtFullName);
        edtRegisterUsername = findViewById(R.id.edtUsername);
        edtRegisterEmail = findViewById(R.id.edtEmail);
        edtRegisterPassword = findViewById(R.id.edtPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btnSelectAvatar = findViewById(R.id.btnSelectAvatar);
        imgAvatarPreview = findViewById(R.id.imgAvatarPreview);
        txtLogin = findViewById(R.id.txtLogin);
        txtLogin.setOnClickListener(v -> {
            Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        });

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imgAvatarPreview.setImageURI(result);
                        selectedImageUri = result;
                    } else {
                        Toast.makeText(RegisterActivity.this, "Không lấy được ảnh", Toast.LENGTH_SHORT).show();
                    }
                });

        btnSelectAvatar.setOnClickListener(v -> {
           pickImageFromGallery();
        });

        btnRegister.setOnClickListener(l -> {
            Editable fullname = edtRegisterFullName.getText(), user = edtRegisterUsername.getText(), email = edtRegisterEmail.getText(), pass = edtRegisterPassword.getText();
            ImageView imgAvt = imgAvatarPreview;

            if (imgAvt.getDrawable() == null) {
                Toast.makeText(RegisterActivity.this, "Vui lòng chọn ảnh đại diện", Toast.LENGTH_SHORT).show();
                return;
            }
            if (StringUtils.isNullOrEmpty(user)) {
                edtRegisterUsername.setError("Vui lòng nhập tên người dùng");
                return;
            }
            if (StringUtils.isNullOrEmpty(pass)) {
                edtRegisterPassword.setError("Vui lòng nhập mật khẩu");
                return;
            }
            if (StringUtils.isNullOrEmpty(fullname)) {
                edtRegisterFullName.setError("Vui lòng nhập họ và tên");
                return;
            }
            if (StringUtils.isNullOrEmpty(email)) {
                edtRegisterEmail.setError("Vui lòng nhập địa chỉ email");
                return;
            }

            MultipartBody.Part avatarPart = null;
            if (selectedImageUri != null) {
                try {
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    byte[] imageData = readBytes(inputStream);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageData);
                    avatarPart = MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestBody);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(RegisterActivity.this, "Không thể đọc ảnh", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            SignUpRequestModel signUpRequestModel = new SignUpRequestModel(fullname.toString(), user.toString().trim(), pass.toString(), email.toString());
            Gson gson = new Gson();
            String signUpRequestBody = gson.toJson(signUpRequestModel);
            RequestBody jsonRequestBody = RequestBody.create(MediaType.parse("application/json"), signUpRequestBody);

            authService.signUpWithAvatar(jsonRequestBody, avatarPart).enqueue(new Callback<AuthResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<AuthResponseModel> call, @NonNull Response<AuthResponseModel> response) {
                    AuthResponseModel authResponse = response.body();
                    if (!response.isSuccessful() || authResponse == null) {
                        DialogUtils.errorUserSee(RegisterActivity.this, HttpErrorSerializer.extractErrorMessage(response.errorBody()));
                        riveRegisterIcon.fireState(RiveConstants.RIVE_STATE_MACHINE, RiveConstants.LOGIN_FAIL_STATE);
                        return;
                    }
                    tokenStorage.clearAllTokens();
                    tokenStorage.save(authResponse.getJwt(), true);
                    riveRegisterIcon.fireState(RiveConstants.RIVE_STATE_MACHINE, RiveConstants.LOGIN_FAIL_STATE);

                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                    }, 1000);
                }

                @Override
                public void onFailure(@NonNull Call<AuthResponseModel> call, @NonNull Throwable throwable) {
                    DialogUtils.errorUserSee(RegisterActivity.this, R.string.error_register);
                    appLogger.error(throwable);
                }
            });
        });
    }

    private void pickImageFromGallery() {
        mGetContent.launch("image/*");
    }

    private byte[] readBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }
}
