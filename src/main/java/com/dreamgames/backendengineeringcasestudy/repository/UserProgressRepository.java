package com.dreamgames.backendengineeringcasestudy.repository;

import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserProgressRepository extends ReactiveCrudRepository<UserProgress, Integer> {



}
