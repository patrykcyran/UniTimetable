package com.uni.timetable.controller;

import com.uni.timetable.model.CalendarEvent;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.service.SemesterClassesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;

@Controller
@RequestMapping("/semesterClasses")
public class SemesterClassesController {

    private final SemesterClassesService semesterClassesService;

    public SemesterClassesController(SemesterClassesService semesterClassesService) {
        this.semesterClassesService = semesterClassesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterClasses>> findAll() {
        return new ResponseEntity<>(semesterClassesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/all/{lecturerName}")
    @ResponseBody
    public List<CalendarEvent> findClassesByLecturerName(@PathVariable String lecturerName) {
        List<CalendarEvent> calendarEvent = new ArrayList<>();
        LocalDateTime now = LocalDateTime.of(2023, Month.OCTOBER, 30, 12, 20, 00);
        CalendarEvent dto1 = new CalendarEvent(now, now.plusHours(3), "T1");
        CalendarEvent dto2 = new CalendarEvent(now.plusDays(1), now.plusDays(1).plusHours(3), "T2");
        CalendarEvent dto3 = new CalendarEvent(now.plusDays(3), now.plusDays(3).plusHours(3), "T3");
        CalendarEvent dto4 = new CalendarEvent(now.plusDays(5), now.plusDays(5).plusHours(3), "T4");
        CalendarEvent dto5 = new CalendarEvent(now.plusDays(7), now.plusDays(7).plusHours(3), "T5");
        CalendarEvent dto6 = new CalendarEvent(now.plusDays(9), now.plusDays(9).plusHours(3), "T6");
        CalendarEvent dto7 = new CalendarEvent(now.plusDays(11), now.plusDays(11).plusHours(3), "T7");
        calendarEvent.add(dto1);
        calendarEvent.add(dto2);
        calendarEvent.add(dto3);
        calendarEvent.add(dto4);

        if (nonNull(lecturerName) && lecturerName.equals("Patryk Cyran")) {
            calendarEvent.add(dto5);
            calendarEvent.add(dto6);
            calendarEvent.add(dto7);
        }
        return calendarEvent;
    }
}
