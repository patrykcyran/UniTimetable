package com.uni.timetable.controller;

import com.uni.timetable.model.Admin;
import com.uni.timetable.model.AdminRequest;
import com.uni.timetable.security.SecurityUtils;
import com.uni.timetable.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    public AdminController (AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@RequestHeader String signature,
            @RequestBody AdminRequest adminRequest) {
        HttpStatus httpsStatus = SecurityUtils.register(signature, adminRequest);

        return new ResponseEntity<>(httpsStatus);
    }
}
