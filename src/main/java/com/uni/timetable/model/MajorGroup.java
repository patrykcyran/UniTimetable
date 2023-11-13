package com.uni.timetable.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "Major_Group")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MajorGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "major_group_sequence")
    @SequenceGenerator(name = "major_group_sequence", sequenceName = "major_group_sequence", allocationSize = 1, initialValue = 10)
    @Column(name = "major_group_id", nullable = false)
    private Long majorGroupId;

    @ManyToOne
    @JoinColumn(name = "major_id")
    private Major major;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "study_year")
    private Integer studyYear;
}
