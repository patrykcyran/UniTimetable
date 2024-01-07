package com.uni.timetable.utils;

import com.uni.timetable.model.CalendarEvent;
import com.uni.timetable.model.CalendarEventDescription;
import com.uni.timetable.model.LecturerNonAvailable;
import com.uni.timetable.model.OneTimeEvent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class LecturerNonAvailableEventFactory implements CalendarEventFactory {

    List<LecturerNonAvailable> lecturerNonAvailableList;

    public LecturerNonAvailableEventFactory(List<LecturerNonAvailable> lecturerNonAvailableList) {
        this.lecturerNonAvailableList = lecturerNonAvailableList;
    }

    @Override
    public List<CalendarEvent> createEventList() {
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
