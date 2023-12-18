package com.uni.timetable.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TimetableException.class)
    public ResponseEntity<String> handleTimetableException(TimetableException e) {
        String errorMessage = "Error: " + e.getMessage();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorMessage);
    }
}
