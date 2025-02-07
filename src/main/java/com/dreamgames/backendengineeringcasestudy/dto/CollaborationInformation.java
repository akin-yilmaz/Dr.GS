package com.dreamgames.backendengineeringcasestudy.dto;

import com.dreamgames.backendengineeringcasestudy.domain.CollaborationStatus;
import com.dreamgames.backendengineeringcasestudy.entity.Invitation;

public record CollaborationInformation(
                                        InvitationInformation invitation,
                                        Integer id,
                                        CollaborationStatus collaborationStatus,
                                        Double requesterHeliumContribution,
                                        Double requestersFriendHeliumContribution,
                                        Integer isEventRewardClaimedByRequester // 0 : FALSE, 1 : TRUE
                                        ) {
}
