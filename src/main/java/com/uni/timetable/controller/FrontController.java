package com.uni.timetable.controller;

import com.uni.timetable.model.ClassesType;
import com.uni.timetable.model.Frequency;
import com.uni.timetable.model.MajorGroup;
import com.uni.timetable.model.SemesterType;
import com.uni.timetable.repository.MajorGroupRepository;
import com.uni.timetable.security.SecurityUtils;
import com.uni.timetable.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public FrontController(LecturerService lecturerService,
                           SemesterService semesterService,
                           MajorService majorService,
                           GroupService groupService,
                           DepartmentService departmentService,
                           ClassroomService classroomService,
                           DepartmentClassroomService departmentClassroomService,
                           SubjectService subjectService,
                           MajorGroupService majorGroupService,
                           ClassesController classesController) {
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
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "full-time-students";
    }

    @GetMapping("/full-time-students")
    public String students(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                           @RequestParam(required = false, defaultValue = "4") String studyYear,
                           @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                           Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllFullTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "full-time-students";
    }

    @GetMapping("/part-time-students")
    public String partTimeStudents(@RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String majorName,
                                   @RequestParam(required = false, defaultValue = "3") String studyYear,
                                   @RequestParam(required = false, defaultValue = "Zimowy") String semesterType,
                                   Model model) {

        model.addAttribute("prevMajor", majorName);
        model.addAttribute("prevStudyYear", studyYear);
        model.addAttribute("prevSemesterType", semesterType);
        model.addAttribute("MajorNames", majorService.findAllPartTimeMajorNames());
        model.addAttribute("StudyYears", List.of(1, 2, 3, 4, 5));
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
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
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
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
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "lecturers";
    }

    @GetMapping("/admin")
    public String adminView(Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "admin";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        Model model) {
        SecurityUtils.verifyAdminLogin("admin", "admin");
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "admin";
    }

    @GetMapping("/logout")
    public String logout(Model model) {
        SecurityUtils.logOut();
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        return "admin";
    }

    @GetMapping("/add-classes")
    public String addClassesView(@RequestParam(required = false, defaultValue = "Wydział Inżynierii Elektrycznej i Komputerowej") String selectedDepartment,
                                 @RequestParam(required = false, defaultValue = "Informatyka w Inżynierii Komputerowej") String selectedMajor,
                                 Model model) {
        model.addAttribute("selectedDepartment", selectedDepartment);
        model.addAttribute("selectedMajor", selectedMajor);
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        model.addAttribute("ClassesTypes", Arrays.stream(ClassesType.values()).map(classesType -> classesType.description));
        model.addAttribute("Weekdays", List.of(
                "Poniedziałek",
                "Wtorek",
                "Środa",
                "Czwartek",
                "Piątek",
                "Sobota",
                "Niedziela"
        ));
        model.addAttribute("Departments", departmentService.findAllDepartmentNames());
        model.addAttribute("Classrooms", departmentClassroomService.findAllClassroomsForDepartment(selectedDepartment));
        model.addAttribute("Majors", majorService.findAllMajorNames());
        List<String> groupNames = new ArrayList<>(majorGroupService.findMajorGroupsByMajor(selectedMajor).stream().map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
        groupNames.add("Cały kierunek");
        model.addAttribute("StudyYears", majorGroupService.findAll().stream().map(MajorGroup::getStudyYear).distinct().toList());
        model.addAttribute("Groups", majorGroupService.findMajorGroupsByMajor(selectedMajor).stream().map(majorGroup -> majorGroup.getGroup().getGroupName()).distinct().toList());
        model.addAttribute("Subjects", subjectService.findAllSubjectNames());
        model.addAttribute("SemesterTypes", Arrays.stream(SemesterType.values()).map(SemesterType::getDescription));
        model.addAttribute("AcademicYears", semesterService.findAllAcademicYears());
        model.addAttribute("Frequencies", Arrays.stream(Frequency.values()).map(Frequency::getDescription));
        model.addAttribute("LecturersNames", lecturerService.findAllNames());
        return "add-classes";
    }

    @PostMapping("/add-classes")
    public String addClasses(@RequestParam("classesType") String classesType,
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
                             Model model) {
        model.addAttribute("isAdminLogged", SecurityUtils.isAdminLogged);
        classesController.saveClasses(classesType, dayOfWeek, startTime, endTime, department, classroom, major, studyYear, group, subject, semesterType, isDiploma, academicYear, frequency, lecturersList);
        //TODO teraz mozna by stowrzyc z tego obiekt Classes, przejsc do widoku dodawania zajęć dla studiów stacjonranych albo niestacjonarnych w zaleznosci od wybranego dnia tygodnia
        return "add-classes";
    }
}
