package com.uni.timetable.repository;

import com.uni.timetable.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface SemesterClassesRepository extends JpaRepository<SemesterClasses, Long> {
    @Transactional
    @Modifying
    @Query("""
            update SemesterClasses s set s.semester = ?1, s.classes = ?2, s.frequency = ?3
            where s.semesterClassesId = ?4""")
    int updateSemesterAndClassesAndFrequencyBySemesterClassesId(Semester semester, Classes classes, Frequency frequency, Long semesterClassesId);
    @Transactional
    long deleteBySemesterClassesId(Long semesterClassesId);
    //List<SemesterClasses> findBySemester_AcademicYearAndSemester_SemesterTypeAndClasses_LecturerId_Name(String academicYear, SemesterType semesterType, String name);

    List<SemesterClasses> findBySemester_AcademicYearAndSemester_SemesterType(String academicYear, SemesterType semesterType);

    List<SemesterClasses> findByClasses_MajorGroup_StudyYearAndSemester_SemesterType(Integer studyYear, SemesterType semesterType);

    List<SemesterClasses> findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterTypeAndClasses_MajorGroup_Major_StudyType(String majorName, Integer studyYear, SemesterType semesterType, StudyType studyType);

    List<SemesterClasses> findByClasses_DepartmentClassroom_Department_DepartmentNameAndClasses_DepartmentClassroom_Classroom_ClassroomName(String departmentName, String classroomName);

    SemesterClasses findBySemesterClassesId(Long semesterClassesId);
}
