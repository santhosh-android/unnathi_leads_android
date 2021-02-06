package com.leadapplication.app.Model;

public class SubProductModel {
    private String subProductId;
    private String subProductCategory;
    private String serviceProductId;
    private String imageSubProduct;
    private String subProductStatus;
    private String productCategory;

    public SubProductModel(String subProductId, String subProductCategory, String serviceProductId, String imageSubProduct, String subProductStatus, String productCategory) {
        this.subProductId = subProductId;
        this.subProductCategory = subProductCategory;
        this.serviceProductId = serviceProductId;
        this.imageSubProduct = imageSubProduct;
        this.subProductStatus = subProductStatus;
        this.productCategory = productCategory;
    }

    public String getSubProductId() {
        return subProductId;
    }

    public String getSubProductCategory() {
        return subProductCategory;
    }

    public String getServiceProductId() {
        return serviceProductId;
    }

    public String getImageSubProduct() {
        return imageSubProduct;
    }

    public String getSubProductStatus() {
        return subProductStatus;
    }

    public String getProductCategory() {
        return productCategory;
    }
}
