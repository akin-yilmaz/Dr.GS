package com.dreamgames.backendengineeringcasestudy.controller;

import com.dreamgames.backendengineeringcasestudy.dto.CollaborationInformation;
import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.entity.Invitation;
import com.dreamgames.backendengineeringcasestudy.exception.ApplicationExceptions;
import com.dreamgames.backendengineeringcasestudy.service.InvitationService;
import com.dreamgames.backendengineeringcasestudy.service.UserProgressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("invitation")
public class InvitationController {

    private InvitationService invitationService;

    @Autowired
    public InvitationController(InvitationService invitationService) {
        this.invitationService = invitationService;
    }

    @GetMapping("/suggestions")
    public Mono<ResponseEntity<List<UserProgressInformation>>> getSuggestions(@RequestParam Integer userProgressId, @RequestParam Integer eventId, @RequestParam Instant timestamp) {

        return this.invitationService.getSuggestions(userProgressId, eventId, timestamp)
                .map(ResponseEntity::ok);
    }

    @GetMapping()
    public Mono<ResponseEntity<List<Invitation>>> getInvitationsReceived(@RequestParam Integer userProgressId, @RequestParam Integer eventId, @RequestParam Instant timestamp) {

        return this.invitationService.getInvitationsReceived(userProgressId, eventId, timestamp)
                .map(ResponseEntity::ok);
    }

    @PostMapping()
    public Mono<ResponseEntity<Invitation>> sendInvitation(@RequestParam Integer senderUserProgressId, @RequestParam Integer receiverUserProgressId, @RequestParam Integer eventId, @RequestParam Instant timestamp) {

        return this.invitationService.createInvitation(senderUserProgressId, receiverUserProgressId, eventId, timestamp)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/reject")
    public Mono<ResponseEntity<Invitation>> rejectInvitation(@RequestParam Integer invitationId, @RequestParam Integer receiverUserProgressId, @RequestParam Integer eventId, @RequestParam Instant timestamp) {

        return this.invitationService.updateInvitationByRejection(invitationId, receiverUserProgressId, eventId, timestamp)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/accept")
    public Mono<ResponseEntity<CollaborationInformation>> approveInvitation(@RequestParam Integer invitationId, @RequestParam Integer receiverUserProgressId, @RequestParam Integer eventId, @RequestParam Instant timestamp) {

        return this.invitationService.updateInvitationByApprove(invitationId, receiverUserProgressId, eventId, timestamp)
                .map(ResponseEntity::ok);

    }

    @GetMapping("/collaboration")
    public Mono<ResponseEntity<CollaborationInformation>> getCollaboration(@RequestParam Integer userProgressId, @RequestParam Integer invitationId, @RequestParam Instant timestamp) {

        return this.invitationService.getCollaboration(userProgressId, invitationId, timestamp)
                .map(ResponseEntity::ok);

    }

    @PutMapping("/collaboration/inflate-balloon")
    public Mono<ResponseEntity<CollaborationInformation>> inflateBalloon(@RequestParam Integer userProgressId, @RequestParam Integer invitationId, @RequestParam Instant timestamp, @RequestParam Double consumedHelium) {

        return this.invitationService.updateCollaborationByInflating(userProgressId, invitationId, timestamp, consumedHelium)
                .map(ResponseEntity::ok);

    }

    @PutMapping("/collaboration/claim-reward")
    public Mono<ResponseEntity<CollaborationInformation>> claimReward(@RequestParam Integer userProgressId, @RequestParam Integer invitationId, @RequestParam Instant timestamp) {

        return this.invitationService.updateCollaborationByClaimingReward(userProgressId, invitationId, timestamp)
                .map(ResponseEntity::ok);

    }

}
