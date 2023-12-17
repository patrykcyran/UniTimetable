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
}
