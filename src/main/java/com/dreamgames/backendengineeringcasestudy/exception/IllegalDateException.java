package com.dreamgames.backendengineeringcasestudy.exception;

public class IllegalDateException extends RuntimeException {
    private static final String MESSAGE = "Event is held between 08.00 - 22.00 (UTC)";

    public IllegalDateException() {
        super(MESSAGE);
    }
}
