package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.Collaboration;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CollaborationRepository extends ReactiveCrudRepository<Collaboration, Integer> {
}
