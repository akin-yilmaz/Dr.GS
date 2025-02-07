package com.dreamgames.backendengineeringcasestudy.domain;

public enum CollaborationStatus {

    IN_PROGRESS(0),
    COMPLETED(1),
    SUSPENDED(2);

    private final int code;

    CollaborationStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static CollaborationStatus fromCode(int code){
        for (CollaborationStatus collaborationStatus : CollaborationStatus.values()) {
            if (collaborationStatus.getCode() == code) {
                return collaborationStatus;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

}
