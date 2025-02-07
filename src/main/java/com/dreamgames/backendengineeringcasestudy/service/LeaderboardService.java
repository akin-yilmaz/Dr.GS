package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.mapper.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.repository.UserProgressRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@Service
public class LeaderboardService {

    private UserProgressRepository userProgressRepository;
    private Sinks.Many<UserProgressInformation> sinkA;
    private Sinks.Many<UserProgressInformation> sinkB;

    @Value("${app.constant.leaderboard-size-limit}")
    private Integer leaderboardSizeLimit;
    @Value("${app.constant.leaderboard-schedule-time}")
    private Integer leaderboardScheduleTime;

    @Autowired
    public LeaderboardService(UserProgressRepository userProgressRepository, @Qualifier("sinkA") Sinks.Many<UserProgressInformation> sinkA, @Qualifier("sinkB") Sinks.Many<UserProgressInformation> sinkB) {
        this.userProgressRepository = userProgressRepository;
        this.sinkA = sinkA;
        this.sinkB = sinkB;
    }

    @PostConstruct
    public void init() { // @Value annotation is run after the constuctor
        startPublishingA();
        startPublishingB();
    }


    private void startPublishingA() {
        Flux.interval(Duration.ofSeconds(leaderboardScheduleTime))  // Runs every 10 seconds
                .flatMap(tick -> userProgressRepository.getLeaderboard(TestGroup.A, leaderboardSizeLimit))
                .doOnNext(userProgress -> sinkA.tryEmitNext(UserProgressMapper.entityToDto(userProgress)))
                .subscribe();
    }

    private void startPublishingB() {
        Flux.interval(Duration.ofSeconds(leaderboardScheduleTime))  // Runs every 10 seconds
                .flatMap(tick -> userProgressRepository.getLeaderboard(TestGroup.B, leaderboardSizeLimit))
                .doOnNext(userProgress -> sinkB.tryEmitNext(UserProgressMapper.entityToDto(userProgress)))
                .subscribe();
    }

    public Flux<UserProgressInformation> getLeaderboardStream(Integer userProgressId){

        return this.userProgressRepository.findById(userProgressId)
                .flatMapMany(userProgress -> {
                    if(userProgress.getTestGroup() == TestGroup.A){
                        return this.sinkA.asFlux().take(leaderboardSizeLimit);
                    }
                    return this.sinkB.asFlux().take(leaderboardSizeLimit);
                });

    }



}
