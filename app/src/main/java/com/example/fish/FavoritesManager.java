package com.example.fish;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesManager {

    private static final String PREFS_NAME = "FavoritesPrefs";
    private static final String KEY_FAVORITES = "favorites";

    //  добавление в избранное
    public static void addToFavorites(Context context, Product product) {
        List<Product> favorites = getFavorites(context);
        boolean exists = false;
        for (Product p : favorites) {
            if (p.getId().equals(product.getId())) {
                exists = true;
                break;
            }
        }
        if (!exists) {
            favorites.add(product);
            saveFavorites(context, favorites);
        }
    }

    // удаление из избранного
    public static void removeFromFavorites(Context context, Product product) {
        List<Product> favorites = getFavorites(context);
        for (int i = 0; i < favorites.size(); i++) {
            if (favorites.get(i).getId().equals(product.getId())) {
                favorites.remove(i);
                break;
            }
        }
        saveFavorites(context, favorites);
    }

    //  проверка, что товар в избранном
    public static boolean isFavorite(Context context, Product product) {
        List<Product> favorites = getFavorites(context);
        for (Product p : favorites) {
            if (p.getId().equals(product.getId())) { // сравнение по id, при необходимости поменяй поле
                return true;
            }
        }
        return false;
    }

    // получить список избранного
    public static List<Product> getFavorites(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_FAVORITES, null);
        if (json == null) {
            return new ArrayList<>();
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<Product>>() {}.getType();
        List<Product> list = gson.fromJson(json, type);
        return list != null ? list : new ArrayList<>();
    }

    // сохранить список избранного
    private static void saveFavorites(Context context, List<Product> favorites) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(favorites);
        prefs.edit().putString(KEY_FAVORITES, json).apply();
    }
}
