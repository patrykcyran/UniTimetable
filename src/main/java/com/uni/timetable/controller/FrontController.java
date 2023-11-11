package com.uni.timetable.controller;

import com.uni.timetable.model.SemesterType;
import com.uni.timetable.security.SecurityUtils;
import com.uni.timetable.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
@Slf4j
public class FrontController {
    LecturerService lecturerService;
    SemesterService semesterService;
    MajorService majorService;
    DepartmentService departmentService;
    DepartmentClassroomService departmentClassroomService;

    public FrontController(LecturerService lecturerService,
                           SemesterService semesterService,
                           MajorService majorService,
                           DepartmentService departmentService,
                           DepartmentClassroomService departmentClassroomService) {
        this.lecturerService = lecturerService;
        this.semesterService = semesterService;
        this.majorService = majorService;
        this.departmentService = departmentService;
        this.departmentClassroomService = departmentClassroomService;
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
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "full-time-students";
    }

    @GetMapping("/part-time-students")
    public String partTimeStudents(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                                   @RequestParam(required = false, defaultValue = "3") String studyYear,
                                   @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                                   Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllPartTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "part-time-students";
    }

    @GetMapping("/classrooms")
    public String classrooms(@RequestParam(required = false, defaultValue = "Wydział Inżynierii Elektrycznej i Komputerowej") String departmentName,
                             @RequestParam(required = false, defaultValue = "A1") String classroomName,
                             Model model) {

        model.addAttribute("prevDepartment", departmentName);
        model.addAttribute("prevClassroom", classroomName);
        model.addAttribute("DepartmentNames", departmentService.findAllDepartmentNames());
        model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(departmentName));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
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
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "lecturers";
    }

    @GetMapping("/admin")
    public String adminView(Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "admin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        SecurityUtils.verifyAdminLogin("admin", "admin");
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "admin";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        SecurityUtils.logOut();
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "admin";
    }
}
