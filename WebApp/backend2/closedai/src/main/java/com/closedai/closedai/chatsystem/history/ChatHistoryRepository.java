package com.closedai.closedai.chatsystem.history;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatHistoryRepository extends JpaRepository<ChatHistoryEntity, Long> {
    
    List<ChatHistoryEntity> findAllBySessionId(String sessionId);

}
