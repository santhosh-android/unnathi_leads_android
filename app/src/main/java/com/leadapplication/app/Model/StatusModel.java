package com.leadapplication.app.Model;

public class StatusModel {
    private String id;
    private String title;
    private String priority;

    public StatusModel(String id, String title, String priority) {
        this.id = id;
        this.title = title;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return title;
    }
}
