package com.closedai.closedai.session;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity // The class will be mapped to a table in a database
@Table(name = "sessions")
public class SessionEntity {
    
    // --------- Fields ---------

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @Column(name = "session_id", nullable=false, unique=true)
    private String sessionId;

    @Column(name = "created_at", updatable=false)
    private LocalDateTime createdAt;

    @Column(name = "last_used")
    private Timestamp lastUsed;

    // --------- Constructors ---------

    public SessionEntity() { }

    public SessionEntity(String sessionId) {
        this.sessionId = sessionId;
        this.createdAt = LocalDateTime.now();
    }

    // --------- Getters & Setters ---------

    public Long getId() { return this.id; }
    
    public String getSessionId() { return this.sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public Timestamp getLastUsed() { return lastUsed; }
    public void setLastUsed(Timestamp lastUsed) { this.lastUsed = lastUsed; }

    // --------- Custom Functions ---------

    @Override
    public String toString() {
        return String.format(
            "SessionEntity{ id = %s, sessionId = %s, createdAt = %s, lastUsed = %s }", 
            this.id, this.sessionId, this.createdAt, this.lastUsed
        );
    }
}
