package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Classes_Lecturer")
public class ClassesLecturers {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "classes_lecturers_id", nullable = false)
    private Long classesLecturersId;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private Classes classes;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
