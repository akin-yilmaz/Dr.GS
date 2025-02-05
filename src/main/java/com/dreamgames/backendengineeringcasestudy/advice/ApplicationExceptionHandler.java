package com.dreamgames.backendengineeringcasestudy.advice;

import com.dreamgames.backendengineeringcasestudy.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/customer-not-found"));
        problem.setTitle("Customer Not Found");
        return problem;
    }

    @ExceptionHandler(IllegalDateException.class)
    public ProblemDetail handleException(IllegalDateException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/illegal-date"));
        problem.setTitle("Illegal Date");
        return problem;
    }

    @ExceptionHandler(NewbieException.class)
    public ProblemDetail handleException(NewbieException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/newbie"));
        problem.setTitle("Insufficient level");
        return problem;
    }

    @ExceptionHandler(IllegalEventException.class)
    public ProblemDetail handleException(IllegalEventException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/illegal-event"));
        problem.setTitle("Illegal event id");
        return problem;
    }

    @ExceptionHandler(RequesterAlreadyCollaboratedException.class)
    public ProblemDetail handleException(RequesterAlreadyCollaboratedException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/requester-already-collaborated"));
        problem.setTitle("Requester already collaborated");
        return problem;
    }

    @ExceptionHandler(RequestedAlreadyCollaboratedException.class)
    public ProblemDetail handleException(RequestedAlreadyCollaboratedException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/requested-already-collaborated"));
        problem.setTitle("Requested already collaborated");
        return problem;
    }

    @ExceptionHandler(BalloonAlreadyInflatedException.class)
    public ProblemDetail handleException(BalloonAlreadyInflatedException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/balloon-already-inflated"));
        problem.setTitle("Balloon already inflated");
        return problem;
    }

    @ExceptionHandler(InsufficientHeliumException.class)
    public ProblemDetail handleException(InsufficientHeliumException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/insufficient-helium"));
        problem.setTitle("Insufficient helium");
        return problem;
    }

    @ExceptionHandler(BalloonNotFullyInflatedException.class)
    public ProblemDetail handleException(BalloonNotFullyInflatedException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/balloon-not-fully-inflated"));
        problem.setTitle("Balloon not fully inflated");
        return problem;
    }

    @ExceptionHandler(EventRewardAlreadyTakenException.class)
    public ProblemDetail handleException(EventRewardAlreadyTakenException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.EXPECTATION_FAILED, ex.getMessage());
        problem.setType(URI.create("http://example.com/problems/event-reward-already-taken"));
        problem.setTitle("Event reward already taken");
        return problem;
    }

}
