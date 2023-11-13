package com.uni.timetable.repository;

import com.uni.timetable.model.Major;
import com.uni.timetable.model.StudyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MajorRepository extends JpaRepository<Major, Long> {
    Major findByMajorNameAndStudyType(String majorName, StudyType studyType);
}
