package com.dreamgames.backendengineeringcasestudy.mapper;

import com.dreamgames.backendengineeringcasestudy.dto.UserProgressInformation;
import com.dreamgames.backendengineeringcasestudy.entity.UserProgress;

public class UserProgressMapper {

    public static UserProgressInformation entityToDto(UserProgress userProgress) {
        return new UserProgressInformation(
                userProgress.getId(),
                userProgress.getCoin(),
                userProgress.getLevelAt(),
                userProgress.getHelium(),
                userProgress.getTestGroup());
    }

    public static UserProgress dtoToEntity(UserProgressInformation userProgressInformation){
        var entity = new UserProgress();
        entity.setCoin(userProgressInformation.coin());
        entity.setLevelAt(userProgressInformation.levelAt());
        entity.setTestGroup(userProgressInformation.testGroup());
        return entity;
    }

}
