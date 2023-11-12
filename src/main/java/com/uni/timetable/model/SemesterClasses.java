package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Semester_Classes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SemesterClasses {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classes_sequence")
    @SequenceGenerator(name = "classes_sequence", sequenceName = "classes_sequence", allocationSize = 1, initialValue = 10)
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
