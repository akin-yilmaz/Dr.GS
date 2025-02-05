package com.dreamgames.backendengineeringcasestudy.service;

import com.dreamgames.backendengineeringcasestudy.domain.TestGroup;
import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;
import com.dreamgames.backendengineeringcasestudy.mapper.UserProgressMapper;
import com.dreamgames.backendengineeringcasestudy.repository.UserProgressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Service
public class UserProgressService {

    private UserProgressRepository userProgressRepository;

    @Value("${app.constant.coin-start}")
    private Double coinStart;
    @Value("${app.constant.level-start}")
    private Integer levelStart;
    @Value("${app.constant.helium-start}")
    private Double heliumStart;
    @Value("${app.constant.coin-reward}")
    private Double coinReward;
    @Value("${app.constant.helium-reward}")
    private Double heliumReward;
    @Value("${app.constant.helium-eligible-level}")
    private Integer heliumEligibleLevel;
    @Value("${app.constant.event-start}")
    private Integer eventStart;
    @Value("${app.constant.event-end}")
    private Integer eventEnd;


    @Autowired
    public UserProgressService(UserProgressRepository userProgressRepository) {
        this.userProgressRepository = userProgressRepository;
    }

    public Mono<UserProgressInformation> getUserProgress(Integer userProgressId){
        return this.userProgressRepository.findById(userProgressId)
                .map(dbEntity -> UserProgressMapper.entityToDto(dbEntity));
    }

    @Transactional
    public Mono<UserProgressInformation> createUserProgress(TestGroup testGroup) {
        return Mono.fromSupplier(createUserProgressHelper(testGroup))
                .flatMap(entity -> this.userProgressRepository.save(entity))
                .map(dbEntity -> UserProgressMapper.entityToDto(dbEntity));

    }

    @Transactional
    public Mono<UserProgressInformation> updateUserProgress(Integer userProgressId, Instant timestamp) {
        return this.userProgressRepository.findById(userProgressId)
                .doOnNext(levelUp())
                .map(rewardHeliumConditionally(timestamp))
                .flatMap(dbEntity -> this.userProgressRepository.save(dbEntity))
                .map(dbEntity -> UserProgressMapper.entityToDto(dbEntity));
    }

    private Supplier<UserProgress> createUserProgressHelper(TestGroup testGroup){
        var userProgress = new UserProgress();
        userProgress.setCoin(this.coinStart);
        userProgress.setLevelAt(this.levelStart);
        userProgress.setHelium(this.heliumStart);
        userProgress.setTestGroup(testGroup);
        return () -> userProgress;
    }

    private Consumer<UserProgress> levelUp(){
        return (userProgress) -> {
            userProgress.setCoin(userProgress.getCoin() + this.coinReward);
            userProgress.setLevelAt(userProgress.getLevelAt() + 1);
        };
    }

    private Predicate<UserProgress> isUserEligibleForHelium(Instant timestamp){
        int hour = timestamp.atZone(ZoneOffset.UTC).getHour();
        Predicate<UserProgress> isLevelAtLeastThreshold = (userProgress) -> userProgress.getLevelAt() > this.heliumEligibleLevel; // already incremented
        return isLevelAtLeastThreshold.and((userProgress) -> hour >= this.eventStart && hour < this.eventEnd);
    }
    /*
    private Consumer<UserProgress> rewardHelium(){
        return (userProgress) -> {
            userProgress.setHelium(userProgress.getHelium() + 10);
        };
    }
    */
    private UnaryOperator<UserProgress> rewardHeliumConditionally(Instant timestamp){

        return (userProgress) -> {

            if(isUserEligibleForHelium(timestamp).test(userProgress)){
                userProgress.setHelium(userProgress.getHelium() + this.heliumReward);
            }
            return userProgress;
        };

    }


}
