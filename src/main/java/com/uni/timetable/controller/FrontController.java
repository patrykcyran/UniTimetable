package com.uni.timetable.controller;

import com.uni.timetable.service.LecturerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class FrontController {
    LecturerService lecturerService;

    public FrontController(LecturerService lecturerService) {
        this.lecturerService = lecturerService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String general(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "index";
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public String students(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "students";
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
    public String selectedLecturerClasses(@RequestParam(required = false) String lecturerName, Model model) {
        if (lecturerName == null || lecturerName.isEmpty()) {
            lecturerName = "Patryk Cyran"; // Set the default value
        }
        model.addAttribute("prevLecturer", lecturerName);
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        return "lecturers";
    }
}
