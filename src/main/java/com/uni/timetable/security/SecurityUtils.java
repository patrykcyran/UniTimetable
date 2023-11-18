package com.uni.timetable.security;

import com.uni.timetable.model.Admin;
import com.uni.timetable.model.AdminRequest;
import com.uni.timetable.service.AdminService;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import static org.apache.commons.codec.digest.DigestUtils.sha256Hex;

@Data
@Slf4j
public class SecurityUtils {

    private static final String TOKEN = "X1xSUHDhVXq2iPZuMmEVjBTOAXOdWs9WVwIbdkJ04qWVkoDuJUO2YoRveX2MzJxp";
    @Setter
    @Getter
    private static boolean isAdminLogged = false;

    public static void verifyAdminLogin(String username, String password) {
        if (!isAdminLogged) {
            if ("admin".equals(username) && "admin".equals(password)) {
                isAdminLogged = true;
                log.info("User is logged as admin");
            }
        }
    }

    public static void logOut() {
        isAdminLogged = false;
        log.info("User logged out");
    }

    public static ResponseEntity<Admin> register(String signature, AdminRequest adminRequest) {

        String hmac = new HmacUtils("HmacSHA256", TOKEN).hmacHex(TOKEN);
        if (!signature.equals(hmac)) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Admin admin = new Admin();
        admin.setUsername(adminRequest.getUsername());
        admin.setPassword(sha256Hex(adminRequest.getPassword()));

        return new ResponseEntity<>(admin, HttpStatus.CREATED);
    }

    public static boolean checkAdminCredentials(AdminRequest adminRequest, Admin adminFromDb) {
        String requestEncryptedPassword = sha256Hex(adminRequest.getPassword());
        String adminEncryptedPassword = adminFromDb.getPassword();
        return requestEncryptedPassword.equals(adminEncryptedPassword);
    }
}
