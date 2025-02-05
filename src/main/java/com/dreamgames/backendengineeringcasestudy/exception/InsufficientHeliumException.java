package com.dreamgames.backendengineeringcasestudy.exception;

public class InsufficientHeliumException extends RuntimeException {
    private static final String MESSAGE = "You do not have this much helium";
    public InsufficientHeliumException() {
        super(MESSAGE.formatted());
    }
}
