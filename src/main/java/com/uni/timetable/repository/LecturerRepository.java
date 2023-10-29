package com.uni.timetable.repository;

import com.uni.timetable.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LecturerRepository  extends JpaRepository<Lecturer, Long> {
}
