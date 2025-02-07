package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.exception.ApplicationExceptions;
import com.dreamgames.backendengineeringcasestudy.service.LeaderboardService;
import com.dreamgames.backendengineeringcasestudy.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("user_progress")
public class UserProgressController {

    private UserProgressService userProgressService;
    private LeaderboardService leaderboardService;

    @Autowired
    public UserProgressController(UserProgressService userProgressService, LeaderboardService leaderboardService) {
        this.userProgressService = userProgressService;
        this.leaderboardService = leaderboardService;
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

    @GetMapping(value = "/leaderboard-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<UserProgressInformation> getLeaderboard(@RequestParam Integer userProgressId) {

        return this.leaderboardService.getLeaderboardStream(userProgressId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(userProgressId));
    }

}
