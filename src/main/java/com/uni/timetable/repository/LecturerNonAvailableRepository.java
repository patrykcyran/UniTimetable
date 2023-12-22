package com.uni.timetable.repository;

import com.uni.timetable.model.LecturerNonAvailable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LecturerNonAvailableRepository extends JpaRepository<LecturerNonAvailable, Long> {
    List<LecturerNonAvailable> findByLecturer_Name(String name);

}
