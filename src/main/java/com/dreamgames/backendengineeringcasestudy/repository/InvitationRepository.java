package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.entity.Invitation;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface InvitationRepository extends ReactiveCrudRepository<Invitation, Integer>  {

    @Query("""
       SELECT
            i.*
        FROM
            invitation i
        WHERE
            i.invitation_status = 'APPROVED' AND i.event_id = :eventId AND (i.sender_user_id = :userProgressId OR i.receiver_user_id = :userProgressId)
       """)
    Mono<Invitation> IsUserAlreadyCollaborated(Integer userProgressId, Integer eventId);

    @Query("""
       SELECT
            u.*
        FROM
            user_progress u
        WHERE
            u.id <> :userProgressId AND u.test_group = :testGroup AND u.level_at >=50
            
            AND u.id NOT IN ( SELECT
                                  u.id
                              FROM
                                  invitation i, user_progress u
                              WHERE
                                  (u.id = i.sender_user_id OR u.id = i.receiver_user_id) AND u.test_group = :testGroup AND i.event_id = :eventId AND i.invitation_status = 'APPROVED'
                             )
            
            AND u.id NOT IN ( SELECT
                                    i.receiver_user_id
                              FROM
                                    invitation i
                              WHERE
                                    i.invitation_status = 'REJECTED' AND i.event_id = :eventId AND i.sender_user_id = :userProgressId  
                            )
                            
            AND u.id NOT IN ( SELECT
                                    i.receiver_user_id
                              FROM
                                    invitation i
                              WHERE
                                    i.invitation_status = 'PENDING' AND i.event_id = :eventId AND i.sender_user_id = :userProgressId  
                            )
            ORDER BY RAND()
            
            LIMIT 10
       """)
    Flux<UserProgress> getSuggestions(Integer userProgressId, Integer eventId, TestGroup testGroup);

    @Query("""
       SELECT
            i.*
        FROM
            invitation i
        WHERE
            i.invitation_status = 'PENDING' AND i.event_id = :eventId AND i.receiver_user_id = :userProgressId
       """)
    Flux<Invitation> getInvitationsReceived(Integer userProgressId, Integer eventId);

    @Query("""
       SELECT
            i.*
        FROM
            invitation i
        WHERE
            i.invitation_status = 'PENDING' AND i.event_id = :eventId AND i.sender_user_id = :userProgressId
       """)
    Flux<Invitation> getInvitationsSent(Integer userProgressId, Integer eventId);

}
