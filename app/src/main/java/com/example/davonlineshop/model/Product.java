package com.example.davonlineshop.model;

import java.util.Date;

public class Product {
    private String id;
    private String name;
    private String desc;
    private String userID;
    private Category category;
    private double price;
    private int count;
    private int active;
    private double rating;
    private Date date;

    public Product() {
    }

    public Product(String id, String name, String desc, String userID, Category category, double price, int count, int active, double rating, Date date) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.userID = userID;
        this.category = category;
        this.price = price;
        this.count = count;
        this.active = active;
        this.rating = rating;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
