package com.example.davonlineshop.model;

public class Message {
    private String id_one;
    private String id_two;

    public Message() {
    }

    public Message(String id_one, String id_two) {
        this.id_one = id_one;
        this.id_two = id_two;
    }

    public String getId_one() {
        return id_one;
    }

    public void setId_one(String id_one) {
        this.id_one = id_one;
    }

    public String getId_two() {
        return id_two;
    }

    public void setId_two(String id_two) {
        this.id_two = id_two;
    }
}
