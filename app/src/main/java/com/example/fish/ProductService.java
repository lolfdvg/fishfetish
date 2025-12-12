package com.example.fish;

import android.os.Handler;
import android.os.Looper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductService {

    public interface ProductCallback {
        void onSuccess(List<Product> products);
        void onError(String error);
    }

    public static void getProducts(ProductCallback callback) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            List<Product> products = new ArrayList<>();

            // Лосось
            Product product1 = new Product();
            product1.setId("1");
            product1.setName("Лосось свежий");
            product1.setDescription("Свежий атлантический лосось, охлажденный");
            product1.setPricePerKg(850.0);
            product1.setCategory("Свежая рыба");
            product1.setAvailable(true);
            product1.setImageUrl("https://avatars.mds.yandex.net/i?id=b7d87eefdab07153feb3f50ab77d8f83ef56262a-4495871-images-thumbs&n=13");
            product1.setCountry("Норвегия");
            product1.setCutType("Филе");
            product1.setChilled(true);
            product1.setFrozen(false);
            product1.setStock(25.5);
            product1.setRating(4.8);
            product1.setReviewCount(127);
            product1.setReviews(Arrays.asList("Отличная рыба!", "Свежайший лосось", "Качество супер"));
            products.add(product1);

            // Тунец
            Product product2 = new Product();
            product2.setId("2");
            product2.setName("Тунец замороженный");
            product2.setDescription("Замороженный тунец высшего сорта");
            product2.setPricePerKg(650.0);
            product2.setCategory("Замороженная рыба");
            product2.setAvailable(true);
            product2.setImageUrl("https://avatars.mds.yandex.net/i?id=8ee2c3f426055789b94ae2ea3eb111e672a25f5e-8969202-images-thumbs&n=13");
            product2.setCountry("Индонезия");
            product2.setCutType("Стейк");
            product2.setChilled(false);
            product2.setFrozen(true);
            product2.setStock(45.0);
            product2.setRating(4.5);
            product2.setReviewCount(89);
            product2.setReviews(Arrays.asList("Хороший тунец", "Для суши идеально"));
            products.add(product2);

            // Креветки
            Product product3 = new Product();
            product3.setId("3");
            product3.setName("Креветки тигровые");
            product3.setDescription("Очищенные тигровые креветки, охлажденные");
            product3.setPricePerKg(1200.0);
            product3.setCategory("Морепродукты");
            product3.setAvailable(true);
            product3.setImageUrl("https://avatars.mds.yandex.net/i?id=f2efd05cb23512b48aea9818f68ebc721e1ceb8e-5233839-images-thumbs&n=13");
            product3.setCountry("Таиланд");
            product3.setCutType("Очищенные");
            product3.setChilled(true);
            product3.setFrozen(false);
            product3.setStock(12.8);
            product3.setRating(4.9);
            product3.setReviewCount(203);
            product3.setReviews(Arrays.asList("Великолепные креветки!", "Очень вкусные", "Рекомендую!"));
            products.add(product3);

            callback.onSuccess(products);
        }, 1000);
    }
}
