package com.example.fish;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Product implements Parcelable {

    private String id;
    private String name;
    private String description;
    private double pricePerKg;
    private String category;
    private boolean isAvailable;
    private String imageUrl;
    private String country;
    private String cutType;
    private boolean isChilled;
    private boolean isFrozen;
    private double stock;

    public Product() {
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        pricePerKg = in.readDouble();
        category = in.readString();
        isAvailable = in.readByte() != 0;
        imageUrl = in.readString();
        country = in.readString();
        cutType = in.readString();
        isChilled = in.readByte() != 0;
        isFrozen = in.readByte() != 0;
        stock = in.readDouble();
    }

    // getters/setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPricePerKg() { return pricePerKg; }
    public void setPricePerKg(double pricePerKg) { this.pricePerKg = pricePerKg; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCutType() { return cutType; }
    public void setCutType(String cutType) { this.cutType = cutType; }

    public boolean isChilled() { return isChilled; }
    public void setChilled(boolean chilled) { isChilled = chilled; }

    public boolean isFrozen() { return isFrozen; }
    public void setFrozen(boolean frozen) { isFrozen = frozen; }

    public double getStock() { return stock; }
    public void setStock(double stock) { this.stock = stock; }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(pricePerKg);
        dest.writeString(category);
        dest.writeByte((byte) (isAvailable ? 1 : 0));
        dest.writeString(imageUrl);
        dest.writeString(country);
        dest.writeString(cutType);
        dest.writeByte((byte) (isChilled ? 1 : 0));
        dest.writeByte((byte) (isFrozen ? 1 : 0));
        dest.writeDouble(stock);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
