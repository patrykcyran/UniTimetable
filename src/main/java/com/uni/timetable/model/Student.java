package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Student")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @Column(name = "album_number")
    private Long albumNumber;

    @ManyToOne
    @JoinColumn(name = "major_group_id")
    private MajorGroup majorGroup;
}
