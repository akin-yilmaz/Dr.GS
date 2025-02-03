package com.dreamgames.backendengineeringcasestudy.exception;

import reactor.core.publisher.Mono;

// acts like an exception factory
public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer id){
        return Mono.error(new CustomerNotFoundException(id));
    }

}