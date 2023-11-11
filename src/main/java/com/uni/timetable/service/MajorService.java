package com.uni.timetable.service;

import com.uni.timetable.model.Major;
import com.uni.timetable.model.StudyType;
import com.uni.timetable.repository.MajorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MajorService {
    private final MajorRepository majorRepository;

    public MajorService(MajorRepository majorRepository) {
        this.majorRepository = majorRepository;
    }

    public List<Major> findAll() {
        log.debug("Finding all semesters");
        List<Major> majors = majorRepository.findAll();
        log.debug("Majors found" + majors);
        return majors;
    }

    public List<String> findAllMajorNames() {
        log.debug("Finding all academic years");
        List<String> majorNames = majorRepository.findAll().stream().map(Major::getMajorName).distinct().toList();
        log.debug("Major names found" + majorNames);
        return majorNames;
    }

    public List<String> findAllFullTimeMajorNames() {
        log.debug("Finding all full time major names");
        List<String> majorNames = majorRepository.findAll().stream().filter(major -> major.getStudyType().equals(StudyType.FULL_TIME)).map(Major::getMajorName).distinct().toList();
        log.debug("Major names found" + majorNames);
        return majorNames;
    }

    public List<String> findAllPartTimeMajorNames() {
        log.debug("Finding all part time major names");
        List<String> majorNames = majorRepository.findAll().stream().filter(major -> major.getStudyType().equals(StudyType.PART_TIME)).map(Major::getMajorName).distinct().toList();
        log.debug("Major names found" + majorNames);
        return majorNames;
    }
}
