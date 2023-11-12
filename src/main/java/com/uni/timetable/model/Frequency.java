package com.uni.timetable.model;

public enum Frequency {
    WEEKLY("Tygodniowo"),
    FORTNIGHTLY("Dwutygodniowo");

    private String description;

    Frequency(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static Frequency fromDescription(String description) {
        for (Frequency frequency : values()) {
            if (frequency.description.equals(description)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}
