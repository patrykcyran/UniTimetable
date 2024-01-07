package com.uni.timetable.utils;

import com.uni.timetable.model.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.uni.timetable.utils.SemesterClassesEventFactory.mapClassToEventDescription;

public class PartTimeSemesterClassesEventFactory implements CalendarEventFactory {

    private List<PartTimeSemesterClasses> partTimeSemesterClasses;
    private List<ClassesLecturers> classesLecturers;

    public PartTimeSemesterClassesEventFactory(List<PartTimeSemesterClasses> partTimeSemesterClasses, List<ClassesLecturers> classesLecturers) {
        this.partTimeSemesterClasses = partTimeSemesterClasses;
        this.classesLecturers = classesLecturers;
    }

    @Override
    public List<CalendarEvent> createEventList() {
        List<CalendarEvent> calendarEvents = new ArrayList<>();

        for (PartTimeSemesterClasses semesterClasses : partTimeSemesterClasses) {
            List<Lecturer> lecturers = classesLecturers.stream()
                    .filter(classesLecturer -> classesLecturer.getClasses().getClassesId().equals(semesterClasses.getClasses().getClassesId())).map(ClassesLecturers::getLecturer).toList();
            calendarEvents.add(mapPartTimeClassToEvent(semesterClasses, lecturers));
        }

        return calendarEvents;
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
}
