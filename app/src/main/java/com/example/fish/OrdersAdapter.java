package com.example.fish;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {
    private List<Order> orders;

    public OrdersAdapter(List<Order> orders) {
        this.orders = orders;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.tvOrderId.setText("Заказ #" + order.getId().substring(0, 8));
        holder.tvTotalPrice.setText(String.format("Сумма: %.2f ₽", order.getTotalPrice()));

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault());
        holder.tvDate.setText("Дата: " + sdf.format(order.getOrderDate()));
        holder.tvStatus.setText("Статус: " + order.getStatus());
    }

    @Override
    public int getItemCount() {
        return orders != null ? orders.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvTotalPrice, tvDate, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }
}
