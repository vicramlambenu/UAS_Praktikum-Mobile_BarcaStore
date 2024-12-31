package com.fadhil.barcastore;

import java.io.Serializable;

public class ProductItem implements Serializable {
    private String name;
    private double price;
    private String description;
    private int imageResourceId; // ID gambar di drawable

    // Konstruktor
    public ProductItem(String name, double price, String description, int imageResourceId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResourceId = imageResourceId;
    }

    // Getter methods
    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}
