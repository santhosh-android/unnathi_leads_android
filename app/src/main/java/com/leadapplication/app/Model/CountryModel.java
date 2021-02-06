package com.leadapplication.app.Model;

public class CountryModel {
    private String countryId;
    private String countryName;
    private String countryStatus;

    public CountryModel(String countryId, String countryName, String countryStatus) {
        this.countryId = countryId;
        this.countryName = countryName;
        this.countryStatus = countryStatus;
    }

    public String getCountryId() {
        return countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public String getCountryStatus() {
        return countryStatus;
    }

    @Override
    public String toString() {
        return countryName;
    }
}
