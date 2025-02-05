package com.dreamgames.backendengineeringcasestudy.entity;

import com.dreamgames.backendengineeringcasestudy.domain.CollaborationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("collaboration")
public class Collaboration {

    @Id
    @Column("invitation_id")
    private Integer invitationId;
    @Column("collaboration_status")
    private CollaborationStatus collaborationStatus;
    @Column("sender_user_helium_contribution")
    private Double senderUserHeliumContribution;
    @Column("receiver_user_helium_contribution")
    private Double receiverUserHeliumContribution;
    @Column("is_reward_claimed_by_sender")
    private Integer isRewardClaimedBySender; // 0 : FALSE, 1 : TRUE
    @Column("is_reward_claimed_by_receiver")
    private Integer isRewardClaimedByReceiver; // 0 : FALSE, 1 : TRUE

    public Integer getInvitationId() {
        return invitationId;
    }

    public void setInvitationId(Integer invitationId) {
        this.invitationId = invitationId;
    }

    public CollaborationStatus getCollaborationStatus() {
        return collaborationStatus;
    }

    public void setCollaborationStatus(CollaborationStatus collaborationStatus) {
        this.collaborationStatus = collaborationStatus;
    }

    public Double getSenderUserHeliumContribution() {
        return senderUserHeliumContribution;
    }

    public void setSenderUserHeliumContribution(Double senderUserHeliumContribution) {
        this.senderUserHeliumContribution = senderUserHeliumContribution;
    }

    public Double getReceiverUserHeliumContribution() {
        return receiverUserHeliumContribution;
    }

    public void setReceiverUserHeliumContribution(Double receiverUserHeliumContribution) {
        this.receiverUserHeliumContribution = receiverUserHeliumContribution;
    }

    public Integer getIsRewardClaimedBySender() {
        return isRewardClaimedBySender;
    }

    public void setIsRewardClaimedBySender(Integer isRewardClaimedBySender) {
        this.isRewardClaimedBySender = isRewardClaimedBySender;
    }

    public Integer getIsRewardClaimedByReceiver() {
        return isRewardClaimedByReceiver;
    }

    public void setIsRewardClaimedByReceiver(Integer isRewardClaimedByReceiver) {
        this.isRewardClaimedByReceiver = isRewardClaimedByReceiver;
    }
}
