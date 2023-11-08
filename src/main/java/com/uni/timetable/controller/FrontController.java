package com.uni.timetable.controller;

import com.uni.timetable.model.SemesterType;
import com.uni.timetable.service.LecturerService;
import com.uni.timetable.service.MajorService;
import com.uni.timetable.service.SemesterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class FrontController {
    LecturerService lecturerService;
    SemesterService semesterService;
    MajorService majorService;

    public FrontController(LecturerService lecturerService,
                           SemesterService semesterService,
                           MajorService majorService) {
        this.lecturerService = lecturerService;
        this.semesterService = semesterService;
        this.majorService = majorService;
    }

    @GetMapping()
    public String general(Model model) {
        return "index";
    }

    @GetMapping("/full-time-students")
    public String students(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                           @RequestParam(required = false, defaultValue = "4") String studyYear,
                           @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                           Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllFullTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1,2,3,4,5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        return "full-time-students";
    }

    @GetMapping("/part-time-students")
    public String partTimeStudents(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                           @RequestParam(required = false, defaultValue = "4") String studyYear,
                           @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                           Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllPartTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1,2,3,4,5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        return "part-time-students";
    }

    @GetMapping("/classrooms")
    public String classrooms(Model model) {
        return "classrooms";
    }

    @GetMapping("/lecturers")
    public String selectedLecturerClasses(@RequestParam(required = false, defaultValue = "Krzysztof Czajkowski") String lecturerName,
                                          @RequestParam(required = false, defaultValue = "23-24") String academicYear,
                                          @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                                          Model model) {
        model.addAttribute("prevLecturer", lecturerName);
        model.addAttribute("prevAcademicYear", academicYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        return "lecturers";
    }
}
