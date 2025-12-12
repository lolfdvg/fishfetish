package com.example.fish;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

public class ProductService {

    public interface ProductCallback {
        void onSuccess(List<Product> products);
        void onError(String error);
    }

    public static void getProducts(ProductCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<Product> products = new ArrayList<>();

            // Тестовые продукты
            Product product1 = new Product();
            product1.setId("1");
            product1.setName("Лосось свежий");
            product1.setDescription("Свежий атлантический лосось, охлажденный");
            product1.setPricePerKg(850.0);
            product1.setCategory("Свежая рыба");
            product1.setAvailable(true);
            product1.setImageUrl("https://example.com/salmon.jpg");
            product1.setCountry("Норвегия");
            product1.setCutType("Филе");
            product1.setChilled(true);
            product1.setFrozen(false);
            product1.setStock(25.5);
            products.add(product1);

            Product product2 = new Product();
            product2.setId("2");
            product2.setName("Тунец замороженный");
            product2.setDescription("Замороженный тунец высшего сорта");
            product2.setPricePerKg(650.0);
            product2.setCategory("Замороженная рыба");
            product2.setAvailable(true);
            product2.setImageUrl("https://example.com/tuna.jpg");
            product2.setCountry("Индонезия");
            product2.setCutType("Стейк");
            product2.setChilled(false);
            product2.setFrozen(true);
            product2.setStock(45.0);
            products.add(product2);

            Product product3 = new Product();
            product3.setId("3");
            product3.setName("Креветки тигровые");
            product3.setDescription("Очищенные тигровые креветки, охлажденные");
            product3.setPricePerKg(1200.0);
            product3.setCategory("Морепродукты");
            product3.setAvailable(true);
            product3.setImageUrl("https://example.com/shrimp.jpg");
            product3.setCountry("Таиланд");
            product3.setCutType("Очищенные");
            product3.setChilled(true);
            product3.setFrozen(false);
            product3.setStock(12.8);
            products.add(product3);

            callback.onSuccess(products);
        }, 1000);
    }
}
