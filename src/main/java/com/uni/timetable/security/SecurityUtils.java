package com.uni.timetable.security;

import com.uni.timetable.model.Admin;
import com.uni.timetable.model.AdminRequest;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.HmacUtils;
import org.springframework.http.HttpStatus;

@Data
@Slf4j
public class SecurityUtils {

    private static final String TOKEN = "X1xSUHDhVXq2iPZuMmEVjBTOAXOdWs9WVwIbdkJ04qWVkoDuJUO2YoRveX2MzJxp";
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

    public static HttpStatus register(String signature, AdminRequest adminRequest) {

        String hmac = new HmacUtils("HmacSHA256", TOKEN).hmacHex(TOKEN);
        if (!signature.equals(hmac)) {
            return HttpStatus.UNAUTHORIZED;
        }


        return HttpStatus.CREATED;
    }
}
