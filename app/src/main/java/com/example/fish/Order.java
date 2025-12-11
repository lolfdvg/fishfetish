package com.example.fish;

import java.util.Date;
import java.util.List;

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
}
