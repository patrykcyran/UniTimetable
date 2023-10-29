package com.uni.timetable.service;

import com.uni.timetable.model.Classes;
import com.uni.timetable.repository.ClassesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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

    public List<Classes> findByGroup() {
        return null;
    }
}
