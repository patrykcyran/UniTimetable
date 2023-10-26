package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "group_id", nullable = false)
    private Long groupID;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @Column(name = "name")
    private String groupName;
}
