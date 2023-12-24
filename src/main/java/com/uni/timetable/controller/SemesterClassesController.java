package com.uni.timetable.controller;

import com.uni.timetable.exception.TimetableException;
import com.uni.timetable.model.*;
import com.uni.timetable.service.*;
import com.uni.timetable.utils.OneTimeEventToCalendarEventMapper;
import com.uni.timetable.utils.SemesterClassesToCalendarEventMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.uni.timetable.controller.ClassesController.*;

@Controller
@RequestMapping("/semesterClasses")
public class SemesterClassesController {

    private final SemesterClassesService semesterClassesService;
    private final PartTimeSemesterClassesService partTimeSemesterClassesService;
    private final ClassesLecturersService classesLecturersService;
    private final ClassesService classesService;
    private final OneTimeEventService oneTimeEventService;
    private final MajorGroupService majorGroupService;
    private final DepartmentClassroomService departmentClassroomService;
    private final SubjectService subjectService;
    private final SemesterService semesterService;
    private final LecturerService lecturerService;
    private final DepartmentService departmentService;
    private final MajorService majorService;
    private final GroupService groupService;
    private final LecturerNonAvailableService lecturerNonAvailableService;

    public SemesterClassesController(SemesterClassesService semesterClassesService,
                                     PartTimeSemesterClassesService partTimeSemesterClassesService,
                                     ClassesLecturersService classesLecturersService,
                                     ClassesService classesService,
                                     OneTimeEventService oneTimeEventService,
                                     MajorGroupService majorGroupService,
                                     DepartmentClassroomService departmentClassroomService,
                                     SubjectService subjectService,
                                     SemesterService semesterService,
                                     LecturerService lecturerService,
                                     DepartmentService departmentService,
                                     MajorService majorService,
                                     GroupService groupService,
                                     LecturerNonAvailableService lecturerNonAvailableService) {
        this.semesterClassesService = semesterClassesService;
        this.partTimeSemesterClassesService = partTimeSemesterClassesService;
        this.classesLecturersService = classesLecturersService;
        this.classesService = classesService;
        this.oneTimeEventService = oneTimeEventService;
        this.majorGroupService = majorGroupService;
        this.departmentClassroomService = departmentClassroomService;
        this.subjectService = subjectService;
        this.semesterService = semesterService;
        this.lecturerService = lecturerService;
        this.departmentService = departmentService;
        this.majorService = majorService;
        this.groupService = groupService;
        this.lecturerNonAvailableService = lecturerNonAvailableService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<SemesterClasses>> findAll() {
        return new ResponseEntity<>(semesterClassesService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/lecturer/{lecturerName}/{academicYear}/{semesterType}")
    @ResponseBody
    public List<CalendarEvent> findClassesByLecturerName(@PathVariable String lecturerName,
                                                         @PathVariable String academicYear,
                                                         @PathVariable String semesterType) {
        List<SemesterClasses> fullTimeClasses = semesterClassesService.findSemesterClassesBySemester(academicYear, SemesterType.fromDescription(semesterType));
        String[] splitName = lecturerName.split(" ");
        int length = splitName.length;
        lecturerName = splitName[length - 2] + " " + splitName[length - 1];
        List<Classes> classesByLecturers = classesLecturersService.findClassesByLecturerName(lecturerName);
        fullTimeClasses = fullTimeClasses.stream()
                .filter(semesterClasses -> classesByLecturers.stream()
                        .anyMatch(classes -> classes.getClassesId().equals(semesterClasses.getClasses().getClassesId()))).toList();

        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesService.findPartTimeSemesterClassesBySemester(academicYear, SemesterType.fromDescription(semesterType));
        partTimeSemesterClasses = partTimeSemesterClasses.stream()
                .filter(partTimeClasses -> classesByLecturers.stream()
                        .anyMatch(classes -> classes.getClassesId().equals(partTimeClasses.getClasses().getClassesId()))).toList();

        List<ClassesLecturers> classesLecturersList = classesLecturersService.findAll();
        List<CalendarEvent> calendarEvents = SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(partTimeSemesterClasses, classesLecturersList);
        calendarEvents.addAll(SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(fullTimeClasses, classesLecturersList));

        //Add lecturer non available
        String lecturerNameWithoutTitle = separateTitleFromLecturer(List.of(lecturerName)).get(0);
        List<LecturerNonAvailable> lecturersNonAvailable = lecturerNonAvailableService.findAllByLecturerName(lecturerNameWithoutTitle);
        calendarEvents.addAll(OneTimeEventToCalendarEventMapper.mapLecturersNonAvailableToCalendarEvents(lecturersNonAvailable));

        return calendarEvents;
    }

    @GetMapping("/full-time-major/{majorName}/{academicYear}/{semesterType}/{group}")
    @ResponseBody
    public List<CalendarEvent> findClassesByFullTimeMajor(@PathVariable String majorName,
                                                          @PathVariable String academicYear,
                                                          @PathVariable String semesterType,
                                                          @PathVariable String group) {
        List<SemesterClasses> classesByFullTimeMajor;
        if ("Cały kierunek".equals(group)) {
            classesByFullTimeMajor = semesterClassesService.findSemesterClassesByFullTimeMajorAndSemester(majorName, academicYear, SemesterType.fromDescription(semesterType));
        } else {
            classesByFullTimeMajor = semesterClassesService.findSemesterClassesByFullTimeMajorSemesterAndGroup(majorName, academicYear, SemesterType.fromDescription(semesterType), group);
        }
        List<ClassesLecturers> classesLecturersList = classesLecturersService.findAll();
        return SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByFullTimeMajor, classesLecturersList);
    }

    @GetMapping("/part-time-major/{majorName}/{academicYear}/{semesterType}/{group}")
    @ResponseBody
    public List<CalendarEvent> findClassesByPartTimeMajor(@PathVariable String majorName,
                                                          @PathVariable String academicYear,
                                                          @PathVariable String semesterType,
                                                          @PathVariable String group) {
        List<PartTimeSemesterClasses> partTimeSemesterClasses;
        if ("Cały kierunek".equals(group)) {
            partTimeSemesterClasses = partTimeSemesterClassesService.findPartTimeSemesterClassesByMajorAndSemester(majorName, academicYear, SemesterType.fromDescription(semesterType));
        } else {
            partTimeSemesterClasses = partTimeSemesterClassesService.findPartTimeSemesterClassesByMajorSemesterAndGroup(majorName, academicYear, SemesterType.fromDescription(semesterType), group);
        }

        List<ClassesLecturers> classesLecturersList = classesLecturersService.findAll();
        return SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(partTimeSemesterClasses, classesLecturersList);
    }

    @GetMapping("/department-classroom/{departmentName}/{classroomName}")
    @ResponseBody
    public List<CalendarEvent> findClassesByDepartmentAndClassroom(@PathVariable String departmentName,
                                                                   @PathVariable String classroomName) {
        List<SemesterClasses> classesByDepartmentAndClassroom = semesterClassesService.findSemesterClassesByDepartmentAndClassroom(departmentName, classroomName);
        List<ClassesLecturers> classesLecturersList = classesLecturersService.findAll();
        List<CalendarEvent> calendarEvents = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByDepartmentAndClassroom, classesLecturersList);

        List<OneTimeEvent> oneTimeEventsByDepartmentAndClassroom = oneTimeEventService.getOneTimeEventsByDepartmentAndClassroom(departmentName, classroomName);
        calendarEvents.addAll(OneTimeEventToCalendarEventMapper.mapOneTimeEventsToCalendarEvents(oneTimeEventsByDepartmentAndClassroom));
        return calendarEvents;
    }

    @GetMapping("/check-full-time-classes-collision")
    @ResponseBody
    public boolean checkForFullTimeClassesCollision(@RequestParam("classesType") String classesType,
                                                    @RequestParam("dayOfWeek") String dayOfWeek,
                                                    @RequestParam("startTime") String startTime,
                                                    @RequestParam("endTime") String endTime,
                                                    @RequestParam("department") String department,
                                                    @RequestParam("classroom") String classroom,
                                                    @RequestParam("major") String major,
                                                    @RequestParam("studyYear") String studyYear,
                                                    @RequestParam("group") String group,
                                                    @RequestParam("subject") String subject,
                                                    @RequestParam("semesterType") String semesterType,
                                                    @RequestParam("isDiploma") String isDiploma,
                                                    @RequestParam("academicYear") String academicYear,
                                                    @RequestParam("frequency") String frequency,
                                                    @RequestParam("lecturers") String lecturers) {
        List<SemesterClasses> semesterClasses = getSemesterClasses(classesType, dayOfWeek, startTime, endTime, department, classroom, major, studyYear, group, subject, semesterType, isDiploma, academicYear, frequency, lecturers);
        List<ClassesLecturers> classesLecturersList = classesLecturersService.findAll();
        List<CalendarEvent> calendarEventsByNewSemesterClasses = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(semesterClasses, classesLecturersList);


        List<SemesterClasses> classesByClassroomAndDepartment = semesterClassesService.findAllClassesByClassroomAndDepartment(classroom, department);
        List<CalendarEvent> calendarEventsByClassroom = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByClassroomAndDepartment, classesLecturersList);

        //Check for collision with classrooms
        for (CalendarEvent newSemesterEvent : calendarEventsByNewSemesterClasses) {
            for (CalendarEvent classroomEvent : calendarEventsByClassroom) {
                if (newSemesterEvent.getStart().equals(classroomEvent.getStart()) &&
                        newSemesterEvent.getEnd().equals(classroomEvent.getEnd())) {
                    return true;
                }
            }
        }

        //Check for collision with lecturers
        List<String> lecturerNames = List.of(lecturers.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);
        List<SemesterClasses> allClasses = semesterClassesService.findAll();
        for (String lecturer : lecturerNames) {
            List<Classes> classesByLecturers = classesLecturersService.findClassesByLecturerName(lecturer);
            allClasses = allClasses.stream()
                    .filter(sc -> classesByLecturers.stream()
                            .anyMatch(classes -> classes.getClassesId().equals(sc.getClasses().getClassesId()))).toList();
            List<CalendarEvent> calendarEventsByLecturer = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByClassroomAndDepartment, classesLecturersList);


            //Check fo collision with lecturers non-available
            List<LecturerNonAvailable> lecturerNonAvailableList = lecturerNonAvailableService.findAllByLecturerName(lecturer);
            List<CalendarEvent> calendarEventsByLecturerNonAvailable = OneTimeEventToCalendarEventMapper.mapLecturersNonAvailableToCalendarEvents(lecturerNonAvailableList);

            for (CalendarEvent newSemesterEvent : calendarEventsByNewSemesterClasses) {
                for (CalendarEvent lecturerEvent : calendarEventsByLecturer) {
                    if (newSemesterEvent.getStart().equals(lecturerEvent.getStart()) &&
                            newSemesterEvent.getEnd().equals(lecturerEvent.getEnd())) {
                        return true;
                    }
                }
                for (CalendarEvent lecturerNonAvailableEvent : calendarEventsByLecturerNonAvailable) {
                    if (newSemesterEvent.getStart().equals(lecturerNonAvailableEvent.getStart()) &&
                            newSemesterEvent.getEnd().equals(lecturerNonAvailableEvent.getEnd())) {
                        return true;
                    }
                }
            }


        }

        //Check for collision with group
        List<SemesterClasses> classesByGroupAndMajor = semesterClassesService.findAllClassesByGroupAndMajor(group, major);
        List<CalendarEvent> calendarEventsByGroup = SemesterClassesToCalendarEventMapper.mapClassesToCalendarEvent(classesByGroupAndMajor, classesLecturersList);
        for (CalendarEvent newSemesterEvent : calendarEventsByNewSemesterClasses) {
            for (CalendarEvent groupEvent : calendarEventsByGroup) {
                if (newSemesterEvent.getStart().equals(groupEvent.getStart()) &&
                        newSemesterEvent.getEnd().equals(groupEvent.getEnd())) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<SemesterClasses> getSemesterClasses(String classesTypeString,
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

        MajorGroup majorGroup = majorGroupService.findByMajorGroupYearAndType(major, studyYear, group, StudyType.FULL_TIME);

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
        savedClasses.add(classesService.createClasses(majorGroup, subject, dayOfWeek, startTime, endTime, classesType, departmentClassroom));


        List<String> lecturerNames = List.of(lecturersList.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);
        for (Classes classes : savedClasses) {
            for (String lecturerName : lecturerNames) {
                Boolean doesExists = classesLecturersService.doesClassesLecturerExists(classes.getMajorGroup(), classes.getSubject(), classes.getDayOfWeek(), classes.getStartTime(), classes.getEndTime(), classes.getClassesType(), classes.getDepartmentClassroom(), lecturerName);
                if (Boolean.FALSE.equals(doesExists)) {
                    Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);
                }
            }
        }

        Boolean isDiploma = "Tak".equals(isDiplomaString);
        Semester semester = semesterService.findSemesterByYearTypeAndDiploma(academicYear, SemesterType.fromDescription(semesterType), isDiploma);
        Frequency frequency = Frequency.fromDescription(frequencyString);
        List<SemesterClasses> semesterClasses = new ArrayList<>();
        for (Classes classes : savedClasses) {
            semesterClasses.add(semesterClassesService.createSemesterClasses(semester, classes, frequency));
        }
        return semesterClasses;
    }

    @GetMapping("/check-part-time-classes-collision")
    @ResponseBody
    public boolean checkForPartTimeClassesCollision(@RequestParam("classesType") String classesType,
                                                    @RequestParam("classesDate") String classesDate,
                                                    @RequestParam("startTime") String startTime,
                                                    @RequestParam("endTime") String endTime,
                                                    @RequestParam("department") String department,
                                                    @RequestParam("classroom") String classroom,
                                                    @RequestParam("major") String major,
                                                    @RequestParam("studyYear") String studyYear,
                                                    @RequestParam("group") String group,
                                                    @RequestParam("subject") String subject,
                                                    @RequestParam("semesterType") String semesterType,
                                                    @RequestParam("isDiploma") String isDiploma,
                                                    @RequestParam("academicYear") String academicYear,
                                                    @RequestParam("lecturers") String lecturers) {
        List<PartTimeSemesterClasses> partTimeSemesterClasses = getPartTimeSemesterClasses(classesType, classesDate, startTime, endTime, department, classroom, major, studyYear, group, subject, semesterType, isDiploma, academicYear, lecturers);
        List<ClassesLecturers> classesLecturersList = classesLecturersService.findAll();
        List<CalendarEvent> calendarEventsByNewSemesterClasses = SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(partTimeSemesterClasses, classesLecturersList);

        List<PartTimeSemesterClasses> classesByClassroomAndDepartment = partTimeSemesterClassesService.findAllPartTimeClassesByClassroomAndDepartment(classroom, department);
        List<CalendarEvent> calendarEventsByClassroom = SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(classesByClassroomAndDepartment, classesLecturersList);

        //Check for collision with classrooms
        for (CalendarEvent newSemesterEvent : calendarEventsByNewSemesterClasses) {
            for (CalendarEvent classroomEvent : calendarEventsByClassroom) {
                if (newSemesterEvent.getStart().equals(classroomEvent.getStart()) &&
                        newSemesterEvent.getEnd().equals(classroomEvent.getEnd())) {
                    return true;
                }
            }
        }

        //Check for collision with lecturers
        List<String> lecturerNames = List.of(lecturers.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);
        List<PartTimeSemesterClasses> allClasses = partTimeSemesterClassesService.findAll();
        for (String lecturer : lecturerNames) {
            List<Classes> classesByLecturers = classesLecturersService.findClassesByLecturerName(lecturer);
            allClasses = allClasses.stream()
                    .filter(sc -> classesByLecturers.stream()
                            .anyMatch(classes -> classes.getClassesId().equals(sc.getClasses().getClassesId()))).toList();
            List<CalendarEvent> calendarEventsByLecturer = SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(classesByClassroomAndDepartment, classesLecturersList);


            //Check fo collision with lecturers non-available
            List<LecturerNonAvailable> lecturerNonAvailableList = lecturerNonAvailableService.findAllByLecturerName(lecturer);
            List<CalendarEvent> calendarEventsByLecturerNonAvailable = OneTimeEventToCalendarEventMapper.mapLecturersNonAvailableToCalendarEvents(lecturerNonAvailableList);

            for (CalendarEvent newSemesterEvent : calendarEventsByNewSemesterClasses) {
                for (CalendarEvent lecturerEvent : calendarEventsByLecturer) {
                    if (newSemesterEvent.getStart().equals(lecturerEvent.getStart()) &&
                            newSemesterEvent.getEnd().equals(lecturerEvent.getEnd())) {
                        return true;
                    }
                }
                for (CalendarEvent lecturerNonAvailableEvent : calendarEventsByLecturerNonAvailable) {
                    if (newSemesterEvent.getStart().equals(lecturerNonAvailableEvent.getStart()) &&
                            newSemesterEvent.getEnd().equals(lecturerNonAvailableEvent.getEnd())) {
                        return true;
                    }
                }
            }
        }

        //Check for collision with group
        List<PartTimeSemesterClasses> classesByGroupAndMajor = partTimeSemesterClassesService.findAllPartTimeClassesByGroupAndMajor(group, major);
        List<CalendarEvent> calendarEventsByGroup = SemesterClassesToCalendarEventMapper.mapPartTimeClassesToCalendarEvent(classesByGroupAndMajor, classesLecturersList);
        for (CalendarEvent newSemesterEvent : calendarEventsByNewSemesterClasses) {
            for (CalendarEvent groupEvent : calendarEventsByGroup) {
                if (newSemesterEvent.getStart().equals(groupEvent.getStart()) &&
                        newSemesterEvent.getEnd().equals(groupEvent.getEnd())) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<PartTimeSemesterClasses> getPartTimeSemesterClasses(String classesTypeString,
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
        MajorGroup majorGroup = majorGroupService.findByMajorGroupYearAndType(major, studyYear, group, StudyType.PART_TIME);


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
        savedClasses.add(classesService.createClasses(majorGroup, subject, classesDate.getDayOfWeek(), startTime, endTime, classesType, departmentClassroom));


        List<String> lecturerNames = List.of(lecturersList.split(","));
        lecturerNames = separateTitleFromLecturer(lecturerNames);
        for (Classes classes : savedClasses) {
            for (String lecturerName : lecturerNames) {
                Boolean doesExists = classesLecturersService.doesClassesLecturerExists(classes.getMajorGroup(), classes.getSubject(), classes.getDayOfWeek(), classes.getStartTime(), classes.getEndTime(), classes.getClassesType(), classes.getDepartmentClassroom(), lecturerName);
                if (Boolean.FALSE.equals(doesExists)) {
                    Lecturer lecturer = lecturerService.findLecturerByName(lecturerName);
                }
            }
        }

        Boolean isDiploma = "Tak".equals(isDiplomaString);
        Semester semester = semesterService.findSemesterByYearTypeAndDiploma(academicYear, SemesterType.fromDescription(semesterType), isDiploma);
        List<PartTimeSemesterClasses> partTimeSemesterClasses = new ArrayList<>();
        for (Classes classes : savedClasses) {
            partTimeSemesterClasses.add(partTimeSemesterClassesService.createPartTimeSemesterClasses(semester, classes, classesDate));
        }
        return partTimeSemesterClasses;
    }

    public Double getSummedAmountOfHoursForPartTimeStudies(String major,
                                                           String studyYear,
                                                           String group,
                                                           String subjectName,
                                                           String semesterType,
                                                           String isDiplomaString,
                                                           String academicYear) {

        Double summedHours = 0d;
        Boolean isDiploma = "Tak".equals(isDiplomaString);
        List<PartTimeSemesterClasses> partTimeSemesterClassesList = partTimeSemesterClassesService.findAllToGetHoursWhenAdding(major, Integer.parseInt(studyYear), group, subjectName, SemesterType.fromDescription(semesterType), isDiploma, academicYear);

        for (PartTimeSemesterClasses partTimeSemesterClasses : partTimeSemesterClassesList) {
            summedHours += calculateHoursFromClasses(partTimeSemesterClasses);
        }

        return summedHours;
    }

    private Double calculateHoursFromClasses(PartTimeSemesterClasses partTimeSemesterClasses) {
        Duration duration = Duration.between(partTimeSemesterClasses.getClasses().getStartTime(), partTimeSemesterClasses.getClasses().getEndTime());
        return (double) duration.toMinutes()/60;
    }
}
