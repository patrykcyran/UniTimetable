package com.uni.timetable.controller;

import com.uni.timetable.exception.TimetableException;
import com.uni.timetable.model.*;
import com.uni.timetable.service.ClassesService;
import com.uni.timetable.service.DepartmentClassroomService;
import com.uni.timetable.service.MajorGroupService;
import com.uni.timetable.service.SubjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/classes")
public class ClassesController {

    private final ClassesService classesService;
    private final MajorGroupService majorGroupService;
    private final DepartmentClassroomService departmentClassroomService;
    private final SubjectService subjectService;

    public ClassesController(ClassesService classesService,
                             MajorGroupService majorGroupService,
                             DepartmentClassroomService departmentClassroomService,
                             SubjectService subjectService) {
        this.classesService = classesService;
        this.majorGroupService = majorGroupService;
        this.departmentClassroomService = departmentClassroomService;
        this.subjectService = subjectService;
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

    public void saveClasses(String classesTypeString,
                            String dayOfWeekString,
                            String startTimeString,
                            String endTimeString,
                            String department,
                            String classroom,
                            String major,
                            String studyYear,
                            String group,
                            String subjectName) {

        List<MajorGroup> majorGroups = new ArrayList<>();
        if ("Cały kierunek".equals(group)) {
            majorGroups.addAll(majorGroupService.findMajorGroupsByMajorAndYear(major, studyYear));
        } else {
            MajorGroup majorGroup = majorGroupService.findByMajorGroupAndYear(major, studyYear, group);
            majorGroups.add(majorGroup);
        }

        Subject subject = subjectService.findSubjectByName(subjectName);
        DayOfWeek dayOfWeek = resolveDayOfWeek(dayOfWeekString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);
        ClassesType classesType = ClassesType.fromDescription(classesTypeString);
        DepartmentClassroom departmentClassroom = departmentClassroomService.findByDepartmentAndClassroomName(department, classroom);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia zajęć nie może być po ich zakończeiu");
        }

        //When whole major is chosen, iterate over each group and add classes to them
        for (MajorGroup majorGroup : majorGroups) {
            classesService.saveClasses(majorGroup, subject, dayOfWeek, startTime, endTime, classesType, departmentClassroom);
        }
    }

    private static DayOfWeek resolveDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Poniedziałek":
                return DayOfWeek.MONDAY;
            case "Wtorek":
                return DayOfWeek.TUESDAY;
            case "Środa":
                return DayOfWeek.WEDNESDAY;
            case "Czwartek":
                return DayOfWeek.THURSDAY;
            case "Piątek":
                return DayOfWeek.FRIDAY;
            case "Sobota":
                return DayOfWeek.SATURDAY;
            case "Niedziela":
                return DayOfWeek.SUNDAY;
            default:
                throw new ClassCastException("Cannot cast given string to DayOfWeek class: " + dayOfWeek);
        }
    }
}
