package com.uni.timetable.repository;

import com.uni.timetable.model.Classes;
import com.uni.timetable.model.Lecturer;
import com.uni.timetable.model.SemesterNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesRepository extends JpaRepository<Classes, Long> {

    List<Classes> findByLecturerId_LecturerId(Long lecturerId);

    List<Classes> findByDepartmentClassroom_Classroom_ClassroomNameAndDepartmentClassroom_Department_DepartmentName(String classroomName, String departmentName);

    List<Classes> findByMajorGroup_Group_GroupNameAndMajorGroup_Group_SemesterNumber(String groupName, SemesterNumber semesterNumber);

    List<Classes> findByLecturerId_Name(String name);

}
