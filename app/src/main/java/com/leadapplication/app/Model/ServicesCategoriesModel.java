package com.leadapplication.app.Model;

public class ServicesCategoriesModel {
    private String id;
    private String categoryName;
    private String image;
    private String serviceStatus;

    public ServicesCategoriesModel(String id, String categoryName, String image, String serviceStatus) {
        this.id = id;
        this.categoryName = categoryName;
        this.image = image;
        this.serviceStatus = serviceStatus;
    }

    public String getId() {
        return id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getImage() {
        return image;
    }

    public String getServiceStatus() {
        return serviceStatus;
    }
}
