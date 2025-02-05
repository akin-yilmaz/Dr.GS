package com.dreamgames.backendengineeringcasestudy.mapper;


import com.dreamgames.backendengineeringcasestudy.dto.InvitationInformation;
import com.dreamgames.backendengineeringcasestudy.entity.Invitation;

public class InvitationMapper {

    public static InvitationInformation entityToDto(Invitation invitation) {
        return new InvitationInformation(
                                        invitation.getId(),
                                        invitation.getInvitationStatus(),
                                        invitation.getSenderId(),
                                        invitation.getReceiverId(),
                                        invitation.getEventId()
                                        );
    }

}
