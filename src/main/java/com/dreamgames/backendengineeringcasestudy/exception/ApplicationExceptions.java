package com.dreamgames.backendengineeringcasestudy.exception;

import reactor.core.publisher.Mono;

// acts like an exception factory
public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer id){
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> IllegalDate(){
        return Mono.error(new IllegalDateException());
    }

    public static <T> Mono<T> levelInsufficient(Integer level){
        return Mono.error(new NewbieException(level));
    }

    public static <T> Mono<T> IllegalEvent(){
        return Mono.error(new IllegalEventException());
    }

    public static <T> Mono<T> RequesterAlreadyCollaborated(){ return Mono.error(new RequesterAlreadyCollaboratedException()); }

    public static <T> Mono<T> RequestedAlreadyCollaborated(){ return Mono.error(new RequestedAlreadyCollaboratedException()); }

    public static <T> Mono<T> BalloonAlreadyInflated(){ return Mono.error(new BalloonAlreadyInflatedException()); }

    public static <T> Mono<T> InsufficientHelium(){ return Mono.error(new InsufficientHeliumException()); }

    public static <T> Mono<T> BalloonNotFullyInflated(){ return Mono.error(new BalloonNotFullyInflatedException()); }

    public static <T> Mono<T> EventRewardAlreadyTaken(){ return Mono.error(new EventRewardAlreadyTakenException()); }
}