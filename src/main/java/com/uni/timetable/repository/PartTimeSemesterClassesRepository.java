package com.uni.timetable.repository;

import com.uni.timetable.model.Classes;
import com.uni.timetable.model.PartTimeSemesterClasses;
import com.uni.timetable.model.Semester;
import com.uni.timetable.model.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PartTimeSemesterClassesRepository extends JpaRepository<PartTimeSemesterClasses, Long> {
    PartTimeSemesterClasses findByPartTimeSemesterClassesId(Long partTimeSemesterClassesId);
    @Transactional
    long deleteByPartTimeSemesterClassesId(Long partTimeSemesterClassesId);
    List<PartTimeSemesterClasses> findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterType(String majorName, Integer studyYear, SemesterType semesterType);

    @Transactional
    @Modifying
    @Query("""
            update PartTimeSemesterClasses p set p.semester = ?1, p.classes = ?2, p.classesDate = ?3
            where p.partTimeSemesterClassesId = ?4""")
    int updateSemesterAndClassesAndClassesDateByPartTimeSemesterClassesId(Semester semester, Classes classes, LocalDate classesDate, Long partTimeSemesterClassesId);

    List<PartTimeSemesterClasses> findBySemester_AcademicYearAndSemester_SemesterType(String academicYear, SemesterType semesterType);
}
