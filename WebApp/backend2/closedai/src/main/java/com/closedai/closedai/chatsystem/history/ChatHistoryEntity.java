package com.closedai.closedai.chatsystem.history;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "chat_history")
public class ChatHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 100)
    @NotNull
    @Column(name = "session_id", nullable = false)
    private String sessionId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private MessageType type;

    @Column(name = "message", columnDefinition = "LONGTEXT")
    private String message;

    @Size(max = 100)
    @Column(name = "chat_session_name")
    private String chatSessionName;

    public ChatHistoryEntity() {
    }

    public ChatHistoryEntity(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getId() {
        return this.id;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageType getType() {
        return this.type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatSessionName() {
        return this.chatSessionName;
    }

    public void setChatSessionName(String chatSessionName) {
        this.chatSessionName = chatSessionName;
    }

    @Override
    public String toString() {
        return String.format(
                "ChatHistoryEntity{ id = %s, sessionId = %s, type = %s, message = %s, chatSessionName = %s }",
                this.id, this.sessionId, this.type, this.message, this.chatSessionName
        );
    }

}
