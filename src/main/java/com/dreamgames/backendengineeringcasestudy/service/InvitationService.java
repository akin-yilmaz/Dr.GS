package com.dreamgames.backendengineeringcasestudy.service;


import com.dreamgames.backendengineeringcasestudy.domain.CollaborationStatus;
import com.dreamgames.backendengineeringcasestudy.domain.InvitationStatus;
import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.dto.CollaborationInformation;
import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.entity.Collaboration;
import com.dreamgames.backendengineeringcasestudy.entity.Invitation;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.exception.ApplicationExceptions;
import com.dreamgames.backendengineeringcasestudy.mapper.CollaborationMapper;
import com.dreamgames.backendengineeringcasestudy.mapper.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.repository.CollaborationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.InvitationRepository;
import com.dreamgames.backendengineeringcasestudy.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class InvitationService {

    private InvitationRepository invitationRepository;
    private UserProgressRepository userProgressRepository;
    private CollaborationRepository collaborationRepository;

    @Autowired
    public InvitationService(InvitationRepository invitationRepository, UserProgressRepository userProgressRepository, CollaborationRepository collaborationRepository) {
        this.invitationRepository = invitationRepository;
        this.userProgressRepository = userProgressRepository;
        this.collaborationRepository = collaborationRepository;
    }

    @Value("${app.constant.helium-eligible-level}")
    private Integer heliumEligibleLevel;
    @Value("${app.constant.event-start}")
    private Integer eventStart;
    @Value("${app.constant.event-end}")
    private Integer eventEnd;
    @Value("${app.constant.balloon-limit-a}")
    private Double balloonLimitA;
    @Value("${app.constant.balloon-limit-b}")
    private Double balloonLimitB;
    @Value("${app.constant.event-reward-a}")
    private Double eventRewardA;
    @Value("${app.constant.event-reward-b}")
    private Double eventRewardB;

    public Mono<List<UserProgressInformation>> getSuggestions(Integer userProgressId, Integer eventId, Instant timestamp) {

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if (hour < this.eventStart || hour >= this.eventEnd) {
            return ApplicationExceptions.IllegalDate();
        }

        if (eventId != 1) {
            return ApplicationExceptions.IllegalEvent();
        }

        //AtomicReference<TestGroup> storedValue = new AtomicReference<>();

        return this.userProgressRepository.findById(userProgressId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(userProgressId))
                .flatMap(dbEntity -> {
                    if (dbEntity.getLevelAt() < 50) {
                        return ApplicationExceptions.levelInsufficient(dbEntity.getLevelAt());
                    }
                    return Mono.just(dbEntity); // I will need the testGroup field of this dbEntity
                })
                .flatMap(dbEntity ->
                        this.invitationRepository.IsUserAlreadyCollaborated(userProgressId, eventId)
                                .flatMap(dbEntityInvitation -> ApplicationExceptions.RequesterAlreadyCollaborated())
                                .switchIfEmpty(Mono.just(dbEntity)) // Pass dbEntity forward
                )
                .cast(UserProgress.class)
                .flatMap(dbEntity ->
                        this.invitationRepository.getSuggestions(userProgressId, eventId, dbEntity.getTestGroup()).collectList()
                )
                .flatMap(list -> {
                    if (list.size() <= 10) {
                        List<UserProgressInformation> mappedList = list.stream().map(element -> UserProgressMapper.entityToDto(element)).collect(Collectors.toList());
                        return Mono.just(mappedList);
                    }
                    Collections.shuffle(list);
                    List<UserProgressInformation> randomTen = list.stream()
                            .limit(10)
                            .map(element -> UserProgressMapper.entityToDto(element))
                            .collect(Collectors.toList());
                    return Mono.just(randomTen);
                });

    }


    public Mono<List<Invitation>> getInvitationsReceived(Integer userProgressId, Integer eventId, Instant timestamp){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if(hour < this.eventStart || hour >= this.eventEnd){
            return ApplicationExceptions.IllegalDate();
        }

        if(eventId != 1){
            return ApplicationExceptions.IllegalEvent();
        }

        return this.userProgressRepository.findById(userProgressId)
                .switchIfEmpty(ApplicationExceptions.customerNotFound(userProgressId))
                .flatMap(dbEntity -> {
                    if (dbEntity.getLevelAt() < 50) {
                        return ApplicationExceptions.levelInsufficient(dbEntity.getLevelAt());
                    }
                    return Mono.just(dbEntity);
                })
                .flatMap(dbEntity ->
                        this.invitationRepository.IsUserAlreadyCollaborated(userProgressId, eventId)
                                .flatMap(dbEntityInvitation -> ApplicationExceptions.RequesterAlreadyCollaborated())
                                .switchIfEmpty(Mono.just(dbEntity)) // Pass dbEntity forward
                )
                .cast(UserProgress.class)
                .flatMap(dbEntity ->
                        this.invitationRepository.getInvitationsReceived(userProgressId, eventId).collectList()
                );
        // Later add pageable
    }

    @Transactional
    public Mono<Invitation> createInvitation(Integer senderUserProgressId, Integer receiverUserProgressId, Integer eventId, Instant timestamp){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if (hour < this.eventStart || hour >= this.eventEnd) {
            return ApplicationExceptions.IllegalDate();
        }

        if (eventId != 1) {
            return ApplicationExceptions.IllegalEvent();
        }

        return Flux.merge(this.invitationRepository.IsUserAlreadyCollaborated(senderUserProgressId, eventId),
                        this.invitationRepository.IsUserAlreadyCollaborated(receiverUserProgressId, eventId))
                .next()
                .flatMap(dbEntityInvitation -> {
                    if(Objects.equals(dbEntityInvitation.getSenderId(), senderUserProgressId) || Objects.equals(dbEntityInvitation.getReceiverId(), senderUserProgressId)){
                        return ApplicationExceptions.RequesterAlreadyCollaborated();
                    }
                    return ApplicationExceptions.RequestedAlreadyCollaborated();})
                .switchIfEmpty(
                        Mono.fromSupplier(createInvitationHelper(senderUserProgressId, receiverUserProgressId, eventId))
                        .flatMap(entity -> this.invitationRepository.save(entity))
                )
                .cast(Invitation.class);

    }

    private Supplier<Invitation> createInvitationHelper(Integer senderUserProgressId, Integer receiverUserProgressId, Integer eventId){
        var invitation = new Invitation();
        invitation.setInvitationStatus(InvitationStatus.PENDING);
        invitation.setSenderId(senderUserProgressId);
        invitation.setReceiverId(receiverUserProgressId);
        invitation.setEventId(eventId);
        return () -> invitation;
    }

    @Transactional
    public Mono<Invitation> updateInvitationByRejection(Integer invitationId, Integer receiverUserProgressId, Integer eventId, Instant timestamp){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if(hour < this.eventStart || hour >= this.eventEnd){
            return ApplicationExceptions.IllegalDate();
        }

        if(eventId != 1){
            return ApplicationExceptions.IllegalEvent();
        }

        return this.invitationRepository.findById(invitationId)
                .doOnNext(reject(receiverUserProgressId))
                .flatMap(dbEntity -> this.invitationRepository.save(dbEntity));

    }

    private Consumer<Invitation> reject(Integer receiverUserProgressId){
        return (invitation) -> {
            invitation.setInvitationStatus(InvitationStatus.REJECTED);
            invitation.setReceiverId(receiverUserProgressId);
        };
    }

    @Transactional
    public Mono<CollaborationInformation> updateInvitationByApprove(Integer invitationId, Integer receiverUserProgressId, Integer eventId, Instant timestamp){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if (hour < this.eventStart || hour >= this.eventEnd) {
            return ApplicationExceptions.IllegalDate();
        }

        if (eventId != 1) {
            return ApplicationExceptions.IllegalEvent();
        }

        return this.invitationRepository.findById(invitationId)
                .flatMap(invitation ->

                        Flux.merge(this.invitationRepository.IsUserAlreadyCollaborated(invitation.getSenderId(), eventId),
                                        this.invitationRepository.IsUserAlreadyCollaborated(invitation.getReceiverId(), eventId))
                                .next()
                                .flatMap(dbEntityInvitation -> {
                                    if (Objects.equals(dbEntityInvitation.getSenderId(), receiverUserProgressId) || Objects.equals(dbEntityInvitation.getReceiverId(), receiverUserProgressId)) {
                                        return ApplicationExceptions.RequesterAlreadyCollaborated();
                                    }
                                    return ApplicationExceptions.RequestedAlreadyCollaborated();
                                })
                                .switchIfEmpty(
                                        Mono.just(invitation)
                                )
                                .cast(Invitation.class)
                )
                .doOnNext(approve(receiverUserProgressId))
                .flatMap(dbEntity -> this.invitationRepository.save(dbEntity))
                .flatMap(invitation ->
                        Mono.fromSupplier(createCollaborationHelper(invitationId))
                                .flatMap(collaboration -> this.collaborationRepository.save(collaboration))
                                .flatMap(collaboration -> Mono.zip(Mono.just(invitation), Mono.just(collaboration)))
                )
                .map(tuple -> CollaborationMapper.entityToDto(tuple.getT1(), tuple.getT2(), receiverUserProgressId))
                .flatMap(collaborationInformation ->

                        Flux.merge(this.invitationRepository.getInvitationsSent(collaborationInformation.invitation().senderId(), eventId),
                                        this.invitationRepository.getInvitationsSent(collaborationInformation.invitation().receiverId(), eventId)
                                )
                                .doOnNext(invitation -> invitation.setInvitationStatus(InvitationStatus.INVALIDATED))
                                .flatMap(invitation -> this.invitationRepository.save(invitation))
                                .then(Mono.just(collaborationInformation))
                                //.flatMap(voidMono -> Mono.just(collaborationInformation))

                );

    }

    private Consumer<Invitation> approve(Integer receiverUserProgressId){
        return (invitation) -> {
            invitation.setInvitationStatus(InvitationStatus.APPROVED);
            invitation.setReceiverId(receiverUserProgressId);
        };
    }

    private Supplier<Collaboration> createCollaborationHelper(Integer invitationId){
        var collaboration = new Collaboration();
        collaboration.setInvitationId(invitationId);
        collaboration.setCollaborationStatus(CollaborationStatus.IN_PROGRESS);
        collaboration.setSenderUserHeliumContribution(0.0);
        collaboration.setReceiverUserHeliumContribution(0.0);
        collaboration.setIsRewardClaimedBySender(0);
        collaboration.setIsRewardClaimedByReceiver(0);
        return () -> collaboration;
    }

    public Mono<CollaborationInformation> getCollaboration(Integer userProgressId, Integer invitationId, Instant timestamp){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if(hour < this.eventStart || hour >= this.eventEnd){
            return ApplicationExceptions.IllegalDate();
        }

        Mono<Invitation> invitationMono = this.invitationRepository.findById(invitationId);
        Mono<Collaboration> collaborationMono = this.collaborationRepository.findByInvitationId(invitationId);

        return Mono.zip(invitationMono, collaborationMono)
                .map(tuple -> CollaborationMapper.entityToDto(tuple.getT1(), tuple.getT2(), userProgressId));

    }
    @Transactional
    public Mono<CollaborationInformation> updateCollaborationByInflating(Integer userProgressId, Integer invitationId, Instant timestamp, Double consumedHelium){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if (hour < this.eventStart || hour >= this.eventEnd) {
            return ApplicationExceptions.IllegalDate();
        }

        return this.collaborationRepository.findByInvitationId(invitationId)
                .flatMap(collaboration -> {
                    if (collaboration.getCollaborationStatus() == CollaborationStatus.COMPLETED) {
                        return ApplicationExceptions.BalloonAlreadyInflated();
                    }
                    return Mono.just(collaboration);
                })
                .flatMap(collaboration ->
                        this.userProgressRepository.findById(userProgressId)
                                .flatMap(userProgress -> {
                                    if (consumedHelium > userProgress.getHelium()) {
                                        return ApplicationExceptions.InsufficientHelium();
                                    }
                                    return Mono.just(userProgress);
                                })
                                .flatMap(userProgress ->

                                        this.invitationRepository.findById(invitationId)
                                                .flatMap(invitation -> {
                                                    Double currTotal = collaboration.getSenderUserHeliumContribution() + collaboration.getReceiverUserHeliumContribution();
                                                    Double diff = userProgress.getTestGroup() == TestGroup.A ? balloonLimitA - currTotal : balloonLimitB - currTotal;

                                                    if (consumedHelium >= diff) {
                                                        Double remainingHelium = consumedHelium - diff;
                                                        userProgress.setHelium(userProgress.getHelium() - (consumedHelium - remainingHelium));
                                                        collaboration.setCollaborationStatus(CollaborationStatus.COMPLETED);
                                                        if (Objects.equals(invitation.getSenderId(), userProgressId)) {
                                                            collaboration.setSenderUserHeliumContribution(collaboration.getSenderUserHeliumContribution() + (consumedHelium - remainingHelium));
                                                        } else if (Objects.equals(invitation.getReceiverId(), userProgressId)) {
                                                            collaboration.setReceiverUserHeliumContribution(collaboration.getReceiverUserHeliumContribution() + (consumedHelium - remainingHelium));
                                                        }
                                                    } else {
                                                        userProgress.setHelium(userProgress.getHelium() - consumedHelium);
                                                        if (Objects.equals(invitation.getSenderId(), userProgressId)) {
                                                            collaboration.setSenderUserHeliumContribution(collaboration.getSenderUserHeliumContribution() + consumedHelium);
                                                        } else if (Objects.equals(invitation.getReceiverId(), userProgressId)) {
                                                            collaboration.setReceiverUserHeliumContribution(collaboration.getReceiverUserHeliumContribution() + consumedHelium);
                                                        }
                                                    }

                                                    return Mono.zip(this.userProgressRepository.save(userProgress),
                                                            Mono.just(invitation),
                                                            this.collaborationRepository.save(collaboration));


                                                })
                                                .map(tuple -> CollaborationMapper.entityToDto(tuple.getT2(), tuple.getT3(), userProgressId))
                                )


                );

    }
    @Transactional
    public Mono<CollaborationInformation> updateCollaborationByClaimingReward(Integer userProgressId, Integer invitationId, Instant timestamp){

        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        if (hour < this.eventStart || hour >= this.eventEnd) {
            return ApplicationExceptions.IllegalDate();
        }

        return this.collaborationRepository.findByInvitationId(invitationId)
                .flatMap(collaboration -> {
                    if (collaboration.getCollaborationStatus() != CollaborationStatus.COMPLETED) {
                        return ApplicationExceptions.BalloonNotFullyInflated();
                    }
                    return Mono.just(collaboration);
                })
                .flatMap(collaboration ->
                        Mono.zip(this.userProgressRepository.findById(userProgressId), this.invitationRepository.findById(invitationId))
                                .flatMap(tuple -> {
                                    if ((Objects.equals(tuple.getT2().getSenderId(), userProgressId) && Objects.equals(collaboration.getIsRewardClaimedBySender(), 1))
                                            || (Objects.equals(tuple.getT2().getReceiverId(), userProgressId) && Objects.equals(collaboration.getIsRewardClaimedByReceiver(), 1))) {
                                        return ApplicationExceptions.EventRewardAlreadyTaken();
                                    }
                                    if (Objects.equals(tuple.getT2().getSenderId(), userProgressId)) {
                                        collaboration.setIsRewardClaimedBySender(1);
                                    } else if (Objects.equals(tuple.getT2().getReceiverId(), userProgressId)) {
                                        collaboration.setIsRewardClaimedByReceiver(1);
                                    }

                                    if (tuple.getT1().getTestGroup() == TestGroup.A) {
                                        tuple.getT1().setCoin(tuple.getT1().getCoin() + eventRewardA);
                                    } else {
                                        tuple.getT1().setCoin(tuple.getT1().getCoin() + eventRewardB);
                                    }
                                    return Mono.zip(Mono.just(tuple.getT1()), Mono.just(tuple.getT2()), Mono.just(collaboration));
                                })
                                .flatMap(tuple ->
                                        Mono.zip(this.userProgressRepository.save(tuple.getT1()), this.collaborationRepository.save(tuple.getT3()))
                                                .flatMap(savedTuple -> Mono.just(CollaborationMapper.entityToDto(tuple.getT2(), savedTuple.getT2(), userProgressId)))
                                )

                );

    }

}
