package com.uni.timetable.model;

public enum SemesterType {
    WINTER("Zimowy"),
    SUMMER("Letni");

    private String description;

    SemesterType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public static SemesterType fromDescription(String description) {
        for (SemesterType semesterType : values()) {
            if (semesterType.description.equals(description)) {
                return semesterType;
            }
        }
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}
