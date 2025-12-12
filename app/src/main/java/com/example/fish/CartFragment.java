package com.example.fish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {
    private RecyclerView rvCartItems;
    private CartAdapter cartAdapter;
    private List<CartItem> cartItems = new ArrayList<>();
    private TextView tvTotalPrice;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        rvCartItems = view.findViewById(R.id.rvCartItems);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        Button btnCheckout = view.findViewById(R.id.btnCheckout);

        rvCartItems.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(cartItems, new CartAdapter.OnCartItemActionListener() {
            @Override
            public void onQuantityChanged() {
                updateTotalPrice();
            }

            @Override
            public void onItemRemoved() {
                updateTotalPrice();
            }
        });
        rvCartItems.setAdapter(cartAdapter);

        btnCheckout.setOnClickListener(v -> checkout());

        loadCart();
        return view;
    }

    private void loadCart() {
        cartItems.clear();
        cartItems.addAll(CartManager.getCartItems(getContext()));
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getProduct().getPricePerKg() * item.getWeight();
        }
        tvTotalPrice.setText(String.format("Итого: %.2f ₽", total));
    }

    private void checkout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(getContext(), "Корзина пуста", Toast.LENGTH_SHORT).show();
            return;
        }

        double totalPrice = 0;
        for (CartItem item : cartItems) {
            totalPrice += item.getProduct().getPricePerKg() * item.getWeight();
        }

        // Используем "guest" для всех пользователей (AuthService не имеет нужных методов)
        String userId = "guest";

        // Сохраняем заказ
        OrderManager.addOrder(getContext(), new ArrayList<>(cartItems), totalPrice, userId);

        // Очищаем корзину
        CartManager.clearCart(getContext());

        // Обновляем UI
        cartItems.clear();
        cartAdapter.notifyDataSetChanged();
        updateTotalPrice();

        Toast.makeText(getContext(), "Заказ успешно оплачен!", Toast.LENGTH_LONG).show();
    }
}
