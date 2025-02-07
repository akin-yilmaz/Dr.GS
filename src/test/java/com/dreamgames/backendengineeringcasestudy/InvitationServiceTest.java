package com.dreamgames.backendengineeringcasestudy;

import com.dreamgames.backendengineeringcasestudy.domain.CollaborationStatus;
import com.dreamgames.backendengineeringcasestudy.domain.InvitationStatus;
import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@AutoConfigureWebTestClient
@SpringBootTest()
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InvitationServiceTest {

    private static final Logger log = LoggerFactory.getLogger(InvitationServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void getSuggestionsIllegalDate() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 1)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T22:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Event is held between 08.00 - 22.00 (UTC)");

    }

    @Test
    public void getSuggestionsIllegalEvent() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 1)
                        .queryParam("eventId", 2)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("eventId must be 1 for demo purposes.");

    }

    @Test
    public void getSuggestionsUserNotFound() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 9999999)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Customer [id=9999999] is not found");

    }

    @Test
    public void getSuggestionsLevelInsufficient() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 2)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Your level [level=30] is less than 50");

    }

    @Test
    public void getSuggestionsRequesterAlreadyCollaboratedAsSender() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 1)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("You are already collaborated.");

    }

    @Test
    public void getSuggestionsRequesterAlreadyCollaboratedAsReceiver() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 3)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("You are already collaborated.");

    }

    @Test
    public void getSuggestions() {

        // APPROVED ones, REJECTING ones and already PENDING ones are all considered and tested.

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/suggestions")
                        .queryParam("userProgressId", 9)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))));

    }

    @Test
    public void getInvitationsReceived() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation")
                        .queryParam("userProgressId", 17)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))));

    }

    @Test
    public void createInvitationAndGetInvitationsReceived() {

        this.client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation")
                        .queryParam("senderUserProgressId", 17)
                        .queryParam("receiverUserProgressId", 19)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                //.jsonPath("$.id").isEqualTo(6)
                .jsonPath("$.invitationStatus").isEqualTo(InvitationStatus.PENDING.toString())
                .jsonPath("$.senderId").isEqualTo(17)
                .jsonPath("$.receiverId").isEqualTo(19)
                .jsonPath("$.eventId").isEqualTo(1);

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation")
                        .queryParam("userProgressId", 19)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))));

    }

    @Test
    public void createInvitationRequestedAlreadyCollaborated() {

        this.client.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation")
                        .queryParam("senderUserProgressId", 17)
                        .queryParam("receiverUserProgressId", 1)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Requested user has just collaborated.");

    }

    @Test
    public void UpdateInvitationByRejection() {

        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/reject")
                        .queryParam("invitationId", 5)
                        .queryParam("receiverUserProgressId", 21)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.id").isEqualTo(5)
                .jsonPath("$.invitationStatus").isEqualTo(InvitationStatus.REJECTED.toString())
                .jsonPath("$.senderId").isEqualTo(19)
                .jsonPath("$.receiverId").isEqualTo(21)
                .jsonPath("$.eventId").isEqualTo(1);

    }

    @Test
    public void updateInvitationByApprove() {

        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/accept")
                        .queryParam("invitationId", 6)
                        .queryParam("receiverUserProgressId", 21)
                        .queryParam("eventId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.invitation.id").isEqualTo(6)
                .jsonPath("$.invitation.invitationStatus").isEqualTo(InvitationStatus.APPROVED.toString())
                .jsonPath("$.invitation.senderId").isEqualTo(23)
                .jsonPath("$.invitation.receiverId").isEqualTo(21)
                .jsonPath("$.invitation.eventId").isEqualTo(1)
                .jsonPath("$.id").isEqualTo(4)
                .jsonPath("$.collaborationStatus").isEqualTo(CollaborationStatus.IN_PROGRESS.toString())
                .jsonPath("$.requesterHeliumContribution").isEqualTo(0)
                .jsonPath("$.requestersFriendHeliumContribution").isEqualTo(0)
                .jsonPath("$.isEventRewardClaimedByRequester").isEqualTo(0);

    }

    @Test
    public void getCollaboration() {

        this.client.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/collaboration")
                        .queryParam("userProgressId", 3)
                        .queryParam("invitationId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.invitation.id").isEqualTo(1)
                .jsonPath("$.invitation.invitationStatus").isEqualTo(InvitationStatus.APPROVED.toString())
                .jsonPath("$.invitation.senderId").isEqualTo(1)
                .jsonPath("$.invitation.receiverId").isEqualTo(3)
                .jsonPath("$.invitation.eventId").isEqualTo(1)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.collaborationStatus").isEqualTo(CollaborationStatus.IN_PROGRESS.toString())
                .jsonPath("$.requesterHeliumContribution").isEqualTo(0)
                .jsonPath("$.requestersFriendHeliumContribution").isEqualTo(0)
                .jsonPath("$.isEventRewardClaimedByRequester").isEqualTo(0);

    }

    @Test
    public void updateCollaborationByInflating() {

        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/collaboration/inflate-balloon")
                        .queryParam("userProgressId", 3)
                        .queryParam("invitationId", 1)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .queryParam("consumedHelium", 90)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.invitation.id").isEqualTo(1)
                .jsonPath("$.invitation.invitationStatus").isEqualTo(InvitationStatus.APPROVED.toString())
                .jsonPath("$.invitation.senderId").isEqualTo(1)
                .jsonPath("$.invitation.receiverId").isEqualTo(3)
                .jsonPath("$.invitation.eventId").isEqualTo(1)
                .jsonPath("$.id").isEqualTo(1)
                .jsonPath("$.collaborationStatus").isEqualTo(CollaborationStatus.IN_PROGRESS.toString())
                .jsonPath("$.requesterHeliumContribution").isEqualTo(90)
                .jsonPath("$.requestersFriendHeliumContribution").isEqualTo(0)
                .jsonPath("$.isEventRewardClaimedByRequester").isEqualTo(0);

    }

    @Test
    public void updateCollaborationByInflatingFully() {

        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/collaboration/inflate-balloon")
                        .queryParam("userProgressId", 8)
                        .queryParam("invitationId", 9)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .queryParam("consumedHelium", 50)
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.invitation.id").isEqualTo(9)
                .jsonPath("$.invitation.invitationStatus").isEqualTo(InvitationStatus.APPROVED.toString())
                .jsonPath("$.invitation.senderId").isEqualTo(4)
                .jsonPath("$.invitation.receiverId").isEqualTo(8)
                .jsonPath("$.invitation.eventId").isEqualTo(1)
                .jsonPath("$.id").isEqualTo(2)
                .jsonPath("$.collaborationStatus").isEqualTo(CollaborationStatus.COMPLETED.toString())
                .jsonPath("$.requesterHeliumContribution").isEqualTo(500)
                .jsonPath("$.requestersFriendHeliumContribution").isEqualTo(1000)
                .jsonPath("$.isEventRewardClaimedByRequester").isEqualTo(0);

    }

    @Test
    public void updateCollaborationByClaimingReward() {

        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/collaboration/claim-reward")
                        .queryParam("userProgressId", 12)
                        .queryParam("invitationId", 10)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.invitation.id").isEqualTo(10)
                .jsonPath("$.invitation.invitationStatus").isEqualTo(InvitationStatus.APPROVED.toString())
                .jsonPath("$.invitation.senderId").isEqualTo(12)
                .jsonPath("$.invitation.receiverId").isEqualTo(14)
                .jsonPath("$.invitation.eventId").isEqualTo(1)
                .jsonPath("$.id").isEqualTo(3)
                .jsonPath("$.collaborationStatus").isEqualTo(CollaborationStatus.COMPLETED.toString())
                .jsonPath("$.requesterHeliumContribution").isEqualTo(1000)
                .jsonPath("$.requestersFriendHeliumContribution").isEqualTo(500)
                .jsonPath("$.isEventRewardClaimedByRequester").isEqualTo(1);

    }

    @Test
    public void updateCollaborationByClaimingRewardEventRewardAlreadyTaken() {

        this.client.put()
                .uri(uriBuilder -> uriBuilder
                        .path("/invitation/collaboration/claim-reward")
                        .queryParam("userProgressId", 14)
                        .queryParam("invitationId", 10)
                        .queryParam("timestamp", "2025-02-03T09:09:11.287243Z")
                        .build())
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody()
                .consumeWith(r -> log.info("{}", new String(Objects.requireNonNull(r.getResponseBody()))))
                .jsonPath("$.detail").isEqualTo("Event reward already taken.");


    }




}
