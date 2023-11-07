package com.uni.timetable.controller;

import com.uni.timetable.model.SemesterType;
import com.uni.timetable.service.LecturerService;
import com.uni.timetable.service.MajorService;
import com.uni.timetable.service.SemesterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import static java.util.Objects.isNull;

@Controller
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

    @RequestMapping(method = RequestMethod.GET)
    public String general(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "index";
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String students(@RequestParam(required = false) String majorName,
                           @RequestParam(required = false) String academicYear,
                           @RequestParam(required = false) String semesterType,
                           Model model) {
        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevAcademicYear", academicYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllFullTimeMajorNames());
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("SemesterTypes", SemesterType.values());
        return "students";
    }

    @RequestMapping(value = "/part-time-students", method = RequestMethod.GET)
    public String partTimeStudents(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "part-time-students";
    }

/*    @RequestMapping(value = "/lecturers", method = RequestMethod.GET)
    public String lecturers(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "lecturers";
    }*/

    @RequestMapping(value = "/classrooms", method = RequestMethod.GET)
    public String classrooms(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "classrooms";
    }

    @GetMapping("/lecturers")
    public String selectedLecturerClasses(@RequestParam(required = false) String lecturerName,
                                          @RequestParam(required = false) String academicYear,
                                          @RequestParam(required = false) String semesterType,
                                          Model model) {
        model.addAttribute("prevLecturer", lecturerName);
        model.addAttribute("prevAcademicYear", academicYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("SemesterTypes", SemesterType.values());
        return "lecturers";
    }
}
