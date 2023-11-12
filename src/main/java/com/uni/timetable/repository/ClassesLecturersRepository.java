package com.uni.timetable.repository;

import com.uni.timetable.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ClassesLecturersRepository extends JpaRepository<ClassesLecturers, Long> {
    @Transactional
    @Modifying
    @Query("delete from ClassesLecturers c where c.classesLecturersId = ?1")
    int deleteByClassesLecturersId(Long classesLecturersId);
    List<ClassesLecturers> findByLecturer_Name(String name);

    boolean existsByClasses_MajorGroupAndClasses_SubjectAndClasses_DayOfWeekAndClasses_StartTimeAndClasses_EndTimeAndClasses_ClassesTypeAndClasses_DepartmentClassroomAndLecturer_Name(MajorGroup majorGroup, Subject subject, DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime, ClassesType classesType, DepartmentClassroom departmentClassroom, String name);

    List<ClassesLecturers> findByClasses_ClassesId(Long classesId);


}
