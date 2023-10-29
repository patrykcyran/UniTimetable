package com.uni.timetable.repository;

import com.uni.timetable.model.SemesterClasses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SemesterClassesRepository extends JpaRepository<SemesterClasses, Long> {
}
