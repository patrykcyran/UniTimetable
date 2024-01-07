package com.uni.timetable.utils;

import com.uni.timetable.model.CalendarEvent;

import java.util.List;

public interface CalendarEventFactory {
    List<CalendarEvent> createEventList();
}
