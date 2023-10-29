package com.uni.timetable.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FrontController {

    @RequestMapping(method = RequestMethod.GET)
    public String students(Model model) {
        // Tutaj możesz umieścić kod do pobierania planu zajęć z bazy danych
        System.out.println("FDS");
        return "index";
    }
}
