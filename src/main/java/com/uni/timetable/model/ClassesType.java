package com.uni.timetable.model;

public enum ClassesType {
    LECTURE("Wykład"),
    LABORATORIES("Laboratoria");

    public String description;

    ClassesType(String description) {
        this.description = description;
    }
}
