package com.example.fish;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import androidx.annotation.NonNull;

public class AuthService {
    // Конфигурация Supabase
    private static final String SUPABASE_URL = "https://ekxlqjqounsstlpfnwqv.supabase.co";
    private static final String SUPABASE_ANON_KEY = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImVreGxxanFvdW5zc3RscGZud3F2Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3NjU0Njg2NTcsImV4cCI6MjA4MTA0NDY1N30.TMjyCLy_4YAqVKo_CqjfpMxdcW4dtIF8Eh0jlW6iuYQ";

    // Константы для API
    private static final String AUTH_PATH = "/auth/v1";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    // Настройки клиента OkHttp
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    // Callback интерфейс
    public interface AuthCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    // Основной метод для создания запросов к Supabase Auth
    private static Request.Builder createSupabaseRequest(String endpoint) {
        return new Request.Builder()
                .url(SUPABASE_URL + AUTH_PATH + endpoint)
                .addHeader("apikey", SUPABASE_ANON_KEY)
                .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
                .addHeader("Content-Type", "application/json");
    }

    // Вход пользователя
    public static void login(String email, String password, AuthCallback<User> callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError("Ошибка создания JSON: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = createSupabaseRequest("/token?grant_type=password")
                .post(body)
                .build();

        executeRequest(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                handleAuthResponse(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Регистрация нового пользователя
    public static void register(String email, String password, AuthCallback<User> callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            callback.onError("Ошибка создания JSON: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = createSupabaseRequest("/signup")
                .post(body)
                .build();

        executeRequest(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                handleAuthResponse(response, callback);
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Выход пользователя (логаут)
    public static void logout(String accessToken, AuthCallback<String> callback) {
        Request request = createSupabaseRequest("/logout")
                .post(RequestBody.create("", JSON))
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        executeRequest(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess("success");
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Нет данных";
                    callback.onError("Ошибка выхода: " + response.code() + " - " + errorBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Восстановление пароля
    public static void forgotPassword(String email, AuthCallback<String> callback) {
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (JSONException e) {
            callback.onError("Ошибка создания JSON: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        Request request = createSupabaseRequest("/recover")
                .post(body)
                .build();

        executeRequest(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess("Письмо для восстановления отправлено на " + email);
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Нет данных";
                    callback.onError("Ошибка: " + response.code() + " - " + errorBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Получение данных текущего пользователя
    public static void getUser(String accessToken, AuthCallback<User> callback) {
        Request request = createSupabaseRequest("/user")
                .get()
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        executeRequest(request, new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        User user = parseUserFromJson(jsonResponse);
                        callback.onSuccess(user);
                    } catch (Exception e) {
                        callback.onError("Ошибка обработки данных пользователя: " + e.getMessage());
                    }
                } else {
                    String errorBody = response.body() != null ? response.body().string() : "Нет данных";
                    callback.onError("Ошибка получения пользователя: " + response.code() + " - " + errorBody);
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Общий метод для выполнения запросов
    private static void executeRequest(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    // Обработка ответов аутентификации
    private static void handleAuthResponse(Response response, AuthCallback<User> callback) throws IOException {
        if (response.isSuccessful()) {
            try {
                String responseBody = response.body().string();
                JSONObject jsonResponse = new JSONObject(responseBody);
                User user = parseUserFromResponse(jsonResponse);
                callback.onSuccess(user);
            } catch (Exception e) {
                callback.onError("Ошибка обработки ответа: " + e.getMessage());
            }
        } else {
            String errorBody = response.body() != null ? response.body().string() : "Нет данных";
            callback.onError("Ошибка: " + response.code() + " - " + errorBody);
        }
    }

    // Парсинг пользователя из ответа аутентификации
    private static User parseUserFromResponse(JSONObject jsonResponse) throws JSONException {
        JSONObject userObject = jsonResponse.getJSONObject("user");
        String id = userObject.getString("id");
        String email = userObject.getString("email");
        String accessToken = jsonResponse.getString("access_token");
        String refreshToken = jsonResponse.optString("refresh_token", "");

        // Парсинг даты истечения токена
        long expiresAt = jsonResponse.optLong("expires_at", 0);

        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setToken(accessToken);
        user.setRefreshToken(refreshToken);
        user.setExpiresAt(expiresAt);

        // Дополнительные поля, если есть
        if (userObject.has("created_at")) {
            user.setCreatedAt(userObject.getString("created_at"));
        }

        return user;
    }

    // Парсинг пользователя из JSON (для метода getUser)
    private static User parseUserFromJson(JSONObject jsonObject) throws JSONException {
        String id = jsonObject.getString("id");
        String email = jsonObject.getString("email");

        User user = new User();
        user.setId(id);
        user.setEmail(email);

        if (jsonObject.has("created_at")) {
            user.setCreatedAt(jsonObject.getString("created_at"));
        }

        if (jsonObject.has("phone")) {
            user.setPhone(jsonObject.getString("phone"));
        }

        return user;
    }

    // Вспомогательный метод для проверки валидности email
    public static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Вспомогательный метод для проверки валидности пароля
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }
}