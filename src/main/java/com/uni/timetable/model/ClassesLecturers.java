package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Classes_Lecturer")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassesLecturers {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "classes_lecturers_sequence")
    @SequenceGenerator(name = "classes_lecturers_sequence", sequenceName = "classes_lecturers_sequence", allocationSize = 1, initialValue = 10)
    @Column(name = "classes_lecturers_id", nullable = false)
    private Long classesLecturersId;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private Classes classes;

    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private Lecturer lecturer;
}
