package com.uni.timetable.service;

import com.uni.timetable.model.Lecturer;
import com.uni.timetable.repository.LecturerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LecturerService {
    private final LecturerRepository lecturerRepository;

    public LecturerService(LecturerRepository lecturerRepository) {
        this.lecturerRepository = lecturerRepository;
    }

    public List<Lecturer> findAll() {
        log.debug("Finding all lecturers");
        List<Lecturer> lecturers = lecturerRepository.findAll();
        log.debug("Lecturers found" + lecturers);
        return lecturers;
    }

    public List<String> findAllPlainNames() {
        log.debug("Finding all lecturers");
        List<String> lecturers = lecturerRepository.findAll().stream().map(Lecturer::getName).distinct().toList();
        log.debug("Lecturers found" + lecturers);
        return lecturers;
    }

    public List<String> findAllNames() {
        List<Lecturer> lecturers = lecturerRepository.findAll();
        List<String> lecturersString = new ArrayList<>();
        for (Lecturer lecturer : lecturers) {
            lecturersString.add(lecturer.getAcademicTitle() + " " + lecturer.getName());
        }
        return lecturersString;
    }

    public Lecturer findLecturerByName(String lecturerName) {
        return lecturerRepository.findByName(lecturerName);
    }
}
