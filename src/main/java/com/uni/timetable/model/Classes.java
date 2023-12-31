package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
@Entity
@Table(name = "Classes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classes_sequence")
    @SequenceGenerator(name = "classes_sequence", sequenceName = "classes_sequence", allocationSize = 1, initialValue = 10)
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
