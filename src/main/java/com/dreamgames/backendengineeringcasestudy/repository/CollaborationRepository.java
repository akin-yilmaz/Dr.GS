package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.Collaboration;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface CollaborationRepository extends ReactiveCrudRepository<Collaboration, Integer> {

    Mono<Collaboration> findByInvitationId(Integer invitationId);

}
