package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Classes")
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "classes_id", nullable = false)
    private Long classesId;

    @ManyToOne
    @JoinColumn(name = "major_group_id")
    private MajorGroup majorGroup;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "classes_type")
    private ClassesType classesType;

    @ManyToOne
    @JoinColumn(name = "department_classroom_id")
    private DepartmentClassroom departmentClassroom;
}
