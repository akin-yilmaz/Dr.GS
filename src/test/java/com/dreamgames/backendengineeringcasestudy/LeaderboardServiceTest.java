package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LeaderboardServiceTest {

    private static final Logger log = LoggerFactory.getLogger(LeaderboardServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void getLeaderboardStreamA() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress/leaderboard-stream")
                        .queryParam("userProgressId", 1)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(UserProgressInformation.class)
                .getResponseBody()
                .doOnNext(dto -> log.info("received: {}", dto))
                .collectList()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .expectComplete()
                .verify();

    }

    @Test
    public void getLeaderboardStreamB() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress/leaderboard-stream")
                        .queryParam("userProgressId", 2)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .returnResult(UserProgressInformation.class)
                .getResponseBody()
                .doOnNext(dto -> log.info("received: {}", dto))
                .collectList()
                .as(StepVerifier::create)
                .expectNextCount(1)
                .expectComplete()
                .verify();

    }

}
