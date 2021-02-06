package com.leadapplication.app.Model;

public class PincodeModel {
    private String pincodeId;
    private String countryIdString;
    private String stateIdString;
    private String districtIdString;
    private String mandalIdString;
    private String villageId;
    private String pincode;
    private String status;

    public PincodeModel(String pincodeId, String countryIdString, String stateIdString,
                        String districtIdString, String mandalIdString, String villageId, String pincode, String status) {
        this.pincodeId = pincodeId;
        this.countryIdString = countryIdString;
        this.stateIdString = stateIdString;
        this.districtIdString = districtIdString;
        this.mandalIdString = mandalIdString;
        this.villageId = villageId;
        this.pincode = pincode;
        this.status = status;
    }

    public String getPincodeId() {
        return pincodeId;
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

    public String getVillageId() {
        return villageId;
    }

    public String getPincode() {
        return pincode;
    }

    public String getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return pincode;
    }
}
