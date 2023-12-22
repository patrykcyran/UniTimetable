package com.uni.timetable.service;

import com.uni.timetable.model.DepartmentClassroom;
import com.uni.timetable.model.Lecturer;
import com.uni.timetable.model.LecturerNonAvailable;
import com.uni.timetable.repository.LecturerNonAvailableRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class LecturerNonAvailableService {

    private final LecturerNonAvailableRepository lecturerNonAvailableRepository;

    public LecturerNonAvailableService(LecturerNonAvailableRepository lecturerNonAvailableRepository) {
        this.lecturerNonAvailableRepository = lecturerNonAvailableRepository;
    }

    public List<LecturerNonAvailable> findAll() {
        List<LecturerNonAvailable> lecturersNonAvailable = lecturerNonAvailableRepository.findAll();
        return lecturersNonAvailable;
    }

    public List<LecturerNonAvailable> findAllByLecturerName(String lecturerName) {
        return lecturerNonAvailableRepository.findByLecturer_Name(lecturerName);
    }

    public LecturerNonAvailable saveLecturerNonAvailable(LocalDate eventDate,
                                                         LocalTime startTime,
                                                         LocalTime endTime,
                                                         Lecturer lecturer) {
        LecturerNonAvailable lecturerNonAvailable = LecturerNonAvailable.builder()
                .eventDate(eventDate)
                .startTime(startTime)
                .endTime(endTime)
                .lecturer(lecturer)
                .build();

        log.debug("One time event to save {} ", lecturerNonAvailable);
        lecturerNonAvailableRepository.save(lecturerNonAvailable);
        return lecturerNonAvailable;
    }
}
