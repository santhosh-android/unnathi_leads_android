package com.leadapplication.app.Model;

public class ProductCategoryModel {
    private String productId;
    private String productName;
    private String imageProduct;
    private String productStatus;

    public ProductCategoryModel(String productId, String productName, String imageProduct, String productStatus) {
        this.productId = productId;
        this.productName = productName;
        this.imageProduct = imageProduct;
        this.productStatus = productStatus;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public String getProductStatus() {
        return productStatus;
    }
}
