package com.uni.timetable.controller;

import com.uni.timetable.model.*;
import com.uni.timetable.service.ClassesLecturersService;
import com.uni.timetable.service.ClassesService;
import com.uni.timetable.service.PartTimeSemesterClassesService;
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
    private final PartTimeSemesterClassesService partTimeSemesterClassesService;
    private final ClassesLecturersService classesLecturersService;
    private final ClassesService classesService;

    public SemesterClassesController(SemesterClassesService semesterClassesService,
                                     PartTimeSemesterClassesService partTimeSemesterClassesService,
                                     ClassesLecturersService classesLecturersService,
                                     ClassesService classesService) {
        this.semesterClassesService = semesterClassesService;
        this.partTimeSemesterClassesService = partTimeSemesterClassesService;
        this.classesLecturersService = classesLecturersService;
        this.classesService = classesService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterClasses>> findAll() {
        return new ResponseEntity<>(semesterClassesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/lecturer/{lecturerName}/{academicYear}/{semesterType}")
    @ResponseBody
    public List<CalendarEvent> findClassesByLecturerName(@PathVariable String lecturerName,
                                                         @PathVariable String academicYear,
                                                         @PathVariable String semesterType) {
        List<SemesterClasses> fullTimeClasses = semesterClassesService.findSemesterClassesBySemester(academicYear, SemesterType.fromDescription(semesterType));
        List<Classes> classesByLecturers = classesLecturersService.findClassesByLecturerName(lecturerName);
        fullTimeClasses = fullTimeClasses.stream()
                .filter(semesterClasses -> classesByLecturers.stream()
                        .anyMatch(classes -> classes.getClassesId().equals(semesterClasses.getSemesterClassesId()))).toList();

        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesService.findPartTimeSemesterClassesBySemester(academicYear, SemesterType.fromDescription(semesterType));
        partTimeSemesterClasses = partTimeSemesterClasses.stream()
                .filter(partTimeClasses -> classesByLecturers.stream()
                        .anyMatch(classes -> classes.getClassesId().equals(partTimeClasses.getPartTimeSemesterClassesId()))).toList();

        List<CalendarEvent> calendarEvents = SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(partTimeSemesterClasses);
        calendarEvents.addAll(SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(fullTimeClasses));
        return calendarEvents;
    }

    @GetMapping("/full-time-major/{majorName}/{academicYear}/{semesterType}")
    @ResponseBody
    public List<CalendarEvent> findClassesByFullTimeMajor(@PathVariable String majorName,
                                                         @PathVariable String academicYear,
                                                         @PathVariable String semesterType) {
        List<SemesterClasses> classesByFullTimeMajor = semesterClassesService.findSemesterClassesByFullTimeMajorAndSemester(majorName, academicYear, SemesterType.fromDescription(semesterType));

        return SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByFullTimeMajor);
    }

    @GetMapping("/part-time-major/{majorName}/{academicYear}/{semesterType}")
    @ResponseBody
    public List<CalendarEvent> findClassesByPartTimeMajor(@PathVariable String majorName,
                                                          @PathVariable String academicYear,
                                                          @PathVariable String semesterType) {
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesService.findPartTimeSemesterClassesByMajorAndSemester(majorName, academicYear, SemesterType.fromDescription(semesterType));
        return SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(partTimeSemesterClasses);
    }

    @GetMapping("/department-classroom/{departmentName}/{classroomName}")
    @ResponseBody
    public List<CalendarEvent> findClassesByDepartmentAndClassroom(@PathVariable String departmentName,
                                                                   @PathVariable String classroomName) {
        List<SemesterClasses> classesByDepartmentAndClassroom = semesterClassesService.findSemesterClassesByDepartmentAndClassroom(departmentName, classroomName);

        return SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByDepartmentAndClassroom);
    }
}
