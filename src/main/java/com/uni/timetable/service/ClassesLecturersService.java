package com.uni.timetable.service;

import com.uni.timetable.model.*;
import com.uni.timetable.repository.ClassesLecturersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class ClassesLecturersService {

    private final ClassesLecturersRepository classesLecturersRepository;

    public ClassesLecturersService(ClassesLecturersRepository classesLecturersRepository) {
        this.classesLecturersRepository = classesLecturersRepository;
    }

    public List<ClassesLecturers> findAll() {
        log.debug("Finding all classes lecturers");
        List<ClassesLecturers> classesLecturers = classesLecturersRepository.findAll();
        log.debug("Classes lecturers classes found" + classesLecturers);
        return classesLecturers;
    }

    public List<ClassesLecturers> findByLecturerName(String name) {
        log.debug("Finding classes lecturers by name {}", name);
        List<ClassesLecturers> classesLecturers = classesLecturersRepository.findByLecturer_Name(name);
        log.debug("Classes lecturers classes found" + classesLecturers);
        return classesLecturers;
    }

    public List<Classes> findClassesByLecturerName(String name) {
        log.debug("Finding classes by name {}", name);
        List<Classes> classes = classesLecturersRepository.findByLecturer_Name(name).stream().map(ClassesLecturers::getClasses).toList();
        log.debug("Classes classes found" + classes);
        return classes;
    }

    public List<Lecturer> findAllLecturersByClasses(Long classesId) {
        return classesLecturersRepository.findByClasses_ClassesId(classesId).stream().map(ClassesLecturers::getLecturer).toList();
    }

    public List<ClassesLecturers> findAllClassesLecturersByClasses(Long classesId) {
        return classesLecturersRepository.findByClasses_ClassesId(classesId).stream().toList();
    }

    public Boolean doesClassesLecturerExists(MajorGroup majorGroup, Subject subject, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, ClassesType classesType, DepartmentClassroom departmentClassroom, String name) {
        return classesLecturersRepository.existsByClasses_MajorGroupAndClasses_SubjectAndClasses_DayOfWeekAndClasses_StartTimeAndClasses_EndTimeAndClasses_ClassesTypeAndClasses_DepartmentClassroomAndLecturer_Name(majorGroup, subject, dayOfWeek, startTime, endTime, classesType, departmentClassroom, name);
    }

    public ClassesLecturers saveClassesLecturers(Classes classes, Lecturer lecturer) {
        ClassesLecturers classesLecturer = ClassesLecturers.builder()
                .classes(classes)
                .lecturer(lecturer)
                .build();
        log.debug("Classes lecturer to save {} ", classesLecturer);
        classesLecturersRepository.save(classesLecturer);
        return classesLecturer;
    }

    public void deleteClassesLecturers(Long classesLecturersId) {
        classesLecturersRepository.deleteByClassesLecturersId(classesLecturersId);
    }
}
