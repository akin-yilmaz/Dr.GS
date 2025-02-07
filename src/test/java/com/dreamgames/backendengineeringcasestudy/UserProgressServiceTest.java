package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest()
public class UserProgressServiceTest {

    private static final Logger log = LoggerFactory.getLogger(UserProgressServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void userProgressById() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress")
                        .queryParam("userProgressId", 1)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.coin").isEqualTo(6000.0)
                .jsonPath("$.levelAt").isEqualTo(60)
                .jsonPath("$.helium").isEqualTo(150.0)
                .jsonPath("$.testGroup").isEqualTo(TestGroup.A.toString());

    }

    @Test
    public void createUserProgress() {

        this.client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress")
                        .queryParam("testGroup", TestGroup.A)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                //.jsonPath("$.id").isEqualTo(13)
                .jsonPath("$.coin").isEqualTo(2000.0)
                .jsonPath("$.levelAt").isEqualTo(1)
                .jsonPath("$.helium").isEqualTo(0.0)
                .jsonPath("$.testGroup").isEqualTo(TestGroup.A.toString());

    }

    @Test
    public void updateUserProgressWithHeliumReward() {
        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress")
                        .queryParam("userProgressId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.coin").isEqualTo(6100.0)
                .jsonPath("$.levelAt").isEqualTo(61)
                .jsonPath("$.helium").isEqualTo(160.0)
                .jsonPath("$.testGroup").isEqualTo(TestGroup.A.toString());
    }

    @Test
    public void updateUserProgressWithoutHeliumRewardBecauseOfLevel() {
        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress")
                        .queryParam("userProgressId", 2)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(2)
                .jsonPath("$.coin").isEqualTo(3100.0)
                .jsonPath("$.levelAt").isEqualTo(31)
                .jsonPath("$.helium").isEqualTo(0.0)
                .jsonPath("$.testGroup").isEqualTo(TestGroup.B.toString());
    }

    @Test
    public void updateUserProgressWithoutHeliumRewardBecauseOfTime() {
        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress")
                        .queryParam("userProgressId", 3)
                        .queryParam("timestamp", "2025-02-03T22:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.coin").isEqualTo(10100.0)
                .jsonPath("$.levelAt").isEqualTo(101)
                .jsonPath("$.helium").isEqualTo(270.0)
                .jsonPath("$.testGroup").isEqualTo(TestGroup.A.toString());
    }

    @Test
    public void userProgressNotFound() {
        // get
        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/user_progress")
                        .queryParam("userProgressId", 16392)
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .jsonPath("$.detail").isEqualTo("Customer [id=16392] is not found");

    }

}
