package com.uni.timetable.controller;

import com.uni.timetable.exception.TimetableException;
import com.uni.timetable.model.CalendarEvent;
import com.uni.timetable.model.DepartmentClassroom;
import com.uni.timetable.model.PartTimeSemesterClasses;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.service.DepartmentClassroomService;
import com.uni.timetable.service.OneTimeEventService;
import com.uni.timetable.service.PartTimeSemesterClassesService;
import com.uni.timetable.service.SemesterClassesService;
import com.uni.timetable.utils.SemesterClassesToCalendarEventMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/one-time-event")
public class OneTimeEventController {

    private final OneTimeEventService oneTimeEventService;
    private final DepartmentClassroomService departmentClassroomService;
    private final SemesterClassesService semesterClassesService;
    private final PartTimeSemesterClassesService partTimeSemesterClassesService;

    public OneTimeEventController(OneTimeEventService oneTimeEventService,
                                  DepartmentClassroomService departmentClassroomService,
                                  SemesterClassesService semesterClassesService,
                                  PartTimeSemesterClassesService partTimeSemesterClassesService) {
        this.oneTimeEventService = oneTimeEventService;
        this.departmentClassroomService = departmentClassroomService;
        this.semesterClassesService = semesterClassesService;
        this.partTimeSemesterClassesService = partTimeSemesterClassesService;
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

        if (checkIfEventCollide(eventDate, startTime, endTime)) {
            throw new TimetableException("Wydarzenie koliduje z aktualnym planem zajec");
        }

        oneTimeEventService.saveOneTimeEvent(eventDate, startTime, endTime, departmentClassroom);
    }

    @GetMapping("/check-event-collision")
    @ResponseBody
    public boolean checkForEventCollision(@RequestParam("classesDate") String eventDateString,
                                          @RequestParam("startTime") String startTimeString,
                                          @RequestParam("endTime") String endTimeString) {
        LocalDate eventDate = LocalDate.parse(eventDateString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);
        return checkIfEventCollide(eventDate, startTime, endTime);
    }

    private boolean checkIfEventCollide(LocalDate eventDate, LocalTime startTime, LocalTime endTime) {
        List<SemesterClasses> allSemesterClasses = semesterClassesService.findAll();
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesService.findAll();
        List<CalendarEvent> allEvents = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(allSemesterClasses, List.of());
        allEvents.addAll(SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(partTimeSemesterClasses, List.of()));

        LocalDateTime startDateTime = LocalDateTime.of(eventDate, startTime);
        LocalDateTime endDateTime = LocalDateTime.of(eventDate, endTime);
        Optional<CalendarEvent> optionalCollidingEvent = allEvents.stream().filter(event -> event.getStart().isBefore(startDateTime) && event.getEnd().isAfter(startDateTime)).findAny();
        return optionalCollidingEvent.isPresent();
    }
}
