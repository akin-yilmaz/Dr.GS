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
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class LeaderboardService {

    private final UserProgressRepository userProgressRepository;
    private final Sinks.Many<UserProgressInformation> sinkA;
    private final Sinks.Many<UserProgressInformation> sinkB;
    private final AtomicInteger _100thHighestScoreA;
    private final AtomicInteger _100thHighestScoreB;

    @Value("${app.constant.leaderboard-size-limit}")
    private Integer leaderboardSizeLimit;
    @Value("${app.constant.leaderboard-schedule-time}")
    private Integer leaderboardScheduleTime;

    @Autowired
    public LeaderboardService(UserProgressRepository userProgressRepository,
                              @Qualifier("sinkA") Sinks.Many<UserProgressInformation> sinkA,
                              @Qualifier("sinkB") Sinks.Many<UserProgressInformation> sinkB,
                              @Qualifier("_100thHighestScoreA") AtomicInteger scoreA,
                              @Qualifier("_100thHighestScoreB") AtomicInteger scoreB) {
        this.userProgressRepository = userProgressRepository;
        this.sinkA = sinkA;
        this.sinkB = sinkB;
        this._100thHighestScoreA = scoreA;
        this._100thHighestScoreB = scoreB;
    }

    @PostConstruct
    public void init() { // @Value annotation is run after the constuctor
        emitTop100A();
        emitTop100B();
    }


     public void emitTop100A() {

        this.userProgressRepository.getLeaderboard(TestGroup.A, leaderboardSizeLimit)
                        .index()
                        .doOnNext(tuple -> sinkA.tryEmitNext(UserProgressMapper.entityToDto(tuple.getT2())))
                        .filter(tuple -> tuple.getT1().equals(100L))
                        .doOnNext(tuple -> this._100thHighestScoreA.set(tuple.getT2().getLevelAt()))
                        .subscribe();
        /*
        Flux.interval(Duration.ofSeconds(leaderboardScheduleTime))  // Runs every 10 seconds
                .flatMap(tick -> userProgressRepository.getLeaderboard(TestGroup.A, leaderboardSizeLimit))
                .doOnNext(userProgress -> sinkA.tryEmitNext(UserProgressMapper.entityToDto(userProgress)))
                .subscribe();
        */
    }

    public void emitTop100B() {

        this.userProgressRepository.getLeaderboard(TestGroup.B, leaderboardSizeLimit)
                .index()
                .doOnNext(tuple -> sinkB.tryEmitNext(UserProgressMapper.entityToDto(tuple.getT2())))
                .filter(tuple -> tuple.getT1().equals(100L))
                .doOnNext(tuple -> this._100thHighestScoreB.set(tuple.getT2().getLevelAt()))
                .subscribe();
        /*
        Flux.interval(Duration.ofSeconds(leaderboardScheduleTime))  // Runs every 10 seconds
                .flatMap(tick -> userProgressRepository.getLeaderboard(TestGroup.B, leaderboardSizeLimit))
                .doOnNext(userProgress -> sinkB.tryEmitNext(UserProgressMapper.entityToDto(userProgress)))
                .subscribe();
        */
    }

    public AtomicInteger get_100thHighestScoreA() {
        return _100thHighestScoreA;
    }

    public AtomicInteger get_100thHighestScoreB() {
        return _100thHighestScoreB;
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
