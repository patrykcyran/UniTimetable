package com.uni.timetable.service;

import com.uni.timetable.model.Department;
import com.uni.timetable.model.DepartmentClassroom;
import com.uni.timetable.repository.DepartmentClassroomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepartmentClassroomService {

    private final DepartmentClassroomRepository departmentClassroomRepository;

    public DepartmentClassroomService(DepartmentClassroomRepository departmentClassroomRepository) {
        this.departmentClassroomRepository = departmentClassroomRepository;
    }

    public List<DepartmentClassroom> findAll() {
        log.debug("Find all department classrooms");
        List<DepartmentClassroom> departmentClassrooms = departmentClassroomRepository.findAll();
        log.debug("Department classrooms found" + departmentClassrooms);
        return departmentClassrooms;
    }

    public List<String> findAllClassroomsForDepartment(String departmentName) {
        log.debug("Find all department classrooms for " + departmentName);
        List<String> classrooms = departmentClassroomRepository.findByDepartment_DepartmentName(departmentName).stream().map(departmentClassroom -> departmentClassroom.getClassroom().getClassroomName()).toList();
        log.debug("Departments classrooms found" + classrooms);
        return classrooms;
    }
}
