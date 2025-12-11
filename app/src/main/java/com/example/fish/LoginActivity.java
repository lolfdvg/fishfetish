package com.example.fish;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> login());
        tvForgotPassword.setOnClickListener(v -> forgotPassword());
        findViewById(R.id.btnRegister).setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }

    private void login() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthService.login(email, password, new AuthService.AuthCallback<User>() {
            @Override
            public void onSuccess(User user) {
                SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
                prefs.edit()
                        .putString("userToken", user.getToken())
                        .putString("userId", user.getId())
                        .apply();
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Ошибка: " + error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }

    private void forgotPassword() {
        String email = etEmail.getText().toString().trim();

        if (email.isEmpty()) {
            Toast.makeText(this, "Введите email", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthService.forgotPassword(email, new AuthService.AuthCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Ссылка для сброса пароля отправлена", Toast.LENGTH_SHORT).show()
                );
            }

            @Override
            public void onError(String error) {
                runOnUiThread(() ->
                        Toast.makeText(LoginActivity.this, "Ошибка: " + error, Toast.LENGTH_SHORT).show()
                );
            }
        });
    }
}
