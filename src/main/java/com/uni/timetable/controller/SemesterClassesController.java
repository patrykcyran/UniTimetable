package com.uni.timetable.controller;

import com.uni.timetable.model.CalendarEvent;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.model.SemesterType;
import com.uni.timetable.service.ClassesService;
import com.uni.timetable.service.SemesterClassesService;
import com.uni.timetable.utils.SemesterClassesToCalendarEventMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/semesterClasses")
public class SemesterClassesController {

    private final SemesterClassesService semesterClassesService;
    private final ClassesService classesService;

    public SemesterClassesController(SemesterClassesService semesterClassesService,
                                     ClassesService classesService) {
        this.semesterClassesService = semesterClassesService;
        this.classesService = classesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterClasses>> findAll() {
        return new ResponseEntity<>(semesterClassesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/all/{lecturerName}/{academicYear}/{semesterType}")
    @ResponseBody
    public List<CalendarEvent> findClassesByLecturerName(@PathVariable String lecturerName,
                                                         @PathVariable String academicYear,
                                                         @PathVariable String semesterType) {
        List<SemesterClasses> classesByLecturer = semesterClassesService.findSemesterClassesByLecturerAndSemester(academicYear, SemesterType.valueOf(semesterType), lecturerName);
        List<CalendarEvent> calendarEvent = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByLecturer);

        return calendarEvent;
    }
}
