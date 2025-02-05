package com.dreamgames.backendengineeringcasestudy.exception;

public class NewbieException extends RuntimeException {
    private static final String MESSAGE = "Your level [level=%d] is less than 50";
    public NewbieException(Integer level) {
        super(MESSAGE.formatted(level));
    }
}
