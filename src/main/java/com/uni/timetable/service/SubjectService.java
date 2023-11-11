package com.uni.timetable.service;

import com.uni.timetable.model.Department;
import com.uni.timetable.model.Subject;
import com.uni.timetable.repository.DepartmentRepository;
import com.uni.timetable.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubjectService {

    private final SubjectRepository subjectRepository;

    public SubjectService(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    public List<Subject> findAll() {
        log.debug("Find all subjects");
        List<Subject> subjects = subjectRepository.findAll();
        log.debug("Subjects found" + subjects);
        return subjects;
    }

    public List<String> findAllSubjectNames() {
        log.debug("Find all subject names");
        List<String> departmentNames = subjectRepository.findAll().stream().map(Subject::getSubjectName).toList();
        log.debug("Subject names found" + departmentNames);
        return departmentNames;
    }

    public Subject findSubjectByName(String subjectName) {
        Subject subject = subjectRepository.findBySubjectName(subjectName);
        log.debug("Subject found by name {} ", subject);
        return subject;
    }
}
