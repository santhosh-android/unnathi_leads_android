package com.leadapplication.app.Model;

public class VillageModel {
    private String idVillage;
    private String countryIdString;
    private String stateIdString;
    private String districtIdString;
    private String mandalIdString;
    private String villageString;
    private String villageStatus;

    public VillageModel(String idVillage, String countryIdString, String stateIdString, String districtIdString,
                        String mandalIdString, String villageString, String villageStatus) {
        this.idVillage = idVillage;
        this.countryIdString = countryIdString;
        this.stateIdString = stateIdString;
        this.districtIdString = districtIdString;
        this.mandalIdString = mandalIdString;
        this.villageString = villageString;
        this.villageStatus = villageStatus;
    }

    public String getIdVillage() {
        return idVillage;
    }

    public String getCountryIdString() {
        return countryIdString;
    }

    public String getStateIdString() {
        return stateIdString;
    }

    public String getDistrictIdString() {
        return districtIdString;
    }

    public String getMandalIdString() {
        return mandalIdString;
    }

    public String getVillageString() {
        return villageString;
    }

    public String getVillageStatus() {
        return villageStatus;
    }

    @Override
    public String toString() {
        return villageString;
    }
}
