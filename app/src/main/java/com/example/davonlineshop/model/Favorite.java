package com.example.davonlineshop.model;

public class Favorite {
    private String id;
    private String user_id;
    private String table_name;
    private String favorite_product_id;

    public Favorite() {
    }

    public Favorite(String id, String user_id, String table_name, String favorite_product_id) {
        this.id = id;
        this.user_id = user_id;
        this.table_name = table_name;
        this.favorite_product_id = favorite_product_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTable_name() {
        return table_name;
    }

    public void setTable_name(String table_name) {
        this.table_name = table_name;
    }

    public String getFavorite_product_id() {
        return favorite_product_id;
    }

    public void setFavorite_product_id(String favorite_product_id) {
        this.favorite_product_id = favorite_product_id;
    }
}
