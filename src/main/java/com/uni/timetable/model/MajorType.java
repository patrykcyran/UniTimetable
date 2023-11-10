package com.uni.timetable.model;

public enum MajorType {
    ENGINEERING("Inżynierskie"),
    BACHELOR("Licencjackie"),
    MASTER("Magisterskie"),
    PHD("Doktorat");

    private String description;

    MajorType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
