package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "Part_Time_Semester_Classes")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartTimeSemesterClasses {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "part_time_semester_classes_id", nullable = false)
    private Long partTimeSemesterClassesId;

    @ManyToOne
    @JoinColumn(name = "semester_id")
    private Semester semester;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private Classes classes;

    @Column(name = "classes_date")
    private LocalDate classesDate;
}
