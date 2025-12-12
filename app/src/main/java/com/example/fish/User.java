package com.example.fish;

public class User {
    private String id;
    private String email;
    private String token;
    private String refreshToken;
    private long expiresAt;
    private String createdAt;
    private String phone;

    // Геттеры и сеттеры
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }

    public long getExpiresAt() { return expiresAt; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    // Проверка валидности токена
    public boolean isTokenValid() {
        if (token == null || token.isEmpty()) {
            return false;
        }
        if (expiresAt > 0) {
            long currentTime = System.currentTimeMillis() / 1000;
            return expiresAt > currentTime;
        }
        return true;
    }
}