package com.uni.timetable.security;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

class SecurityUtilsTest {

    @Test
    void registerUserTest() {
        SecurityUtils.register("", null);
    }
}