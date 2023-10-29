package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Classroom")
public class Classroom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "classroom_id", nullable = false)
    private Long classroomId;

    @Column(name = "classroom_name")
    private String classroomName;
}
