package com.uni.timetable.model;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class AdminRequest {
    private String username;
    private String password;
}
