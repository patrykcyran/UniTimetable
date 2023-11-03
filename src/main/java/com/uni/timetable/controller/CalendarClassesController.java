package com.uni.timetable.controller;

import com.uni.timetable.model.CalendarClassesDto;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.service.SemesterClassesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/calendarClasses")
public class CalendarClassesController {
    private final SemesterClassesService semesterClassesService;

    public CalendarClassesController(SemesterClassesService semesterClassesService) {
        this.semesterClassesService = semesterClassesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CalendarClassesDto>> findAll() {
        List<SemesterClasses> semesterClasses = semesterClassesService.findAll();
        List<CalendarClassesDto> calendarClassesDto = new ArrayList<>();
        calendarClassesDto.add(new CalendarClassesDto(LocalDateTime.now(), LocalDateTime.now().plusHours(3), "Test"));
        return new ResponseEntity<>(calendarClassesDto, HttpStatus.OK);
    }
}
