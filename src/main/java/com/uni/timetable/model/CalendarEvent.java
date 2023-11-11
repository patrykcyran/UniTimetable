package com.uni.timetable.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CalendarEvent {
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private CalendarEventDescription description;
    private String color;
}
