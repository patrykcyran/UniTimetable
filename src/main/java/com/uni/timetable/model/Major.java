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

    @Column(name = "name", nullable = false)
    private String majorName;
}
