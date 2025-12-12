package com.example.fish;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class OrdersFragment extends Fragment {
    private RecyclerView rvOrders;
    private OrdersAdapter ordersAdapter;
    private List<Order> orders = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        rvOrders = view.findViewById(R.id.rvOrders);
        rvOrders.setLayoutManager(new LinearLayoutManager(getContext()));

        ordersAdapter = new OrdersAdapter(orders);
        rvOrders.setAdapter(ordersAdapter);

        loadOrders();
        return view;
    }

    private void loadOrders() {
        if (getContext() != null) {
            orders.clear();
            orders.addAll(OrderManager.getOrders(getContext()));
            ordersAdapter.notifyDataSetChanged();
        }
    }
}
