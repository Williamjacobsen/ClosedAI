package com.closedai.closedai.chatsystem.history;

public record ChatHistoryDTO(
        MessageType type,
        String message,
        String chatSessionName) {

}
