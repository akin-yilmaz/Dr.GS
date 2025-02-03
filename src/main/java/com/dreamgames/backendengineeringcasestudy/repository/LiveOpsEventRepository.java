package com.dreamgames.backendengineeringcasestudy.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

// I am currently not using this but may need this in the future

public interface LiveOpsEventRepository extends ReactiveCrudRepository<LiveOpsEventRepository, Integer> {
}
