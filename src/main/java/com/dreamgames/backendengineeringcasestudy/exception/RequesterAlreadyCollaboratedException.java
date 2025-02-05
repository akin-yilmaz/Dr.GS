package com.dreamgames.backendengineeringcasestudy.exception;

public class RequesterAlreadyCollaboratedException extends RuntimeException {
    private static final String MESSAGE = "You are already collaborated.";
    public RequesterAlreadyCollaboratedException() {
        super(MESSAGE.formatted());
    }
}
