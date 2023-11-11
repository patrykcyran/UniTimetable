package com.uni.timetable.service;

import com.uni.timetable.model.*;
import com.uni.timetable.repository.ClassesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class ClassesService {

    private final ClassesRepository classesRepository;

    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    public List<Classes> findAll() {
        log.debug("Finding all classes");
        List<Classes> classes = classesRepository.findAll();
        log.debug("Classes found" + classes);
        return classes;
    }

    public List<Classes> findByGroupAndSemesterNumber(String groupName, String semesterNumberString) {
        log.debug("Finding classes by group name " + groupName + " and semester number " + semesterNumberString);
        SemesterNumber semesterNumber = SemesterNumber.valueOf(semesterNumberString);
        List<Classes> classesFound = classesRepository.findByMajorGroup_Group_GroupNameAndMajorGroup_Group_SemesterNumber(groupName, semesterNumber);
        log.debug("Classes found" + classesFound);
        return classesFound;
    }

    public List<Classes> findByLecturerId(Long lecturerId) {
        log.debug("Finding classes by lecturer id " + lecturerId);
        //List<Classes> classesFound = classesRepository.findByLecturerId_LecturerId(lecturerId);
        //log.debug("Classes found" + classesFound);
        return null;
    }

    public List<Classes> findByClassroom(String classroomName, String departmentName) {
        log.debug("Finding classes by classroom " + classroomName + " and department " + departmentName);
        List<Classes> classesFound = classesRepository.findByDepartmentClassroom_Classroom_ClassroomNameAndDepartmentClassroom_Department_DepartmentName(classroomName, departmentName);
        log.debug("Classes found" + classesFound);
        return classesFound;
    }

    public List<Classes> findByLecturerName(String lecturerName) {
        log.debug("Finding classes by lecturer name " + lecturerName);
/*        List<Classes> classesFound = classesRepository.findByLecturerId_Name(lecturerName);
        log.debug("Classes found" + classesFound);*/
        return null;
    }

    public void saveClasses(MajorGroup majorGroup,
                            Subject subject,
                            DayOfWeek dayOfWeek,
                            LocalTime startTime,
                            LocalTime endTime,
                            ClassesType classesType,
                            DepartmentClassroom departmentClassroom) {
        Classes classes = Classes.builder()
                .majorGroup(majorGroup)
                .subject(subject)
                .dayOfWeek(dayOfWeek)
                .startTime(startTime)
                .endTime(endTime)
                .classesType(classesType)
                .departmentClassroom(departmentClassroom)
                .build();

        log.debug("Classes to save {} ", classes);
        classesRepository.save(classes);
    }


}
