package com.uni.timetable.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class CalendarEvent {
    private Long eventId;
    private StudyType studyType;
    private LocalDateTime start;
    private LocalDateTime end;
    private String title;
    private CalendarEventDescription description;
    private String color;

    public CalendarEvent(CalendarEvent other) {
        this.eventId = other.eventId;
        this.studyType = other.studyType;
        this.start = other.start;
        this.end = other.end;
        this.title = other.title;
        this.description = new CalendarEventDescription(other.description); // Assuming CalendarEventDescription has a copy constructor
        this.color = other.color;
    }
}
