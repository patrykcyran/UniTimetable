package com.uni.timetable.utils;

import com.uni.timetable.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SemesterClassesToCalendarEventMapper {

    public static List<CalendarEvent> mapClassesToCalendarEvent(List<SemesterClasses> semesterClassesList) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        for (SemesterClasses semesterClasses : semesterClassesList) {
            Classes classes = semesterClasses.getClasses();
            Semester semester = semesterClasses.getSemester();
            LocalDate semesterStartDate = semester.getStartDate();
            LocalDate semesterEndDate = semester.getEndDate();
            Frequency classesFrequency = semesterClasses.getFrequency();

            //Calculate first day of classes in given semester
            DayOfWeek semesterStartDay = semesterStartDate.getDayOfWeek();
            int daysToAdd = classes.getDayOfWeek().getValue() - semesterStartDay.getValue();
            LocalDate classesDay = semesterStartDate.plusDays(daysToAdd);

            int daysToIncrement = classesFrequency.equals(Frequency.WEEKLY) ? 7 : 14;
            while (classesDay.isBefore(semesterEndDate)) {
                calendarEvents.add(mapClassToEvent(semesterClasses, classesDay));
                classesDay = classesDay.plusDays(daysToIncrement);
            }
        }
        return calendarEvents;
    }

    private static CalendarEvent mapClassToEvent(SemesterClasses semesterClasses, LocalDate classesDay) {
        LocalDateTime classesStart = semesterClasses.getClasses().getStartTime().atDate(classesDay);
        LocalDateTime classesEnd = semesterClasses.getClasses().getEndTime().atDate(classesDay);
        return CalendarEvent.builder()
                .title(semesterClasses.getClasses().getSubject().getSubjectName())
                .start(classesStart)
                .end(classesEnd)
                .description(mapClassToEventDescription(semesterClasses.getClasses()))
                .build();
    }

    private static CalendarEventDescription mapClassToEventDescription(Classes classes) {
        return CalendarEventDescription.builder()
                .group(classes.getMajorGroup().getGroup().getGroupName())
                .major(classes.getMajorGroup().getMajor().getMajorName())
                .type(classes.getClassesType().name())
                .lecturerName(classes.getLecturerId().getName())
                .department(classes.getDepartmentClassroom().getDepartment().getDepartmentName())
                .classroom(classes.getDepartmentClassroom().getClassroom().getClassroomName())
                .build();
    }
}
