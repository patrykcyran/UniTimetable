package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Subject")
public class Subject {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "name", nullable = false)
    private String name;
}
