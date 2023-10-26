package com.uni.timetable.repository;

import com.uni.timetable.model.Lecturer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LecturerRepository  extends JpaRepository<Lecturer, Long> {
}
