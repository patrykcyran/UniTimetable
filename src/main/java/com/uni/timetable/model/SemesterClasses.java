package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Semester_Classes")
public class SemesterClasses {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "semmester_classes_id", nullable = false)
    private Long semesterClassesId;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private Classes classes;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;
}
