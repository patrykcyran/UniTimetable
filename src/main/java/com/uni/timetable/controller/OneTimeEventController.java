package com.uni.timetable.controller;

import com.uni.timetable.exception.TimetableException;
import com.uni.timetable.model.DepartmentClassroom;
import com.uni.timetable.service.DepartmentClassroomService;
import com.uni.timetable.service.OneTimeEventService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/one-time-event")
public class OneTimeEventController {

    private final OneTimeEventService oneTimeEventService;
    private final DepartmentClassroomService departmentClassroomService;

    public OneTimeEventController(OneTimeEventService oneTimeEventService,
                                  DepartmentClassroomService departmentClassroomService) {
        this.oneTimeEventService = oneTimeEventService;
        this.departmentClassroomService = departmentClassroomService;
    }

    public void saveOneTimeEvent(String eventDateString,
                                   String startTimeString,
                                   String endTimeString,
                                   String department,
                                   String classroom) {
        LocalDate eventDate = LocalDate.parse(eventDateString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia wydarzenia nie może być po ich zakończeniu");
        }

        DepartmentClassroom departmentClassroom = departmentClassroomService.findByDepartmentAndClassroomName(department, classroom);

        oneTimeEventService.saveOneTimeEvent(eventDate, startTime, endTime, departmentClassroom);
    }
}
