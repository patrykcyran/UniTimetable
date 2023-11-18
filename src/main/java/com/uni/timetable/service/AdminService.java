package com.uni.timetable.service;

import com.uni.timetable.model.Admin;
import com.uni.timetable.repository.AdminRepository;
import org.apache.catalina.realm.AuthenticatedUserRealm;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final AdminRepository adminRepository;

    public AdminService (AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    public void addAdmin(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin findByUsername(String username) {
        return adminRepository.findByUsername(username);
    }
}
