package com.example.fish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {
    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private final List<Product> productList = new ArrayList<>();
    private ProgressBar progressBar;

    public CatalogFragment() {
        // обязательный пустой конструктор
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);
        initViews(view);
        setupRecyclerView();
        loadProducts();
        setupFilter();
        return view;
    }

    private void initViews(View view) {
        rvProducts = view.findViewById(R.id.rv_products);
        progressBar = view.findViewById(R.id.progress_bar);
    }

    private void setupRecyclerView() {
        rvProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
        adapter = new ProductAdapter(productList, this::onProductClick);
        rvProducts.setAdapter(adapter);
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        // TODO: Замените на ваш реальный сервис продуктов
        // ProductService.getProducts(new ProductCallback() { ... });
        progressBar.setVisibility(View.GONE);
        Toast.makeText(getContext(), "Загрузка продуктов...", Toast.LENGTH_SHORT).show();
    }

    private void onProductClick(Product product) {
        Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    // Заглушка для фильтра
    private void setupFilter() {
        // TODO: реализуйте фильтр каталога, если нужно
    }
}
