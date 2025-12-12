package com.example.fish;

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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<CartItem> cartItems;
    private OnCartItemActionListener listener;

    public interface OnCartItemActionListener {
        void onQuantityChanged();
        void onItemRemoved();
    }

    public CartAdapter(List<CartItem> cartItems, OnCartItemActionListener listener) {
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CartItem cartItem = cartItems.get(position);
        Product product = cartItem.getProduct();

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(String.format("%.2f ₽/кг", product.getPricePerKg()));
        holder.tvWeight.setText(String.format("%.2f кг", cartItem.getWeight()));
        holder.tvItemTotal.setText(String.format("%.2f ₽", product.getPricePerKg() * cartItem.getWeight()));

        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .into(holder.ivProduct);

        holder.btnPlus.setOnClickListener(v -> {
            cartItem.setWeight(cartItem.getWeight() + 0.1);
            holder.tvWeight.setText(String.format("%.2f кг", cartItem.getWeight()));
            holder.tvItemTotal.setText(String.format("%.2f ₽", product.getPricePerKg() * cartItem.getWeight()));
            CartManager.updateCartItem(holder.itemView.getContext(), cartItem);
            if (listener != null) listener.onQuantityChanged();
        });

        holder.btnMinus.setOnClickListener(v -> {
            if (cartItem.getWeight() > 0.1) {
                cartItem.setWeight(cartItem.getWeight() - 0.1);
                holder.tvWeight.setText(String.format("%.2f кг", cartItem.getWeight()));
                holder.tvItemTotal.setText(String.format("%.2f ₽", product.getPricePerKg() * cartItem.getWeight()));
                CartManager.updateCartItem(holder.itemView.getContext(), cartItem);
                if (listener != null) listener.onQuantityChanged();
            }
        });

        holder.btnRemove.setOnClickListener(v -> {
            CartManager.removeFromCart(holder.itemView.getContext(), cartItem);
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                cartItems.remove(pos);
                notifyItemRemoved(pos);
                if (listener != null) listener.onItemRemoved();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems != null ? cartItems.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct;
        TextView tvName, tvPrice, tvWeight, tvItemTotal;
        Button btnPlus, btnMinus, btnRemove;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvWeight = itemView.findViewById(R.id.tvWeight);
            tvItemTotal = itemView.findViewById(R.id.tvItemTotal);
            btnPlus = itemView.findViewById(R.id.btnPlus);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnRemove = itemView.findViewById(R.id.btnRemove);
        }
    }
}
