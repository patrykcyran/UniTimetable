package com.uni.timetable.service;

import com.uni.timetable.model.PartTimeSemesterClasses;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.model.SemesterType;
import com.uni.timetable.repository.PartTimeSemesterClassesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class PartTimeSemesterClassesService {

    private final PartTimeSemesterClassesRepository partTimeSemesterClassesRepository;

    public PartTimeSemesterClassesService(PartTimeSemesterClassesRepository partTimeSemesterClassesRepository) {
        this.partTimeSemesterClassesRepository = partTimeSemesterClassesRepository;
    }

    public List<PartTimeSemesterClasses> findAll() {
        log.debug("Finding all classes");
        List<PartTimeSemesterClasses> semesterClasses = partTimeSemesterClassesRepository.findAll();
        log.debug("Semester classes found" + semesterClasses);
        return semesterClasses;
    }

    public List<PartTimeSemesterClasses> findPartTimeSemesterClassesByMajorAndSemester(String majorName, String studyYear, SemesterType semesterType) {
        log.debug("Finding part time classes by major name, semester type, study year " + majorName + ", " + semesterType + ", " + studyYear);
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesRepository.findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterType(majorName, Integer.valueOf(studyYear), semesterType);
        log.debug("Classes found {}", partTimeSemesterClasses);
        return partTimeSemesterClasses;
    }

    public List<PartTimeSemesterClasses> findPartTimeSemesterClassesBySemester(String studyYear, SemesterType semesterType) {
        log.debug("Finding part time classes by semester type and study year " + semesterType + ", " + studyYear);
        List<PartTimeSemesterClasses> partTimeSemesterClasses = partTimeSemesterClassesRepository.findBySemester_AcademicYearAndSemester_SemesterType(studyYear, semesterType);
        log.debug("Classes found {}", partTimeSemesterClasses);
        return partTimeSemesterClasses;
    }
}
