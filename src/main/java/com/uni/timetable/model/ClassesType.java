package com.uni.timetable.model;

public enum ClassesType {
    LECTURE("Wykład"),
    LABORATORIES("Laboratoria"),
    PC_LABORATORIES("Laboratoria Komputerowe"),
    PROJECTS("Projekty"),
    SEMINAR("Seminarium");

    public String description;

    ClassesType(String description) {
        this.description = description;
    }
}
