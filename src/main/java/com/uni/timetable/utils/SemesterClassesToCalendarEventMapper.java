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
        String group = classes.getMajorGroup().getGroup().getGroupName();
        String major = classes.getMajorGroup().getMajor().getMajorName();
        String type = classes.getClassesType().description;
        String lecturerName = classes.getLecturerId().getName();
        String department = classes.getDepartmentClassroom().getDepartment().getDepartmentName();
        String classroom = classes.getDepartmentClassroom().getClassroom().getClassroomName();

        String descriptionText = "Grupa = " + group +
                "\nKierunek = " + major +
                "\nTyp = " + type +
                "\nProwadzący = " + lecturerName +
                "\nWydział = " + department +
                "\nSala = " + classroom;
        return CalendarEventDescription.builder()
                .group(group)
                .major(major)
                .type(type)
                .lecturerName(lecturerName)
                .department(department)
                .classroom(classroom)
                .descriptionText(descriptionText)
                .build();
    }
}