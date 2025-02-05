package com.dreamgames.backendengineeringcasestudy.domain;

public enum InvitationStatus {

    PENDING(0),
    APPROVED(1),
    REJECTED(2),
    INVALIDATED(3);

    private final int code;

    InvitationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static InvitationStatus fromCode(int code){
        for (InvitationStatus invitationStatus : InvitationStatus.values()) {
            if (invitationStatus.getCode() == code) {
                return invitationStatus;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

}
