package com.example.fish;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order {
    private String id;
    private String userId;
    private List<CartItem> items;
    private double totalPrice;
    private String deliveryAddress;
    private String deliveryTime;
    private String paymentMethod;
    private String status;
    private Date orderDate;

    // Конструктор для создания заказа из корзины (без userId)
    public Order(List<CartItem> items, double totalPrice) {
        this.id = UUID.randomUUID().toString();
        this.userId = "guest"; // Для гостей, или можно оставить пустым
        this.items = new ArrayList<>(items);
        this.totalPrice = totalPrice;
        this.deliveryAddress = "";
        this.deliveryTime = "";
        this.paymentMethod = "Онлайн";
        this.status = "Оплачен";
        this.orderDate = new Date();
    }

    // Конструктор по умолчанию
    public Order() {}

    // Геттеры
    public String getId() { return id; }
    public String getUserId() { return userId; }
    public List<CartItem> getItems() { return items; }
    public double getTotalPrice() { return totalPrice; }
    public String getDeliveryAddress() { return deliveryAddress; }
    public String getDeliveryTime() { return deliveryTime; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getStatus() { return status; }
    public Date getOrderDate() { return orderDate; }

    // Сеттеры
    public void setId(String id) { this.id = id; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setItems(List<CartItem> items) { this.items = items; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }
    public void setDeliveryTime(String deliveryTime) { this.deliveryTime = deliveryTime; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public void setStatus(String status) { this.status = status; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }
}
