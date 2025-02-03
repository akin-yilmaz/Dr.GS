package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.exception.ApplicationExceptions;
import com.dreamgames.backendengineeringcasestudy.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;

@RestController
@RequestMapping("user_progress")
public class UserProgressController {

    private UserProgressService userProgressService;

    @Autowired
    public UserProgressController(UserProgressService userProgressService) {
        this.userProgressService = userProgressService;
    }

    @GetMapping()
    public Mono<ResponseEntity<UserProgressInformation>> getUserProgress(@RequestParam Integer userProgressId) {
        return this.userProgressService.getUserProgress(userProgressId)
                .map(ResponseEntity::ok)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(userProgressId));
    }

    @PostMapping
    public Mono<ResponseEntity<UserProgressInformation>> saveUserProgress(@RequestParam TestGroup testGroup) {
        return this.userProgressService.createUserProgress(testGroup)
                .map(ResponseEntity::ok);
    }

    @PutMapping()
    public Mono<ResponseEntity<UserProgressInformation>> updateUserProgress(@RequestParam Integer userProgressId, @RequestParam Instant timestamp) {
        return this.userProgressService.updateUserProgress(userProgressId, timestamp)
                .map(ResponseEntity::ok)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(userProgressId));
    }

}
