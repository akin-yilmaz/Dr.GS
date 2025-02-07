package com.dreamgames.backendengineeringcasestudy.mapper;

import com.dreamgames.backendengineeringcasestudy.dto.CollaborationInformation;
import com.dreamgames.backendengineeringcasestudy.entity.Collaboration;
import com.dreamgames.backendengineeringcasestudy.entity.Invitation;

public class CollaborationMapper {

    public static CollaborationInformation entityToDto(Invitation invitation, Collaboration collaboration, Integer requesterUserProgressId) {

        if(requesterUserProgressId == invitation.getSenderId()){
            return new CollaborationInformation(
                    InvitationMapper.entityToDto(invitation),
                    collaboration.getId(),
                    collaboration.getCollaborationStatus(),
                    collaboration.getSenderUserHeliumContribution(),
                    collaboration.getReceiverUserHeliumContribution(),
                    collaboration.getIsRewardClaimedBySender()
            );
        }
        else{
            return new CollaborationInformation(
                    InvitationMapper.entityToDto(invitation),
                    collaboration.getId(),
                    collaboration.getCollaborationStatus(),
                    collaboration.getReceiverUserHeliumContribution(),
                    collaboration.getSenderUserHeliumContribution(),
                    collaboration.getIsRewardClaimedByReceiver()
            );
        }

    }

}
