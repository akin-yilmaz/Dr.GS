package com.dreamgames.backendengineeringcasestudy.exception;

public class RequestedAlreadyCollaboratedException extends RuntimeException {
    private static final String MESSAGE = "Requested user has just collaborated.";
    public RequestedAlreadyCollaboratedException() {
        super(MESSAGE.formatted());
    }
}
