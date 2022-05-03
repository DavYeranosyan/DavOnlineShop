package com.example.davonlineshop.model;

public class Worker {
    private String id;
    private String email;
    private String description;
    private String phone_number;
    private String list_product_id;

    public Worker() {
    }

    public Worker(String id, String email, String description, String phone_number, String list_product_id) {
        this.id = id;
        this.email = email;
        this.description = description;
        this.phone_number = phone_number;
        this.list_product_id = list_product_id;
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getList_product_id() {
        return list_product_id;
    }

    public void setList_product_id(String list_product_id) {
        this.list_product_id = list_product_id;
    }
}
