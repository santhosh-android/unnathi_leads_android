package com.leadapplication.app.Model;

public class NotificationListModel {
    private String title;
    private String message;
    private String date;

    public NotificationListModel(String title, String message, String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}
