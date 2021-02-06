package com.leadapplication.app.Model;

public class DistrictModel {
    private String distId;
    private String country_id;
    private String state_id;
    private String districtName;
    private String districtStatus;

    public DistrictModel(String distId, String country_id, String state_id, String districtName, String districtStatus) {
        this.distId = distId;
        this.country_id = country_id;
        this.state_id = state_id;
        this.districtName = districtName;
        this.districtStatus = districtStatus;
    }

    public String getDistId() {
        return distId;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getDistrictStatus() {
        return districtStatus;
    }

    @Override
    public String toString() {
        return districtName;
    }
}
