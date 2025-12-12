package com.example.fish;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    private Product product;

    private ImageView ivProduct;
    private TextView tvName, tvDescription, tvPrice, tvCountry, tvCutType;
    private Button btnAddToCart;
    private EditText etWeight;
    private TextView tvTotalPrice;

    // рейтинг
    private RatingBar ratingBar;
    private TextView tvRatingValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        product = getIntent().getParcelableExtra("product");

        initViews();
        setupProductDetails();

        // Обработчик кнопки "Добавить в корзину"
        btnAddToCart.setOnClickListener(v -> {
            try {
                double weight = Double.parseDouble(etWeight.getText().toString());
                if (weight > 0) {
                    CartItem cartItem = new CartItem(product, weight, "");
                    CartManager.addToCart(ProductDetailActivity.this, cartItem);
                    Toast.makeText(
                            ProductDetailActivity.this,
                            "Добавлено в корзину: " + product.getName(),
                            Toast.LENGTH_SHORT
                    ).show();
                } else {
                    Toast.makeText(
                            ProductDetailActivity.this,
                            "Введите вес больше 0",
                            Toast.LENGTH_SHORT
                    ).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(
                        ProductDetailActivity.this,
                        "Введите корректный вес",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });
    }

    private void initViews() {
        ivProduct = findViewById(R.id.iv_product);
        tvName = findViewById(R.id.tv_name);
        tvDescription = findViewById(R.id.tv_description);
        tvPrice = findViewById(R.id.tv_price);
        tvCountry = findViewById(R.id.tv_country);
        tvCutType = findViewById(R.id.tv_cut_type);
        btnAddToCart = findViewById(R.id.btn_add_to_cart);
        etWeight = findViewById(R.id.et_weight);
        tvTotalPrice = findViewById(R.id.tv_total_price);

        // элементы рейтинга
        ratingBar = findViewById(R.id.rating_bar);
        tvRatingValue = findViewById(R.id.tv_rating_value);
    }

    private void setupProductDetails() {
        if (product == null) return;

        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        tvPrice.setText(String.format("%.2f ₽/кг", product.getPricePerKg()));
        tvCountry.setText(product.getCountry());
        tvCutType.setText(product.getCutType());

        // изображение
        Glide.with(this)
                .load(product.getImageUrl())
                .into(ivProduct);

        // начальные значения рейтинга из модели
        double currentRating = product.getRating();
        int currentCount = product.getReviewCount();
        ratingBar.setRating((float) currentRating);
        tvRatingValue.setText(String.format("%.1f (%d)", currentRating, currentCount));

        // изменение рейтинга пользователем
        ratingBar.setOnRatingBarChangeListener((bar, rating, fromUser) -> {
            if (!fromUser) return;

            double oldRating = product.getRating();
            int oldCount = product.getReviewCount();

            int newCount = oldCount + 1;
            double newAvg = (oldRating * oldCount + rating) / newCount;

            product.setRating(newAvg);
            product.setReviewCount(newCount);

            tvRatingValue.setText(String.format("%.1f (%d)", newAvg, newCount));

            Toast.makeText(
                    ProductDetailActivity.this,
                    "Спасибо за оценку!",
                    Toast.LENGTH_SHORT
            ).show();
        });

        // перерасчет итоговой цены при вводе веса
        etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    double weight = Double.parseDouble(s.toString());
                    double totalPrice = weight * product.getPricePerKg();
                    tvTotalPrice.setText(String.format("Итого: %.2f ₽", totalPrice));
                } catch (NumberFormatException e) {
                    tvTotalPrice.setText("Итого: 0.00 ₽");
                }
            }
        });
    }
}
