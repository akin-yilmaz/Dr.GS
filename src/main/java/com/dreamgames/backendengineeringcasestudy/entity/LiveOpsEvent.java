package com.dreamgames.backendengineeringcasestudy.entity;

import com.dreamgames.backendengineeringcasestudy.domain.EventType;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

// I am currently not using this but may need this in the future

@Table("live_ops_event")
public class LiveOpsEvent {

    @Id
    @Column("id")
    private Integer id;
    @Column("event_type")
    private EventType eventType;
    @Column("name")
    private String name;
    @Column("started_at")
    private Instant startedAt;
    @Column("ended_at")
    private Instant endedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getEndedAt() {
        return endedAt;
    }

    public void setEndedAt(Instant endedAt) {
        this.endedAt = endedAt;
    }
}
