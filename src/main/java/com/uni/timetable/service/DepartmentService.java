package com.uni.timetable.service;

import com.uni.timetable.model.Department;
import com.uni.timetable.repository.DepartmentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        log.debug("Find all departments");
        List<Department> departments = departmentRepository.findAll();
        log.debug("Departments found" + departments);
        return departments;
    }

    public List<String> findAllDepartmentNames() {
        log.debug("Find all departments");
        List<String> departmentNames = departmentRepository.findAll().stream().map(Department::getDepartmentName).toList();
        log.debug("Departments found" + departmentNames);
        return departmentNames;
    }
}
