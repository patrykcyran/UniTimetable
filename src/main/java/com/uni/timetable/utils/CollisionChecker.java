package com.uni.timetable.utils;

import com.uni.timetable.model.CalendarEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class CollisionChecker {

    public static boolean checkIfEventsCollideByDate(LocalDate eventDate, LocalTime startTime, LocalTime endTime, List<CalendarEvent> existingCalendarEvents) {
        LocalDateTime startDateTime = LocalDateTime.of(eventDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(eventDate, endTime);
        Optional<CalendarEvent> optionalCollidingEvent = existingCalendarEvents.stream().filter(event -> (event.getStart().isBefore(startDateTime) && event.getEnd().isAfter(startDateTime)) || (event.getStart().isBefore(endDateTime) && event.getEnd().isAfter(endDateTime))).findAny();
        return optionalCollidingEvent.isPresent();
    }

    //public static boolean checkIfEventsCollide
}
