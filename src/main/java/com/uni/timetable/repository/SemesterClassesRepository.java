package com.uni.timetable.repository;

import com.uni.timetable.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SemesterClassesRepository extends JpaRepository<SemesterClasses, Long> {
    List<SemesterClasses> findBySemester_AcademicYearAndSemester_SemesterTypeAndClasses_LecturerId_Name(String academicYear, SemesterType semesterType, String name);

    List<SemesterClasses> findBySemester_AcademicYearAndSemester_SemesterType(String academicYear, SemesterType semesterType);

    List<SemesterClasses> findByClasses_MajorGroup_StudyYearAndSemester_SemesterType(Integer studyYear, SemesterType semesterType);

    List<SemesterClasses> findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterTypeAndClasses_MajorGroup_Major_StudyType(String majorName, Integer studyYear, SemesterType semesterType, StudyType studyType);

    List<SemesterClasses> findByClasses_DepartmentClassroom_Department_DepartmentNameAndClasses_DepartmentClassroom_Classroom_ClassroomName(String departmentName, String classroomName);



    @Query("SELECT sc.classes FROM SemesterClasses sc " +
            "WHERE sc.semester.academicYear = :academicYear " +
            "AND sc.semester.semesterType = :semesterType " +
            "AND sc.classes.lecturerId.name = :name")
    List<Classes> findClassesByAcademicYearSemesterTypeAndLecturerName(@Param("academicYear") String academicYear,
                                        @Param("semesterType") SemesterType semesterType,
                                        @Param("name") String name);
}
