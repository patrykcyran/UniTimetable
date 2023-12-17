package com.uni.timetable.repository;

import com.uni.timetable.model.OneTimeEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OneTimeEventRepository extends JpaRepository<OneTimeEvent, Long> {
    List<OneTimeEvent> findByDepartmentClassroom_Department_DepartmentNameAndDepartmentClassroom_Classroom_ClassroomName(String departmentName, String classroomName);

}
