package com.uni.timetable.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @Column(name = "user_id")
    private Integer userId;

    @Column
    private String username;

    @Column
    private String password;
}
