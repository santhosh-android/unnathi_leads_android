package com.leadapplication.app.Model;

public class StateModel {
    private String stateId;
    private String cId;
    private String stateName;
    private String stateStatus;

    public StateModel(String stateId, String cId, String stateName, String stateStatus) {
        this.stateId = stateId;
        this.cId = cId;
        this.stateName = stateName;
        this.stateStatus = stateStatus;
    }

    public String getStateId() {
        return stateId;
    }

    public String getcId() {
        return cId;
    }

    public String getStateName() {
        return stateName;
    }

    public String getStateStatus() {
        return stateStatus;
    }

    @Override
    public String toString() {
        return stateName;
    }
}
