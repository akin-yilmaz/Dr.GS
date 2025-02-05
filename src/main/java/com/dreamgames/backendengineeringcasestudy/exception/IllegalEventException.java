package com.dreamgames.backendengineeringcasestudy.exception;

public class IllegalEventException extends RuntimeException {
    private static final String MESSAGE = "eventId must be 1 for demo purposes.";
    public IllegalEventException() {
        super(MESSAGE);
    }
}
