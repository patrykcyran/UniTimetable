package com.uni.timetable.repository;

import com.uni.timetable.model.ClassesLecturers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassesLecturersRepository extends JpaRepository<ClassesLecturers, Long> {
    List<ClassesLecturers> findByLecturer_Name(String name);

}
