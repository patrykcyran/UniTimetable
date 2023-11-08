package com.uni.timetable.repository;

import com.uni.timetable.model.Classes;
import com.uni.timetable.model.Major;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.model.SemesterType;
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



    @Query("SELECT sc.classes FROM SemesterClasses sc " +
            "WHERE sc.semester.academicYear = :academicYear " +
            "AND sc.semester.semesterType = :semesterType " +
            "AND sc.classes.lecturerId.name = :name")
    List<Classes> findClassesByAcademicYearSemesterTypeAndLecturerName(@Param("academicYear") String academicYear,
                                        @Param("semesterType") SemesterType semesterType,
                                        @Param("name") String name);
}
