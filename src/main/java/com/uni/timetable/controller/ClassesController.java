package com.uni.timetable.controller;

import com.uni.timetable.model.Classes;
import com.uni.timetable.service.ClassesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/classes")
public class ClassesController {

    private final ClassesService classesService;

    public ClassesController(ClassesService classesService) {
        this.classesService = classesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Classes>> findAll() {
        return new ResponseEntity<>(classesService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/byGroupAndSemester")
    public ResponseEntity<List<Classes>> findByGroup(@RequestParam("semesterNumber") String semesterNumber, @RequestParam("groupName") String groupName) {
        List<Classes> allClasses = classesService.findAll();

       return new ResponseEntity<>(classesService.findAll(), HttpStatus.OK);
    }
}
