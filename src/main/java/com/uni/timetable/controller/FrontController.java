package com.uni.timetable.controller;

import com.uni.timetable.model.*;
import com.uni.timetable.security.SecurityUtils;
import com.uni.timetable.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

import static com.uni.timetable.controller.ClassesController.separateTitleFromLecturer;

@Controller
@Slf4j
public class FrontController {
    LecturerService lecturerService;
    SemesterService semesterService;
    MajorService majorService;
    GroupService groupService;
    DepartmentService departmentService;
    ClassroomService classroomService;
    DepartmentClassroomService departmentClassroomService;
    SubjectService subjectService;
    MajorGroupService majorGroupService;
    ClassesController classesController;
    SemesterClassesService semesterClassesService;
    PartTimeSemesterClassesService partTimeSemesterClassesService;
    ClassesLecturersService classesLecturersService;
    OneTimeEventController oneTimeEventController;
    LecturerNonAvailableController lecturerNonAvailableController;
    SemesterClassesController semesterClassesController;
    AdminService adminService;

    @Autowired
    private RestTemplate restTemplate;

    public FrontController(LecturerService lecturerService,
                           SemesterService semesterService,
                           MajorService majorService,
                           GroupService groupService,
                           DepartmentService departmentService,
                           ClassroomService classroomService,
                           DepartmentClassroomService departmentClassroomService,
                           SubjectService subjectService,
                           MajorGroupService majorGroupService,
                           ClassesController classesController,
                           SemesterClassesService semesterClassesService,
                           PartTimeSemesterClassesService partTimeSemesterClassesService,
                           ClassesLecturersService classesLecturersService,
                           OneTimeEventController oneTimeEventController,
                           LecturerNonAvailableController lecturerNonAvailableController,
                           SemesterClassesController semesterClassesController,
                           AdminService adminService) {
        this.lecturerService = lecturerService;
        this.semesterService = semesterService;
        this.majorService = majorService;
        this.groupService = groupService;
        this.departmentService = departmentService;
        this.classroomService = classroomService;
        this.departmentClassroomService = departmentClassroomService;
        this.subjectService = subjectService;
        this.majorGroupService = majorGroupService;
        this.classesController = classesController;
        this.semesterClassesService = semesterClassesService;
        this.partTimeSemesterClassesService = partTimeSemesterClassesService;
        this.classesLecturersService = classesLecturersService;
        this.oneTimeEventController = oneTimeEventController;
        this.lecturerNonAvailableController = lecturerNonAvailableController;
        this.semesterClassesController = semesterClassesController;
        this.adminService = adminService;
    }

    @GetMapping()
    public String general(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                          @RequestParam(required = false, defaultValue = "4") String studyYear,
                          @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                          Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllFullTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "full-time-students";
    }

    @GetMapping("/full-time-students")
    public String students(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                           @RequestParam(required = false, defaultValue = "4") String studyYear,
                           @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                           @RequestParam(required = false, defaultValue = "Cały kierunek") String group,
                           Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("prevGroup", group);
        model.addAttribute("MajorNames", majorService.findAllFullTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(majorName).stream().map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "full-time-students";
    }

    @GetMapping("/part-time-students")
    public String partTimeStudents(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                                   @RequestParam(required = false, defaultValue = "3") String studyYear,
                                   @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                                   @RequestParam(required = false, defaultValue = "Cały kierunek") String group,
                                   Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("prevGroup", group);
        model.addAttribute("MajorNames", majorService.findAllPartTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(majorName).stream().map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "part-time-students";
    }

    @GetMapping("/classrooms")
    public String classrooms(@RequestParam(required = false, defaultValue = "Wydział Inżynierii Elektrycznej i Komputerowej") String departmentName,
                             @RequestParam(required = false, defaultValue = "A1") String classroomName,
                             Model model) {

        model.addAttribute("prevDepartment", departmentName);
        model.addAttribute("prevClassroom", classroomName);
        model.addAttribute("DepartmentNames", departmentService.findAllDepartmentNames());
        model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(departmentName));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "classrooms";
    }

    @GetMapping("/lecturers")
    public String selectedLecturerClasses(@RequestParam(required = false, defaultValue = "Krzysztof Czajkowski") String lecturerName,
                                          @RequestParam(required = false, defaultValue = "23-24") String academicYear,
                                          @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                                          Model model) {
        model.addAttribute("prevLecturer", lecturerName);
        model.addAttribute("prevAcademicYear", academicYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "lecturers";
    }

    @GetMapping("/admin")
    public String adminView(Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "admin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        AdminRequest adminRequest = new AdminRequest();
        adminRequest.setUsername(username);
        adminRequest.setPassword(password);
        HttpEntity<AdminRequest> requestEntity = new HttpEntity<>(adminRequest, headers);
        ResponseEntity<String> authorizationResponse;
        try {
            //Call internal controller method
            authorizationResponse = restTemplate.postForEntity("http://localhost:8080/adminController/authorize",
                    requestEntity,
                    String.class);
        } catch (HttpClientErrorException e) {
            authorizationResponse = new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
        boolean authorized = authorizationResponse.getStatusCode().equals(HttpStatus.OK);
        model.addAttribute("isAdminLogged", authorized);
        model.addAttribute("errorMessage", authorizationResponse.getBody());

        if (authorized) {
            return "lecturers";
        }

        return "admin";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        SecurityUtils.logOut();
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        return "admin";
    }

    @GetMapping("/add-full-time-classes")
    public String addFullTimeClassesView(@RequestParam(required = false, defaultValue = "Wydział Inżynierii Elektrycznej i Komputerowej") String selectedDepartment,
                                 @RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String selectedMajor,
                                 Model model) {
        model.addAttribute("selectedDepartment", selectedDepartment);
        model.addAttribute("selectedMajor", selectedMajor);
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        model.addAttribute("ClassesTypes", Arrays.stream(ClassesType.values()).map(classesType -> classesType.description));
        model.addAttribute("Weekdays", List.of(
                "Poniedziałek",
                "Wtorek",
                "Środa",
                "Czwartek",
                "Piątek"
        ));
        model.addAttribute("Departments", departmentService.findAllDepartmentNames());
        model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(selectedDepartment));
        model.addAttribute("Majors", majorService.findAllFullTimeMajorNames());
        model.addAttribute("StudyYears", majorGroupService.findAll().stream().map(MajorGroup::getStudyYear).distinct().toList());
        model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(selectedMajor).stream().filter(majorGroup -> majorGroup.getMajor().getStudyType().equals(StudyType.FULL_TIME)).map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
        model.addAttribute("Subjects", subjectService.findAllSubjectNames());
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("Frequencies", Arrays.stream(Frequency.values()).map(Frequency::getDescription));
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        return "add-full-time-classes";
    }

    @PostMapping("/add-full-time-classes")
    public String addFullTimeClasses(@RequestParam("classesType") String classesType,
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
                             @RequestParam("lecturers") String lecturersList,
                             @RequestParam("customStartDate") String customStartDate,
                             @RequestParam("customEndDate") String customEndDate,
                             Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        classesController.saveFullTimeClasses(classesType, dayOfWeek, startTime, endTime, department, classroom, major, studyYear, group, subject, semesterType, isDiploma, academicYear, frequency, lecturersList, customStartDate, customEndDate);
        return "add-full-time-classes";
    }

    @GetMapping("/modify-classes")
    public String modifyClasses(@RequestParam Long eventId,
                                @RequestParam("studyType") String studyTypeString,
                                Model model) {

        StudyType studyType = StudyType.valueOf(studyTypeString);
        if (studyType.equals(StudyType.FULL_TIME)) {
            model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
            model.addAttribute("eventId", eventId);


            SemesterClasses semesterClasses = semesterClassesService.findById(eventId);
            String classesTypeString = semesterClasses.getClasses().getClassesType().description;
            String weekday = mapWeekdayToPolish(semesterClasses.getClasses().getDayOfWeek());
            String startTime = semesterClasses.getClasses().getStartTime().toString();
            String endTime = semesterClasses.getClasses().getEndTime().toString();
            String department = semesterClasses.getClasses().getDepartmentClassroom().getDepartment().getDepartmentName();
            String classroom = semesterClasses.getClasses().getDepartmentClassroom().getClassroom().getClassroomName();
            String major = semesterClasses.getClasses().getMajorGroup().getMajor().getMajorName();
            String studyYear = semesterClasses.getClasses().getMajorGroup().getStudyYear().toString();
            String group = semesterClasses.getClasses().getMajorGroup().getGroup().getGroupName();
            String subject = semesterClasses.getClasses().getSubject().getSubjectName();
            String semesterType = semesterClasses.getSemester().getSemesterType().getDescription();
            String academicYear = semesterClasses.getSemester().getAcademicYear();
            String frequency = semesterClasses.getFrequency().getDescription();
            Long classesId = semesterClasses.getClasses().getClassesId();

            //Dane z aktualnie wybranych zajęć
            model.addAttribute("selectedType", classesTypeString);
            model.addAttribute("selectedWeekday", weekday);
            model.addAttribute("startTimeSelected", startTime);
            model.addAttribute("endTimeSelected", endTime);
            model.addAttribute("selectedDepartment", department);
            model.addAttribute("selectedClassroom", classroom);
            model.addAttribute("selectedMajor", major);
            model.addAttribute("selectedStudyYear", studyYear);
            model.addAttribute("selectedGroup", group);
            model.addAttribute("selectedSubject", subject);
            model.addAttribute("selectedSemesterType", semesterType);
            model.addAttribute("selectedAcademicYear", academicYear);
            model.addAttribute("selectedFrequency", frequency);
            model.addAttribute("selectedLecturers", classesLecturersService.findAllLecturersNamesWithTitlesByClasses(classesId));


            model.addAttribute("ClassesTypes", Arrays.stream(ClassesType.values()).map(classesType -> classesType.description));
            model.addAttribute("Weekdays", List.of(
                    "Poniedziałek",
                    "Wtorek",
                    "Środa",
                    "Czwartek",
                    "Piątek"
            ));
            model.addAttribute("Departments", departmentService.findAllDepartmentNames());
            model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(department));
            model.addAttribute("Majors", majorService.findAllFullTimeMajorNames());
            model.addAttribute("StudyYears", majorGroupService.findAll().stream().map(MajorGroup::getStudyYear).distinct().toList());
            model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(major).stream().map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
            model.addAttribute("Subjects", subjectService.findAllSubjectNames());
            model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
            model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
            model.addAttribute("Frequencies", Arrays.stream(Frequency.values()).map(Frequency::getDescription));
            model.addAttribute("LecturersNames", lecturerService.findAllNames());

            return "modify-full-time-classes";
        } else {
            model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
            model.addAttribute("eventId", eventId);
            PartTimeSemesterClasses partTimeSemesterClasses = partTimeSemesterClassesService.findById(eventId);
            String classesTypeString = partTimeSemesterClasses.getClasses().getClassesType().description;
            String date = partTimeSemesterClasses.getClassesDate().toString();
            String startTime = partTimeSemesterClasses.getClasses().getStartTime().toString();
            String endTime = partTimeSemesterClasses.getClasses().getEndTime().toString();
            String department = partTimeSemesterClasses.getClasses().getDepartmentClassroom().getDepartment().getDepartmentName();
            String classroom = partTimeSemesterClasses.getClasses().getDepartmentClassroom().getClassroom().getClassroomName();
            String major = partTimeSemesterClasses.getClasses().getMajorGroup().getMajor().getMajorName();
            String studyYear = partTimeSemesterClasses.getClasses().getMajorGroup().getStudyYear().toString();
            String group = partTimeSemesterClasses.getClasses().getMajorGroup().getGroup().getGroupName();
            String subject = partTimeSemesterClasses.getClasses().getSubject().getSubjectName();
            String semesterType = partTimeSemesterClasses.getSemester().getSemesterType().getDescription();
            String academicYear = partTimeSemesterClasses.getSemester().getAcademicYear();
            Long classesId = partTimeSemesterClasses.getClasses().getClassesId();

            //Dane z aktualnie wybranych zajęć
            model.addAttribute("selectedType", classesTypeString);
            model.addAttribute("selectedDate", date);
            model.addAttribute("startTimeSelected", startTime);
            model.addAttribute("endTimeSelected", endTime);
            model.addAttribute("selectedDepartment", department);
            model.addAttribute("selectedClassroom", classroom);
            model.addAttribute("selectedMajor", major);
            model.addAttribute("selectedStudyYear", studyYear);
            model.addAttribute("selectedGroup", group);
            model.addAttribute("selectedSubject", subject);
            model.addAttribute("selectedSemesterType", semesterType);
            model.addAttribute("selectedAcademicYear", academicYear);
            model.addAttribute("selectedLecturers", classesLecturersService.findAllLecturersNamesWithTitlesByClasses(classesId));


            model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
            model.addAttribute("ClassesTypes", Arrays.stream(ClassesType.values()).map(classesType -> classesType.description));
            model.addAttribute("Departments", departmentService.findAllDepartmentNames());
            model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(department));
            model.addAttribute("Majors", majorService.findAllPartTimeMajorNames());
            model.addAttribute("StudyYears", majorGroupService.findAll().stream().map(MajorGroup::getStudyYear).distinct().toList());
            model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(major).stream().map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
            model.addAttribute("Subjects", subjectService.findAllSubjectNames());
            model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
            model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
            model.addAttribute("LecturersNames", lecturerService.findAllNames());

            return "modify-part-time-classes";
        }
    }

    @PostMapping("/modify-full-time-classes")
    public String modifyFullTimeClasses(@RequestParam("classesType") String classesType,
                                        @RequestParam("dayOfWeek") String dayOfWeek,
                                        @RequestParam("startTime") String startTime,
                                        @RequestParam("endTime") String endTime,
                                        @RequestParam("department") String department,
                                        @RequestParam("classroom") String classroom,
                                        @RequestParam("subject") String subject,
                                        @RequestParam("frequency") String frequency,
                                        @RequestParam("lecturers") String lecturersList,
                                        @RequestParam("eventId") Long semesterClassesId,
                                        Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        classesController.updateFullTimeClasses(classesType, dayOfWeek, startTime, endTime, department, classroom, subject, frequency, lecturersList, semesterClassesId);
        return "/modify-full-time-classes";
    }

    @PostMapping("/modify-part-time-classes")
    public String modifyPartTimeClasses(@RequestParam("classesType") String classesType,
                                        @RequestParam("classesDate") String classesDate,
                                        @RequestParam("startTime") String startTime,
                                        @RequestParam("endTime") String endTime,
                                        @RequestParam("department") String department,
                                        @RequestParam("classroom") String classroom,
                                        @RequestParam("subject") String subject,
                                        @RequestParam("lecturers") String lecturersList,
                                        @RequestParam("eventId") Long semesterClassesId,
                                        Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        classesController.updatePartTimeClasses(classesType, classesDate, startTime, endTime, department, classroom, subject, lecturersList, semesterClassesId);
        return "/modify-part-time-classes";
    }

    @PostMapping("/delete-full-time-classes")
    public String deleteFullTimeClasses(@RequestParam Long eventId,
                                        Model model) {
        semesterClassesService.deleteSemesterClassesById(eventId);
        return students("Informatyka w Inżynierii Komputerowej",
                "4",
                "Zimowy",
                "Cały kierunek",
                model);
    }

    @PostMapping("/delete-part-time-classes")
    public String deletePartTimeClasses(@RequestParam Long eventId,
                                        Model model) {
        partTimeSemesterClassesService.deletePartTimeSemesterClassesById(eventId);
        return partTimeStudents("Informatyka w Inżynierii Komputerowej",
                "4",
                "Zimowy",
                "Cały kierunek",
                model);
    }

    @GetMapping("/add-part-time-classes")
    public String addPartTimeClassesView(@RequestParam(required = false, defaultValue = "Wydział Inżynierii Elektrycznej i Komputerowej") String selectedDepartment,
                                 @RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String selectedMajor,
                                 @RequestParam(required = false, defaultValue = "1") String selectedStudyYear,
                                 @RequestParam(required = false, defaultValue = "42i") String selectedGroup,
                                 @RequestParam(required = false, defaultValue = "Zaawansowane Bazy Danych") String selectedSubject,
                                 @RequestParam(required = false, defaultValue = "Zimowy") String selectedSemesterType,
                                 @RequestParam(required = false, defaultValue = "Nie") String selectedIsDiploma,
                                 @RequestParam(required = false, defaultValue = "23-24") String selectedAcademicYear,
                                 Model model) {
        model.addAttribute("selectedDepartment", selectedDepartment);
        model.addAttribute("selectedMajor", selectedMajor);
        model.addAttribute("selectedStudyYear", Integer.parseInt(selectedStudyYear));
        model.addAttribute("selectedGroup", selectedGroup);
        model.addAttribute("selectedSubject", selectedSubject);
        model.addAttribute("selectedSemesterType", selectedSemesterType);
        model.addAttribute("selectedIsDiploma", selectedIsDiploma);
        model.addAttribute("selectedAcademicYear", selectedAcademicYear);
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        model.addAttribute("ClassesTypes", Arrays.stream(ClassesType.values()).map(classesType -> classesType.description));
        model.addAttribute("Departments", departmentService.findAllDepartmentNames());
        model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(selectedDepartment));
        model.addAttribute("Majors", majorService.findAllPartTimeMajorNames());
        model.addAttribute("StudyYears", majorGroupService.findAll().stream().map(MajorGroup::getStudyYear).distinct().toList());
        model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(selectedMajor).stream().filter(majorGroup -> majorGroup.getMajor().getStudyType().equals(StudyType.PART_TIME)).map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
        model.addAttribute("Subjects", subjectService.findAllSubjectNames());
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        model.addAttribute("SummedHours", semesterClassesController.getSummedAmountOfHoursForPartTimeStudies(selectedMajor, selectedStudyYear, selectedGroup, selectedSubject, selectedSemesterType, selectedIsDiploma, selectedAcademicYear));

        return "add-part-time-classes";
    }

    @PostMapping("/add-part-time-classes")
    public String addPartTimeClasses(@RequestParam("classesType") String classesType,
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
                                     @RequestParam("lecturers") String lecturersList,
                                     Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        classesController.savePartTimeClasses(classesType, classesDate, startTime, endTime, department, classroom, major, studyYear, group, subject, semesterType, isDiploma, academicYear, lecturersList);
        return "add-part-time-classes";
    }

    @GetMapping("/add-one-time-event")
    public String addOneTimeEvent(@RequestParam(required = false, defaultValue = "Wydział Inżynierii Elektrycznej i Komputerowej") String selectedDepartment,
                                  Model model) {
        model.addAttribute("selectedDepartment", selectedDepartment);
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        model.addAttribute("Departments", departmentService.findAllDepartmentNames());
        model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(selectedDepartment));
        model.addAttribute("Majors", majorService.findAllPartTimeMajorNames());
        return "add-one-time-event";
    }

    @PostMapping("/add-one-time-event")
    public String addOneTimeEvent(@RequestParam("classesDate") String eventDate,
                                  @RequestParam("startTime") String startTime,
                                  @RequestParam("endTime") String endTime,
                                  @RequestParam("department") String department,
                                  @RequestParam("classroom") String classroom,
                                  Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        oneTimeEventController.saveOneTimeEvent(eventDate, startTime, endTime, department, classroom);
        return "add-one-time-event";
    }

    @GetMapping("/add-lecturer-non-available")
    public String addLecturerNonAvailable(Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        return "add-lecturer-non-available";
    }

    @PostMapping("/add-lecturer-non-available")
    public String addLecturerNonAvailable(@RequestParam("classesDate") String eventDate,
                                  @RequestParam("startTime") String startTime,
                                  @RequestParam("endTime") String endTime,
                                  @RequestParam("lecturer") String lecturer,
                                  Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged());
        String lecturerName = separateTitleFromLecturer(List.of(lecturer)).get(0);
        lecturerNonAvailableController.saveLecturerNonAvailable(eventDate, startTime, endTime, lecturerName);
        return "add-lecturer-non-available";
    }

    static String mapWeekdayToPolish(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {

            case MONDAY -> {
                return "Poniedziałek";
            }
            case TUESDAY -> {
                return "Wtorek";
            }
            case WEDNESDAY -> {
                return "Środa";
            }
            case THURSDAY -> {
                return "Czwartek";
            }
            case FRIDAY -> {
                return "Piątek";
            }
            case SATURDAY -> {
                return "Sobota";
            }
            case SUNDAY -> {
                return "Niedziela";
            }
            default -> {
                throw new ClassCastException("Cannot cast given string to DayOfWeek class: " + dayOfWeek);
            }
        }
    }
}
