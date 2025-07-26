package com.closedai.closedai.chatsystem.history;

import java.util.List;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.closedai.closedai.session.SessionService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
public class ChatHistoryController {

    private final SessionService sessionService;
    private final ChatHistoryRepository chatHistoryRepository;

    public ChatHistoryController(SessionService sessionService, ChatHistoryRepository chatHistoryRepository) {
        this.sessionService = sessionService;
        this.chatHistoryRepository = chatHistoryRepository;
    }

    @GetMapping("/get-chat-history")
    public List<ChatHistoryEntity> test(
            @CookieValue(value = "SESSION_ID", required = true) String cookieSessionId,
            HttpServletResponse response
    ) {
        String sessionId = sessionService.getOrCreateSession(cookieSessionId, response).getSessionId();

        // TODO: SHOULD NOT RETURN sessionId & id, INSTEAD USE SOME DTO
        List<ChatHistoryEntity> messages = chatHistoryRepository.findAllBySessionId(sessionId);

        return messages;
    }

}
