package com.uni.timetable.utils;

import com.uni.timetable.model.CalendarEvent;
import com.uni.timetable.model.CalendarEventDescription;
import com.uni.timetable.model.LecturerNonAvailable;
import com.uni.timetable.model.OneTimeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OneTimeEventToCalendarEventMapper {

    public static List<CalendarEvent> mapOneTimeEventsToCalendarEvents(List<OneTimeEvent> oneTimeEventList) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        for (OneTimeEvent oneTimeEvent : oneTimeEventList) {
            calendarEvents.add(mapOneTimeEventToCalendarevent(oneTimeEvent));
        }
        return calendarEvents;
    }

    private static CalendarEvent mapOneTimeEventToCalendarevent(OneTimeEvent oneTimeEvent) {
        LocalDate eventDay = oneTimeEvent.getEventDate();
        LocalDateTime eventStart = oneTimeEvent.getStartTime().atDate(eventDay);
        LocalDateTime eventEnd = oneTimeEvent.getEndTime().atDate(eventDay);

        return CalendarEvent.builder()
                .eventId(oneTimeEvent.getOneTimeEventId())
                .title("Rezerwacja sali")
                .start(eventStart)
                .end(eventEnd)
                .description(CalendarEventDescription.builder().descriptionText("Rezerwacja").build())
                .color("#808080")
                .build();
    }

    public static List<CalendarEvent> mapLecturersNonAvailableToCalendarEvents(List<LecturerNonAvailable> lecturerNonAvailableList) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        for (LecturerNonAvailable lecturerNonAvailable : lecturerNonAvailableList) {
            calendarEvents.add(mapOLecturerNonAvailableToCalendarevent(lecturerNonAvailable));
        }
        return calendarEvents;
    }

    private static CalendarEvent mapOLecturerNonAvailableToCalendarevent(LecturerNonAvailable lecturerNonAvailable) {
        LocalDate eventDay = lecturerNonAvailable.getEventDate();
        LocalDateTime eventStart = lecturerNonAvailable.getStartTime().atDate(eventDay);
        LocalDateTime eventEnd = lecturerNonAvailable.getEndTime().atDate(eventDay);

        return CalendarEvent.builder()
                .eventId(lecturerNonAvailable.getOneTimeEventId())
                .title("Niedostępny")
                .start(eventStart)
                .end(eventEnd)
                .description(CalendarEventDescription.builder().descriptionText("Niedostępny").build())
                .color("#808080")
                .build();
    }
}
