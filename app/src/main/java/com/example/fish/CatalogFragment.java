package com.example.fish;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatalogFragment extends Fragment {

    private RecyclerView rvProducts;
    private ProductAdapter adapter;
    private List<Product> allProducts = new ArrayList<>();
    private List<Product> filteredProducts = new ArrayList<>();
    private EditText etSearch;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        rvProducts = view.findViewById(R.id.rv_products);
        progressBar = view.findViewById(R.id.progress_bar);
        etSearch = view.findViewById(R.id.et_search);

        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProductAdapter(filteredProducts, new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                Intent intent = new Intent(getContext(), ProductDetailActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
            }
        });

        // Кнопка "В избранное"
        adapter.setAddToCartListener(product -> {
            FavoritesManager.addToFavorites(requireContext(), product);
            Toast.makeText(requireContext(),
                    "Добавлено в избранное: " + product.getName(),
                    Toast.LENGTH_SHORT).show();
        });

        rvProducts.setAdapter(adapter);

        loadProducts();
        setupSearch();

        return view;
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);
        ProductService.getProducts(new ProductService.ProductCallback() {
            @Override
            public void onSuccess(List<Product> products) {
                progressBar.setVisibility(View.GONE);
                allProducts.clear();
                allProducts.addAll(products);
                applyFilter("");
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        "Ошибка загрузки: " + error,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private void applyFilter(String query) {
        filteredProducts.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredProducts.addAll(allProducts);
        } else {
            String lower = query.toLowerCase();
            for (Product p : allProducts) {
                String name = p.getName() != null ? p.getName().toLowerCase() : "";
                String category = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
                if (name.contains(lower) || category.contains(lower)) {
                    filteredProducts.add(p);
                }
            }
        }
        adapter.setProducts(filteredProducts);
    }
}
