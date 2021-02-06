package com.leadapplication.app.Model;

public class CityModel {
    private String id;
    private String country_id;
    private String state_id;
    private String district_id;
    private String city;
    private String status;

    public CityModel(String id, String country_id, String state_id, String district_id, String city, String status) {
        this.id = id;
        this.country_id = country_id;
        this.state_id = state_id;
        this.district_id = district_id;
        this.city = city;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public String getCity() {
        return city;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return city;
    }
}
