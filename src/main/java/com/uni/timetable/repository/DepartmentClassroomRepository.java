package com.uni.timetable.repository;

import com.uni.timetable.model.DepartmentClassroom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentClassroomRepository extends JpaRepository<DepartmentClassroom, Long> {
    List<DepartmentClassroom> findByDepartment_DepartmentName(String departmentName);

}
