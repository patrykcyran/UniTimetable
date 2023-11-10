package com.uni.timetable.service;

import com.uni.timetable.model.Classes;
import com.uni.timetable.model.ClassesLecturers;
import com.uni.timetable.repository.ClassesLecturersRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
}
