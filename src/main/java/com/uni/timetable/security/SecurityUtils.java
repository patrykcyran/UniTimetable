package com.uni.timetable.security;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class SecurityUtils {

    public static boolean isAdminLogged = true;

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
}
