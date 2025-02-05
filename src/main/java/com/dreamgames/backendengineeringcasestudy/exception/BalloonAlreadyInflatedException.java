package com.dreamgames.backendengineeringcasestudy.exception;

public class BalloonAlreadyInflatedException extends RuntimeException {
    private static final String MESSAGE = "Balloon can not be inflated more.";
    public BalloonAlreadyInflatedException() {
      super(MESSAGE.formatted());
    }
}
