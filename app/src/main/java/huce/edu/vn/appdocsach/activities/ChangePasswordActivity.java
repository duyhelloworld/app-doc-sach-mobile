package huce.edu.vn.appdocsach.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.models.auth.ChangePassRequestModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextInputEditText edtCurrentPassword, edtNewPassword;
    private TextInputLayout layoutCurrentPassword, layoutNewPassword;
    private Button btnChangePass;

    private AuthService authService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        authService = AuthService.authService;



        edtCurrentPassword = findViewById(R.id.edtCurrentPassword);
        edtNewPassword = findViewById(R.id.edtNewPassword);
        layoutCurrentPassword = findViewById(R.id.layoutCurrentPassword);
        layoutNewPassword = findViewById(R.id.layoutNewPassword);
        btnChangePass = findViewById(R.id.btnChangePass);

        btnChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleChangePassword();
            }
        });
    }

    private void handleChangePassword() {
        String oldPassword = edtCurrentPassword.getText().toString();
        String newPassword = edtNewPassword.getText().toString();

        if (TextUtils.isEmpty(oldPassword)) {
            layoutCurrentPassword.setError(getString(R.string.missing_current_password));
            return;
        } else {
            layoutCurrentPassword.setError(null);
        }

        if (TextUtils.isEmpty(newPassword)) {
            layoutNewPassword.setError(getString(R.string.missing_new_password));
            return;
        } else {
            layoutNewPassword.setError(null);
        }

        ChangePassRequestModel changePassRequestModel = new ChangePassRequestModel(oldPassword.toString(),newPassword.toString());

        Call<Void> call = authService.changePass(changePassRequestModel);
        authService.changePass(changePassRequestModel).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                   Toast.makeText(ChangePasswordActivity.this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Failed to change password", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                    Toast.makeText(ChangePasswordActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                }

        });

    }
}
