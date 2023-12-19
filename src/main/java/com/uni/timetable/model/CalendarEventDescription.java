package com.uni.timetable.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CalendarEventDescription {
    String group;
    String major;
    String type;
    String lecturerName;
    String department;
    String classroom;
    String descriptionText;

    public CalendarEventDescription(CalendarEventDescription other) {
        this.group = other.group;
        this.major = other.major;
        this.type = other.type;
        this.lecturerName = other.lecturerName;
        this.department = other.department;
        this.classroom = other.classroom;
        this.descriptionText = other.descriptionText;
    }
}
