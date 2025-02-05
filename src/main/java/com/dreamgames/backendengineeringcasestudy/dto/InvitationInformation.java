package com.dreamgames.backendengineeringcasestudy.dto;

import com.dreamgames.backendengineeringcasestudy.domain.InvitationStatus;

public record InvitationInformation(Integer id,
                                    InvitationStatus invitationStatus,
                                    Integer senderId,
                                    Integer receiverId,
                                    Integer eventId) {
}
