package com.example.fish;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class ProfileFragment extends Fragment {

    private TextView tvEmail;
    private TextView tvPhone;
    private TextView tvLocation;
    private EditText etPhone;
    private EditText etLocation;
    private Button btnSavePhone;
    private Button btnSaveLocation;
    private Button btnLogout;
    private Button btnOpenOrders;
    private Button btnOpenFavorites;

    private static final String PREFS_NAME = "user_prefs";
    private static final String KEY_LOCATION = "user_location";
    private static final String KEY_PHONE = "user_phone";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);
        tvLocation = view.findViewById(R.id.tv_location);

        etPhone = view.findViewById(R.id.et_phone);
        etLocation = view.findViewById(R.id.et_location);

        btnSavePhone = view.findViewById(R.id.btn_save_phone);
        btnSaveLocation = view.findViewById(R.id.btn_save_location);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnOpenOrders = view.findViewById(R.id.btn_open_orders);
        btnOpenFavorites = view.findViewById(R.id.btn_open_favorites);

        // Заглушка для email
        tvEmail.setText("Email: не задан");

        // Загружаем сохранённый телефон
        String savedPhone = loadPhone();
        if (savedPhone == null || savedPhone.isEmpty()) {
            tvPhone.setText("Телефон: не задан");
        } else {
            tvPhone.setText("Телефон: " + savedPhone);
            etPhone.setText(savedPhone);
        }

        // Загружаем сохранённую локацию
        String savedLocation = loadLocation();
        if (savedLocation == null || savedLocation.isEmpty()) {
            tvLocation.setText("Локация: не указана");
        } else {
            tvLocation.setText("Локация: " + savedLocation);
            etLocation.setText(savedLocation);
        }

        // Сохранить телефон
        btnSavePhone.setOnClickListener(v -> {
            String newPhone = etPhone.getText().toString().trim();
            if (newPhone.isEmpty()) {
                tvPhone.setText("Телефон: не задан");
                savePhone("");
            } else {
                tvPhone.setText("Телефон: " + newPhone);
                savePhone(newPhone);
            }
        });

        // Сохранить локацию
        btnSaveLocation.setOnClickListener(v -> {
            String newLocation = etLocation.getText().toString().trim();
            if (newLocation.isEmpty()) {
                tvLocation.setText("Локация: не указана");
                saveLocation("");
            } else {
                tvLocation.setText("Локация: " + newLocation);
                saveLocation(newLocation);
            }
        });

        setupButtons();

        return view;
    }

    private void savePhone(String phone) {
        if (getContext() == null) return;
        getContext()
                .getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_PHONE, phone)
                .apply();
    }

    private String loadPhone() {
        if (getContext() == null) return "";
        return getContext()
                .getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
                .getString(KEY_PHONE, "");
    }

    private void saveLocation(String location) {
        if (getContext() == null) return;
        getContext()
                .getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_LOCATION, location)
                .apply();
    }

    private String loadLocation() {
        if (getContext() == null) return "";
        return getContext()
                .getSharedPreferences(PREFS_NAME, android.content.Context.MODE_PRIVATE)
                .getString(KEY_LOCATION, "");
    }

    private void setupButtons() {
        // Выход из профиля
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_NEW_TASK
                    | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        // Переход к истории заказов
        btnOpenOrders.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openOrdersFromProfile();
            } else {
                openFragment(new OrdersFragment());
            }
        });

        // Переход к избранному
        btnOpenFavorites.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).openFavoritesFromProfile();
            } else {
                openFragment(new FavoritesFragment());
            }
        });
    }

    private void openFragment(Fragment fragment) {
        if (getActivity() == null) return;

        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
