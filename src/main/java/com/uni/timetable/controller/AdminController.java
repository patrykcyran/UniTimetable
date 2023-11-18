package com.uni.timetable.controller;

import com.uni.timetable.model.Admin;
import com.uni.timetable.model.AdminRequest;
import com.uni.timetable.security.SecurityUtils;
import com.uni.timetable.service.AdminService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import static java.util.Objects.isNull;

@Controller
@RequestMapping("/adminController")
public class AdminController {

    private final AdminService adminService;

    public AdminController (AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/register")
    public ResponseEntity<Admin> registerAdmin(@RequestHeader String signature,
            @RequestBody AdminRequest adminRequest) {
        ResponseEntity<Admin> responseEntity = SecurityUtils.register(signature, adminRequest);
        adminService.addAdmin(responseEntity.getBody());
        return responseEntity;
    }

    @PostMapping("/authorize")
    public ResponseEntity<String> verifyLogin(@RequestBody AdminRequest adminRequest) {
        Admin adminFromDb = adminService.findByUsername(adminRequest.getUsername());
        if (isNull(adminFromDb)) {
            return new ResponseEntity<>("Podany login nie istnieje", HttpStatus.NOT_FOUND);
        }
        boolean areCredentialsCorrect = SecurityUtils.checkAdminCredentials(adminRequest, adminFromDb);
        if (!areCredentialsCorrect) {
            return new ResponseEntity<>("Podano błędne hasło", HttpStatus.NOT_FOUND);
        }
        SecurityUtils.setAdminLogged(areCredentialsCorrect);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
