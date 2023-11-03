package com.uni.timetable.controller;

import com.uni.timetable.model.CalendarClassesDto;
import com.uni.timetable.model.Lecturer;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.service.LecturerService;
import com.uni.timetable.service.SemesterClassesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/lecturer")
public class LecturerController {
    private final LecturerService lecturerService;

    public LecturerController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Lecturer>> findAll() {
        return new ResponseEntity<>(lecturerService.findAll(), HttpStatus.OK);
    }
}
