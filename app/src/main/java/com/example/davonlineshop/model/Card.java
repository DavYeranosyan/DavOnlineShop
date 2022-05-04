package com.example.davonlineshop.model;

public class Card {
    private String id;
    private String product_id;
    private String table_name;
    private String user_id;

    public Card() {
    }

    public Card(String id, String product_id, String table_name, String user_id) {
        this.id = id;
        this.product_id = product_id;
        this.table_name = table_name;
        this.user_id = user_id;
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

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
