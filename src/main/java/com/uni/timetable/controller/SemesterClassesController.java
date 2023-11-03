package com.uni.timetable.controller;

import com.uni.timetable.model.CalendarClassesDto;
import com.uni.timetable.model.Classes;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.model.SemesterType;
import com.uni.timetable.service.ClassesService;
import com.uni.timetable.service.SemesterClassesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
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
    public ResponseEntity<List<CalendarClassesDto>> findClassesByLecturerName(String lecturerName) {
        List<CalendarClassesDto> calendarClassesDto = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        CalendarClassesDto dto1 = new CalendarClassesDto(now, now.plusHours(3), "T1");
        CalendarClassesDto dto2 = new CalendarClassesDto(now.plusDays(1), now.plusHours(3), "T2");
        CalendarClassesDto dto3 = new CalendarClassesDto(now.plusDays(2), now.plusHours(3), "T3");
        CalendarClassesDto dto4 = new CalendarClassesDto(now.plusDays(3), now.plusHours(3), "T4");
        CalendarClassesDto dto5 = new CalendarClassesDto(now.plusDays(4), now.plusHours(3), "T5");
        CalendarClassesDto dto6 = new CalendarClassesDto(now.plusDays(5), now.plusHours(3), "T6");
        CalendarClassesDto dto7 = new CalendarClassesDto(now.plusDays(6), now.plusHours(3), "T7");
        calendarClassesDto.add(dto1);
        calendarClassesDto.add(dto2);
        calendarClassesDto.add(dto3);
        calendarClassesDto.add(dto4);

        if (nonNull(lecturerName) && lecturerName.equals("Patryk Cyran")) {
            calendarClassesDto.add(dto5);
            calendarClassesDto.add(dto6);
            calendarClassesDto.add(dto7);
        }
        return new ResponseEntity<>(calendarClassesDto, HttpStatus.OK);
    }
}
