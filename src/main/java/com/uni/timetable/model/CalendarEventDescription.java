package com.uni.timetable.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CalendarEventDescription {
    String group;
    String major;
    String type;
    String lecturerName;
    String department;
    String classroom;
}
