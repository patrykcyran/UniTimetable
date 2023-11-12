package com.uni.timetable.service;

import com.uni.timetable.model.Semester;
import com.uni.timetable.model.SemesterType;
import com.uni.timetable.repository.SemesterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SemesterService {

    private final SemesterRepository semesterRepository;

    public SemesterService(SemesterRepository semesterRepository) {
        this.semesterRepository = semesterRepository;
    }

    public List<Semester> findAll() {
        log.debug("Finding all semesters");
        List<Semester> semesters = semesterRepository.findAll();
        log.debug("Semesters found" + semesters);
        return semesters;
    }

    public List<String> findAllAcademicYears() {
        log.debug("Finding all academic years");
        List<String> academicYears = semesterRepository.findAll().stream().map(Semester::getAcademicYear).distinct().toList();
        log.debug("Academic years found" + academicYears);
        return academicYears;
    }

    public Semester findSemesterByYearTypeAndDiploma(String academicYear, SemesterType semesterType, Boolean isDiploma) {
        Semester semester = semesterRepository.findByAcademicYearAndSemesterTypeAndIsDiploma(academicYear, semesterType, isDiploma);
        log.debug("Semester found by year {} type {} and isDiploma {} : {}", academicYear, semesterType, isDiploma, semester);
        return semester;
    }
}
