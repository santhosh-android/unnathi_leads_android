package com.leadapplication.app.Model;

public class IamSpinnerModel {
    private String iId;
    private String iamString;

    public IamSpinnerModel(String iId, String iamString) {
        this.iId = iId;
        this.iamString = iamString;
    }

    public String getiId() {
        return iId;
    }

    public void setiId(String iId) {
        this.iId = iId;
    }

    public String getIamString() {
        return iamString;
    }

    public void setIamString(String iamString) {
        this.iamString = iamString;
    }

    @Override
    public String toString() {
        return iamString;
    }
}
