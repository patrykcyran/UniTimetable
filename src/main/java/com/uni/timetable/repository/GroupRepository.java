package com.uni.timetable.repository;

import com.uni.timetable.model.Group;
import com.uni.timetable.model.SemesterNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    Group findByGroupNameAndSemesterNumber(String groupName, SemesterNumber semesterNumber);
}
