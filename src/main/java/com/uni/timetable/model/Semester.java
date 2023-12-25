package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Semester")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Semester {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "semester_sequence")
    @SequenceGenerator(name = "semester_sequence", sequenceName = "semester_sequence", allocationSize = 1, initialValue = 10)
    @Column(name = "semester_id", nullable = false)
    private Long semesterId;

    @Column(name = "academic_year")
    private String academicYear;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester_type")
    private SemesterType semesterType;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "is_diploma")
    private Boolean isDiploma;

    @Column(name = "is_custom")
    private Boolean isCustom;
}
