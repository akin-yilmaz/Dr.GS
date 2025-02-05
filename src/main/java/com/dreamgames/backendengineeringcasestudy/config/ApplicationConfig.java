package com.dreamgames.backendengineeringcasestudy.config;

import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

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
}
