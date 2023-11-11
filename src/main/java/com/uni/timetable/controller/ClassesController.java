package com.uni.timetable.controller;

import com.uni.timetable.model.Classes;
import com.uni.timetable.service.ClassesService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Classes>> findByGroup(@RequestParam("groupName") String groupName, @RequestParam("semesterNumber") String semesterNumber) {
       return new ResponseEntity<>(classesService.findByGroupAndSemesterNumber(groupName, semesterNumber), HttpStatus.OK);
    }

    @PostMapping("/byLecturerId")
    public ResponseEntity<List<Classes>> findByLecturer(@RequestParam("lecturerId") Long lecturerId) {
        return new ResponseEntity<>(classesService.findByLecturerId(lecturerId), HttpStatus.OK);
    }

    @PostMapping("/byClassroom")
    public ResponseEntity<List<Classes>> findByClassroom(@RequestParam("classroom") String classroomName, @RequestParam("department") String departmentName) {
        return new ResponseEntity<>(classesService.findByClassroom(classroomName, departmentName), HttpStatus.OK);
    }
}
