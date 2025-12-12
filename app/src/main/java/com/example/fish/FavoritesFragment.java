package com.example.fish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView rvFavorites;
    private ProductAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_favorites, container, false);

        rvFavorites = view.findViewById(R.id.rv_favorites);
        rvFavorites.setLayoutManager(new LinearLayoutManager(getContext()));

        // загружаем избранные товары
        List<Product> favorites = FavoritesManager.getFavorites(requireContext());

        adapter = new ProductAdapter(favorites, product -> {
            // открываем детали товара через Intent
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        // кнопка в списке избранного удаляет товар из избранного
        adapter.setAddToCartListener(product -> {
            FavoritesManager.removeFromFavorites(requireContext(), product);
            List<Product> updated = FavoritesManager.getFavorites(requireContext());
            adapter.setProducts(updated);
        });

        rvFavorites.setAdapter(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null) {
            List<Product> favorites = FavoritesManager.getFavorites(requireContext());
            adapter.setProducts(favorites);
        }
    }
}
