package com.example.fish;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private List<Product> products;
    private OnProductClickListener listener;
    private OnAddToCartClickListener addToCartListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public interface OnAddToCartClickListener {
        void onAddToCartClick(Product product);
    }

    public ProductAdapter(List<Product> products, OnProductClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    public void setAddToCartListener(OnAddToCartClickListener listener) {
        this.addToCartListener = listener;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        Product product = products.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%.2f ₽/кг", product.getPricePerKg()));
        holder.tvCategory.setText(product.getCategory());

        if (!product.isAvailable()) {
            holder.tvStatus.setText("Нет в наличии");
            holder.tvStatus.setTextColor(Color.RED);
            holder.btnAddToCart.setEnabled(false);
        } else {
            holder.tvStatus.setText("В наличии");
            holder.tvStatus.setTextColor(Color.GREEN);
            holder.btnAddToCart.setEnabled(true);
        }

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.placeholderfish)
                .into(holder.ivProduct);

        // клик по кнопке
        holder.btnAddToCart.setOnClickListener(v -> {
            if (listener != null) {
                listener.onProductClick(product);
            }
        });

        // === КНОПКА ИЗБРАННОГО ===
        boolean isFavorite = FavoritesManager.isFavorite(holder.itemView.getContext(), product);
        holder.btnFavorite.setText(isFavorite ? "Удалить из избранного" : "Добавить в избранное");

        holder.btnFavorite.setOnClickListener(v -> {
            boolean nowFavorite = FavoritesManager.isFavorite(v.getContext(), product);
            if (nowFavorite) {
                FavoritesManager.removeFromFavorites(v.getContext(), product);
                holder.btnFavorite.setText("Добавить в избранное");
            } else {
                FavoritesManager.addToFavorites(v.getContext(), product);
                holder.btnFavorite.setText("Удалить из избранного");
            }
        });
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivProduct;
        TextView tvName, tvPrice, tvCategory, tvStatus;
        Button btnAddToCart;
        Button btnFavorite; // новая кнопка

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.iv_product);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPrice = itemView.findViewById(R.id.tv_price);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnAddToCart = itemView.findViewById(R.id.btn_add_to_cart);
            btnFavorite = itemView.findViewById(R.id.btnFavorite); // инициализация избранного
        }
    }
}
