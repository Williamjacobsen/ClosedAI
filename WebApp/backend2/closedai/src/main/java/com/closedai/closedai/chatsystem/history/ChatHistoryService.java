package com.closedai.closedai.chatsystem.history;

import org.springframework.stereotype.Service;

@Service
public class ChatHistoryService {

    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryService(ChatHistoryRepository chatHistoryRepository) {
        this.chatHistoryRepository = chatHistoryRepository;
    }

    public void addToChatHistory(String sessionId, String prompt, String chatSessionName, MessageType type) {
        ChatHistoryEntity entity = new ChatHistoryEntity();
        entity.setSessionId(sessionId);
        entity.setType(type);
        entity.setMessage(prompt);
        entity.setChatSessionName(chatSessionName);

        chatHistoryRepository.save(entity);
    }

}
