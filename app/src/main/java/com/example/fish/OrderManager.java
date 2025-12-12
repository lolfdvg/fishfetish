package com.example.fish;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OrderManager {
    private static final String PREF_NAME = "order_prefs";
    private static final String KEY_ORDERS = "orders";
    private static final Gson gson = new Gson();

    public static void addOrder(Context context, List<CartItem> items, double totalPrice, String userId) {
        Order order = new Order(items, totalPrice);
        order.setUserId(userId); // Устанавливаем userId отдельно
        List<Order> orders = getOrders(context);
        orders.add(order);
        saveOrders(context, orders);
    }

    public static List<Order> getOrders(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String json = prefs.getString(KEY_ORDERS, null);
        if (json == null) return new ArrayList<>();
        Type type = new TypeToken<List<Order>>(){}.getType();
        return gson.fromJson(json, type);
    }

    private static void saveOrders(Context context, List<Order> orders) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(orders);
        editor.putString(KEY_ORDERS, json);
        editor.apply();
    }
}
