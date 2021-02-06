package com.leadapplication.app.Model;

public class SubServicesModel {
    private String subServiceId;
    private String subServiceCategory;
    private String serviceCategoryId;
    private String imageSubService;
    private String subServiceStatus;
    private String serviceCategory;

    public SubServicesModel(String subServiceId, String subServiceCategory, String serviceCategoryId, String imageSubService, String subServiceStatus, String serviceCategory) {
        this.subServiceId = subServiceId;
        this.subServiceCategory = subServiceCategory;
        this.serviceCategoryId = serviceCategoryId;
        this.imageSubService = imageSubService;
        this.subServiceStatus = subServiceStatus;
        this.serviceCategory = serviceCategory;
    }

    public String getSubServiceId() {
        return subServiceId;
    }

    public String getSubServiceCategory() {
        return subServiceCategory;
    }

    public String getServiceCategoryId() {
        return serviceCategoryId;
    }

    public String getImageSubService() {
        return imageSubService;
    }

    public String getSubServiceStatus() {
        return subServiceStatus;
    }

    public String getServiceCategory() {
        return serviceCategory;
    }
}
