package com.dreamgames.backendengineeringcasestudy.domain;

public enum EventType {

    POP_THE_BALLOON(0);

    private final int code;

    EventType(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static EventType fromCode(int code){
        for (EventType eventType : EventType.values()) {
            if (eventType.getCode() == code) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }
}
