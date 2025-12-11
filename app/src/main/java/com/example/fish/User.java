package com.example.fish;

import java.util.List;

public class User {
    private String id;
    private String email;
    private String name;
    private String phone;
    private String token;
    private List<String> favoriteFishTypes;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getFavoriteFishTypes() {
        return favoriteFishTypes;
    }

    public void setFavoriteFishTypes(List<String> favoriteFishTypes) {
        this.favoriteFishTypes = favoriteFishTypes;
    }
}
