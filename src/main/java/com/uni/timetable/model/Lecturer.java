package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Lecturer")
public class Lecturer {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "lecturer_id", nullable = false)
    private Long lecturerId;

    @Column
    private String name;

    @Column(name = "academic_title")
    private AcademicTitle academicTitle;
}
