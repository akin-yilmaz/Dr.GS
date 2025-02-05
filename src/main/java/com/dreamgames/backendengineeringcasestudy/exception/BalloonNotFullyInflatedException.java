package com.dreamgames.backendengineeringcasestudy.exception;

public class BalloonNotFullyInflatedException extends RuntimeException {
    private static final String MESSAGE = "Balloon is not fully inflated.";
    public BalloonNotFullyInflatedException() {
        super(MESSAGE.formatted());
    }
}
