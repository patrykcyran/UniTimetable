package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Major")
public class Major {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "major_id", nullable = false)
    private Long majorID;

    @Column(name = "major_name", nullable = false)
    private String majorName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Enumerated(EnumType.STRING)
    @Column(name = "study_type")
    private StudyType studyType;

    @Enumerated(EnumType.STRING)
    @Column(name = "major_type")
    private MajorType majorType;
}
