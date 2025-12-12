package com.example.fish;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
    private Button btnFilter;

    // параметры фильтра
    private Double currentMinPrice = null;
    private Double currentMaxPrice = null;
    private boolean currentOnlyAvailable = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        rvProducts = view.findViewById(R.id.rv_products);
        progressBar = view.findViewById(R.id.progress_bar);
        etSearch = view.findViewById(R.id.et_search);
        btnFilter = view.findViewById(R.id.btn_filter);

        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ProductAdapter(filteredProducts, product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        adapter.setAddToCartListener(product -> {
            FavoritesManager.addToFavorites(requireContext(), product);
            Toast.makeText(requireContext(),
                    "Добавлено в избранное: " + product.getName(),
                    Toast.LENGTH_SHORT).show();
        });

        rvProducts.setAdapter(adapter);

        loadProducts();
        setupFilterButton();

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
                applyFilter(); // сразу показываем с текущими параметрами (по умолчанию без ограничений)
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

    private void setupFilterButton() {
        btnFilter.setOnClickListener(v -> showFilterDialog());
    }

    private void showFilterDialog() {
        if (getContext() == null) return;

        LinearLayout layout = new LinearLayout(getContext());
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        final EditText etMinPrice = new EditText(getContext());
        etMinPrice.setHint("Минимальная цена");
        etMinPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (currentMinPrice != null) {
            etMinPrice.setText(String.valueOf(currentMinPrice));
        }
        layout.addView(etMinPrice);

        final EditText etMaxPrice = new EditText(getContext());
        etMaxPrice.setHint("Максимальная цена");
        etMaxPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        if (currentMaxPrice != null) {
            etMaxPrice.setText(String.valueOf(currentMaxPrice));
        }
        layout.addView(etMaxPrice);

        final CheckBox cbOnlyAvailable = new CheckBox(getContext());
        cbOnlyAvailable.setText("Только в наличии");
        cbOnlyAvailable.setChecked(currentOnlyAvailable);
        layout.addView(cbOnlyAvailable);

        new AlertDialog.Builder(getContext())
                .setTitle("Фильтр товаров")
                .setView(layout)
                .setPositiveButton("Применить", (dialog, which) -> {
                    String minStr = etMinPrice.getText().toString().trim();
                    String maxStr = etMaxPrice.getText().toString().trim();

                    currentMinPrice = parseDoubleOrNull(minStr);
                    currentMaxPrice = parseDoubleOrNull(maxStr);
                    currentOnlyAvailable = cbOnlyAvailable.isChecked();

                    applyFilter();
                })
                .setNegativeButton("Сбросить", (dialog, which) -> {
                    currentMinPrice = null;
                    currentMaxPrice = null;
                    currentOnlyAvailable = false;
                    applyFilter();
                })
                .show();
    }

    private Double parseDoubleOrNull(String s) {
        if (TextUtils.isEmpty(s)) return null;
        try {
            return Double.parseDouble(s.replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void applyFilter() {
        filteredProducts.clear();

        String query = etSearch.getText() != null
                ? etSearch.getText().toString().trim()
                : "";
        String lower = query.toLowerCase();

        for (Product p : allProducts) {
            // фильтр по поисковой строке (имя/категория)
            String name = p.getName() != null ? p.getName().toLowerCase() : "";
            String category = p.getCategory() != null ? p.getCategory().toLowerCase() : "";
            boolean matchesSearch = TextUtils.isEmpty(lower)
                    || name.contains(lower)
                    || category.contains(lower);

            if (!matchesSearch) continue;

            // фильтр по наличию
            if (currentOnlyAvailable && !p.isAvailable()) {
                continue;
            }

            // фильтр по цене
            double price = p.getPricePerKg();
            if (currentMinPrice != null && price < currentMinPrice) {
                continue;
            }
            if (currentMaxPrice != null && price > currentMaxPrice) {
                continue;
            }

            filteredProducts.add(p);
        }

        adapter.setProducts(filteredProducts);

        if (filteredProducts.isEmpty()) {
            Toast.makeText(getContext(),
                    "По заданным параметрам ничего не найдено",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
