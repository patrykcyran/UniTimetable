package com.uni.timetable.repository;

import com.uni.timetable.model.MajorGroup;
import com.uni.timetable.model.StudyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MajorGroupRepository extends JpaRepository<MajorGroup, Long> {
    MajorGroup findByMajor_MajorNameAndGroup_GroupName(String majorName, String groupName);

    List<MajorGroup> findByMajor_MajorName(String majorName);

    List<MajorGroup> findByMajor_MajorNameAndStudyYear(String majorName, Integer studyYear);

    MajorGroup findByMajor_MajorNameAndStudyYearAndGroup_GroupName(String majorName, Integer studyYear, String groupName);

    List<MajorGroup> findByMajor_MajorNameAndStudyYearAndGroup_GroupNameAndMajor_StudyType(String majorName, Integer studyYear, String groupName, StudyType studyType);


}
