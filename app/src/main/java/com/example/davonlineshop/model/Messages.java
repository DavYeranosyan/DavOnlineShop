package com.example.davonlineshop.model;

public class Messages {
    private String id;
    private String from_id;
    private String to_id;
    private String message;

    public Messages() {
    }

    public Messages(String id, String from_id, String to_id, String message) {
        this.id = id;
        this.from_id = from_id;
        this.to_id = to_id;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
