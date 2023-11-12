package com.uni.timetable.repository;

import com.uni.timetable.model.Semester;
import com.uni.timetable.model.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterRepository extends JpaRepository<Semester, Long> {
    Semester findByAcademicYearAndSemesterTypeAndIsDiploma(String academicYear, SemesterType semesterType, Boolean isDiploma);

}
