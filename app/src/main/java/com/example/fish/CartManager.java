package com.example.fish;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CartManager {
    private static final String PREF_NAME = "cart_prefs";
    private static final String KEY_CART_ITEMS = "cart_items";
    private static final Gson gson = new Gson();

    public static void addToCart(Context context, CartItem cartItem) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        List<CartItem> cartItems = getCartItems(context);

        // Проверяем, есть ли уже такой товар
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(cartItem.getProduct().getId())) {
                // Увеличиваем количество
                CartItem existingItem = cartItems.get(i);
                existingItem.setWeight(existingItem.getWeight() + cartItem.getWeight());
                saveCartItems(context, cartItems);
                return;
            }
        }

        // Добавляем новый товар
        cartItems.add(cartItem);
        saveCartItems(context, cartItems);
    }

    public static void updateCartItem(Context context, CartItem cartItem) {
        List<CartItem> cartItems = getCartItems(context);
        for (int i = 0; i < cartItems.size(); i++) {
            if (cartItems.get(i).getProduct().getId().equals(cartItem.getProduct().getId())) {
                cartItems.set(i, cartItem);
                saveCartItems(context, cartItems);
                return;
            }
        }
    }

    public static void removeFromCart(Context context, CartItem cartItem) {
        List<CartItem> cartItems = getCartItems(context);
        cartItems.removeIf(item -> item.getProduct().getId().equals(cartItem.getProduct().getId()));
        saveCartItems(context, cartItems);
    }

    public static List<CartItem> getCartItems(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_CART_ITEMS, null);
        if (json == null) {
            return new ArrayList<>();
        }
        Type type = new TypeToken<List<CartItem>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private static void saveCartItems(Context context, List<CartItem> cartItems) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(cartItems);
        editor.putString(KEY_CART_ITEMS, json);
        editor.apply();
    }
    public static void clearCart(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("cart_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();
    }
}
