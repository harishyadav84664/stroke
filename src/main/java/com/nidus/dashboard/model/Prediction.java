package com.nidus.dashboard.model;

public enum Prediction {
    ISCHEMIC("Ischemic stroke"),
    HEMORRHAGIC("Hemorrhagic stroke"),
    NO_STROKE("No acute stroke");

    private final String label;

    Prediction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
