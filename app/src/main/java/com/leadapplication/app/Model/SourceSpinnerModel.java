package com.leadapplication.app.Model;

public class SourceSpinnerModel {
    private String sourceId;
    private String source;

    public SourceSpinnerModel(String sourceId, String source) {
        this.sourceId = sourceId;
        this.source = source;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public String toString() {
        return source;
    }
}
