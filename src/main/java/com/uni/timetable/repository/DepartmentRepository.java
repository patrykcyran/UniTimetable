package com.uni.timetable.repository;

import com.uni.timetable.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Department findByDepartmentName(String departmentName);
}
