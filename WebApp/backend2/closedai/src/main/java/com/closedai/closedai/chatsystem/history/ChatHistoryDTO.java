package com.closedai.closedai.chatsystem.history;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ChatHistoryDTO {

    @NotNull
    @Size(max = 100)
    private String sessionId;

    @NotNull
    private MessageType type;

    @Size(max = 10000)
    private String message;

    @Size(max = 100)
    private String chatSessionName;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getChatSessionName() {
        return chatSessionName;
    }

    public void setChatSessionName(String chatSessionName) {
        this.chatSessionName = chatSessionName;
    }

}
