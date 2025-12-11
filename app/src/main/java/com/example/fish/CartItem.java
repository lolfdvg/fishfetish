package com.example.fish;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class CartItem implements Parcelable {

    private Product product;
    private double weight;
    private String specialRequest;

    public CartItem() {
    }

    public CartItem(Product product, double weight, String specialRequest) {
        this.product = product;
        this.weight = weight;
        this.specialRequest = specialRequest;
    }

    protected CartItem(Parcel in) {
        product = in.readParcelable(Product.class.getClassLoader());
        weight = in.readDouble();
        specialRequest = in.readString();
    }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public String getSpecialRequest() { return specialRequest; }
    public void setSpecialRequest(String specialRequest) { this.specialRequest = specialRequest; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(product, flags);
        dest.writeDouble(weight);
        dest.writeString(specialRequest);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CartItem> CREATOR = new Creator<CartItem>() {
        @Override
        public CartItem createFromParcel(Parcel in) {
            return new CartItem(in);
        }

        @Override
        public CartItem[] newArray(int size) {
            return new CartItem[size];
        }
    };
}
