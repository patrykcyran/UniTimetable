package com.uni.timetable.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CalendarEvent {
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
}
