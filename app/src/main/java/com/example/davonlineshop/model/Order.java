package com.example.davonlineshop.model;

public class Order {
    private String id;
    private String product_id;
    private String table_name;
    private Integer count;
    private String full_price;
    private String email;
    private String phone_number;
    private String location;

    public Order() {
    }

    public Order(String id, String product_id, String table_name, Integer count, String full_price, String email, String phone_number, String location) {
        this.id = id;
        this.product_id = product_id;
        this.table_name = table_name;
        this.count = count;
        this.full_price = full_price;
        this.email = email;
        this.phone_number = phone_number;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getFull_price() {
        return full_price;
    }

    public void setFull_price(String full_price) {
        this.full_price = full_price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
