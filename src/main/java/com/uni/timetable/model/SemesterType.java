package com.uni.timetable.model;

import java.util.List;

public enum SemesterType {
    WINTER("Zimowy"),
    SUMMER("Letni"),
    WINTER_DIPLOMA("Zimowy dyplomowy"),
    SUMMER_DIPLOMA("Letni dyplomowy");

    public String description;

    SemesterType(String description) {
        this.description = description;
    }

    public static List<SemesterType> normalSemesters() {
        return List.of(WINTER, SUMMER);
    }

    public static SemesterType fromDescription(String description) {
        for (SemesterType semesterType : values()) {
            if (semesterType.description.equals(description)) {
                return semesterType;
            }
        }
        // Handle the case where the description doesn't match any enum constant
        throw new IllegalArgumentException("No enum constant with description: " + description);
    }
}
