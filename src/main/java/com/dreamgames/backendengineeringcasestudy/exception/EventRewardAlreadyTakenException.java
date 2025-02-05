package com.dreamgames.backendengineeringcasestudy.exception;

public class EventRewardAlreadyTakenException extends RuntimeException {
  private static final String MESSAGE = "Event reward already taken.";
    public EventRewardAlreadyTakenException() {
        super(MESSAGE.formatted());
    }
}
