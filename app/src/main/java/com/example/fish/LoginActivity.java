package com.example.fish;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin, btnRegister;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
        setupListeners();
    }

    private void initViews() {
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnRegister = findViewById(R.id.btn_register);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
    }

    private void setupListeners() {
        btnLogin.setOnClickListener(v -> loginUser());
        btnRegister.setOnClickListener(v -> openRegistration());
        tvForgotPassword.setOnClickListener(v -> resetPassword());
    }

    private void loginUser() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (validateInput(email, password)) {
            AuthService.login(email, password, new AuthService.AuthCallback() {
                @Override
                public void onSuccess(User user) {
                    saveUserSession(user);
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                }

                @Override
                public void onError(String error) {
                    showError(error);
                }
            });
        }
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Введите корректный email");
            return false;
        }

        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError("Пароль должен содержать минимум 6 символов");
            return false;
        }

        return true;
    }

    private void saveUserSession(User user) {
        // TODO: сохранить пользователя в SharedPreferences/Local DB
    }

    private void showError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    private void openRegistration() {
        // TODO: открыть экран регистрации
        Toast.makeText(this, "Регистрация", Toast.LENGTH_SHORT).show();
    }

    private void resetPassword() {
        // TODO: восстановление пароля
        Toast.makeText(this, "Восстановление пароля", Toast.LENGTH_SHORT).show();
    }
}
