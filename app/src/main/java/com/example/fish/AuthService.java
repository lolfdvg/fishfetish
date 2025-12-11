package com.example.fish;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import androidx.annotation.NonNull;  // Добавьте этот импорт

public class AuthService {
    private static final String SUPABASE_URL = "https://your-project.supabase.co";
    private static final String SUPABASE_KEY = "your-anon-key";

    public interface AuthCallback {
        void onSuccess(User user);
        void onError(String error);
    }

    public static void login(String email, String password, AuthCallback callback) {
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

    private static User parseUser(JSONObject jsonResponse) {
        User user = new User();
        try {
            JSONObject userData = jsonResponse.getJSONObject("user");
            user.setId(userData.getString("id"));
            user.setEmail(userData.getString("email"));
            // Можно добавить другие поля из ответа Supabase
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }
}