package com.uni.timetable.model;

public enum ClassesType {
    LECTURE("Wyklad"),
    LABORATORIES("Laboratoria"),
    PC_LABORATORIES("Laboratoria Komputerowe"),
    PROJECTS("Projekty"),
    SEMINAR("Seminarium");

    public String description;

    ClassesType(String description) {
        this.description = description;
    }

    public static ClassesType fromDescription(String description) {
        for (ClassesType type : ClassesType.values()) {
            if (type.description.equalsIgnoreCase(description)) {
                return type;
            }
        }
        // Rzucić wyjątek lub zwrócić domyślną wartość, jeśli nie znaleziono pasującej instancji
        throw new IllegalArgumentException("No matching ClassesType for description: " + description);
    }
}
