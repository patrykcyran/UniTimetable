package com.uni.timetable.controller;

import com.uni.timetable.exception.TimetableException;
import com.uni.timetable.model.DepartmentClassroom;
import com.uni.timetable.model.Lecturer;
import com.uni.timetable.service.LecturerNonAvailableService;
import com.uni.timetable.service.LecturerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("lecturer-non-available")
public class LecturerNonAvailableController {

    LecturerNonAvailableService lecturerNonAvailableService;
    LecturerService lecturerService;

    public LecturerNonAvailableController(LecturerNonAvailableService lecturerNonAvailableService,
                                          LecturerService lecturerService) {
        this.lecturerNonAvailableService = lecturerNonAvailableService;
        this.lecturerService = lecturerService;
    }

    public void saveLecturerNonAvailable(String eventDateString,
                                         String startTimeString,
                                         String endTimeString,
                                         String lecturerName) {
        LocalDate eventDate = LocalDate.parse(eventDateString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia wydarzenia nie może być po ich zakończeniu");
        }

        Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);

        lecturerNonAvailableService.saveLecturerNonAvailable(eventDate, startTime, endTime, lecturer);
    }

    @DeleteMapping("/delete/{eventId}")
    public String deleteEvent(@PathVariable Long eventId) {
        lecturerNonAvailableService.deleteLecturerNonAvailableById(eventId);
        return "lecturers";
    }
}
