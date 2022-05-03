package com.example.davonlineshop.model;

public class List {
    private String id;
    private String nameProduct;
    private String description;
    private int price;
    private String image_id;

    public List() {
    }

    public List(String id, String nameProduct, String description, String image_id) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.description = description;
        this.image_id = image_id;
    }

    public List(String id, String nameProduct, String description, int price, String image_id) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.description = description;
        this.price = price;
        this.image_id = image_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }
}
