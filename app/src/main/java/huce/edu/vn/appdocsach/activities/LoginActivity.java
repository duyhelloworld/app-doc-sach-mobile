package huce.edu.vn.appdocsach.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import huce.edu.vn.appdocsach.R;
import huce.edu.vn.appdocsach.apiservices.AuthService;
import huce.edu.vn.appdocsach.configurations.TokenStorage;
import huce.edu.vn.appdocsach.configurations.TokenStorageManager;

public class LoginActivity extends AppCompatActivity {
    TokenStorage tokenStorage = new TokenStorageManager();
    AuthService authService = AuthService.authService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}