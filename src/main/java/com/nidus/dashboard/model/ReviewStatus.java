package com.nidus.dashboard.model;

public enum ReviewStatus {
    QUEUED("Queued"),
    REVIEWED("Reviewed");

    private final String label;

    ReviewStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
