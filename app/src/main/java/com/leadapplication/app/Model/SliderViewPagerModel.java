package com.leadapplication.app.Model;

public class SliderViewPagerModel {

    private String idImage;
    private String imgSlider;
    private String status;
    private String created_date;
    private String updated_date;

    public SliderViewPagerModel(String idImage, String imgSlider, String status, String created_date, String updated_date) {
        this.idImage = idImage;
        this.imgSlider = imgSlider;
        this.status = status;
        this.created_date = created_date;
        this.updated_date = updated_date;
    }

    public String getIdImage() {
        return idImage;
    }

    public String getImgSlider() {
        return imgSlider;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_date() {
        return created_date;
    }

    public String getUpdated_date() {
        return updated_date;
    }
}
