package com.uni.timetable.service;

import com.uni.timetable.model.Classes;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.repository.ClassesRepository;
import com.uni.timetable.repository.SemesterClassesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SemesterClassesService {

    private final SemesterClassesRepository semesterClassesRepository;

    public SemesterClassesService(SemesterClassesRepository semesterClassesRepository) {
        this.semesterClassesRepository = semesterClassesRepository;
    }

    public List<SemesterClasses> findAll() {
        log.debug("Finding all classes");
        List<SemesterClasses> semesterClasses = semesterClassesRepository.findAll();
        log.debug("Semester classes found" + semesterClasses);
        return semesterClasses;
    }
}
