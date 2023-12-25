package com.uni.timetable.utils;

import com.uni.timetable.model.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SemesterClassesToCalendarEventMapper {

    public static List<CalendarEvent> mapClassesToCalendarEvent(List<SemesterClasses> semesterClassesList, List<ClassesLecturers> classesLecturers) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();
        for (SemesterClasses semesterClasses : semesterClassesList) {
            List<Lecturer> lecturers = classesLecturers.stream()
                    .filter(classesLecturer -> classesLecturer.getClasses().getClassesId().equals(semesterClasses.getClasses().getClassesId())).map(ClassesLecturers::getLecturer).toList();

            Classes classes = semesterClasses.getClasses();
            Semester semester = semesterClasses.getSemester();
            LocalDate semesterStartDate = semester.getStartDate();
            LocalDate semesterEndDate = semester.getEndDate();
            Frequency classesFrequency = semesterClasses.getFrequency();

            //Calculate first day of classes in given semester
            DayOfWeek semesterStartDay = semesterStartDate.getDayOfWeek();
            int daysToAdd = classes.getDayOfWeek().getValue() - semesterStartDay.getValue();
            LocalDate classesDay = semesterStartDate.plusDays(daysToAdd);

            int daysToIncrement = classesFrequency.equals(Frequency.FORTNIGHTLY) ? 14 : 7;
            while (classesDay.isBefore(semesterEndDate)) {
                calendarEvents.add(mapClassToEvent(semesterClasses, classesDay, lecturers));
                classesDay = classesDay.plusDays(daysToIncrement);
            }
        }
        return calendarEvents;
    }

    private static CalendarEvent mapClassToEvent(SemesterClasses semesterClasses, LocalDate classesDay, List<Lecturer> lecturers) {
        LocalDateTime classesStart = semesterClasses.getClasses().getStartTime().atDate(classesDay);
        LocalDateTime classesEnd = semesterClasses.getClasses().getEndTime().atDate(classesDay);

        Classes classes = semesterClasses.getClasses();
        StringBuilder title = new StringBuilder(semesterClasses.getClasses().getSubject().getSubjectName() + "\n");

        for (Lecturer lecturer : lecturers) {
            title.append(lecturer.getAcademicTitle()).append(" ").append(lecturer.getName()).append("\n");
        }
        title.append(classes.getDepartmentClassroom().getClassroom().getClassroomName());

        return CalendarEvent.builder()
                .eventId(semesterClasses.getSemesterClassesId())
                .studyType(StudyType.FULL_TIME)
                .title(title.toString())
                .start(classesStart)
                .end(classesEnd)
                .description(mapClassToEventDescription(semesterClasses.getClasses(), List.of()))
                .color(resolveEventColor(semesterClasses.getClasses().getClassesType()))
                .build();
    }

    private static String resolveEventColor(ClassesType classesType) {
        switch (classesType) {
            case LECTURE -> {
                return "#2596be";
            }
            case LABORATORIES -> {
                return "#8231F6";
            }
            case PC_LABORATORIES -> {
                return "#31ABF6";
            }
            case PROJECTS -> {
                return "#D5F631";
            }
            case SEMINAR -> {
                return "#F69C31";
            }
            default -> {
                return "#31A8F6";
            }
        }
    }

    private static CalendarEvent mapPartTimeClassToEvent(PartTimeSemesterClasses partTimeSemesterClasses, List<Lecturer> lecturers) {
        LocalDate classesDay = partTimeSemesterClasses.getClassesDate();
        LocalDateTime classesStart = partTimeSemesterClasses.getClasses().getStartTime().atDate(classesDay);
        LocalDateTime classesEnd = partTimeSemesterClasses.getClasses().getEndTime().atDate(classesDay);

        Classes classes = partTimeSemesterClasses.getClasses();
        StringBuilder title = new StringBuilder(partTimeSemesterClasses.getClasses().getSubject().getSubjectName() + "\n");

        for (Lecturer lecturer : lecturers) {
            title.append(lecturer.getAcademicTitle()).append(" ").append(lecturer.getName()).append("\n");
        }
                title.append(classes.getDepartmentClassroom().getClassroom().getClassroomName());
        return CalendarEvent.builder()
                .eventId(partTimeSemesterClasses.getPartTimeSemesterClassesId())
                .studyType(StudyType.PART_TIME)
                .title(title.toString())
                .start(classesStart)
                .end(classesEnd)
                .description(mapClassToEventDescription(partTimeSemesterClasses.getClasses(), lecturers))
                .build();
    }

    private static CalendarEventDescription mapClassToEventDescription(Classes classes, List<Lecturer> lecturers) {
        String group = classes.getMajorGroup().getGroup().getGroupName();
        String major = classes.getMajorGroup().getMajor().getMajorName();
        String type = classes.getClassesType().description;
        StringBuilder lecturerNames = new StringBuilder();
        for (Lecturer lecturer : lecturers) {
            lecturerNames.append(lecturer.getAcademicTitle()).append(" ").append(lecturer.getName()).append("\n");
        }
        String department = classes.getDepartmentClassroom().getDepartment().getDepartmentName();
        String classroom = classes.getDepartmentClassroom().getClassroom().getClassroomName();

        String descriptionText = "Grupa = " + group +
                "\nKierunek = " + major +
                "\nTyp = " + type +
                "\nProwadzący = " + lecturerNames +
                "\nWydział = " + department +
                "\nSala = " + classroom;
        return CalendarEventDescription.builder()
                .group(group)
                .major(major)
                .type(type)
                .lecturerName(lecturerNames.toString())
                .department(department)
                .classroom(classroom)
                .descriptionText(descriptionText)
                .build();
    }

    public static List<CalendarEvent> mapPartTimeClassesToCalendarEvent(List<PartTimeSemesterClasses> partTimeSemesterClasses, List<ClassesLecturers> classesLecturers) {
        List<CalendarEvent> calendarEvents = new ArrayList<>();

        for (PartTimeSemesterClasses semesterClasses : partTimeSemesterClasses) {
            List<Lecturer> lecturers = classesLecturers.stream()
                            .filter(classesLecturer -> classesLecturer.getClasses().getClassesId().equals(semesterClasses.getClasses().getClassesId())).map(ClassesLecturers::getLecturer).toList();
            calendarEvents.add(mapPartTimeClassToEvent(semesterClasses, lecturers));
        }

        return calendarEvents;
    }
}
