package com.uni.timetable.service;

import com.uni.timetable.model.Classroom;
import com.uni.timetable.model.Department;
import com.uni.timetable.repository.ClassroomRepository;
import com.uni.timetable.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClassroomService {
    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public List<Classroom> findAll() {
        log.debug("Find all classrooms");
        List<Classroom> classrooms = classroomRepository.findAll();
        log.debug("Classrooms found" + classrooms);
        return classrooms;
    }

    public List<String> findAllClassroomsNames() {
        log.debug("Find all classrooms");
        List<String> classroomNames = classroomRepository.findAll().stream().map(Classroom::getClassroomName).toList();
        log.debug("Classrooms found" + classroomNames);
        return classroomNames;
    }
}
