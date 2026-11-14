package com.nidus.dashboard.model;

public enum Modality {
    CT("CT"),
    MRI("MRI"),
    CTA("CTA");

    private final String label;

    Modality(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
