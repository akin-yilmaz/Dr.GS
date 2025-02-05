package com.dreamgames.backendengineeringcasestudy.entity;

import com.dreamgames.backendengineeringcasestudy.domain.InvitationStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("invitation")
public class Invitation {

    @Id
    @Column("id")
    private Integer id;
    @Column("invitation_status")
    private InvitationStatus invitationStatus;
    @Column("sender_user_id")
    private Integer senderId;
    @Column("receiver_user_id")
    private Integer receiverId;
    @Column("event_id")
    private Integer eventId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public InvitationStatus getInvitationStatus() {
        return invitationStatus;
    }

    public void setInvitationStatus(InvitationStatus invitationStatus) {
        this.invitationStatus = invitationStatus;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Integer getEventId() {
        return eventId;
    }

    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }
}
