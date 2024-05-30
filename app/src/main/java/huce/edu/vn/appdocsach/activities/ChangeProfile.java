package huce.edu.vn.appdocsach.activities;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.io.InputStream;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.ImageLoader;
import huce.edu.vn.appdocsach.models.auth.AuthInfoModel;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeProfile extends AppCompatActivity {
    private ImageView imageViewAvatar;
    private TextInputEditText txtInputFullName;
    private TextInputEditText txtInputEmail;
    private Button btnChangeAva, btnChangeProfile;
    private AuthService authService = AuthService.authService;
    private ActivityResultLauncher<String> mGetContent;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_profile);

        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        txtInputFullName = findViewById(R.id.txtInputEditTextFullName);
        txtInputEmail = findViewById(R.id.txtInputEditTextEmail);
        btnChangeAva = findViewById(R.id.btnChangeAva);
        btnChangeProfile = findViewById(R.id.btnChangeProfile);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        imageViewAvatar.setImageURI(result);
                        selectedImageUri = result;
                    } else {
                        Toast.makeText(ChangeProfile.this, "Không lấy được ảnh", Toast.LENGTH_SHORT).show();
                    }
                });

        loadUserInfo();

        btnChangeAva.setOnClickListener(v -> {
            pickImageFromGallery();
        });

        btnChangeProfile.setOnClickListener(v -> {
            String newFullName = txtInputFullName.getText().toString();
            String newEmail = txtInputEmail.getText().toString();

            if (newFullName.isEmpty() || newEmail.isEmpty()) {
                Toast.makeText(ChangeProfile.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            AuthInfoModel updatedInfo = new AuthInfoModel();
            updatedInfo.setFullname(newFullName);
            updatedInfo.setEmail(newEmail);

            updateProfile(updatedInfo);
        });
    }

    private void loadUserInfo() {
        authService.getInfo().enqueue(new Callback<AuthInfoModel>() {
            @Override
            public void onResponse(Call<AuthInfoModel> call, Response<AuthInfoModel> response) {
                if (response.isSuccessful()) {
                    AuthInfoModel userInfo = response.body();
                    if (userInfo != null) {
                        renderUserInfo(userInfo);
                    }
                } else {
                    Toast.makeText(ChangeProfile.this, "Không thể lấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AuthInfoModel> call, Throwable t) {
                Toast.makeText(ChangeProfile.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderUserInfo(AuthInfoModel userInfo) {
        String avatarUrl = userInfo.getAvatar();
        ImageLoader.getInstance().show(avatarUrl, imageViewAvatar);

        txtInputFullName.setText(userInfo.getFullname());
        txtInputEmail.setText(userInfo.getEmail());
    }

    private void pickImageFromGallery() {
        mGetContent.launch("image/*");
    }

    private void updateProfile(AuthInfoModel updatedInfo) {
        if (selectedImageUri != null) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                byte[] imageData = readBytes(inputStream);
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageData);
                MultipartBody.Part avatarPart = MultipartBody.Part.createFormData("avatar", "avatar.jpg", requestBody);

                authService.changeProfile(updatedInfo, avatarPart).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ChangeProfile.this, "Hồ sơ đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ChangeProfile.this, "Không thể cập nhật hồ sơ. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ChangeProfile.this, "Lỗi kết nối. Vui lòng kiểm tra kết nối của bạn", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(ChangeProfile.this, "Không thể đọc ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            authService.changeProfile(updatedInfo, null).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangeProfile.this, "Hồ sơ đã được cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(ChangeProfile.this, "Không thể cập nhật hồ sơ. Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(ChangeProfile.this, "Lỗi kết nối. Vui lòng kiểm tra kết nối của bạn", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
