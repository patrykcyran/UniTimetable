package com.uni.timetable.repository;

import com.uni.timetable.model.PartTimeSemesterClasses;
import com.uni.timetable.model.SemesterClasses;
import com.uni.timetable.model.SemesterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartTimeSemesterClassesRepository extends JpaRepository<PartTimeSemesterClasses, Long> {
    List<PartTimeSemesterClasses> findByClasses_MajorGroup_Major_MajorNameAndClasses_MajorGroup_StudyYearAndSemester_SemesterType(String majorName, Integer studyYear, SemesterType semesterType);


}
