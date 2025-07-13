package com.closedai.closedai.getchathistory;

import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "message_history")
public class MessageHistory {

    // ── Columns ───────────────────────────────────────────────
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channel;

    /** "`key`" is a MySQL keyword, so map it explicitly */
    @Column(name = "`key`")
    private String key;

    @Column(columnDefinition = "TEXT")
    private String value;

    @Column(name = "received_at")
    private OffsetDateTime receivedAt;

    // ── Constructors ──────────────────────────────────────────
    /** No-args constructor required by JPA */
    public MessageHistory() {
    }

    /** All-args constructor (useful in tests or manual instantiation) */
    public MessageHistory(
            Long id,
            String channel,
            String key,
            String value,
            OffsetDateTime receivedAt) {
        this.id = id;
        this.channel = channel;
        this.key = key;
        this.value = value;
        this.receivedAt = receivedAt;
    }

    // ── Getters & Setters ─────────────────────────────────────
    public Long getId()               { return id; }
    public void setId(Long id)        { this.id = id; }

    public String getChannel()        { return channel; }
    public void setChannel(String channel) { this.channel = channel; }

    public String getKey()            { return key; }
    public void setKey(String key)    { this.key = key; }

    public String getValue()          { return value; }
    public void setValue(String value){ this.value = value; }

    public OffsetDateTime getReceivedAt()           { return receivedAt; }
    public void setReceivedAt(OffsetDateTime when)  { this.receivedAt = when; }

    // ── Builder-style convenience (optional) ──────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String channel;
        private String key;
        private String value;
        private OffsetDateTime receivedAt;

        public Builder id(Long id)                     { this.id = id; return this; }
        public Builder channel(String channel)         { this.channel = channel; return this; }
        public Builder key(String key)                 { this.key = key; return this; }
        public Builder value(String value)             { this.value = value; return this; }
        public Builder receivedAt(OffsetDateTime ts)   { this.receivedAt = ts; return this; }

        public MessageHistory build() {
            return new MessageHistory(id, channel, key, value, receivedAt);
        }
    }
}
