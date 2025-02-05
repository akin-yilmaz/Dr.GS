package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserProgressRepository extends ReactiveCrudRepository<UserProgress, Integer> {

    @Query("""
       SELECT
            u.*
        FROM
            user_progress u
        WHERE
            u.test_group = :testGroup
        ORDER BY level_at DESC
        LIMIT :limit
       """)
    Flux<UserProgress> getLeaderboard(TestGroup testGroup, Integer limit);

}
