package com.uni.timetable.exception;

public class TimetableException extends RuntimeException {
    public TimetableException(String message) {
        super(message);
    }

    public TimetableException(String message, RuntimeException e) {
        super(message, e);
    }
}
