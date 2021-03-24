package com.example.davonlineshop.ui.notifications;

public class Sender {
    public Notification notification;
    public String to;

    public Sender(Notification notification, String to) {
        this.notification = notification;
        this.to = to;
    }
}
