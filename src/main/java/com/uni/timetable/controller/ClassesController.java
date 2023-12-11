package com.uni.timetable.controller;

import com.uni.timetable.exception.TimetableException;
import com.uni.timetable.model.*;
import com.uni.timetable.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;


@Controller
@RequestMapping("/classes")
public class ClassesController {

    private final ClassesService classesService;
    private final MajorGroupService majorGroupService;
    private final DepartmentClassroomService departmentClassroomService;
    private final SubjectService subjectService;
    private final SemesterClassesService semesterClassesService;
    private final PartTimeSemesterClassesService partTimeSemesterClassesService;
    private final SemesterService semesterService;
    private final ClassesLecturersService classesLecturersService;
    private final LecturerService lecturerService;
    private final DepartmentService departmentService;
    private final MajorService majorService;
    private final GroupService groupService;

    public ClassesController(ClassesService classesService,
                             MajorGroupService majorGroupService,
                             DepartmentClassroomService departmentClassroomService,
                             SubjectService subjectService,
                             SemesterClassesService semesterClassesService,
                             PartTimeSemesterClassesService partTimeSemesterClassesService,
                             SemesterService semesterService,
                             ClassesLecturersService classesLecturersService,
                             LecturerService lecturerService,
                             DepartmentService departmentService,
                             MajorService majorService,
                             GroupService groupService) {
        this.classesService = classesService;
        this.majorGroupService = majorGroupService;
        this.departmentClassroomService = departmentClassroomService;
        this.subjectService = subjectService;
        this.semesterClassesService = semesterClassesService;
        this.partTimeSemesterClassesService = partTimeSemesterClassesService;
        this.semesterService = semesterService;
        this.classesLecturersService = classesLecturersService;
        this.lecturerService = lecturerService;
        this.departmentService = departmentService;
        this.majorService = majorService;
        this.groupService = groupService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<Classes>> findAll() {
        return new ResponseEntity<>(classesService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/byGroupAndSemester")
    public ResponseEntity<List<Classes>> findByGroup(@RequestParam("groupName") String groupName, @RequestParam("semesterNumber") String semesterNumber) {
        return new ResponseEntity<>(classesService.findByGroupAndSemesterNumber(groupName, semesterNumber), HttpStatus.OK);
    }

    @PostMapping("/byLecturerId")
    public ResponseEntity<List<Classes>> findByLecturer(@RequestParam("lecturerId") Long lecturerId) {
        return new ResponseEntity<>(classesService.findByLecturerId(lecturerId), HttpStatus.OK);
    }

    @PostMapping("/byClassroom")
    public ResponseEntity<List<Classes>> findByClassroom(@RequestParam("classroom") String classroomName, @RequestParam("department") String departmentName) {
        return new ResponseEntity<>(classesService.findByClassroom(classroomName, departmentName), HttpStatus.OK);
    }

    public void saveFullTimeClasses(String classesTypeString,
                                    String dayOfWeekString,
                                    String startTimeString,
                                    String endTimeString,
                                    String department,
                                    String classroom,
                                    String major,
                                    String studyYear,
                                    String group,
                                    String subjectName,
                                    String semesterType,
                                    String isDiplomaString,
                                    String academicYear,
                                    String frequencyString,
                                    String lecturersList) {

        MajorGroup majorGroup = majorGroupService.findByMajorGroupAndYear(major, studyYear, group);
        if (isNull(majorGroup)) {
            Major majorToSave = majorService.findFullTimeMajorByName(major);
            SemesterNumber semesterNumber = resolveSemesterNumber(SemesterType.fromDescription(semesterType), Integer.valueOf(studyYear));
            Group groupToSave = groupService.findGroupByNameAndSemester(group, semesterNumber);
            majorGroup = majorGroupService.saveMajorGroup(majorToSave, groupToSave, Integer.valueOf(studyYear));
        }

        Subject subject = subjectService.findSubjectByName(subjectName);
        DayOfWeek dayOfWeek = resolveDayOfWeek(dayOfWeekString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);
        ClassesType classesType = ClassesType.fromDescription(classesTypeString);
        DepartmentClassroom departmentClassroom = departmentClassroomService.findByDepartmentAndClassroomName(department, classroom);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia zajęć nie może być po ich zakończeniu");
        }

        List<Classes> savedClasses = new ArrayList<>();
        savedClasses.add(classesService.saveClasses(majorGroup, subject, dayOfWeek, startTime, endTime, classesType, departmentClassroom));


        List<String> lecturerNames = List.of(lecturersList.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);
        for (Classes classes : savedClasses) {
            for (String lecturerName : lecturerNames) {
                Boolean doesExists = classesLecturersService.doesClassesLecturerExists(classes.getMajorGroup(), classes.getSubject(), classes.getDayOfWeek(), classes.getStartTime(), classes.getEndTime(), classes.getClassesType(), classes.getDepartmentClassroom(), lecturerName);
                if (Boolean.FALSE.equals(doesExists)) {
                    Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);
                    classesLecturersService.saveClassesLecturers(classes, lecturer);
                }
            }
        }

        Boolean isDiploma = "Tak".equals(isDiplomaString);
        Semester semester = semesterService.findSemesterByYearTypeAndDiploma(academicYear, SemesterType.fromDescription(semesterType), isDiploma);
        Frequency frequency = Frequency.fromDescription(frequencyString);
        for (Classes classes : savedClasses) {
            semesterClassesService.saveSemesterClasses(semester, classes, frequency);
        }
    }

    private List<String> separateTitleFromLecturer(List<String> lecturers) {
        List<String> returnList = new ArrayList<>();
        for (String lecturer : lecturers) {
            String[] splitName = lecturer.split(" ");
            int length = splitName.length;
            returnList.add(splitName[length-2] + " " + splitName[length-1]);
        }
        return returnList;
    }

    public void updateFullTimeClasses(String classesTypeString,
                                      String dayOfWeekString,
                                      String startTimeString,
                                      String endTimeString,
                                      String departmentName,
                                      String classroom,
                                      String subjectName,
                                      String frequencyString,
                                      String lecturersList,
                                      Long semesterClassesId) {
        SemesterClasses semesterClasses = semesterClassesService.findById(semesterClassesId);
        Classes classes = semesterClasses.getClasses();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);
        DepartmentClassroom departmentClassroom = departmentClassroomService.findByDepartmentAndClassroomName(departmentName, classroom);
        Subject subject = subjectService.findSubjectByName(subjectName);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia zajęć nie może być po ich zakończeniu");
        }

        semesterClasses.getClasses().setClassesType(ClassesType.fromDescription(classesTypeString));
        semesterClasses.getClasses().setDayOfWeek(resolveDayOfWeek(dayOfWeekString));
        semesterClasses.getClasses().setStartTime(startTime);
        semesterClasses.getClasses().setEndTime(endTime);
        semesterClasses.getClasses().setDepartmentClassroom(departmentClassroom);
        semesterClasses.getClasses().setSubject(subject);
        semesterClasses.setFrequency(Frequency.fromDescription(frequencyString));

        semesterClassesService.update(semesterClasses);
        List<String> lecturerNames = List.of(lecturersList.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);

        List<ClassesLecturers> classesLecturers = classesLecturersService.findAllClassesLecturersByClasses(semesterClasses.getClasses().getClassesId());


        //Delete all lecturers that are no longer part of the classes
        for (ClassesLecturers cl : classesLecturers) {
            if (!lecturerNames.contains(cl.getLecturer().getName())) {
                classesLecturersService.deleteClassesLecturers(cl.getClassesLecturersId());
            }
        }

        //Add all new lecturers that were not part of the classes until not
        for (String lecturerName : lecturerNames) {
            if (classesLecturers.stream().noneMatch(cl -> cl.getLecturer().getName().equals(lecturerName))) {
                Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);
                classesLecturersService.saveClassesLecturers(classes, lecturer);
            }
        }

    }

    public void savePartTimeClasses(String classesTypeString,
                                    String classesDateString,
                                    String startTimeString,
                                    String endTimeString,
                                    String department,
                                    String classroom,
                                    String major,
                                    String studyYear,
                                    String group,
                                    String subjectName,
                                    String semesterType,
                                    String isDiplomaString,
                                    String academicYear,
                                    String lecturersList) {

        MajorGroup majorGroup = majorGroupService.findByMajorGroupAndYear(major, studyYear, group);
        if (isNull(majorGroup)) {
            Major majorToSave = majorService.findFullTimeMajorByName(major);
            SemesterNumber semesterNumber = resolveSemesterNumber(SemesterType.fromDescription(semesterType), Integer.valueOf(studyYear));
            Group groupToSave = groupService.findGroupByNameAndSemester(group, semesterNumber);
            majorGroup = majorGroupService.saveMajorGroup(majorToSave, groupToSave, Integer.valueOf(studyYear));
        }


        Subject subject = subjectService.findSubjectByName(subjectName);
        LocalDate classesDate = LocalDate.parse(classesDateString);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);
        ClassesType classesType = ClassesType.fromDescription(classesTypeString);
        DepartmentClassroom departmentClassroom = departmentClassroomService.findByDepartmentAndClassroomName(department, classroom);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia zajęć nie może być po ich zakończeniu");
        }

        List<Classes> savedClasses = new ArrayList<>();
        savedClasses.add(classesService.saveClasses(majorGroup, subject, classesDate.getDayOfWeek(), startTime, endTime, classesType, departmentClassroom));


        List<String> lecturerNames = List.of(lecturersList.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);
        for (Classes classes : savedClasses) {
            for (String lecturerName : lecturerNames) {
                Boolean doesExists = classesLecturersService.doesClassesLecturerExists(classes.getMajorGroup(), classes.getSubject(), classes.getDayOfWeek(), classes.getStartTime(), classes.getEndTime(), classes.getClassesType(), classes.getDepartmentClassroom(), lecturerName);
                if (Boolean.FALSE.equals(doesExists)) {
                    Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);
                    classesLecturersService.saveClassesLecturers(classes, lecturer);
                }
            }
        }

        Boolean isDiploma = "Tak".equals(isDiplomaString);
        Semester semester = semesterService.findSemesterByYearTypeAndDiploma(academicYear, SemesterType.fromDescription(semesterType), isDiploma);
        for (Classes classes : savedClasses) {
            partTimeSemesterClassesService.savePartTimeSemesterClasses(semester, classes, classesDate);
        }
    }

    public void updatePartTimeClasses(String classesTypeString,
                                      String classesDate,
                                      String startTimeString,
                                      String endTimeString,
                                      String departmentName,
                                      String classroom,
                                      String subjectName,
                                      String lecturersList,
                                      Long partTimeSemesterClassesId) {
        PartTimeSemesterClasses partTimeSemesterClasses = partTimeSemesterClassesService.findById(partTimeSemesterClassesId);
        Classes classes = partTimeSemesterClasses.getClasses();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime startTime = LocalTime.parse(startTimeString, formatter);
        LocalTime endTime = LocalTime.parse(endTimeString, formatter);
        DepartmentClassroom departmentClassroom = departmentClassroomService.findByDepartmentAndClassroomName(departmentName, classroom);
        Subject subject = subjectService.findSubjectByName(subjectName);

        if (startTime.isAfter(endTime)) {
            throw new TimetableException("Czas rozpoczęcia zajęć nie może być po ich zakończeniu");
        }

        partTimeSemesterClasses.getClasses().setClassesType(ClassesType.fromDescription(classesTypeString));
        partTimeSemesterClasses.setClassesDate(LocalDate.parse(classesDate));
        partTimeSemesterClasses.getClasses().setStartTime(startTime);
        partTimeSemesterClasses.getClasses().setEndTime(endTime);
        partTimeSemesterClasses.getClasses().setDepartmentClassroom(departmentClassroom);
        partTimeSemesterClasses.getClasses().setSubject(subject);

        partTimeSemesterClassesService.update(partTimeSemesterClasses);
        List<String> lecturerNames = List.of(lecturersList.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);

        List<ClassesLecturers> classesLecturers = classesLecturersService.findAllClassesLecturersByClasses(partTimeSemesterClasses.getClasses().getClassesId());


        //Delete all lecturers that are no longer part of the classes
        for (ClassesLecturers cl : classesLecturers) {
            if (!lecturerNames.contains(cl.getLecturer().getName())) {
                classesLecturersService.deleteClassesLecturers(cl.getClassesLecturersId());
            }
        }

        //Add all new lecturers that were not part of the classes until not
        for (String lecturerName : lecturerNames) {
            if (classesLecturers.stream().noneMatch(cl -> cl.getLecturer().getName().equals(lecturerName))) {
                Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);
                classesLecturersService.saveClassesLecturers(classes, lecturer);
            }
        }

    }

    private static DayOfWeek resolveDayOfWeek(String dayOfWeek) {
        switch (dayOfWeek) {
            case "Poniedziałek":
                return DayOfWeek.MONDAY;
            case "Wtorek":
                return DayOfWeek.TUESDAY;
            case "Środa":
                return DayOfWeek.WEDNESDAY;
            case "Czwartek":
                return DayOfWeek.THURSDAY;
            case "Piątek":
                return DayOfWeek.FRIDAY;
            case "Sobota":
                return DayOfWeek.SATURDAY;
            case "Niedziela":
                return DayOfWeek.SUNDAY;
            default:
                throw new ClassCastException("Cannot cast given string to DayOfWeek class: " + dayOfWeek);
        }
    }

    private static SemesterNumber resolveSemesterNumber(SemesterType semesterType, Integer studyYear) {
        if (semesterType.equals(SemesterType.WINTER)) {
            switch (studyYear) {
                case 1:
                    return SemesterNumber.I;
                case 2:
                    return SemesterNumber.III;
                case 3:
                    return SemesterNumber.V;
                case 4:
                    return SemesterNumber.VII;
            }
        } else {
            switch (studyYear) {
                case 1:
                    return SemesterNumber.II;
                case 2:
                    return SemesterNumber.IV;
                case 3:
                    return SemesterNumber.VI;
            }
        }
        return SemesterNumber.I;
    }
}
