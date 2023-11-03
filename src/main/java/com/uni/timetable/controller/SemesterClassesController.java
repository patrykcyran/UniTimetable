package com.uni.timetable.controller;

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

import java.util.List;

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
}
