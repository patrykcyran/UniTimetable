package com.uni.timetable.repository;

import com.uni.timetable.model.LecturerNonAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerNonAvailableRepository extends JpaRepository<LecturerNonAvailable, Long> {
}
