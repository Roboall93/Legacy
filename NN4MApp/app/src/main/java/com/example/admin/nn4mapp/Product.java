package com.example.admin.nn4mapp;

import android.graphics.Bitmap;

/*
 * Stores import nfo for each product
 * Caches any retrieved images as a bitmap to save on loading
 */

class Product {
    private String name;
    private double cost;
    private String id;
    public Bitmap cachedImage;

    Product(String name, double cost, String id){
        this.name = name;
        this.cost = cost;
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Bitmap getCachedImage() {
        return cachedImage;
    }

    public void setCachedImage(Bitmap cachedImage) {
        this.cachedImage = cachedImage;
    }
}
