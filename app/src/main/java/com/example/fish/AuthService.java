package com.example.fish;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import androidx.annotation.NonNull;

public class AuthService {

    private static final String SUPABASE_URL = "https://ekxlqjqounsstlpfnwqv.supabase.co";
    private static final String SUPABASE_KEY = "sbpublishableM2zt7Ka2mhdFceld0GeevwDt3xr3br";

    public interface AuthCallback<T> {
        void onSuccess(T result);
        void onError(String error);
    }

    // Метод для входа
    public static void login(String email, String password, AuthCallback<User> callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
            callback.onError("Ошибка создания запроса");
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/token?grant_type=password")
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        User user = parseUser(jsonResponse);
                        callback.onSuccess(user);
                    } catch (Exception e) {
                        callback.onError("Ошибка парсинга ответа");
                    }
                } else {
                    callback.onError("Ошибка авторизации: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Метод для регистрации
    public static void register(String email, String password, AuthCallback<User> callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("password", password);
            // Если нужно хранить доп. данные пользователя – Supabase требует объект options.
            JSONObject options = new JSONObject();
            JSONObject data = new JSONObject();
            data.put("role", "authenticated");
            options.put("data", data);
            json.put("options", options);
        } catch (JSONException e) {
            callback.onError(e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/signup")
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        String responseBody = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseBody);
                        User user = parseUser(jsonResponse);
                        callback.onSuccess(user);
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                } else {
                    callback.onError("Ошибка регистрации: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError(e.getMessage());
            }
        });
    }

    // Метод для восстановления пароля
    public static void forgotPassword(String email, AuthCallback<String> callback) {
        OkHttpClient client = new OkHttpClient();
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
        } catch (JSONException e) {
            callback.onError("Ошибка создания запроса: " + e.getMessage());
            return;
        }

        RequestBody body = RequestBody.create(
                json.toString(),
                MediaType.parse("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
                .url(SUPABASE_URL + "/auth/v1/recover")
                .post(body)
                .addHeader("apikey", SUPABASE_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) {
                if (response.isSuccessful()) {
                    callback.onSuccess("success");
                } else {
                    callback.onError("Ошибка восстановления пароля: " + response.code());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                callback.onError("Ошибка сети: " + e.getMessage());
            }
        });
    }

    // Метод для парсинга пользователя из JSON
    private static User parseUser(JSONObject jsonResponse) throws JSONException {
        JSONObject userObject = jsonResponse.getJSONObject("user");
        String id = userObject.getString("id");
        String email = userObject.getString("email");
        String accessToken = jsonResponse.getString("access_token");
        User user = new User();
        user.setId(id);
        user.setEmail(email);
        user.setToken(accessToken);
        return user;
    }
}
