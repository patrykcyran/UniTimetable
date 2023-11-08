package com.uni.timetable.model;

public enum SemesterType {
    WINTER("Zimowy"),
    SUMMER("Letni"),
    WINTER_DIPLOMA("Zimowy dyplomowy"),
    SUMMER_DIPLOMA("Letni dyplomowy");

    public String description;

    SemesterType(String description) {
        this.description = description;
    }
}
