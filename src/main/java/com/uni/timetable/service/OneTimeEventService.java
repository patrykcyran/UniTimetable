package com.uni.timetable.service;

import com.uni.timetable.model.DepartmentClassroom;
import com.uni.timetable.model.OneTimeEvent;
import com.uni.timetable.repository.OneTimeEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@Slf4j
public class OneTimeEventService {

    private final OneTimeEventRepository oneTimeEventRepository;

    public OneTimeEventService(OneTimeEventRepository oneTimeEventRepository) {
        this.oneTimeEventRepository = oneTimeEventRepository;
    }

    public List<OneTimeEvent> findAll() {
        log.debug("Finding all one time events");
        List<OneTimeEvent> oneTimeEvents = oneTimeEventRepository.findAll();
        log.debug("One time events found" + oneTimeEvents);
        return oneTimeEvents;
    }

    public List<OneTimeEvent> getOneTimeEventsByDepartmentAndClassroom(String departmentName, String classroomName) {
        return oneTimeEventRepository.findByDepartmentClassroom_Department_DepartmentNameAndDepartmentClassroom_Classroom_ClassroomName(departmentName, classroomName);
    }

    public OneTimeEvent saveOneTimeEvent(LocalDate eventDate,
                                         LocalTime startTime,
                                         LocalTime endTime,
                                         DepartmentClassroom departmentClassroom) {
        OneTimeEvent oneTimeEvent = OneTimeEvent.builder()
                .eventDate(eventDate)
                .startTime(startTime)
                .endTime(endTime)
                .departmentClassroom(departmentClassroom)
                .build();

        log.debug("One time event to save {} ", oneTimeEvent);
        oneTimeEventRepository.save(oneTimeEvent);
        return oneTimeEvent;
    }

    public void deleteOneTimeEventById(Long id) {
        oneTimeEventRepository.deleteById(id);
    }
}
