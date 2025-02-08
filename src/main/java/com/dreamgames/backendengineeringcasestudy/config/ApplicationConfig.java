package com.dreamgames.backendengineeringcasestudy.config;

import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

import java.util.concurrent.atomic.AtomicInteger;

@Configuration
public class ApplicationConfig {

    @Value("${app.constant.leaderboard-size-limit}")
    private Integer leaderboardSizeLimit;

    @Bean("sinkA")
    public Sinks.Many<UserProgressInformation> sinkA(){
        return Sinks.many().replay().limit(leaderboardSizeLimit);
    }

    @Bean("sinkB")
    public Sinks.Many<UserProgressInformation> sinkB(){
        return Sinks.many().replay().limit(leaderboardSizeLimit);
    }

    @Bean("_100thHighestScoreA")
    public AtomicInteger _100thHighestScoreA(){
        return new AtomicInteger(0);
    }

    @Bean("_100thHighestScoreB")
    public AtomicInteger _100thHighestScoreB(){
        return new AtomicInteger(0);
    }
}
